package kik.lab1;

import java.security.SecureRandom;

import kik.lab1.util.ByteUtils;

public class GenRandomBytes {
	private static final SecureRandom RNG = new SecureRandom();
	
	public static void main(String[] args) {
		
		int size = 16;
		if (args.length > 0) {
			try {
				size = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				System.out.println("NumberFormatException.");
				return;
			}
		}
		
		if (size < 0) {
			System.out.println("Unesen negativan broj.");
			return;
		}
		
		byte[] arr = new byte[size];
		RNG.nextBytes(arr);
		System.out.println(ByteUtils.byteToHex(arr));
	}
	
}
