package com.kreative.resplendence.template;

import javax.swing.JTextField;

public class TFEditorFixed extends JTextField implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private boolean l;
	
	public TFEditorFixed(int bitWidth, boolean littleEndian) {
		super(24);
		this.setFont(mono);
		w = bitWidth;
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
		v <<= (8*(8-b.length));
		v >>= (8*(8-b.length));
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
		v <<= (64-b.length);
		v >>= (64-b.length);
		return v;
	}
	
	private byte[] valueToBytes(long v, int nb) {
		byte[] b = new byte[nb];
		if (l) {
			for (int i=0; i<b.length; i++) {
				b[i] = (byte)(v & 0xFF);
				v >>>= 8;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = (byte)(v & 0xFF);
				v >>>= 8;
			}
		}
		return b;
	}
	
	private boolean[] valueToBooleans(long v, int nb) {
		boolean[] b = new boolean[nb];
		if (l) {
			for (int i=0; i<b.length; i++) {
				b[i] = ((v & 1) > 0);
				v >>>= 1;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = ((v & 1) > 0);
				v >>>= 1;
			}
		}
		return b;
	}
	
	private double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
	
	public Position readValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			byte[] stuff = new byte[w>>>3];
			pos = pos.getBytes(data, stuff);
			double v = bytesToValue(stuff)/Math.pow(2.0, w/2.0);
			setText(Double.toString(v));
		} else {
			boolean[] stuff = new boolean[w];
			pos = pos.getBits(data, stuff);
			double v = booleansToValue(stuff)/Math.pow(2.0, w/2.0);
			setText(Double.toString(v));
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			long v = (long)Math.round(parseDouble(getText())*Math.pow(2.0, w/2.0));
			byte[] stuff = valueToBytes(v, w>>>3);
			pos = pos.setBytes(data, stuff);
		} else {
			long v = (long)Math.round(parseDouble(getText())*Math.pow(2.0, w/2.0));
			boolean[] stuff = valueToBooleans(v, w);
			pos = pos.setBits(data, stuff);
		}
		return pos;
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
