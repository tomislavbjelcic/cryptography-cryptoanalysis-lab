package kik.lab1.aes;

import static kik.lab1.aes.AESAlgorithm.BLOCK_SIZE_BYTES;

public class AESECB {
	
	private AESKeySchedule sched;
	
	public AESECB(byte[] keyBytes) {
		sched = new AESKeySchedule(keyBytes.length * 8);
		sched.generate(keyBytes);
	}
	
	public byte[] encrypt(byte[] plaintext) {
		byte[] paddedPlaintext = SimplePadding.pad(plaintext);
		byte[] ciphertext = new byte[paddedPlaintext.length];
		byte[] bufpl = new byte[BLOCK_SIZE_BYTES];
		byte[] bufciph = new byte[BLOCK_SIZE_BYTES];
		
		int blockCount = ciphertext.length / BLOCK_SIZE_BYTES;
		for (int i=0; i<blockCount; i++) {
			System.arraycopy(paddedPlaintext, i*BLOCK_SIZE_BYTES, bufpl, 0, BLOCK_SIZE_BYTES);
			AESAlgorithm.encrypt(bufpl, bufciph, sched);
			System.arraycopy(bufciph, 0, ciphertext, i*BLOCK_SIZE_BYTES, BLOCK_SIZE_BYTES);
		}
		
		return ciphertext;
	}
	
	public byte[] decrypt(byte[] ciphertext) {
		if (ciphertext.length % BLOCK_SIZE_BYTES != 0)
			throw new IllegalArgumentException("Ciphertext length " + 
					ciphertext.length + " not divisible by " + BLOCK_SIZE_BYTES);
		byte[] bufpl = new byte[BLOCK_SIZE_BYTES];
		byte[] bufciph = new byte[BLOCK_SIZE_BYTES];
		byte[] plaintextpadded = new byte[ciphertext.length];
		
		int blockCount = ciphertext.length / BLOCK_SIZE_BYTES;
		for (int i=0; i<blockCount; i++) {
			System.arraycopy(ciphertext, i*BLOCK_SIZE_BYTES, bufciph, 0, BLOCK_SIZE_BYTES);
			AESAlgorithm.decrypt(bufpl, bufciph, sched);
			System.arraycopy(bufpl, 0, plaintextpadded, i*BLOCK_SIZE_BYTES, BLOCK_SIZE_BYTES);
		}
		
		return SimplePadding.unpad(plaintextpadded);
	}
	
}
