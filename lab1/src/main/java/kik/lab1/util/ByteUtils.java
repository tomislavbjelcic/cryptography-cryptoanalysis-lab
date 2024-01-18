package kik.lab1.util;

import java.util.Objects;

public class ByteUtils {
	
	
	private static final String HEX_REGEX = "(\\p{XDigit})*";
	
	private static final int INT_MASK = 0x00_00_00_ff;
	
	private static final int LEFT_MASK = 0x00_00_00_f0;
	private static final int RIGHT_MASK = 0x00_00_00_0f;
	
	private static final char[] DIGITS_CHARS = new char[] {
			'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	
	private ByteUtils() {}
	
	public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
		return ((b1<<24) | 
				(b2<<16 & 0x00ff0000) | 
				(b3<<8 & 0x0000ff00) | 
				(b4 & 0x000000ff));
	}
	
	public static String byteToHex(byte[] bytes) {
		int len = bytes.length;
		StringBuilder sb = new StringBuilder(len * 2);
		
		for (byte b : bytes) {
			int i = ((int) b) & INT_MASK;
			int leftDigit = (i & LEFT_MASK) >>> 4;
			int rightDigit = (i & RIGHT_MASK);
			sb.append(DIGITS_CHARS[leftDigit]).append(DIGITS_CHARS[rightDigit]);
		}
		
		return sb.toString();
	}
	
	public static String byteToHex(byte b) {
		int i = ((int) b) & INT_MASK;
		int leftDigit = (i & LEFT_MASK) >>> 4;
		int rightDigit = (i & RIGHT_MASK);
		return ("" + DIGITS_CHARS[leftDigit]) + DIGITS_CHARS[rightDigit];
	}
	
	public static String intToHex(int n) {
		StringBuilder sb = new StringBuilder(8);
		for (int i=7; i>=0; i--) {
			int idx = (n >>> (i*4)) & 0xf;
			sb.append(DIGITS_CHARS[idx]);
		}
		return sb.toString();
	}

	
	private static void checkHexString(String hex) {
		int len = hex.length();
		if (len%2 != 0)
			throw new IllegalArgumentException("Hex " + hex + " has odd number of hex digits.");
		
		boolean match = hex.matches(HEX_REGEX);
		if (!match)
			throw new IllegalArgumentException("Invalid hex: " + hex);
	}
	
	/**
	 * Pretvara hex zapis {@code hex} u niz bajtova ako je hex zapis ispravan.<br>
	 * Ispravan hex zapis se sastoji isključivo od znakova koje predstavljaju heksadekadske znamenke 
	 * i duljina zapisa je paran broj.
	 * 
	 * @param hex
	 * @return niz bajtova hex zapisa {@code hex}.
	 * @throws NullPointerException ako je predani hex zapis {@code null}.
	 * @throws IllegalArgumentException ako je predan neispravan hex zapis.
	 */
	public static byte[] hexToByte(String hex) {
		Objects.requireNonNull(hex);
		checkHexString(hex);
		
		int count = hex.length() / 2;
		byte[] output = new byte[count];
		for (int i=0; i<count; i++) {
			int off = i*2;
			char firstDigitChar = hex.charAt(off);
			char secondDigitChar = hex.charAt(off+1);
			
			int firstDigit = hexDigitValue(firstDigitChar);
			int secondDigit = hexDigitValue(secondDigitChar);
			
			int val = firstDigit * 16 + secondDigit;
			output[i] = (byte) val;
		}
		return output;
	}
	
	private static int hexDigitValue(char digit) {
		boolean isDigit = Character.isDigit(digit);
		if (isDigit)
			return (int) (digit - '0');
		char lowercase = Character.toLowerCase(digit);
		return (int) (lowercase - 'a' + 10);
	}
	
	public static void arrayXor(byte[] a, byte[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException("Array size mismatch.");
		for (int i=0; i<a.length; i++) {
			a[i] ^= b[i];
		}
	}


	
}
