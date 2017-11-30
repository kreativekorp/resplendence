package com.kreative.resplendence.menus;

import java.awt.event.*;

import javax.swing.JComponent;

import com.kreative.resplendence.*;

public class ResplendenceKeystrokeAdapter implements ActionListener, MenuConstants {
	private static ResplendenceKeystrokeAdapter instance;
	
	public static ResplendenceKeystrokeAdapter getInstance() {
		if (instance == null) instance = new ResplendenceKeystrokeAdapter();
		return instance;
	}
	
	public void addUndoAction(JComponent c) {
		c.registerKeyboardAction(this, "Undo", KEYSTROKE_UNDO, JComponent.WHEN_FOCUSED);
	}
	
	public void addCutCopyPasteAction(JComponent c) {
		c.registerKeyboardAction(this, "Cut", KEYSTROKE_CUT, JComponent.WHEN_FOCUSED);
		c.registerKeyboardAction(this, "Copy", KEYSTROKE_COPY, JComponent.WHEN_FOCUSED);
		c.registerKeyboardAction(this, "Paste", KEYSTROKE_PASTE, JComponent.WHEN_FOCUSED);
		c.registerKeyboardAction(this, "PasteAfter", KEYSTROKE_PASTE_AFTER, JComponent.WHEN_FOCUSED);
	}
	
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		if (a.equals("Undo")) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.UNDO, "Undo", null));
		}
		else if (a.equals("Cut")) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.CUT, "Cut", null));
		}
		else if (a.equals("Copy")) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.COPY, "Copy", null));
		}
		else if (a.equals("Paste")) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.PASTE, "Paste", null));
		}
		else if (a.equals("PasteAfter")) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.PASTE_AFTER, "Paste After Cursor", null));
		}
	}
}
