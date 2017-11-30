package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

public interface DataFilter {
	public String category(int i);
	public String name(int i);
	public byte[] filter(int i, byte[] b);
	public KeyStroke keystroke(int i);
	public int numberOfFilters();
}
