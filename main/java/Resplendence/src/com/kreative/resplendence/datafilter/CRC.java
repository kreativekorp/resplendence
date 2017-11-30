package com.kreative.resplendence.datafilter;

import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

import com.kreative.resplendence.ResplUtils;
import com.kreative.util.ParameterizedCRC;

public class CRC implements DataFilter {
	public String category(int i) {
		return "CRC";
	}

	public byte[] filter(int i, byte[] b) {
		ParameterizedCRC crc;
		switch (i) {
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
		case 10:
			crc = CRCWindow.showCRCWindow(b);
			if (crc == null) return null;
			crc.reset();
			break;
		default: return null;
		}
		crc.update(b);
		long c = crc.getValue();
		int w = (int)(crc.getBitWidth()+7)/8;
		byte[] d = new byte[w];
		while (w > 0) {
			d[--w] = (byte)(c & 0xFF); c >>>= 8;
		}
		return d;
	}

	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		switch (i) {
		case 0: return "CRC-32";
		case 1: return "CRC-16 XMODEM";
		case 2: return "CRC-16 Kermit";
		case 3: return "CRC-16 ARC";
		case 4: return "CRC-16 I-CODE";
		case 5: return "CRC-16 X.25";
		case 6: return "CRC-16 USB";
		case 7: return "CRC-16 CCITT";
		case 8: return "CRC-16 MCRF4XX";
		case 9: return "CRC-16 Modbus";
		case 10: return "Custom...";
		default: return null;
		}
	}

	public int numberOfFilters() {
		return 11;
	}
	
	private static class CRCWindow extends JDialog implements ActionListener, DocumentListener, ItemListener {
		private static final long serialVersionUID = 1L;
		private ParameterizedCRC crc = null;
		private byte[] data = null;

		private JComboBox presets;
		private JTextField bitWidth;
		private JTextField polynomial;
		private JTextField initialValue;
		private JCheckBox reflectIn;
		private JCheckBox reflectOut;
		private JTextField xorValue;
		private JLabel check;
		private JLabel crcl;
		
		public static ParameterizedCRC showCRCWindow(byte[] data) {
			CRCWindow d = new CRCWindow(data);
			d.setVisible(true);
			return d.crc;
		}
		
		public CRCWindow(byte[] d) {
			super((Frame)null, "CRC Calculator", true);
			crc = null;
			data = d;
			JPanel main = new JPanel(new BorderLayout());
			
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
			main.add(form, BorderLayout.CENTER);
			
			JPanel buttons = new JPanel(new FlowLayout());
			JButton okButton = new JButton("OK");
			JButton cancelButton = new JButton("Cancel");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					crc = null;
					dispose();
				}
			});
			buttons.add(okButton);
			buttons.add(cancelButton);
			main.add(buttons, BorderLayout.PAGE_END);
			
			main.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
			setContentPane(main);
			getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(getRootPane(), cancelButton);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			itemStateChanged(null);
			pack();
			setSize(480, getHeight());
			setResizable(false);
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
				crc.update(data);
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
