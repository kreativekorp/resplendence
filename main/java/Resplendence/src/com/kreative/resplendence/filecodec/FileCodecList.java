package com.kreative.resplendence.filecodec;

import java.util.*;

public class FileCodecList {
	private Map<String,FileCodec> map = new HashMap<String,FileCodec>();
	
	public void add(FileCodec aw) {
		map.put(aw.name(), aw);
	}
	
	public void remove(FileCodec aw) {
		map.remove(aw.name());
	}
	
	public FileCodec get(String name) {
		return map.get(name);
	}
	
	public String[] getNameList() {
		String[] stuff = map.keySet().toArray(new String[0]);
		Arrays.sort(stuff);
		return stuff;
	}
	
	public FileCodec[] getFileCodecList() {
		FileCodec[] stuff = map.values().toArray(new FileCodec[0]);
		Arrays.sort(stuff, new Comparator<FileCodec>() {
			public int compare(FileCodec a, FileCodec b) {
				return a.name().compareTo(b.name());
			}
		});
		return stuff;
	}
}
