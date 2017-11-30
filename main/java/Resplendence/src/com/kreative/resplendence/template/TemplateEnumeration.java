package com.kreative.resplendence.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateEnumeration extends TemplateField {
	public List<TemplateField> options = new ArrayList<TemplateField>();
	
	public TemplateEnumeration(int type, String name) {
		super(type,name);
	}
	
	public TFEditor createEditor(String tenc) {
		switch (type) {
		case TYPE_ENUM_BIT_BE: return new TFEditorEnumeration(1, false, options);
		case TYPE_ENUM_NIBBLE_BE: return new TFEditorEnumeration(4, false, options);
		case TYPE_ENUM_BYTE_BE: return new TFEditorEnumeration(8, false, options);
		case TYPE_ENUM_WORD_BE: return new TFEditorEnumeration(16, false, options);
		case TYPE_ENUM_LONG_BE: return new TFEditorEnumeration(32, false, options);
		case TYPE_ENUM_LONG_LONG_BE: return new TFEditorEnumeration(64, false, options);
		case TYPE_ENUM_BIT_LE: return new TFEditorEnumeration(1, true, options);
		case TYPE_ENUM_NIBBLE_LE: return new TFEditorEnumeration(4, true, options);
		case TYPE_ENUM_BYTE_LE: return new TFEditorEnumeration(8, true, options);
		case TYPE_ENUM_WORD_LE: return new TFEditorEnumeration(16, true, options);
		case TYPE_ENUM_LONG_LE: return new TFEditorEnumeration(32, true, options);
		case TYPE_ENUM_LONG_LONG_LE: return new TFEditorEnumeration(64, true, options);
		default:
			if (((type & 0xFFFF0000) == 0x454E0000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorEnumeration(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false, options);
			else if (((type & 0x0000FFFF) == 0x00004E45) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorEnumeration(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true, options);
			else
				return super.createEditor(tenc);
		}
	}
}
