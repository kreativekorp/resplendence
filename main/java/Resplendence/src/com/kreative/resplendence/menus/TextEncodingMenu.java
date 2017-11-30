package com.kreative.resplendence.menus;

import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import javax.swing.*;

import com.kreative.ksfl.*;
import com.kreative.resplendence.*;

public class TextEncodingMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1L;
	
	public TextEncodingMenu() {
		super("Text Encoding");
		setMnemonic(KeyEvent.VK_E);
		JMenuItem mi;
		Map<String,JMenu> mc = new HashMap<String,JMenu>();
		for (String[] tenc : getEncodings()) {
			if (tenc == null) {
				addSeparator();
			} else {
				mi = new JMenuItem(tenc[0]);
				mi.addActionListener(new TextEncodingMenuAction(tenc[1]));
				if (tenc.length > 2 && tenc[2] != null && tenc[2].length() > 0) {
					JMenu sub = mc.get(tenc[2]);
					if (sub == null) {
						sub = new JMenu(tenc[2]);
						mc.put(tenc[2], sub);
						add(sub);
					}
					sub.add(mi);
				} else {
					add(mi);
				}
			}
		}
		addSeparator();
		mi = new JMenuItem("Other...");
		mi.setMnemonic(KeyEvent.VK_O);
		mi.setAccelerator(KeyStroke.getKeyStroke('E', META_ALT_SHIFT_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_TEXT_ENCODING,"Other...",null));
				o = JOptionPane.showInputDialog(
						null,
						"Enter text encoding name:",
						"Other Text Encoding",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						o
				);
				if (o != null && o.toString().length() > 0) {
					ResplMain.setFrontmostResplendenceListener(l);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_TEXT_ENCODING,"Other...",o));
				}
			}
		});
		add(mi);
	}

	private static Vector<String[]> encodings = null;
	private static Vector<String[]> getEncodings() {
		if (encodings != null) return encodings;
		encodings = new Vector<String[]>();
		encodings.add(new String[]{"US ASCII", "US-ASCII"});
		encodings.add(new String[]{"UTF-8", "UTF-8"});
		encodings.add(new String[]{"UTF-16BE", "UTF-16BE"});
		encodings.add(new String[]{"UTF-16LE", "UTF-16LE"});
		encodings.add(null);
		Scanner sc = new Scanner(new ByteArrayInputStream(ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.Text_Pln, 1)));
		while (sc.hasNextLine()) {
			String ss[] = sc.nextLine().trim().split("\t");
			if (ss.length > 0 && ss[0].length() > 0) {
				if (ss[0].equals("-")) {
					encodings.add(null);
				} else if (ss.length > 1) {
					encodings.add(ss);
				}
			}
		}
		return encodings;
	}

	private static class TextEncodingMenuAction implements ActionListener {
		private String te;
		public TextEncodingMenuAction(String name) {
			te = name;
		}
		public void actionPerformed(ActionEvent ae) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_TEXT_ENCODING,te,te));
		}
	}
}
