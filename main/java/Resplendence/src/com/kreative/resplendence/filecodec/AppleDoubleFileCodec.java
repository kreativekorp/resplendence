package com.kreative.resplendence.filecodec;

import java.io.*;
import java.nio.channels.*;
import com.kreative.resplendence.*;

public class AppleDoubleFileCodec implements FileCodec {
	// For actual encoding and decoding algorithms, see protected methods in AppleSingleFileCodec
	
	private File getHeaderFile(File f) {
		return new File(f.getParentFile(), "%"+f.getName());
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
		AppleSingleFileCodec.decode(getHeaderFile(f), wc, 0x00051607, true);
		copyFile(f, wc.getDataFork());
	}

	public void encode(File f, RWCFile wc) throws IOException {
		AppleSingleFileCodec.encode(getHeaderFile(f), wc, 0x00051607, true);
		if (wc.getDataFork().exists()) copyFile(wc.getDataFork(), f);
		else new FileOutputStream(f).close();
	}

	public String name() {
		return "AppleDouble";
	}

	public int recognizes(File f) {
		File h = getHeaderFile(f);
		if (h == null || !h.exists()) return FileCodec.DOES_NOT_RECOGNIZE;
		else return AppleSingleFileCodec.recognizes(h, 0x00051607, FileCodec.RECOGNIZES_MULTIPART_FILE);
	}
	
	public void removeExtras(File f) {
		File h = getHeaderFile(f);
		if (h != null && h.exists()) h.delete();
	}
}
