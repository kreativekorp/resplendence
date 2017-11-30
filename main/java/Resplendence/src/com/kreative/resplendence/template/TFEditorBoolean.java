package com.kreative.resplendence.template;

import javax.swing.JCheckBox;

public class TFEditorBoolean extends JCheckBox implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int w;
	private boolean l;
	
	public TFEditorBoolean(int bitWidth, boolean littleEndian) {
		this.setFont(mono);
		w = bitWidth;
		l = littleEndian;
	}
	
	public Position readValue(byte[] data, Position pos) {
		boolean[] b = new boolean[w];
		pos = pos.getBits(data, b);
		boolean v = false;
		for (int i=0; i<b.length; i++) if (b[i]) { v = true; break; }
		this.setSelected(v);
		return pos;
	}

	public Position writeValue(byte[] data, Position pos) {
		boolean[] b = new boolean[w];
		if (l) {
			b[Math.min(7,b.length-1)] = this.isSelected();
		} else {
			b[b.length-1] = this.isSelected();
		}
		return pos.setBits(data, b);
	}

	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
