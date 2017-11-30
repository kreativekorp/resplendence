package com.kreative.resplendence.menus;

import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class ViewMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1L;
	
	public ViewMenu(long features) {
		super("View");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_V);
		
		JMenuItem mi;
		if (mac || (features & MENUS_LIST_THUMB_VIEW) > 0) {
			mi = new JMenuItem("List View");
			mi.setMnemonic(KeyEvent.VK_L);
			mi.setAccelerator(KeyStroke.getKeyStroke('L', META_ALT_MASK));
			mi.setEnabled((features & MENUS_LIST_THUMB_VIEW) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.LIST_VIEW, "List View", null));
				}
			});
			add(mi);
			mi = new JMenuItem("Thumbnail View");
			mi.setMnemonic(KeyEvent.VK_T);
			mi.setAccelerator(KeyStroke.getKeyStroke('P', META_ALT_MASK));
			mi.setEnabled((features & MENUS_LIST_THUMB_VIEW) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.THUMBNAIL_VIEW, "List View", null));
				}
			});
			add(mi);
		}
		if (mac || (features & (MENUS_ARRANGE_BY_NAME | MENUS_ARRANGE_BY_NUM | MENUS_ARRANGE_BY_SIZE | MENUS_ARRANGE_BY_KIND)) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("By Name");
			mi.setMnemonic(KeyEvent.VK_N);
			mi.setAccelerator(KeyStroke.getKeyStroke('1', META_ALT_MASK));
			mi.setEnabled((features & MENUS_ARRANGE_BY_NAME) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.ARRANGE_BY, "Name", "Name"));
				}
			});
			add(mi);
			mi = new JMenuItem("By Number");
			mi.setMnemonic(KeyEvent.VK_U);
			mi.setAccelerator(KeyStroke.getKeyStroke('2', META_ALT_MASK));
			mi.setEnabled((features & MENUS_ARRANGE_BY_NUM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.ARRANGE_BY, "Number", "Number"));
				}
			});
			add(mi);
			mi = new JMenuItem("By Size");
			mi.setMnemonic(KeyEvent.VK_S);
			mi.setAccelerator(KeyStroke.getKeyStroke('3', META_ALT_MASK));
			mi.setEnabled((features & MENUS_ARRANGE_BY_SIZE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.ARRANGE_BY, "Size", "Size"));
				}
			});
			add(mi);
			mi = new JMenuItem("By Kind");
			mi.setMnemonic(KeyEvent.VK_K);
			mi.setAccelerator(KeyStroke.getKeyStroke('4', META_ALT_MASK));
			mi.setEnabled((features & MENUS_ARRANGE_BY_KIND) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.ARRANGE_BY, "Kind", "Kind"));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_REFRESH) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Refresh");
			mi.setMnemonic(KeyEvent.VK_R);
			mi.setAccelerator(KeyStroke.getKeyStroke('R', META_MASK));
			mi.setEnabled((features & MENUS_REFRESH) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REFRESH, "Refresh", null));
				}
			});
			add(mi);
		}
		if ((features & (MENUS_TEXT_ENCODING | MENUS_COLOR_SCHEME | MENUS_BYTES_PER_ROW | MENUS_MINISCREEN)) > 0) {
			if (getItemCount() > 0) addSeparator();
			if ((features & MENUS_TEXT_ENCODING) > 0) add(new TextEncodingMenu());
			if ((features & MENUS_COLOR_SCHEME) > 0) add(new ColorSchemeMenu());
			if ((features & MENUS_BYTES_PER_ROW) > 0) add(new BytesPerRowMenu());
			if ((features & MENUS_MINISCREEN) > 0) add(new MiniScreenMenu());
		}
	}
}
