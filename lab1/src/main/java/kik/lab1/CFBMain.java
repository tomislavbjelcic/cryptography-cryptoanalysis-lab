package kik.lab1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Scanner;

import kik.lab1.aes.AESCFB;
import kik.lab1.util.ByteUtils;

public class CFBMain {
	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Demonstracijski program algoritma AES u CFB načinu rada.\n");
		System.out.print("Čitati iz datoteke? (y/n) >");
		String in = sc.nextLine().strip().toLowerCase();
		byte[] plaintext = null;
		if (in.equals("y")) {
			System.out.print("Unesite putanju do datoteke >");
			String pathstr = sc.nextLine();
			Path path = Path.of(pathstr);
			try {
				plaintext = Files.readAllBytes(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.print("Hex unos? (y/n) >");
			String inh = sc.nextLine().strip().toLowerCase();
			if (inh.equals("y")) {
				System.out.print("Hex jasni/kriptirani tekst >");
				String hexplain = sc.nextLine();
				plaintext = ByteUtils.hexToByte(removeWhitespaces(hexplain));
			} else {
				System.out.print("Jasni/kriptirani tekst >");
				String hexplain = sc.nextLine();
				plaintext = hexplain.getBytes(StandardCharsets.UTF_8);
			}
		}
		
		byte[] keyBytes = null;
		System.out.print("Hex tajni ključ >");
		String hkey = sc.nextLine();
		keyBytes = ByteUtils.hexToByte(removeWhitespaces(hkey));
		
		byte[] iv = null;
		System.out.print("Inicijalizacijski vektor (za slučajno generiranje unesite \"R\")>");
		String ivhex = sc.nextLine().strip().toLowerCase();
		if (ivhex.equals("r")) {
			iv = new byte[AESCFB.IV_SIZE_BYTES];
			SecureRandom rng = new SecureRandom();
			rng.nextBytes(iv);
		} else
			iv = ByteUtils.hexToByte(removeWhitespaces(ivhex));
		
		System.out.print("Kriptirati ili dekriptirati? (Unos \"E\" za kriptiranje) >");
		String e_or_d = sc.nextLine().strip().toLowerCase();
		boolean e = e_or_d.equals("e");
		String _in = e ? "Jasni" : "Kriptirani";
		String _out = e ? "Kriptirani" : "Jasni";
		
		byte[] ciphertext = null;
		try {
			AESCFB cipher = new AESCFB(iv, keyBytes);
			if (e)
				ciphertext = cipher.encrypt(plaintext);
			else
				ciphertext = cipher.decrypt(plaintext);
		} catch (RuntimeException ex) {
			System.out.println("Dogodila se pogreška. Poruka greške: " + ex.getMessage());
			return;
		}
		
		System.out.println("\n");
		System.out.println(_in + " tekst: " + ByteUtils.byteToHex(plaintext));
		System.out.println("Inicijalizacijski vektor: " + ByteUtils.byteToHex(iv));
		System.out.println("Ključ: " + ByteUtils.byteToHex(keyBytes));
		System.out.println(_out + " tekst: " + ByteUtils.byteToHex(ciphertext));
		
		
		
		
		
	}
	
	
	public static String removeWhitespaces(String str) {
		char[] arr = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : arr) {
			if (!Character.isWhitespace(c))
				sb.append(c);
		}
		return sb.toString();
	}
}

