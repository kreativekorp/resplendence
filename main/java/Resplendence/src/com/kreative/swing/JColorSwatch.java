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
import javax.swing.event.*;
import com.kreative.swing.event.*;

public class JColorSwatch extends JComponent implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1;
	
	private JColorChooser myChooser;
	private Color myColor;
	private EventListenerList myListeners = new EventListenerList();
	
	public JColorSwatch() {
		super();
		addMouseListener(this);
		myChooser = new JColorChooser(Color.white);
		myColor = Color.white;
		repaint();
	}
	
	public JColorSwatch(Color c) {
		super();
		addMouseListener(this);
		myChooser = new JColorChooser(c);
		myColor = c;
		repaint();
	}
	
	public JColorSwatch(JColorChooser jcc) {
		super();
		addMouseListener(this);
		myChooser = jcc;
		myColor = jcc.getColor();
		repaint();
	}
	
	public JColorSwatch(JColorChooser jcc, Color c) {
		super();
		addMouseListener(this);
		myChooser = jcc;
		myColor = c;
		jcc.setColor(c);
		repaint();
	}
	
	public JColorChooser getChooser() {
		return myChooser;
	}
	
	public void setChooser(JColorChooser jcc) {
		myChooser = jcc;
	}
	
	private Color light(Color c) {
		return new Color(255-(255-c.getRed())*2/3, 255-(255-c.getGreen())*2/3, 255-(255-c.getBlue())*2/3, c.getAlpha());
	}
	
	private Color lighter(Color c) {
		return new Color(255-(255-c.getRed())/3, 255-(255-c.getGreen())/3, 255-(255-c.getBlue())/3, c.getAlpha());
	}
	
	private Color dark(Color c) {
		return new Color(c.getRed()*2/3, c.getGreen()*2/3, c.getBlue()*2/3, c.getAlpha());
	}
	
	private Color darker(Color c) {
		return new Color(c.getRed()/3, c.getGreen()/3, c.getBlue()/3, c.getAlpha());
	}
	
	protected void paintComponent(Graphics g) {
		Insets i = getInsets();
		Color c = g.getColor();
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(c);
		}
		if (myColor == null) return;
		int w = getWidth();
		int h = getHeight();
		g.setColor(myColor);
		g.fillRect(i.left, i.top, w-i.right-i.left, h-i.bottom-i.top);
		g.setColor(light(myColor));
		g.drawLine(i.left+1, i.top+1, w-i.right-2, i.top+1);
		g.drawLine(i.left+1, i.top+1, i.left+1, h-i.bottom-2);
		g.setColor(lighter(myColor));
		g.drawLine(i.left, i.top, w-i.right-1, i.top);
		g.drawLine(i.left, i.top, i.left, h-i.bottom-1);
		g.setColor(dark(myColor));
		g.drawLine(w-i.right-2, i.top+1, w-i.right-2, h-i.bottom-2);
		g.drawLine(i.left+1, h-i.bottom-2, w-i.right-2, h-i.bottom-2);
		g.setColor(darker(myColor));
		g.drawLine(w-i.right-1, i.top, w-i.right-1, h-i.bottom-1);
		g.drawLine(i.left, h-i.bottom-1, w-i.right-1, h-i.bottom-1);
		g.setColor(c);
	}
	
	public Color getColor() {
		return myColor;
	}
	
	public void setColor(Color c) {
		myColor = c;
		myChooser.setColor(c);
		repaint();
	}
	
	public Dimension getMinimumSize() {
		Insets i = getInsets();
		return new Dimension(8+i.left+i.right,8+i.top+i.bottom);
	}
	
	public Dimension getPreferredSize() {
		Insets i = getInsets();
		return new Dimension(16+i.left+i.right,16+i.top+i.bottom);
	}
	
	public Dimension getMaximumSize() {
		return new Dimension(32000,32000);
	}
	
	private boolean inside(Insets i, int w, int h, int x, int y) {
		return (x>=i.left && y>=i.top && x<(w-i.right) && y<(h-i.bottom));
	}
	
	public void mouseClicked(MouseEvent e) {
		Insets i = getInsets();
		if (inside(i,getWidth(),getHeight(),e.getX(),e.getY())) {
			JDialog d = JColorChooser.createDialog(this, "Select new color:", false, myChooser, this, this);
			d.pack();
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("OK")) {
			setColor(myChooser.getColor());
			
			ColorChangeEvent ee = new ColorChangeEvent(this, 1, "setColor", myColor);
			ColorChangeListener[] l = myListeners.getListeners(ColorChangeListener.class);
			for (int i=0; i<l.length; i++) l[i].colorChanged(ee);
		} else {
			myChooser.setColor(myColor);
		}
	}
	
	public void addColorChangeListener(ColorChangeListener l) {
		myListeners.add(ColorChangeListener.class, l);
	}
	
	public void removeColorChangeListener(ColorChangeListener l) {
		myListeners.remove(ColorChangeListener.class, l);
	}
	
	public ColorChangeListener[] getColorChangeListeners() {
		return myListeners.getListeners(ColorChangeListener.class);
	}
}
