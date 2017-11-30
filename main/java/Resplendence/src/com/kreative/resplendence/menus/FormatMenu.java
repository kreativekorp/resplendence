package com.kreative.resplendence.menus;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.*;
import javax.swing.*;

import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.FontPalette;

public class FormatMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1l;
	
	public FormatMenu(long features) {
		super("Format");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_M);
		
		if (mac || (features & MENUS_FORMAT)>0) {
			JMenuItem mi = new JMenuItem("Show Fonts");
			mi.setMnemonic(KeyEvent.VK_F);
			mi.setAccelerator(KeyStroke.getKeyStroke('F', META_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FontPalette.showInstance();
				}
			});
			add(mi);
			this.addSeparator();
			fillStyleMenu(this, (features & MENUS_FORMAT)>0);
			this.addSeparator();
			fillSizeMenu(this, (features & MENUS_FORMAT)>0);
		}
	}

	private static void fillSizeMenu(JMenu size, boolean enabled) {
		JMenuItem mi;
		mi = new JMenuItem("Smaller");
		mi.setMnemonic(KeyEvent.VK_S);
		mi.setAccelerator(KeyStroke.getKeyStroke('[', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Smaller",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Font nf = f.deriveFont(f.getSize2D()-1.0f);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Smaller",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		size.add(mi);
		mi = new JMenuItem("Larger");
		mi.setMnemonic(KeyEvent.VK_L);
		mi.setAccelerator(KeyStroke.getKeyStroke(']', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Larger",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Font nf = f.deriveFont(f.getSize2D()+1.0f);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Larger",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		size.add(mi);
		mi = new JMenuItem("Other Size...");
		mi.setMnemonic(KeyEvent.VK_O);
		mi.setAccelerator(KeyStroke.getKeyStroke('\\', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Other...",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Object oo = JOptionPane.showInputDialog(
							null,
							"Enter new size:",
							"Other Size",
							JOptionPane.QUESTION_MESSAGE,
							null,
							null,
							Float.toString(f.getSize2D())
					);
					if (oo != null && oo.toString().length() > 0) try {
						Font nf = f.deriveFont(Float.parseFloat(oo.toString()));
						ResplMain.setFrontmostResplendenceListener(l);
						ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Other...",nf));
					} catch (NumberFormatException nfe) {}
				}
			}
		});
		mi.setEnabled(enabled);
		size.add(mi);
	}
	
	@SuppressWarnings("unchecked")
	private static void fillStyleMenu(JMenu style, boolean enabled) {
		JMenuItem mi;
		mi = new JMenuItem("Plain");
		mi.setMnemonic(KeyEvent.VK_P);
		mi.setAccelerator(KeyStroke.getKeyStroke('T', META_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Plain",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Font nf = f.deriveFont(0).deriveFont(new HashMap<TextAttribute,Object>());
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Plain",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		style.add(mi);
		mi = new JMenuItem("Bold");
		mi.setMnemonic(KeyEvent.VK_B);
		mi.setAccelerator(KeyStroke.getKeyStroke('B', META_MASK));
		mi.setFont(mi.getFont().deriveFont(Font.BOLD));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Bold",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Font nf = f.deriveFont(f.getStyle() ^ Font.BOLD);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Bold",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		style.add(mi);
		mi = new JMenuItem("Italic");
		mi.setMnemonic(KeyEvent.VK_I);
		mi.setAccelerator(KeyStroke.getKeyStroke('I', META_MASK));
		mi.setFont(mi.getFont().deriveFont(Font.ITALIC));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Italic",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Font nf = f.deriveFont(f.getStyle() ^ Font.ITALIC);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Italic",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		style.add(mi);
		mi = new JMenuItem("Underline");
		mi.setMnemonic(KeyEvent.VK_U);
		mi.setAccelerator(KeyStroke.getKeyStroke('U', META_MASK));
		Map<TextAttribute,Object> map1 = new HashMap<TextAttribute,Object>();
		map1.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		mi.setFont(mi.getFont().deriveFont(map1));
		mi.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Underline",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Map map = f.getAttributes();
					if (map.containsKey(TextAttribute.UNDERLINE)) {
						map.remove(TextAttribute.UNDERLINE);
					} else {
						map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					}
					Font nf = f.deriveFont(map);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Underline",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		style.add(mi);
		mi = new JMenuItem("Strikethrough");
		mi.setMnemonic(KeyEvent.VK_T);
		mi.setAccelerator(KeyStroke.getKeyStroke('S', META_ALT_MASK));
		Map<TextAttribute,Object> map2 = new HashMap<TextAttribute,Object>();
		map2.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		mi.setFont(mi.getFont().deriveFont(map1));
		mi.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent ae) {
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_FONT,"Strikethrough",null));
				if (o instanceof Font) {
					Font f = (Font)o;
					Map map = f.getAttributes();
					if (map.containsKey(TextAttribute.STRIKETHROUGH)) {
						map.remove(TextAttribute.STRIKETHROUGH);
					} else {
						map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
					}
					Font nf = f.deriveFont(map);
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_FONT,"Strikethrough",nf));
				}
			}
		});
		mi.setEnabled(enabled);
		style.add(mi);
	}
}
