package com.kreative.resplendence.menus;

import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class EditMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;
	
	public EditMenu(long features) {
		super("Edit");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_E);
		
		JMenuItem mi;
		if (mac || (features & MENUS_UNDO) > 0) {
			mi = new JMenuItem("Undo");
			mi.setMnemonic(KeyEvent.VK_U);
			mi.setAccelerator(KEYSTROKE_UNDO);
			mi.setEnabled((features & MENUS_UNDO) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.UNDO, "Undo", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_CUT_COPY_PASTE) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Cut");
			mi.setMnemonic(KeyEvent.VK_T);
			mi.setAccelerator(KEYSTROKE_CUT);
			mi.setEnabled((features & MENUS_CUT_COPY_PASTE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.CUT, "Cut", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Copy");
			mi.setMnemonic(KeyEvent.VK_C);
			mi.setAccelerator(KEYSTROKE_COPY);
			mi.setEnabled((features & MENUS_CUT_COPY_PASTE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.COPY, "Copy", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Paste");
			mi.setMnemonic(KeyEvent.VK_P);
			mi.setAccelerator(KEYSTROKE_PASTE);
			mi.setEnabled((features & MENUS_CUT_COPY_PASTE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.PASTE, "Paste", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Paste After Cursor");
			mi.setMnemonic(KeyEvent.VK_E);
			mi.setAccelerator(KEYSTROKE_PASTE_AFTER);
			mi.setEnabled((features & MENUS_CUT_COPY_PASTE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.PASTE_AFTER, "Paste After Cursor", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Clear");
			mi.setMnemonic(KeyEvent.VK_R);
			mi.setEnabled((features & MENUS_CUT_COPY_PASTE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.CLEAR, "Clear", null));
				}
			});
			add(mi);
		}
		if (mac || (features & (MENUS_SELECT_ALL|MENUS_GO_TO)) > 0) {
			if (getItemCount() > 0) addSeparator();
		}
		if (mac || (features & MENUS_SELECT_ALL) > 0) {
			mi = new JMenuItem("Select All");
			mi.setMnemonic(KeyEvent.VK_A);
			mi.setAccelerator(KeyStroke.getKeyStroke('A', META_MASK));
			mi.setEnabled((features & MENUS_SELECT_ALL) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.SELECT_ALL, "Select All", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_GO_TO) > 0) {
			mi = new JMenuItem("Go To...");
			mi.setMnemonic(KeyEvent.VK_G);
			mi.setAccelerator(KeyStroke.getKeyStroke('G', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_GO_TO) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.GO_TO, "Go To...", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_FIND_REPLACE) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Find & Replace...");
			mi.setMnemonic(KeyEvent.VK_F);
			mi.setAccelerator(KeyStroke.getKeyStroke('F', META_MASK));
			mi.setEnabled((features & MENUS_FIND_REPLACE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.FIND, "Find & Replace...", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Find Again");
			mi.setMnemonic(KeyEvent.VK_N);
			mi.setAccelerator(KeyStroke.getKeyStroke('G', META_MASK));
			mi.setEnabled((features & MENUS_FIND_REPLACE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.FIND_AGAIN, "Find Again", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Replace & Find Again");
			mi.setMnemonic(KeyEvent.VK_I);
			mi.setAccelerator(KeyStroke.getKeyStroke('L', META_MASK));
			mi.setEnabled((features & MENUS_FIND_REPLACE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REPLACE_FIND_AGAIN, "Replace & Find Again", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Replace All");
			mi.setMnemonic(KeyEvent.VK_L);
			mi.setAccelerator(KeyStroke.getKeyStroke('L', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_FIND_REPLACE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REPLACE_ALL, "Replace All", null));
				}
			});
			add(mi);
		}
	}
}
