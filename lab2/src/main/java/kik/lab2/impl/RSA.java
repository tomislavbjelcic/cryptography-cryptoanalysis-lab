package kik.lab2.impl;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import kik.lab2.EncryptionAlgorithm;

public class RSA implements EncryptionAlgorithm {

	@Override
	public boolean isSymmetricBlockCipher() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAlgorithmName() {
		return "RSA";
	}

	@Override
	public String getTransformation() {
		return "RSA/NONE/OAEPPadding";
	}

	@Override
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return new OAEPParameterSpec("SHA256", 
				 "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
	}

	@Override
	public void extractData(Map<String, List<String>> data) {
		//prazno
	}
	
	
	
}
