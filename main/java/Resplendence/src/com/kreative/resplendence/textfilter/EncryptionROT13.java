package com.kreative.resplendence.textfilter;

import java.text.*;

import javax.swing.KeyStroke;

public class EncryptionROT13 implements TextFilter {
	public String category(int i) {
		return "Encryption";
	}

	public String filter(int i, String s) {
		String r = "";
		CharacterIterator it = new StringCharacterIterator(s);
		switch (i) {
		case 0:
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((ch >= 'A' && ch <= 'M') || (ch >= 'a' && ch <= 'm')) r += (char)(ch+13);
				else if ((ch >= 'N' && ch <= 'Z') || (ch >= 'n' && ch <= 'z')) r += (char)(ch-13);
				else r += ch;
			}
			break;
		case 1:
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (ch >= '0' && ch <= '4') r += (char)(ch+5);
				else if (ch >= '5' && ch <= '9') r += (char)(ch-5);
				else r += ch;
			}
			break;
		case 2:
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if ((ch >= 'A' && ch <= 'M') || (ch >= 'a' && ch <= 'm')) r += (char)(ch+13);
				else if ((ch >= 'N' && ch <= 'Z') || (ch >= 'n' && ch <= 'z')) r += (char)(ch-13);
				else if (ch >= '0' && ch <= '4') r += (char)(ch+5);
				else if (ch >= '5' && ch <= '9') r += (char)(ch-5);
				else r += ch;
			}
			break;
		case 3:
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (ch >= '!' && ch <= 'O') r += (char)(ch+47);
				else if (ch >= 'P' && ch <= '~') r += (char)(ch-47);
				else r += ch;
			}
			break;
		default:
			return null;
		}
		return r;
	}

	public String name(int i) {
		switch (i) {
		case 0: return "ROT13";
		case 1: return "ROT5";
		case 2: return "ROT18";
		case 3: return "ROT47";
		default: return null;
		}
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public boolean insert(int i) {
		return false;
	}
	
	public int numberOfFilters() {
		return 4;
	}
}
