package kik.lab1.aes;

import java.util.Arrays;

import kik.lab1.util.ByteUtils;

public class SimplePadding {
	
	
	private static final byte FIRST_PAD = (byte) 0x80;
	private static final byte REST_PAD = (byte) 0;
	private static final int SIZE = 16;
	
	private static int numberOfPadBytes(byte[] arr) {
		return SIZE - arr.length % SIZE;
	}
	
	public static byte[] pad(byte[] arr) {
		int n = numberOfPadBytes(arr);
		int total = arr.length + n;
		byte[] padded = Arrays.copyOf(arr, total);
		int firstIdx = arr.length;
		padded[firstIdx] = FIRST_PAD;
		for (int i=1; i<n; i++) {
			padded[firstIdx+i] = REST_PAD;
		}
		return padded;
	}
	
	public static byte[] unpad(byte[] arr) {
		if (arr.length % SIZE != 0)
			throw new IllegalArgumentException("Size " + arr.length + " not divisible by " + SIZE);
		int i = arr.length-1;
		for (; i>=0; i--) {
			if (arr[i] != REST_PAD && arr[i] != FIRST_PAD)
				throw new IllegalArgumentException("Bad padding for " + ByteUtils.byteToHex(arr));
			if (arr[i] == REST_PAD)
				continue;
			if (arr[i] == FIRST_PAD)
				break;
			
		}
		if (i == -1)
			throw new IllegalArgumentException("Bad padding."); //sve nule ili prazno
		byte[] unpadded = new byte[i];
		System.arraycopy(arr, 0, unpadded, 0, i);
		return unpadded;
	}
	
	
}
