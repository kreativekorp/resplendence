package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.ksfl.*;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;

public class MacclutPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		try {
			Color[] colors;
			if (ColorLookUpTableResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
				colors = new ColorLookUpTableResource((short)0, ro.getData()).getColorsByArrayIndex();
			} else if (ColorPaletteResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
				colors = new ColorPaletteResource((short)0, ro.getData()).getColors();
			} else {
				return ResplRsrcs.getPNG("RSRC");
			}
			BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.getGraphics();
			g.setColor(new Color(0x7FFFFFFF, true));
			g.fillRect(0, 0, 64, 64);
			int ns = 1, ss = 64;
			while (ss > 1 && ns*ns < colors.length) {
				ns*=2;
				ss/=2;
			}
			for (int i=0, y=0; i<colors.length && y<ns; y++) {
				for (int x=0; i<colors.length && x<ns; x++, i++) {
					g.setColor(colors[i]);
					g.fillRect(x*ss, y*ss, ss, ss);
				}
			}
			return bi;
		} catch (Exception e) {
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "clut/pltt Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& (ColorLookUpTableResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
				|| ColorPaletteResource.isMyType(KSFLUtilities.fcc(ro.getUDTI())))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
