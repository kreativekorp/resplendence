package com.kreative.resplendence.template;

import javax.swing.JTextField;

import com.kreative.util.CustomFloats;

public class TFEditorFloat extends JTextField implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int e, m, w;
	private boolean l;
	
	public TFEditorFloat(int bitWidth, boolean littleEndian) {
		super(24);
		this.setFont(mono);
		if (bitWidth < 1) throw new IllegalArgumentException("Cannot have float field with non-positive bit width!");
		switch (bitWidth) {
		case  1: e =  0; m =  0; break; //intro zero
		case  2: e =  1; m =  0; break; //intro inf
		case  3: e =  1; m =  1; break; //intro one, nan
		case  4: e =  2; m =  1; break;
		case  5: e =  3; m =  1; break;
		case  6: e =  4; m =  1; break;
		case  7: e =  4; m =  2; break;
		case  8: e =  4; m =  3; break;
		case  9: e =  5; m =  3; break;
		case 10: e =  5; m =  4; break;
		case 11: e =  5; m =  5; break;
		case 12: e =  5; m =  6; break;
		case 13: e =  5; m =  7; break;
		case 14: e =  5; m =  8; break;
		case 15: e =  5; m =  9; break;
		case 16: e =  5; m = 10; break;
		case 24: e =  7; m = 16; break;
		case 32: e =  8; m = 23; break;
		case 48: e = 10; m = 37; break;
		case 64: e = 11; m = 52; break;
		default:
			e = (int)Math.round(4.71*Math.log(bitWidth)-8.16);
			m = bitWidth-1-e;
			break;
		}
		w = bitWidth;
		l = littleEndian;
	}
	
	public TFEditorFloat(int exponent, int mantissa, boolean littleEndian) {
		if (exponent < 0 || mantissa < 0) throw new IllegalArgumentException("Cannot have negative exponent or mantissa bits!");
		e = exponent;
		m = mantissa;
		w = 1+exponent+mantissa;
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
	
	private float parseFloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public Position readValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			byte[] stuff = new byte[w>>>3];
			pos = pos.getBytes(data, stuff);
			long v = bytesToValue(stuff);
			if (e <= 8 && m <= 23) {
				setText(Float.toString(CustomFloats.bitsToCustomFloat(v,e,m)));
			} else {
				setText(Double.toString(CustomFloats.bitsToCustomDouble(v,e,m)));
			}
		} else {
			boolean[] stuff = new boolean[w];
			pos = pos.getBits(data, stuff);
			long v = booleansToValue(stuff);
			if (e <= 8 && m <= 23) {
				setText(Float.toString(CustomFloats.bitsToCustomFloat(v,e,m)));
			} else {
				setText(Double.toString(CustomFloats.bitsToCustomDouble(v,e,m)));
			}
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			long v;
			if (e <= 8 && m <= 23) {
				v = CustomFloats.customFloatToRawBits(parseFloat(getText()),e,m);
			} else {
				v = CustomFloats.customDoubleToRawBits(parseDouble(getText()),e,m);
			}
			byte[] stuff = valueToBytes(v, w>>>3);
			pos = pos.setBytes(data, stuff);
		} else {
			long v;
			if (e <= 8 && m <= 23) {
				v = CustomFloats.customFloatToRawBits(parseFloat(getText()),e,m);
			} else {
				v = CustomFloats.customDoubleToRawBits(parseDouble(getText()),e,m);
			}
			boolean[] stuff = valueToBooleans(v, w);
			pos = pos.setBits(data, stuff);
		}
		return pos;
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
