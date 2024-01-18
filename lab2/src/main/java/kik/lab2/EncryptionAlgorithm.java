package kik.lab2;

import java.security.spec.AlgorithmParameterSpec;
import java.util.List;
import java.util.Map;

public interface EncryptionAlgorithm {
	
	boolean isSymmetricBlockCipher();
	
	String getAlgorithmName();
	String getTransformation();
	AlgorithmParameterSpec getAlgorithmParameterSpec();
	
	void extractData(Map<String, List<String>> data);
	
}
