package com.kreative.resplendence.acc;

import java.util.*;

public class AccessoryWindowList {
	private Map<String,AccessoryWindow> map = new HashMap<String,AccessoryWindow>();
	private Map<String,Integer> imap = new HashMap<String,Integer>();
	
	public void add(AccessoryWindow aw) {
		int m = aw.numberOfWindows();
		for (int i=0; i<m; i++) {
			map.put(aw.name(i), aw);
			imap.put(aw.name(i), i);
		}
	}
	
	public void remove(AccessoryWindow aw) {
		int m = aw.numberOfWindows();
		for (int i=0; i<m; i++) {
			map.remove(aw.name(i));
			imap.remove(aw.name(i));
		}
	}
	
	public AccessoryWindow get(String name) {
		return map.get(name);
	}
	
	public String[] getNameList() {
		String[] stuff = map.keySet().toArray(new String[0]);
		Arrays.sort(stuff);
		return stuff;
	}
	
	public AccessoryWindow getAccWin(String name) {
		return map.get(name);
	}
	
	public int getAccWinIndex(String name) {
		return imap.get(name);
	}
}
