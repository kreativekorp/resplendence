package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.dff.DFFResourceProvider;
import com.kreative.ksfl.*;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;

public class QTPictPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		byte[] stuff = ro.getData();
		Image i;
		if (ro.getUDTI().equals("PLTE")) {
			Object op = ro.getProvider();
			PictureResource rp;
			if (op instanceof MacResourceProvider) {
				rp = ((MacResourceProvider)op).get(KSFLConstants.PICT, KSFLUtilities.getShort(stuff, 8)).shallowRecast(PictureResource.class);
			} else if (op instanceof DFFResourceProvider) {
				rp = KSFLConverter.makeMacResourceFromDFFResource(((DFFResourceProvider)op).get(KSFLConstants.Mac_PICT, KSFLUtilities.getShort(stuff, 8))).shallowRecast(PictureResource.class);
			} else {
				rp = null;
			}
			if (rp == null) {
				i = ResplRsrcs.getPNGnow("RSRC");
			} else {
				try {
					i = rp.toImage();
				} catch (Exception e) {
					i = ResplRsrcs.getPNGnow("RSRC");
				}
			}
		} else {
			try {
				PictureResource rp = new PictureResource((short)0, stuff);
				i = rp.toImage();
			} catch (Exception e) {
				i = ResplRsrcs.getPNGnow("RSRC");
			}
		}
		int w = Math.min(i.getWidth(null), 126);
		int h = Math.min(i.getHeight(null), 126);
		int x = 64-w/2;
		int y = 64-h/2;
		BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.gray);
		g.drawRect(0, 0, 127, 127);
		long ms = System.currentTimeMillis()+100;
		while (!g.drawImage(i, x, y, w, h, null) && System.currentTimeMillis() < ms);
		return bi;
	}

	public String name() {
		return "PICT Picker";
	}
	
	private boolean isMyType(String t) {
		return (t.equals("PICT") || t.equals("Img PICT") || t.equals("ImagePCT") || t.equals("PLTE"));
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& isMyType(ro.getUDTI())
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
