package kik.lab2;

import java.util.Map;
import java.util.function.Supplier;

import kik.lab2.impl.AES;
import kik.lab2.impl.DESede;
import kik.lab2.impl.RSA;
import kik.lab2.impl.RSAKeyExtractor;
import kik.lab2.impl.SymmetricBlockCipher;
import kik.lab2.impl.SymmetricKeyExtractor;

public class Common {
	
	public static final Map<String, Supplier<KeyExtractor>> KEY_EXTRACTORS 
		= Map.of("RSA", RSAKeyExtractor::new);
	
	public static final Map<String, Supplier<KeyExtractor>> SYMMETRIC_KEY_EXTRACTORS
		= Map.of("AES", () -> new SymmetricKeyExtractor("AES"),
				"DESede", () -> new SymmetricKeyExtractor("DESede"));
	
	public static final Map<String, Supplier<SymmetricBlockCipher>> SYMMETRIC_BLOCK_CIPHERS
		= Map.of("AES", AES::new,
				"DESede", DESede::new);
	
	public static final Map<String, Supplier<EncryptionAlgorithm>> ASYMMETRIC_CIPHERS
		= Map.of("RSA", RSA::new);
}
