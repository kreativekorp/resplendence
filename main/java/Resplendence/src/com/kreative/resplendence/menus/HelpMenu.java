package com.kreative.resplendence.menus;

import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.*;

public class HelpMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1l;
	
	public HelpMenu() {
		super("Help");
		if (!ResplMain.RUNNING_ON_MAC_OS) setMnemonic(KeyEvent.VK_H);
		
		JMenuItem mi;
		mi = new JMenuItem("Resplendence Help");
		mi.setMnemonic(KeyEvent.VK_H);
		mi.setAccelerator(KeyStroke.getKeyStroke('/', META_SHIFT_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplMain.resplHelp();
			}
		});
		add(mi);
		if (!ResplMain.RUNNING_ON_MAC_OS) {
			addSeparator();
			mi = new JMenuItem("About Resplendence...");
			mi.setMnemonic(KeyEvent.VK_A);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					AboutBox.showInstance(ae);
				}
			});
			add(mi);
		}
	}
}
