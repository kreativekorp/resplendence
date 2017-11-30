package com.kreative.resplendence;

import java.io.*;
import java.util.*;
import com.kreative.dff.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.misc.*;

public class ResplPrefs implements OSConstants {
	private ResplPrefs() {
		// no constructor for you
	}
	
	private static final long DFF_TYPE_INT_LIST = KSFLUtilities.ecc("UInt32BE");
	private static final long DFF_TYPE_LONG_LIST = KSFLUtilities.ecc("UInt64BE");
	private static final long DFF_TYPE_STRING_LIST = KSFLUtilities.ecc("CStrUTF8");
	private static final long DFF_TYPE_FILE_LIST = KSFLUtilities.ecc("FileList");
	
	private static DFFResourceProvider resplPrefs = null;
	public static DFFResourceProvider getPrefsDFFResourceProvider() {
		if (resplPrefs == null) {
			try {
				resplPrefs = new DFFResourceFile(ResplPrefs.getPreferencesFile(), "rwd", DFFResourceFile.CREATE_IF_EMPTY, 3, false);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return resplPrefs;
	}
	
	private static File prefsFile = null;
	public static File getPreferencesFile() {
		if (prefsFile == null) {
			if (ResplMain.safeMode()) {
				try {
					prefsFile = File.createTempFile("ResplSafePrefs-", ".dff");
				} catch (IOException ioe) {
					prefsFile = new File("ResplSafePrefs-"+(int)(Math.random()*Integer.MAX_VALUE)+".dff");
					prefsFile.deleteOnExit();
				}
			} else if (RUNNING_ON_MAC_OS) {
				File u = new File(System.getProperty("user.home"));
				File l = new File(u, "Library");
				if (!l.exists()) l.mkdir();
				File p = new File(l, "Preferences");
				if (!p.exists()) p.mkdir();
				prefsFile = new File(p, "com.kreative.resplendence.dff");
			} else if (RUNNING_ON_WINDOWS) {
				File u = new File(System.getProperty("user.home"));
				File a = new File(u, "Application Data");
				if (!a.exists()) a.mkdir();
				File k = new File(a, "Kreative");
				if (!k.exists()) k.mkdir();
				prefsFile = new File(k, "Resplendence.dff");
			} else {
				File u = new File(System.getProperty("user.home"));
				prefsFile = new File(u, ".resplendence.dff");
			}
		}
		if (!prefsFile.exists()) {
			try {
				prefsFile.createNewFile();
			} catch (IOException ioe) {}
		}
		if (prefsFile.length() == 0) {
			try {
				RandomAccessFile raf = new RandomAccessFile(prefsFile, "rwd");
				raf.writeInt(DFFResourceProvider.MAGIC_NUMBER_DFF3);
				raf.writeInt(0);
				raf.close();
			} catch (IOException ioe) {}
		}
		return prefsFile;
	}
	
	private static List<String> dataToStrings(byte[] data) {
		Vector<String> v = new Vector<String>();
		if (data != null) {
			int s = 0;
			while (s < data.length) {
				int e = s;
				while (e < data.length && data[e] != 0) e++;
				try {
					v.add(new String(data, s, e-s, "UTF-8"));
				} catch (UnsupportedEncodingException uee) {
					v.add(new String(data, s, e-s));
				}
				s = e+1;
			}
		}
		return v;
	}
	
	private static byte[] stringsToData(List<String> v) {
		byte[][] stuff = new byte[v.size()][];
		int tlen = 0;
		int which = 0;
		for (String s : v) {
			try {
				stuff[which] = s.getBytes("UTF-8");
			} catch (UnsupportedEncodingException uee) {
				stuff[which] = s.getBytes();
			}
			tlen += stuff[which].length+1;
			which++;
		}
		byte[] data = new byte[tlen];
		for (int p=0, i=0; p < data.length && i < stuff.length; p++, i++) {
			for (int j=0; p < data.length && j < stuff[i].length; p++, j++) {
				data[p] = stuff[i][j];
			}
			data[p] = 0;
		}
		return data;
	}
	
	public static int getInt(String key) {
		byte[] data = getPrefsDFFResourceProvider().getData(DFF_TYPE_INT_LIST, key);
		int thisint = 0;
		if (data != null) for (int k = 0; k < 4; k++) {
			thisint = (thisint << 8) | (data[k] & 0xFF);
		}
		return thisint;
	}
	
	public static List<Integer> getInts(String key) {
		Vector<Integer> v = new Vector<Integer>();
		byte[] data = getPrefsDFFResourceProvider().getData(DFF_TYPE_INT_LIST, key);
		if (data != null) for (int i = 0; i < data.length; i += 4) {
			int thisint = 0;
			for (int j = i, k = 0; j < data.length && k < 4; j++, k++) {
				thisint = (thisint << 8) | (data[j] & 0xFF);
			}
			v.add(thisint);
		}
		return v;
	}
	
	public static long getLong(String key) {
		byte[] data = getPrefsDFFResourceProvider().getData(DFF_TYPE_LONG_LIST, key);
		long thislong = 0;
		if (data != null) for (int k = 0; k < 8; k++) {
			thislong = (thislong << 8) | (data[k] & 0xFF);
		}
		return thislong;
	}
	
	public static List<Long> getLongs(String key) {
		Vector<Long> v = new Vector<Long>();
		byte[] data = getPrefsDFFResourceProvider().getData(DFF_TYPE_LONG_LIST, key);
		if (data != null) for (int i = 0; i < data.length; i += 8) {
			long thislong = 0;
			for (int j = i, k = 0; j < data.length && k < 8; j++, k++) {
				thislong = (thislong << 8) | (data[j] & 0xFF);
			}
			v.add(thislong);
		}
		return v;
	}
	
	public static String getString(String key) {
		List<String> l = dataToStrings(getPrefsDFFResourceProvider().getData(DFF_TYPE_STRING_LIST, key));
		if (l != null && l.size() > 0) return l.get(0);
		return null;
	}
	
	public static List<String> getStrings(String key) {
		return dataToStrings(getPrefsDFFResourceProvider().getData(DFF_TYPE_STRING_LIST, key));
	}
	
	public static File getFile(String key) {
		List<String> l = dataToStrings(getPrefsDFFResourceProvider().getData(DFF_TYPE_FILE_LIST, key));
		if (l != null && l.size() > 0) return new File(l.get(0));
		return null;
	}
	
	public static List<File> getFiles(String key) {
		Vector<File> v = new Vector<File>();
		List<String> l = dataToStrings(getPrefsDFFResourceProvider().getData(DFF_TYPE_FILE_LIST, key));
		if (l != null) for (String s : l) {
			v.add(new File(s));
		}
		return v;
	}
	
	public static void setInt(String key, int i) {
		byte[] data = new byte[]{(byte)((i >>> 24) & 0xFF), (byte)((i >>> 16) & 0xFF), (byte)((i >>> 8) & 0xFF), (byte)(i & 0xFF)};
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_INT_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_INT_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_INT_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_INT_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setInts(String key, List<Integer> ii) {
		byte[] data = new byte[ii.size() * 4];
		Iterator<Integer> it = ii.iterator();
		for (int i = 0; it.hasNext() && i < data.length; i += 4) {
			int thisint = it.next();
			data[i+0] = (byte)((thisint >>> 24) & 0xFF);
			data[i+1] = (byte)((thisint >>> 16) & 0xFF);
			data[i+2] = (byte)((thisint >>> 8) & 0xFF);
			data[i+3] = (byte)(thisint & 0xFF);
		}
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_INT_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_INT_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_INT_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_INT_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLong(String key, long i) {
		byte[] data = new byte[]{
				(byte)((i >>> 56l) & 0xFFl),
				(byte)((i >>> 48l) & 0xFFl),
				(byte)((i >>> 40l) & 0xFFl),
				(byte)((i >>> 32l) & 0xFFl),
				(byte)((i >>> 24l) & 0xFFl),
				(byte)((i >>> 16l) & 0xFFl),
				(byte)((i >>> 8l) & 0xFFl),
				(byte)(i & 0xFFl)
		};
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_LONG_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_LONG_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_LONG_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_LONG_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLongs(String key, List<Long> ii) {
		byte[] data = new byte[ii.size() * 8];
		Iterator<Long> it = ii.iterator();
		for (int i = 0; it.hasNext() && i < data.length; i += 8) {
			long thislong = it.next();
			data[i+0] = (byte)((thislong >>> 56) & 0xFF);
			data[i+1] = (byte)((thislong >>> 48) & 0xFF);
			data[i+2] = (byte)((thislong >>> 40) & 0xFF);
			data[i+3] = (byte)((thislong >>> 32) & 0xFF);
			data[i+4] = (byte)((thislong >>> 24) & 0xFF);
			data[i+5] = (byte)((thislong >>> 16) & 0xFF);
			data[i+6] = (byte)((thislong >>> 8) & 0xFF);
			data[i+7] = (byte)(thislong & 0xFF);
		}
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_LONG_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_LONG_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_LONG_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_LONG_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setString(String key, String s) {
		Vector<String> v = new Vector<String>(); v.add(s);
		byte[] data = stringsToData(v);
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_STRING_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_STRING_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_STRING_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_STRING_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setStrings(String key, List<String> s) {
		byte[] data = stringsToData(s);
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_STRING_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_STRING_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_STRING_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_STRING_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setFile(String key, File f) {
		Vector<String> v = new Vector<String>();
		v.add(f.getAbsolutePath());
		byte[] data = stringsToData(v);
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_FILE_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_FILE_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_FILE_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_FILE_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void setFiles(String key, List<File> ff) {
		Vector<String> v = new Vector<String>();
		for (File f : ff) v.add(f.getAbsolutePath());
		byte[] data = stringsToData(v);
		if (getPrefsDFFResourceProvider().contains(DFF_TYPE_FILE_LIST, key)) {
			getPrefsDFFResourceProvider().setData(DFF_TYPE_FILE_LIST, key, data);
		} else try {
			getPrefsDFFResourceProvider().add(new DFFResource(DFF_TYPE_FILE_LIST, getPrefsDFFResourceProvider().getNextAvailableID(DFF_TYPE_FILE_LIST), key, data));
		} catch (DFFResourceAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetInt(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_INT_LIST, key);
	}
	
	public static void resetInts(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_INT_LIST, key);
	}
	
	public static void resetLong(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_LONG_LIST, key);
	}
	
	public static void resetLongs(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_LONG_LIST, key);
	}
	
	public static void resetString(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_STRING_LIST, key);
	}
	
	public static void resetStrings(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_STRING_LIST, key);
	}
	
	public static void resetFile(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_FILE_LIST, key);
	}
	
	public static void resetFiles(String key) {
		getPrefsDFFResourceProvider().remove(DFF_TYPE_FILE_LIST, key);
	}
}
