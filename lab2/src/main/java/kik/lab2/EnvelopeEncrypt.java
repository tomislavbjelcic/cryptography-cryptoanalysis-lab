package kik.lab2;

import static kik.lab2.CryptoData.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import kik.lab2.impl.SymmetricBlockCipher;

public class EnvelopeEncrypt {
	
	private static final SecureRandom RNG = new SecureRandom();
	
	public static void main(String[] args) throws Exception {
		AddBcProv.add();
		
		Path file = Path.of(args[0]);
		Path pubkeypath = Path.of(args[1]);
		String algomode = args[2];
		int keysize = Integer.parseInt(args[3]);
		Path outpath = Path.of(args[4]);
		
		Map<String, List<String>> data = new HashMap<>();
		data.put(METHOD, List.of(algomode));
		String[] splitted = algomode.split("/");
		String algo = splitted[0];
		EncryptionMode mode = EncryptionMode.valueOf(splitted[1]);
		SymmetricBlockCipher symc = Common.SYMMETRIC_BLOCK_CIPHERS.get(algo).get();
		
		KeyGenerator keygen = KeyGenerator.getInstance(algo);
		keygen.init(keysize);
		
		SecretKey key = keygen.generateKey();
		byte[] keybytes = key.getEncoded();
		String ivhex = null;
		if (mode.requiresIv()) {
			byte[] iv = new byte[symc.getBlockSize()/8];
			RNG.nextBytes(iv);
			ivhex = Util.byteToHex(iv);
			data.put(IV, List.of(ivhex));
		}
		
		symc.extractData(data);
		
		Cipher ciph = Cipher.getInstance(symc.getTransformation());
		ciph.init(Cipher.ENCRYPT_MODE, key, symc.getAlgorithmParameterSpec());
		
		byte[] filebytes = Files.readAllBytes(file);
		byte[] ciphertext = ciph.doFinal(filebytes);
		
		var asymdata = CryptoData.fromFile(pubkeypath);
		String asymalg = asymdata.get(METHOD).get(0);
		var keyextr = Common.KEY_EXTRACTORS.get(asymalg);
		if (keyextr == null) {
			System.out.println("Algorithm " + asymalg + " unknown.");
			return;
		}
		
		var extr = keyextr.get();
		Key keyy = extr.extract(asymdata);
		if (!(keyy instanceof PublicKey)) {
			System.out.println("Loaded key is not a public key!");
			return;
		}
		PublicKey pk = (PublicKey) keyy;
		
		var asymciph = Common.ASYMMETRIC_CIPHERS.get(asymalg).get();
		Cipher asymcipher = Cipher.getInstance(asymciph.getTransformation());
		asymcipher.init(Cipher.ENCRYPT_MODE, pk, asymciph.getAlgorithmParameterSpec());
		
		byte[] encryptedkeybytes = asymcipher.doFinal(keybytes);
		
		Map<String, List<String>> cryptodata = new HashMap<>();
		
		cryptodata.put(DESCRIPTION, List.of("Envelope"));
		cryptodata.put(FILE_NAME, List.of(file.toString()));
		cryptodata.put(METHOD, List.of(algomode, asymalg));
		
		String keysizesymlenstr = Util.intToHex(keysize);
		String asymkeysizelenstr = Util.intToHex(extr.getKeySize());
		cryptodata.put(KEY_LENGTH, List.of(keysizesymlenstr, asymkeysizelenstr));
		
		String envdata = Base64.getEncoder().encodeToString(ciphertext);
		cryptodata.put(ENVELOPE_DATA, Util.splitString(envdata, UNIT_SIZE));
		
		String cryptkey = Util.byteToHex(encryptedkeybytes);
		cryptodata.put(ENVELOPE_CRYPT_KEY, Util.splitString(cryptkey, UNIT_SIZE));
		
		if (ivhex != null) {
			cryptodata.put(IV, Util.splitString(ivhex, UNIT_SIZE));
		}
		
		CryptoData.writeToFile(cryptodata, outpath);
		System.out.println("Done");
		
	}
	
}
