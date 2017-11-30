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
import javax.swing.*;
import com.kreative.swing.event.*;

public class JHexEditorHeader extends JLabel
implements HexAccumulatorListener, HexDataChangeListener, HexDisplayChangeListener, HexModeChangeListener, HexSelectionChangeListener, MouseListener {
	private static final long serialVersionUID = 1;
	
	private JHexEditor he;
	
	private String h(int i) {
		if (he.getDecimalAddresses()) {
			String s = "         "+Integer.toString(i);
			return s.substring(s.length()-9);
		} else {
			String s = "00000000"+Integer.toHexString(i).toUpperCase();
			return "$"+s.substring(s.length()-8);
		}
	}
	
	private String h(long l) {
		if (he.getDecimalAddresses()) {
			String s = "                 "+Long.toString(l);
			return s.substring(s.length()-17);
		} else {
			String s = "0000000000000000"+Long.toHexString(l).toUpperCase();
			return "$"+s.substring(s.length()-16);
		}
	}
	
	private void updateText() {
		int sels = he.getSelectionStart();
		int sele = he.getSelectionEnd();
		int ss = Math.min(sels, sele);
		int se = Math.max(sels, sele);
		this.setText("Len: "+h(he.getData().length)+"|Sel: "+h(ss)+":"+h(se)+" / "+h(se-ss)+"|Acc: "+h(he.getAccumulator())+"|"+(he.isLittleEndian()?"L":"B")+(he.getOvertypeMode()?"O":"I"));
	}
	
	private void updateDisplay() {
		this.setFont(he.getFont());
		this.setOpaque(true);
		this.setBackground(he.getColorScheme()[26]);
		this.setForeground(he.getColorScheme()[27]);
	}
	
	public JHexEditorHeader(JHexEditor he) {
		this.he = he;
		updateDisplay();
		updateText();
		he.addHexAccumulatorListener(this);
		he.addHexDataChangeListener(this);
		he.addHexDisplayChangeListener(this);
		he.addHexModeChangeListener(this);
		he.addHexSelectionChangeListener(this);
		this.addMouseListener(this);
	}

	public void hexAccumulatorChanged(HexAccumulatorEvent e) {
		updateText();
	}

	public void hexDataChanged(HexDataChangeEvent e) {
		updateText();
	}

	public void hexDisplayChanged(HexDisplayChangeEvent e) {
		updateDisplay();
	}

	public void hexModeChanged(HexModeChangeEvent e) {
		updateText();
	}

	public void hexSelectionChanged(HexSelectionChangeEvent e) {
		updateText();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets i = getInsets();
		g.setColor(he.getColorScheme()[28]);
		g.drawLine(0, getHeight()-i.bottom-1, getWidth(), getHeight()-i.bottom-1);
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
	
	private java.util.Timer t;

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 0) {
			final int cc = e.getClickCount();
			java.util.TimerTask tt = new java.util.TimerTask(){
				public void run() {
					JHexEditorHeader.this.removeMouseListener(JHexEditorHeader.this);
					t = null;
					he.setWidth(cc*8);
					JHexEditorHeader.this.addMouseListener(JHexEditorHeader.this);
				}
			};
			if (t != null) t.cancel();
			t = new java.util.Timer();
			t.schedule(tt,200);
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
