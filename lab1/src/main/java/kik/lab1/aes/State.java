package kik.lab1.aes;

import kik.lab1.util.ByteUtils;

import static kik.lab1.aes.Galois.gmul;

public class State {
	
	private static final int SIZE = 16;
	private static final int D = 4;
	
	private byte[] state;
	
	public State(byte[] state) {
		if (state.length != SIZE)
			throw new IllegalArgumentException("State of " + state.length + " bytes.");
		this.state = state;
	}
	
	public void addRoundKey(byte[] roundKey) {
		for (int i=0; i<SIZE; i++) {
			state[i] ^= roundKey[i];
		}
	}
	
	public void subBytes() {
		for (int i=0; i<SIZE; i++) {
			state[i] = SBox.subByte(state[i]);
		}
	}
	
	public void invSubBytes() {
		for (int i=0; i<SIZE; i++) {
			state[i] = SBox.subByteInv(state[i]);
		}
	}
	
	public void shiftRows() {
		byte tmp10 = state[1 + D*0];
		state[1 + D*0] = state[1 + D*1];
		state[1 + D*1] = state[1 + D*2];
		state[1 + D*2] = state[1 + D*3];
		state[1 + D*3] = tmp10;
		
		byte tmp20 = state[2 + D*0];
		byte tmp21 = state[2 + D*1];
		state[2 + D*0] = state[2 + D*2];
		state[2 + D*1] = state[2 + D*3];
		state[2 + D*2] = tmp20;
		state[2 + D*3] = tmp21;
		
		byte tmp33 = state[3 + D*3];
		state[3 + D*3] = state[3 + D*2];
		state[3 + D*2] = state[3 + D*1];
		state[3 + D*1] = state[3 + D*0];
		state[3 + D*0] = tmp33;
	}
	
	public void invShiftRows() {
		byte tmp30 = state[3 + D*0];
		state[3 + D*0] = state[3 + D*1];
		state[3 + D*1] = state[3 + D*2];
		state[3 + D*2] = state[3 + D*3];
		state[3 + D*3] = tmp30;
		
		byte tmp20 = state[2 + D*0];
		byte tmp21 = state[2 + D*1];
		state[2 + D*0] = state[2 + D*2];
		state[2 + D*1] = state[2 + D*3];
		state[2 + D*2] = tmp20;
		state[2 + D*3] = tmp21;
		
		byte tmp13 = state[1 + D*3];
		state[1 + D*3] = state[1 + D*2];
		state[1 + D*2] = state[1 + D*1];
		state[1 + D*1] = state[1 + D*0];
		state[1 + D*0] = tmp13;
	}
	
	private static int rc(int r, int c) {
		return r+D*c;
	}
	
	public void mixColumns() {
		for (int col=0; col<D; col++) {
			byte b0 = (byte)
							(   gmul(state[rc(0,col)],(byte)2) ^
								gmul(state[rc(1,col)],(byte)3) ^
								gmul(state[rc(2,col)],(byte)1) ^
								gmul(state[rc(3,col)],(byte)1));
			byte b1 = (byte)
					(   gmul(state[rc(0,col)],(byte)1) ^
						gmul(state[rc(1,col)],(byte)2) ^
						gmul(state[rc(2,col)],(byte)3) ^
						gmul(state[rc(3,col)],(byte)1));
			byte b2 = (byte)
					(   gmul(state[rc(0,col)],(byte)1) ^
						gmul(state[rc(1,col)],(byte)1) ^
						gmul(state[rc(2,col)],(byte)2) ^
						gmul(state[rc(3,col)],(byte)3));
			byte b3 = (byte)
					(   gmul(state[rc(0,col)],(byte)3) ^
						gmul(state[rc(1,col)],(byte)1) ^
						gmul(state[rc(2,col)],(byte)1) ^
						gmul(state[rc(3,col)],(byte)2));
			
			state[rc(0, col)] = b0;
			state[rc(1, col)] = b1;
			state[rc(2, col)] = b2;
			state[rc(3, col)] = b3;
		}
	}
	
	public void invMixColumns() {
		for (int col=0; col<D; col++) {
			byte b0 = (byte)
							(   gmul(state[rc(0,col)],(byte)14) ^
								gmul(state[rc(1,col)],(byte)11) ^
								gmul(state[rc(2,col)],(byte)13) ^
								gmul(state[rc(3,col)],(byte)9));
			byte b1 = (byte)
					(   gmul(state[rc(0,col)],(byte)9) ^
						gmul(state[rc(1,col)],(byte)14) ^
						gmul(state[rc(2,col)],(byte)11) ^
						gmul(state[rc(3,col)],(byte)13));
			byte b2 = (byte)
					(   gmul(state[rc(0,col)],(byte)13) ^
						gmul(state[rc(1,col)],(byte)9) ^
						gmul(state[rc(2,col)],(byte)14) ^
						gmul(state[rc(3,col)],(byte)11));
			byte b3 = (byte)
					(   gmul(state[rc(0,col)],(byte)11) ^
						gmul(state[rc(1,col)],(byte)13) ^
						gmul(state[rc(2,col)],(byte)9) ^
						gmul(state[rc(3,col)],(byte)14));
			
			state[rc(0, col)] = b0;
			state[rc(1, col)] = b1;
			state[rc(2, col)] = b2;
			state[rc(3, col)] = b3;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(SIZE*3+D);
		for (int i=0; i<D; i++) {
			for (int j=0; j<D; j++) {
				sb.append(ByteUtils.byteToHex(state[j*D+i])).append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
}
