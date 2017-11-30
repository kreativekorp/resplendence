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
import java.util.*;

public class JIconList extends JList {
	private static final long serialVersionUID = 1;
	
	private Map<Object,Icon> icons;
	private boolean tv = true;
	private int max = 0;
	private JListAlias alias;
	
	public JIconList() {
		super();
		icons = new HashMap<Object,Icon>();
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(ListModel dataModel) {
		super(dataModel);
		icons = new HashMap<Object,Icon>();
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(Object[] listData) {
		super(listData);
		icons = new HashMap<Object,Icon>();
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}

	public JIconList(Vector<?> listData) {
		super(listData);
		icons = new HashMap<Object,Icon>();
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(Map<Object,Icon> listIcons) {
		super();
		icons = listIcons;
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(ListModel dataModel, Map<Object,Icon> listIcons) {
		super(dataModel);
		icons = listIcons;
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(Object[] listData, Map<Object,Icon> listIcons) {
		super(listData);
		icons = listIcons;
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}

	public JIconList(Vector<?> listData, Map<Object,Icon> listIcons) {
		super(listData);
		icons = listIcons;
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public JIconList(Object[] listData, Icon[] listIcons) {
		super(listData);
		icons = new HashMap<Object,Icon>();
		for (int i=0; i<listData.length && i<listIcons.length; i++) {
			icons.put(listData[i], listIcons[i]);
		}
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}

	public JIconList(Vector<?> listData, Vector<Icon> listIcons) {
		super(listData);
		icons = new HashMap<Object,Icon>();
		Iterator<?> i = listData.iterator();
		Iterator<Icon> j = listIcons.iterator();
		while (i.hasNext() && j.hasNext()) {
			icons.put(i.next(), j.next());
		}
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setCellRenderer(new MyCellRenderer());
	}
	
	public Map<Object,Icon> getListIcons() {
		return icons;
	}
	
	public void setListIcons(Map<Object,Icon> listIcons) {
		icons = listIcons;
	}
	
	public void setListData(Object[] listData, Icon[] listIcons) {
		setListData(listData);
		icons = new HashMap<Object,Icon>();
		for (int i=0; i<listData.length && i<listIcons.length; i++) {
			icons.put(listData[i], listIcons[i]);
		}
	}
	
	public void setListData(Object[] listData, Map<Object,Icon> listIcons) {
		setListData(listData);
		icons = listIcons;
	}
	
	public void setTextVisible(boolean vis) {
		tv = vis;
		setVisibleRowCount(getVisibleRowCount()+1);
		setVisibleRowCount(getVisibleRowCount()-1);
	}
	
	public boolean getTextVisible() {
		return tv;
	}
	
	public void setMaxWidth(int w) {
		max = w;
		setVisibleRowCount(getVisibleRowCount()+1);
		setVisibleRowCount(getVisibleRowCount()-1);
	}
	
	public int getMaxWidth() {
		return max;
	}
	
	public void setListAlias(JListAlias a) {
		alias = a;
		setVisibleRowCount(getVisibleRowCount()+1);
		setVisibleRowCount(getVisibleRowCount()-1);
	}
	
	public JListAlias getListAlias() {
		return alias;
	}
	
	private class MyCellRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1;
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean sel, boolean focus) {
			if (tv) {
				Object o;
				if (alias != null) o = alias.getListAlias(list, value, index);
				else o = value;
				if (o == null) setText(null);
				else setText(o.toString());
			} else {
				setText(null);
			}
			if (icons.containsKey(value)) setIcon(icons.get(value));
			if (sel) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont().deriveFont(9.0f));
			setHorizontalAlignment(JLabel.CENTER);
			setVerticalAlignment(JLabel.BOTTOM);
			setAlignmentX(JLabel.CENTER_ALIGNMENT);
			setAlignmentY(JLabel.BOTTOM_ALIGNMENT);
			setVerticalTextPosition(JLabel.BOTTOM);
			setHorizontalTextPosition(JLabel.CENTER);
			setIconTextGap(8);
			setOpaque(true);
			setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
			if (max != 0) {
				setMaximumSize(new Dimension(max+24, getMaximumSize().height));
				setPreferredSize(new Dimension(max+24, getPreferredSize().height));
			}
			return this;
		}
	}
}
