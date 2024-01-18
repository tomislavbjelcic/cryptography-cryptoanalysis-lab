package kik.lab2;

import static kik.lab2.CryptoData.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import kik.lab2.impl.SymmetricBlockCipher;

public class EnvelopeDecrypt {
	
	public static void main(String[] args) throws Exception {
		AddBcProv.add();
		
		Path envpath = Path.of(args[0]);
		Path privkeypath = Path.of(args[1]);
		
		Map<String, List<String>> envdata = CryptoData.fromFile(envpath);
		Map<String, List<String>> pkdata = CryptoData.fromFile(privkeypath);
		
		List<String> algos = envdata.get(METHOD);
		String asymalg = algos.get(1);
		
		EncryptionAlgorithm asymenc = Common.ASYMMETRIC_CIPHERS.get(asymalg).get();
		KeyExtractor asymextr = Common.KEY_EXTRACTORS.get(asymalg).get();
		Key skk = asymextr.extract(pkdata);
		if (!(skk instanceof PrivateKey)) {
			System.out.println("Loaded key is not a private key!");
			return;
		}
		PrivateKey sk = (PrivateKey) skk;
		
		Cipher asymcipher = Cipher.getInstance(asymenc.getTransformation());
		asymcipher.init(Cipher.DECRYPT_MODE, sk, asymenc.getAlgorithmParameterSpec());
		String enckeyhex = Util.concatenate(envdata.get(ENVELOPE_CRYPT_KEY));
		byte[] enckey = Util.hexToByte(enckeyhex);
		byte[] keybytes = asymcipher.doFinal(enckey);
		
		Map<String, List<String>> symencdata = new HashMap<>();
		String symalg = algos.get(0);
		String[] symalgsplitted = symalg.split("/");
		String symkeysizehex = envdata.get(KEY_LENGTH).get(1);
		symencdata.put(SECRET_KEY, List.of(enckeyhex));
		symencdata.put(METHOD, List.of(symalg));
		symencdata.put(KEY_LENGTH, List.of(symkeysizehex));
		
		EncryptionMode encmode = EncryptionMode.valueOf(symalgsplitted[1]);
		if (encmode.requiresIv()) {
			symencdata.put(IV, envdata.get(IV));
		}
		
		
		SymmetricBlockCipher sbc = Common.SYMMETRIC_BLOCK_CIPHERS.get(symalgsplitted[0]).get();
		sbc.extractData(symencdata);
		
		Cipher symciph = Cipher.getInstance(sbc.getTransformation());
		SecretKey symkey = new SecretKeySpec(keybytes, symalgsplitted[0]);
		symciph.init(Cipher.DECRYPT_MODE, symkey, sbc.getAlgorithmParameterSpec());
		
		byte[] ciphertext = Base64.getDecoder().decode(Util.concatenate(envdata.get(ENVELOPE_DATA)));
		byte[] plaintext = symciph.doFinal(ciphertext);
		
		String decryptedstr = Base64.getEncoder().encodeToString(plaintext);
		String decryptedtxt = new String(plaintext, StandardCharsets.UTF_8);
		
		System.out.println(decryptedstr);
		System.out.println();
		System.out.println(decryptedtxt);
		
		
	}
	
}
