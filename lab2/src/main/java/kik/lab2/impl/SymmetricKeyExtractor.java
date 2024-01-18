package kik.lab2.impl;

import static kik.lab2.CryptoData.KEY_LENGTH;
import static kik.lab2.CryptoData.SECRET_KEY;

import java.security.Key;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import kik.lab2.KeyExtractor;
import kik.lab2.Util;

public class SymmetricKeyExtractor implements KeyExtractor {
	
	private int keySize;
	private String algo;
	
	public SymmetricKeyExtractor(String algo) {
		this.algo = algo;
	}
	
	@Override
	public Key extract(Map<String, List<String>> data) {
		String keysizestr = data.get(KEY_LENGTH).get(0);
		keySize = Integer.valueOf(keysizestr, 16).intValue();
		
		String keybyteshex = Util.concatenate(data.get(SECRET_KEY));
		byte[] keybytes = Util.hexToByte(keybyteshex);
		SecretKey keyspec = new SecretKeySpec(keybytes, algo);
		
		return keyspec;
	}
	
	@Override
	public int getKeySize() {
		return keySize;
	}

	@Override
	public String getAlgorithm() {
		return algo;
	}
	
	
}
