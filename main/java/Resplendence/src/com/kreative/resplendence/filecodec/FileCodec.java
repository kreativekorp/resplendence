package com.kreative.resplendence.filecodec;

import java.io.*;
import com.kreative.resplendence.*;

public interface FileCodec {
	public static final int DOES_NOT_RECOGNIZE = Integer.MIN_VALUE;
	public static final int RECOGNIZES_NATIVE = 0;
	public static final int RECOGNIZES_MULTIPART_FILE = 128;
	public static final int RECOGNIZES_ONE_PART_FILE = 256;
	
	public String name();
	public int recognizes(File f);
	public void decode(File f, RWCFile wc) throws IOException;
	public void encode(File f, RWCFile wc) throws IOException;
	public void removeExtras(File f);
}
