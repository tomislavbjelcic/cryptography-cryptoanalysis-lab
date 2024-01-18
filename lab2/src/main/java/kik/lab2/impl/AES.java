package kik.lab2.impl;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import kik.lab2.EncryptionMode;

public class AES extends SymmetricBlockCipher {

	private static final String ALGO_NAME = "AES";
	private static final int BLOCK_SIZE = 128;
	
	@Override
	public String getAlgorithmName() {
		return ALGO_NAME;
	}

	@Override
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		
		if (!ecmode.requiresIv())
			return null;
		if (iv.length != BLOCK_SIZE/8)
			throw new RuntimeException("Wrong IV");
		return ecmode==EncryptionMode.GCM ? new GCMParameterSpec(128, iv)
				: new IvParameterSpec(iv);
	}

	@Override
	public int getBlockSize() {
		return BLOCK_SIZE;
	}

}
