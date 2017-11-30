package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.*;
import com.kreative.resplendence.*;

public class ImagePicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		byte[] stuff = ro.getData();
		Image i;
		if (stuff.length == 0) {
			i = ResplRsrcs.getPNGnow("RSRC");
		} else {
			try {
				i = ResplUtils.createImage(stuff);
				if (i == null) i = ResplRsrcs.getPNGnow("RSRC");
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
		return "Image Picker";
	}
	
	private boolean isMyType(String t) {
		if (t.equals("GIF ") || t.equals("GIFf") || t.equals("gif ")) return true;
		if (t.equals("JPEG") || t.equals("jpeg") || t.equals("JPG ") || t.equals("JPGg") || t.equals("jpg ")) return true;
		if (t.equals("PNG ") || t.equals("PNGg") || t.equals("png ")) return true;
		if (t.equals("TIFF") || t.equals("tiff") || t.equals("TIF ") || t.equals("TIFf") || t.equals("tif ")) return true;
		if (t.equals("BMP ") || t.equals("BMPp") || t.equals("bmp ")) return true;
		if (t.equals("ImageGIF") || t.equals("Img GIF ")) return true;
		if (t.equals("ImageJPG") || t.equals("Img JPG ") || t.equals("Img JPEG")) return true;
		if (t.equals("ImagePNG") || t.equals("Img PNG ")) return true;
		if (t.equals("ImageTIF") || t.equals("Img TIF ") || t.equals("Img TIFF")) return true;
		if (t.equals("ImageBMP") || t.equals("Img BMP ")) return true;
		if (t.equals("Icon GIF") || t.equals("Icn GIF ")) return true;
		if (t.equals("IconJPEG") || t.equals("Icon JPG") || t.equals("Icn JPEG") || t.equals("Icn JPG ")) return true;
		if (t.equals("Icon PNG") || t.equals("Icn PNG ")) return true;
		if (t.equals("IconTIFF") || t.equals("Icon TIF") || t.equals("Icn TIFF") || t.equals("Icn TIF ")) return true;
		if (t.equals("Icon BMP") || t.equals("Icn BMP ")) return true;
		return false;
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
