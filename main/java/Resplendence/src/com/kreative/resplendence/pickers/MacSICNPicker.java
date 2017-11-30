package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.IconListResource;

public class MacSICNPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		if (IconListResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
			try {
				IconListResource rp = new IconListResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				int n = rp.getIconCount();
				BufferedImage bi = new BufferedImage(n*(rp.width+1)-1, rp.height, BufferedImage.TYPE_INT_ARGB);
				Graphics g = bi.getGraphics();
				for (int idx=0, bx=0; idx<n; idx++, bx+=(rp.width+1)) {
					Image i = rp.getIcon(idx).getImage();
					g.drawImage(i, bx, 0, null);
				}
				return bi;
			} catch (Exception e) {
				return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
			}
		} else {
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "SICN Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& IconListResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
