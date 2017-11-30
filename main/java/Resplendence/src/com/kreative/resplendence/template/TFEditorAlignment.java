package com.kreative.resplendence.template;

public class TFEditorAlignment implements TFEditor {
	private int w;
	
	public TFEditorAlignment(int bitWidth) {
		w = bitWidth;
	}
	public Position readValue(byte[] data, Position pos) {
		return pos.alignToBitMultiple(w);
	}

	public Position writeValue(byte[] data, Position pos) {
		return pos.alignToBitMultiple(w);
	}
	
	public Position writeValue(Position pos) {
		return pos.alignToBitMultiple(w);
	}
}
