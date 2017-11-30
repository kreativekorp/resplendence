package com.kreative.resplendence.template;

import java.io.*;
import javax.swing.*;

public class TFEditorString extends JTextArea implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private boolean l;
	private int f;
	private boolean e;
	private boolean o;
	private String t;
	
	public TFEditorString(int countWidth, boolean littleEndian, int fixedWidth, boolean evenPadded, boolean oddPadded, String textEncoding) {
		super(8,45);
		this.setFont(mono);
		w = countWidth;
		l = littleEndian;
		f = fixedWidth;
		e = evenPadded;
		o = oddPadded;
		t = textEncoding;
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
	
	private byte[] getBytes() {
		try {
			return getText().getBytes(t);
		} catch (UnsupportedEncodingException ex) {
			return getText().getBytes();
		}
	}
	
	private void setBytes(byte[] b, int s, int l) {
		try {
			setText(new String(b,s,l,t));
		} catch (UnsupportedEncodingException ex) {
			setText(new String(b,s,l));
		}
	}
	
	public Position readValue(byte[] data, Position pos) {
		if (w <= 0) {
			//C string
			if (f > 0) {
				//fixed width C string
				byte[] stuff = new byte[f];
				pos = pos.getBytes(data, stuff);
				int l = 0;
				while (l < stuff.length && stuff[l] != 0) l++;
				setBytes(stuff, 0, l);
				return pos;
			} else {
				//variable width C string
				Position start = pos;
				Position end = pos;
				byte[] tmp = new byte[]{77};
				while (tmp[0] != 0) end = end.getBytes(data, tmp);
				long l = (end.bitlength()-start.bitlength())>>3;
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) end = end.skipBytes(1);
				l--;
				byte[] stuff = new byte[(int)l];
				start.getBytes(data, stuff);
				setBytes(stuff, 0, (int)l);
				return end;
			}
		} else {
			//P string
			if (f > 0) {
				//fixed width P string
				long l;
				if ((w & 7) == 0) {
					byte[] cnt = new byte[w>>>3];
					pos = pos.getBytes(data, cnt);
					l = bytesToValue(cnt);
				} else {
					boolean[] cnt = new boolean[w];
					pos = pos.getBits(data, cnt);
					l = booleansToValue(cnt);
				}
				if (l > f) l = f;
				byte[] stuff = new byte[f];
				pos = pos.getBytes(data, stuff);
				setBytes(stuff, 0, (int)l);
				return pos;
			} else {
				//variable width P string
				long tl;
				if ((w & 7) == 0) {
					byte[] cnt = new byte[w>>>3];
					pos = pos.getBytes(data, cnt);
					tl = bytesToValue(cnt);
				} else {
					boolean[] cnt = new boolean[w];
					pos = pos.getBits(data, cnt);
					tl = booleansToValue(cnt);
				}
				long l = tl+(w>>>3);
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) l++;
				byte[] stuff = new byte[(int)l-(w>>>3)];
				pos = pos.getBytes(data, stuff);
				setBytes(stuff, 0, (int)tl);
				return pos;
			}
		}
	}

	public Position writeValue(byte[] data, Position pos) {
		if (w <= 0) {
			//C string
			if (f > 0) {
				//fixed width C string
				byte[] text = getBytes();
				byte[] stuff = new byte[f];
				for (int i=0; i<text.length && i<stuff.length; i++) stuff[i] = text[i];
				stuff[Math.min(stuff.length-1, text.length)] = 0;
				return pos.setBytes(data, stuff);
			} else {
				//variable width C string
				byte[] text = getBytes();
				int l = text.length+1;
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) l++;
				byte[] stuff = new byte[l];
				for (int i=0; i<text.length && i<stuff.length; i++) stuff[i] = text[i];
				stuff[Math.min(stuff.length-1, text.length)] = 0;
				return pos.setBytes(data, stuff);
			}
		} else {
			//P string
			if (f > 0) {
				//fixed width P string
				byte[] text = getBytes();
				if ((w & 7) == 0) {
					pos = pos.setBytes(data, valueToBytes(Math.min(text.length, f), w>>>3));
				} else {
					pos = pos.setBits(data, valueToBooleans(Math.min(text.length, f), w));
				}
				byte[] stuff = new byte[f];
				for (int i=0; i<text.length && i<stuff.length; i++) stuff[i] = text[i];
				return pos.setBytes(data, stuff);
			} else {
				//variable width P string
				byte[] text = getBytes();
				int tl = Math.min(text.length,(1<<w)-1);
				int l = tl+(w>>>3);
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) l++;
				byte[] stuff = new byte[l-(w>>>3)];
				for (int i=0; i<text.length && i<stuff.length; i++) stuff[i] = text[i];
				if ((w & 7) == 0) {
					pos = pos.setBytes(data, valueToBytes(tl, w>>>3));
				} else {
					pos = pos.setBits(data, valueToBooleans(tl, w));
				}
				return pos.setBytes(data, stuff);
			}
		}
	}

	public Position writeValue(Position pos) {
		if (w <= 0) {
			//C string
			if (f > 0) {
				//fixed width C string
				return pos.skipBytes(f);
			} else {
				//variable width C string
				int l = getBytes().length+1;
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) l++;
				return pos.skipBytes(l);
			}
		} else {
			//P string
			if (f > 0) {
				//fixed width P string
				return pos.skip(f,w);
			} else {
				//variable width P string
				int l = Math.min(getBytes().length,(1<<w)-1)+(w>>>3);
				if ((e && ((l & 1) == 1)) || (o && ((l & 1) == 0))) l++;
				return pos.skip(l-(w>>>3),w);
			}
		}
	}
}
