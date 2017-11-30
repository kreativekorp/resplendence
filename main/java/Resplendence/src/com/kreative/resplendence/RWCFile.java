package com.kreative.resplendence;

import java.io.*;
import java.net.*;
import com.kreative.ksfl.KSFLUtilities;

public class RWCFile extends File {
	private static final long serialVersionUID = 1L;

	public static final int ATTRIBUTE_FILE_SYSTEM      = KSFLUtilities.fcc("fsys");
	public static final int ATTRIBUTE_CREATE_DATE      = KSFLUtilities.fcc("kdat");
	public static final int ATTRIBUTE_MODIFY_DATE      = KSFLUtilities.fcc("mdat");
	public static final int ATTRIBUTE_CHANGE_DATE      = KSFLUtilities.fcc("cdat");
	public static final int ATTRIBUTE_ACCESS_DATE      = KSFLUtilities.fcc("adat");
	public static final int ATTRIBUTE_BACKUP_DATE      = KSFLUtilities.fcc("bdat");
	public static final int ATTRIBUTE_HFS_FILE_TYPE    = KSFLUtilities.fcc("ftyp");
	public static final int ATTRIBUTE_HFS_CREATOR      = KSFLUtilities.fcc("crea");
	public static final int ATTRIBUTE_FINDER_FLAGS     = KSFLUtilities.fcc("fndr");
	public static final int ATTRIBUTE_ICON_POSITION    = KSFLUtilities.fcc("icxy");
	public static final int ATTRIBUTE_ICON_SIZE        = KSFLUtilities.fcc("icwh");
	public static final int ATTRIBUTE_PARENT_DIRECTORY = KSFLUtilities.fcc("pdid");
	public static final int ATTRIBUTE_ICON_ID          = KSFLUtilities.fcc("icid");
	public static final int ATTRIBUTE_SCRIPT_CODE      = KSFLUtilities.fcc("scpt");
	public static final int ATTRIBUTE_FINDER_FLAGS_X   = KSFLUtilities.fcc("fndx");
	public static final int ATTRIBUTE_COMMENT_ID       = KSFLUtilities.fcc("cmid");
	public static final int ATTRIBUTE_ORIGINAL_PARENT  = KSFLUtilities.fcc("opid");
	public static final int ATTRIBUTE_PROTECTED        = KSFLUtilities.fcc("prot");
	public static final int ATTRIBUTE_LOCKED           = KSFLUtilities.fcc("lock");
	public static final int ATTRIBUTE_FILE_MODE        = KSFLUtilities.fcc("mode");
	public static final int ATTRIBUTE_INODE_NUMBER     = KSFLUtilities.fcc("ino ");
	public static final int ATTRIBUTE_ENTRY_DEVICE_ID  = KSFLUtilities.fcc("dev ");
	public static final int ATTRIBUTE_DEVICE_ID        = KSFLUtilities.fcc("rdev");
	public static final int ATTRIBUTE_LINK_NUMBER      = KSFLUtilities.fcc("nlnk");
	public static final int ATTRIBUTE_USER_ID          = KSFLUtilities.fcc("uid ");
	public static final int ATTRIBUTE_GROUP_ID         = KSFLUtilities.fcc("gid ");
	public static final int ATTRIBUTE_SIZE             = KSFLUtilities.fcc("size");
	public static final int ATTRIBUTE_BLOCK_SIZE       = KSFLUtilities.fcc("bsiz");
	public static final int ATTRIBUTE_BLOCK_COUNT      = KSFLUtilities.fcc("blox");
	public static final int ATTRIBUTE_PRODOS_ACCESS    = KSFLUtilities.fcc("pdac");
	public static final int ATTRIBUTE_PRODOS_FILE_TYPE = KSFLUtilities.fcc("pdft");
	public static final int ATTRIBUTE_PRODOS_AUX_TYPE  = KSFLUtilities.fcc("pdat");
	public static final int ATTRIBUTE_FAT_ATTRIBUTES   = KSFLUtilities.fcc("fatt");
	public static final int ATTRIBUTE_AFP_ATTRIBUTES   = KSFLUtilities.fcc("afpf");
	public static final int ATTRIBUTE_AFP_DIRECTORY_ID = KSFLUtilities.fcc("afpd");
	public static final int ATTRIBUTE_MSDOS_NAME       = KSFLUtilities.fcc("cnam");
	public static final int ATTRIBUTE_PRODOS_NAME      = KSFLUtilities.fcc("pnam");
	public static final int ATTRIBUTE_MACOS_NAME       = KSFLUtilities.fcc("mnam");
	public static final int ATTRIBUTE_LISA_NAME        = KSFLUtilities.fcc("lnam");
	public static final int ATTRIBUTE_MACOSX_NAME      = KSFLUtilities.fcc("xnam");
	public static final int ATTRIBUTE_WINDOWS_NAME     = KSFLUtilities.fcc("wnam");
	public static final int ATTRIBUTE_UNIX_NAME        = KSFLUtilities.fcc("unam");
	public static final int ATTRIBUTE_VMS_NAME         = KSFLUtilities.fcc("vnam");
	public static final int ATTRIBUTE_AFP_NAME         = KSFLUtilities.fcc("anam");
	public static final int ATTRIBUTE_DISPLAY_NAME     = KSFLUtilities.fcc("dnam");
	public static final int ATTRIBUTE_MIME_TYPE        = KSFLUtilities.fcc("mime");
	public static final int ATTRIBUTE_COMMENT          = KSFLUtilities.fcc("comm");
	
	public static RWCFile createTempRWCFile(File f) throws IOException {
		RWCFile rwctmp = null;
		while (rwctmp == null) {
			try {
				File tmp = File.createTempFile(f.getName()+"-", ".rwc");
				tmp.delete();
				rwctmp = new RWCFile(tmp);
			} catch (IOException ioe) {
				rwctmp = null;
			} catch (IllegalArgumentException iae) {
				rwctmp = null;
			}
		}
		rwctmp.deleteOnExit();
		return rwctmp;
	}
	
	public RWCFile(File f) {
		super(f.getAbsolutePath());
		if (!this.exists()) this.mkdir();
		else if (!this.isDirectory()) throw new IllegalArgumentException();
		getForkHandle();
		getMetaHandle();
		getAttributeHandle();
	}

	public RWCFile(File parent, String child) {
		super(parent, child);
		if (!this.exists()) this.mkdir();
		else if (!this.isDirectory()) throw new IllegalArgumentException();
		getForkHandle();
		getMetaHandle();
		getAttributeHandle();
	}
	
	public RWCFile(String pathname) {
		super(pathname);
		if (!this.exists()) this.mkdir();
		else if (!this.isDirectory()) throw new IllegalArgumentException();
		getForkHandle();
		getMetaHandle();
		getAttributeHandle();
	}
	
	public RWCFile(String parent, String child) {
		super(parent, child);
		if (!this.exists()) this.mkdir();
		else if (!this.isDirectory()) throw new IllegalArgumentException();
		getForkHandle();
		getMetaHandle();
		getAttributeHandle();
	}
	
	public RWCFile(URI uri) {
		super(uri);
		if (!this.exists()) this.mkdir();
		else if (!this.isDirectory()) throw new IllegalArgumentException();
		getForkHandle();
		getMetaHandle();
		getAttributeHandle();
	}
	
	public void truncate() {
		truncate(listFiles());
	}
	
	private void truncate(File[] f) {
		if (f != null) for (int i=0; i<f.length; i++) {
			truncate(f[i].listFiles());
			f[i].delete();
		}
	}
	
	public File getForkHandle() {
		File f = new File(this, "fork");
		if (!f.exists()) f.mkdir();
		else if (!f.isDirectory()) throw new IllegalArgumentException();
		return f;
	}
	
	public File getDataFork() {
		return new File(getForkHandle(), "data");
	}
	
	public File getResourceFork() {
		return new File(getForkHandle(), "rsrc");
	}
	
	public File getNamedFork(String name) {
		return new File(getForkHandle(), name);
	}
	
	public File getMetaHandle() {
		File f = new File(this, "meta");
		if (!f.exists()) f.mkdir();
		else if (!f.isDirectory()) throw new IllegalArgumentException();
		return f;
	}
	
	public File getFinderInfo() {
		return new File(getMetaHandle(), "finf");
	}
	
	public File getFinderComment() {
		return new File(getMetaHandle(), "fcmt");
	}
	
	public File getFinderIcon() {
		return new File(getMetaHandle(), "icon");
	}
	
	public File getFinderColorIcon() {
		return new File(getMetaHandle(), "cicn");
	}
	
	public File getNamedMeta(String name) {
		return new File(getMetaHandle(), name);
	}
	
	public AttributeFile getAttributeHandle() {
		AttributeFile f = new AttributeFile(this, "attr");
		if (!f.exists()) try { f.createNewFile(); } catch (IOException e) {}
		return f;
	}
}
