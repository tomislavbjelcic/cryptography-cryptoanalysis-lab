package kik.lab2;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AddBcProv {
	
	/**
	 * Registrira dodatnog vanjskog providera, za slučaj da već dostupni provideri 
	 * ne mogu ponuditi neke kriptografske algoritme.
	 */
	public static void add() {
		Provider prov = new BouncyCastleProvider();
		String provName = prov.getName();
		int pos = Security.addProvider(prov);
		if (pos == -1) {
			System.out.println("Provider " + provName + " already installed!");
		} else
			System.out.println("Provider " + provName + " successfully added! (" + pos + ")");
	}
	
}
