package com.kreative.resplendence.menus;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.swing.JHexEditorColorSchemes;

public class ColorSchemeMenu extends JMenu implements MenuConstants, JHexEditorColorSchemes {
	public static final long serialVersionUID = 1L;
	
	public ColorSchemeMenu() {
		super("Color Scheme");
		setMnemonic(KeyEvent.VK_C);
		for (int i=0; i<COLOR_SCHEME_COUNT; i++) {
			JMenuItem mi = new JMenuItem(COLOR_SCHEME_NAMES[i]);
			mi.addActionListener(new ColorSchemeMenuAction(COLOR_SCHEME_NAMES[i], COLOR_SCHEMES[i]));
			add(mi);
		}
	}
	
	private static class ColorSchemeMenuAction implements ActionListener {
		private String cn;
		private Color[] cs;
		public ColorSchemeMenuAction(String cn, Color[] cs) {
			this.cn = cn;
			this.cs = cs;
		}
		public void actionPerformed(ActionEvent ae) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_COLOR_SCHEME,cn,cs));
		}
	}
}
