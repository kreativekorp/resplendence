package com.kreative.resplendence.misc;

import javax.swing.*;

public class SaveChangesDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final int SAVE = 1;
	public static final int CLOSE = 2;
	public static final int DONT_SAVE_OR_CLOSE = 0;
	public static final int CLOSE_BUT_DONT_SAVE = CLOSE;
	public static final int SAVE_AND_CLOSE = SAVE | CLOSE;
	
	public static int showSaveChangesDialog(String what) {
		Object o = JOptionPane.showOptionDialog(null, "<html>Save changes to "+what+" before closing?</html>", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Cancel", "Don't Save"}, "Save");
		if (o.equals(0)) return SAVE_AND_CLOSE;
		else if (o.equals(1)) return DONT_SAVE_OR_CLOSE;
		else if (o.equals(2)) return CLOSE_BUT_DONT_SAVE;
		else return DONT_SAVE_OR_CLOSE;
	}
	
	private SaveChangesDialog() {
		// the JDialogness of this class is completely meaningless
	}
}
