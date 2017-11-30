package com.kreative.resplendence.textfilter;

import javax.swing.KeyStroke;

public class InsertMiscCrap implements TextFilter {
	public String category(int i) {
		return "Insert";
	}

	public String filter(int i, String s) {
		switch (i) {
		case 0:
			return "         1         2         3         4         5         6         7         8" +
			"\n" + "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
		case 1:
			return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
		case 2:
			String ss = "";
			for (char ch = ' '; ch < 127; ch++) {
				ss += ch;
				if (ch % 16 == 15) ss += '\n';
			}
			return ss;
		case 3:
			return "0123456789ABCDEF";
		default:
			return null;
		}
	}

	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		switch (i) {
		case 0: return "80 Columns";
		case 1: return "Alphanumerics";
		case 2: return "ASCII Chart";
		case 3: return "Hex Digits";
		default: return null;
		}
	}
	
	public boolean insert(int i) {
		return true;
	}

	public int numberOfFilters() {
		return 4;
	}
}
