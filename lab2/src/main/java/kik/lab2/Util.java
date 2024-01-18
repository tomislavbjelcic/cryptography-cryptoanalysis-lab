package kik.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Util {
	
	
	private static final String HEX_REGEX = "(\\p{XDigit})*";
	
	private static final int INT_MASK = 0x00_00_00_ff;
	
	private static final int LEFT_MASK = 0x00_00_00_f0;
	private static final int RIGHT_MASK = 0x00_00_00_0f;
	
	private static final char[] DIGITS_CHARS = new char[] {
			'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	
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
	
	public static String concatenate(List<String> list) {
		if (list.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder(list.get(0).length() * list.size());
		for (String s : list)
			sb.append(s);
		return sb.toString();
	}
	
	public static List<String> splitString(String str, int unitSize) {
		int strlen = str.length();
		List<String> list = new ArrayList<>();
		for (int off=0; off<strlen ; off+=unitSize) {
			int to = Math.min(off+unitSize, strlen);
			String substring = str.substring(off, to);
			list.add(substring);
		}
		return list;
	}
	
	public static String intToHex(int num) {
		StringBuilder sb = new StringBuilder();
		boolean reachedNonZero = false;
		for (int i=0; i<4; i++) {
			int b = (num >>> (8*(3-i))) & 0xff;
			if (b != 0 || i==3)
				reachedNonZero = true;
			
			if (reachedNonZero)
				sb.append(byteToHex((byte) b));
		}
		return sb.toString();
	}
	
	public static String byteToHex(byte b) {
		int i = ((int) b) & INT_MASK;
		int leftDigit = (i & LEFT_MASK) >>> 4;
		int rightDigit = (i & RIGHT_MASK);
		return ("" + DIGITS_CHARS[leftDigit]) + DIGITS_CHARS[rightDigit];
	}
	
	public static String hexFix(String hex) {
		int len = hex.length();
		return len%2==0 ? hex : "0"+hex;
	}
	
	public static void main(String[] args) {
		String h = "15f";
		System.out.println(hexFix(h));
	}
	
}
