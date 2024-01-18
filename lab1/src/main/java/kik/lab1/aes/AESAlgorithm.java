package kik.lab1.aes;

public class AESAlgorithm {
	
	
	public static final int BLOCK_SIZE_BYTES = 16;
	
	public static byte[] encrypt(byte[] plaintext, byte[] keyBytes) {
		byte[] ciphertext = new byte[BLOCK_SIZE_BYTES];
		encrypt(plaintext, ciphertext, keyBytes);
		return ciphertext;
	}
	
	public static void encrypt(byte[] plaintext, byte[] ciphertext, byte[] keyBytes) {
		int keyBitCount = keyBytes.length * 8;
		AESKeySchedule sched = new AESKeySchedule(keyBitCount);
		sched.generate(keyBytes);
		encrypt(plaintext, ciphertext, sched);
	}
	
	public static void encrypt(byte[] plaintext, byte[] ciphertext, AESKeySchedule sched) {
		if (plaintext.length != BLOCK_SIZE_BYTES)
			throw new IllegalArgumentException("Plaintext illegal block size: " + plaintext.length);
		if (ciphertext.length != BLOCK_SIZE_BYTES)
			throw new IllegalArgumentException("Ciphertext array illegal size: " + ciphertext.length);
		byte[][] roundKeys = sched.getAsRoundKeys();
		
		for (int i=0; i<BLOCK_SIZE_BYTES; i++) {
			ciphertext[i] = plaintext[i];
		}
		State state = new State(ciphertext);
		state.addRoundKey(roundKeys[0]);
		
		for (int i=1; i<roundKeys.length-1; i++) {
			state.subBytes();
			state.shiftRows();
			state.mixColumns();
			state.addRoundKey(roundKeys[i]);
		}
		
		state.subBytes();
		state.shiftRows();
		state.addRoundKey(roundKeys[roundKeys.length-1]);
	}
	
	public static byte[] decrypt(byte[] ciphertext, byte[] keyBytes) {
		byte[] plaintext = new byte[BLOCK_SIZE_BYTES];
		decrypt(plaintext, ciphertext, keyBytes);
		return plaintext;
	}
	
	public static void decrypt(byte[] plaintext, byte[] ciphertext, byte[] keyBytes) {
		int keyBitCount = keyBytes.length * 8;
		AESKeySchedule sched = new AESKeySchedule(keyBitCount);
		sched.generate(keyBytes);
		decrypt(plaintext, ciphertext, sched);
	}
	
	public static void decrypt(byte[] plaintext, byte[] ciphertext, AESKeySchedule sched) {
		if (ciphertext.length != BLOCK_SIZE_BYTES)
			throw new IllegalArgumentException("Ciphertext illegal block size: " + ciphertext.length);
		if (plaintext.length < BLOCK_SIZE_BYTES)
			throw new IllegalArgumentException("Plaintext array illegal size: " + plaintext.length);
		byte[][] roundKeys = sched.getAsRoundKeys();
		
		for (int i=0; i<BLOCK_SIZE_BYTES; i++) {
			plaintext[i] = ciphertext[i];
		}
		int L = roundKeys.length;
		State state = new State(plaintext);
		state.addRoundKey(roundKeys[L-1]);
		state.invShiftRows();
		state.invSubBytes();
		
		for (int i=L-2; i>=1; i--) {
			state.addRoundKey(roundKeys[i]);
			state.invMixColumns();
			state.invShiftRows();
			state.invSubBytes();
		}
		
		state.addRoundKey(roundKeys[0]);
	}

}
