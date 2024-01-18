package kik.lab2;

import static kik.lab2.CryptoData.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.PublicKey;
import java.security.Signature;
import java.util.List;
import java.util.Map;


public class CheckSig {
	
	public static void main(String[] args) throws Exception {
		AddBcProv.add();
		
		//Path filepath = Path.of(args[0]);
		Path sigpath = Path.of(args[0]);
		Path pubkeypath = Path.of(args[1]);
		
		Map<String, List<String>> pubkeydata = CryptoData.fromFile(pubkeypath);
		Map<String, List<String>> sigdata = CryptoData.fromFile(sigpath);
		Path filepath = Path.of(sigdata.get(FILE_NAME).get(0));
		List<String> methods = sigdata.get(METHOD);
		if (methods.size() != 2) {
			System.out.println("Signature doesn't specify exactly 2 methods.");
			return;
		}
		
		String m0 = methods.get(0);
		String m1 = methods.get(1);
		String asym = null;
		String hash = null;
		asym = (Common.KEY_EXTRACTORS.get(m0)!=null) ? m0 : m1;
		hash = asym==m0 ? m1 : m0;
		
		var extr = Common.KEY_EXTRACTORS.get(asym).get();
		Key key = extr.extract(pubkeydata);
		if (!(key instanceof PublicKey)) {
			System.out.println("Loaded key is not a public key!");
			return;
		}
		
		PublicKey pk = (PublicKey) key;
		Signature sig = Signature.getInstance(hash + "with" + asym);
		sig.initVerify(pk);
		
		byte[] filebytes = Files.readAllBytes(filepath);
		List<String> siglines = sigdata.get(SIG);
		String sighex = Util.concatenate(siglines);
		byte[] sigbytes = Util.hexToByte(sighex);
		
		sig.update(filebytes);
		boolean valid = sig.verify(sigbytes);
		String verdict = valid ? "VALID" : "INVALID";
		System.out.println("SIGNATURE IS " + verdict);
	}
	
}
