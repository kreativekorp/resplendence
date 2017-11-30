package com.kreative.resplendence.menus;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.kreative.resplendence.*;

public class WindowMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;
	
	public WindowMenu() {
		super("Window");
		if (!ResplMain.RUNNING_ON_MAC_OS) setMnemonic(KeyEvent.VK_W);
		update();
	}

	public void update() {
		removeAll();
		boolean enabled = (ResplMain.getTopmostWindow() != null);
		JMenuItem mi;
		mi = new JMenuItem("Close Window");
		mi.setMnemonic(KeyEvent.VK_W);
		mi.setAccelerator(KeyStroke.getKeyStroke('W', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Window w = ResplMain.getTopmostWindow();
				if (w != null) {
					EventQueue eq = w.getToolkit().getSystemEventQueue();
					eq.postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		mi.setEnabled(enabled);
		add(mi);
		mi = new JMenuItem("Minimize Window");
		mi.setMnemonic(KeyEvent.VK_M);
		mi.setAccelerator(KeyStroke.getKeyStroke('M', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Window w = ResplMain.getTopmostWindow();
				if (w != null) {
					if (w instanceof Frame) {
						Frame f = ((Frame)w);
						if (f.getState() == Frame.ICONIFIED)
							f.setState(Frame.NORMAL);
						else
							f.setState(Frame.ICONIFIED);
					}
				}
			}
		});
		mi.setEnabled(enabled);
		add(mi);
		mi = new JMenuItem(ResplMain.RUNNING_ON_MAC_OS?"Zoom Window":"Maximize Window");
		mi.setMnemonic(ResplMain.RUNNING_ON_MAC_OS?KeyEvent.VK_Z:KeyEvent.VK_X);
		mi.setAccelerator(KeyStroke.getKeyStroke('/', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Window w = ResplMain.getTopmostWindow();
				if (w != null) {
					if (w instanceof Frame) {
						Frame f = ((Frame)w);
						if (f.getExtendedState() == Frame.MAXIMIZED_BOTH)
							f.setExtendedState(Frame.NORMAL);
						else
							f.setExtendedState(Frame.MAXIMIZED_BOTH);
					}
				}
			}
		});
		mi.setEnabled(enabled);
		add(mi);
		mi = new JMenuItem("Bring All to Front");
		mi.setMnemonic(KeyEvent.VK_B);
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Window[] ww = ResplMain.getWindows();
				for (int i=ww.length-1; i>=0; i--) {
					ww[i].toFront();
				}
			}
		});
		mi.setEnabled(enabled);
		add(mi);
		if (enabled) addSeparator();
		List<Window> wins = new ArrayList<Window>();
		for (Window w : ResplMain.getWindows()) wins.add(w);
		while (wins.size() > 0) {
			Window w = wins.remove(0);
			if (w instanceof ResplendenceEditorWindow) {
				ResplendenceEditorWindow rew = (ResplendenceEditorWindow)w;
				while (rew.getParentEditor() != null) rew = rew.getParentEditor();
				addResplendenceEditorWindow(rew, wins, "");
			} else {
				mi = new JMenuItem((w instanceof Frame)?(((Frame)w).getTitle()):(w.getName()));
				mi.addActionListener(new WindowMenuAction(w));
				add(mi);
			}
		}
	}
	
	private void addResplendenceEditorWindow(ResplendenceEditorWindow rew, List<Window> wins, String indent) {
		wins.remove(rew);
		JMenuItem mi = new JMenuItem(indent+(
				(rew.getParentEditor() == null)?
						rew.getResplendenceObject().getTitleForWindows():
							rew.getResplendenceObject().getTitleForWindowMenu()
		));
		mi.addActionListener(new WindowMenuAction(rew));
		add(mi);
		if (rew.getChildren() != null) {
			for (ResplendenceEditorWindow ch : rew.getChildren()) {
				addResplendenceEditorWindow(ch, wins, indent+"    ");
			}
		}
	}
	
	private static class WindowMenuAction implements ActionListener {
		private Window w;
		public WindowMenuAction(Window w) {
			this.w = w;
		}
		public void actionPerformed(ActionEvent e) {
			w.toFront();
		}
	}
}
