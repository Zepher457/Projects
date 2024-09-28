import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // ascii

    // apply move-to-front encoding, reading from stdin and writing to stdout
    public static void encode() {
        // alphabet array
        char[] ascii = new char[R];

        // instantiates alphabet array
        for (char c = 0; c < R; c++) {
            ascii[c] = c;
        }

        // read input and iterate through alphabet
        while (!BinaryStdIn.isEmpty()) {
            char a = BinaryStdIn.readChar(8);

            for (char c = 0; c < R; c++) {
                // if you find matching character, move to front and shift all
                // previous characters over by one
                if (a == ascii[c]) {
                    shift(ascii, c);
                    BinaryStdOut.write(c);
                    break;
                }
            }
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from stdin and writing to stdout
    public static void decode() {
        // alphabet array
        char[] ascii = new char[R];

        // instantiates alphabet array
        for (char c = 0; c < R; c++) {
            ascii[c] = c;
        }

        // read input and iterate through alphabet
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar(8);
            BinaryStdOut.write(ascii[i]);
            shift(ascii, i);

        }
        BinaryStdOut.close();
    }
    // helper method to move i to front
    private static void shift(char[] ascii, char i) {
        char tmp = ascii[i];
        for (int j = i; j > 0; j--) {
            ascii[j] = ascii[j - 1];
        }
        ascii[0] = tmp;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();

        else if (args[0].equals("+"))
            decode();
    }

}
