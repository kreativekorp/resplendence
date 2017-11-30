package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.*;
import com.kreative.resplendence.*;
import com.kreative.swing.*;

public class TFEditorColor extends JColorSwatch implements TFEditor, ResplendenceListener {
	private static final long serialVersionUID = 1;
	
	public static final int FORMAT_ARGB = 1;
	public static final int FORMAT_RGBA = 2;
	public static final int FORMAT_ABGR = 3;
	public static final int FORMAT_BGRA = 4;
	
	private int w;
	private int a,r,g,b,f;
	private boolean ia;
	private boolean l;
	
	public TFEditorColor(int abits, int rbits, int gbits, int bbits, int format, boolean invertAlpha, boolean littleEndian) {
		super(); //begin the hackathon!
		this.removeMouseListener(this); //remove its clicky thing to call JColorChooser
		this.setFocusable(true); //make it focusable
		this.setRequestFocusEnabled(true);
		this.addMouseListener(new MouseListener() { //force the frontmost ResplendenceListener to be me!!!
			public void mouseClicked(MouseEvent arg0) {
				arg0.getComponent().requestFocusInWindow();
				ResplMain.setFrontmostResplendenceListener(TFEditorColor.this);
				if (arg0.getClickCount() > 1) TFEditorColor.this.mouseClicked(arg0);
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		w = abits + rbits + gbits + bbits;
		a = abits; r = rbits; g = gbits; b = bbits;
		f = format; ia = invertAlpha; l = littleEndian;
	}
	
	private long decompose(Color c) {
		float[] ch = c.getRGBComponents(null);
		long rv = (long)Math.round(ch[0] * ((1L<<r)-1L));
		long gv = (long)Math.round(ch[1] * ((1L<<g)-1L));
		long bv = (long)Math.round(ch[2] * ((1L<<b)-1L));
		long av = (long)Math.round((ia?(1.0f-ch[3]):ch[3]) * ((1L<<a)-1L));
		switch (f) {
		case FORMAT_ARGB: return (av << (r+g+b)) | (rv << (g+b)) | (gv << b) | bv;
		case FORMAT_RGBA: return (rv << (g+b+a)) | (gv << (b+a)) | (bv << a) | av;
		case FORMAT_ABGR: return (av << (b+g+r)) | (bv << (g+r)) | (gv << r) | rv;
		case FORMAT_BGRA: return (bv << (g+r+a)) | (gv << (r+a)) | (rv << a) | av;
		default: return 0;
		}
	}
	
	private Color compose(long l) {
		long rv, gv, bv, av;
		switch (f) {
		case FORMAT_ARGB: av = (l >>> (r+g+b)) & ((1L<<a)-1L); rv = (l >>> (g+b)) & ((1L<<r)-1L); gv = (l >>> b) & ((1L<<g)-1L); bv = l & ((1L<<b)-1L); break;
		case FORMAT_RGBA: rv = (l >>> (g+b+a)) & ((1L<<r)-1L); gv = (l >>> (b+a)) & ((1L<<g)-1L); bv = (l >>> a) & ((1L<<b)-1L); av = l & ((1L<<a)-1L); break;
		case FORMAT_ABGR: av = (l >>> (b+g+r)) & ((1L<<a)-1L); bv = (l >>> (g+r)) & ((1L<<b)-1L); gv = (l >>> r) & ((1L<<g)-1L); rv = l & ((1L<<r)-1L); break;
		case FORMAT_BGRA: bv = (l >>> (g+r+a)) & ((1L<<b)-1L); gv = (l >>> (r+a)) & ((1L<<g)-1L); rv = (l >>> a) & ((1L<<r)-1L); av = l & ((1L<<a)-1L); break;
		default: rv=0; gv=0; bv=0; av=0;
		}
		float rf = (float)rv/(float)((1L<<r)-1L);
		float gf = (float)gv/(float)((1L<<g)-1L);
		float bf = (float)bv/(float)((1L<<b)-1L);
		float af = (float)av/(float)((1L<<a)-1L);
		return new Color(rf, gf, bf, ((a == 0)?(1.0f):(ia)?(1.0f-af):(af)));
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
	
	public Position readValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			byte[] stuff = new byte[w>>>3];
			pos = pos.getBytes(data, stuff);
			long v = bytesToValue(stuff);
			this.setColor(compose(v));
		} else {
			boolean[] stuff = new boolean[w];
			pos = pos.getBits(data, stuff);
			long v = booleansToValue(stuff);
			this.setColor(compose(v));
		}
		return pos;
	}
	
	public Position writeValue(byte[] data, Position pos) {
		if ((w & 7) == 0) {
			long v = decompose(this.getColor());
			byte[] stuff = valueToBytes(v, w>>>3);
			pos = pos.setBytes(data, stuff);
		} else {
			long v = decompose(this.getColor());
			boolean[] stuff = valueToBooleans(v, w);
			pos = pos.setBits(data, stuff);
		}
		return pos;
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}

	public Object respondToResplendenceEvent(ResplendenceEvent e) {
		switch (e.getID()) {
		case ResplendenceEvent.GET_COLOR:
			return this.getColor();
		case ResplendenceEvent.SET_COLOR:
			this.setColor(e.getColor());
			return null;
		default:
			return null;
		}
	}
}
