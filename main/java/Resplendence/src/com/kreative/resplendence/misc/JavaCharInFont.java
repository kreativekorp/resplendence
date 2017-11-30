package com.kreative.resplendence.misc;

import java.awt.Font;

public class JavaCharInFont extends CharInFont {
	@Override
	public boolean isCharInFont(String fontName, int charToCheck) {
		Font f = new Font(fontName, Font.PLAIN, 12);
		return f.canDisplay(charToCheck);
	}

	@Override
	public boolean[] isCharInFont(String fontName, int startChar, int endChar) {
		Font f = new Font(fontName, Font.PLAIN, 12);
		boolean[] b = new boolean[endChar-startChar+1];
		for (int i = 0, c = startChar; i < b.length; i++, c++) {
			b[i] = f.canDisplay(c);
		}
		return b;
	}

	@Override
	public boolean[] isCharInFont(String fontName, int[] charsToCheck) {
		Font f = new Font(fontName, Font.PLAIN, 12);
		boolean[] b = new boolean[charsToCheck.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = f.canDisplay(charsToCheck[i]);
		}
		return b;
	}
}
