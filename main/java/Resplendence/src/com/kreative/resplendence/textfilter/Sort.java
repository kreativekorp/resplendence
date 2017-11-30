package com.kreative.resplendence.textfilter;

import java.util.*;
import java.util.regex.*;
import javax.swing.KeyStroke;
import com.kreative.resplendence.ResplMain;

public class Sort implements TextFilter {
	public String category(int i) {
		return "Sort";
	}
	
	public String filter(int w, String s) {
		boolean endsWithLineEnding = false;
		if (s.endsWith("\r\n")) {
			s = s.substring(0, s.length()-2);
			endsWithLineEnding = true;
		} else if (s.endsWith("\n") || s.endsWith("\r")) {
			s = s.substring(0, s.length()-1);
			endsWithLineEnding = true;
		}
		String[] stuff = s.split("\n|\r\n|\r");
		switch (w) {
		case 0: Arrays.sort(stuff); break;
		case 1: Arrays.sort(stuff, new StringReverseComparator()); break;
		case 2: Arrays.sort(stuff, new NaturalSortComparator()); break;
		case 3: Arrays.sort(stuff, new NaturalSortDescComparator()); break;
		case 4: Collections.shuffle(Arrays.asList(stuff)); break;
		case 5:
			s = "";
			for (int i=stuff.length-1; i>=0; i--) {
				s += stuff[i];
				if (endsWithLineEnding || i>0) s += "\n";
			}
			return s;
		default: return null;
		}
		s = "";
		for (int i=0; i<stuff.length; i++) {
			s += stuff[i];
			if (endsWithLineEnding || i<(stuff.length-1)) s += "\n";
		}
		return s;
	}
	
	public KeyStroke keystroke(int i) {
		switch (i) {
		case 2: return KeyStroke.getKeyStroke('K', ResplMain.META_SHIFT_MASK);
		case 3: return KeyStroke.getKeyStroke('K', ResplMain.META_ALT_SHIFT_MASK);
		default: return null;
		}
	}
	
	public String name(int i) {
		switch (i) {
		case 0: return "ASCII Sort Ascending";
		case 1: return "ASCII Sort Descending";
		case 2: return "Natural Sort Ascending";
		case 3: return "Natural Sort Descending";
		case 4: return "Randomize";
		case 5: return "Reverse";
		default: return null;
		}
	}
	
	public boolean insert(int i) {
		return false;
	}

	public int numberOfFilters() {
		return 6;
	}
	
	private static class StringReverseComparator implements Comparator<String> {
		public int compare(String a, String b) {
			return b.compareTo(a);
		}
	}
	
	private static class NaturalSortComparator implements Comparator<String> {
		Pattern sp = Pattern.compile("(\\d+(\\.\\d+)?|\\D+)");
		public int compare(String a, String b) {
			if (a == null && b == null) return 0;
			else if (a == null) return -1;
			else if (b == null) return 1;
			Matcher am = sp.matcher(a);
			Matcher bm = sp.matcher(b);
			int r = 0;
			while (r == 0) {
				boolean af = am.find();
				boolean bf = bm.find();
				if (!af && !bf) return a.compareTo(b);
				else if (!af) r = -1;
				else if (!bf) r = 1;
				else {
					String as = am.group();
					String bs = bm.group();
					if (Character.isDigit(as.charAt(0))) {
						r = new Double(as).compareTo(new Double(bs));
					} else {
						r = as.compareToIgnoreCase(bs);
					}
				}
			}
			return r;
		}
	}
	
	private static class NaturalSortDescComparator implements Comparator<String> {
		Pattern sp = Pattern.compile("(\\d+(\\.\\d+)?|\\D+)");
		public int compare(String a, String b) {
			if (a == null && b == null) return 0;
			else if (a == null) return 1;
			else if (b == null) return -1;
			Matcher am = sp.matcher(a);
			Matcher bm = sp.matcher(b);
			int r = 0;
			while (r == 0) {
				boolean af = am.find();
				boolean bf = bm.find();
				if (!af && !bf) return -a.compareTo(b);
				else if (!af) r = -1;
				else if (!bf) r = 1;
				else {
					String as = am.group();
					String bs = bm.group();
					if (Character.isDigit(as.charAt(0))) {
						r = new Double(as).compareTo(new Double(bs));
					} else {
						r = as.compareToIgnoreCase(bs);
					}
				}
			}
			return -r;
		}
	}
}
