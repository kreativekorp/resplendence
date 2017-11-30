package com.kreative.resplendence.textfilter;

import javax.swing.KeyStroke;

public interface TextFilter {
	public String category(int i);
	public String name(int i);
	public String filter(int i, String s);
	public KeyStroke keystroke(int i);
	public boolean insert(int i);
	public int numberOfFilters();
}
