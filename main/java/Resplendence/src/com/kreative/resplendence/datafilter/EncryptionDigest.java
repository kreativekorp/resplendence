package com.kreative.resplendence.datafilter;

import java.security.MessageDigest;
import javax.swing.KeyStroke;

public class EncryptionDigest implements DataFilter {
	public String category(int i) {
		return "Encryption";
	}

	public byte[] filter(int i, byte[] b) {
		try {
			return MessageDigest.getInstance(name(i)).digest(b);
		} catch (Exception e) {
			return b;
		}
	}

	public String name(int i) {
		switch (i) {
		case 0: return "MD2";
		case 1: return "MD5";
		case 2: return "SHA-1";
		case 3: return "SHA-256";
		case 4: return "SHA-384";
		case 5: return "SHA-512";
		default: return null;
		}
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 6;
	}
}
