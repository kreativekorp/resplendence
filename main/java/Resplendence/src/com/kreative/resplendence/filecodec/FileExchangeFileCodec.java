package com.kreative.resplendence.filecodec;

import java.io.*;
import java.nio.channels.*;
import com.kreative.resplendence.*;

public class FileExchangeFileCodec implements FileCodec {
	private File getRsrcFile(File f, boolean mkdir) {
		File res = new File(f.getParentFile(), "RESOURCE.FRK");
		if (!res.exists() && mkdir) res.mkdir();
		return new File(res, f.getName());
	}
	
	private void copyFile(File src, File dest) throws IOException {
		dest.createNewFile();
		if (!src.exists()) return;
		FileInputStream srcin = new FileInputStream(src);
		FileOutputStream destout = new FileOutputStream(dest);
		FileChannel srcc = srcin.getChannel();
		FileChannel destc = destout.getChannel();
		destc.transferFrom(srcc, 0, srcc.size());
		srcc.close();
		destc.close();
		srcin.close();
		destout.close();
	}
	
	public void decode(File f, RWCFile wc) throws IOException {
		copyFile(f, wc.getDataFork());
		File r = getRsrcFile(f, false);
		if (r != null && r.exists()) {
			copyFile(r, wc.getResourceFork());
			RandomAccessFile raf = new RandomAccessFile(r, "r");
			byte[] stuff = new byte[32];
			RandomAccessFile out;
			raf.seek(0x30);
			raf.read(stuff);
			String name;
			try {
				name = new String(stuff, 1, (stuff[0]<0)?0:(stuff[0]<32)?stuff[0]:31, "MACROMAN");
			} catch (UnsupportedEncodingException uee) {
				name = new String(stuff, 1, (stuff[0]<0)?0:(stuff[0]<32)?stuff[0]:31);
			}
			wc.getAttributeHandle().putString(RWCFile.ATTRIBUTE_MACOS_NAME, name);
			raf.seek(0x52);
			raf.read(stuff);
			out = new RandomAccessFile(wc.getFinderInfo(), "rwd");
			out.setLength(0);
			out.write(stuff);
			out.close();
			raf.close();
		}
	}

	public void encode(File f, RWCFile wc) throws IOException {
		if (wc.getDataFork().exists()) {
			copyFile(wc.getDataFork(), f);
		} else {
			new FileOutputStream(f).close();
		}
		File r = getRsrcFile(f, true);
		if (r != null) {
			if (wc.getResourceFork().exists()) {
				copyFile(wc.getResourceFork(), r);
			}
			RandomAccessFile raf = new RandomAccessFile(r, "rwd");
			String s;
			if (wc.getAttributeHandle().containsKey(RWCFile.ATTRIBUTE_MACOS_NAME)) {
				s = wc.getAttributeHandle().getString(RWCFile.ATTRIBUTE_MACOS_NAME);
			} else {
				s = f.getName();
			}
			byte[] bs;
			try {
				bs = s.getBytes("MACROMAN");
			} catch (UnsupportedEncodingException uee) {
				bs = s.getBytes();
			}
			if (bs.length > 31) {
				byte[] tmp = new byte[31];
				for (int i=0; i<tmp.length && i<bs.length; i++) tmp[i] = bs[i];
				bs = tmp;
			}
			raf.seek(0x30);
			raf.writeByte(bs.length);
			raf.write(bs);
			if (wc.getFinderInfo().exists()) {
				byte[] stuff = new byte[32];
				RandomAccessFile in = new RandomAccessFile(wc.getFinderInfo(), "r");
				in.read(stuff);
				in.close();
				raf.seek(0x50);
				raf.writeShort(0x0200);
				raf.write(stuff);
			} else {
				raf.seek(0x50);
				raf.writeShort(0x0200);
				raf.write(new byte[32]);
			}
			raf.close();
		}
	}

	public String name() {
		return "File Exchange";
	}

	public int recognizes(File f) {
		File r = getRsrcFile(f, false);
		if (r == null || !r.exists()) return FileCodec.DOES_NOT_RECOGNIZE;
		else return FileCodec.RECOGNIZES_MULTIPART_FILE;
	}
	
	public void removeExtras(File f) {
		File r = getRsrcFile(f,false);
		if (r != null && r.exists()) r.delete();
		File res = new File(f.getParentFile(), "RESOURCE.FRK");
		if (res != null && res.exists() && res.isDirectory() && (res.listFiles() == null || res.listFiles().length < 1)) res.delete();
	}
}
