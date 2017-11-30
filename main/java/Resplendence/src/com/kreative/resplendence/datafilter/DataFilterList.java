package com.kreative.resplendence.datafilter;

import java.util.*;

public class DataFilterList {
	private Map<String,Map<String,DataFilter>> map = new HashMap<String,Map<String,DataFilter>>();
	private Map<String,Map<String,Integer>> imap = new HashMap<String,Map<String,Integer>>();
	
	public void add(DataFilter df) {
		int m = df.numberOfFilters();
		for (int i=0; i<m; i++) {
			String c = df.category(i);
			String n = df.name(i);
			if (!map.containsKey(c)) map.put(c, new HashMap<String,DataFilter>());
			if (!imap.containsKey(c)) imap.put(c, new HashMap<String,Integer>());
			map.get(c).put(n, df);
			imap.get(c).put(n, i);
		}
	}
	
	public void remove(DataFilter df) {
		int m = df.numberOfFilters();
		for (int i=0; i<m; i++) {
			String c = df.category(i);
			String n = df.name(i);
			if (map.containsKey(c)) {
				map.get(c).remove(n);
				imap.get(c).remove(n);
				if (map.get(c).isEmpty()) map.remove(c);
				if (imap.get(c).isEmpty()) imap.remove(c);
			}
		}
	}
	
	public Map<String,DataFilter> get(String category) {
		return map.get(category);
	}
	
	public DataFilter get(String category, String name) {
		if (map.containsKey(category)) {
			return map.get(category).get(name);
		} else return null;
	}
	
	public String[] getCategoryList() {
		String[] stuff = map.keySet().toArray(new String[0]);
		Arrays.sort(stuff);
		return stuff;
	}
	
	public String[] getNameList(String category) {
		if (map.containsKey(category)) {
			String[] stuff = map.get(category).keySet().toArray(new String[0]);
			Arrays.sort(stuff);
			return stuff;
		} else return null;
	}
	
	public DataFilter getDataFilter(String category, String name) {
		if (map.containsKey(category)) {
			if (map.get(category).containsKey(name)) {
				return map.get(category).get(name);
			} else return null;
		} else return null;
	}
	
	public int getDataFilterIndex(String category, String name) {
		if (imap.containsKey(category)) {
			if (imap.get(category).containsKey(name)) {
				return imap.get(category).get(name);
			} else return 0;
		} else return 0;
	}
}
