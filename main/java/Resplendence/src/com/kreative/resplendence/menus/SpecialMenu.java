package com.kreative.resplendence.menus;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.acc.*;

public class SpecialMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;

	public SpecialMenu() {
		super("Special");
		if (!ResplMain.RUNNING_ON_MAC_OS) setMnemonic(KeyEvent.VK_S);
		
		Set<String> cats = new HashSet<String>(); // all your vector<string> are belong to us
		for (String name : ResplMain.getAccessoryWindowNames()) {
			AccessoryWindow awi = ResplMain.getAccessoryWindow(name);
			int aii = ResplMain.getAccessoryWindowIndex(name);
			if (awi.category(aii) == null) {
				JMenuItem mi = new JMenuItem(name);
				mi.setAccelerator(awi.keystroke(aii));
				mi.addActionListener(new SpecialMenuAction(awi, aii));
				add(mi);
			} else {
				cats.add(awi.category(aii));
			}
		}
		if (!cats.isEmpty()) {
			addSeparator();
			String[] catss = cats.toArray(new String[0]);
			Arrays.sort(catss);
			for (String cat : catss) {
				JMenu m = new JMenu(cat);
				for (String name : ResplMain.getAccessoryWindowNames()) {
					AccessoryWindow awi = ResplMain.getAccessoryWindow(name);
					int aii = ResplMain.getAccessoryWindowIndex(name);
					if (cat.equals(awi.category(aii))) {
						JMenuItem mi = new JMenuItem(name);
						mi.setAccelerator(awi.keystroke(aii));
						mi.addActionListener(new SpecialMenuAction(awi, aii));
						m.add(mi);
					}
				}
				add(m);
			}
		}
	}
		
	private static class SpecialMenuAction implements ActionListener {
		private AccessoryWindow awi;
		private int aii;
		public SpecialMenuAction(AccessoryWindow awi, int aii) {
			this.awi = awi;
			this.aii = aii;
		}
		public void actionPerformed(ActionEvent arg0) {
			awi.open(aii);
		}
	}
}
