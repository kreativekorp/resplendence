package com.kreative.resplendence.filecodec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.kreative.resplendence.RWCFile;

public class BasiliskIIFileCodec implements FileCodec {
	private File getRsrcFile(File f, boolean mkdir) {
		File res = new File(f.getParentFile(), ".rsrc");
		if (!res.exists() && mkdir) res.mkdir();
		return new File(res, f.getName());
	}
	
	private File getFinfFile(File f, boolean mkdir) {
		File res = new File(f.getParentFile(), ".finf");
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
		}
		File i = getFinfFile(f, false);
		if (i != null && i.exists()) {
			copyFile(i, wc.getFinderInfo());
		}
	}

	public void encode(File f, RWCFile wc) throws IOException {
		if (wc.getDataFork().exists()) {
			copyFile(wc.getDataFork(), f);
		} else {
			new FileOutputStream(f).close();
		}
		File r = getRsrcFile(f, true);
		if (wc.getResourceFork().exists()) {
			copyFile(wc.getResourceFork(), r);
		} else if (r.exists()) {
			r.delete();
		}
		File i = getFinfFile(f, true);
		if (wc.getFinderInfo().exists()) {
			copyFile(wc.getFinderInfo(), i);
		} else if (i.exists()) {
			i.delete();
		}
	}

	public String name() {
		return "Basilisk II";
	}

	public int recognizes(File f) {
		File r = getRsrcFile(f, false);
		File i = getFinfFile(f, false);
		if ((r != null && r.exists()) || (i != null && i.exists())) return FileCodec.RECOGNIZES_MULTIPART_FILE;
		else return FileCodec.DOES_NOT_RECOGNIZE;
	}

	public void removeExtras(File f) {
		File r = getRsrcFile(f,false);
		if (r != null && r.exists()) r.delete();
		File i = getFinfFile(f,false);
		if (i != null && i.exists()) i.delete();
		File res = new File(f.getParentFile(), ".rsrc");
		if (res != null && res.exists() && res.isDirectory() && (res.listFiles() == null || res.listFiles().length < 1)) res.delete();
		File inf = new File(f.getParentFile(), ".finf");
		if (inf != null && inf.exists() && inf.isDirectory() && (inf.listFiles() == null || inf.listFiles().length < 1)) inf.delete();
	}
}
