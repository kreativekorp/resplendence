package com.kreative.resplendence.editors;

import java.awt.*;
import com.kreative.resplendence.*;

public interface ResplendenceEditor {
	public static final int DOES_NOT_RECOGNIZE = Integer.MIN_VALUE;
	public static final int REFUSE_TO_EDIT = Integer.MIN_VALUE;
	public static final int CAN_EDIT_IF_REQUESTED = -16777216;
	public static final int CAN_EDIT_IF_NO_DEFAULT = -1024576;
	public static final int DEFAULT_EDITOR = 0;
	public static final int SPECIALTY_DEFAULT_EDITOR = 1024;
	public static final int DECENT_EDITOR = 1024576;
	public static final int TEMPLATE_EDITOR = 4194304;
	public static final int PREFERRED_EDITOR = 16777216;
	public static final int ONLY_EDITOR = 1073741824;
	
	public String name();
	public String shortName();
	public Image smallIcon();
	public Image largeIcon();
	public int recognizes(ResplendenceObject ro);
	public ResplendenceEditorWindow openEditor(ResplendenceObject ro);
}
