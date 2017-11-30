package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.resplendence.*;

public class BaseConverter implements AccessoryWindow {
	private WBaseConverter instance;
	
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "Base Converter";
	}

	public void open(int i) {
		if (instance == null) instance = new WBaseConverter();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WBaseConverter extends JFrame implements DocumentListener, MouseListener {
		private static final long serialVersionUID = 1;
		private static final Font mono = new Font("Monospaced", Font.PLAIN, 12);
		
		private JTextField oct, dec, hex;
		private JBits bin;
		private boolean eventexec = false;
		
		public WBaseConverter() {
			super("Base Converter");
			ResplMain.registerWindow(this);
			JPanel main = new JPanel(new GridBagLayout());
			GridBagConstraints left = new GridBagConstraints();
			left.insets = new Insets(0,0,5,5);
			left.anchor = GridBagConstraints.EAST;
			left.gridx = 0;
			GridBagConstraints right = new GridBagConstraints();
			right.insets = new Insets(0,0,5,5);
			right.anchor = GridBagConstraints.WEST;
			right.gridx = 1;
			left.gridy = right.gridy = 0;
			main.add(new JLabel("Bin:"), left);
			main.add(bin = new JBits(64, 2, 32), right); bin.setFont(mono);
			left.gridy = right.gridy = 1;
			main.add(new JLabel("Oct:"), left);
			main.add(oct = new JTextField(22), right); oct.setFont(mono);
			left.gridy = right.gridy = 2;
			main.add(new JLabel("Dec:"), left);
			main.add(dec = new JTextField(20), right); dec.setFont(mono);
			left.gridy = right.gridy = 3;
			main.add(new JLabel("Hex:"), left);
			main.add(hex = new JTextField(16), right); hex.setFont(mono);
			Cursor pencil = ResplRsrcs.getCURS(1024);
			Cursor licnep = ResplRsrcs.getCURS(1025);
			JBit[] binb = bin.getBitArray();
			for (int i= 0; i<32; i++) binb[i].setCursor(pencil);
			for (int i=32; i<64; i++) binb[i].setCursor(licnep);
			bin.setPreferredSize(oct.getPreferredSize());
			bin.addMouseListener(this);
			oct.getDocument().addDocumentListener(this);
			dec.getDocument().addDocumentListener(this);
			hex.getDocument().addDocumentListener(this);
			main.setBorder(BorderFactory.createEmptyBorder(10, 12, 5, 7));
			setContentPane(main);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setResizable(false);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		private static long parseHex(String s) {
			long r = 0;
			boolean negate = false;
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
				if (ch == '-') negate = true;
				else if (ch >= '0' && ch <= '9') {
					r <<= 4; r |= (ch-'0');
				}
				else if (ch >= 'A' && ch <= 'F') {
					r <<= 4; r |= (ch-'A'+10);
				}
				else if (ch >= 'a' && ch <= 'f') {
					r <<= 4; r |= (ch-'a'+10);
				}
				else throw new NumberFormatException();
			}
			return negate?-r:r;
		}
		
		private static long parseOctal(String s) {
			long r = 0;
			boolean negate = false;
			CharacterIterator it = new StringCharacterIterator(s);
			for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
				if (ch == '-') negate = true;
				else if (ch >= '0' && ch <= '7') {
					r <<= 3; r |= (ch-'0');
				}
				else throw new NumberFormatException();
			}
			return negate?-r:r;
		}
		
		private void updateValues(Object src) {
			if (!eventexec) {
				eventexec = true;
				long v = 0;
				try {
					if (src instanceof JBit) {
						v = bin.getValue();
						oct.setText(Long.toOctalString(v));
						dec.setText(Long.toString(v));
						hex.setText(Long.toHexString(v).toUpperCase());
					} else if (src == oct.getDocument()) {
						if (oct.getText().length() < 1) {
							bin.setValue(0);
							dec.setText("");
							hex.setText("");
						} else {
							v = parseOctal(oct.getText());
							bin.setValue(v);
							dec.setText(Long.toString(v));
							hex.setText(Long.toHexString(v).toUpperCase());
						}
					} else if (src == dec.getDocument()) {
						if (dec.getText().length() < 1) {
							bin.setValue(0);
							oct.setText("");
							hex.setText("");
						} else {
							v = Long.parseLong(dec.getText());
							bin.setValue(v);
							oct.setText(Long.toOctalString(v));
							hex.setText(Long.toHexString(v).toUpperCase());
						}
					} else if (src == hex.getDocument()) {
						if (hex.getText().length() < 1) {
							bin.setValue(0);
							oct.setText("");
							dec.setText("");
						} else {
							v = parseHex(hex.getText());
							bin.setValue(v);
							oct.setText(Long.toOctalString(v));
							dec.setText(Long.toString(v));
						}
					}
				} catch (NumberFormatException nfe) {
					if (src instanceof JBit) {
						oct.setText("");
						dec.setText("");
						hex.setText("");
					} else if (src == oct.getDocument()) {
						if (oct.getText().length() < 1) {
							bin.setValue(0);
							dec.setText("");
							hex.setText("");
						}
					} else if (src == dec.getDocument()) {
						if (dec.getText().length() < 1) {
							bin.setValue(0);
							oct.setText("");
							hex.setText("");
						}
					} else if (src == hex.getDocument()) {
						if (hex.getText().length() < 1) {
							bin.setValue(0);
							oct.setText("");
							dec.setText("");
						}
					}
				}
				eventexec = false;
			}
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) { if (e.getButton() != MouseEvent.NOBUTTON) updateValues(e.getSource()); }
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) { updateValues(e.getSource()); }
		public void mouseReleased(MouseEvent e) { updateValues(e.getSource()); }
		
		public void changedUpdate(DocumentEvent e) {}
		public void insertUpdate(DocumentEvent e) { updateValues(e.getDocument()); }
		public void removeUpdate(DocumentEvent e) { updateValues(e.getDocument()); }
		
		private static class JBits extends JPanel {
			private static final long serialVersionUID = 1;
			private JBit[] bits;
			/*
			public JBits(int numbits) {
				this(numbits, 1, numbits);
			}
			public JBits(int rows, int cols) {
				this(rows*cols, rows, cols);
			}
			*/
			public JBits(int numbits, int rows, int cols) {
				super(new GridLayout(rows,cols,-1,-1));
				bits = new JBit[numbits];
				for (int i=0; i<bits.length; i++) {
					add(bits[i] = new JBit());
				}
				colorize();
			}
			public void setValue(long v) {
				for (int i=bits.length-1; i>=0; i--) {
					bits[i].setValue((v & 1) != 0);
					v >>>= 1;
				}
			}
			public long getValue() {
				long v = 0L;
				for (int i=0; i<bits.length; i++) {
					v <<= 1L;
					if (bits[i].getValue()) v |= 1L;
				}
				return v;
			}
			/*
			public void setOnColor(Color c) {
				for (int i=0; i<bits.length; i++) bits[i].setOnColor(c);
			}
			public void setOffColor(Color c) {
				for (int i=0; i<bits.length; i++) bits[i].setOffColor(c);
			}
			*/
			public void colorize() {
				switch (bits.length) {
				case 64:
					for (int i= 0; i< 8; i++) bits[i].setOnColor(Color.magenta.darker());
					for (int i= 8; i<16; i++) bits[i].setOnColor(Color.red.darker());
					for (int i=16; i<24; i++) bits[i].setOnColor(new Color(0xFF8000).darker());
					for (int i=24; i<32; i++) bits[i].setOnColor(Color.yellow.darker());
					for (int i=32; i<40; i++) bits[i].setOnColor(Color.green.darker());
					for (int i=40; i<48; i++) bits[i].setOnColor(Color.cyan.darker());
					for (int i=48; i<56; i++) bits[i].setOnColor(Color.blue.darker());
					for (int i=56; i<64; i++) bits[i].setOnColor(new Color(0x8000FF).darker());
					break;
				case 48:
					for (int i= 0; i< 8; i++) bits[i].setOnColor(Color.magenta.darker());
					for (int i= 8; i<16; i++) bits[i].setOnColor(Color.red.darker());
					for (int i=16; i<24; i++) bits[i].setOnColor(Color.yellow.darker());
					for (int i=24; i<32; i++) bits[i].setOnColor(Color.green.darker());
					for (int i=32; i<40; i++) bits[i].setOnColor(Color.cyan.darker());
					for (int i=40; i<48; i++) bits[i].setOnColor(Color.blue.darker());
					break;
				case 32:
					for (int i= 0; i< 8; i++) bits[i].setOnColor(Color.red.darker());
					for (int i= 8; i<16; i++) bits[i].setOnColor(Color.yellow.darker());
					for (int i=16; i<24; i++) bits[i].setOnColor(Color.green.darker());
					for (int i=24; i<32; i++) bits[i].setOnColor(Color.blue.darker());
					break;
				case 24:
					for (int i= 0; i< 8; i++) bits[i].setOnColor(Color.red.darker());
					for (int i= 8; i<16; i++) bits[i].setOnColor(Color.green.darker());
					for (int i=16; i<24; i++) bits[i].setOnColor(Color.blue.darker());
					break;
				case 16:
					for (int i= 0; i< 8; i++) bits[i].setOnColor(Color.red.darker());
					for (int i= 8; i<16; i++) bits[i].setOnColor(Color.blue.darker());
					break;
				}
			}
			public JBit[] getBitArray() {
				return bits;
			}
			public void addMouseListener(MouseListener l) {
				for (int i=0; i<bits.length; i++) bits[i].addMouseListener(l);
			}
			public void removeMouseListener(MouseListener l) {
				for (int i=0; i<bits.length; i++) bits[i].removeMouseListener(l);
			}
			public MouseListener[] getMouseListeners() {
				return bits[0].getMouseListeners();
			}
		}
		
		private static class JBit extends JPanel implements MouseListener {
			private static final long serialVersionUID = 1;
			private Color on = Color.black;
			private Color off = Color.white;
			private static boolean lastclick = true;
			public JBit() {
				this.setOpaque(true);
				this.setBackground(off);
				this.setBorder(BorderFactory.createLineBorder(Color.black));
				this.addMouseListener(this);
			}
			public void setValue(boolean b) {
				this.setBackground(b?on:off);
			}
			public boolean getValue() {
				Color c = this.getBackground();
				if (c.equals(on)) return true;
				else if (c.equals(off)) return false;
				else return ((c.getRed()+c.getGreen()+c.getBlue())<384);
			}
			public void setOnColor(Color c) {
				boolean b = getValue();
				on = c;
				setValue(b);
			}
			/*
			public void setOffColor(Color c) {
				boolean b = getValue();
				off = c;
				setValue(b);
			}
			*/
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {
				if (e.getButton() != MouseEvent.NOBUTTON) setValue(lastclick);
			}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				setValue(lastclick = !getValue());
			}
			public void mouseReleased(MouseEvent e) {}
		}
	}
}
