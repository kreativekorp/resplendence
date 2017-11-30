package com.kreative.resplendence;

import java.awt.*;
import java.io.*;
import java.util.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.misc.*;
import com.kreative.rsrc.CursorResource;
import com.kreative.resplendence.template.Template;
import com.kreative.util.VectorMap;

public class ResplRsrcs implements OSConstants {
	private ResplRsrcs() {
		// no constructor for you
	}
	
	// RESOURCES & PREFERENCES
	private static DFFResourceProvider resplRsrcs = null;
	private static Map<String,Map<String,String>> pngMap = null;
	public static DFFResourceProvider getAppDFFResourceProvider() {
		if (resplRsrcs == null) {
			DFFResourceSearchPath sp = new DFFResourceSearchPath();
			try {
				DFFResourceProvider dp = new DFFResourceFile(new File("ResplendenceResources.dff"), "r", DFFResourceFile.CREATE_NEVER, 3, false);
				sp.pushProvider(dp);
			} catch (IOException ioe) {}
			sp.pushProvider(ResplPrefs.getPrefsDFFResourceProvider());
			resplRsrcs = sp;
		}
		if (pngMap == null) {
			pngMap = new HashMap<String,Map<String,String>>();
			String[] names = resplRsrcs.getNames(KSFLConstants.ImagePNG);
			for (String name : names) {
				String[] parts = name.split("\\|");
				if (parts.length > 1) {
					Map<String,String> subMap = pngMap.get(parts[0]);
					if (subMap == null) {
						subMap = new HashMap<String,String>();
						pngMap.put(parts[0], subMap);
					}
					for (int i = 1; i < parts.length; i++) {
						subMap.put(parts[i], name);
					}
				}
			}
		}
		return resplRsrcs;
	}
	private static String expandPNGName(String namespace, String name) {
		getAppDFFResourceProvider();
		if (pngMap.containsKey(namespace)) {
			Map<String,String> subMap = pngMap.get(namespace);
			if (subMap.containsKey(name)) {
				return subMap.get(name);
			}
		}
		return namespace;
	}
	
	@Deprecated
	public static String ls(String id, String defLang, String defValue) {
		try {
			DFFResourceProvider dp = getAppDFFResourceProvider();
			if (!dp.contains(KSFLConstants.LStrUTF8, id)) {
				byte[] l = defLang.split("_")[0].getBytes("UTF-8");
				byte[] ll = defLang.getBytes("UTF-8");
				byte[] s = defValue.getBytes("UTF-8");
				byte[] data = new byte[l.length+1+s.length+1+ll.length+1+s.length+1];
				int p = 0;
				for (int sp = 0; sp < l.length; sp++, p++) data[p] = l[sp]; data[p++] = 0;
				for (int sp = 0; sp < s.length; sp++, p++) data[p] = s[sp]; data[p++] = 0;
				for (int sp = 0; sp < ll.length; sp++, p++) data[p] = ll[sp]; data[p++] = 0;
				for (int sp = 0; sp < s.length; sp++, p++) data[p] = s[sp]; data[p++] = 0;
				DFFResource o = new DFFResource(KSFLConstants.LStrUTF8, dp.getNextAvailableID(KSFLConstants.LStrUTF8), id, data);
				dp.add(o);
				return defValue;
			} else {
				String lang = System.getProperty("user.language").toLowerCase();
				String reg = System.getProperty("user.country").toLowerCase();
				Map<String,String> ss = new HashMap<String,String>();
				byte[] data = dp.getData(KSFLConstants.LStrUTF8, id);
				int p = 0;
				while (p < data.length) {
					int sp;
					sp = p;
					while (p < data.length && data[p] != 0) p++;
					String l = new String(data, sp, p-sp);
					p++;
					sp = p;
					while (p < data.length && data[p] != 0) p++;
					String s = new String(data, sp, p-sp);
					p++;
					ss.put(l.toLowerCase(), s);
				}
				if (ss.containsKey(lang+"_"+reg)) return ss.get(lang+"_"+reg);
				else if (ss.containsKey(lang)) return ss.get(lang);
				else return defValue;
			}
		} catch (Exception e) {
			return defValue;
		}
	}
	
	public static Image getPNG(int id) {
		try {
			return Toolkit.getDefaultToolkit().createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, id));
		} catch (NullPointerException e) { return null; }
	}
	public static Image getPNG(String name) {
		try {
			return Toolkit.getDefaultToolkit().createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, name));
		} catch (NullPointerException e) { return null; }
	}
	public static Image getPNG(String namespace, String name) {
		return getPNG(expandPNGName(namespace, name));
	}
	public static Image getPNG(Toolkit tk, int id) {
		return tk.createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, id));
	}
	public static Image getPNG(Toolkit tk, String name) {
		return tk.createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, name));
	}
	public static Image getPNG(Toolkit tk, String namespace, String name) {
		return getPNG(tk, expandPNGName(namespace, name));
	}

	public static Image getPNGnow(int id) {
		try {
			Image i = Toolkit.getDefaultToolkit().createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, id));
			while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null));
			return i;
		} catch (NullPointerException e) { return null; }
	}
	public static Image getPNGnow(String name) {
		try {
			Image i = Toolkit.getDefaultToolkit().createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, name));
			while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null));
			return i;
		} catch (NullPointerException e) { return null; }
	}
	public static Image getPNGnow(String namespace, String name) {
		return getPNGnow(expandPNGName(namespace, name));
	}
	public static Image getPNGnow(Toolkit tk, int id) {
		Image i = tk.createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, id));
		while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null));
		return i;
	}
	public static Image getPNGnow(Toolkit tk, String name) {
		Image i = tk.createImage(getAppDFFResourceProvider().getData(KSFLConstants.ImagePNG, name));
		while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null));
		return i;
	}
	public static Image getPNGnow(Toolkit tk, String namespace, String name) {
		return getPNGnow(tk, expandPNGName(namespace, name));
	}
	
	public static Cursor getCURS(int id) {
		byte[] cd = getAppDFFResourceProvider().getData(KSFLConstants.Mac_CURS, id);
		CursorResource c = new CursorResource((short)id, cd);
		return c.toCursor(Toolkit.getDefaultToolkit());
	}
	public static Cursor getCURS(Toolkit tk, int id) {
		byte[] cd = getAppDFFResourceProvider().getData(KSFLConstants.Mac_CURS, id);
		CursorResource c = new CursorResource((short)id, cd);
		return c.toCursor(tk);
	}
	
	public static Template getTMPL(String type) {
		DFFResource r = getAppDFFResourceProvider().get(KSFLConstants.Mac_TMPL, type);
		if (r == null || r.data.length == 0) return null;
		else return new Template(r.data);
	}
	public static boolean hasTMPL(String type) {
		DFFResource r = getAppDFFResourceProvider().get(KSFLConstants.Mac_TMPL, type);
		return (r != null && r.data.length > 0);
	}
	
	public static VectorMap<Integer,String> getSymbolReference(String tablName, int tablId, int dummy) {
		return getSymbolReference(KSFLUtilities.ecc(tablName), tablId, dummy);
	}
	public static VectorMap<Integer,String> getSymbolReference(String tablName, String tablOName, int dummy) {
		return getSymbolReference(KSFLUtilities.ecc(tablName), tablOName, dummy);
	}
	public static VectorMap<Long,String> getSymbolReference(String tablName, int tablId, long dummy) {
		return getSymbolReference(KSFLUtilities.ecc(tablName), tablId, dummy);
	}
	public static VectorMap<Long,String> getSymbolReference(String tablName, String tablOName, long dummy) {
		return getSymbolReference(KSFLUtilities.ecc(tablName), tablOName, dummy);
	}
	public static String getSymbolDescription(String tablName, int tablId, int sym) {
		return getSymbolDescription(KSFLUtilities.ecc(tablName), tablId, sym);
	}
	public static String getSymbolDescription(String tablName, String tablOName, int sym) {
		return getSymbolDescription(KSFLUtilities.ecc(tablName), tablOName, sym);
	}
	public static String getSymbolDescription(String tablName, int tablId, long sym) {
		return getSymbolDescription(KSFLUtilities.ecc(tablName), tablId, sym);
	}
	public static String getSymbolDescription(String tablName, String tablOName, long sym) {
		return getSymbolDescription(KSFLUtilities.ecc(tablName), tablOName, sym);
	}
	public static VectorMap<Integer,String> getSymbolReference(long tablName, int tablId, int dummy) {
		VectorMap<Integer,String> a = new VectorMap<Integer,String>();
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablId);
		int i = 0;
		while (i < t.length) {
			int thisSym = 0;
			for (int j=0; j<4; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			String thisDesc = new String(t, ss, se-ss);
			if (!thisDesc.startsWith("!!")) {
				a.put(thisSym, thisDesc);
			}
		}
		return a;
	}
	public static VectorMap<Integer,String> getSymbolReference(long tablName, String tablOName, int dummy) {
		VectorMap<Integer,String> a = new VectorMap<Integer,String>();
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablOName);
		int i = 0;
		while (i < t.length) {
			int thisSym = 0;
			for (int j=0; j<4; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			String thisDesc = new String(t, ss, se-ss);
			if (!thisDesc.startsWith("!!")) {
				a.put(thisSym, thisDesc);
			}
		}
		return a;
	}
	public static VectorMap<Long,String> getSymbolReference(long tablName, int tablId, long dummy) {
		VectorMap<Long,String> a = new VectorMap<Long,String>();
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablId);
		int i = 0;
		while (i < t.length) {
			long thisSym = 0;
			for (int j=0; j<8; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			String thisDesc = new String(t, ss, se-ss);
			if (!thisDesc.startsWith("!!")) {
				a.put(thisSym, thisDesc);
			}
		}
		return a;
	}
	public static VectorMap<Long,String> getSymbolReference(long tablName, String tablOName, long dummy) {
		VectorMap<Long,String> a = new VectorMap<Long,String>();
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablOName);
		int i = 0;
		while (i < t.length) {
			long thisSym = 0;
			for (int j=0; j<8; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			String thisDesc = new String(t, ss, se-ss);
			if (!thisDesc.startsWith("!!")) {
				a.put(thisSym, thisDesc);
			}
		}
		return a;
	}
	public static String getSymbolDescription(long tablName, int tablId, int sym) {
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablId);
		int i = 0;
		while (i < t.length) {
			int thisSym = 0;
			for (int j=0; j<4; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			if (thisSym == sym) {
				String thisDesc = new String(t, ss, se-ss);
				if (thisDesc.startsWith("!!")) {
					return thisDesc.substring(2);
				} else {
					return thisDesc;
				}
			}
		}
		return "";
	}
	public static String getSymbolDescription(long tablName, String tablOName, int sym) {
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablOName);
		int i = 0;
		while (i < t.length) {
			int thisSym = 0;
			for (int j=0; j<4; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			if (thisSym == sym) {
				String thisDesc = new String(t, ss, se-ss);
				if (thisDesc.startsWith("!!")) {
					return thisDesc.substring(2);
				} else {
					return thisDesc;
				}
			}
		}
		return "";
	}
	public static String getSymbolDescription(long tablName, int tablId, long sym) {
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablId);
		int i = 0;
		while (i < t.length) {
			long thisSym = 0;
			for (int j=0; j<8; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			if (thisSym == sym) {
				String thisDesc = new String(t, ss, se-ss);
				if (thisDesc.startsWith("!!")) {
					return thisDesc.substring(2);
				} else {
					return thisDesc;
				}
			}
		}
		return "";
	}
	public static String getSymbolDescription(long tablName, String tablOName, long sym) {
		byte[] t = getAppDFFResourceProvider().getData(tablName, tablOName);
		int i = 0;
		while (i < t.length) {
			long thisSym = 0;
			for (int j=0; j<8; j++) {
				thisSym <<= 8;
				thisSym |= (t[i++] & 0xFF);
			}
			int ss = i;
			while (t[i] != 0) i++;
			int se = i++;
			if (thisSym == sym) {
				String thisDesc = new String(t, ss, se-ss);
				if (thisDesc.startsWith("!!")) {
					return thisDesc.substring(2);
				} else {
					return thisDesc;
				}
			}
		}
		return "";
	}
}
