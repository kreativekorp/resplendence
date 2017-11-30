package com.kreative.resplendence.template;

public class TFEditorFiller implements TFEditor {
	private int w;
	
	public TFEditorFiller(int bitWidth) {
		w = bitWidth;
	}

	public Position readValue(byte[] data, Position pos) {
		return pos.skipBits(w);
	}

	public Position writeValue(byte[] data, Position pos) {
		return pos.skipBits(w);
	}
	
	public Position writeValue(Position pos) {
		return pos.skipBits(w);
	}
}
