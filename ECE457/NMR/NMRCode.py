''' Instructions:
    Fill in the parts of code that have question marks.
    Test your code first with the writeSquareArb function.
    Hints/directions are given in the comments, if you need more help feel free to Slack me
'''

import numpy as np
import time
?????  # import pyvisa here


class FuncGen(object):
    def __init__(self, devID):
        self.rm   = ???????????    # create a resource manager
        self.inst = ???????????    # open your resource manager

    def syncChannels(self):
        self.inst.write("FUNC:ARB:SYNC")

    def writeArbV3(self, dataPts, chanNum, phase=0, amp=0.5, offs=0):
        self.inst.write('SOUR%i:DATA:VOL:CLE' % chanNum)
        self.inst.write("SOUR%i:FUNC ARB" % chanNum)
        header = "SOUR%i:DATA:ARB:DAC PULSE%i, " % (chanNum, chanNum)
        ptsDACPython = [int(i * 32767) for i in dataPts]
        ptsDAC = np.array(ptsDACPython, dtype='int16')
        self.inst.write('FORMat:BORDer SWAP')
        self.inst.write_binary_values(header, ptsDAC, datatype='i')
        self.inst.write("SOUR%i:FUNC:ARB PULSE%i" % (chanNum, chanNum))
        # write code here which sets output voltage given a channel number and amplitude
        # write code here which sets the DC offset voltage given a channel number and offset
        self.inst.write("SOUR%i:BURS:MODE TRIG" % chanNum)
        self.inst.write("SOUR%i:BURS:NCYC 1" % chanNum)
        self.inst.write("SOUR%i:BURS:PHAS %f DEG" % (chanNum, phase))
        self.inst.write("TRIG%i:SOUR EXT" % chanNum)
        self.inst.write("SOUR%i:FUNC:ARB:SRAT 1e7" % chanNum)
        self.inst.write('SOUR%i:FUNC:ARB:FILT STEP' % chanNum)
        self.inst.write("SOUR%i:BURS:STAT ON" % chanNum)

        self.syncChannels()
        # write code here which turns the output on given a channel number
        time.sleep(0.1)
        print("AWG ON now!")

    def setDClevel(self, chanNum, DCoffset):
        self.inst.write("SOUR%i:APPLy:DC DEF, DEF, %f V" % (chanNum, DCoffset))
        # write code here which turns the output on given a channel number
        time.sleep(0.1)

    def writeSquareArb(self, chanNum, pulseWidth, phase=0, amp=0.50, offs=0):
        pulseWidth = pulseWidth / 2
        amp_I = amp * 2
        amp_Q = amp * 2
        nBins = int(np.around(?????))  # convert your pulsewidth to a number of bins here
        waveform = np.zeros(nBins + 50)
        for i in range(nBins):
            waveform[i] = 1
        waveform = np.around(waveform, decimals=6)
        if chanNum == 1:
            self.writeArbV3(waveform, 1, phase, amp_I, offs)
        else:
            self.writeArbV3(waveform, 2, phase, amp_Q, offs)

    def writeHahnArb(self, piBy2Width, piWidth, tau, pulseangle=0, phase=0, amp=0.50, offs=0):
        piBy2Width = piBy2Width / 2
        piWidth = piWidth / 2
        tau = tau / 2
        amp_I = amp * 2
        amp_Q = amp * 2
        nBins = int(np.around(???????))           # find total number of bins you need for your pulse sequence
        piWidthBins = int(np.around(???????))     # convert your pi pulse width to a number of bins here
        piBy2WidthBins = int(np.around(???????))  # convert your pi pulse width to a number of bins here
        tau1Bins = int(np.around(??????))         # convert your tau width to a number of bins here
        waveform_Isig = np.zeros(nBins + 50)  # AWG requires a waveform that is padded with zeros
        waveform_Qsig = np.zeros(nBins + 50)
        for i in range(piBy2WidthBins):
            waveform_Isig[i] = 1
        for i in range(piWidthBins):
            waveform_Isig[i + piBy2WidthBins + tau1Bins] = 1
        waveform_Isig = np.around(waveform_Isig, decimals=6)
        waveform_Qsig = np.around(waveform_Qsig, decimals=6)
        # can disregard this bottom code, just scales the amplitude
        if (np.min(waveform_Isig) < 0) and (np.max(waveform_Isig) > 0):
            scaled_amp_I = amp_I * (np.max(waveform_Isig) - np.min(waveform_Isig))
            self.writeArbV3(waveform_Isig, 1, phase, scaled_amp_I, offs)
        else:
            self.writeArbV3(waveform_Isig, 1, phase, amp_I, offs)
        if (np.min(waveform_Qsig) < 0) and (np.max(waveform_Qsig) > 0):
            scaled_amp_Q = amp_Q * (np.max(waveform_Qsig) - np.min(waveform_Qsig))
            self.writeArbV3(waveform_Qsig, 2, phase, scaled_amp_Q, offs)
        else:
            self.writeArbV3(waveform_Qsig, 2, phase, amp_Q, offs)

    def writeCPMG(self, numOfRefPulses, piBy2Width, piWidth, tau, pulseangle=0, phase=0, amp=0.50, offs=0):
        piBy2Width = piBy2Width / 2
        piWidth = piWidth / 2
        tau = tau / 2
        amp_I = amp * 2
        amp_Q = amp * 2
        nBins = int(np.around(?????????))
        piWidthBins = int(np.around(?????????))
        piBy2WidthBins = int(np.around((????????????))
        tauBins = int(np.around(??????????))
        waveform_Isig = np.zeros(nBins + 50)  # 50 to just pad with zeros, AWG works better if pulse has this
        waveform_Qsig = np.zeros(nBins + 50)
        for i in range(piBy2WidthBins):
            waveform_Isig[i] = 1
        for ii in range(numOfRefPulses):
            for i in range(piWidthBins):
                waveform_Isig[i + piBy2WidthBins + tauBins + ii * (tauBins + piWidthBins + tauBins)] = np.cos(
                    np.pi * (pulseangle) / 180.0)
                waveform_Qsig[i + piBy2WidthBins + tauBins + ii * (tauBins + piWidthBins + tauBins)] = np.sin(
                    np.pi * (pulseangle) / 180.0)

        waveform_Isig = np.around(waveform_Isig, decimals=6)
        waveform_Qsig = np.around(waveform_Qsig, decimals=6)
        if (np.min(waveform_Isig) < 0) and (np.max(waveform_Isig) > 0):
            scaled_amp_I = amp_I * (np.max(waveform_Isig) - np.min(waveform_Isig))
            self.writeArbV3(waveform_Isig, 1, phase, scaled_amp_I, offs)
        else:
            self.writeArbV3(waveform_Isig, 1, phase, amp_I, offs)

        if (np.min(waveform_Qsig) < 0) and (np.max(waveform_Qsig) > 0):
            scaled_amp_Q = amp_Q * (np.max(waveform_Qsig) - np.min(waveform_Qsig))
            self.writeArbV3(waveform_Qsig, 2, phase, scaled_amp_Q, offs)
        else:
            self.writeArbV3(waveform_Qsig, 2, phase, amp_Q, offs)

     '''write your XY sequence here. It will look almost identical to CPMG function but pi pulses will alternate being 
        about the x-axis and y-axis. Assume your pi/2 pulse is about the x-axis, then a pi pulse about the y-axis 
        will be shifted 90 degrees from this pulse. Think about how this will change your I and Q waveforms. 
       ''''' 

'''Run your code here'''
if __name__ == '__main__':
    arbObj = FuncGen(devID='????????')  # insert USB address string here

    arbObj.setDClevel(chanNum=1, DCoffset=0)
    arbObj.setDClevel(chanNum=2, DCoffset=0)

    # test your function! careful, do not exceed an amplitude of 2.25V
    arbObj.writeSquareArb(chanNum=2, pulseWidth=3.72e-6, amp=2, phase=0)