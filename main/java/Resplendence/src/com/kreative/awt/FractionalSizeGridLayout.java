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

/**
 * The <code>FractionalSizeGridLayout</code> class is a
 * layout manager that lays out a container's components
 * in a rectangular grid. The container is divided into
 * equal-sized rectangles, and one component is placed
 * in each rectangle.
 * <p>
 * The difference between <code>FractionalSizeGridLayout</code>
 * and <code>GridLayout</code> is that <code>FractionalSizeGridLayout</code>
 * will not always make every component <i>exactly</i>
 * the same size; some components may be smaller or bigger
 * by one pixel in width or height to make all the components
 * spread out over the entire container. <code>GridLayout</code>
 * will make every component <i>exactly</i> the same size,
 * leaving leftover space at the right or bottom of the layout,
 * which <code>FractionalSizeGridLayout</code> will never leave.
 * In other words, <code>FractionalSizeGridLayout</code>
 * divides the container using floating point division, while
 * <code>GridLayout</code> divides the container using
 * integer division.
 * @see java.awt.GridLayout
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class FractionalSizeGridLayout extends GridLayout {
	private static final long serialVersionUID = 1;
	
	/**
	 * Creates a grid layout with a default of one column
	 * per component, in a single row.
	 */
	public FractionalSizeGridLayout() {
		super();
	}
	
	/**
	 * Creates a grid layout with the specified number of
	 * rows and columns. All components in the layout are
	 * given equal size.
	 * <p>
	 * One, but not both, of rows and cols can be zero,
	 * which means that any number of objects can be placed
	 * in a row or in a column. 
	 * @param rows the rows, with the value zero meaning any number of rows.
	 * @param cols the columns, with the value zero meaning any number of columns.
	 * @throws IllegalArgumentException if the value of both rows and cols is set to zero
	 */
	public FractionalSizeGridLayout(int rows, int cols) {
		super(rows, cols);
	}
	
	/**
	 * Creates a grid layout with the specified number of
	 * rows and columns. All components in the layout are
	 * given equal size.
	 * <p>
	 * In addition, the horizontal and vertical gaps are
	 * set to the specified values. Horizontal gaps are
	 * placed at the left and right edges, and between
	 * each of the columns. Vertical gaps are placed at
	 * the top and bottom edges, and between each of the
	 * rows.
	 * <p>
	 * One, but not both, of rows and cols can be zero,
	 * which means that any number of objects can be placed
	 * in a row or in a column.
	 * @param rows the rows, with the value zero meaning any number of rows
	 * @param cols the columns, with the value zero meaning any number of columns
	 * @param hgap the horizontal gap
	 * @param vgap the vertical gap
	 * @throws IllegalArgumentException if the value of both rows and cols is set to zero
	 */
	public FractionalSizeGridLayout(int rows, int cols, int hgap, int vgap) {
		super(rows, cols, hgap, vgap);
	}
	
	/**
	 * Lays out the specified container using this layout.
	 * <p>
	 * This method reshapes the components in the specified
	 * target container in order to satisfy the constraints
	 * of the <code>FractionalSizeGridLayout</code> object.
	 * <p>
	 * The grid layout manager determines the size of
	 * individual components by dividing the free space
	 * in the container into equal-sized portions according
	 * to the number of rows and columns in the layout.
	 * The container's free space equals the container's
	 * size minus any insets and any specified horizontal
	 * or vertical gap. All components in a grid layout
	 * are given the same size.
	 */
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = getRows();
			int ncols = getColumns();
			if (ncomponents == 0) return;
			if (nrows > 0) ncols = (ncomponents + nrows - 1) / nrows;
			else nrows = (ncomponents + ncols - 1) / ncols;
			int hgap = getHgap();
			int vgap = getVgap();
			double w = (double)(parent.getWidth()-insets.left-insets.right+hgap)/(double)ncols;
			double h = (double)(parent.getHeight()-insets.top-insets.bottom+vgap)/(double)nrows;
			for (int c = 0; c < ncols; c ++) {
				int x1 = insets.left+(int)Math.floor(w*c);
				int x2 = insets.left+(int)Math.floor(w*(c+1));
				for (int r = 0; r < nrows; r ++) {
					int y1 = insets.top+(int)Math.floor(h*r);
					int y2 = insets.top+(int)Math.floor(h*(r+1));
					int i = r * ncols + c;
					if (i < ncomponents) {
						parent.getComponent(i).setBounds(x1, y1, x2-x1-hgap, y2-y1-vgap);
					}
				}
			}
		}
	}
}
