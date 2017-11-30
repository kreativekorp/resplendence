package com.kreative.resplendence.filecodec;

import java.io.*;
import java.text.*;
import java.util.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.util.ParameterizedCRC;

public class BinHexFileCodec implements FileCodec {
	private static final String grStr = "(This file must be converted with BinHex 4.0)";
	
	private static void check(short a, short b) throws IOException {
		if (a != b) throw new IOException("Checksum doesn't match: "+Short.toString(a)+" != "+Short.toString(b));
	}
	
	public void decode(File f, RWCFile wc) throws IOException {
		ParameterizedCRC crc = ParameterizedCRC.createCRC16XMODEM();
		File hqxf = File.createTempFile("BinHexTemp", ".xqh");
		BinHexFile hqx = new BinHexFile(hqxf, "rwd");
		hqxf.deleteOnExit();
		BufferedReader r = null;
		String l = null;
		RandomAccessFile out;
		r = new BufferedReader(new FileReader(f));
		while (true) {
			l = r.readLine();
			if (l == null) { r.close(); hqx.close(); throw new IOException(); }
			else if (l.trim().equals(grStr)) break;
		}
		while (true) {
			l = r.readLine();
			if (l == null) { r.close(); hqx.close(); throw new IOException(); }
			else if (l.trim().equals(""));
			else if (l.trim().startsWith(":")) {
				hqx.writeHQX(l);
				break;
			}
			else { r.close(); hqx.close(); throw new IOException(); }
		}
		if (!l.trim().endsWith(":")) {
			while (true) {
				l = r.readLine();
				if (l == null) { r.close(); hqx.close(); throw new IOException(); }
				else {
					hqx.writeHQX(l);
					if (l.trim().endsWith(":")) break;
				}
			}
		}
		while (true) {
			l = r.readLine();
			if (l == null) break;
			else if (l.trim().equals(""));
			else { r.close(); hqx.close(); throw new IOException(); }
		}
		r.close();
		hqx.seek(0);
		int nl = hqx.readByte() & 0xFF;
		byte[] head = new byte[nl+20];
		hqx.seek(0);
		hqx.read(head);
		if (head[nl+1] != 0) { r.close(); hqx.close(); throw new IOException(); }
		String name;
		try {
			name = new String(head, 1, nl, "MACROMAN");
		} catch (UnsupportedEncodingException uee) {
			name = new String(head, 1, nl);
		}
		wc.getAttributeHandle().putString(RWCFile.ATTRIBUTE_MACOS_NAME, name);
		out = new RandomAccessFile(wc.getFinderInfo(), "rwd");
		out.setLength(0);
		out.write(head, nl+2, 10);
		out.write(new byte[22]);
		out.close();
		hqx.seek(nl+12);
		int dl = hqx.readInt();
		int rl = hqx.readInt();
		crc.reset(); crc.update(head); check((short)crc.getValue(), hqx.readShort());
		if (dl < 0 || rl < 0 || (26+nl+dl+rl) > hqx.length()) { r.close(); hqx.close(); throw new IOException(); }
		byte[] data = new byte[dl];
		hqx.read(data);
		out = new RandomAccessFile(wc.getDataFork(), "rwd");
		out.setLength(0);
		out.write(data);
		out.close();
		crc.reset(); crc.update(data); check((short)crc.getValue(), hqx.readShort());
		if (rl > 0) {
			byte[] rsrc = new byte[rl];
			hqx.read(rsrc);
			out = new RandomAccessFile(wc.getResourceFork(), "rwd");
			out.setLength(0);
			out.write(rsrc);
			out.close();
			crc.reset(); crc.update(rsrc); check((short)crc.getValue(), hqx.readShort());
		}
		hqx.close();
		hqxf.delete();
	}

	public void encode(File f, RWCFile wc) throws IOException {
		ParameterizedCRC crc = ParameterizedCRC.createCRC16XMODEM();
		File hqxf = File.createTempFile("BinHexTemp", ".xqh");
		BinHexFile hqx = new BinHexFile(hqxf, "rwd");
		hqxf.deleteOnExit();
		RandomAccessFile in;
		String name;
		if (wc.getAttributeHandle().containsKey(RWCFile.ATTRIBUTE_MACOS_NAME)) {
			name = wc.getAttributeHandle().getString(RWCFile.ATTRIBUTE_MACOS_NAME);
		} else {
			name = f.getName();
		}
		byte[] nb;
		try {
			nb = name.getBytes("MACROMAN");
		} catch (UnsupportedEncodingException uee) {
			nb = name.getBytes();
		}
		byte[] head = new byte[20+nb.length];
		head[0] = (byte)nb.length;
		for (int i=0; i<nb.length; i++) head[i+1] = nb[i];
		head[nb.length+1] = 0;
		if (wc.getFinderInfo().exists()) {
			in = new RandomAccessFile(wc.getFinderInfo(), "r");
			in.read(head, nb.length+2, 10);
			in.close();
		}
		int dl = 0; byte[] data = new byte[0];
		int rl = 0; byte[] rsrc = new byte[0];
		if (wc.getDataFork().exists()) {
			in = new RandomAccessFile(wc.getDataFork(), "r");
			in.read(data = new byte[dl = (int)in.length()]);
			in.close();
		}
		if (wc.getResourceFork().exists()) {
			in = new RandomAccessFile(wc.getResourceFork(), "r");
			in.read(rsrc = new byte[rl = (int)in.length()]);
			in.close();
		}
		KSFLUtilities.putInt(head, head.length-8, dl);
		KSFLUtilities.putInt(head, head.length-4, rl);
		hqx.seek(0);
		hqx.write(head);
		crc.reset(); crc.update(head);
		hqx.writeShort((short)crc.getValue());
		hqx.write(data);
		crc.reset(); crc.update(data);
		hqx.writeShort((short)crc.getValue());
		hqx.write(rsrc);
		crc.reset(); crc.update(rsrc);
		hqx.writeShort((short)crc.getValue());
		hqx.seek(0);
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		w.write(grStr); w.newLine();
		boolean first = true;
		String s;
		while (true) {
			try {
				if (first) {
					s = hqx.readHQX(63);
					w.write(":");
					first = false;
				} else {
					s = hqx.readHQX(64);
					w.newLine();
				}
				w.write(s);
			} catch (IOException ioe) {
				break;
			}
		}
		w.write(":");
		w.newLine();
		w.close();
		hqx.close();
		hqxf.delete();
	}

	public String name() {
		return "BinHex";
	}

	public int recognizes(File f) {
		BufferedReader r = null;
		String l = null;
		try {
			r = new BufferedReader(new FileReader(f));
			while (true) {
				l = r.readLine();
				if (l == null) { r.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
				else if (l.trim().equals(grStr)) break;
			}
			while (true) {
				l = r.readLine();
				if (l == null) { r.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
				else if (l.trim().equals(""));
				else if (l.trim().startsWith(":")) break;
				else { r.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			}
			if (!l.trim().endsWith(":")) {
				while (true) {
					l = r.readLine();
					if (l == null) { r.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
					else if (l.trim().endsWith(":")) break;
				}
			}
			while (true) {
				l = r.readLine();
				if (l == null) break;
				else if (l.trim().equals(""));
				else { r.close(); return FileCodec.DOES_NOT_RECOGNIZE; }
			}
			r.close();
			return FileCodec.RECOGNIZES_ONE_PART_FILE;
		} catch (IOException ioe) {
			try {
				if (r != null) r.close();
			} catch (Exception e) {}
			return FileCodec.DOES_NOT_RECOGNIZE;
		}
	}

	public void removeExtras(File f) {
		//nothing
	}
	
	protected class BinHexFile extends RandomAccessFile {
		private static final String hqxStr = "!\"#$%&'()*+,-012345689@ABCDEFGHIJKLMNPQRSTUVXYZ[`abcdefhijklmpqr";
		private int bits = 0;
		private int numbits = 0;
		private boolean repeat = false;
		private byte last = 0;
		private ArrayList<Byte> bbuf = new ArrayList<Byte>();
		private ArrayList<Character> cbuf = new ArrayList<Character>();
		public BinHexFile(File file, String mode) throws FileNotFoundException {
			super(file, mode);
		}
		public BinHexFile(String file, String mode) throws FileNotFoundException {
			super(file, mode);
		}
		public void seek(long pos) throws IOException {
			super.seek(pos);
			bits = numbits = 0;
			repeat = false;
			last = 0;
			bbuf = new ArrayList<Byte>();
			cbuf = new ArrayList<Character>();
		}
		public void writeHQX(String s) throws IOException {
			s = s.trim();
			if (s.startsWith(":")) s = s.substring(1);
			if (s.endsWith(":")) s = s.substring(0, s.length()-1);
			CharacterIterator i = new StringCharacterIterator(s);
			for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) writeHQX(ch);
		}
		public void writeHQX(char ch) throws IOException {
			if (ch >= 0 && ch <= 0x20) return;
			int v = hqxStr.indexOf(ch);
			if (v < 0 || v > 63) throw new IOException();
			bits = (bits << 6) | v;
			numbits += 6;
			if (numbits >= 8) {
				numbits -= 8;
				byte b = (byte)((bits >>> numbits) & 0xFF);
				if (repeat) {
					if (b == 0) {
						writeByte((byte)0x90);
						last = (byte)0x90;
					} else {
						int n = b & 0xFF;
						while (--n > 0) writeByte(last);
					}
					repeat = false;
				} else if (b == (byte)0x90) {
					repeat = true;
				} else {
					writeByte(b);
					last = b;
				}
			}
		}
		public String readHQX(int nc) throws IOException {
			String s = "";
			if (nc <= 0) return s;
			try {
				while (nc-- > 0) s += readHQX();
				return s;
			} catch (IOException ioe) {
				if (s.length() < 1) throw ioe;
				else return s;
			}
		}
		public char readHQX() throws IOException {
			if (!cbuf.isEmpty()) {
				return cbuf.remove(0);
			} else {
				try {
					while (bbuf.size() < 3) {
						long sav = getFilePointer();
						byte b = readByte();
						int nb = 1;
						try {
							while (nb < 256 && readByte() == b) nb++;
						} catch (IOException ioe) {}
						if (b == (byte)0x90) {
							bbuf.add((byte)0x90);
							bbuf.add((byte)0x00);
						} else {
							bbuf.add(b);
						}
						if (nb > 2) {
							bbuf.add((byte)0x90);
							bbuf.add((byte)nb);
							super.seek(sav+nb);
						} else {
							super.seek(sav+1);
						}
					}
				} catch (IOException ioe) {
					if (bbuf.isEmpty()) throw ioe;
					else while (bbuf.size() < 3) bbuf.add((byte)0);
				}
				byte b1 = bbuf.remove(0);
				byte b2 = bbuf.remove(0);
				byte b3 = bbuf.remove(0);
				int bp = ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
				cbuf.add(hqxStr.charAt((bp >>> 12) & 0x3F));
				cbuf.add(hqxStr.charAt((bp >>>  6) & 0x3F));
				cbuf.add(hqxStr.charAt((bp       ) & 0x3F));
				return  (hqxStr.charAt((bp >>> 18) & 0x3F));
			}
		}
	}
}
