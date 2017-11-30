package com.kreative.resplendence.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateIfStructure extends TemplateField {
	public List<TemplateIfBranch> branches = new ArrayList<TemplateIfBranch>();
	
	public TemplateIfStructure(int type, String name) {
		super(type,name);
	}
	
	public TFEditor createEditor(TFEditorTemplate root, int indent, String tenc) {
		return new TFEditorIfStructure(indent, tenc, branches, root);
	}
}
