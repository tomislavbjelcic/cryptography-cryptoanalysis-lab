package kik.lab2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CryptoData {
	
	public static final String BEGIN = "---BEGIN CRYPTO DATA---";
	public static final String END = "---END CRYPTO DATA---";
	private static final Charset CS = StandardCharsets.US_ASCII;
	private static final String SEP = ":";
	private static final String PREF = "    ";
	
	public static final int UNIT_SIZE = 60;
	
	public static final String DESCRIPTION = "description";
	public static final String FILE_NAME = "file name";
	public static final String METHOD = "method";
	public static final String KEY_LENGTH = "key length";
	public static final String SECRET_KEY = "secret key";
	public static final String IV = "initialization vector";
	public static final String MODULUS = "modulus";
	public static final String PUBLIC_EXPONENT = "public exponent";
	public static final String PRIVATE_EXPONENT = "private exponent";
	public static final String SIG = "signature";
	public static final String ENCRYPTED_DATA = "data";
	public static final String ENVELOPE_DATA = "envelope data";
	public static final String ENVELOPE_CRYPT_KEY = "envelope crypt key";
	
	private static final Set<String> FIELDS = new HashSet<>();
	private static final List<String> FIELDS_LIST = new ArrayList<>(16);
	
	static {
		registerFields(DESCRIPTION, FILE_NAME, METHOD, KEY_LENGTH, SECRET_KEY, 
				IV, MODULUS, PUBLIC_EXPONENT, PRIVATE_EXPONENT, SIG,
				ENCRYPTED_DATA, ENVELOPE_DATA, ENVELOPE_CRYPT_KEY);
	}
	
	public static void registerFields(String... fields) {
		for (String f : fields) {
			FIELDS.add(f);
			FIELDS_LIST.add(f);
		}
	}
	
	public static Map<String, List<String>> fromFile(Path file) {
		Map<String, List<String>> data = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(file, CS)) {
			int state = 0;	// 0=nije doslo do pocetka, 1=citaju se kriptografski podaci, 2=doslo je do kraja
			boolean readingField = false;
			String field = null;
			List<String> values = null;
			while (true) {
				String lineRead = br.readLine();
				if (lineRead == null)
					break;
				
				String lineStripped = lineRead.strip();
				if (state == 0) {
					if (lineStripped.equals(BEGIN)) {
						state = 1;
					}
					continue;
				}
				
				if (state == 2)
					continue;
				
				if (state == 1) {
					if (!readingField) {
						if (lineStripped.isEmpty())
							continue;
						
						if (lineRead.equals(END)) {
							state = 2;
							continue;
						}
						
						if (!lineStripped.endsWith(SEP))
							throw new RuntimeException(lineStripped + " ne zavrsava sa " + SEP);
						
						field = lineRead.substring(0, lineRead.length()-SEP.length()).toLowerCase();
						
						if (!FIELDS.contains(field))
							throw new RuntimeException("Nepoznato polje: " + field);
						readingField = true;
						values = new ArrayList<>();
						data.put(field, values);
						continue;
					}
					
					if (lineStripped.isEmpty()) {
						field = null;
						readingField = false;
						continue;
					}
					
					if (!lineRead.startsWith(PREF))
						throw new RuntimeException("Invalid line: " + lineRead);
					
					
					values.add(lineStripped);
					continue;
				}
				
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void writeToFile(Map<String, List<String>> data, Path file) {
		try (BufferedWriter bw = Files.newBufferedWriter(file, CS)) {
			bw.write(BEGIN);
			bw.newLine();
			
			for (String f : FIELDS_LIST) {
				List<String> values = data.get(f);
				if (values == null)
					continue;
				
				bw.write(f);
				bw.write(SEP);
				bw.newLine();
				
				for (String v : values) {
					bw.write(PREF);
					bw.write(v);
					bw.newLine();
				}
				bw.newLine();
			}
			
			bw.write(END);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
