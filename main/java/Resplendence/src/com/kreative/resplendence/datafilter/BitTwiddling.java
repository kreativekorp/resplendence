package com.kreative.resplendence.datafilter;

import java.util.Random;

import javax.swing.KeyStroke;

public class BitTwiddling implements DataFilter {
	private Random rand = new Random();
	
	public String category(int i) {
		return "Bit Twiddling";
	}

	public byte[] filter(int w, byte[] b) {
		byte[] r = new byte[b.length];
		switch (w) {
		case 0:
			java.util.Arrays.fill(r, (byte)0);
			break;
		case 1:
			java.util.Arrays.fill(r, (byte)-1);
			break;
		case 2:
			for (int i=0; i<b.length; i++) r[i] = (byte)(~b[i]);
			break;
		case 3:
			rand.nextBytes(r);
			break;
		case 4:
			for (int i=0, j=b.length-1; i<b.length && j>=0; i++, j--)
				r[i] = (byte)(Integer.reverse(b[j]) >>> 24);
			break;
		case 5:
			for (int i=0, j=b.length-1; i<b.length && j>=0; i++, j--)
				r[i] = b[j];
			break;
		default:
			return null;
		}
		return r;
	}

	public String name(int i) {
		switch (i) {
		case 0: return "Zero";
		case 1: return "One";
		case 2: return "Invert";
		case 3: return "Randomize";
		case 4: return "Swap Bits";
		case 5: return "Swap Bytes";
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
