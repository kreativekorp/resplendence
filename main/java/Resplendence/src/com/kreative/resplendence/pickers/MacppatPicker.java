package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.*;

public class MacppatPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		if (PixelPatternResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
			try {
				PixelPatternResource rp = new PixelPatternResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = bi.createGraphics();
				g.setPaint(rp.toPaint(Color.black, Color.white));
				g.fillRect(0, 0, 32, 32);
				g.dispose();
				return bi;
			} catch (Exception e) {
				e.printStackTrace();
				return ResplRsrcs.getPNG("RSRC");
			}
		} else if (PixelPatternListResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
			try {
				PixelPatternListResource rp = new PixelPatternListResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				int n = rp.getPatternCount();
				BufferedImage bi = new BufferedImage(n*17-1, 16, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = bi.createGraphics();
				for (int idx=0, bx=0; idx<n; idx++, bx+=17) {
					g.setPaint(rp.getPattern(idx).toPaint(Color.black, Color.white));
					g.fillRect(bx, 0, 16, 16);
				}
				g.dispose();
				return bi;
			} catch (Exception e) {
				e.printStackTrace();
				return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
			}
		} else {
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "ppat Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& (
						PixelPatternResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
						|| PixelPatternListResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
				)
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
