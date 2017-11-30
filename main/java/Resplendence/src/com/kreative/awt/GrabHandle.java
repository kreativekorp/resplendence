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
import java.awt.image.*;

public class GrabHandle extends Component {
	private static final long serialVersionUID = 1;

	public static final int CENTER = 0;
	public static final int NORTHWEST = 1;
	public static final int NORTH = 2;
	public static final int NORTHEAST = 3;
	public static final int WEST = 4;
	public static final int EAST = 5;
	public static final int SOUTHWEST = 6;
	public static final int SOUTH = 7;
	public static final int SOUTHEAST = 8;
	
	private int z = 7;
	private Image ghi = null;
	private int ax=1, ay=1;
	
	private Image ghi() {
		if (ghi != null) return ghi;
		ghi = new BufferedImage(z,z,BufferedImage.TYPE_INT_RGB);
		Graphics g = ghi.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, z, z);
		g.setColor(Color.blue.darker());
		g.fillRect(1, 1, z-2, z-2);
		return ghi;
	}
	
	public GrabHandle(int type) {
		switch (type) {
		case NORTHWEST: ax=0; ay=0; break;
		case NORTH:     ax=1; ay=0; break;
		case NORTHEAST: ax=2; ay=0; break;
		case WEST:      ax=0; ay=1; break;
		case CENTER:    ax=1; ay=1; break;
		case EAST:      ax=2; ay=1; break;
		case SOUTHWEST: ax=0; ay=2; break;
		case SOUTH:     ax=1; ay=2; break;
		case SOUTHEAST: ax=2; ay=2; break;
		}
	}
	
	public GrabHandle(int type, int size) {
		z = size;
		switch (type) {
		case NORTHWEST: ax=0; ay=0; break;
		case NORTH:     ax=1; ay=0; break;
		case NORTHEAST: ax=2; ay=0; break;
		case WEST:      ax=0; ay=1; break;
		case CENTER:    ax=1; ay=1; break;
		case EAST:      ax=2; ay=1; break;
		case SOUTHWEST: ax=0; ay=2; break;
		case SOUTH:     ax=1; ay=2; break;
		case SOUTHEAST: ax=2; ay=2; break;
		}
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(z,z);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(z,z);
	}
	
	public Dimension getMaximumSize() {
		return new Dimension(z,z);
	}
	
	public void paint(Graphics g) {
		int x, y;
		x = ((this.getWidth()-z)*ax)/2;
		y = ((this.getHeight()-z)*ay)/2;
		g.drawImage(ghi(), x, y, this);
	}
	
	public void paintAll(Graphics g) {
		paint(g);
	}
	
	public void print(Graphics g) {
		paint(g);
	}
	
	public void printAll(Graphics g) {
		paint(g);
	}
}
