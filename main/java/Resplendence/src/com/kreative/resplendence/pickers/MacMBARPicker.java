package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;
import com.kreative.rsrc.MenuResource;

public class MacMBARPicker implements ResplendencePicker {
	private static final Font chic = new Font("Charcoal", Font.PLAIN, 12);
	
	public Image imageFor(ResplendenceObject ro) {
		try {
			Object op = ro.getProvider();
			byte[] stuff = ro.getData();
			int menuCount = KSFLUtilities.getShort(stuff, 0);
			String menus[] = new String[menuCount];
			for (int i=0, o=2; i<menuCount && o < stuff.length; i++, o+=2) {
				short id = KSFLUtilities.getShort(stuff, o);
				if (op instanceof MacResourceProvider) {
					MacResourceProvider rp = (MacResourceProvider)op;
					MacResource m = rp.get(KSFLConstants.MENU, id);
					if (m == null) m = rp.get(KSFLConstants.CMNU, id);
					if (m == null) m = rp.get(KSFLConstants.cmnu, id);
					if (m == null) menus[i] = Short.toString(id);
					else menus[i] = m.shallowRecast(MenuResource.class).getTitle();
				} else if (op instanceof DFFResourceProvider) {
					DFFResourceProvider dp = (DFFResourceProvider)op;
					MacResource m = KSFLConverter.makeMacResourceFromDFFResource(dp.get(KSFLConstants.Mac_MENU, id));
					if (m == null) m = KSFLConverter.makeMacResourceFromDFFResource(dp.get(KSFLConstants.Mac_CMNU, id));
					if (m == null) m = KSFLConverter.makeMacResourceFromDFFResource(dp.get(KSFLConstants.Mac_cmnu, id));
					if (m == null) menus[i] = Short.toString(id);
					else menus[i] = m.shallowRecast(MenuResource.class).getTitle();
				} else {
					menus[i] = Short.toString(id);
				}
			}
			
			BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			FontMetrics mfm = bi.getGraphics().getFontMetrics(chic);
			int w = 4+19;
			for (String menu : menus) {
				w += 15 + mfm.stringWidth(menu);
			}
			bi = new BufferedImage(w, 20, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.getGraphics();
			g.setFont(chic);
			mfm = g.getFontMetrics();
			g.setColor(Color.white);
			g.fillRect(0, 0, w, 20);
			g.setColor(Color.black);
			int x = 19;
			int y = 10 - mfm.getHeight()/2 + mfm.getAscent();
			for (String menu : menus) {
				g.drawString(menu, x, y);
				x += 15 + mfm.stringWidth(menu);
			}
			g.setColor(new Color(0xFFCCCCCC));
			g.drawLine(0, 0, w-1, 0);
			g.drawLine(0, 0, 0, 19);
			g.drawLine(w-1, 0, w-1, 19);
			g.setColor(Color.black);
			g.drawLine(0, 19, w-1, 19);
			g.drawLine(0, 0, 4, 0);
			g.drawLine(0, 1, 2, 1);
			g.drawLine(0, 2, 1, 2);
			g.drawLine(0, 3, 0, 4);
			g.drawLine(w-1, 0, w-5, 0);
			g.drawLine(w-1, 1, w-3, 1);
			g.drawLine(w-1, 2, w-2, 2);
			g.drawLine(w-1, 3, w-1, 4);
			return bi;
		} catch (Exception e) {
			return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
		}
	}

	public String name() {
		return "MBAR Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& ro.getUDTI().equals("MBAR")
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
