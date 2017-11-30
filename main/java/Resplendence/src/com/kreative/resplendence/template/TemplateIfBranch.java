package com.kreative.resplendence.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateIfBranch extends TemplateField {
	public List<TemplateField> subfields = new ArrayList<TemplateField>();
	public TFEditor pane;
	
	public TemplateIfBranch(int type, String name) {
		super(type,name);
	}
	
	public TFEditor createEditor(int indent, String tenc) {
		return new TFEditorTemplate(indent, tenc, subfields);
	}
}
