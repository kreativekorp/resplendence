package com.kreative.resplendence.misc;

import java.util.BitSet;
import com.kreative.resplendence.ResplUtils;

public abstract class CharInFont {
	private static CharInFont instance = null;
	public static CharInFont getInstance() {
		if (instance == null) {
			if (ResplUtils.RUNNING_ON_MAC_OS) {
				try {
					Class<?> mcif = Class.forName("com.kreative.resplendence.mac.MacCharInFont");
					instance = (CharInFont)mcif.newInstance();
				} catch (Exception e) {
					instance = new JavaCharInFont();
				}
			} else {
				instance = new JavaCharInFont();
			}
		}
		return instance;
	}
	
	public abstract boolean isCharInFont(String fontName, int charToCheck);
	public abstract boolean[] isCharInFont(String fontName, int startChar, int endChar);
	public abstract boolean[] isCharInFont(String fontName, int[] charsToCheck);
	
	public BitSet allCharsInFont(String fontName) {
		BitSet res = new BitSet(0x10000);
		for (int plane = 0; plane < 0x110000; plane += 0x10000) {
			for (int page = 0; page < 0x10000; page += 0x100) {
				boolean[] allCharsInPage = isCharInFont(fontName, plane+page, plane+page+0xFF);
				for (int chr = 0; chr < 0x100; chr++) {
					if (allCharsInPage[chr]) res.set(plane+page+chr);
				}
			}
		}
		return res;
	}
}
