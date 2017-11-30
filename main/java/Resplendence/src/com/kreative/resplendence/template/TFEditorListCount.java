package com.kreative.resplendence.template;

import javax.swing.JLabel;

public class TFEditorListCount extends JLabel implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private boolean l;
	private boolean z;
	
	public TFEditorListCount(int bitWidth, boolean littleEndian, boolean zeroBased) {
		this.setFont(mono);
		w = bitWidth;
		l = littleEndian;
		z = zeroBased;
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
				v >>= 8;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = (byte)(v & 0xFF);
				v >>= 8;
			}
		}
		return b;
	}
	
	private boolean[] valueToBooleans(long v, int nb) {
		boolean[] b = new boolean[nb];
		if (l) {
			for (int i=0; i<b.length; i++) {
				b[i] = ((v & 1) > 0);
				v >>= 1;
			}
		} else {
			for (int i=b.length-1; i>=0; i--) {
				b[i] = ((v & 1) > 0);
				v >>= 1;
			}
		}
		return b;
	}
	
	private long parseLong(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public long getCount() {
		long count = parseLong(this.getText());
		if (z) count++;
		return count;
	}
	
	public void setCount(long count) {
		if (z) count--;
		setText(Long.toString(count));
	}
	
	public Position readValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			byte[] stuff = new byte[w>>>3];
			pos = pos.getBytes(data, stuff);
			long v = bytesToValue(stuff);
			setText(Long.toString(v));
		} else {
			boolean[] stuff = new boolean[w];
			pos = pos.getBits(data, stuff);
			long v = booleansToValue(stuff);
			setText(Long.toString(v));
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			long v = parseLong(getText());
			byte[] stuff = valueToBytes(v, w>>>3);
			pos = pos.setBytes(data, stuff);
		} else {
			long v = parseLong(getText());
			boolean[] stuff = valueToBooleans(v, w);
			pos = pos.setBits(data, stuff);
		}
		return pos;
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
