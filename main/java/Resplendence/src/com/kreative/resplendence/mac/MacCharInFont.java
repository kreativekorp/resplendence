package com.kreative.resplendence.mac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.kreative.resplendence.misc.CharInFont;

public class MacCharInFont extends CharInFont {
	private static native boolean inFont(String fontName, int charToCheck);
	private static native boolean[] inFont(String fontName, int startChar, int endChar);
	private static native boolean[] inFont(String fontName, int[] charsToCheck);
	
	static {
		try {
			File temp = File.createTempFile("libMacCharInFont", ".jnilib");
			temp.deleteOnExit();
			FileOutputStream out = new FileOutputStream(temp);
			InputStream in = MacCharInFont.class.getResourceAsStream("libMacCharInFont.jnilib");
			byte[] buf = new byte[65536]; int len = 0;
			while ((len = in.read(buf)) >= 0) out.write(buf, 0, len);
			in.close();
			out.close();
			System.load(temp.getAbsolutePath());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isCharInFont(String fontName, int charToCheck) {
		return inFont(fontName, charToCheck);
	}
	
	public boolean[] isCharInFont(String fontName, int startChar, int endChar) {
		return inFont(fontName, startChar, endChar);
	}
	
	public boolean[] isCharInFont(String fontName, int[] charsToCheck) {
		return inFont(fontName, charsToCheck);
	}
}
