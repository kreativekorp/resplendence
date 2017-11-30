package com.kreative.resplendence.template;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.swing.JTextField;

public class TFEditorSymbol extends JTextField implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private boolean l;
	private String t;
	private boolean p,tn,ts;
	
	public TFEditorSymbol(int bitWidth, boolean littleEndian, String textEncoding, boolean spacePad, boolean trimNulls, boolean trimSpaces) {
		super(bitWidth>>>3);
		this.setFont(mono);
		w = bitWidth;
		l = littleEndian;
		t = textEncoding;
		p = spacePad;
		tn = trimNulls;
		ts = trimSpaces;
	}
	
	private String sanitize(String s) {
		if (ts) return s.trim();
		else if (tn) {
			int st = 0;
			int en = s.length();
			while (st < en && s.charAt(st) == 0) st++;
			while (en > st && s.charAt(en-1) == 0) en--;
			return s.substring(st, en);
		}
		else return s;
	}
	
	private byte[] reverse(byte[] b) {
		byte[] r = new byte[b.length];
		for (int i=0, j=b.length-1; i<b.length && j>=0; i++, j--) r[j] = b[i];
		return r;
	}
	
	public Position readValue(byte[] data, Position pos) {
		byte[] stuff = new byte[w>>>3];
		pos = pos.getBytes(data, stuff);
		if (l) stuff = reverse(stuff);
		try {
			setText(sanitize(new String(stuff,t)));
		} catch (UnsupportedEncodingException e) {
			setText(sanitize(new String(stuff)));
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		byte[] junk;
		try {
			junk = sanitize(getText()).getBytes(t);
		} catch (UnsupportedEncodingException e) {
			junk = sanitize(getText()).getBytes();
		}
		byte[] stuff = new byte[w>>>3];
		if (p) Arrays.fill(stuff, (byte)0x20);
		for (int i=0; i < junk.length && i < stuff.length; i++) stuff[i] = junk[i];
		if (l) stuff = reverse(stuff);
		pos = pos.setBytes(data, stuff);
		return pos;
	}

	public Position writeValue(Position pos) {
		return pos.skipBytes(w>>>3);
	}
}
