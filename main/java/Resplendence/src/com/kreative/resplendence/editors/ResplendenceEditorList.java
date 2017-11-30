package com.kreative.resplendence.editors;

import java.util.*;

public class ResplendenceEditorList {
	private Map<String,ResplendenceEditor> map = new HashMap<String,ResplendenceEditor>();
	
	public void add(ResplendenceEditor aw) {
		map.put(aw.name(), aw);
	}
	
	public void remove(ResplendenceEditor aw) {
		map.remove(aw.name());
	}
	
	public ResplendenceEditor get(String name) {
		return map.get(name);
	}
	
	public String[] getNameList() {
		String[] stuff = map.keySet().toArray(new String[0]);
		Arrays.sort(stuff);
		return stuff;
	}
	
	public ResplendenceEditor[] getResplendenceEditorList() {
		ResplendenceEditor[] stuff = map.values().toArray(new ResplendenceEditor[0]);
		Arrays.sort(stuff, new Comparator<ResplendenceEditor>() {
			public int compare(ResplendenceEditor a, ResplendenceEditor b) {
				return a.name().compareTo(b.name());
			}
		});
		return stuff;
	}
}
