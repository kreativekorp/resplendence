package com.kreative.resplendence.textfilter;

import java.text.*;
import java.util.Random;

import javax.swing.KeyStroke;

public class ChangeCase implements TextFilter {
	public String category(int i) {
		return "Change Case";
	}
	
	private Random rand = new Random();
	private boolean isSentenceEnder(char ch) {
		return (ch == '.' || ch == '!' || ch == '?');
	}

	public String filter(int i, String s) {
		switch (i) {
		case 0: return s.toUpperCase();
		case 1: return s.toLowerCase();
		case 2: {
			boolean wasWhitespace = true;
			String r = "";
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (Character.isWhitespace(ch)) {
					r += ch;
					wasWhitespace = true;
				} else if (wasWhitespace) {
					r += Character.toTitleCase(ch);
					wasWhitespace = false;
				} else {
					r += Character.toLowerCase(ch);
				}
			}
			return r;
		}
		case 3: {
			boolean wasEnd = true;
			String r = "";
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (isSentenceEnder(ch)) {
					r += ch;
					wasEnd = true;
				} else if (Character.isLetterOrDigit(ch) && wasEnd) {
					r += Character.toTitleCase(ch);
					wasEnd = false;
				} else {
					r += Character.toLowerCase(ch);
				}
			}
			return r;
		}
		case 4: {
			String r = "";
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (Character.isUpperCase(ch)) {
					r += Character.toLowerCase(ch);
				} else if (Character.isLowerCase(ch)) {
					r += Character.toUpperCase(ch);
				} else {
					r += ch;
				}
			}
			return r;
		}
		case 5: {
			boolean up = true;
			String r = "";
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (up) {
					r += Character.toUpperCase(ch);
					up = false;
				} else {
					r += Character.toLowerCase(ch);
					up = true;
				}
			}
			return r;
		}
		case 6: {
			String r = "";
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
				if (rand.nextBoolean()) {
					r += Character.toUpperCase(ch);
				} else {
					r += Character.toLowerCase(ch);
				}
			}
			return r;
		}
		default: return null;
		}
	}

	public String name(int i) {
		switch (i) {
		case 0: return "UPPERCASE";
		case 1: return "lowercase";
		case 2: return "Title Case";
		case 3: return "Sentence case.";
		case 4: return "tOGGLE cASE";
		case 5: return "AlTeRnAtInG CaSe";
		case 6: return "rANdOm CasE";
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
		return 7;
	}
}
