package kik.lab2;

public enum EncryptionMode {
	
	ECB(true, false),
	NONE(true, false),
	CBC(true, true),
	OFB(false, true),
	CFB(false, true),
	CTR(false, true),
	GCM(false, true);
	
	private final boolean requiresPadding;
	private final boolean requiresIv;
	
	private EncryptionMode(boolean requiresPadding, boolean requiresIv) {
		this.requiresPadding = requiresPadding;
		this.requiresIv = requiresIv;
	}
	
	public boolean requiresPadding() {
		return requiresPadding;
	}
	
	public boolean requiresIv() {
		return requiresIv;
	}
	
}
