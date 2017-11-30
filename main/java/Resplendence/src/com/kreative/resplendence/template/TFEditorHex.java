package com.kreative.resplendence.template;

import java.awt.*;
import com.kreative.swing.*;

public class TFEditorHex extends JHexEditorSuite implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int n;
	
	public TFEditorHex(int byteWidth, boolean littleEndian) {
		n = byteWidth;
		int w = 336;
		this.setMinimumSize(new Dimension(w,100));
		this.setPreferredSize(new Dimension(w,200));
		this.setLittleEndian(littleEndian);
	}
	
	public Position readValue(byte[] data, Position pos) {
		if (n > 0) {
			byte[] b = new byte[n];
			pos = pos.getBytes(data, b);
			this.setData(b);
			return pos;
		} else if (pos.bytepos() < data.length) {
			byte[] b = new byte[(int)(data.length-pos.bytepos())];
			pos = pos.getBytes(data, b);
			this.setData(b);
			return pos;
		} else {
			return pos;
		}
	}

	public Position writeValue(byte[] data, Position pos) {
		if (n > 0) {
			byte[] b = new byte[n];
			byte[] d = this.getData();
			for (int i=0; i<b.length && i<d.length; i++) {
				b[i] = d[i];
			}
			return pos.setBytes(data, b);
		} else {
			return pos.setBytes(data, this.getData());
		}
	}

	public Position writeValue(Position pos) {
		if (n > 0) {
			return pos.skipBytes(n);
		} else {
			return pos.skipBytes(this.getData().length);
		}
	}
}
