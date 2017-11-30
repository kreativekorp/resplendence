package com.kreative.resplendence;

import java.io.*;

public class FSObject extends ResplendenceObject {
	private File f;
	
	public FSObject(File f) {
		this.f = f;
	}
	
	@Override
	public boolean addChild(ResplendenceObject rn) {
		if (!f.isDirectory()) return false;
		String s = rn.getTitleForExportedFile();
		if (s == null || s.length() < 1) s = rn.toString();
		File cf = new File(f,s);
		if (rn.isDataType()) {
			try {
				RandomAccessFile raf = new RandomAccessFile(cf,"rwd");
				raf.seek(0);
				raf.setLength(0);
				raf.write(rn.getData());
				raf.close();
				return true;
			} catch (IOException ioe) {
				return false;
			}
		} else if (rn.isContainerType()) {
			boolean ret = false;
			cf.mkdirs();
			FSObject cfs = new FSObject(cf);
			ResplendenceObject[] rns = rn.getChildren();
			for (int i=0; i<rns.length; i++) ret = ret || cfs.addChild(rns[i]);
			return ret;
		}
		return false;
	}

	@Override
	public ResplendenceObject getChild(int i) {
		ResplendenceObject ro = new FSObject(f.listFiles()[i]);
		return ro;
	}

	@Override
	public int getChildCount() {
		return f.list().length;
	}

	@Override
	public ResplendenceObject[] getChildren() {
		File[] lf = f.listFiles();
		ResplendenceObject[] ro = new ResplendenceObject[lf.length];
		for (int i=0; i<lf.length; i++) {
			ro[i] = new FSObject(lf[i]);
		}
		return ro;
	}

	@Override
	public byte[] getData() {
		try {
			RandomAccessFile raf = new RandomAccessFile(f,"r");
			raf.seek(0);
			int l = (int)Math.min(raf.length(), Integer.MAX_VALUE);
			byte[] b = new byte[l];
			raf.read(b);
			raf.close();
			return b;
		} catch (IOException ioe) {
			return null;
		}
	}

	@Override
	public File getNativeFile() {
		return f;
	}
	
	@Override
	public Object getProperty(String key) {
		try {
			if (key.equals("readable")) {
				return f.canRead();
			} else if (key.equals("writable")) {
				return f.canWrite();
			} else if (key.equals("directory")) {
				return f.isDirectory();
			} else if (key.equals("file")) {
				return f.isFile();
			} else if (key.equals("hidden")) {
				return f.isHidden();
			} else if (key.equals("absolute")) {
				return f.isAbsolute();
			} else if (key.equals("exists")) {
				return f.exists();
			} else if (key.equals("length")) {
				return f.length();
			} else if (key.equals("modified")) {
				return f.lastModified();
			} else if (key.equals("absolutefile")) {
				return f.getAbsoluteFile();
			} else if (key.equals("absolutefsobject")) {
				return new FSObject(f.getAbsoluteFile());
			} else if (key.equals("absolutepath")) {
				return f.getAbsolutePath();
			} else if (key.equals("canonicalfile")) {
				return f.getCanonicalFile();
			} else if (key.equals("canonicalfsobject")) {
				return new FSObject(f.getCanonicalFile());
			} else if (key.equals("canonicalpath")) {
				return f.getCanonicalPath();
			} else if (key.equals("name")) {
				return f.getName();
			} else if (key.equals("parent")) {
				return f.getParent();
			} else if (key.equals("parentfile")) {
				return f.getParentFile();
			} else if (key.equals("parentfsobject")) {
				return new FSObject(f.getParentFile());
			} else if (key.equals("path")) {
				return f.getPath();
			}
		} catch (IOException ioe) {}
		return null;
	}
	
	@Override
	public Object getProvider() {
		return null;
	}
	
	@Override
	public RandomAccessFile getRandomAccessData(String mode) {
		try {
			return new RandomAccessFile(f,mode);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public long getSize() {
		return f.length();
	}

	@Override
	public String getTitleForExportedFile() {
		return f.getName();
	}
	
	@Override
	public String getTitleForIcons() {
		return f.getName();
	}
	
	@Override
	public String getTitleForWindowMenu() {
		return f.getName();
	}

	@Override
	public String getTitleForWindows() {
		return f.getName();
	}
	
	@Override
	public String getType() {
		return TYPE_FSOBJECT;
	}
	
	@Override
	public String getUDTI() {
		return f.isDirectory()?".directory":".file";
	}

	@Override
	public RWCFile getWorkingCopy() {
		return null;
	}

	@Override
	public boolean isContainerType() {
		return f.isDirectory();
	}

	@Override
	public boolean isDataType() {
		return f.isFile();
	}

	@Override
	public ResplendenceObject removeChild(int i) {
		ResplendenceObject rt = new FSObject(f.listFiles()[i]);
		f.listFiles()[i].delete();
		return rt;
	}

	@Override
	public ResplendenceObject removeChild(ResplendenceObject ro) {
		if (!ro.getNativeFile().getParentFile().equals(f)) return null;
		ResplendenceObject rt = new FSObject(ro.getNativeFile());
		ro.getNativeFile().delete();
		return rt;
	}

	@Override
	public boolean replaceChild(int i, ResplendenceObject rn) {
		return (removeChild(i) != null) && addChild(rn);
	}

	@Override
	public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
		return (removeChild(ro) != null) && addChild(rn);
	}

	@Override
	public boolean setData(byte[] data) {
		try {
			RandomAccessFile raf = new RandomAccessFile(f,"rwd");
			raf.seek(0);
			raf.setLength(0);
			raf.write(data);
			raf.close();
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	@Override
	public boolean setProperty(String key, Object value) {
		try {
			if (key.equals("exists")) {
				if ((Boolean)value) {
					if (!f.exists()) f.createNewFile();
				} else {
					if (f.exists()) f.delete();
				}
				return true;
			} else if (key.equals("modified")) {
				f.setLastModified(((Number)value).longValue());
				return true;
			} else if (key.equals("path")) {
				return f.renameTo(value instanceof File ? (File)value : new File(value.toString()));
			}
		} catch (Exception ioe) {}
		return false;
	}
}
