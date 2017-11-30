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

package com.kreative.awt;

import java.awt.*;
import java.awt.event.*;

public class GrabHandleHookup implements MouseListener, MouseMotionListener {
	public static final int CENTER = 0;
	public static final int NORTHWEST = 1;
	public static final int NORTH = 2;
	public static final int NORTHEAST = 3;
	public static final int WEST = 4;
	public static final int EAST = 5;
	public static final int SOUTHWEST = 6;
	public static final int SOUTH = 7;
	public static final int SOUTHEAST = 8;
	
	private Component c;
	private int type;
	private int ox, oy, ow, oh, dx, dy, dw, dh;
	
	public GrabHandleHookup(Component what, Component grabHandle, int whichCorner) {
		this.c = what;
		this.type = whichCorner;
		grabHandle.addMouseListener(this);
		grabHandle.addMouseMotionListener(this);
	}
	
	public void mousePressed(MouseEvent e) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		ox = c.getX();
		oy = c.getY();
		ow = c.getWidth();
		oh = c.getHeight();
		dx = p.x - ox;
		dy = p.y - oy;
		dw = p.x - ow;
		dh = p.y - oh;
	}
	
	public void mouseDragged(MouseEvent e) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		if (type == CENTER) {
			c.setLocation(p.x-dx, p.y-dy);
		} else if (type == SOUTHEAST) {
			c.setSize(p.x-dw, p.y-dh);
		} else if (type == EAST) {
			c.setSize(p.x-dw, oh);
		} else if (type == SOUTH) {
			c.setSize(ow, p.y-dh);
		} else if (type == NORTHWEST) {
			c.setBounds(p.x-dx, p.y-dy, ow-p.x+dx+ox, oh-p.y+dy+oy);
		} else if (type == WEST) {
			c.setBounds(p.x-dx, oy, ow-p.x+dx+ox, oh);
		} else if (type == NORTH) {
			c.setBounds(ox, p.y-dy, ow, oh-p.y+dy+oy);
		} else if (type == NORTHEAST) {
			c.setBounds(ox, p.y-dy, p.x-dw, oh-p.y+dy+oy);
		} else if (type == SOUTHWEST) {
			c.setBounds(p.x-dx, oy, ow-p.x+dx+ox, p.y-dh);
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}
