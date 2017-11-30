package com.kreative.resplendence.acc;

import javax.swing.KeyStroke;

public interface AccessoryWindow {
	public String category(int i);
	public String name(int i);
	public void open(int i);
	public KeyStroke keystroke(int i);
	public int numberOfWindows();
}
