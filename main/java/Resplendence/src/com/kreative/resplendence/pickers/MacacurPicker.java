package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.dff.DFFResourceProvider;
import com.kreative.rsrc.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;

public class MacacurPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		try {
			Object p = ro.getProvider();
			byte[] stuff = ro.getData();
			int n = KSFLUtilities.getShort(stuff, 0);
			BufferedImage bi = new BufferedImage(n*17-1, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.getGraphics();
			for (int idx=0, bx=0; idx<n; idx++, bx+=17) {
				short id = KSFLUtilities.getShort(stuff, 4+idx*4);
				Image i;
				if (p instanceof MacResourceProvider) {
					i = ((MacResourceProvider)p).get(KSFLConstants.CURS, id).shallowRecast(CursorResource.class).getComposite();
				} else if (p instanceof DFFResourceProvider) {
					byte[] junk = ((DFFResourceProvider)p).getData(KSFLConstants.Mac_CURS, id);
					i = new CursorResource((short)0, junk).getComposite();
				} else {
					throw new IllegalArgumentException();
				}
				g.drawImage(i, bx, 0, null);
			}
			return bi;
		} catch (Exception e) {
			return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
		}
	}

	public String name() {
		return "acur Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& (ro.getProvider() instanceof MacResourceProvider
				|| ro.getProvider() instanceof DFFResourceProvider)
				&& ro.getUDTI().equals("acur")
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
