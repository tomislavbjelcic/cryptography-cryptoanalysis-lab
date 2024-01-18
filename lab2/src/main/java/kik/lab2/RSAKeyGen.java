package kik.lab2;

import static kik.lab2.CryptoData.*;

import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RSAKeyGen {
	
	private static final String ALGO = "RSA";

	public static void main(String[] args) throws Exception {
		AddBcProv.add();

		int keysize = Integer.parseInt(args[0]);
		Path outpk = Path.of(args[1]);
		Path outsk = Path.of(args[2]);

		KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGO);
		keygen.initialize(keysize);
		KeyPair keypair = keygen.generateKeyPair();
		RSAPublicKey pk = (RSAPublicKey) keypair.getPublic();
		RSAPrivateKey sk = (RSAPrivateKey) keypair.getPrivate();
		
		String Nhex = Util.hexFix(pk.getModulus().toString(16));
		String ehex = Util.hexFix(pk.getPublicExponent().toString(16));
		String dhex = Util.hexFix(sk.getPrivateExponent().toString(16));
		String keysizestr = Util.hexFix(Util.intToHex(keysize));
		
		
		System.out.println("Generated RSA keypair. Writing to files...");

		Map<String, List<String>> data = new HashMap<>();
		data.put(DESCRIPTION, List.of("public key"));
		data.put(METHOD, List.of(ALGO));
		data.put(KEY_LENGTH, List.of(keysizestr));
		data.put(MODULUS, Util.splitString(Nhex, UNIT_SIZE));
		
		Map<String, List<String>> datapk = new HashMap<>(data);
		datapk.put(PUBLIC_EXPONENT, Util.splitString(ehex, UNIT_SIZE));
		
		Map<String, List<String>> datask = new HashMap<>(data);
		datask.put(PRIVATE_EXPONENT, Util.splitString(dhex, UNIT_SIZE));
		
		CryptoData.writeToFile(datapk, outpk);
		CryptoData.writeToFile(datask, outsk);
		System.out.println("Done.");
	}

}
