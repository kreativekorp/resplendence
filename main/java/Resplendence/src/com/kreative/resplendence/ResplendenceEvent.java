package com.kreative.resplendence;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ResplendenceEvent extends ActionEvent {
	private static final long serialVersionUID = 1L;
	
	public static final int FIRST_ID = 0;
	public static final int SUPPORTS_EVENT = 0;
	public static final int GET_FONT = 1;
	public static final int SET_FONT = 2;
	public static final int GET_COLOR = 3;
	public static final int SET_COLOR = 4;
	public static final int GET_SELECTED_TEXT = 5;
	public static final int SET_SELECTED_TEXT = 6;
	public static final int INSERT_TEXT = 29;
	public static final int SAVE = 7;
	public static final int REVERT = 8;
	public static final int PRINT = 9;
	public static final int UNDO = 10;
	public static final int CUT = 11;
	public static final int COPY = 12;
	public static final int PASTE = 13;
	public static final int CLEAR = 14;
	public static final int SELECT_ALL = 15;
	public static final int MERGE = 16;
	public static final int IMPORT_FILE = 17;
	public static final int EXPORT_FILE = 18;
	public static final int GET_TEXT_COMPONENT = 19;
	public static final int GET_SELECTED_DATA = 20;
	public static final int SET_SELECTED_DATA = 21;
	public static final int GET_SELECTED_IMAGE = 22;
	public static final int SET_SELECTED_IMAGE = 23;
	public static final int NEW_ITEM = 24;
	public static final int DUPLICATE_ITEM = 25;
	public static final int REMOVE_ITEM = 26;
	public static final int ITEM_INFO = 27;
	public static final int REARRANGE = 28;
	public static final int GET_CONTROL_PROCID = 30;
	public static final int SET_CONTROL_PROCID = 31;
	public static final int GET_MENU_PROCID = 32;
	public static final int SET_MENU_PROCID = 33;
	public static final int GET_WINDOW_PROCID = 34;
	public static final int SET_WINDOW_PROCID = 35;
	public static final int GET_LIST_PROCID = 36;
	public static final int SET_LIST_PROCID = 37;
	public static final int GET_MENUBAR_PROCID = 38;
	public static final int SET_MENUBAR_PROCID = 39;
	public static final int GET_PRINTER_PROCID = 40;
	public static final int SET_PRINTER_PROCID = 41;
	public static final int GET_PAINT_SETTINGS = 42;
	public static final int SET_PAINT_SETTINGS = 43;
	public static final int SAVE_AS = 44;
	public static final int SAVE_A_COPY = 45;
	public static final int PAGE_SETUP = 46;
	public static final int GO_TO = 47;
	public static final int FIND = 48;
	public static final int FIND_AGAIN = 49;
	public static final int REPLACE_FIND_AGAIN = 50;
	public static final int REPLACE_ALL = 51;
	public static final int PASTE_AFTER = 52;
	public static final int GET_SELECTED_RESPL_OBJECT = 53;
	public static final int LIST_VIEW = 54;
	public static final int THUMBNAIL_VIEW = 55;
	public static final int ARRANGE_BY = 56;
	public static final int GET_TEXT_ENCODING = 57;
	public static final int SET_TEXT_ENCODING = 58;
	public static final int GET_COLOR_SCHEME = 59;
	public static final int SET_COLOR_SCHEME = 60;
	public static final int GET_BYTES_PER_ROW = 61;
	public static final int SET_BYTES_PER_ROW = 62;
	public static final int GET_MINISCREEN_SIZE = 63;
	public static final int SET_MINISCREEN_SIZE = 64;
	public static final int GOING_DOWN = 65;
	public static final int REFRESH = 66;
	public static final int LAST_ID = 66;
	
	private Object thing;
	
	public ResplendenceEvent(Object src, int id, String cmd, Object obj) {
		super(src,id,cmd);
		thing=obj;
	}
	
	public Object getObject() {
		return thing;
	}
	
	public Font getFont() {
		if (thing instanceof Font) return (Font)thing;
		else return null;
	}
	
	public Color getColor() {
		if (thing instanceof Color) return (Color)thing;
		else return null;
	}
	
	public String getString() {
		if (thing instanceof String) return (String)thing;
		else return null;
	}
	
	public byte[] getData() {
		if (thing instanceof byte[]) return (byte[])thing;
		else return null;
	}
	
	public Image getImage() {
		if (thing instanceof Image) return (Image)thing;
		else return null;
	}
	
	public PaintSettings getPaintSettings() {
		if (thing instanceof PaintSettings) return (PaintSettings)thing;
		else return null;
	}
	
	public byte getByte() {
		if (thing instanceof Number) return ((Number)thing).byteValue();
		else return 0;
	}
	
	public short getShort() {
		if (thing instanceof Number) return ((Number)thing).shortValue();
		else return 0;
	}
	
	public int getInt() {
		if (thing instanceof Number) return ((Number)thing).intValue();
		else return 0;
	}
	
	public long getLong() {
		if (thing instanceof Number) return ((Number)thing).longValue();
		else return 0;
	}
	
	public float getFloat() {
		if (thing instanceof Number) return ((Number)thing).floatValue();
		else return 0;
	}
	
	public double getDouble() {
		if (thing instanceof Number) return ((Number)thing).doubleValue();
		else return 0;
	}
	
	public Number getNumber() {
		if (thing instanceof Number) return (Number)thing;
		else return null;
	}
	
	public File getFile() {
		if (thing instanceof File) return (File)thing;
		else return null;
	}
}
