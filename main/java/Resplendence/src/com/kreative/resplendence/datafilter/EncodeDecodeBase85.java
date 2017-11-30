package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

import com.kreative.util.Base85;

public class EncodeDecodeBase85 implements DataFilter {
	public String category(int i) {
		return (i==0)?"Encode":"Decode";
	}

	public byte[] filter(int i, byte[] b) {
		if (i==0) {
			return Base85.encode(b).getBytes();
		} else {
			return Base85.decode(new String(b));
		}
	}

	public String name(int i) {
		return "Base85";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 2;
	}
}
