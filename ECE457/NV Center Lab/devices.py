import time
import warnings

# AD2
from ctypes import *
from dwfconstants import *

dwf = cdll.dwf

# Microwave Signal Generator
import pyvisa as visa

# Nanopositioner
from mcculw import ul
from mcculw.enums import ULRange

# TimeTagger
import TimeTagger

# data
import numpy as np

class DigitalController:
    def __init__(self, green=0, microwave=1, awg=2, reference=3, signal=4):
        inst = c_int()
        dwf.FDwfDeviceOpen(c_int(-1), byref(inst))

        if inst.value == 0:
            print("Failed to open digital controller.")
            error = create_string_buffer(512)
            dwf.FDwfGetLastErrorMsg(error)
            print(str(error.value))

        self.inst = inst

        self.green = green
        self.microwave = microwave
        self.awg = awg
        self.reference = reference
        self.signal = signal

        self.initialize_microwave_switch()

        dwf.FDwfDeviceAutoConfigureSet(inst, c_int(0))

    def write_digital(self, channel, data):
        dwf.FDwfDigitalOutEnableSet(self.inst, c_int(channel), c_int(1))
        dwf.FDwfDigitalOutTypeSet(self.inst, c_int(channel), DwfDigitalOutTypeCustom)
        
        buffer = (c_ubyte * ((len(data) + 7) >> 3))(0)
        for index in range(len(data)):
            if data[index] != 0:
                buffer[index >> 3] |= 1 << (index & 7)

        dwf.FDwfDigitalOutDataSet(self.inst, c_int(channel), byref(buffer), c_int(len(data)))
    
    def configure_digital(self, value):
        dwf.FDwfDigitalOutConfigure(self.inst, c_int(int(value)))

    def enable_digital(self, channel):
        self.write_digital(channel, np.array([1]))
        self.configure_digital(True)

    def disable_digital(self, channel):
        self.write_digital(channel, np.array([0]))
        self.configure_digital(True)
    
    def reset_digital(self):
        dwf.FDwfDigitalOutReset(self.inst)
        self.configure_digital(True)

    def initialize_microwave_switch(self):
        dwf.FDwfAnalogIOChannelNodeSet(self.inst, c_int(0), c_int(0), c_double(True)) 
        dwf.FDwfAnalogIOChannelNodeSet(self.inst, c_int(0), c_int(1), c_double(5.0)) 
        dwf.FDwfAnalogIOChannelNodeSet(self.inst, c_int(1), c_int(0), c_double(True)) 
        dwf.FDwfAnalogIOChannelNodeSet(self.inst, c_int(1), c_int(1), c_double(-5.0)) 
        dwf.FDwfAnalogIOEnableSet(self.inst, c_int(True))

    # def set_divider(self, value):
    #     for channel in range(5):
    #         dwf.FDwfDigitalOutDividerInitSet(self.inst, c_int(channel), unsigned int v) 
    #         dwf.FDwfDigitalOutDividerSet(self.inst, c_int(channel), unsigned int v) 

    def __del__(self):
        self.reset_digital()
        dwf.FDwfDeviceCloseAll()

class MicrowaveGenerator:
    def __init__(self, address):
        rm = visa.ResourceManager()

        try:
            self.inst = rm.open_resource(address)
        except:
            warnings.warn(f'Device {address} not found.')

    def check_error(self):
        error = self.inst.query('LERR?')
        if int(error) != 0:
            print(f"Error: {error}")

    def enable_output(self):
        self.inst.write('ENBR 1')
        self.check_error()

    def disable_output(self):
        self.inst.write('ENBR 0')
        self.check_error()

    def set_amplitude(self, amplitude, units='dBm'):
        self.inst.write(f'AMPR {amplitude} {units}')
        self.check_error()

    def set_frequency(self, frequency, units='Hz'):
        self.inst.write(f'FREQ {frequency} {units}')

    def enable_modulation(self):
        self.check_error()

        self.inst.write('MODL 1')
        self.check_error()

        self.inst.write('TYPE 6')
        self.check_error()

        self.inst.write('QFNC 5')

    def disable_modulation(self):
        self.inst.write('MODL 0')
        self.check_error()

class Nanopositioner:
    def __init__(self, board_number=0, voltage_range=ULRange.UNI10VOLTS):
        self.board_number = board_number
        self.voltage_range = voltage_range

    def set_position_component(self, channel, value):
        ul.v_out(self.board_number, channel, self.voltage_range, value)

    def set_position(self, position):
        for i in range(3):
            self.set_position_component(i, position[i])

class Devices:
    def __init__(self):
        self.digital_controller = DigitalController()
        self.microwave_generator = MicrowaveGenerator(address="TCPIP0::169.254.209.144::inst0::INSTR")
        self.nanopositioner = Nanopositioner()
        self.time_tagger = TimeTagger.createTimeTagger()