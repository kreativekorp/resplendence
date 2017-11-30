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
import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.kreative.swing.event.*;

public class JHexEditorSuite extends JPanel implements JHexEditorColorSchemes {
	private static final long serialVersionUID = 1;
	
	private JHexEditor he;
	private JHexEditorHeader hh;
	private JHexEditorInspector hi;
	private JScrollPane hs;
	
	public JHexEditorSuite() {
		super(new BorderLayout());
		he = new JHexEditor();
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(boolean littleEndian) {
		super(new BorderLayout());
		he = new JHexEditor(littleEndian);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(String textEncoding) {
		super(new BorderLayout());
		he = new JHexEditor(textEncoding);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(String textEncoding, boolean littleEndian) {
		super(new BorderLayout());
		he = new JHexEditor(textEncoding, littleEndian);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(byte[] data) {
		super(new BorderLayout());
		he = new JHexEditor(data);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(byte[] data, boolean littleEndian) {
		super(new BorderLayout());
		he = new JHexEditor(data, littleEndian);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(byte[] data, String textEncoding) {
		super(new BorderLayout());
		he = new JHexEditor(data, textEncoding);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditorSuite(byte[] data, String textEncoding, boolean littleEndian) {
		super(new BorderLayout());
		he = new JHexEditor(data, textEncoding, littleEndian);
		he.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		he.useColorSchemeBackground(true);
		hh = new JHexEditorHeader(he);
		hh.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
		hi = new JHexEditorInspector(he);
		hs = new JScrollPane(he, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(hs, BorderLayout.CENTER);
		add(hh, BorderLayout.PAGE_START);
		add(hi, BorderLayout.PAGE_END);
	}
	
	public JHexEditor getHexEditor() {
		return he;
	}
	
	public JScrollPane getHexEditorScrollPane() {
		return hs;
	}
	
	public JHexEditorHeader getHexEditorHeader() {
		return hh;
	}
	
	public JHexEditorInspector getHexEditorInspector() {
		return hi;
	}
	
	// inherit several methods from JHexEditor for convenience
	
	public byte[] getData() {
		return he.getData();
	}
	
	public void setData(byte[] data) {
		he.setData(data);
	}
	
	public boolean isLittleEndian() {
		return he.isLittleEndian();
	}
	
	public boolean isBigEndian() {
		return he.isBigEndian();
	}
	
	public void setLittleEndian(boolean littleEndian) {
		he.setLittleEndian(littleEndian);
	}
	
	public void setBigEndian(boolean bigEndian) {
		he.setBigEndian(bigEndian);
	}
	
	public boolean getOvertypeMode() {
		return he.getOvertypeMode();
	}
	
	public void setOvertypeMode(boolean overtype) {
		he.setOvertypeMode(overtype);
	}
	
	public boolean getDecimalAddresses() {
		return he.getDecimalAddresses();
	}
	
	public void setDecimalAddresses(boolean decimalAddresses) {
		he.setDecimalAddresses(decimalAddresses);
	}
	
	public String getTextEncoding() {
		return he.getTextEncoding();
	}
	
	public void setTextEncoding(String textEncoding) {
		he.setTextEncoding(textEncoding);
	}
	
	public Color[] getColorScheme() {
		return he.getColorScheme();
	}
	
	public void setColorScheme(Color[] cs) {
		he.setColorScheme(cs);
	}
	
	public boolean ignoreControlKeys() {
		return he.ignoreControlKeys();
	}
	
	public void setIgnoreControlKeys(boolean b) {
		he.setIgnoreControlKeys(b);
	}
	
	public long getAccumulator() {
		return he.getAccumulator();
	}
	
	public void setAccumulator(long a) {
		he.setAccumulator(a);
	}
	
	public boolean isOnDataSide() {
		return he.isOnDataSide();
	}
	
	public boolean isOnHexSide() {
		return he.isOnHexSide();
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (he != null) he.setFont(font);
	}
	
	public void setWidth(int numBytes) {
		he.setWidth(numBytes);
	}
	
	public void cut() {
		he.cut();
	}
	
	public void copy() {
		he.copy();
	}
	
	public void paste() {
		he.paste();
	}
	
	public int getSelectionStart() {
		return he.getSelectionStart();
	}
	
	public int getSelectionEnd() {
		return he.getSelectionEnd();
	}
	
	public int getSelectionLength() {
		return he.getSelectionLength();
	}
	
	public void setSelectionStart(int start) {
		he.setSelectionStart(start);
	}
	
	public void setSelectionEnd(int end) {
		he.setSelectionEnd(end);
	}
	
	public void select(int start, int end) {
		he.select(start, end);
	}
	
	public void selectAll() {
		he.selectAll();
	}
	
	public byte[] getSelectedData() {
		return he.getSelectedData();
	}
	
	public String getSelectedHex() {
		return he.getSelectedHex();
	}
	
	public long getSelectedValue() {
		return he.getSelectedValue();
	}
	
	public void replaceSelection(byte[] data) {
		he.replaceSelection(data);
	}
	
	public void replaceSelection(String hex) {
		he.replaceSelection(hex);
	}
	
	public void replaceSelection(long l) {
		he.replaceSelection(l);
	}
	
	public boolean isEditable() {
		return he.isEditable();
	}
	
	public void setEditable(boolean b) {
		he.setEditable(b);
	}
	
	public void addHexDataChangeListener(HexDataChangeListener l) {
		he.addHexDataChangeListener(l);
	}
	
	public void removeHexDataChangeListener(HexDataChangeListener l) {
		he.removeHexDataChangeListener(l);
	}
	
	public HexDataChangeListener[] getHexDataChangeListeners() {
		return he.getHexDataChangeListeners();
	}
	
	public void addHexSelectionChangeListener(HexSelectionChangeListener l) {
		he.addHexSelectionChangeListener(l);
	}
	
	public void removeHexSelectionChangeListener(HexSelectionChangeListener l) {
		he.removeHexSelectionChangeListener(l);
	}
	
	public HexSelectionChangeListener[] getHexSelectionChangeListeners() {
		return he.getHexSelectionChangeListeners();
	}
	
	public void addHexAccumulatorListener(HexAccumulatorListener l) {
		he.addHexAccumulatorListener(l);
	}
	
	public void removeHexAccumulatorListener(HexAccumulatorListener l) {
		he.removeHexAccumulatorListener(l);
	}
	
	public HexAccumulatorListener[] getHexAccumulatorListeners() {
		return he.getHexAccumulatorListeners();
	}
	
	public void addHexDisplayChangeListener(HexDisplayChangeListener l) {
		he.addHexDisplayChangeListener(l);
	}
	
	public void removeHexDisplayChangeListener(HexDisplayChangeListener l) {
		he.removeHexDisplayChangeListener(l);
	}
	
	public HexDisplayChangeListener[] getHexDisplayChangeListeners() {
		return he.getHexDisplayChangeListeners();
	}
	
	public void addHexModeChangeListener(HexModeChangeListener l) {
		he.addHexModeChangeListener(l);
	}
	
	public void removeHexModeChangeListener(HexModeChangeListener l) {
		he.removeHexModeChangeListener(l);
	}
	
	public HexModeChangeListener[] getHexModeChangeListeners() {
		return he.getHexModeChangeListeners();
	}
	
	// inherit some methods from JHexEditorInspector for convenience
	
	public boolean isInspectorExpanded() {
		return hi.isExpanded();
	}
	
	public boolean isInspectorCollapsed() {
		return hi.isCollapsed();
	}
	
	public void setInspectorExpanded(boolean expanded) {
		hi.setExpanded(expanded);
	}
	
	public void setInspectorCollapsed(boolean collapsed) {
		hi.setCollapsed(collapsed);
	}
	
	public JLabel getHexEditorFooter() {
		return hi.getHexEditorFooter();
	}
	
	public JPanel getInspectorPanel() {
		return hi.getPanel();
	}
	
	public JScrollPane getInspectorScrollPane() {
		return hi.getScrollPane();
	}
	
	public void setLabelDisplay(JLabel label) {
		hi.setLabelDisplay(label);
	}
	
	public void setTextComponentDisplay(JTextComponent tc) {
		hi.setTextComponentDisplay(tc);
	}
	
	public JLabel createCustomInspectorLabel(String text) {
		return hi.createCustomInspectorLabel(text);
	}
	
	public JTextField createCustomInspectorTextField() {
		return hi.createCustomInspectorTextField();
	}
	
	public JTextField createCustomInspectorTextField(int width) {
		return hi.createCustomInspectorTextField(width);
	}
	
	public JTextField createCustomInspectorTextField(String text) {
		return hi.createCustomInspectorTextField(text);
	}
	
	public JTextField createCustomInspectorTextField(String text, int width) {
		return hi.createCustomInspectorTextField(text, width);
	}
	
	public void addCustomInspectorField(JLabel name, Component field) {
		hi.addCustomInspectorField(name, field);
	}
}
