package kik.lab1.aes;

public class Galois {
	
	public static byte gmul(byte a, byte b) { // Galois Field (256) Multiplication of two Bytes
	    byte p = 0;

	    for (int counter = 0; counter < 8; counter++) {
	        if ((b & 1) != 0) {
	            p ^= a;
	        }

	        boolean hi_bit_set = (a & 0x80) != 0;
	        a <<= 1;
	        if (hi_bit_set) {
	            a ^= 0x1B; /* x^8 + x^4 + x^3 + x + 1 */
	        }
	        b >>= 1;
	    }

	    return p;
	}
	
	
}
