package com.kreative.resplendence;

import java.io.*;

public abstract class ResplendenceObject {
	public static final String TYPE_MY_COMPUTER = "__MYCOMPUTER";
	public static final String TYPE_FSOBJECT = "__FSOBJECT";
	public static final String TYPE_FORK = "__FORK";
	public static final String TYPE_META = "__META";
	public static final String TYPE_ATTR = "__ATTR";
	public static final String TYPE_MAC_RESOURCE_TYPE = "__RESTYPE";
	public static final String TYPE_MAC_RESOURCE_OBJECT = "__RESOBJECT";
	public static final String TYPE_DFF_TYPE = "__DFFTYPE";
	public static final String TYPE_DFF_OBJECT = "__DFFOBJECT";
	public static final String TYPE_PRC_HEADER = "__PRCHEADER";
	public static final String TYPE_PRC_RESOURCE_TYPE = "__PRCTYPE";
	public static final String TYPE_PRC_RESOURCE_OBJECT = "__PRCOBJECT";
	public static final String TYPE_WIN_PE_HEADER = "__WPEHEADER";
	public static final String TYPE_WIN_PE_TYPE = "__WPETYPE";
	public static final String TYPE_WIN_PE_ID = "__WPEID";
	public static final String TYPE_WIN_PE_LANGUAGE = "__WPELANG";
	public static final String TYPE_WIN_NE_HEADER = "__WNEHEADER";
	public static final String TYPE_WIN_NE_TYPE = "__WNETYPE";
	public static final String TYPE_WIN_NE_ID = "__WNEID";
	public static final String TYPE_WIN_NE_LANGUAGE = "__WNELANG";
	public static final String TYPE_CHUNK_HEADER = "__CHUNKHEADER";
	public static final String TYPE_CHUNK_OBJECT = "__CHUNKOBJECT";
	public static final String TYPE_DATABASE_SERVER = "__DBSERVER";
	public static final String TYPE_DATABASE_DATABASE = "__DBDATABASE";
	public static final String TYPE_DATABASE_TABLE = "__DBTABLE";
	public static final String TYPE_DATABASE_RECORD = "__DBRECORD";
	public static final String TYPE_DATABASE_FIELD = "__DBFIELD";
	public static final String TYPE_HEADER = "__HEADER";
	public static final String TYPE_DIRECTORY = "__DIRECTORY";
	public static final String TYPE_DATA = "__DATA";
	public static final String TYPE_TEXT = "__TEXT";
	public static final String TYPE_IMAGE = "__IMAGE";
	
	public abstract RWCFile getWorkingCopy();
	public abstract File getNativeFile();
	public abstract Object getProvider(); //i.e. ResourceProvider, DFFProvider, PRCProvider
	public abstract String getType();
	public abstract String getUDTI(); //Universal Data Type Identifier: four chars of mac resource type, eight chars for DFF type, longer strings for other things
	public abstract String getTitleForIcons();
	public abstract String getTitleForWindows();
	public abstract String getTitleForWindowMenu();
	public abstract String getTitleForExportedFile();
	
	public abstract boolean isDataType();
	public abstract byte[] getData();
	public abstract RandomAccessFile getRandomAccessData(String mode);
	public abstract boolean setData(byte[] data);
	public abstract long getSize();
	
	public abstract boolean isContainerType();
	public abstract ResplendenceObject[] getChildren();
	public abstract int getChildCount();
	public abstract ResplendenceObject getChild(int i);
	public abstract boolean addChild(ResplendenceObject rn);
	public abstract boolean replaceChild(int i, ResplendenceObject rn);
	public abstract boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn);
	public abstract ResplendenceObject removeChild(int i);
	public abstract ResplendenceObject removeChild(ResplendenceObject ro);
	
	public abstract Object getProperty(String key);
	public abstract boolean setProperty(String key, Object value);
}
