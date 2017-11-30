package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

import com.kreative.resplendence.*;
import com.kreative.swing.*;
import com.kreative.swing.event.*;
import com.kreative.util.ParameterizedCRC;

public class CRCCalculator implements AccessoryWindow {
	private static WCRCCalculator instance;
	
	public String category(int i) {
		return null;
	}

	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "CRC Calculator";
	}

	public int numberOfWindows() {
		return 1;
	}

	public void open(int i) {
		if (instance == null) instance = new WCRCCalculator();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	private static class WCRCCalculator extends JFrame implements ActionListener, DocumentListener, HexDataChangeListener, ItemListener, ResplendenceListener {
		private static final long serialVersionUID = 1L;
		private ParameterizedCRC crc = null;

		private JHexEditorSuite hex;
		private JComboBox presets;
		private JTextField bitWidth;
		private JTextField polynomial;
		private JTextField initialValue;
		private JCheckBox reflectIn;
		private JCheckBox reflectOut;
		private JTextField xorValue;
		private JLabel check;
		private JLabel crcl;
		
		public WCRCCalculator() {
			super("CRC Calculator");
			ResplMain.addResplendenceListener(this, this);
			ResplMain.registerWindow(this,
					ResplMain.MENUS_IMPORT_EXPORT |
					ResplMain.MENUS_CUT_COPY_PASTE |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_GO_TO |
					ResplMain.MENUS_FORMAT |
					ResplMain.MENUS_TEXT_ENCODING |
					ResplMain.MENUS_COLOR_SCHEME |
					ResplMain.MENUS_BYTES_PER_ROW |
					ResplMain.MENUS_TEXT_FILTERS |
					ResplMain.MENUS_DATA_FILTERS
			);
			crc = null;
			JPanel main = new JPanel(new BorderLayout());
			
			hex = new JHexEditorSuite();
			hex.setIgnoreControlKeys(true);
			hex.addHexDataChangeListener(this);
			main.add(hex, BorderLayout.CENTER);
			String s = ResplPrefs.getString("Hex Color Scheme");
			if (s != null && s.length() > 0) {
				for (int i=0; i<JHexEditor.COLOR_SCHEME_COUNT; i++) {
					if (JHexEditor.COLOR_SCHEME_NAMES[i].equalsIgnoreCase(s)) {
						hex.setColorScheme(JHexEditor.COLOR_SCHEMES[i]);
						break;
					}
				}
			}
			
			JPanel form = new JPanel(new GridLayout(0,1));
			presets = new JComboBox(new String[]{
					"CRC-32",
					"CRC-16 XMODEM",
					"CRC-16 Kermit",
					"CRC-16 ARC",
					"CRC-16 I-CODE",
					"CRC-16 X.25",
					"CRC-16 USB",
					"CRC-16 CCITT",
					"CRC-16 MCRF4XX",
					"CRC-16 Modbus",
					"Custom"
			});
			presets.setEditable(false); presets.setMaximumRowCount(32); presets.addItemListener(this);
			form.add(makeLine("Presets:", 100, presets));
			bitWidth = new JTextField(5); bitWidth.getDocument().addDocumentListener(this);
			form.add(makeLine("Bit Width:", 100, bitWidth));
			polynomial = new JTextField(16); polynomial.getDocument().addDocumentListener(this);
			form.add(makeLine("Polynomial:", 100, polynomial));
			initialValue = new JTextField(16); initialValue.getDocument().addDocumentListener(this);
			form.add(makeLine("Initial Value:", 100, initialValue));
			reflectIn = new JCheckBox("Input Bytes"); reflectIn.addActionListener(this);
			reflectOut = new JCheckBox("Output Value"); reflectOut.addActionListener(this);
			form.add(makeLine("Reflect:", 100, reflectIn, reflectOut));
			xorValue = new JTextField(16); xorValue.getDocument().addDocumentListener(this);
			form.add(makeLine("Output XOR:", 100, xorValue));
			check = new JLabel();
			form.add(makeLine("Check:", 100, check));
			crcl = new JLabel();
			form.add(makeLine("CRC for Data:", 100, crcl));
			form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			main.add(form, BorderLayout.PAGE_END);
			
			setContentPane(main);
			itemStateChanged(null);
			pack();
			setSize(getWidth(), getHeight()+120);
			hex.setWidth(16);
			setLocationRelativeTo(null);
		}
		
		private static long LongparseLong(String s, int radix) {
			long l = 0;
			s = s.trim();
			boolean neg = (s.startsWith("-"));
			if (s.startsWith("-") || s.startsWith("+")) s = s.substring(1);
			if (s.length() == 0) throw new NumberFormatException();
			
			CharacterIterator i = new StringCharacterIterator(s);
			for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) {
				if (ch >= '0' && ch <= '9') l = l * radix + (ch-'0');
				else if (ch >= 'A' && ch <= 'Z') l = l * radix + (ch-'A'+10);
				else if (ch >= 'a' && ch <= 'z') l = l * radix + (ch-'a'+10);
				else throw new NumberFormatException();
			}
			return neg ? -l : l;
		}
		
		public void update() {
			try {
				long w = Long.parseLong(bitWidth.getText());
				long p = LongparseLong(polynomial.getText(), 16);
				long i = LongparseLong(initialValue.getText(), 16);
				boolean ri = reflectIn.isSelected();
				boolean ro = reflectOut.isSelected();
				long x = LongparseLong(xorValue.getText(), 16);
				crc = new ParameterizedCRC(w, p, i, ri, ro, x, 0);
				long ch = crc.calculatedCheckValue();
				crc.update(hex.getData());
				long cr = crc.getValue();
				int b = (int)((w+3)/4);
				String chs = "";
				String crs = "";
				for (int k = 0; k < b; k++) {
					chs += "0";
					crs += "0";
				}
				chs += Long.toHexString(ch).toUpperCase();
				crs += Long.toHexString(cr).toUpperCase();
				chs = chs.substring(chs.length() - b);
				crs = crs.substring(crs.length() - b);
				check.setText(chs);
				crcl.setText(crs);
			} catch (NumberFormatException nfe) {}
		}
		
		private boolean event = false;

		public void actionPerformed(ActionEvent e) {
			if (!event) {
				event = true;
				presets.setSelectedItem("Custom");
				update();
				event = false;
			}
		}

		public void changedUpdate(DocumentEvent e) {
			if (!event) {
				event = true;
				presets.setSelectedItem("Custom");
				update();
				event = false;
			}
		}

		public void insertUpdate(DocumentEvent e) {
			if (!event) {
				event = true;
				presets.setSelectedItem("Custom");
				update();
				event = false;
			}
		}

		public void removeUpdate(DocumentEvent e) {
			if (!event) {
				event = true;
				presets.setSelectedItem("Custom");
				update();
				event = false;
			}
		}

		public void itemStateChanged(ItemEvent e) {
			if (!event && (e == null || e.getStateChange() == ItemEvent.SELECTED)) {
				event = true;
				switch (presets.getSelectedIndex()) {
				case 0: crc = ParameterizedCRC.createCRC32(); break;
				case 1: crc = ParameterizedCRC.createCRC16XMODEM(); break;
				case 2: crc = ParameterizedCRC.createCRC16KERMIT(); break;
				case 3: crc = ParameterizedCRC.createCRC16ARC(); break;
				case 4: crc = ParameterizedCRC.createCRC16ICODE(); break;
				case 5: crc = ParameterizedCRC.createCRC16X25(); break;
				case 6: crc = ParameterizedCRC.createCRC16USB(); break;
				case 7: crc = ParameterizedCRC.createCRC16CCITT(); break;
				case 8: crc = ParameterizedCRC.createCRC16MCRF4XX(); break;
				case 9: crc = ParameterizedCRC.createCRC16MODBUS(); break;
				}
				bitWidth.setText(Long.toString(crc.getBitWidth()));
				polynomial.setText(Long.toHexString(crc.getPolynomial()).toUpperCase());
				initialValue.setText(Long.toHexString(crc.getInitialValue()).toUpperCase());
				reflectIn.setSelected(crc.getReflectIn());
				reflectOut.setSelected(crc.getReflectOut());
				xorValue.setText(Long.toHexString(crc.getOutputXOR()).toUpperCase());
				update();
				event = false;
			}
		}

		public void hexDataChanged(HexDataChangeEvent e) {
			update();
		}

		public Object respondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.EXPORT_FILE:
				try {
					RandomAccessFile raf = new RandomAccessFile(e.getFile(), "rwd");
					raf.seek(0);
					raf.setLength(0);
					raf.write(hex.getData());
					raf.close();
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "Could not export because an I/O error occurred.", "Export", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case ResplendenceEvent.IMPORT_FILE:
				try {
					RandomAccessFile raf = new RandomAccessFile(e.getFile(), "r");
					byte[] stuff = new byte[(int)Math.min(raf.length(), Integer.MAX_VALUE)];
					raf.seek(0);
					raf.read(stuff);
					raf.close();
					hex.setData(stuff);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "Could not import because an I/O error occurred.", "Import", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case ResplendenceEvent.CUT:
				if (hex.isOnDataSide()) {
					ResplScrap.setScrap(hex.getSelectedData());
				} else {
					ResplScrap.setScrap(hex.getSelectedHex());
				}
				hex.replaceSelection(new byte[0]);
				break;
			case ResplendenceEvent.COPY:
				if (hex.isOnDataSide()) {
					ResplScrap.setScrap(hex.getSelectedData());
				} else {
					ResplScrap.setScrap(hex.getSelectedHex());
				}
				break;
			case ResplendenceEvent.PASTE:
				if (hex.isOnDataSide()) {
					byte[] data = ResplScrap.getScrapData();
					if (data != null) hex.replaceSelection(data);
				} else {
					String s = ResplScrap.getScrapString();
					if (s != null) hex.replaceSelection(s);
				}
				int cursorIsAt1 = Math.max(hex.getSelectionStart(), hex.getSelectionEnd());
				hex.select(cursorIsAt1, cursorIsAt1);
				break;
			case ResplendenceEvent.PASTE_AFTER:
				if (hex.isOnDataSide()) {
					byte[] data = ResplScrap.getScrapData();
					if (data != null) hex.replaceSelection(data);
				} else {
					String s = ResplScrap.getScrapString();
					if (s != null) hex.replaceSelection(s);
				}
				int cursorIsAt2 = Math.min(hex.getSelectionStart(), hex.getSelectionEnd());
				hex.select(cursorIsAt2, cursorIsAt2);
				break;
			case ResplendenceEvent.CLEAR:
				hex.replaceSelection(new byte[0]);
				break;
			case ResplendenceEvent.SELECT_ALL:
				hex.selectAll();
				break;
			case ResplendenceEvent.GO_TO:
				hex.setIgnoreControlKeys(false);
				hex.getHexEditor().keyPressed(new KeyEvent(
						this,
						KeyEvent.KEY_PRESSED,
						0l,
						KeyEvent.META_DOWN_MASK,
						KeyEvent.VK_J,
						KeyEvent.CHAR_UNDEFINED
				));
				hex.setIgnoreControlKeys(true);
				break;
			case ResplendenceEvent.GET_FONT:
				return hex.getFont();
			case ResplendenceEvent.SET_FONT:
				hex.setFont(e.getFont());
				break;
			case ResplendenceEvent.GET_TEXT_ENCODING:
				return hex.getTextEncoding();
			case ResplendenceEvent.SET_TEXT_ENCODING:
				hex.setTextEncoding(e.getString());
				break;
			case ResplendenceEvent.GET_COLOR_SCHEME:
				return hex.getColorScheme();
			case ResplendenceEvent.SET_COLOR_SCHEME:
				hex.setColorScheme((Color[])e.getObject());
				break;
			case ResplendenceEvent.GET_BYTES_PER_ROW:
				return 0;
			case ResplendenceEvent.SET_BYTES_PER_ROW:
				hex.setWidth(e.getInt());
				break;
			case ResplendenceEvent.GET_SELECTED_DATA:
				return (hex.getSelectionStart()==hex.getSelectionEnd())?hex.getData():hex.getSelectedData();
			case ResplendenceEvent.SET_SELECTED_DATA:
				if (hex.getSelectionStart()==hex.getSelectionEnd()) {
					int ss = hex.getSelectionStart();
					hex.setData(e.getData());
					hex.setSelectionStart(ss);
					hex.setSelectionEnd(ss);
				} else {
					int ss = hex.getSelectionStart();
					hex.replaceSelection(e.getData());
					hex.setSelectionStart(ss);
				}
				return null;
			case ResplendenceEvent.GET_SELECTED_TEXT:
				byte[] outdata = (hex.getSelectionStart()==hex.getSelectionEnd())?hex.getData():hex.getSelectedData();
				try {
					return new String(outdata, hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					return new String(outdata);
				}
			case ResplendenceEvent.SET_SELECTED_TEXT:
				byte[] indata;
				try {
					indata = e.getString().getBytes(hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					indata = e.getString().getBytes();
				}
				if (hex.getSelectionStart()==hex.getSelectionEnd()) {
					int ss = hex.getSelectionStart();
					hex.setData(indata);
					hex.setSelectionStart(ss);
					hex.setSelectionEnd(ss);
				} else {
					int ss = hex.getSelectionStart();
					hex.replaceSelection(indata);
					hex.setSelectionStart(ss);
				}
				return null;
			case ResplendenceEvent.INSERT_TEXT:
				byte[] insdata;
				try {
					insdata = e.getString().getBytes(hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					insdata = e.getString().getBytes();
				}
				{
					int ss = hex.getSelectionStart();
					hex.replaceSelection(insdata);
					hex.setSelectionStart(ss+insdata.length);
					hex.setSelectionEnd(ss+insdata.length);
				}
				return null;
			case ResplendenceEvent.GET_CONTROL_PROCID:
			case ResplendenceEvent.GET_WINDOW_PROCID:
				if (hex.getSelectionLength() == 2) {
					byte[] b = hex.getSelectedData();
					return hex.isLittleEndian()?(((b[1]&0xFF)<<8)|(b[0]&0xFF)):(((b[0]&0xFF)<<8)|(b[1]&0xFF));
				}
				return null;
			case ResplendenceEvent.SET_CONTROL_PROCID:
			case ResplendenceEvent.SET_WINDOW_PROCID:
				if (e.getObject() instanceof Number && hex.getSelectionLength() == 2) {
					int i = ((Number)e.getObject()).intValue();
					byte[] b = hex.isLittleEndian()?(new byte[]{(byte)(i&0xFF),(byte)((i>>8)&0xFF)}):(new byte[]{(byte)((i>>8)&0xFF),(byte)(i&0xFF)});
					hex.replaceSelection(b);
				}
				return null;
			}
			return null;
		}
		
		private static JLabel makeFixedLabel(String s, int width) {
			JLabel l = new JLabel(s);
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
		
		private static JPanel makeLine(String s, int width, Component c1, Component c2) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.add(makeFixedLabel(s, width));
			p.add(Box.createHorizontalStrut(8));
			p.add(c1);
			p.add(c2);
			p.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			p.setAlignmentY(JPanel.TOP_ALIGNMENT);
			return p;
		}
	}
}
