package com.kreative.resplendence;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.kreative.resplendence.editors.ResplendenceEditor;
import com.kreative.resplendence.misc.SaveChangesDialog;

public abstract class ResplendenceEditorWindow extends JFrame implements WindowListener, ResplendenceListener {
	private static final long serialVersionUID = 1L;
	private ResplendenceObject objectEdited;
	private boolean changesMade;
	private ResplendenceEditorWindow parent;
	private List<ResplendenceEditorWindow> children;
	
	public ResplendenceEditorWindow(ResplendenceObject ro, boolean trackChildren) {
		super(ro.getTitleForWindows());
		if (ro.getNativeFile() != null && !ro.getNativeFile().getAbsolutePath().contains(".rwc/")) {
			getRootPane().putClientProperty("Window.documentFile", ro.getNativeFile());
		}
		objectEdited = ro;
		changesMade = false;
		getRootPane().putClientProperty("Window.documentModified", false);
		parent = null;
		children = trackChildren ? new ArrayList<ResplendenceEditorWindow>() : null;
		ResplMain.addResplendenceListener(this, this);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public final void register() {
		ResplMain.registerWindow(this);
	}
	
	public final void register(long menuFeatures) {
		ResplMain.registerWindow(this, menuFeatures);
	}
	
	public final void register(long menuFeatures, JMenu[] extra) {
		ResplMain.registerWindow(this, menuFeatures, extra);
	}

	public void myWindowActivated(WindowEvent we) {}
	public void myWindowClosed(WindowEvent we) {}
	public void myWindowClosing(WindowEvent we) {}
	public void myWindowDeactivated(WindowEvent we) {}
	public void myWindowDeiconified(WindowEvent we) {}
	public void myWindowIconified(WindowEvent we) {}
	public void myWindowOpened(WindowEvent we) {}
	public Object myRespondToResplendenceEvent(ResplendenceEvent e) { return null; }
	
	public final void windowActivated(WindowEvent we) {
		myWindowActivated(we);
	}
	
	public final void windowClosed(WindowEvent we) {
		if (parent != null) parent.removeChild(this);
		myWindowClosed(we);
	}
	
	public final void windowClosing(WindowEvent we) {
		myWindowClosing(we);
		if (children != null && children.size() > 0) {
			for (ResplendenceEditorWindow w : children) {
				EventQueue eq = w.getToolkit().getSystemEventQueue();
				eq.postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
			}
			EventQueue eq = this.getToolkit().getSystemEventQueue();
			eq.postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			return;
		}
		if (changesMade) {
			int i = SaveChangesDialog.showSaveChangesDialog(getTitle());
			if ((i & SaveChangesDialog.SAVE) != 0) save();
			if ((i & SaveChangesDialog.CLOSE) != 0) dispose();
		} else {
			dispose();
		}
	}

	public final void windowDeactivated(WindowEvent we) {
		myWindowDeactivated(we);
	}

	public final void windowDeiconified(WindowEvent we) {
		myWindowDeiconified(we);
	}

	public final void windowIconified(WindowEvent we) {
		myWindowIconified(we);
	}

	public final void windowOpened(WindowEvent we) {
		myWindowOpened(we);
	}
	
	public final Object respondToResplendenceEvent(ResplendenceEvent e) {
		switch (e.getID()) {
		case ResplendenceEvent.SAVE:
			if (children != null) {
				for (ResplendenceEditorWindow w : children) {
					w.save();
				}
			}
			save();
			break;
		case ResplendenceEvent.SAVE_AS:
			if (parent != null) parent.removeChild(this);
			parent = null;
			objectEdited = new FSObject(e.getFile());
			setTitle(objectEdited.getTitleForWindows());
			if (children != null) {
				for (ResplendenceEditorWindow w : children) {
					w.save();
				}
			}
			save();
			break;
		case ResplendenceEvent.SAVE_A_COPY:
			if (children != null) {
				for (ResplendenceEditorWindow w : children) {
					w.save();
				}
			}
			save(new FSObject(e.getFile()));
			break;
		case ResplendenceEvent.REVERT:
			revert();
			if (children != null) {
				for (ResplendenceEditorWindow w : children) {
					w.revert();
				}
			}
			break;
		}
		return myRespondToResplendenceEvent(e);
	}
	
	public final ResplendenceObject getResplendenceObject() {
		return objectEdited;
	}
	
	public final ResplendenceEditorWindow resplOpen(ResplendenceObject ro) {
		ResplendenceEditorWindow ef = ResplMain.resplOpen(ro);
		addChild(ef);
		return ef;
	}
	
	public final ResplendenceEditorWindow resplOpen(ResplendenceEditor ed, ResplendenceObject ro) {
		ResplendenceEditorWindow ef = ResplMain.resplOpen(ed, ro);
		addChild(ef);
		return ef;
	}
	
	public final ResplendenceEditorWindow resplOpen(ResplendenceEditorWindow rew) {
		addChild(rew);
		return rew;
	}
	
	private final void addChild(ResplendenceEditorWindow child) {
		if (child != null) {
			if (children != null) {
				child.parent = this;
				children.add(child);
			} else if (parent != null) {
				parent.addChild(child);
			}
		}
	}
	
	private final void removeChild(ResplendenceEditorWindow child) {
		if (child != null) {
			if (children != null) {
				child.parent = null;
				children.remove(child);
			} else if (parent != null) {
				parent.removeChild(child);
			}
		}
	}
	
	public final ResplendenceEditorWindow getParentEditor() {
		return parent;
	}
	
	public final ResplendenceEditorWindow[] getChildren() {
		return (children == null) ? null : children.toArray(new ResplendenceEditorWindow[0]);
	}
	
	public final boolean getChangesMade() {
		return changesMade;
	}
	
	public final void setChangesMade() {
		changesMade = true;
		getRootPane().putClientProperty("Window.documentModified", true);
	}
	
	public final void save() {
		save(objectEdited);
		changesMade = false;
		getRootPane().putClientProperty("Window.documentModified", false);
	}
	
	public void save(ResplendenceObject ro) {}
	
	public final void revert() {
		revert(objectEdited);
		changesMade = false;
		getRootPane().putClientProperty("Window.documentModified", false);
	}
	
	public void revert(ResplendenceObject ro) {}
}
