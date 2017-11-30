package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.FontResource;

public class MacNFNTPicker implements ResplendencePicker {
	private static final String FONT_STRING = "Amiga Monkeys: 2369?";
	
	public Image imageFor(ResplendenceObject ro) {
		if (
				ro.getSize() == 0l
				&& ro.getUDTI().equals("FONT")
				&& ro.getProperty("id") instanceof Number
				&& (((Number)ro.getProperty("id")).intValue() % 128) == 0
		) {
			String n = ro.getProperty("name").toString();
			BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = bi.getGraphics();
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(n);
			int h = fm.getHeight();
			int a = fm.getAscent();
			bi = new BufferedImage(w+4, h+4, BufferedImage.TYPE_INT_ARGB);
			g = bi.getGraphics();
			g.setColor(Color.black);
			g.drawString(n, 2, a+2);
			return bi;
		} else {
			try {
				FontResource f = new FontResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				FontResource.FontInfo fi = f.getInfo();
				int w = fi.getStringWidth(FONT_STRING, "US-ASCII");
				int h = fi.ascent+fi.descent+fi.leading;
				BufferedImage bi = new BufferedImage(w+4, h+4, BufferedImage.TYPE_INT_ARGB);
				fi.drawString(bi.getGraphics(), 2, fi.ascent+2, FONT_STRING, "US-ASCII", Integer.MAX_VALUE, 0, 0xFF000000);
				return bi;
			} catch (Exception e) {
				return ResplRsrcs.getPNG("RSRC");
			}
		}
	}

	public String name() {
		return "NFNT Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& FontResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
