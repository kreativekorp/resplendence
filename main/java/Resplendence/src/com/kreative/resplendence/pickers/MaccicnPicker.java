package com.kreative.resplendence.pickers;

import java.awt.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.ColorIconResource;

public class MaccicnPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		try {
			ColorIconResource icon = new ColorIconResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
			Image ii = icon.getComposite();
			if (ii != null) return ii;
			else return ResplRsrcs.getPNG("RSRC");
		} catch (Exception e) {
			e.printStackTrace();
			return ResplRsrcs.getPNG("RSRC");
		}
	}

	public String name() {
		return "cicn Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& ColorIconResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
