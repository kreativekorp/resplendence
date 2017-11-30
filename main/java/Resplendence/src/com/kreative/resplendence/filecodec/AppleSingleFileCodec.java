package com.kreative.resplendence.filecodec;

import java.io.*;
import java.util.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;

public class AppleSingleFileCodec implements FileCodec {
	public void decode(File f, RWCFile wc) throws IOException {
		decode(f, wc, 0x00051600, false);
	}
	
	public void encode(File f, RWCFile wc) throws IOException {
		encode(f, wc, 0x00051600, false);
	}

	public String name() {
		return "AppleSingle";
	}
	
	public int recognizes(File f) {
		return recognizes(f, 0x0051600, FileCodec.RECOGNIZES_ONE_PART_FILE);
	}
	
	public void removeExtras(File f) {
		//nothing
	}
	
	private static boolean isHumanReadable(byte[] b) {
		for (int i=0; i<b.length; i++) if ((b[i] & 0xFF) < 0x20) return false;
		return true;
	}
	
	private static boolean isZeroes(byte[] b) {
		for (int i=0; i<b.length; i++) if (b[i] != 0) return false;
		return true;
	}
	
	private static String ns(byte[] b, String te) {
		if (b == null) return "";
		try {
			return new String(b, te);
		} catch (UnsupportedEncodingException uee) {
			return new String(b);
		}
	}
	
	private static byte[] gb(String s, String te) {
		if (s == null) return new byte[0];
		try {
			return s.getBytes(te);
		} catch (UnsupportedEncodingException uee) {
			return s.getBytes();
		}
	}

	private static Calendar prodosdate(int i) {
		short date = (short)((i >>> 16) & 0xFFFF);
		short time = (short)(i & 0xFFFF);
		int year = 1900 + ((date >>> 9) & 0x7F);
		int month = ((date >>> 5) & 0x0F) - 1;
		int day = (date & 0x1F);
		int hour = ((time >>> 8) & 0x1F);
		int minute = (time & 0x3F);
		return new GregorianCalendar(year, month, day, hour, minute);
	}
	private static int prodosdate(Calendar c) {
		if (c == null) return 0;
		int year = c.get(Calendar.YEAR)-1900;
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		short date = (short)(((year & 0x7F) << 9) | ((month & 0x0F) << 5) | (day & 0x1F));
		short time = (short)(((hour & 0x1F) << 8) | (minute & 0x3F));
		return ((date & 0xFFFF) << 16) | (time & 0xFFFF);
	}
	
	private static Calendar unixdate(int d) {
		long l = (long)d & 0xFFFFFFFFl;
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(l*1000);
		return c;
	}
	private static int unixdate(Calendar c) {
		if (c == null) return 0;
		return (int)( c.getTimeInMillis()/1000l );
	}
	
	private static final Calendar macepoch = new GregorianCalendar(1904, 0, 1);
	private static Calendar macdate(int d) {
		long l = (long)d & 0xFFFFFFFFl;
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(macepoch.getTimeInMillis()+l*1000);
		return c;
	}
	private static int macdate(Calendar c) {
		if (c == null) return 0;
		return (int)( (c.getTimeInMillis()-macepoch.getTimeInMillis())/1000l );
	}
	
	private static final Calendar msdosepoch = new GregorianCalendar(1980, 0, 1);
	private static Calendar msdosdate(int d) {
		long l = (long)d & 0xFFFFFFFFl;
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(msdosepoch.getTimeInMillis()+l*1000);
		return c;
	}
	private static int msdosdate(Calendar c) {
		if (c == null) return 0;
		return (int)( (c.getTimeInMillis()-msdosepoch.getTimeInMillis())/1000l );
	}
	
	private static final Calendar asepoch = new GregorianCalendar(2000, 0, 1);
	private static Calendar asdate(int d) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(asepoch.getTimeInMillis()+(long)d*1000l);
		return c;
	}
	private static int asdate(Calendar c) {
		if (c == null) return Integer.MIN_VALUE;
		return (int)( (c.getTimeInMillis()-asepoch.getTimeInMillis())/1000l );
	}
	
	protected static void decode(File f, RWCFile wc, int magic, boolean ignoreData) throws IOException {
		AttributeFile afi = wc.getAttributeHandle();
		RandomAccessFile out;
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		if (raf.length() < 26) { raf.close(); throw new IOException(); }
		if (raf.readInt() != magic) { raf.close(); throw new IOException(); }
		int v = raf.readShort();
		if (v < 1 || v > 2) { raf.close(); throw new IOException(); }
		if (raf.readShort() != 0) { raf.close(); throw new IOException(); }
		byte[] fsys = new byte[16];
		raf.read(fsys);
		if (v == 1 && !isHumanReadable(fsys)) { raf.close(); throw new IOException(); }
		if (v == 2 && !isZeroes(fsys)) { raf.close(); throw new IOException(); }
		String fsyss = null;
		if (v == 1) {
			fsyss = ns(fsys, "US-ASCII");
			wc.getAttributeHandle().putString(RWCFile.ATTRIBUTE_FILE_SYSTEM, fsyss);
		}
		int n = raf.readShort();
		int[][] parts = new int[n][];
		for (int i=0; i<n; i++) {
			int t = raf.readInt();
			int o = raf.readInt();
			int l = raf.readInt();
			parts[i] = new int[]{t,o,l};
		}
		for (int i=0; i<n; i++) {
			byte[] stuff = new byte[parts[i][2]];
			raf.seek((long)parts[i][1] & 0xFFFFFFFFl);
			raf.read(stuff);
			switch (parts[i][0]) {
			case 1: //data fork
				if (!ignoreData) {
					out = new RandomAccessFile(wc.getDataFork(), "rwd");
					out.setLength(0);
					out.write(stuff);
					out.close();
				}
				break;
			case 2: //resource fork
				out = new RandomAccessFile(wc.getResourceFork(), "rwd");
				out.setLength(0);
				out.write(stuff);
				out.close();
				break;
			case 3: //name
				if (fsyss == null) {
					//other name
					afi.putString(RWCFile.ATTRIBUTE_DISPLAY_NAME, ns(stuff, "UTF-8"));
				} else if (fsyss.equals("ProDOS")) {
					//prodos name
					afi.putString(RWCFile.ATTRIBUTE_PRODOS_NAME, ns(stuff, "MACROMAN"));
				} else if (fsyss.equals("Macintosh")) {
					//mac name
					afi.putString(RWCFile.ATTRIBUTE_MACOS_NAME, ns(stuff, "MACROMAN"));
				} else if (fsyss.equals("MS-DOS")) {
					//msdos name
					afi.putString(RWCFile.ATTRIBUTE_MSDOS_NAME, ns(stuff, "CP734"));
				} else if (fsyss.equals("Unix")) {
					//unix name
					afi.putString(RWCFile.ATTRIBUTE_UNIX_NAME, ns(stuff, "UTF-8"));
				} else if (fsyss.equals("VAX VMS")) {
					//unix name
					afi.putString(RWCFile.ATTRIBUTE_VMS_NAME, ns(stuff, "UTF-8"));
				} else {
					//other name
					afi.putString(RWCFile.ATTRIBUTE_DISPLAY_NAME, ns(stuff, "UTF-8"));
				}
				break;
			case 4: //comment
				out = new RandomAccessFile(wc.getFinderComment(), "rwd");
				out.setLength(0);
				out.write(stuff);
				out.close();
				break;
			case 5: //ICON
				out = new RandomAccessFile(wc.getFinderIcon(), "rwd");
				out.setLength(0);
				out.write(stuff);
				out.close();
				break;
			case 6: //cicn
				out = new RandomAccessFile(wc.getFinderColorIcon(), "rwd");
				out.setLength(0);
				out.write(stuff);
				out.close();
				break;
			case 7: //info (v1; varies depending on fsys)
				if (fsyss == null) {
					throw new IOException();
				} else if (fsyss.equals("ProDOS")) {
					//prodos info (v1)
					afi.putCalendar(RWCFile.ATTRIBUTE_CREATE_DATE, prodosdate(KSFLUtilities.getInt(stuff, 0)));
					afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, prodosdate(KSFLUtilities.getInt(stuff, 4)));
					afi.putShort(RWCFile.ATTRIBUTE_PRODOS_ACCESS, KSFLUtilities.getShort(stuff, 8));
					afi.putShort(RWCFile.ATTRIBUTE_PRODOS_FILE_TYPE, KSFLUtilities.getShort(stuff, 10));
					afi.putInt(RWCFile.ATTRIBUTE_PRODOS_AUX_TYPE, KSFLUtilities.getInt(stuff, 12));
				} else if (fsyss.equals("Macintosh")) {
					//mac info (v1)
					afi.putCalendar(RWCFile.ATTRIBUTE_CREATE_DATE, macdate(KSFLUtilities.getInt(stuff, 0)));
					afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, macdate(KSFLUtilities.getInt(stuff, 4)));
					afi.putCalendar(RWCFile.ATTRIBUTE_BACKUP_DATE, macdate(KSFLUtilities.getInt(stuff, 8)));
					int att = KSFLUtilities.getInt(stuff, 12);
					afi.putBoolean(RWCFile.ATTRIBUTE_LOCKED, (att & 0x01) != 0);
					afi.putBoolean(RWCFile.ATTRIBUTE_PROTECTED, (att & 0x02) != 0);
				} else if (fsyss.equals("MS-DOS")) {
					//msdos info (v1)
					afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, msdosdate(KSFLUtilities.getInt(stuff, 0)));
					afi.putShort(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES, KSFLUtilities.getShort(stuff, 4));
				} else if (fsyss.equals("Unix")) {
					//unix info (v1)
					afi.putCalendar(RWCFile.ATTRIBUTE_CREATE_DATE, unixdate(KSFLUtilities.getInt(stuff, 0)));
					afi.putCalendar(RWCFile.ATTRIBUTE_ACCESS_DATE, unixdate(KSFLUtilities.getInt(stuff, 4)));
					afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, unixdate(KSFLUtilities.getInt(stuff, 8)));
				} else {
					throw new IOException();
				}
				break;
			case 8: //dates
				afi.putCalendar(RWCFile.ATTRIBUTE_CREATE_DATE, asdate(KSFLUtilities.getInt(stuff, 0)));
				afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, asdate(KSFLUtilities.getInt(stuff, 4)));
				afi.putCalendar(RWCFile.ATTRIBUTE_BACKUP_DATE, asdate(KSFLUtilities.getInt(stuff, 8)));
				afi.putCalendar(RWCFile.ATTRIBUTE_ACCESS_DATE, asdate(KSFLUtilities.getInt(stuff, 12)));
				break;
			case 9: //finder info
				out = new RandomAccessFile(wc.getFinderInfo(), "rwd");
				out.setLength(0);
				out.write(stuff);
				out.close();
				break;
			case 10: //mac info (v2)
				int att = KSFLUtilities.getInt(stuff, 0);
				afi.putBoolean(RWCFile.ATTRIBUTE_LOCKED, (att & 0x01) != 0);
				afi.putBoolean(RWCFile.ATTRIBUTE_PROTECTED, (att & 0x02) != 0);
				break;
			case 11: //prodos info (v2)
				afi.putShort(RWCFile.ATTRIBUTE_PRODOS_ACCESS, KSFLUtilities.getShort(stuff, 0));
				afi.putShort(RWCFile.ATTRIBUTE_PRODOS_FILE_TYPE, KSFLUtilities.getShort(stuff, 2));
				afi.putInt(RWCFile.ATTRIBUTE_PRODOS_AUX_TYPE, KSFLUtilities.getInt(stuff, 4));
				break;
			case 12: //msdos info (v2)
				afi.putShort(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES, KSFLUtilities.getShort(stuff, 0));
				break;
			case 13: //afp short name
				afi.putString(RWCFile.ATTRIBUTE_AFP_NAME, ns(stuff, "MACROMAN"));
				break;
			case 14: //afp info
				afi.putInt(RWCFile.ATTRIBUTE_AFP_ATTRIBUTES, KSFLUtilities.getInt(stuff, 0));
				break;
			case 15: //afp dir id
				afi.putInt(RWCFile.ATTRIBUTE_AFP_DIRECTORY_ID, KSFLUtilities.getInt(stuff, 0));
				break;
			default:
				throw new IOException();
			}
		}
		raf.close();
	}

	protected static void encode(File f, RWCFile wc, int magic, boolean ignoreData) throws IOException {
		AttributeFile afi = wc.getAttributeHandle();
		RandomAccessFile in;
		RandomAccessFile raf = new RandomAccessFile(f, "rwd");
		raf.setLength(0);
		raf.writeInt(magic);
		int v = 0;
		String fsyss = null;
		if (wc.getAttributeHandle().containsKey(RWCFile.ATTRIBUTE_FILE_SYSTEM)) {
			v = 1;
			fsyss = wc.getAttributeHandle().getString(RWCFile.ATTRIBUTE_FILE_SYSTEM);
		} else {
			v = 2;
		}
		raf.writeShort(v);
		raf.writeShort(0);
		byte[] fsys = new byte[16];
		if (fsyss != null) {
			byte[] bb = gb(fsyss, "US-ASCII");
			int i=0;
			for (; i<bb.length && i<fsys.length; i++) fsys[i] = bb[i];
			for (; i<fsys.length; i++) fsys[i] = ' ';
		}
		raf.write(fsys);
		int n = 0;
		if (!ignoreData && wc.getDataFork().exists()) n++; //1
		if (wc.getResourceFork().exists()) n++; //2
		if (wc.getFinderComment().exists()) n++; //4
		if (wc.getFinderIcon().exists()) n++; //5
		if (wc.getFinderColorIcon().exists()) n++; //6
		if (wc.getFinderInfo().exists()) n++; //9
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_NAME)) n++; //13
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_ATTRIBUTES)) n++; //14
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_DIRECTORY_ID)) n++; //15
		if (v == 1) {
			if (fsyss.equals("ProDOS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_NAME)) n++; //3
				if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_ACCESS)) n++; //7
			}
			else if (fsyss.equals("Macintosh")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_MACOS_NAME)) n++; //3
				if (afi.containsKey(RWCFile.ATTRIBUTE_PROTECTED)) n++; //7
			}
			else if (fsyss.equals("MS-DOS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_MSDOS_NAME)) n++; //3
				if (afi.containsKey(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES)) n++; //7
			}
			else if (fsyss.equals("Unix")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_UNIX_NAME)) n++; //3
				if (afi.containsKey(RWCFile.ATTRIBUTE_ACCESS_DATE)) n++; //7
			}
			else if (fsyss.equals("VAX VMS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_VMS_NAME)) n++; //3
			}
			else {
				if (afi.containsKey(RWCFile.ATTRIBUTE_DISPLAY_NAME)) n++; //3
			}
		} else if (v == 2) {
			if (afi.containsKey(RWCFile.ATTRIBUTE_DISPLAY_NAME)) n++; //3
			if (afi.containsKey(RWCFile.ATTRIBUTE_BACKUP_DATE)) n++; //8
			if (afi.containsKey(RWCFile.ATTRIBUTE_PROTECTED)) n++; //10
			if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_ACCESS)) n++; //11
			if (afi.containsKey(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES)) n++; //12
		}
		raf.writeShort(n);
		int ofst = (int)raf.getFilePointer() + 12*n;
		if (wc.getFinderComment().exists()) {
			in = new RandomAccessFile(wc.getFinderComment(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(4);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		if (wc.getFinderIcon().exists()) {
			in = new RandomAccessFile(wc.getFinderIcon(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(5);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		if (wc.getFinderColorIcon().exists()) {
			in = new RandomAccessFile(wc.getFinderColorIcon(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(6);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		if (wc.getFinderInfo().exists()) {
			in = new RandomAccessFile(wc.getFinderInfo(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(9);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		if (v == 1) {
			if (fsyss.equals("ProDOS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_PRODOS_NAME);
					byte[] nb = gb(name, "MACROMAN");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
				if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_ACCESS)) {
					Calendar c = afi.getCalendar(RWCFile.ATTRIBUTE_CREATE_DATE);
					Calendar m = afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE);
					short acc = afi.getShort(RWCFile.ATTRIBUTE_PRODOS_ACCESS);
					short ft = afi.getShort(RWCFile.ATTRIBUTE_PRODOS_FILE_TYPE);
					int at = afi.getInt(RWCFile.ATTRIBUTE_PRODOS_AUX_TYPE);
					raf.writeInt(7);
					raf.writeInt(ofst);
					raf.writeInt(16);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.writeInt(prodosdate(c));
					raf.writeInt(prodosdate(m));
					raf.writeShort(acc);
					raf.writeShort(ft);
					raf.writeInt(at);
					raf.seek(sav);
					ofst += 16;
				}
			}
			else if (fsyss.equals("Macintosh")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_MACOS_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_MACOS_NAME);
					byte[] nb = gb(name, "MACROMAN");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
				if (afi.containsKey(RWCFile.ATTRIBUTE_PROTECTED)) {
					Calendar c = afi.getCalendar(RWCFile.ATTRIBUTE_CREATE_DATE);
					Calendar m = afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE);
					Calendar b = afi.getCalendar(RWCFile.ATTRIBUTE_BACKUP_DATE);
					boolean lck = afi.getBoolean(RWCFile.ATTRIBUTE_LOCKED);
					boolean prt = afi.getBoolean(RWCFile.ATTRIBUTE_PROTECTED);
					int att = (lck?0x01:0) | (prt?0x02:0);
					raf.writeInt(7);
					raf.writeInt(ofst);
					raf.writeInt(16);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.writeInt(macdate(c));
					raf.writeInt(macdate(m));
					raf.writeInt(macdate(b));
					raf.writeInt(att);
					raf.seek(sav);
					ofst += 16;
				}
			}
			else if (fsyss.equals("MS-DOS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_MSDOS_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_MSDOS_NAME);
					byte[] nb = gb(name, "CP734");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
				if (afi.containsKey(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES)) {
					Calendar m = afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE);
					short att = afi.getShort(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES);
					raf.writeInt(7);
					raf.writeInt(ofst);
					raf.writeInt(6);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.writeInt(msdosdate(m));
					raf.writeShort(att);
					raf.seek(sav);
					ofst += 6;
				}
			}
			else if (fsyss.equals("Unix")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_UNIX_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_UNIX_NAME);
					byte[] nb = gb(name, "UTF-8");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
				if (afi.containsKey(RWCFile.ATTRIBUTE_ACCESS_DATE)) {
					Calendar c = afi.getCalendar(RWCFile.ATTRIBUTE_CREATE_DATE);
					Calendar a = afi.getCalendar(RWCFile.ATTRIBUTE_ACCESS_DATE);
					Calendar m = afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE);
					raf.writeInt(7);
					raf.writeInt(ofst);
					raf.writeInt(12);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.writeInt(unixdate(c));
					raf.writeInt(unixdate(a));
					raf.writeInt(unixdate(m));
					raf.seek(sav);
					ofst += 12;
				}
			}
			else if (fsyss.equals("VAX VMS")) {
				if (afi.containsKey(RWCFile.ATTRIBUTE_VMS_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_VMS_NAME);
					byte[] nb = gb(name, "UTF-8");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
			}
			else {
				if (afi.containsKey(RWCFile.ATTRIBUTE_DISPLAY_NAME)) {
					String name = afi.getString(RWCFile.ATTRIBUTE_DISPLAY_NAME);
					byte[] nb = gb(name, "UTF-8");
					raf.writeInt(3);
					raf.writeInt(ofst);
					raf.writeInt(nb.length);
					long sav = raf.getFilePointer();
					raf.seek(ofst);
					raf.write(nb);
					raf.seek(sav);
					ofst += nb.length;
				}
			}
		} else if (v == 2) {
			if (afi.containsKey(RWCFile.ATTRIBUTE_DISPLAY_NAME)) {
				String name = afi.getString(RWCFile.ATTRIBUTE_DISPLAY_NAME);
				byte[] nb = gb(name, "UTF-8");
				raf.writeInt(3);
				raf.writeInt(ofst);
				raf.writeInt(nb.length);
				long sav = raf.getFilePointer();
				raf.seek(ofst);
				raf.write(nb);
				raf.seek(sav);
				ofst += nb.length;
			}
			if (afi.containsKey(RWCFile.ATTRIBUTE_BACKUP_DATE)) {
				Calendar c = afi.getCalendar(RWCFile.ATTRIBUTE_CREATE_DATE);
				Calendar m = afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE);
				Calendar b = afi.getCalendar(RWCFile.ATTRIBUTE_BACKUP_DATE);
				Calendar a = afi.getCalendar(RWCFile.ATTRIBUTE_ACCESS_DATE);
				raf.writeInt(8);
				raf.writeInt(ofst);
				raf.writeInt(16);
				long sav = raf.getFilePointer();
				raf.seek(ofst);
				raf.writeInt(asdate(c));
				raf.writeInt(asdate(m));
				raf.writeInt(asdate(b));
				raf.writeInt(asdate(a));
				raf.seek(sav);
				ofst += 16;
			}
			if (afi.containsKey(RWCFile.ATTRIBUTE_PROTECTED)) {
				boolean lck = afi.getBoolean(RWCFile.ATTRIBUTE_LOCKED);
				boolean prt = afi.getBoolean(RWCFile.ATTRIBUTE_PROTECTED);
				int att = (lck?0x01:0) | (prt?0x02:0);
				raf.writeInt(10);
				raf.writeInt(ofst);
				raf.writeInt(4);
				long sav = raf.getFilePointer();
				raf.seek(ofst);
				raf.writeInt(att);
				raf.seek(sav);
				ofst += 4;
			}
			if (afi.containsKey(RWCFile.ATTRIBUTE_PRODOS_ACCESS)) {
				short acc = afi.getShort(RWCFile.ATTRIBUTE_PRODOS_ACCESS);
				short ft = afi.getShort(RWCFile.ATTRIBUTE_PRODOS_FILE_TYPE);
				int at = afi.getInt(RWCFile.ATTRIBUTE_PRODOS_AUX_TYPE);
				raf.writeInt(11);
				raf.writeInt(ofst);
				raf.writeInt(8);
				long sav = raf.getFilePointer();
				raf.seek(ofst);
				raf.writeShort(acc);
				raf.writeShort(ft);
				raf.writeInt(at);
				raf.seek(sav);
				ofst += 8;
			}
			if (afi.containsKey(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES)) {
				short att = afi.getShort(RWCFile.ATTRIBUTE_FAT_ATTRIBUTES);
				raf.writeInt(12);
				raf.writeInt(ofst);
				raf.writeInt(2);
				long sav = raf.getFilePointer();
				raf.seek(ofst);
				raf.writeShort(att);
				raf.seek(sav);
				ofst += 2;
			}
		}
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_NAME)) {
			String afpn = afi.getString(RWCFile.ATTRIBUTE_AFP_NAME);
			byte[] afpnb = gb(afpn, "MACROMAN");
			raf.writeInt(13);
			raf.writeInt(ofst);
			raf.writeInt(afpnb.length);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(afpnb);
			raf.seek(sav);
			ofst += afpnb.length;
		}
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_ATTRIBUTES)) {
			int att = afi.getInt(RWCFile.ATTRIBUTE_AFP_ATTRIBUTES);
			raf.writeInt(14);
			raf.writeInt(ofst);
			raf.writeInt(4);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.writeInt(att);
			raf.seek(sav);
			ofst += 4;
		}
		if (afi.containsKey(RWCFile.ATTRIBUTE_AFP_DIRECTORY_ID)) {
			int dirid = afi.getInt(RWCFile.ATTRIBUTE_AFP_DIRECTORY_ID);
			raf.writeInt(15);
			raf.writeInt(ofst);
			raf.writeInt(4);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.writeInt(dirid);
			raf.seek(sav);
			ofst += 4;
		}
		if (wc.getResourceFork().exists()) {
			in = new RandomAccessFile(wc.getResourceFork(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(2);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		if (!ignoreData && wc.getDataFork().exists()) {
			in = new RandomAccessFile(wc.getDataFork(), "r");
			long ll = in.length();
			int l = (ll > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)ll;
			byte[] stuff = new byte[l];
			in.read(stuff);
			in.close();
			raf.writeInt(1);
			raf.writeInt(ofst);
			raf.writeInt(l);
			long sav = raf.getFilePointer();
			raf.seek(ofst);
			raf.write(stuff);
			raf.seek(sav);
			ofst += l;
		}
		raf.close();
	}

	protected static int recognizes(File f, int magic, int ret) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(f, "r");
			if (raf.length() < 26) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			if (raf.readInt() != magic) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			int v = raf.readShort();
			if (v < 1 || v > 2) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			if (raf.readShort() != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			byte[] fsys = new byte[16];
			raf.read(fsys);
			if (v == 1 && !isHumanReadable(fsys)) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			if (v == 2 && !isZeroes(fsys)) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.close(); return ret;
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
			return FileCodec.DOES_NOT_RECOGNIZE;
		}
	}
}
