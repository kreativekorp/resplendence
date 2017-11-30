package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import javax.swing.*;
import com.kreative.awt.FractionalSizeGridLayout;
//import com.kreative.ksfl.*;
import com.kreative.resplendence.*;

public class CharacterChart implements AccessoryWindow {
	private static WCharacterChart instance = null;
	
	private static final String UNSUPPORTED_MESSAGE =
		"The selected text encoding is not supported by your Java installation. "+
		"Try installing charsets.jar if it is not installed. If charsets.jar is "+
		"installed, you will have to wait until Java supports this character set. "+
		"The Good Tech Fairy wishes you good luck.";
	
	public String category(int i) {
		return null;
	}
	
	public String name(int i) {
		return "Character Chart";
	}

	public void open(int i) {
		if (instance == null) instance = new WCharacterChart();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke('U', ResplMain.META_ALT_MASK);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WCharacterChart extends JFrame {
		private static final long serialVersionUID = 1;
		
		private ResplendenceListener ll = null;
		private JPanel chart;
		private JLabel[] chartcells = new JLabel[256];
		private EncodingMenu emenu;
		private ScriptMenu smenu;
		private JLabel footer;
		private ResplendenceListener myrl;
		private WindowListener mywl;
		private Map<Integer,String> uniBlocks = ResplUtils.getUnicodeBlocks();
		private Map<Integer,String> uniNames = ResplUtils.getUnicodeNames();
		private Map<Integer,String> uniCats = ResplUtils.getUnicodeCategories();
		
		private boolean eventsDisabled = false;
		
		private String encoding = "UTF-8";
		private int selectedcell = -1;
		
		private void selectcell(int i) {
			selectedcell = i;
			for (int j=0; j<256; j++) {
				chartcells[j].setBackground((j==i)?SystemColor.textHighlight:Color.white);
				chartcells[j].setForeground((j==i)?SystemColor.textHighlightText:Color.black);
			}
			if (i < 0 || i > 255) {
				footer.setText(" ");
			} else {
				String s = "";
				if (!smenu.isVisible()) {
					s += "Hex: 0x"+zeroPad(Integer.toHexString(i).toUpperCase(),2)+"    Oct: "+zeroPad(Integer.toOctalString(i),3)+"    Dec: "+Integer.toString(i)+"    -    ";
				}
				int ch = chartcells[i].getText().codePointAt(0);
				s += "Unicode: U+"+zeroPad(Integer.toHexString(ch).toUpperCase(),4)+"    Dec: "+Integer.toString(ch);
				String n = uniNames.get(ch);
				if (n != null) s += "    Name: "+n;
				s += "    Type: "+unicodeType(ch);
				footer.setText(s);
			}
		}
		
		private String unicodeType(int i) {
			String c = uniCats.get(i);
			if (c == null) return characterType(i);
			else if (c.equals("Mc")) return "Combining Spacing Mark";
			else if (c.equals("Pc")) return "Connecting Punctuation";
			else if (c.equals("Cc")) return "Control Character";
			else if (c.equals("Sc")) return "Currency Symbol";
			else if (c.equals("Pd")) return "Dash Punctuation";
			else if (c.equals("Nd")) return "Decimal Digit";
			else if (c.equals("Me")) return "Enclosing Mark";
			else if (c.equals("Pe")) return "Closing Punctuation";
			else if (c.equals("Pf")) return "Final Quote Punctuation";
			else if (c.equals("Cf")) return "Format Character";
			else if (c.equals("Pi")) return "Initial Quote Punctuation";
			else if (c.equals("Nl")) return "Letterlike Number";
			else if (c.equals("Zl")) return "Line Separator";
			else if (c.equals("Ll")) return "Lowercase Letter";
			else if (c.equals("Sm")) return "Math Symbol";
			else if (c.equals("Lm")) return "Modifier Letter";
			else if (c.equals("Sk")) return "Modifier Symbol";
			else if (c.equals("Mn")) return "Non-Spacing Mark";
			else if (c.equals("Lo")) return "Other Letter";
			else if (c.equals("No")) return "Other Number";
			else if (c.equals("Po")) return "Other Punctuation";
			else if (c.equals("So")) return "Other Symbol";
			else if (c.equals("Zp")) return "Paragraph Separator";
			else if (c.equals("Co")) return "Private Use";
			else if (c.equals("Zs")) return "Space Separator";
			else if (c.equals("Ps")) return "Opening Punctuation";
			else if (c.equals("Cs")) return "Surrogate";
			else if (c.equals("Lt")) return "Titlecase Letter";
			else if (c.equals("Cn")) return "Unassigned";
			else if (c.equals("Lu")) return "Uppercase Letter";
			else return characterType(i);
		}
		
		private String characterType(int i) {
			switch (Character.getType(i)) {
			case Character.COMBINING_SPACING_MARK: return "Combining Spacing Mark";
			case Character.CONNECTOR_PUNCTUATION: return "Connecting Punctuation";
			case Character.CONTROL: return "Control Character";
			case Character.CURRENCY_SYMBOL: return "Currency Symbol";
			case Character.DASH_PUNCTUATION: return "Dash Punctuation";
			case Character.DECIMAL_DIGIT_NUMBER: return "Decimal Digit";
			case Character.ENCLOSING_MARK: return "Enclosing Mark";
			case Character.END_PUNCTUATION: return "Closing Punctuation";
			case Character.FINAL_QUOTE_PUNCTUATION: return "Final Quote Punctuation";
			case Character.FORMAT: return "Format Character";
			case Character.INITIAL_QUOTE_PUNCTUATION: return "Initial Quote Punctuation";
			case Character.LETTER_NUMBER: return "Letterlike Number";
			case Character.LINE_SEPARATOR: return "Line Separator";
			case Character.LOWERCASE_LETTER: return "Lowercase Letter";
			case Character.MATH_SYMBOL: return "Math Symbol";
			case Character.MODIFIER_LETTER: return "Modifier Letter";
			case Character.MODIFIER_SYMBOL: return "Modifier Symbol";
			case Character.NON_SPACING_MARK: return "Non-Spacing Mark";
			case Character.OTHER_LETTER: return "Other Letter";
			case Character.OTHER_NUMBER: return "Other Number";
			case Character.OTHER_PUNCTUATION: return "Other Punctuation";
			case Character.OTHER_SYMBOL: return "Other Symbol";
			case Character.PARAGRAPH_SEPARATOR: return "Paragraph Separator";
			case Character.PRIVATE_USE: return "Private Use";
			case Character.SPACE_SEPARATOR: return "Space Separator";
			case Character.START_PUNCTUATION: return "Opening Punctuation";
			case Character.SURROGATE: return "Surrogate";
			case Character.TITLECASE_LETTER: return "Titlecase Letter";
			case Character.UNASSIGNED: return "Unassigned";
			case Character.UPPERCASE_LETTER: return "Uppercase Letter";
			default: return "Unknown";
			}
		}
		
		public WCharacterChart() {
			super("Character Chart");
			ll = ResplMain.getFrontmostResplendenceListener();
			ResplMain.registerWindow(this, ResplMain.MENUS_FORMAT | ResplMain.MENUS_TEXT_ENCODING);
			myrl = new ResplendenceListener(){
				public Object respondToResplendenceEvent(ResplendenceEvent e) {
					switch (e.getID()) {
					case ResplendenceEvent.GET_FONT:
						return chartcells[0].getFont();
					case ResplendenceEvent.SET_FONT:
						Font f = e.getFont();
						for (int i=0; i<256; i++) chartcells[i].setFont(f);
						return null;
					case ResplendenceEvent.GET_TEXT_ENCODING:
						return getTextEncoding();
					case ResplendenceEvent.SET_TEXT_ENCODING:
						setTextEncoding(e.getString());
						return null;
					}
					return null;
				}
			};
			ResplMain.addResplendenceListener(myrl, this);
			mywl = new WindowListener(){
				public void windowActivated(WindowEvent arg0) {
					ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
					if (l != myrl) ll = l;
				}
				public void windowOpened(WindowEvent arg0) {
					ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
					if (l != myrl) ll = l;
				}
				public void windowClosed(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) {}
				public void windowDeactivated(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
			};
			ResplMain.addWindowListener(mywl);
			
			footer = new JLabel(" ");
			footer.setFont(footer.getFont().deriveFont(11.0f));
			footer.setBorder(BorderFactory.createEmptyBorder(1, 10, 10, 10));
			chart = new JPanel(new FractionalSizeGridLayout(16,16,-1,-1));
			chart.setBorder(BorderFactory.createEmptyBorder(5, 10, 1, 10));
			chart.setFocusable(true);
			chart.setRequestFocusEnabled(true);
			chart.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent arg0) {
					switch (arg0.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						selectcell(Math.max(0, selectedcell-1));
						break;
					case KeyEvent.VK_RIGHT:
						selectcell((selectedcell<0)?255:Math.min(255, selectedcell+1));
						break;
					case KeyEvent.VK_UP:
						if ((arg0.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0 || (arg0.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0) {
							emenu.setSelectedIndex(Math.max(0,emenu.getSelectedIndex()-1));
						} else if ((arg0.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0 && smenu.isVisible()) {
							smenu.setSelectedIndex(Math.max(0,smenu.getSelectedIndex()-1));
						} else {
							selectcell(Math.max(0, selectedcell-16));
						}
						break;
					case KeyEvent.VK_DOWN:
						if ((arg0.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0 || (arg0.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0) {
							emenu.setSelectedIndex(Math.min(emenu.getItemCount()-2,emenu.getSelectedIndex()+1));
						} else if ((arg0.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0 && smenu.isVisible()) {
							smenu.setSelectedIndex(Math.min(smenu.getItemCount()-1,smenu.getSelectedIndex()+1));
						} else {
							selectcell((selectedcell<0)?255:Math.min(255, selectedcell+16));
						}
						break;
					default:
						return;
					}
					arg0.consume();
				}
				
				public void keyReleased(KeyEvent arg0) {}
				public void keyTyped(KeyEvent arg0) {}
			});
			for (int i=0; i<256; i++) {
				chartcells[i] = new JLabel(new String(new char[]{(char)i}));
				chartcells[i].setFont(chartcells[i].getFont().deriveFont(18.0f));
				chartcells[i].setOpaque(true);
				chartcells[i].setBackground(Color.white);
				chartcells[i].setForeground(Color.black);
				chartcells[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
				chartcells[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
				chartcells[i].setAlignmentY(JLabel.CENTER_ALIGNMENT);
				chartcells[i].setHorizontalAlignment(JLabel.CENTER);
				chartcells[i].setVerticalAlignment(JLabel.CENTER);
				chartcells[i].setHorizontalTextPosition(JLabel.CENTER);
				chartcells[i].setVerticalTextPosition(JLabel.CENTER);
				ChartCellListener ccl = new ChartCellListener(i);
				chartcells[i].addMouseListener(ccl);
				chartcells[i].addMouseMotionListener(ccl);
				chart.add(chartcells[i]);
			}
			smenu = new ScriptMenu();
			smenu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (!eventsDisabled && e.getStateChange() == ItemEvent.SELECTED) {
						int b = smenu.getSelectedIndex()*256;
						if (b >= 0x10000) {
							char h = (char)((((b-0x10000) >>> 10) & 0x3FF) + 0xD800);
							char l = (char)((((b-0x10000)       ) & 0x3FF) + 0xDC00);
							for (int i=0; i<256; i++) {
								chartcells[i].setText(new String(new char[]{h,(char)(l+i)}));
							}
						} else {
							for (int i=0; i<256; i++) {
								chartcells[i].setText(new String(new char[]{(char)(b+i)}));
							}
						}
						selectcell(-1);
					}
				}
			});
			emenu = new EncodingMenu();
			emenu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (!eventsDisabled && e.getStateChange() == ItemEvent.SELECTED) {
						String te = emenu.getTextEncoding();
						if (te != null) setTextEncoding(te);
					}
				}
			});
			JPanel header = new JPanel();
			header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
			header.add(emenu);
			header.add(smenu);
			header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
			JPanel main = new JPanel(new BorderLayout());
			main.add(header, BorderLayout.PAGE_START);
			main.add(chart, BorderLayout.CENTER);
			main.add(footer, BorderLayout.PAGE_END);
			setContentPane(main);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(520,560));
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void dispose() {
			ResplMain.removeResplendenceListener(myrl);
			ResplMain.removeWindowListener(mywl);
			super.dispose();
		}
		
		public String getTextEncoding() {
			return encoding;
		}
		
		public void setTextEncoding(String te) {
			encoding = te;
			if (te.length() < 1 || te.startsWith("UTF") || te.startsWith("UCS")) {
				//unicode
				eventsDisabled = true;
				emenu.setSelectedIndex(0);
				eventsDisabled = false;
				smenu.setVisible(true);
				int b = smenu.getSelectedIndex()*256;
				if (b >= 0x10000) {
					char h = (char)((((b-0x10000) >>> 10) & 0x3FF) + 0xD800);
					char l = (char)((((b-0x10000)       ) & 0x3FF) + 0xDC00);
					for (int i=0; i<256; i++) {
						chartcells[i].setText(new String(new char[]{h,(char)(l+i)}));
					}
				} else {
					for (int i=0; i<256; i++) {
						chartcells[i].setText(new String(new char[]{(char)(b+i)}));
					}
				}
			} else {
				//8-bit
				eventsDisabled = true;
				emenu.setTextEncoding(te);
				eventsDisabled = false;
				smenu.setVisible(false);
				try {
					for (int i=0; i<256; i++) {
						chartcells[i].setText(new String(new byte[]{(byte)i}, te));
					}
				} catch (UnsupportedEncodingException uue) {
					for (int i=0; i<256; i++) {
						chartcells[i].setText(UNSUPPORTED_MESSAGE.substring(i,i+1));
					}
				}
			}
			selectcell(-1);
		}
		
		private String zeroPad(String s, int n) {
			if (s.length() > n) return s;
			else {
				String z = "0";
				while (z.length()<n) z+=z;
				z+=s;
				return z.substring(z.length()-n);
			}
		}
		
		private class ChartCellListener implements MouseListener, MouseMotionListener {
			private int i;
			
			public ChartCellListener(int i) {
				this.i = i;
			}
			
			public void mousePressed(MouseEvent arg0) {
				arg0.consume();
				selectcell(i);
				chart.requestFocusInWindow();
			}
			
			public void mouseEntered(MouseEvent arg0) {
				if (arg0.getButton() != MouseEvent.NOBUTTON) {
					arg0.consume();
					selectcell(i);
					chart.requestFocusInWindow();
				}
			}
			
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					if (ll != null) {
						ResplMain.sendResplendenceEventTo(ll, new ResplendenceEvent(arg0.getSource(), ResplendenceEvent.INSERT_TEXT, chartcells[i].getText(), chartcells[i].getText()));
					}
				}
			}
			
			public void mouseDragged(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseMoved(MouseEvent arg0) {}
		}
		
		private class EncodingMenu extends JComboBox {
			private static final long serialVersionUID = 1;
			
			private Vector<String[]> encodings = new Vector<String[]>();
			
			public EncodingMenu() {
				setEditable(false);
				setMaximumRowCount(32);
				addItem("Unicode");
				/*
				Scanner sc = new Scanner(new ByteArrayInputStream(ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.Text_Pln, 1)));
				while (sc.hasNextLine()) {
					String ss[] = sc.nextLine().trim().split("\t");
					if (ss.length > 1 && ss[0].length() > 0 && !ss[0].equals("-")) {
						addItem(ss[0]);
						encodings.add(ss);
					}
				}
				addItem("Other...");
				*/
				for (Charset charset : Charset.availableCharsets().values()) {
					addItem(charset.displayName());
					encodings.add(new String[]{charset.displayName(), charset.name(), charset.name()});
				}
				setSelectedIndex(0);
			}
			
			public void setTextEncoding(String te) {
				if (
						te == null || te.length() < 1
						|| te.equalsIgnoreCase("UNICODE")
						|| te.startsWith("UTF") || te.startsWith("utf")
						|| te.startsWith("UCS") || te.startsWith("ucs")
				) {
					setSelectedIndex(0);
					return;
				}
				for (String[] ss : encodings) {
					if (ss.length > 1 && ss[1].equalsIgnoreCase(te)) {
						setSelectedItem(ss[0]);
						return;
					}
				}
				setSelectedIndex(getItemCount()-1);
			}
			
			public String getTextEncoding() {
				String so = getSelectedItem().toString();
				if (so.equalsIgnoreCase("Unicode")) {
					return "UTF-8";
				}
				for (String[] ss : encodings) {
					if (ss.length > 1 && ss[0].equalsIgnoreCase(so)) return ss[1];
				}
				Object o = JOptionPane.showInputDialog(
						null,
						"Enter text encoding name:",
						"Other Text Encoding",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						""
				);
				if (o != null && o.toString().length() > 0) return (o.toString());
				else return null;
			}
		}
		
		private class ScriptMenu extends JComboBox {
			private static final long serialVersionUID = 1;
			
			public ScriptMenu() {
				setEditable(false);
				setMaximumRowCount(32);
				
				String[] lines = new String[0x200];
				Vector<Integer> starts = new Vector<Integer>();
				starts.addAll(uniBlocks.keySet());
				Collections.sort(starts);
				
				Iterator<Integer> si = starts.iterator();
				int start = si.hasNext() ? si.next() : 0;
				while (si.hasNext()) {
					int end = si.next();
					String name = uniBlocks.get(start);
					
					for (int i = start & ~0xFF, j = start >> 8; i < end; i += 0x100, j++) {
						if (j >= lines.length);
						else if (lines[j] == null) lines[j] = name;
						else lines[j] += ", "+name;
					}

					start = end;
				}
				
				for (int i = 0; i < lines.length; i++) {
					String h = Integer.toHexString(i << 8).toUpperCase();
					if (h.length() < 4) { h = "0000"+h; h = h.substring(h.length()-4); }
					if (lines[i] == null) lines[i] = h+" Undefined";
					else lines[i] = h+" "+lines[i];
				}
				
				for (String s : lines) addItem(s);
				setSelectedIndex(0);
			}
			
			public void addItem(String s) {
				String[] ss = s.split(" ", 2);
				if (ss != null && ss.length == 2) {
					super.addItem("<html><code>"+ss[0]+"</code>&nbsp;&nbsp;"+ss[1]+"</html>");
				} else {
					super.addItem(s);
				}
			}
		}
	}
}
