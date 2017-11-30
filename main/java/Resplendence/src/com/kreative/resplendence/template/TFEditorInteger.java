package com.kreative.resplendence.template;

import javax.swing.JTextField;

public class TFEditorInteger extends JTextField implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private int r;
	private boolean u;
	private boolean l;
	
	public TFEditorInteger(int bitWidth, int radix, boolean unsigned, boolean littleEndian) {
		super((int)Math.ceil(((bitWidth-1)*Math.log(2)/Math.log(radix))+1));
		this.setFont(mono);
		w = bitWidth;
		r = radix;
		u = unsigned;
		l = littleEndian;
	}
	
	private long bytesToValue(byte[] b) {
		long v = 0;
		if (l) {
			for (int i=b.length-1; i>=0; i--) {
				v <<= 8;
				v |= (b[i] & 0xFF);
			}
		} else {
			for (int i=0; i<b.length; i++) {
				v <<= 8;
				v |= (b[i] & 0xFF);
			}
		}
		if (!u) {
			v <<= (8*(8-b.length));
			v >>= (8*(8-b.length));
		}
		return v;
	}
	
	private long booleansToValue(boolean[] b) {
		long v = 0;
		if (l) {
			for (int i=b.length-1; i>=0; i--) {
				v <<= 1;
				if (b[i]) v |= 1;
			}
		} else {
			for (int i=0; i<b.length; i++) {
				v <<= 1;
				if (b[i]) v |= 1;
			}
		}
		if (!u) {
			v <<= (64-b.length);
			v >>= (64-b.length);
		}
		return v;
	}
	
	private byte[] valueToBytes(long v, int nb) {
		byte[] b = new byte[nb];
		if (l) {
			for (int i=0; i<b.length; i++) {
				b[i] = (byte)(v & 0xFF);
				if (u) v >>>= 8; else v >>= 8;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = (byte)(v & 0xFF);
				if (u) v >>>= 8; else v >>= 8;
			}
		}
		return b;
	}
	
	private boolean[] valueToBooleans(long v, int nb) {
		boolean[] b = new boolean[nb];
		if (l) {
			for (int i=0; i<b.length; i++) {
				b[i] = ((v & 1) > 0);
				if (u) v >>>= 1; else v >>= 1;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = ((v & 1) > 0);
				if (u) v >>>= 1; else v >>= 1;
			}
		}
		return b;
	}
	
	private long parseLong(String s, int radix) {
		try {
			return Long.parseLong(s, radix);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public Position readValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			byte[] stuff = new byte[w>>>3];
			pos = pos.getBytes(data, stuff);
			long v = bytesToValue(stuff);
			setText(Long.toString(v, r).toUpperCase());
		} else {
			boolean[] stuff = new boolean[w];
			pos = pos.getBits(data, stuff);
			long v = booleansToValue(stuff);
			setText(Long.toString(v, r).toUpperCase());
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			long v = parseLong(getText(), r);
			byte[] stuff = valueToBytes(v, w>>>3);
			pos = pos.setBytes(data, stuff);
		} else {
			long v = parseLong(getText(), r);
			boolean[] stuff = valueToBooleans(v, w);
			pos = pos.setBits(data, stuff);
		}
		return pos;
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
