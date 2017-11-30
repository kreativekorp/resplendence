package com.kreative.resplendence.filecodec;

import java.io.*;
import java.util.*;
import com.kreative.resplendence.*;

public class MacBinaryFileCodec implements FileCodec {
	private static int ceil128(int i) {
		if ((i & 0x7F) == 0) return i;
		else return (i | 0x7F)+1;
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
	
	public void decode(File f, RWCFile wc) throws IOException {
		AttributeFile afi = wc.getAttributeHandle();
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		
		//sanity checks
		if (raf.length() < 128 || (raf.length() & 0x7F) != 0) { raf.close(); throw new IOException(); }
		raf.seek(0x00); if (raf.readByte() != 0) { raf.close(); throw new IOException(); }
		int nl = raf.readByte(); if (nl <= 0 || nl > 63) { raf.close(); throw new IOException(); }
		raf.seek(0x4A); if (raf.readByte() != 0) { raf.close(); throw new IOException(); }
		raf.seek(0x52); if (raf.readByte() != 0) { raf.close(); throw new IOException(); }
		int dl = raf.readInt(); int rl = raf.readInt();
		raf.seek(0x63); int cl = raf.readShort();
		if (dl < 0 || rl < 0  || cl < 0 || (128+dl+rl+cl) > raf.length()) { raf.close(); throw new IOException(); }
		raf.seek(0x7E); if (raf.readShort() != 0) { raf.close(); throw new IOException(); }
		
		//name
		byte[] nb = new byte[nl];
		raf.seek(0x02); raf.read(nb);
		String name;
		try {
			name = new String(nb, "MACROMAN");
		} catch (UnsupportedEncodingException uee) {
			name = new String(nb);
		}
		afi.putString(RWCFile.ATTRIBUTE_MACOS_NAME, name);
		
		//finder info
		RandomAccessFile fout = new RandomAccessFile(wc.getFinderInfo(), "rwd");
		fout.setLength(0);
		raf.seek(0x41); fout.writeInt(raf.readInt());
		raf.seek(0x45); fout.writeInt(raf.readInt());
		raf.seek(0x49); fout.writeByte(raf.readByte());
		raf.seek(0x65); fout.writeByte(raf.readByte());
		raf.seek(0x4B); fout.writeShort(raf.readShort());
		raf.seek(0x4D); fout.writeShort(raf.readShort());
		raf.seek(0x4F); fout.writeShort(raf.readShort());
		fout.writeLong(0);
		raf.seek(0x6A); fout.writeShort(raf.readShort());
		fout.writeShort(0);
		fout.writeInt(0);
		fout.close();
		
		//protected
		raf.seek(0x51); int att = raf.readByte();
		afi.putBoolean(RWCFile.ATTRIBUTE_LOCKED, (att & 0x01) != 0);
		afi.putBoolean(RWCFile.ATTRIBUTE_PROTECTED, (att & 0x02) != 0);
		
		//dates
		raf.seek(0x5B);
		afi.putCalendar(RWCFile.ATTRIBUTE_CREATE_DATE, macdate(raf.readInt()));
		raf.seek(0x5F);
		afi.putCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE, macdate(raf.readInt()));
		
		RandomAccessFile out;
		byte[] stuff;
		
		//data
		if (dl > 0) {
			stuff = new byte[dl];
			raf.seek(0x80);
			raf.read(stuff);
			out = new RandomAccessFile(wc.getDataFork(), "rwd");
			out.setLength(0);
			out.write(stuff);
			out.close();
		} else {
			new RandomAccessFile(wc.getDataFork(), "rwd").close(); 
		}
		
		//rsrc
		if (rl > 0) {
			stuff = new byte[rl];
			raf.seek(0x80 + ceil128(dl));
			raf.read(stuff);
			out = new RandomAccessFile(wc.getResourceFork(), "rwd");
			out.setLength(0);
			out.write(stuff);
			out.close();
		}
		
		//comment
		if (cl > 0) {
			stuff = new byte[cl];
			raf.seek(0x80 + ceil128(dl) + ceil128(rl));
			raf.read(stuff);
			out = new RandomAccessFile(wc.getFinderComment(), "rwd");
			out.setLength(0);
			out.write(stuff);
			out.close();
		}
		
		raf.close();
	}

	public void encode(File f, RWCFile wc) throws IOException {
		AttributeFile afi = wc.getAttributeHandle();
		RandomAccessFile raf = new RandomAccessFile(f, "rwd");
		raf.setLength(0);
		
		//constant fields
		raf.seek(0x00); raf.writeByte(0);
		raf.seek(0x4A); raf.writeByte(0);
		raf.seek(0x52); raf.writeByte(0);
		raf.seek(0x66); raf.writeInt(0x6D42494E);
		raf.seek(0x6C); raf.write(new byte[8]);
		raf.seek(0x74); raf.write(0);
		raf.seek(0x78); raf.write(0);
		raf.seek(0x7A); raf.writeByte(130);
		raf.seek(0x7B); raf.writeByte(128);
		raf.seek(0x7E); raf.writeShort(0);
		
		//name
		String name; byte[] nb;
		if (afi.containsKey(RWCFile.ATTRIBUTE_MACOS_NAME)) {
			name = afi.getString(RWCFile.ATTRIBUTE_MACOS_NAME);
		} else {
			name = f.getName();
		}
		try {
			nb = name.getBytes("MACROMAN");
		} catch (UnsupportedEncodingException uee) {
			nb = name.getBytes();
		}
		raf.seek(0x01);
		raf.writeByte(Math.min(nb.length, 63));
		raf.write(nb, 0, Math.min(nb.length, 63));
		
		//finder info
		if (wc.getFinderInfo().exists()) {
			RandomAccessFile in = new RandomAccessFile(wc.getFinderInfo(), "r");
			raf.seek(0x41); raf.writeInt(in.readInt());
			raf.seek(0x45); raf.writeInt(in.readInt());
			raf.seek(0x49); raf.writeByte(in.readByte());
			raf.seek(0x65); raf.writeByte(in.readByte());
			raf.seek(0x4B); raf.writeShort(in.readShort());
			raf.seek(0x4D); raf.writeShort(in.readShort());
			raf.seek(0x4F); raf.writeShort(in.readShort());
			in.readLong();
			raf.seek(0x6A); raf.writeShort(in.readShort());
			in.close();
		} else {
			raf.seek(0x41); raf.writeInt(0);
			raf.seek(0x45); raf.writeInt(0);
			raf.seek(0x49); raf.writeByte(0);
			raf.seek(0x65); raf.writeByte(0);
			raf.seek(0x4B); raf.writeShort(0);
			raf.seek(0x4D); raf.writeShort(0);
			raf.seek(0x4F); raf.writeShort(0);
			raf.seek(0x6A); raf.writeShort(0);
		}
		
		//protected
		boolean lck = afi.getBoolean(RWCFile.ATTRIBUTE_LOCKED);
		boolean prt = afi.getBoolean(RWCFile.ATTRIBUTE_PROTECTED);
		int att = (lck?0x01:0) | (prt?0x02:0);
		raf.seek(0x51); raf.writeByte(att);
		
		//dates
		raf.seek(0x5B);
		if (afi.containsKey(RWCFile.ATTRIBUTE_CREATE_DATE)) {
			raf.writeInt(macdate(afi.getCalendar(RWCFile.ATTRIBUTE_CREATE_DATE)));
		} else {
			raf.writeInt(0);
		}
		raf.seek(0x5F);
		if (afi.containsKey(RWCFile.ATTRIBUTE_MODIFY_DATE)) {
			raf.writeInt(macdate(afi.getCalendar(RWCFile.ATTRIBUTE_MODIFY_DATE)));
		} else {
			raf.writeInt(0);
		}
		
		long headStopsAt = 0x80;
		long dataStopsAt = 0x80;
		long rsrcStopsAt = 0x80;
		
		//data
		if (wc.getDataFork().exists()) {
			RandomAccessFile in = new RandomAccessFile(wc.getDataFork(), "r");
			int dl = (int)Math.min(in.length(), Integer.MAX_VALUE);
			byte[] stuff = new byte[dl];
			in.read(stuff);
			in.close();
			raf.seek(0x53); raf.writeInt(dl);
			raf.seek(headStopsAt); raf.write(stuff);
			if ((dl & 0x7F) != 0) {
				int n = 128-(dl & 0x7F);
				raf.write(new byte[n]);
			}
			dataStopsAt = raf.getFilePointer();
		} else {
			raf.seek(0x53); raf.writeInt(0);
			dataStopsAt = headStopsAt;
		}
		
		//rsrc
		if (wc.getResourceFork().exists()) {
			RandomAccessFile in = new RandomAccessFile(wc.getResourceFork(), "r");
			int rl = (int)Math.min(in.length(), Integer.MAX_VALUE);
			byte[] stuff = new byte[rl];
			in.read(stuff);
			in.close();
			raf.seek(0x57); raf.writeInt(rl);
			raf.seek(dataStopsAt); raf.write(stuff);
			if ((rl & 0x7F) != 0) {
				int n = 128-(rl & 0x7F);
				raf.write(new byte[n]);
			}
			rsrcStopsAt = raf.getFilePointer();
		} else {
			raf.seek(0x57); raf.writeInt(0);
			rsrcStopsAt = dataStopsAt;
		}
		
		//comment
		if (wc.getFinderComment().exists()) {
			RandomAccessFile in = new RandomAccessFile(wc.getFinderComment(), "r");
			int cl = (int)Math.min(in.length(), Short.MAX_VALUE);
			byte[] stuff = new byte[cl];
			in.read(stuff);
			in.close();
			raf.seek(0x63); raf.writeShort(cl);
			raf.seek(rsrcStopsAt); raf.write(stuff);
			if ((cl & 0x7F) != 0) {
				int n = 128-(cl & 0x7F);
				raf.write(new byte[n]);
			}
		} else {
			raf.seek(0x63); raf.writeShort(0);
		}
		
		//crc
		short crc = 0;
		raf.seek(0x00);
		for (int i=0; i<0x7E; i++) {
			byte b = raf.readByte();
			for (int j=0x80; j>0; j>>>=1) {
				boolean xor = ((crc & 0x8000) != 0);
				crc <<= 1;
				if ((b&j)>0) crc |= 1;
				if (xor) crc ^= 0x1021;
			}
		}
		raf.seek(0x7C);
		raf.writeShort(crc);
		
		raf.close();
	}

	public String name() {
		return "MacBinary";
	}

	public int recognizes(File f) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(f, "r");
			if (raf.length() < 128) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			if ((raf.length() & 0x7F) != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.seek(0x00);
			if (raf.readByte() != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			int nl = raf.readByte(); if (nl <= 0 || nl > 63) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.seek(0x4A);
			if (raf.readByte() != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.seek(0x52);
			if (raf.readByte() != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			int dl = raf.readInt(); int rl = raf.readInt();
			if (dl < 0 || rl < 0 || (128+dl+rl) > raf.length()) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.seek(0x7E);
			if (raf.readShort() != 0) { raf.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			raf.close(); return FileCodec.RECOGNIZES_ONE_PART_FILE;
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
			return FileCodec.DOES_NOT_RECOGNIZE;
		}
	}

	public void removeExtras(File f) {
		//nothing
	}
}
