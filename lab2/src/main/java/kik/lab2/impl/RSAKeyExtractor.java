package kik.lab2.impl;

import static kik.lab2.CryptoData.KEY_LENGTH;
import static kik.lab2.CryptoData.METHOD;
import static kik.lab2.CryptoData.MODULUS;
import static kik.lab2.CryptoData.PRIVATE_EXPONENT;
import static kik.lab2.CryptoData.PUBLIC_EXPONENT;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Map;

import kik.lab2.KeyExtractor;
import kik.lab2.Util;

public class RSAKeyExtractor implements KeyExtractor {
	
	private int keySize;
	
	@Override
	public Key extract(Map<String, List<String>> data) {
		int idx = data.get(METHOD).indexOf("RSA");
		String keysizestr = data.get(KEY_LENGTH).get(idx);
		Integer ks = Integer.valueOf(keysizestr, 16);
		keySize = ks.intValue();
		
		String Nstr = Util.concatenate(data.get(MODULUS));
		BigInteger N = new BigInteger(Nstr, 16);
		
		KeyFactory kf = null;
		try {
			kf = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean priv = data.containsKey(PRIVATE_EXPONENT);
		Key key = null;
		if (priv) {
			String dstr = Util.concatenate(data.get(PRIVATE_EXPONENT));
			BigInteger d = new BigInteger(dstr, 16);
			var spec = new RSAPrivateKeySpec(N, d);
			try {
				key = kf.generatePrivate(spec);
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String estr = Util.concatenate(data.get(PUBLIC_EXPONENT));
			BigInteger e = new BigInteger(estr, 16);
			var spec = new RSAPublicKeySpec(N, e);
			try {
				key = kf.generatePublic(spec);
			} catch (InvalidKeySpecException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		return key;
	}

	@Override
	public int getKeySize() {
		return keySize;
	}

	@Override
	public String getAlgorithm() {
		return "RSA";
	}
	
}
