package kik.lab2.impl;

import java.util.List;
import java.util.Map;

import kik.lab2.EncryptionAlgorithm;
import kik.lab2.EncryptionMode;
import kik.lab2.Util;

import static kik.lab2.CryptoData.*;

public abstract class SymmetricBlockCipher implements EncryptionAlgorithm {
	
	protected EncryptionMode ecmode = EncryptionMode.ECB;
	protected byte[] iv;
	
	@Override
	public boolean isSymmetricBlockCipher() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void extractData(Map<String, List<String>> data) {
		// TODO Auto-generated method stub
		String algoname = this.getAlgorithmName();
		List<String> methods = data.get(METHOD);
		for (String m : methods) {
			if (!m.matches(algoname + ".*"))
				continue;
			String[] splitted = m.split("/");
			if (splitted.length == 2)
				this.ecmode = EncryptionMode.valueOf(splitted[1]);
			break;
		}
		
		if (!ecmode.requiresIv())
			return;
		
		String ivhex = Util.concatenate(data.get(IV));
		this.iv = Util.hexToByte(ivhex);
	}
	
	protected String getDefaultPadding() {
		return ecmode.requiresPadding() ? "PKCS5Padding" : "NoPadding";
	}
	
	public abstract int getBlockSize();


	@Override
	public String getTransformation() {
		String transformation = this.getAlgorithmName() + '/' + ecmode.toString() + '/'
				+ this.getDefaultPadding();
		return transformation;
	}
	

}
