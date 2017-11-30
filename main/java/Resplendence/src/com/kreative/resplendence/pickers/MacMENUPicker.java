package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.rsrc.MenuResource;
import com.kreative.resplendence.*;

public class MacMENUPicker implements ResplendencePicker {
	private static final Font chic = new Font("Charcoal", Font.PLAIN, 12);
	
	public Image imageFor(ResplendenceObject ro) {
		BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		FontMetrics mfm;
		try {
			MenuResource m = new MenuResource((short)0, ro.getData());
			g.setFont(chic);
			mfm = g.getFontMetrics();
			g.setColor(m.getEnabled() ? Color.black : Color.gray);
			String ms = m.getTitle();
			int msw = mfm.stringWidth(ms);
			g.fillRect(0, 0, msw+20, 18);
			g.setColor(Color.white);
			g.drawString(ms, 10, 9-mfm.getHeight()/2+mfm.getAscent());
			g.setColor(Color.black);
			g.drawLine(0, 18, 126, 18);
			int y = 19;
			Iterator<MenuResource.MenuItem> it = m.iterator();
			while (y+16 <= 126 && it.hasNext()) {
				MenuResource.MenuItem mi = it.next();
				g.setColor(Color.white);
				g.fillRect(0, y, 127, 16);
				if (mi.menuItemName.equals("-")) {
					g.setColor(Color.gray);
					g.drawLine(1, y+8, 125, y+8);
				} else {
					g.setFont(new Font(mi.getAttributes(chic)));
					mfm = g.getFontMetrics();
					g.setColor(mi.enabled ? Color.black : Color.gray);
					g.drawString(mi.menuItemName, 16, y+8-mfm.getHeight()/2+mfm.getAscent());
					if (mi.commandChar == MenuResource.MenuItem.COMMANDCHAR_SUBMENU) {
						g.fillPolygon(new int[]{112,117,112}, new int[]{y+2, y+7, y+12}, 3);
					} else {
						if (mi.markChar >= ' ') {
							String ks = ""+mi.markChar;
							g.drawString(ks, 4, y+8-mfm.getHeight()/2+mfm.getAscent());
						}
						if (mi.commandChar >= ' ') {
							String cs = "\u2318"+mi.commandChar;
							g.drawString(cs, 103, y+8-mfm.getHeight()/2+mfm.getAscent());
						}
					}
				}
				g.setColor(Color.black);
				g.drawLine(0, y, 0, y+15);
				g.drawLine(126, y, 126, y+15);
				g.drawLine(127, (y==19)?21:y, 127, y+15);
				y+=16;
			}
			if (it.hasNext()) {
				y-=16;
				g.setColor(Color.white);
				g.fillRect(0, y, 127, 16);
				g.setColor(Color.black);
				g.drawLine(0, y, 0, y+15);
				g.drawLine(126, y, 126, y+15);
				g.drawLine(127, y, 127, y+15);
				g.fillPolygon(new int[]{14,24,19}, new int[]{y+5,y+5,y+10}, 3);
				y+=16;
			}
			g.setColor(Color.black);
			g.drawLine(0, y, 127, y);
			g.drawLine(3, y+1, 127, y+1);
		} catch (Exception e) {
			bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
			g = bi.getGraphics();
			g.drawImage(ResplRsrcs.getPNGnow("RSRC"), 48, 48, null);
		}
		return bi;
	}

	public String name() {
		return "MENU Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& MenuResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
