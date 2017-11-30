package com.kreative.resplendence.filecodec;

import java.io.*;
import java.nio.channels.*;
import com.kreative.resplendence.*;

public class NativeFileCodec implements FileCodec {
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
		try {
			copyFile(new File(new File(f,"..namedfork"),"rsrc"), wc.getResourceFork());
		} catch (IOException ioe) {}
		if (ResplMain.RUNNING_ON_MAC_OS) {
			try {
				RandomAccessFile out = new RandomAccessFile(wc.getFinderInfo(), "rwd");
				out.setLength(0);
				com.kreative.resplendence.mac.MacNativeIO.getAndWriteFinderInfo(f, out);
				out.close();
			} catch (Exception e) {}
			try {
				String t = com.kreative.resplendence.mac.MacNativeIO.getFinderComment(f);
				RandomAccessFile out = new RandomAccessFile(wc.getFinderComment(), "rwd");
				out.setLength(0);
				try {
					out.write(t.getBytes("MACROMAN"));
				} catch (UnsupportedEncodingException uee) {
					out.write(t.getBytes());
				}
				out.close();
			} catch (Exception e) {}
		}
	}

	public void encode(File f, RWCFile wc) throws IOException {
		copyFile(wc.getDataFork(), f);
		try {
			copyFile(wc.getResourceFork(), new File(new File(f,"..namedfork"),"rsrc"));
		} catch (IOException ioe) {}
		if (ResplMain.RUNNING_ON_MAC_OS) {
			if (wc.getFinderInfo().exists()) try {
				RandomAccessFile in = new RandomAccessFile(wc.getFinderInfo(), "r");
				com.kreative.resplendence.mac.MacNativeIO.readAndSetFinderInfo(in, f);
				in.close();
			} catch (Exception e) {}
			if (wc.getFinderComment().exists()) try {
				RandomAccessFile in = new RandomAccessFile(wc.getFinderComment(), "r");
				byte[] tb = new byte[(int)in.length()];
				in.read(tb);
				in.close();
				String t = null;
				try {
					t = new String(tb, "MACROMAN");
				} catch (UnsupportedEncodingException uee) {
					t = new String(tb);
				}
				com.kreative.resplendence.mac.MacNativeIO.setFinderComment(f, t);
			} catch (Exception e) {}
		}
	}

	public String name() {
		return "No Encoding";
	}

	public int recognizes(File f) {
		return FileCodec.RECOGNIZES_NATIVE;
	}
	
	public void removeExtras(File f) {
		//nothing
	}
}
