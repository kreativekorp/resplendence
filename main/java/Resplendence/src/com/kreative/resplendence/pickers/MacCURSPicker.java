package com.kreative.resplendence.pickers;

import java.awt.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.CursorResource;

public class MacCURSPicker implements ResplendencePicker {
	public Image imageFor(ResplendenceObject ro) {
		try {
			CursorResource curs = new CursorResource(KSFLUtilities.fcc(ro.getUDTI()), (short)0, ro.getData());
			return curs.getComposite();
		} catch (Exception e) {
			return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
		}
	}

	public String name() {
		return "CURS Picker";
	}

	public int recognizes(ResplendenceObject ro) {
		if (
				ro.isContainerType()
				&& CursorResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))
		) {
			return 0;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
}
