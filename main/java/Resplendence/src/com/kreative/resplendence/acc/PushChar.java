package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.kreative.awt.FractionalSizeGridLayout;
import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.CharInFont;

public class PushChar implements AccessoryWindow {
	private static WCharacterChart instance = null;
	
	public String category(int i) {
		return null;
	}

	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke('U', ResplMain.META_ALT_SHIFT_MASK);
	}

	public String name(int i) {
		return "PushChar";
	}

	public int numberOfWindows() {
		return 1;
	}

	public void open(int i) {
		if (instance == null) instance = new WCharacterChart();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}

	private static class WCharacterChart extends JFrame {
		private static final long serialVersionUID = 1;
		private static final Color HEADER_COLOR = new Color(0xFF4D4C67);
		private static final Color HEADER_TEXT = Color.white;
		private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 10);
		
		private ResplendenceListener ll = null;
		private ResplendenceListener myrl;
		private WindowListener mywl;
		private Font myFont = new Font("SansSerif", Font.PLAIN, 12);
		private JPanel myPanel;
		private JLabel footer;
		private Map<Integer,String> uniBlocks = null;
		private Map<Integer,String> uniNames = null;
		private Map<Integer,String> uniCats = null;
		
		public WCharacterChart() {
			super("PushChar");
			ll = ResplMain.getFrontmostResplendenceListener();
			ResplMain.registerWindow(this, ResplMain.MENUS_FORMAT);
			myrl = new ResplendenceListener(){
				public Object respondToResplendenceEvent(ResplendenceEvent e) {
					switch (e.getID()) {
					case ResplendenceEvent.GET_FONT:
						return getMyFont();
					case ResplendenceEvent.SET_FONT:
						setMyFont(e.getFont());
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
			footer.setBorder(BorderFactory.createEmptyBorder(0, 4, 2, 4));
			
			myPanel = new JPanel();
			myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
			JPanel myWrapper = new JPanel(new BorderLayout());
			myWrapper.add(myPanel, BorderLayout.NORTH);
			JScrollPane myScrollPane = new JScrollPane(myWrapper, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
			JPanel main = new JPanel(new BorderLayout());
			main.add(myScrollPane, BorderLayout.CENTER);
			main.add(footer, BorderLayout.SOUTH);
			setContentPane(main);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(520,560));
			setLocationRelativeTo(null);
			setVisible(true);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					WCharacterChart.this.uniBlocks = ResplUtils.getUnicodeBlocks();
					WCharacterChart.this.uniNames = ResplUtils.getUnicodeNames();
					WCharacterChart.this.uniCats = ResplUtils.getUnicodeCategories();
					WCharacterChart.this.update();
				}
			});
		}
		
		public void dispose() {
			ResplMain.removeResplendenceListener(myrl);
			ResplMain.removeWindowListener(mywl);
			super.dispose();
		}
		
		public Font getMyFont() {
			return myFont;
		}
		
		public void setMyFont(Font f) {
			myFont = f;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					WCharacterChart.this.update();
				}
			});
		}
		
		public void update() {
			myPanel.removeAll();
			
			Vector<Integer> starts = new Vector<Integer>();
			starts.addAll(uniBlocks.keySet());
			Collections.sort(starts);
			BitSet chars = CharInFont.getInstance().allCharsInFont(myFont.getName());
			
			Iterator<Integer> si = starts.iterator();
			int start = si.hasNext() ? si.next() : 0;
			while (si.hasNext()) {
				int end = si.next();
				String name = uniBlocks.get(start);
				
				int num = chars.get(start,end).cardinality();
				if (num > 0) {
					JPanel headerp = new JPanel(new FractionalSizeGridLayout(1,1));
					JLabel header = new JLabel(name+" ("+num+")");
					header.setFont(HEADER_FONT);
					header.setHorizontalAlignment(JLabel.LEFT);
					header.setOpaque(true);
					header.setBackground(HEADER_COLOR);
					header.setForeground(HEADER_TEXT);
					header.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
					headerp.add(header);
					myPanel.add(headerp);
					
					JPanel blockChars = new JPanel(new FractionalSizeGridLayout(0,1));
					for (int mstart = start, mend = start + 16; mstart < end; mstart += 16, mend += 16) {
						if (!chars.get(mstart,mend).isEmpty()) {
							JPanel rowChars = new JPanel(new FractionalSizeGridLayout(1,16));
							for (int ch = mstart; ch < mend; ch++) {
								if (chars.get(ch)) {
									JLabel l = new JLabel(new String(Character.toChars(ch)));
									l.setFont(myFont);
									l.setHorizontalAlignment(JLabel.CENTER);
									l.setOpaque(true);
									l.setBackground(Color.white);
									l.setForeground(Color.black);
									l.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
									ChartCellListener ll = new ChartCellListener(ch);
									l.addMouseListener(ll);
									l.addMouseMotionListener(ll);
									rowChars.add(l);
								} else {
									JLabel l = new JLabel();
									l.setOpaque(true);
									l.setBackground(Color.lightGray);
									rowChars.add(l);
								}
							}
							blockChars.add(rowChars);
						}
					}
					myPanel.add(blockChars);
				}
				
				start = end;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					WCharacterChart.this.validate();
				}
			});
		}
		
		private class ChartCellListener implements MouseListener, MouseMotionListener {
			private int i;
			
			public ChartCellListener(int i) {
				this.i = i;
			}
			
			public void mouseClicked(MouseEvent arg0) {
				if (ll != null) {
					ResplMain.sendResplendenceEventTo(ll, new ResplendenceEvent(arg0.getSource(), ResplendenceEvent.INSERT_TEXT, new String(Character.toChars(i)), new String(Character.toChars(i))));
				}
			}
			
			public void mouseEntered(MouseEvent arg0) {
				arg0.getComponent().setBackground(SystemColor.textHighlight);
				arg0.getComponent().setForeground(SystemColor.textHighlightText);
				
				String h = "000000"+Integer.toHexString(i).toUpperCase();
				String s = "U+"+h.substring(h.length()-((i >= 0x10000)?6:4))+"    #"+Integer.toString(i);
				String n = uniNames.get(i);
				if (n != null) s += "    "+n;
				s += "    "+unicodeType(i);
				footer.setText(s);
			}
			
			public void mouseExited(MouseEvent arg0) {
				arg0.getComponent().setBackground(Color.white);
				arg0.getComponent().setForeground(Color.black);
				
				footer.setText(" ");
			}
			
			public void mouseDragged(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseMoved(MouseEvent arg0) {}
			
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
		}
	}
	
	public static void main(String[] args) {
		Map<Integer,String> m = ResplUtils.getUnicodeBlocks();
		Vector<Integer> v = new Vector<Integer>();
		v.addAll(m.keySet());
		Collections.sort(v);
		for (int i : v) {
			System.out.println(Integer.toHexString(i).toUpperCase()+"\t"+m.get(i));
		}
	}
}
