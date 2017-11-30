package com.kreative.resplendence.pickers;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;

public class MacICONPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		try {
			if (IconResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
				IconResource icon = new IconResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				return icon.getImage();
			} else if (IconSuiteResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
				BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
				IconSuiteResource is = new IconSuiteResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
				IconResource icon = null, mask = null;
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_32BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_8BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_HUGE_32BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_HUGE_8BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_LARGE_32BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_LARGE_8BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_4BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_BW);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_HUGE_4BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_HUGE_BW);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_LARGE_4BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_LARGE_BW);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_SMALL_32BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_SMALL_8BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_SMALL_4BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_SMALL_BW);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_MINI_32BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_MINI_8BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_MINI_4BIT);
				if (icon == null) icon = is.getMember(IconResource.RESOURCE_TYPE_MINI_BW);
				if (icon == null) return ResplRsrcs.getPNG("RSRC");
				switch (icon.height) {
				case 128:
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_MASK);
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_THUMBNAIL_BW);
					break;
				case 48:
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_HUGE_MASK);
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_HUGE_BW);
					break;
				case 32:
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_LARGE_MASK);
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_LARGE_BW);
					break;
				case 16:
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_SMALL_MASK);
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_SMALL_BW);
					break;
				case 12:
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_MINI_MASK);
					if (mask == null) mask = is.getMember(IconResource.RESOURCE_TYPE_MINI_BW);
					break;
				}
				Image i = icon.getImageWithMask(mask);
				if (i == null) i = icon.getImage();
				if (i == null) return ResplRsrcs.getPNG("RSRC");
				bi.getGraphics().drawImage(i, 0, 0, 64, 64, null);
				return bi;
			} else {
				return ResplRsrcs.getPNG("RSRC");
			}
		} catch (Exception e) {
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "ICON Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& (IconResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
				|| IconSuiteResource.isMyType(KSFLUtilities.fcc(ro.getUDTI())))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
