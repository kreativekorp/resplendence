package com.kreative.resplendence.template;

import java.awt.Font;

public interface TFEditor {
	public static final Font mono = new Font("Monospaced", Font.PLAIN, 12);
	public Position readValue(byte[] data, Position pos);
	public Position writeValue(byte[] data, Position pos);
	public Position writeValue(Position pos);
}
