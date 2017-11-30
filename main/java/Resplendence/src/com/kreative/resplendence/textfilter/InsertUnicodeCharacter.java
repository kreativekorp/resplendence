package com.kreative.resplendence.textfilter;

import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.acc.*;

public class InsertUnicodeCharacter implements TextFilter {
	public String category(int i) {
		return "Insert";
	}

	public String filter(int i, String s) {
		(new CharacterChart()).open(0);
		return null;
	}

	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke('A', ResplMain.META_SHIFT_MASK);
	}

	public String name(int i) {
		return "Unicode Character...";
	}
	
	public boolean insert(int i) {
		return false;
	}

	public int numberOfFilters() {
		return 1;
	}
}
