package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import com.kreative.resplendence.*;

public class TTFPicker implements ResplendencePicker {
	private static final String FONT_STRING = "Amiga Monkeys: 2369?";
	private static final float FONT_SIZE = 24;
	
	public Image imageFor(ResplendenceObject ro) {
		try {
			InputStream i = new ByteArrayInputStream(ro.getData());
			Font f = Font.createFont(Font.TRUETYPE_FONT, i).deriveFont(FONT_SIZE);
			BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			FontMetrics fm = tmp.getGraphics().getFontMetrics(f);
			int w = fm.stringWidth(FONT_STRING);
			int h = fm.getAscent()+fm.getDescent()+fm.getLeading();
			BufferedImage bi = new BufferedImage(w+4, h+4, BufferedImage.TYPE_INT_ARGB);
			Graphics bg = bi.getGraphics();
			bg.setColor(Color.black);
			bg.setFont(f);
			bg.drawString(FONT_STRING, 2, fm.getAscent()+2);
			return bi;
		} catch (Exception e) {
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "TTF Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& (ro.getUDTI().equals("sfnt") || ro.getUDTI().equals("FontTrTy") || ro.getUDTI().equals("FontOpTy")
						|| ro.getUDTI().equals("Font TTF") || ro.getUDTI().equals("Font OTF"))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
