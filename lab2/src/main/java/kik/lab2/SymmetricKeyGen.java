package kik.lab2;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static kik.lab2.CryptoData.*;

public class SymmetricKeyGen {
	
	public static void main(String[] args) throws Exception {
		AddBcProv.add();
		
		String algo = args[0].toUpperCase();
		int keysize = Integer.parseInt(args[1]);
		Path out = Path.of(args[2]);
		
		KeyGenerator keygen = KeyGenerator.getInstance(algo);
		keygen.init(keysize);
		SecretKey key = keygen.generateKey();
		byte[] keyenc = key.getEncoded();
		
		System.out.println("Generated " + key.getAlgorithm() 
			+ " key of format " + key.getFormat() + ", size " + keyenc.length*8);
		
		String keyhex = Util.byteToHex(keyenc);
		System.out.println(keyhex);
		
		Map<String, List<String>> data = new HashMap<>();
		data.put(DESCRIPTION, List.of("secret key"));
		data.put(METHOD, List.of(key.getAlgorithm()));
		data.put(KEY_LENGTH, List.of(Util.intToHex(keysize)));
		data.put(SECRET_KEY, List.of(keyhex));
		
		System.out.println("Writing to file...");
		CryptoData.writeToFile(data, out);
		System.out.println("Done.");
		
		
	}
	
}
