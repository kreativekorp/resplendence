package com.kreative.resplendence.menus;

import java.awt.event.*;

import javax.swing.*;

import com.kreative.resplendence.*;
import com.kreative.resplendence.datafilter.*;
import com.kreative.resplendence.textfilter.*;

public class TextMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;

	public TextMenu(long features) {
		super("Text");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_T);
		
		if (mac || (features & MENUS_TEXT_FILTERS)>0) {
			for (String cat : ResplMain.getTextFilterCategories()) {
				JMenu m = new JMenu(cat);
				for (String name : ResplMain.getTextFilterNames(cat)) {
					JMenuItem mi = new JMenuItem(name);
					TextFilter tf = ResplMain.getTextFilter(cat, name);
					int w = ResplMain.getTextFilterIndex(cat, name);
					mi.setAccelerator(tf.keystroke(w));
					mi.addActionListener(new TextMenuAction(tf,w));
					m.add(mi);
				}
				m.setEnabled((features & MENUS_TEXT_FILTERS)>0);
				add(m);
			}
		}
		if (mac || ((features & MENUS_TEXT_FILTERS)>0 && (features & MENUS_DATA_FILTERS)>0)) {
			if (getItemCount() > 0) addSeparator();
		}
		if (mac || (features & MENUS_DATA_FILTERS)>0) {
			for (String cat : ResplMain.getDataFilterCategories()) {
				JMenu m = new JMenu(cat);
				for (String name : ResplMain.getDataFilterNames(cat)) {
					JMenuItem mi = new JMenuItem(name);
					DataFilter df = ResplMain.getDataFilter(cat, name);
					int ii = ResplMain.getDataFilterIndex(cat, name);
					mi.setAccelerator(df.keystroke(ii));
					mi.addActionListener(new DataMenuAction(df,ii));
					m.add(mi);
				}
				m.setEnabled((features & MENUS_DATA_FILTERS)>0);
				add(m);
			}
		}
	}
	
	private static class TextMenuAction implements ActionListener {
		private TextFilter tfi;
		private int tfii;
		private String tfn;
		private boolean tfiii;
		public TextMenuAction(TextFilter tf, int i) {
			tfi = tf;
			tfii = i;
			tfn = tf.name(i);
			tfiii = tf.insert(i);
		}
		public void actionPerformed(ActionEvent ae) {
			ResplendenceListener rl = ResplMain.getFrontmostResplendenceListener();
			Object o = ResplMain.sendResplendenceEventTo(rl, new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_SELECTED_TEXT,tfn,null));
			if (o instanceof String) {
				String s = (String)o;
				String s2 = tfi.filter(tfii, s);
				if (s2 != null && s2 != s) {
					if (tfiii) {
						ResplMain.sendResplendenceEventTo(rl, new ResplendenceEvent(ae.getSource(),ResplendenceEvent.INSERT_TEXT,tfn,s2));
					} else {
						ResplMain.sendResplendenceEventTo(rl, new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_SELECTED_TEXT,tfn,s2));
					}
				}
			}
		}
	}
	
	private static class DataMenuAction implements ActionListener {
		private DataFilter dfi;
		private int dfii;
		private String dfn;
		public DataMenuAction(DataFilter df, int i) {
			dfi = df;
			dfii = i;
			dfn = df.name(i);
		}
		public void actionPerformed(ActionEvent ae) {
			ResplendenceListener rl = ResplMain.getFrontmostResplendenceListener();
			Object o = ResplMain.sendResplendenceEventTo(rl, new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_SELECTED_DATA,dfn,null));
			if (o instanceof byte[]) {
				byte[] b = (byte[])o;
				byte[] b2 = dfi.filter(dfii,b);
				if (b2 != null && b2 != b) {
					ResplMain.sendResplendenceEventTo(rl, new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_SELECTED_DATA,dfn,b2));
				}
			}
		}
	}
}
