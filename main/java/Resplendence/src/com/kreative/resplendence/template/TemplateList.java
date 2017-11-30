package com.kreative.resplendence.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateList extends TemplateField {
	public TemplateField counter = null;
	public List<TemplateField> subfields = new ArrayList<TemplateField>();
	
	public TemplateList(int type, String name) {
		super(type,name);
	}
	
	public TFEditor createEditor(TFEditorListCount llc, int indent, String tenc) {
		int lt = 0;
		if (TemplateList.isEOFTerminatedListType(type)) {
			lt = TFEditorList.EOF_TERMINATED;
		} else if (TemplateList.isZeroTerminatedListType(type)) {
			lt = TFEditorList.ZERO_TERMINATED;
		} else if (TemplateList.isCountedListType(type)) {
			lt = TFEditorList.COUNTED;
		} else {
			return super.createEditor(tenc);
		}
		return new TFEditorList(indent, tenc, subfields, name, lt, llc);
	}
}
