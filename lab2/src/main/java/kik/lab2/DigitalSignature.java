package kik.lab2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kik.lab2.CryptoData.*;


public class DigitalSignature {
	
	public static void main(String[] args) throws Exception {
		AddBcProv.add();
		
		Path file = Path.of(args[0]);
		Path keypath = Path.of(args[1]);
		String md = args[2];
		Path outpath = Path.of(args[3]);
		
		Map<String, List<String>> keydata = CryptoData.fromFile(keypath);
		String algo = keydata.get(METHOD).get(0);
		var keyextr = Common.KEY_EXTRACTORS.get(algo);
		if (keyextr == null) {
			System.out.println("Algorithm " + algo + " unknown.");
			return;
		}
		
		var extr = keyextr.get();
		Key key = extr.extract(keydata);
		if (!(key instanceof PrivateKey)) {
			System.out.println("Loaded key is not a private key!");
			return;
		}
		PrivateKey sk = (PrivateKey) key;
		Signature sig = Signature.getInstance(md + "with" + algo);
		sig.initSign(sk);
		
		byte[] plain = Files.readAllBytes(file);
		sig.update(plain);
		byte[] signed = sig.sign();
		
		System.out.println(Util.byteToHex(signed));
		
		Map<String, List<String>> outdata = new HashMap<>();
		outdata.put(DESCRIPTION, Util.splitString("Signature", UNIT_SIZE));
		outdata.put(FILE_NAME, List.of(file.toString()));
		outdata.put(METHOD, List.of(md, algo));
		outdata.put(KEY_LENGTH, List.of(Util.intToHex(extr.getKeySize())));
		outdata.put(SIG, Util.splitString(Util.byteToHex(signed), UNIT_SIZE));
		
		CryptoData.writeToFile(outdata, outpath);
		System.out.println("Done");
		
		
	}
	
}
