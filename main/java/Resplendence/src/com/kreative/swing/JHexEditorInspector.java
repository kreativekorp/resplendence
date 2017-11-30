/*
 * Copyright &copy; 2007-2009 Rebecca G. Bettencourt / Kreative Software
 * <p>
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <a href="http://www.mozilla.org/MPL/">http://www.mozilla.org/MPL/</a>
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Alternatively, the contents of this file may be used under the terms
 * of the GNU Lesser General Public License (the "LGPL License"), in which
 * case the provisions of LGPL License are applicable instead of those
 * above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the LGPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the LGPL License.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */

package com.kreative.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import com.kreative.swing.event.*;
import com.kreative.util.CustomFloats;

public class JHexEditorInspector extends JPanel
implements HexDataChangeListener, HexModeChangeListener, HexSelectionChangeListener {
	private static final long serialVersionUID = 1;
	
	public static final int NUMBER_BASE_BINARY = 2;
	public static final int NUMBER_BASE_OCTAL = 8;
	public static final int NUMBER_BASE_DECIMAL = 10;
	public static final int NUMBER_BASE_HEXADECIMAL = 16;
	
	public static final boolean ENDIANNESS_LITTLE = true;
	public static final boolean ENDIANNESS_BIG = false;
	
	public static final int COLOR_FORMAT_MIN = 0;
	public static final int COLOR_FORMAT_ARGB = 0;
	public static final int COLOR_FORMAT_RGBA = 1;
	public static final int COLOR_FORMAT_ABGR = 2;
	public static final int COLOR_FORMAT_BGRA = 3;
	public static final int COLOR_FORMAT_MAX = 3;
	
	public static final int EPOCH_POSIX = 1970;
	public static final int EPOCH_MACINTOSH = 1904;
	public static final int EPOCH_PALMOS = 1904;
	public static final int EPOCH_WINDOWS = 1601;
	public static final int EPOCH_MS_DOS = 1980;
	public static final int EPOCH_IBM_S390 = 1900;
	public static final int EPOCH_NTP = 1900;
	public static final int EPOCH_AMIGAOS = 1978;
	public static final int EPOCH_SYMBIAN = 1;
	
	private JHexEditor he;
	private int numberBase = 10;
	private int colorFormat = COLOR_FORMAT_ARGB;
	private int epoch = 1970;
	private int timescale = 1000;
	private boolean reading = false;
	private Object writing = null;
	private JHexEditorFooter footer;
	private JHexEditorPanel panel;
	private JHexEditorScrollPane spanel;
	private Component lastItem;
	
	private JHexEditorLabel signedLbl, unsignedLbl, fixedLbl, floatLbl;
	private JTextField signedFld, unsignedFld, fixedFld, floatFld;
	private JHexEditorLabel rgbLbl, rgbaLbl, rgbaaLbl;
	private JColorSwatch rgbFld, rgbaFld, rgbaaFld;
	private JHexEditorLabel dateLbl;
	private SpinnerDateModel dateModel;
	private JSpinner.DateEditor dateEditor;
	private JSpinner dateFld;
	
	public JHexEditorInspector(JHexEditor he) {
		super(new BorderLayout());
		this.he = he;
		final JHexEditor he2 = he;
		panel = new JHexEditorPanel(new GridBagLayout()); // ...totally
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(1,0,0,8);
		c.weighty = 0.0; c.gridy = -1;
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(signedLbl = new JHexEditorLabel("Signed"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(signedFld = new JHexEditorField(20),c);
		signedFld.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection(Long.parseLong(e.getDocument().getText(0,e.getDocument().getLength()), numberBase));
					} catch (Exception ex) {}
					writing = null;
				}
			}
			public void removeUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection(Long.parseLong(e.getDocument().getText(0,e.getDocument().getLength()), numberBase));
					} catch (Exception ex) {}
					writing = null;
				}
			}
		});
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(unsignedLbl = new JHexEditorLabel("Unsigned"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(unsignedFld = new JHexEditorField(20),c);
		unsignedFld.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection(Long.parseLong(e.getDocument().getText(0,e.getDocument().getLength()), numberBase));
					} catch (Exception ex) {}
					writing = null;
				}
			}
			public void removeUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection(Long.parseLong(e.getDocument().getText(0,e.getDocument().getLength()), numberBase));
					} catch (Exception ex) {}
					writing = null;
				}
			}
		});
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(fixedLbl = new JHexEditorLabel("Fixed"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(fixedFld = new JHexEditorField(24),c);
		fixedFld.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection((long)(Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()))*Math.pow(16.0, he2.getSelectionLength())));
					} catch (Exception ex) {}
					writing = null;
				}
			}
			public void removeUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						he2.replaceSelection((long)(Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()))*Math.pow(16.0, he2.getSelectionLength())));
					} catch (Exception ex) {}
					writing = null;
				}
			}
		});
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(floatLbl = new JHexEditorLabel("Float"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(floatFld = new JHexEditorField(24),c);
		floatFld.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						switch (he2.getSelectionLength()) {
						case 1:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 4, 3));
							break;
						case 2:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 5, 10));
							break;
						case 3:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 7, 16));
							break;
						case 4:
							he2.replaceSelection(Float.floatToRawIntBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength()))));
							break;
						case 8:
							he2.replaceSelection(Double.doubleToRawLongBits(Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()))));
							break;
						}
					} catch (Exception ex) {}
					writing = null;
				}
			}
			public void removeUpdate(DocumentEvent e) {
				if (writing == null && !reading) {
					writing = e.getDocument();
					try {
						switch (he2.getSelectionLength()) {
						case 1:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 4, 3));
							break;
						case 2:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 5, 10));
							break;
						case 3:
							he2.replaceSelection(CustomFloats.customFloatToRawBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength())), 7, 16));
							break;
						case 4:
							he2.replaceSelection(Float.floatToRawIntBits(Float.parseFloat(e.getDocument().getText(0,e.getDocument().getLength()))));
							break;
						case 8:
							he2.replaceSelection(Double.doubleToRawLongBits(Double.parseDouble(e.getDocument().getText(0,e.getDocument().getLength()))));
							break;
						}
					} catch (Exception ex) {}
					writing = null;
				}
			}
		});
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbLbl = new JHexEditorLabel("RGB"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbFld = new JColorSwatch(),c);
		rgbFld.addColorChangeListener(new ColorChangeListener() {
			public void colorChanged(ColorChangeEvent e) {
				if (writing == null && !reading) {
					writing = e.getSource();
					he2.replaceSelection(encodeColor(e.getNewColor(), he2.getSelectionLength(), false, false));
					writing = null;
				}
			}
		});

		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbaLbl = new JHexEditorLabel("RGB+A"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbaFld = new JColorSwatch(),c);
		rgbaFld.addColorChangeListener(new ColorChangeListener() {
			public void colorChanged(ColorChangeEvent e) {
				if (writing == null && !reading) {
					writing = e.getSource();
					he2.replaceSelection(encodeColor(e.getNewColor(), he2.getSelectionLength(), true, false));
					writing = null;
				}
			}
		});

		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbaaLbl = new JHexEditorLabel("RGB-A"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(rgbaaFld = new JColorSwatch(),c);
		rgbaaFld.addColorChangeListener(new ColorChangeListener() {
			public void colorChanged(ColorChangeEvent e) {
				if (writing == null && !reading) {
					writing = e.getSource();
					he2.replaceSelection(encodeColor(e.getNewColor(), he2.getSelectionLength(), true, true));
					writing = null;
				}
			}
		});

		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 1; c.gridheight = 1;
		panel.add(dateLbl = new JHexEditorLabel("Date"),c);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(dateFld = new JSpinner(dateModel = new SpinnerDateModel()),c);
		dateEditor = new JSpinner.DateEditor(dateFld, "EEE d MMM yyyy GGG h:mm:ss.SSS aaa Z");
		dateFld.setEditor(dateEditor);
		HexDisplayChangeListener dateDispListener = new HexDisplayChangeListener() {
			public void hexDisplayChanged(HexDisplayChangeEvent e) {
				dateFld.setFont(he2.getFont());
				dateFld.setBackground(he2.getColorScheme()[3]);
				dateFld.setForeground(he2.getColorScheme()[5]);
				dateEditor.setFont(he2.getFont());
				dateEditor.setBackground(he2.getColorScheme()[3]);
				dateEditor.setForeground(he2.getColorScheme()[5]);
				dateEditor.getTextField().setFont(he2.getFont());
				dateEditor.getTextField().setBackground(he2.getColorScheme()[3]);
				dateEditor.getTextField().setForeground(he2.getColorScheme()[5]);
				dateEditor.getTextField().setSelectionColor(he2.getColorScheme()[14]);
				dateEditor.getTextField().setSelectedTextColor(he2.getColorScheme()[18]);
			}
		};
		he.addHexDisplayChangeListener(dateDispListener);
		dateDispListener.hexDisplayChanged(null);
		dateFld.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (writing == null && !reading) {
					writing = dateFld;
					Calendar ep = new GregorianCalendar(epoch,0,1);
					he2.replaceSelection((dateModel.getDate().getTime()-ep.getTimeInMillis())/timescale);
					writing = null;
				}
			}
		});
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 2; c.gridheight = 1;
		c.weighty = 1.0;
		c.insets = new Insets(0,0,0,0);
		panel.add(lastItem = Box.createGlue(),c);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
		spanel = new JHexEditorScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spanel.setPreferredSize(new Dimension(spanel.getPreferredSize().width, 88));
		spanel.getVerticalScrollBar().setUnitIncrement(8);
		spanel.getVerticalScrollBar().setBlockIncrement(80);
		spanel.setVisible(false);
		this.add(spanel, BorderLayout.CENTER);
		this.add(footer = new JHexEditorFooter(), BorderLayout.PAGE_END);
		he.addHexDataChangeListener(this);
		he.addHexModeChangeListener(this);
		he.addHexSelectionChangeListener(this);
		updateFields();
	}
	
	public void setLabelDisplay(JLabel label) {
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setHorizontalTextPosition(JLabel.LEFT);
		label.setFont(he.getFont());
		label.setBackground(he.getColorScheme()[26]);
		label.setForeground(he.getColorScheme()[27]);
	}
	
	public void setTextComponentDisplay(JTextComponent tc) {
		tc.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		tc.setFont(he.getFont());
		tc.setBackground(he.getColorScheme()[3]);
		tc.setForeground(he.getColorScheme()[5]);
		tc.setSelectionColor(he.getColorScheme()[14]);
		tc.setSelectedTextColor(he.getColorScheme()[18]);
	}
	
	public JLabel createCustomInspectorLabel(String text) {
		return new JHexEditorLabel(text);
	}
	
	public JTextField createCustomInspectorTextField() {
		return new JHexEditorField();
	}
	
	public JTextField createCustomInspectorTextField(int width) {
		return new JHexEditorField(width);
	}
	
	public JTextField createCustomInspectorTextField(String text) {
		return new JHexEditorField(text);
	}
	
	public JTextField createCustomInspectorTextField(String text, int width) {
		return new JHexEditorField(text, width);
	}
	
	public void addCustomInspectorField(JLabel name, Component field) {
		GridBagConstraints c = ((GridBagLayout)panel.getLayout()).getConstraints(lastItem);
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(1,0,0,8);
		c.weighty = 0.0;
		panel.remove(lastItem);
		
		c.weightx = 0.0; c.gridx = 0; c.gridwidth = 1; c.gridheight = 1;
		panel.add(name,c);
		if (name instanceof HexDataChangeListener) he.addHexDataChangeListener((HexDataChangeListener)name);
		if (name instanceof HexSelectionChangeListener) he.addHexSelectionChangeListener((HexSelectionChangeListener)name);
		if (name instanceof HexModeChangeListener) he.addHexModeChangeListener((HexModeChangeListener)name);
		if (name instanceof HexDisplayChangeListener) he.addHexDisplayChangeListener((HexDisplayChangeListener)name);
		if (name instanceof HexAccumulatorListener) he.addHexAccumulatorListener((HexAccumulatorListener)name);
		c.weightx = 1.0; c.gridx = 1; c.gridwidth = 1; c.gridheight = 1;
		panel.add(field,c);
		if (field instanceof HexDataChangeListener) he.addHexDataChangeListener((HexDataChangeListener)field);
		if (field instanceof HexSelectionChangeListener) he.addHexSelectionChangeListener((HexSelectionChangeListener)field);
		if (field instanceof HexModeChangeListener) he.addHexModeChangeListener((HexModeChangeListener)field);
		if (field instanceof HexDisplayChangeListener) he.addHexDisplayChangeListener((HexDisplayChangeListener)field);
		if (field instanceof HexAccumulatorListener) he.addHexAccumulatorListener((HexAccumulatorListener)field);
		
		c.weightx = 0.0; c.gridx = 0; c.gridy++; c.gridwidth = 2; c.gridheight = 1;
		c.weighty = 1.0;
		c.insets = new Insets(0,0,0,0);
		panel.add(lastItem,c);
	}
	
	public boolean isExpanded() {
		return spanel.isVisible();
	}
	
	public boolean isCollapsed() {
		return !spanel.isVisible();
	}
	
	public void setExpanded(boolean expanded) {
		if (spanel != null) {
			spanel.setVisible(expanded);
			footer.updateText();
		}
	}
	
	public void setCollapsed(boolean collapsed) {
		if (spanel != null) {
			spanel.setVisible(!collapsed);
			footer.updateText();
		}
	}
	
	public JHexEditor getHexEditor() {
		return he;
	}
	
	public JLabel getHexEditorFooter() {
		return footer;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public JScrollPane getScrollPane() {
		return spanel;
	}
	
	private long unsigned(long l, int len) {
		return l & ((-1L) >>> ((8-len) << 3));
	}
	
	private long signed(long l, int len) {
		long s = l & (0x80L << ((len-1) << 3));
		l &= ((-1L) >>> ((8-len) << 3));
		if (s > 0) l |= (0x8000000000000000L >> ((8-len) << 3));
		return l;
	}
	
	private Color decodeColor(long bits, int numbytes, boolean hasalpha, boolean invalpha) {
		float r=0.0f, g=0.0f, b=0.0f, a=invalpha?1.0f:0.0f;
		switch (numbytes) {
		case 1:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0x80) >>> 7) / 1.0f;
					r = ((bits & 0x60) >>> 5) / 3.0f;
					g = ((bits & 0x1C) >>> 2) / 7.0f;
					b = ((bits & 0x03) >>> 0) / 3.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xC0) >>> 6) / 3.0f;
					g = ((bits & 0x38) >>> 3) / 7.0f;
					b = ((bits & 0x06) >>> 1) / 3.0f;
					a = ((bits & 0x01) >>> 0) / 1.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0x80) >>> 7) / 1.0f;
					b = ((bits & 0x60) >>> 5) / 3.0f;
					g = ((bits & 0x1C) >>> 2) / 7.0f;
					r = ((bits & 0x03) >>> 0) / 3.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xC0) >>> 6) / 3.0f;
					g = ((bits & 0x38) >>> 3) / 7.0f;
					r = ((bits & 0x06) >>> 1) / 3.0f;
					a = ((bits & 0x01) >>> 0) / 1.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xC0) >>> 6) / 3.0f;
					g = ((bits & 0x38) >>> 3) / 7.0f;
					b = ((bits & 0x07) >>> 0) / 7.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xE0) >>> 5) / 7.0f;
					g = ((bits & 0x1C) >>> 2) / 7.0f;
					r = ((bits & 0x03) >>> 0) / 3.0f;
					break;
				}
				return new Color(r,g,b);
			}
		case 2:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0x8000) >>> 15) / 1.0f;
					r = ((bits & 0x7C00) >>> 10) / 31.0f;
					g = ((bits & 0x03E0) >>>  5) / 31.0f;
					b = ((bits & 0x001F) >>>  0) / 31.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xF800) >>> 11) / 31.0f;
					g = ((bits & 0x07C0) >>>  6) / 31.0f;
					b = ((bits & 0x003E) >>>  1) / 31.0f;
					a = ((bits & 0x0001) >>>  0) / 1.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0x8000) >>> 15) / 1.0f;
					b = ((bits & 0x7C00) >>> 10) / 31.0f;
					g = ((bits & 0x03E0) >>>  5) / 31.0f;
					r = ((bits & 0x001F) >>>  0) / 31.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xF800) >>> 11) / 31.0f;
					g = ((bits & 0x07C0) >>>  6) / 31.0f;
					r = ((bits & 0x003E) >>>  1) / 31.0f;
					a = ((bits & 0x0001) >>>  0) / 1.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xF800) >>> 11) / 31.0f;
					g = ((bits & 0x07E0) >>>  5) / 63.0f;
					b = ((bits & 0x001F) >>>  0) / 31.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xF800) >>> 11) / 31.0f;
					g = ((bits & 0x07E0) >>>  5) / 63.0f;
					r = ((bits & 0x001F) >>>  0) / 31.0f;
					break;
				}
				return new Color(r,g,b);
			}
		case 3:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0xFC0000) >>> 18) / 63.0f;
					r = ((bits & 0x03F000) >>> 12) / 63.0f;
					g = ((bits & 0x000FC0) >>>  6) / 63.0f;
					b = ((bits & 0x00003F) >>>  0) / 63.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFC0000) >>> 18) / 63.0f;
					g = ((bits & 0x03F000) >>> 12) / 63.0f;
					b = ((bits & 0x000FC0) >>>  6) / 63.0f;
					a = ((bits & 0x00003F) >>>  0) / 63.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0xFC0000) >>> 18) / 63.0f;
					b = ((bits & 0x03F000) >>> 12) / 63.0f;
					g = ((bits & 0x000FC0) >>>  6) / 63.0f;
					r = ((bits & 0x00003F) >>>  0) / 63.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFC0000) >>> 18) / 63.0f;
					g = ((bits & 0x03F000) >>> 12) / 63.0f;
					r = ((bits & 0x000FC0) >>>  6) / 63.0f;
					a = ((bits & 0x00003F) >>>  0) / 63.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFF0000) >>> 16) / 255.0f;
					g = ((bits & 0x00FF00) >>>  8) / 255.0f;
					b = ((bits & 0x0000FF) >>>  0) / 255.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFF0000) >>> 16) / 255.0f;
					g = ((bits & 0x00FF00) >>>  8) / 255.0f;
					r = ((bits & 0x0000FF) >>>  0) / 255.0f;
					break;
				}
				return new Color(r,g,b);
			}
		case 4:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0xFF000000) >>> 24) / 255.0f;
					r = ((bits & 0x00FF0000) >>> 16) / 255.0f;
					g = ((bits & 0x0000FF00) >>>  8) / 255.0f;
					b = ((bits & 0x000000FF) >>>  0) / 255.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFF000000) >>> 24) / 255.0f;
					g = ((bits & 0x00FF0000) >>> 16) / 255.0f;
					b = ((bits & 0x0000FF00) >>>  8) / 255.0f;
					a = ((bits & 0x000000FF) >>>  0) / 255.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0xFF000000) >>> 24) / 255.0f;
					b = ((bits & 0x00FF0000) >>> 16) / 255.0f;
					g = ((bits & 0x0000FF00) >>>  8) / 255.0f;
					r = ((bits & 0x000000FF) >>>  0) / 255.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFF000000) >>> 24) / 255.0f;
					g = ((bits & 0x00FF0000) >>> 16) / 255.0f;
					r = ((bits & 0x0000FF00) >>>  8) / 255.0f;
					a = ((bits & 0x000000FF) >>>  0) / 255.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFFC00000) >>> 22) / 1023.0f;
					g = ((bits & 0x003FF800) >>> 11) / 2047.0f;
					b = ((bits & 0x000007FF) >>>  0) / 2047.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFFE00000) >>> 21) / 2047.0f;
					g = ((bits & 0x001FFC00) >>> 10) / 2047.0f;
					r = ((bits & 0x000003FF) >>>  0) / 1023.0f;
					break;
				}
				return new Color(r,g,b);
			}
		case 6:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0xFFF000000000l) >>> 36) / 4095.0f;
					r = ((bits & 0x000FFF000000l) >>> 24) / 4095.0f;
					g = ((bits & 0x000000FFF000l) >>> 12) / 4095.0f;
					b = ((bits & 0x000000000FFFl) >>>  0) / 4095.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFFF000000000l) >>> 36) / 4095.0f;
					g = ((bits & 0x000FFF000000l) >>> 24) / 4095.0f;
					b = ((bits & 0x000000FFF000l) >>> 12) / 4095.0f;
					a = ((bits & 0x000000000FFFl) >>>  0) / 4095.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0xFFF000000000l) >>> 36) / 4095.0f;
					b = ((bits & 0x000FFF000000l) >>> 24) / 4095.0f;
					g = ((bits & 0x000000FFF000l) >>> 12) / 4095.0f;
					r = ((bits & 0x000000000FFFl) >>>  0) / 4095.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFFF000000000l) >>> 36) / 4095.0f;
					g = ((bits & 0x000FFF000000l) >>> 24) / 4095.0f;
					r = ((bits & 0x000000FFF000l) >>> 12) / 4095.0f;
					a = ((bits & 0x000000000FFFl) >>>  0) / 4095.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFFFF00000000l) >>> 32) / 65535.0f;
					g = ((bits & 0x0000FFFF0000l) >>> 16) / 65535.0f;
					b = ((bits & 0x00000000FFFFl) >>>  0) / 65535.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFFFF00000000l) >>> 32) / 65535.0f;
					g = ((bits & 0x0000FFFF0000l) >>> 16) / 65535.0f;
					r = ((bits & 0x00000000FFFFl) >>>  0) / 65535.0f;
					break;
				}
				return new Color(r,g,b);
			}
		case 8:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					a = ((bits & 0xFFFF000000000000l) >>> 48) / 65535.0f;
					r = ((bits & 0x0000FFFF00000000l) >>> 32) / 65535.0f;
					g = ((bits & 0x00000000FFFF0000l) >>> 16) / 65535.0f;
					b = ((bits & 0x000000000000FFFFl) >>>  0) / 65535.0f;
					break;
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFFFF000000000000l) >>> 48) / 65535.0f;
					g = ((bits & 0x0000FFFF00000000l) >>> 32) / 65535.0f;
					b = ((bits & 0x00000000FFFF0000l) >>> 16) / 65535.0f;
					a = ((bits & 0x000000000000FFFFl) >>>  0) / 65535.0f;
					break;
				case COLOR_FORMAT_ABGR:
					a = ((bits & 0xFFFF000000000000l) >>> 48) / 65535.0f;
					b = ((bits & 0x0000FFFF00000000l) >>> 32) / 65535.0f;
					g = ((bits & 0x00000000FFFF0000l) >>> 16) / 65535.0f;
					r = ((bits & 0x000000000000FFFFl) >>>  0) / 65535.0f;
					break;
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFFFF000000000000l) >>> 48) / 65535.0f;
					g = ((bits & 0x0000FFFF00000000l) >>> 32) / 65535.0f;
					r = ((bits & 0x00000000FFFF0000l) >>> 16) / 65535.0f;
					a = ((bits & 0x000000000000FFFFl) >>>  0) / 65535.0f;
					break;
				}
				return new Color(r,g,b,invalpha?(1.0f-a):a);
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					r = ((bits & 0xFFFFF80000000000l) >>> 43) / 2097151.0f;
					g = ((bits & 0x000007FFFFE00000l) >>> 21) / 4194303.0f;
					b = ((bits & 0x00000000001FFFFFl) >>>  0) / 2097151.0f;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					b = ((bits & 0xFFFFF80000000000l) >>> 43) / 2097151.0f;
					g = ((bits & 0x000007FFFFE00000l) >>> 21) / 4194303.0f;
					r = ((bits & 0x00000000001FFFFFl) >>>  0) / 2097151.0f;
					break;
				}
				return new Color(r,g,b);
			}
		}
		return null;
	}
	
	private long encodeColor(Color c, int numbytes, boolean hasalpha, boolean invalpha) {
		float[] cc = c.getRGBComponents(null);
		float r = cc[0];
		float g = cc[1];
		float b = cc[2];
		float a = invalpha?(1.0f-cc[3]):cc[3];
		long bits = 0L;
		switch (numbytes) {
		case 1:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)(1.0f*a) & 0x01) << 7;
					bits |= ((long)(3.0f*r) & 0x03) << 5;
					bits |= ((long)(7.0f*g) & 0x07) << 2;
					bits |= ((long)(3.0f*b) & 0x03) << 0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(3.0f*r) & 0x03) << 6;
					bits |= ((long)(7.0f*g) & 0x07) << 3;
					bits |= ((long)(3.0f*b) & 0x03) << 1;
					bits |= ((long)(1.0f*a) & 0x01) << 0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)(1.0f*a) & 0x01) << 7;
					bits |= ((long)(3.0f*b) & 0x03) << 5;
					bits |= ((long)(7.0f*g) & 0x07) << 2;
					bits |= ((long)(3.0f*r) & 0x03) << 0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(3.0f*b) & 0x03) << 6;
					bits |= ((long)(7.0f*g) & 0x07) << 3;
					bits |= ((long)(3.0f*r) & 0x03) << 1;
					bits |= ((long)(1.0f*a) & 0x01) << 0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(3.0f*r) & 0x03) << 6;
					bits |= ((long)(7.0f*g) & 0x07) << 3;
					bits |= ((long)(7.0f*b) & 0x07) << 0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(7.0f*b) & 0x07) << 5;
					bits |= ((long)(7.0f*g) & 0x07) << 2;
					bits |= ((long)(3.0f*r) & 0x03) << 0;
					break;
				}
			}
			break;
		case 2:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)( 1.0f*a) & 0x01) << 15;
					bits |= ((long)(31.0f*r) & 0x1F) << 10;
					bits |= ((long)(31.0f*g) & 0x1F) <<  5;
					bits |= ((long)(31.0f*b) & 0x1F) <<  0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(31.0f*r) & 0x1F) << 11;
					bits |= ((long)(31.0f*g) & 0x1F) <<  6;
					bits |= ((long)(31.0f*b) & 0x1F) <<  1;
					bits |= ((long)( 1.0f*a) & 0x01) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)( 1.0f*a) & 0x01) << 15;
					bits |= ((long)(31.0f*b) & 0x1F) << 10;
					bits |= ((long)(31.0f*g) & 0x1F) <<  5;
					bits |= ((long)(31.0f*r) & 0x1F) <<  0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(31.0f*b) & 0x1F) << 11;
					bits |= ((long)(31.0f*g) & 0x1F) <<  6;
					bits |= ((long)(31.0f*r) & 0x1F) <<  1;
					bits |= ((long)( 1.0f*a) & 0x01) <<  0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(31.0f*r) & 0x1F) << 11;
					bits |= ((long)(63.0f*g) & 0x3F) <<  5;
					bits |= ((long)(31.0f*b) & 0x1F) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(31.0f*b) & 0x1F) << 11;
					bits |= ((long)(63.0f*g) & 0x3F) <<  5;
					bits |= ((long)(31.0f*r) & 0x1F) <<  0;
					break;
				}
			}
			break;
		case 3:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)(63.0f*a) & 0x3F) << 18;
					bits |= ((long)(63.0f*r) & 0x3F) << 12;
					bits |= ((long)(63.0f*g) & 0x3F) <<  6;
					bits |= ((long)(63.0f*b) & 0x3F) <<  0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(63.0f*r) & 0x3F) << 18;
					bits |= ((long)(63.0f*g) & 0x3F) << 12;
					bits |= ((long)(63.0f*b) & 0x3F) <<  6;
					bits |= ((long)(63.0f*a) & 0x3F) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)(63.0f*a) & 0x3F) << 18;
					bits |= ((long)(63.0f*b) & 0x3F) << 12;
					bits |= ((long)(63.0f*g) & 0x3F) <<  6;
					bits |= ((long)(63.0f*r) & 0x3F) <<  0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(63.0f*b) & 0x3F) << 18;
					bits |= ((long)(63.0f*g) & 0x3F) << 12;
					bits |= ((long)(63.0f*r) & 0x3F) <<  6;
					bits |= ((long)(63.0f*a) & 0x3F) <<  0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(255.0f*r) & 0xFF) << 16;
					bits |= ((long)(255.0f*g) & 0xFF) <<  8;
					bits |= ((long)(255.0f*b) & 0xFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(255.0f*b) & 0xFF) << 16;
					bits |= ((long)(255.0f*g) & 0xFF) <<  8;
					bits |= ((long)(255.0f*r) & 0xFF) <<  0;
					break;
				}
			}
			break;
		case 4:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)(255.0f*a) & 0xFF) << 24;
					bits |= ((long)(255.0f*r) & 0xFF) << 16;
					bits |= ((long)(255.0f*g) & 0xFF) <<  8;
					bits |= ((long)(255.0f*b) & 0xFF) <<  0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(255.0f*r) & 0xFF) << 24;
					bits |= ((long)(255.0f*g) & 0xFF) << 16;
					bits |= ((long)(255.0f*b) & 0xFF) <<  8;
					bits |= ((long)(255.0f*a) & 0xFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)(255.0f*a) & 0xFF) << 24;
					bits |= ((long)(255.0f*b) & 0xFF) << 16;
					bits |= ((long)(255.0f*g) & 0xFF) <<  8;
					bits |= ((long)(255.0f*r) & 0xFF) <<  0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(255.0f*b) & 0xFF) << 24;
					bits |= ((long)(255.0f*g) & 0xFF) << 16;
					bits |= ((long)(255.0f*r) & 0xFF) <<  8;
					bits |= ((long)(255.0f*a) & 0xFF) <<  0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(1023.0f*r) & 0x03FF) << 22;
					bits |= ((long)(2047.0f*g) & 0x07FF) << 11;
					bits |= ((long)(2047.0f*b) & 0x07FF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(2047.0f*b) & 0x07FF) << 21;
					bits |= ((long)(2047.0f*g) & 0x07FF) << 10;
					bits |= ((long)(1023.0f*r) & 0x03FF) <<  0;
					break;
				}
			}
			break;
		case 6:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)(4095.0f*a) & 0x0FFF) << 36;
					bits |= ((long)(4095.0f*r) & 0x0FFF) << 24;
					bits |= ((long)(4095.0f*g) & 0x0FFF) << 12;
					bits |= ((long)(4095.0f*b) & 0x0FFF) <<  0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(4095.0f*r) & 0x0FFF) << 36;
					bits |= ((long)(4095.0f*g) & 0x0FFF) << 24;
					bits |= ((long)(4095.0f*b) & 0x0FFF) << 12;
					bits |= ((long)(4095.0f*a) & 0x0FFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)(4095.0f*a) & 0x0FFF) << 36;
					bits |= ((long)(4095.0f*b) & 0x0FFF) << 24;
					bits |= ((long)(4095.0f*g) & 0x0FFF) << 12;
					bits |= ((long)(4095.0f*r) & 0x0FFF) <<  0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(4095.0f*b) & 0x0FFF) << 36;
					bits |= ((long)(4095.0f*g) & 0x0FFF) << 24;
					bits |= ((long)(4095.0f*r) & 0x0FFF) << 12;
					bits |= ((long)(4095.0f*a) & 0x0FFF) <<  0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(65535.0f*r) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*b) & 0xFFFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(65535.0f*b) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*r) & 0xFFFF) <<  0;
					break;
				}
			}
			break;
		case 8:
			if (hasalpha) {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
					bits |= ((long)(65535.0f*a) & 0xFFFF) << 48;
					bits |= ((long)(65535.0f*r) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*b) & 0xFFFF) <<  0;
					break;
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(65535.0f*r) & 0xFFFF) << 48;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*b) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*a) & 0xFFFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
					bits |= ((long)(65535.0f*a) & 0xFFFF) << 48;
					bits |= ((long)(65535.0f*b) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*r) & 0xFFFF) <<  0;
					break;
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(65535.0f*b) & 0xFFFF) << 48;
					bits |= ((long)(65535.0f*g) & 0xFFFF) << 32;
					bits |= ((long)(65535.0f*r) & 0xFFFF) << 16;
					bits |= ((long)(65535.0f*a) & 0xFFFF) <<  0;
					break;
				}
			} else {
				switch (colorFormat) {
				case COLOR_FORMAT_ARGB:
				case COLOR_FORMAT_RGBA:
					bits |= ((long)(2097151.0f*r) & 0x1FFFFF) << 43;
					bits |= ((long)(2097151.0f*g) & 0x3FFFFF) << 21;
					bits |= ((long)(2097151.0f*b) & 0x1FFFFF) <<  0;
					break;
				case COLOR_FORMAT_ABGR:
				case COLOR_FORMAT_BGRA:
					bits |= ((long)(2097151.0f*b) & 0x1FFFFF) << 43;
					bits |= ((long)(2097151.0f*g) & 0x3FFFFF) << 21;
					bits |= ((long)(2097151.0f*r) & 0x1FFFFF) <<  0;
					break;
				}
			}
			break;
		}
		return bits;
	}
	
	private void updateFields() {
		if (!reading) {
			reading = true;
			// int ss = he.getSelectionStart();
			// int se = he.getSelectionEnd();
			int sl = he.getSelectionLength();
			long sv = he.getSelectedValue();
			
			signedLbl.setVisible(sl > 0 && sl <= 8);
			signedFld.setVisible(sl > 0 && sl <= 8);
			if (signedFld.isVisible() && writing != signedFld.getDocument()) {
				signedFld.setText(Long.toString(signed(sv,sl), numberBase).toUpperCase());
			}
			
			unsignedLbl.setVisible(sl > 0 && sl < 8);
			unsignedFld.setVisible(sl > 0 && sl < 8);
			if (unsignedFld.isVisible() && writing != unsignedFld.getDocument()) {
				unsignedFld.setText(Long.toString(unsigned(sv,sl), numberBase).toUpperCase());
			}
			
			fixedLbl.setVisible(sl > 0 && sl <= 8);
			fixedFld.setVisible(sl > 0 && sl <= 8);
			if (fixedFld.isVisible() && writing != fixedFld.getDocument()) {
				fixedFld.setText(Double.toString(signed(sv,sl)/Math.pow(16.0, sl)));
			}
			
			floatLbl.setVisible((sl > 0 && sl <= 4) || sl == 8);
			floatFld.setVisible((sl > 0 && sl <= 4) || sl == 8);
			if (floatFld.isVisible() && writing != floatFld.getDocument()) {
				switch (sl) {
				case 1:
					floatFld.setText(Float.toString(CustomFloats.bitsToCustomFloat(sv, 4, 3)));
					break;
				case 2:
					floatFld.setText(Float.toString(CustomFloats.bitsToCustomFloat(sv, 5, 10)));
					break;
				case 3:
					floatFld.setText(Float.toString(CustomFloats.bitsToCustomFloat(sv, 7, 16)));
					break;
				case 4:
					floatFld.setText(Float.toString(Float.intBitsToFloat((int)sv)));
					break;
				case 8:
					floatFld.setText(Double.toString(Double.longBitsToDouble(sv)));
					break;
				}
			}
			
			rgbLbl.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			rgbFld.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			if (rgbFld.isVisible() && writing != rgbFld) {
				rgbFld.setColor(decodeColor(sv,sl,false,false));
			}
			
			rgbaLbl.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			rgbaFld.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			if (rgbaFld.isVisible() && writing != rgbaFld) {
				rgbaFld.setColor(decodeColor(sv,sl,true,false));
			}
			
			rgbaaLbl.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			rgbaaFld.setVisible((sl > 0 && sl <= 4) || sl == 6 || sl == 8);
			if (rgbaaFld.isVisible() && writing != rgbaaFld) {
				rgbaaFld.setColor(decodeColor(sv,sl,true,true));
			}
			
			dateLbl.setVisible(sl >= 4 && sl <= 8);
			dateFld.setVisible(sl >= 4 && sl <= 8);
			if (dateFld.isVisible() && writing != dateFld) {
				Calendar ep = new GregorianCalendar(epoch,0,1);
				dateModel.setValue(new Date(ep.getTimeInMillis()+signed(sv,sl)*timescale));
			}
			
			reading = false;
		}
	}

	public void hexDataChanged(HexDataChangeEvent e) {
		updateFields();
	}

	public void hexModeChanged(HexModeChangeEvent e) {
		updateFields();
	}

	public void hexSelectionChanged(HexSelectionChangeEvent e) {
		updateFields();
	}
	
	private class JHexEditorField extends JTextField
	implements HexDisplayChangeListener {
		private static final long serialVersionUID = 1;
		
		public void updateDisplay() {
			this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			this.setHorizontalAlignment(JLabel.LEFT);
			this.setFont(he.getFont());
			this.setBackground(he.getColorScheme()[3]);
			this.setForeground(he.getColorScheme()[5]);
			this.setSelectionColor(he.getColorScheme()[14]);
			this.setSelectedTextColor(he.getColorScheme()[18]);
		}
		
		public JHexEditorField() {
			super();
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}
		
		public JHexEditorField(String l) {
			super(l);
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}
		
		public JHexEditorField(int c) {
			super(c);
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}
		
		public JHexEditorField(String l, int c) {
			super(l,c);
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}

		public void hexDisplayChanged(HexDisplayChangeEvent e) {
			updateDisplay();
		}
	}
	
	private class JHexEditorLabel extends JLabel
	implements HexDisplayChangeListener {
		private static final long serialVersionUID = 1;
		
		public void updateDisplay() {
			this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			this.setHorizontalAlignment(JLabel.LEFT);
			this.setHorizontalTextPosition(JLabel.LEFT);
			this.setFont(he.getFont());
			this.setBackground(he.getColorScheme()[26]);
			this.setForeground(he.getColorScheme()[27]);
		}
		
		public JHexEditorLabel(String l) {
			super(l);
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}

		public void hexDisplayChanged(HexDisplayChangeEvent e) {
			updateDisplay();
		}
	}
	
	private class JHexEditorScrollPane extends JScrollPane
	implements HexDisplayChangeListener {
		private static final long serialVersionUID = 1;
		
		public void updateDisplay() {
			this.setFont(he.getFont());
			this.setOpaque(true);
			this.setBackground(he.getColorScheme()[26]);
			this.setForeground(he.getColorScheme()[27]);
			this.setBorder(BorderFactory.createMatteBorder(1,0,0,0,he.getColorScheme()[28]));
		}
		
		public JHexEditorScrollPane(Component c, int pol1, int pol2) {
			super(c,pol1,pol2);
		}

		public void hexDisplayChanged(HexDisplayChangeEvent e) {
			updateDisplay();
		}
	}
	
	private class JHexEditorPanel extends JPanel
	implements HexDisplayChangeListener {
		private static final long serialVersionUID = 1;
		
		public void updateDisplay() {
			this.setFont(he.getFont());
			this.setOpaque(true);
			this.setBackground(he.getColorScheme()[26]);
			this.setForeground(he.getColorScheme()[27]);
		}
		
		/*
		public JHexEditorPanel() {
			super();
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}
		*/
		
		public JHexEditorPanel(LayoutManager lm) {
			super(lm);
			updateDisplay();
			he.addHexDisplayChangeListener(this);
		}

		public void hexDisplayChanged(HexDisplayChangeEvent e) {
			updateDisplay();
		}
	}
	
	private class JHexEditorFooter extends JLabel
	implements HexDisplayChangeListener, HexModeChangeListener, MouseListener, MouseMotionListener {
		private static final long serialVersionUID = 1;
		
		private String baseName(int i) {
			switch (i) {
			case 2:
				return "Bin";
			case 3:
				return "Ter";
			case 5:
				return "Qui";
			case 8:
				return "Oct";
			case 10:
				return "Dec";
			case 12:
				return "Duo";
			case 16:
				return "Hex";
			case 20:
				return "Vig";
			case 36:
				return "0AZ";
			default:
				String s = "  "+Integer.toString(i);
				return "B"+s.substring(s.length()-2);
			}
		}
		
		private String clrFmtName(int i) {
			switch (i) {
			case COLOR_FORMAT_ARGB:
				return "ARGB";
			case COLOR_FORMAT_RGBA:
				return "RGBA";
			case COLOR_FORMAT_ABGR:
				return "ABGR";
			case COLOR_FORMAT_BGRA:
				return "BGRA";
			default:
				return "";
			}
		}
		
		private String timeFmtName(int i) {
			switch (i) {
			case 1000:
				return "s";
			case 100:
				return "ds";
			case 10:
				return "cs";
			case 1:
				return "ms";
			default:
				return i+"ms";
			}
		}
		
		public void updateText() {
			this.setText(( (spanel == null)?"--":spanel.isVisible()?"\\/":"/\\" )+"\t"+(he.isLittleEndian()?"Lil":"Big")+"\t"+(he.getOvertypeMode()?"Ovr":"Ins")+"\t"+baseName(numberBase)+"\t"+clrFmtName(colorFormat)+"\t"+epoch+"\t"+timeFmtName(timescale));
		}
		
		public void updateDisplay() {
			this.setFont(he.getFont());
			this.setOpaque(true);
			this.setBackground(he.getColorScheme()[26]);
			this.setForeground(he.getColorScheme()[27]);
		}
		
		public JHexEditorFooter() {
			updateDisplay();
			updateText();
			he.addHexDisplayChangeListener(this);
			he.addHexModeChangeListener(this);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public void hexDisplayChanged(HexDisplayChangeEvent e) {
			updateDisplay();
		}

		public void hexModeChanged(HexModeChangeEvent e) {
			updateText();
		}
		
		protected void paintComponent(Graphics g) {
			Insets i = getInsets();
			if (isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			g.setFont(getFont());
			g.setColor(getForeground());
			String[] ss = getText().split("\t");
			int ty = i.top+(getHeight()-i.top-i.bottom-g.getFontMetrics().getHeight()+1)/2+g.getFontMetrics().getAscent();
			for (int tx=i.left+6, j=0; j<ss.length; tx+=48, j++) {
				g.drawString(ss[j], tx, ty);
			}
			g.setColor(he.getColorScheme()[28]);
			g.drawLine(0, i.top, getWidth(), i.top);
		}
		
		public Dimension getMinimumSize() {
			Dimension d = super.getMinimumSize();
			d.height += 6;
			return d;
		}
		
		public Dimension getPreferredSize() {
			Dimension d = super.getMinimumSize();
			d.height += 6;
			return d;
		}

		public void mouseClicked(MouseEvent e) {
			if (!e.isPopupTrigger()) {
				Insets i = getInsets();
				int x = (e.getX()-i.left)/48;
				switch (x) {
				case 0:
					if (spanel != null) {
						spanel.setVisible(!spanel.isVisible());
						updateText();
					}
					break;
				case 1:
					he.setLittleEndian(!he.isLittleEndian());
					break;
				case 2:
					he.setOvertypeMode(!he.getOvertypeMode());
					break;
				case 3:
					if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK)>0) {
						numberBase++;
						if (numberBase > Character.MAX_RADIX)
							numberBase = Character.MIN_RADIX;
					} else if (numberBase < 8) {
						numberBase = 8;
					} else if (numberBase < 10) {
						numberBase = 10;
					} else if (numberBase < 16) {
						numberBase = 16;
					} else {
						numberBase = 2;
					}
					updateText();
					updateFields();
					break;
				case 4:
					colorFormat++;
					if (colorFormat > COLOR_FORMAT_MAX)
						colorFormat = COLOR_FORMAT_MIN;
					updateText();
					updateFields();
					break;
				case 5:
					switch (epoch) {
					case 1970:
						epoch = 1904;
						break;
					case 1904:
						epoch = 1601;
						break;
					case 1601:
						epoch = 1980;
						break;
					case 1980:
						epoch = 1900;
						break;
					case 1900:
						epoch = 1978;
						break;
					case 1978:
						epoch = 1;
						break;
					case 1:
					default:
						epoch = 1970;
						break;
					}
					updateText();
					updateFields();
					break;
				case 6:
					timescale /= 10;
					if (timescale == 0) timescale = 1000;
					updateText();
					updateFields();
					break;
				}
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			Insets i = getInsets();
			int x = (e.getX()-i.left)/48;
			switch (x) {
			case 0:
				setToolTipText("Show/Hide Inspector");
				break;
			case 1:
				setToolTipText("Endianness");
				break;
			case 2:
				setToolTipText("Insert/Overtype Mode");
				break;
			case 3:
				setToolTipText("Integer Radix (Number Base)");
				break;
			case 4:
				setToolTipText("RGB Component Order");
				break;
			case 5:
				setToolTipText("Date Epoch Year");
				break;
			case 6:
				setToolTipText("Date Tick Duration");
				break;
			default:
				setToolTipText(null);
				break;
			}
		}
		
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
	}
}
