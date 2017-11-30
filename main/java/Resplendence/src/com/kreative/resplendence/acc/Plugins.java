package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.kreative.resplendence.ResplMain;
import com.kreative.resplendence.datafilter.DataFilter;
import com.kreative.resplendence.editors.ResplendenceEditor;
import com.kreative.resplendence.filecodec.FileCodec;
import com.kreative.resplendence.pickers.ResplendencePicker;
import com.kreative.resplendence.textfilter.TextFilter;

public class Plugins implements AccessoryWindow {
	private static WPlugins instance;
	
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "Plugins";
	}

	public void open(int i) {
		if (instance == null) instance = new WPlugins();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WPlugins extends JFrame {
		private static final long serialVersionUID = 1;
		
		private JTree tree;
		
		public WPlugins() {
			super("Plugins");
			ResplMain.registerWindow(this);
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Plugins");
			DefaultTreeModel model = new DefaultTreeModel(root);
			tree = new JTree(model);
			tree.setCellRenderer(new MyRenderer());
			tree.addMouseListener(new MyMouseListener());
			
			//acc
			DefaultMutableTreeNode accrn = new DefaultMutableTreeNode("Accessory Windows");
			root.add(accrn);
			for (String name : ResplMain.getAccessoryWindowNames()) {
				DefaultMutableTreeNode accn = new DefaultMutableTreeNode(new Object[]{ResplMain.getAccessoryWindow(name),ResplMain.getAccessoryWindowIndex(name)});
				accrn.add(accn);
			}
			
			//datafilter
			DefaultMutableTreeNode dfrn = new DefaultMutableTreeNode("Data Filters");
			root.add(dfrn);
			for (String s : ResplMain.getDataFilterCategories()) {
				DefaultMutableTreeNode dfcn = new DefaultMutableTreeNode(s);
				dfrn.add(dfcn);
				for (String n : ResplMain.getDataFilterNames(s)) {
					DefaultMutableTreeNode dffn = new DefaultMutableTreeNode(new Object[]{ResplMain.getDataFilter(s, n),ResplMain.getDataFilterIndex(s, n)});
					dfcn.add(dffn);
				}
			}
			
			//editors
			DefaultMutableTreeNode editrn = new DefaultMutableTreeNode("Editors");
			root.add(editrn);
			for (ResplendenceEditor edit : ResplMain.getEditorList()) {
				DefaultMutableTreeNode editn = new DefaultMutableTreeNode(edit);
				editrn.add(editn);
			}
			
			//filecodec
			DefaultMutableTreeNode fcrn = new DefaultMutableTreeNode("File Codecs");
			root.add(fcrn);
			for (FileCodec fc : ResplMain.getFileCodecList()) {
				DefaultMutableTreeNode fcn = new DefaultMutableTreeNode(fc);
				fcrn.add(fcn);
			}
			
			//pickers
			DefaultMutableTreeNode pickrn = new DefaultMutableTreeNode("Pickers");
			root.add(pickrn);
			for (ResplendencePicker pick : ResplMain.getPickerList()) {
				DefaultMutableTreeNode pickn = new DefaultMutableTreeNode(pick);
				pickrn.add(pickn);
			}
			
			//textfilter
			DefaultMutableTreeNode tfrn = new DefaultMutableTreeNode("Text Filters");
			root.add(tfrn);
			for (String s : ResplMain.getTextFilterCategories()) {
				DefaultMutableTreeNode tfcn = new DefaultMutableTreeNode(s);
				tfrn.add(tfcn);
				for (String n : ResplMain.getTextFilterNames(s)) {
					DefaultMutableTreeNode tffn = new DefaultMutableTreeNode(new Object[]{ResplMain.getTextFilter(s, n),ResplMain.getTextFilterIndex(s, n)});
					tfcn.add(tffn);
				}
			}
			
			tree.expandPath(tree.getPathForRow(0));
			//tree.setRootVisible(false);
			JScrollPane pane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			setContentPane(pane);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(600,300));
			setLocationRelativeTo(null);
		}
		
		private class MyRenderer extends DefaultTreeCellRenderer {
			private static final long serialVersionUID = 1L;

			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				if (value instanceof DefaultMutableTreeNode) {
					//tricky - value is not the REAL value!
					//here we get the REAL value
					Object realValue = ((DefaultMutableTreeNode)value).getUserObject();
					if (realValue instanceof Object[]) {
						Object[] realValues = (Object[])realValue;
						if (realValues.length == 2 && realValues[0] instanceof TextFilter && realValues[1] instanceof Number) {
							setText(((TextFilter)realValues[0]).name(((Number)realValues[1]).intValue()));
						}
						else if (realValues.length == 2 && realValues[0] instanceof DataFilter && realValues[1] instanceof Number) {
							setText(((DataFilter)realValues[0]).name(((Number)realValues[1]).intValue()));
						}
						else if (realValues.length == 2 && realValues[0] instanceof AccessoryWindow && realValues[1] instanceof Number) {
							setText(((AccessoryWindow)realValues[0]).name(((Number)realValues[1]).intValue()));
						}
					}
					else if (realValue instanceof ResplendenceEditor) {
						setText(((ResplendenceEditor)realValue).name());
						setIcon(new ImageIcon(((ResplendenceEditor)realValue).smallIcon()));
					}
					else if (realValue instanceof FileCodec) {
						setText(((FileCodec)realValue).name());
					}
					else if (realValue instanceof ResplendencePicker) {
						setText(((ResplendencePicker)realValue).name());
					}
				}
				return this;
			}
		}
		
		private class MyMouseListener extends MouseAdapter {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					TreePath p = tree.getSelectionPath();
					if (p != null) {
						Object o = p.getLastPathComponent();
						if (o instanceof DefaultMutableTreeNode) {
							Object n = ((DefaultMutableTreeNode)o).getUserObject();
							if (!(n instanceof String)) {
								new InfoWindow(n).setVisible(true);
							}
						}
					}
				}
			}
		}
		
		private static class InfoWindow extends JFrame {
			private static final long serialVersionUID = 1L;
			private static final int LW = 150;
			public InfoWindow(Object o) {
				super("About Plugin");
				ResplMain.registerWindow(this);
				JPanel main = new JPanel();
				main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
				main.setAlignmentX(JPanel.LEFT_ALIGNMENT);
				main.setAlignmentY(JPanel.TOP_ALIGNMENT);
				
				Object[] oo;
				if (o instanceof Object[]) {
					oo = (Object[])o;
					o = (oo.length > 0) ? oo[0] : null;
				} else {
					oo = new Object[]{o};
				}
				
				main.add(makeLine("Class Name:", LW, new JLabel(o.getClass().getName())));
				main.add(makeLine("Instance ID:", LW, new JLabel(o.toString().split("@")[1])));
				main.add(makeLine("Extends:", LW, new JLabel(o.getClass().getSuperclass().getName())));
				main.add(makeLine("Implements:", LW, new JLabel(ca2s(o.getClass().getInterfaces()))));
				if (o instanceof AccessoryWindow && oo.length > 1 && oo[1] instanceof Number) {
					AccessoryWindow a = (AccessoryWindow)o;
					int ii = ((Number)oo[1]).intValue();
					main.add(makeLine("Number of Windows:", LW, new JLabel(Integer.toString(a.numberOfWindows()))));
					main.add(makeLine("This Window:", LW, new JLabel(Integer.toString(ii))));
					main.add(makeLine("Name:", LW, new JLabel(a.name(ii))));
					main.add(makeLine("Keystroke:", LW, new JLabel(ks2s(a.keystroke(ii)))));
					main.add(Box.createVerticalStrut(4));
					JButton openButton = new JButton("Open");
					openButton.addActionListener(new AccListener(a,ii));
					main.add(makeLine("", LW, openButton));
				}
				else if (o instanceof DataFilter && oo.length > 1 && oo[1] instanceof Number) {
					DataFilter a = (DataFilter)o;
					int ii = ((Number)oo[1]).intValue();
					main.add(makeLine("Number of Filters:", LW, new JLabel(Integer.toString(a.numberOfFilters()))));
					main.add(makeLine("This Filter:", LW, new JLabel(Integer.toString(ii))));
					main.add(makeLine("Category:", LW, new JLabel(a.category(ii))));
					main.add(makeLine("Name:", LW, new JLabel(a.name(ii))));
					main.add(makeLine("Keystroke:", LW, new JLabel(ks2s(a.keystroke(ii)))));
				}
				else if (o instanceof ResplendenceEditor) {
					ResplendenceEditor a = (ResplendenceEditor)o;
					main.add(Box.createVerticalStrut(4));
					main.add(makeLine("Large Icon:", LW, new JLabel(new ImageIcon(a.largeIcon()))));
					main.add(Box.createVerticalStrut(4));
					main.add(makeLine("Small Icon:", LW, new JLabel(new ImageIcon(a.smallIcon()))));
					main.add(Box.createVerticalStrut(4));
					main.add(makeLine("Long Name:", LW, new JLabel(a.name())));
					main.add(makeLine("Short Name:", LW, new JLabel(a.shortName())));
				}
				else if (o instanceof FileCodec) {
					FileCodec a = (FileCodec)o;
					main.add(makeLine("Name:", LW, new JLabel(a.name())));
				}
				else if (o instanceof ResplendencePicker) {
					ResplendencePicker a = (ResplendencePicker)o;
					main.add(makeLine("Name:", LW, new JLabel(a.name())));
				}
				else if (o instanceof TextFilter && oo.length > 1 && oo[1] instanceof Number) {
					TextFilter a = (TextFilter)o;
					int ii = ((Number)oo[1]).intValue();
					main.add(makeLine("Number of Filters:", LW, new JLabel(Integer.toString(a.numberOfFilters()))));
					main.add(makeLine("This Filter:", LW, new JLabel(Integer.toString(ii))));
					main.add(makeLine("Category:", LW, new JLabel(a.category(ii))));
					main.add(makeLine("Name:", LW, new JLabel(a.name(ii))));
					main.add(makeLine("Keystroke:", LW, new JLabel(ks2s(a.keystroke(ii)))));
					main.add(makeLine("Overwrite Mode:", LW, new JLabel(a.insert(ii)?"Insert":"Replace")));
				}
				
				main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
				setContentPane(main);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				pack();
				setResizable(false);
				setLocationRelativeTo(null);
			}
		}
		
		private static String ca2s(Class<?>[] ca) {
			if (ca == null || ca.length == 0) return "None";
			String s = "";
			for (Class<?> c : ca) {
				s += "<br>"+c.getName();
			}
			return "<html>"+s.substring(4)+"</html>";
		}
		
		private static String ks2s(KeyStroke k) {
			if (k == null) return "None";
			int m = k.getModifiers();
			int t = k.getKeyCode();
			String s = "";
			if ((m & KeyEvent.CTRL_MASK) != 0) s += ResplMain.RUNNING_ON_MAC_OS?"\u2303":"Ctrl ";
			if ((m & KeyEvent.META_MASK) != 0) s += ResplMain.RUNNING_ON_MAC_OS?"\u2318":"Meta ";
			if ((m & KeyEvent.ALT_MASK) != 0) s += ResplMain.RUNNING_ON_MAC_OS?"\u2325":"Alt ";
			if ((m & KeyEvent.SHIFT_MASK) != 0) s += ResplMain.RUNNING_ON_MAC_OS?"\u21E7":"Shift ";
			switch (t) {
			case KeyEvent.VK_0: s += "0"; break;
			case KeyEvent.VK_1: s += "1"; break;
			case KeyEvent.VK_2: s += "2"; break;
			case KeyEvent.VK_3: s += "3"; break;
			case KeyEvent.VK_4: s += "4"; break;
			case KeyEvent.VK_5: s += "5"; break;
			case KeyEvent.VK_6: s += "6"; break;
			case KeyEvent.VK_7: s += "7"; break;
			case KeyEvent.VK_8: s += "8"; break;
			case KeyEvent.VK_9: s += "9"; break;
			case KeyEvent.VK_A: s += "A"; break;
			case KeyEvent.VK_ACCEPT: s += "Accept"; break;
			case KeyEvent.VK_ADD: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328+":"Num +"; break;
			case KeyEvent.VK_AGAIN: s += "Again"; break;
			case KeyEvent.VK_ALL_CANDIDATES: s += "AllCandidates"; break;
			case KeyEvent.VK_ALPHANUMERIC: s += "Alphanumeric"; break;
			case KeyEvent.VK_ALT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2325":"Alt"; break;
			case KeyEvent.VK_ALT_GRAPH: s += "AltGr"; break;
			case KeyEvent.VK_AMPERSAND: s += "&"; break;
			case KeyEvent.VK_ASTERISK: s += "*"; break;
			case KeyEvent.VK_AT: s += "@"; break;
			case KeyEvent.VK_B: s += "B"; break;
			case KeyEvent.VK_BACK_QUOTE: s += "`"; break;
			case KeyEvent.VK_BACK_SLASH: s += "\\"; break;
			case KeyEvent.VK_BACK_SPACE: s += ResplMain.RUNNING_ON_MAC_OS?"\u232B":"Bksp"; break;
			case KeyEvent.VK_BRACELEFT: s += "{"; break;
			case KeyEvent.VK_BRACERIGHT: s += "}"; break;
			case KeyEvent.VK_C: s += "C"; break;
			case KeyEvent.VK_CANCEL: s += "Cancel"; break;
			case KeyEvent.VK_CAPS_LOCK: s += ResplMain.RUNNING_ON_MAC_OS?"\u21EA":"CapsLk"; break;
			case KeyEvent.VK_CIRCUMFLEX: s += "^"; break;
			case KeyEvent.VK_CLEAR: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328\u238B":"Clear"; break;
			case KeyEvent.VK_CLOSE_BRACKET: s += "]"; break;
			case KeyEvent.VK_CODE_INPUT: s += "CodeInput"; break;
			case KeyEvent.VK_COLON: s += ":"; break;
			case KeyEvent.VK_COMMA: s += ","; break;
			case KeyEvent.VK_COMPOSE: s += "Compose"; break;
			case KeyEvent.VK_CONTROL: s += ResplMain.RUNNING_ON_MAC_OS?"\u2303":"Ctrl"; break;
			case KeyEvent.VK_CONVERT: s += "Convert"; break;
			case KeyEvent.VK_COPY: s += "Copy"; break;
			case KeyEvent.VK_CUT: s += "Cut"; break;
			case KeyEvent.VK_D: s += "D"; break;
			case KeyEvent.VK_DEAD_ABOVEDOT: s += "\u02D9"; break;
			case KeyEvent.VK_DEAD_ABOVERING: s += "\u02DA"; break;
			case KeyEvent.VK_DEAD_ACUTE: s += "\u02CA"; break;
			case KeyEvent.VK_DEAD_BREVE: s += "\u02D8"; break;
			case KeyEvent.VK_DEAD_CARON: s += "\u02C7"; break;
			case KeyEvent.VK_DEAD_CEDILLA: s += "\u00B8"; break;
			case KeyEvent.VK_DEAD_CIRCUMFLEX: s += "\u02C6"; break;
			case KeyEvent.VK_DEAD_DIAERESIS: s += "\u00A8"; break;
			case KeyEvent.VK_DEAD_DOUBLEACUTE: s += "\u02DD"; break;
			case KeyEvent.VK_DEAD_GRAVE: s += "\u02CB"; break;
			case KeyEvent.VK_DEAD_IOTA: s += "\u037A"; break;
			case KeyEvent.VK_DEAD_MACRON: s += "\u02C9"; break;
			case KeyEvent.VK_DEAD_OGONEK: s += "\u02DB"; break;
			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND: s += "\u309C"; break;
			case KeyEvent.VK_DEAD_TILDE: s += "\u02DC"; break;
			case KeyEvent.VK_DEAD_VOICED_SOUND: s += "\u309B"; break;
			case KeyEvent.VK_DECIMAL: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328.":"Num ."; break;
			case KeyEvent.VK_DELETE: s += ResplMain.RUNNING_ON_MAC_OS?"\u2326":"Del"; break;
			case KeyEvent.VK_DIVIDE: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328/":"Num /"; break;
			case KeyEvent.VK_DOLLAR: s += "$"; break;
			case KeyEvent.VK_DOWN: s += ResplMain.RUNNING_ON_MAC_OS?"\u2193":"Down"; break;
			case KeyEvent.VK_E: s += "E"; break;
			case KeyEvent.VK_END: s += ResplMain.RUNNING_ON_MAC_OS?"\u21F2":"End"; break;
			case KeyEvent.VK_ENTER: s += ResplMain.RUNNING_ON_MAC_OS?"\u21B5":"Enter"; break;
			case KeyEvent.VK_EQUALS: s += "="; break;
			case KeyEvent.VK_ESCAPE: s += ResplMain.RUNNING_ON_MAC_OS?"\u238B":"Esc"; break;
			case KeyEvent.VK_EURO_SIGN: s += "\u20AC"; break;
			case KeyEvent.VK_EXCLAMATION_MARK: s += "!"; break;
			case KeyEvent.VK_F: s += "F"; break;
			case KeyEvent.VK_F1: s += "F1"; break;
			case KeyEvent.VK_F10: s += "F10"; break;
			case KeyEvent.VK_F11: s += "F11"; break;
			case KeyEvent.VK_F12: s += "F12"; break;
			case KeyEvent.VK_F13: s += "F13"; break;
			case KeyEvent.VK_F14: s += "F14"; break;
			case KeyEvent.VK_F15: s += "F15"; break;
			case KeyEvent.VK_F16: s += "F16"; break;
			case KeyEvent.VK_F17: s += "F17"; break;
			case KeyEvent.VK_F18: s += "F18"; break;
			case KeyEvent.VK_F19: s += "F19"; break;
			case KeyEvent.VK_F2: s += "F2"; break;
			case KeyEvent.VK_F20: s += "F20"; break;
			case KeyEvent.VK_F21: s += "F21"; break;
			case KeyEvent.VK_F22: s += "F22"; break;
			case KeyEvent.VK_F23: s += "F23"; break;
			case KeyEvent.VK_F24: s += "F24"; break;
			case KeyEvent.VK_F3: s += "F3"; break;
			case KeyEvent.VK_F4: s += "F4"; break;
			case KeyEvent.VK_F5: s += "F5"; break;
			case KeyEvent.VK_F6: s += "F6"; break;
			case KeyEvent.VK_F7: s += "F7"; break;
			case KeyEvent.VK_F8: s += "F8"; break;
			case KeyEvent.VK_F9: s += "F9"; break;
			case KeyEvent.VK_FINAL: s += "Final"; break;
			case KeyEvent.VK_FIND: s += "Find"; break;
			case KeyEvent.VK_FULL_WIDTH: s += "FullWidth"; break;
			case KeyEvent.VK_G: s += "G"; break;
			case KeyEvent.VK_GREATER: s += ">"; break;
			case KeyEvent.VK_H: s += "H"; break;
			case KeyEvent.VK_HALF_WIDTH: s += "HalfWidth"; break;
			case KeyEvent.VK_HELP: s += ResplMain.RUNNING_ON_MAC_OS?"?\u20DD":"Help"; break;
			case KeyEvent.VK_HIRAGANA: s += "Hiragana"; break;
			case KeyEvent.VK_HOME: s += ResplMain.RUNNING_ON_MAC_OS?"\u21F1":"Home"; break;
			case KeyEvent.VK_I: s += "I"; break;
			case KeyEvent.VK_INPUT_METHOD_ON_OFF: s += "InputMethod"; break;
			case KeyEvent.VK_INSERT: s += "Ins"; break;
			case KeyEvent.VK_INVERTED_EXCLAMATION_MARK: s += "\u00A1"; break;
			case KeyEvent.VK_J: s += "J"; break;
			case KeyEvent.VK_JAPANESE_HIRAGANA: s += "JHiragana"; break;
			case KeyEvent.VK_JAPANESE_KATAKANA: s += "JKatakana"; break;
			case KeyEvent.VK_JAPANESE_ROMAN: s += "JRoman"; break;
			case KeyEvent.VK_K: s += "K"; break;
			case KeyEvent.VK_KANA: s += "Kana"; break;
			case KeyEvent.VK_KANA_LOCK: s += "KanaLock"; break;
			case KeyEvent.VK_KANJI: s += "Kanji"; break;
			case KeyEvent.VK_KATAKANA: s += "Katakana"; break;
			case KeyEvent.VK_KP_DOWN: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328\u2193":"Num Down"; break;
			case KeyEvent.VK_KP_LEFT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328\u2190":"Num Left"; break;
			case KeyEvent.VK_KP_RIGHT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328\u2192":"Num Right"; break;
			case KeyEvent.VK_KP_UP: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328\u2191":"Num Up"; break;
			case KeyEvent.VK_L: s += "L"; break;
			case KeyEvent.VK_LEFT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2190":"Left"; break;
			case KeyEvent.VK_LEFT_PARENTHESIS: s += "("; break;
			case KeyEvent.VK_LESS: s += "<"; break;
			case KeyEvent.VK_M: s += "M"; break;
			case KeyEvent.VK_META: s += ResplMain.RUNNING_ON_MAC_OS?"\u2318":"Meta"; break;
			case KeyEvent.VK_MINUS: s += "-"; break;
			case KeyEvent.VK_MODECHANGE: s += "ModeChange"; break;
			case KeyEvent.VK_MULTIPLY: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328*":"Num *"; break;
			case KeyEvent.VK_N: s += "N"; break;
			case KeyEvent.VK_NONCONVERT: s += "Nonconvert"; break;
			case KeyEvent.VK_NUM_LOCK: s += "NumLk"; break;
			case KeyEvent.VK_NUMBER_SIGN: s += "#"; break;
			case KeyEvent.VK_NUMPAD0: s += ResplMain.RUNNING_ON_MAC_OS?"\u23280":"Num 0"; break;
			case KeyEvent.VK_NUMPAD1: s += ResplMain.RUNNING_ON_MAC_OS?"\u23281":"Num 1"; break;
			case KeyEvent.VK_NUMPAD2: s += ResplMain.RUNNING_ON_MAC_OS?"\u23282":"Num 2"; break;
			case KeyEvent.VK_NUMPAD3: s += ResplMain.RUNNING_ON_MAC_OS?"\u23283":"Num 3"; break;
			case KeyEvent.VK_NUMPAD4: s += ResplMain.RUNNING_ON_MAC_OS?"\u23284":"Num 4"; break;
			case KeyEvent.VK_NUMPAD5: s += ResplMain.RUNNING_ON_MAC_OS?"\u23285":"Num 5"; break;
			case KeyEvent.VK_NUMPAD6: s += ResplMain.RUNNING_ON_MAC_OS?"\u23286":"Num 6"; break;
			case KeyEvent.VK_NUMPAD7: s += ResplMain.RUNNING_ON_MAC_OS?"\u23287":"Num 7"; break;
			case KeyEvent.VK_NUMPAD8: s += ResplMain.RUNNING_ON_MAC_OS?"\u23288":"Num 8"; break;
			case KeyEvent.VK_NUMPAD9: s += ResplMain.RUNNING_ON_MAC_OS?"\u23289":"Num 9"; break;
			case KeyEvent.VK_O: s += "O"; break;
			case KeyEvent.VK_OPEN_BRACKET: s += "["; break;
			case KeyEvent.VK_P: s += "P"; break;
			case KeyEvent.VK_PAGE_DOWN: s += ResplMain.RUNNING_ON_MAC_OS?"\u21DF":"PgDn"; break;
			case KeyEvent.VK_PAGE_UP: s += ResplMain.RUNNING_ON_MAC_OS?"\u21DE":"PgUp"; break;
			case KeyEvent.VK_PASTE: s += "Paste"; break;
			case KeyEvent.VK_PAUSE: s += "Pause"; break;
			case KeyEvent.VK_PERIOD: s += "."; break;
			case KeyEvent.VK_PLUS: s += "+"; break;
			case KeyEvent.VK_PREVIOUS_CANDIDATE: s += "PrevCandidate"; break;
			case KeyEvent.VK_PRINTSCREEN: s += "PrtSc"; break;
			case KeyEvent.VK_PROPS: s += "Props"; break;
			case KeyEvent.VK_Q: s += "Q"; break;
			case KeyEvent.VK_QUOTE: s += "\'"; break;
			case KeyEvent.VK_QUOTEDBL: s += "\""; break;
			case KeyEvent.VK_R: s += "R"; break;
			case KeyEvent.VK_RIGHT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2192":"Right"; break;
			case KeyEvent.VK_RIGHT_PARENTHESIS: s += ")"; break;
			case KeyEvent.VK_ROMAN_CHARACTERS: s += "RomanChars"; break;
			case KeyEvent.VK_S: s += "S"; break;
			case KeyEvent.VK_SCROLL_LOCK: s += "ScrLk"; break;
			case KeyEvent.VK_SEMICOLON: s += ";"; break;
			case KeyEvent.VK_SEPARATOR: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328,":"Num ,"; break;
			case KeyEvent.VK_SHIFT: s += ResplMain.RUNNING_ON_MAC_OS?"\u21E7":"Shift"; break;
			case KeyEvent.VK_SLASH: s += "/"; break;
			case KeyEvent.VK_SPACE: s += ResplMain.RUNNING_ON_MAC_OS?"\u2423":"Space"; break;
			case KeyEvent.VK_STOP: s += "Stop"; break;
			case KeyEvent.VK_SUBTRACT: s += ResplMain.RUNNING_ON_MAC_OS?"\u2328-":"Num -"; break;
			case KeyEvent.VK_T: s += "T"; break;
			case KeyEvent.VK_TAB: s += ResplMain.RUNNING_ON_MAC_OS?"\u21E5":"Tab"; break;
			case KeyEvent.VK_U: s += "U"; break;
			case KeyEvent.VK_UNDERSCORE: s += "_"; break;
			case KeyEvent.VK_UNDO: s += "Undo"; break;
			case KeyEvent.VK_UP: s += ResplMain.RUNNING_ON_MAC_OS?"\u2191":"Up"; break;
			case KeyEvent.VK_V: s += "V"; break;
			case KeyEvent.VK_W: s += "W"; break;
			case KeyEvent.VK_X: s += "X"; break;
			case KeyEvent.VK_Y: s += "Y"; break;
			case KeyEvent.VK_Z: s += "Z"; break;
			}
			return s;
		}
		
		private static JLabel makeFixedLabel(String s, int width) {
			JLabel l = new JLabel(s);
			l.setFont(l.getFont().deriveFont(Font.BOLD));
			l.setSize(width, l.getHeight());
			l.setMinimumSize(new Dimension(width, l.getMinimumSize().height));
			l.setPreferredSize(new Dimension(width, l.getPreferredSize().height));
			l.setMaximumSize(new Dimension(width, l.getMaximumSize().height));
			l.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
			l.setHorizontalAlignment(JLabel.RIGHT);
			l.setHorizontalTextPosition(JLabel.RIGHT);
			return l;
		}
		
		private static JPanel makeLine(String s, int width, Component c) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.add(makeFixedLabel(s, width));
			p.add(Box.createHorizontalStrut(8));
			p.add(c);
			p.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			p.setAlignmentY(JPanel.TOP_ALIGNMENT);
			return p;
		}
		
		private static class AccListener implements ActionListener {
			private AccessoryWindow a;
			private int i;
			public AccListener(AccessoryWindow a, int i) {
				this.a = a;
				this.i = i;
			}
			public void actionPerformed(ActionEvent ae) {
				a.open(i);
			}
		}
	}
}
