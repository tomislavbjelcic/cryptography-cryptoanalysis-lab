package kik.lab1.aes;

import java.util.List;

import kik.lab1.util.ByteUtils;

public class AESKeySchedule {
	
	public static final List<Integer> PERMITTED_KEY_SIZES_BITS = List.of(128, 192, 256);
	private static final int[] RCON = new int[] {
		0x01_00_00_00,
		0x02_00_00_00,
		0x04_00_00_00,
		0x08_00_00_00,
		0x10_00_00_00,
		0x20_00_00_00,
		0x40_00_00_00,
		0x80_00_00_00,
		0x1b_00_00_00,
		0x36_00_00_00
	};
	
	private int R;
	private int keySizeBits;
	private int[] words;
	private byte[][] roundKeys;
	
	public AESKeySchedule(int keySizeBits) {
		
		if (!PERMITTED_KEY_SIZES_BITS.contains(keySizeBits)) {
			throw new IllegalArgumentException("Invalid key size! Permitted: " + PERMITTED_KEY_SIZES_BITS);
		}
		this.keySizeBits = keySizeBits;
		this.R = roundCount(keySizeBits);
		this.words = new int[4*R];
		
	}
	
	private static int roundCount(int keySizeBits) {
		return 11 + ((keySizeBits - 128) >>> 5);
	}
	
	public void generate(byte[] keyBytes) {
		if (keyBytes.length != keySizeBits/8)
			throw new IllegalArgumentException("This AES Key Schedule accepts only " + keySizeBits + " bit keys!");
		
		int N = keySizeBits/32;
		for (int i=0; i<words.length; i++) {
			if (i<N) {
				words[i] = ByteUtils.fromBytes(keyBytes[i*4], keyBytes[i*4+1], keyBytes[i*4+2], keyBytes[i*4+3]);
			} else if (i>=N && i%N==0) {
				words[i] = words[i-N] ^ SBox.subWord(Integer.rotateLeft(words[i-1], 8)) ^ RCON[i/N - 1];
			} else if (i>=N && N>6 && i%N==4) {
				words[i] = words[i-N] ^ SBox.subWord(words[i-1]);
			} else {
				words[i] = words[i-N] ^ words[i-1];
			}
		}
		this.roundKeys = genAsRoundKeys(this.words);
	}
	
	public int[] getWords() {
		return words;
	}
	
	public byte[][] getAsRoundKeys() {
		return roundKeys;
	}
	
	private static byte[][] genAsRoundKeys(int[] words) {
		int rows = words.length/4;
		byte[][] rk = new byte[rows][16];
		for (int i=0; i<rows; i++) {
			for (int j=0; j<4; j++) {
				int word = words[4*i+j];
				for (int wb=0; wb<4; wb++) {
					byte b = (byte) (word >>> (24-8*wb));
					rk[i][4*j+wb] = b;
				}
			}
		}
		return rk;
	}
	
}
