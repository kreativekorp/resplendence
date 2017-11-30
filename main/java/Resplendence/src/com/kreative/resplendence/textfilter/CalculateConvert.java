package com.kreative.resplendence.textfilter;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.kreative.resplendence.ResplMain;

public class CalculateConvert implements TextFilter {
	public String category(int i) {
		return "Calculate";
	}

	public String filter(int i, String s) {
		try {
			String z = s.trim();
			long l = 0;
			boolean negate = z.startsWith("-");
			if (z.startsWith("-") || z.startsWith("+")) z = z.substring(1).trim();
			if (z.startsWith("%")) {
				l = Long.parseLong(z.substring(1).trim(), 2);
			} else if (z.startsWith("$") || z.startsWith("#")) {
				l = Long.parseLong(z.substring(1).trim(), 16);
			} else if (z.startsWith("0x") || z.startsWith("0X")) {
				l = Long.parseLong(z.substring(2).trim(), 16);
			} else if (z.startsWith("0")) {
				l = Long.parseLong(z.substring(1).trim(), 8);
			} else {
				l = Long.decode(z);
			}
			switch (i) {
			case 0: return (negate?"-%":"%")+Long.toBinaryString(l);
			case 1: return (negate?"-0":"0")+Long.toOctalString(l);
			case 2: return (negate?"-":"")+Long.toString(l);
			case 3: return (negate?"-0x":"0x")+Long.toHexString(l).toUpperCase();
			default: return null;
			}
		} catch (NumberFormatException nfe) {
			return s;
		}
	}

	public String name(int i) {
		switch (i) {
		case 0: return "Convert to Binary";
		case 1: return "Convert to Octal";
		case 2: return "Convert to Decimal";
		case 3: return "Convert to Hexadecimal";
		default: return null;
		}
	}
	
	public KeyStroke keystroke(int i) {
		switch (i) {
		case 0: return KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, ResplMain.META_ALT_SHIFT_MASK);
		case 1: return KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, ResplMain.META_ALT_MASK);
		case 2: return KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, ResplMain.META_MASK);
		case 3: return KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, ResplMain.META_SHIFT_MASK);
		default: return null;
		}
	}
	
	public boolean insert(int i) {
		return false;
	}

	public int numberOfFilters() {
		return 4;
	}
}
