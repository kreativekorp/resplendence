package com.kreative.resplendence.pickers;

import java.awt.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.menus.*;

public interface ResplendencePicker extends MenuConstants {
	public static final int DOES_NOT_RECOGNIZE = Integer.MIN_VALUE;
	
	public String name();
	public int recognizes(ResplendenceObject ro);
	public Image imageFor(ResplendenceObject ro);
}
