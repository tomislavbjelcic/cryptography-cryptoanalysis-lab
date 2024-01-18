package kik.lab1;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import kik.lab1.aes.AESAlgorithm;
import kik.lab1.util.ByteUtils;

public class Test {
	
	public static void main(String[] args) {
		testRandomSingleBlock();
	}
	
	public static void testRandomSingleBlock() {
		Random rng = new Random();
		String ALGO = "AES";
		String ALGO_MODE = "AES/ECB/NoPadding";
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ALGO_MODE);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] rb = new byte[AESAlgorithm.BLOCK_SIZE_BYTES];
		rng.nextBytes(rb);
		byte[] key = new byte[AESAlgorithm.BLOCK_SIZE_BYTES];
		rng.nextBytes(key);
		Key k = new SecretKeySpec(key, ALGO);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, k);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] ciphertextJavaCipher = null;
		try {
			ciphertextJavaCipher = cipher.doFinal(rb);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] ciphertext = AESAlgorithm.encrypt(rb, key);
		
		System.out.println(ByteUtils.byteToHex(ciphertextJavaCipher));
		System.out.println(ByteUtils.byteToHex(ciphertext));
		System.out.println(Arrays.equals(ciphertext, ciphertextJavaCipher));
	}
	
}
