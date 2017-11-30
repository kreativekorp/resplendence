package com.kreative.resplendence.menus;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public interface MenuConstants {
	public static final int META_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	public static final int ALT_MASK = (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() == KeyEvent.ALT_MASK)?KeyEvent.CTRL_MASK:KeyEvent.ALT_MASK;
	public static final int SHIFT_MASK = KeyEvent.SHIFT_MASK;
	public static final int META_ALT_MASK = META_MASK | ALT_MASK;
	public static final int META_SHIFT_MASK = META_MASK | SHIFT_MASK;
	public static final int META_ALT_SHIFT_MASK = META_MASK | ALT_MASK | SHIFT_MASK;
	
	public static final KeyStroke KEYSTROKE_UNDO = KeyStroke.getKeyStroke('Z', META_MASK);
	public static final KeyStroke KEYSTROKE_CUT = KeyStroke.getKeyStroke('X', META_MASK);
	public static final KeyStroke KEYSTROKE_COPY = KeyStroke.getKeyStroke('C', META_MASK);
	public static final KeyStroke KEYSTROKE_PASTE = KeyStroke.getKeyStroke('V', META_MASK);
	public static final KeyStroke KEYSTROKE_PASTE_AFTER = KeyStroke.getKeyStroke('V', META_SHIFT_MASK);
	
	public static final long MENUS_GLOBAL          = 0x0000000000000001L;
	public static final long MENUS_SAVE_REVERT     = 0x0000000000000002L;
	public static final long MENUS_PRINT           = 0x0000000000000008L;
	public static final long MENUS_IMPORT_EXPORT   = 0x0000000000000010L;
	public static final long MENUS_UNDO            = 0x0000000000000020L;
	public static final long MENUS_CUT_COPY_PASTE  = 0x0000000000000040L;
	public static final long MENUS_NEW_ITEM        = 0x0000000000000080L;
	public static final long MENUS_DUPLICATE_ITEM  = 0x0000000000000100L;
	public static final long MENUS_REMOVE_ITEM     = 0x0000000000000200L;
	public static final long MENUS_ITEM_INFO       = 0x0000000000000400L;
	public static final long MENUS_REARRANGE       = 0x0000000000000800L;
	public static final long MENUS_FORMAT          = 0x0000000000001000L;
	public static final long MENUS_COLOR           = 0x0000000000002000L;
	public static final long MENUS_FIND_REPLACE    = 0x0000000000004000L;
	public static final long MENUS_TEXT_FILTERS    = 0x0000000000008000L;
	public static final long MENUS_DATA_FILTERS    = 0x0000000000010000L;
	public static final long MENUS_IMAGE_FILTERS   = 0x0000000000020000L;
	public static final long MENUS_OPEN_ITEM       = 0x0000000000040000L;
	public static final long MENUS_SELECT_ALL      = 0x0000000000080000L;
	public static final long MENUS_GO_TO           = 0x0000000000100000L;
	public static final long MENUS_REFRESH         = 0x0000000000200000L;
	public static final long MENUS_LIST_THUMB_VIEW = 0x0000000100000000L;
	public static final long MENUS_ARRANGE_BY_NAME = 0x0000000200000000L;
	public static final long MENUS_ARRANGE_BY_NUM  = 0x0000000400000000L;
	public static final long MENUS_ARRANGE_BY_SIZE = 0x0000000800000000L;
	public static final long MENUS_ARRANGE_BY_KIND = 0x0000001000000000L;
	public static final long MENUS_TEXT_ENCODING   = 0x0000010000000000L;
	public static final long MENUS_COLOR_SCHEME    = 0x0000020000000000L;
	public static final long MENUS_BYTES_PER_ROW   = 0x0000040000000000L;
	public static final long MENUS_MINISCREEN      = 0x0000080000000000L;
}
