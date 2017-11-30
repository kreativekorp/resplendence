package com.kreative.resplendence.pickers;

import java.util.*;

public class ResplendencePickerList {
	private Map<String,ResplendencePicker> map = new HashMap<String,ResplendencePicker>();
	
	public void add(ResplendencePicker aw) {
		map.put(aw.name(), aw);
	}
	
	public void remove(ResplendencePicker aw) {
		map.remove(aw.name());
	}
	
	public ResplendencePicker get(String name) {
		return map.get(name);
	}
	
	public String[] getNameList() {
		String[] stuff = map.keySet().toArray(new String[0]);
		Arrays.sort(stuff);
		return stuff;
	}
	
	public ResplendencePicker[] getResplendencePickerList() {
		ResplendencePicker[] stuff = map.values().toArray(new ResplendencePicker[0]);
		Arrays.sort(stuff, new Comparator<ResplendencePicker>() {
			public int compare(ResplendencePicker a, ResplendencePicker b) {
				return a.name().compareTo(b.name());
			}
		});
		return stuff;
	}
}
