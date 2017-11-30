package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

public class EncryptionMemfrob implements DataFilter {
	public String category(int i) {
		return "Encryption";
	}

	public byte[] filter(int w, byte[] b) {
		byte[] r = new byte[b.length];
		for (int i=0; i<b.length; i++) r[i] = (byte)(b[i] ^ 0x2A);
		return r;
	}

	public String name(int i) {
		return "Memfrob";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 1;
	}
}
