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

package com.kreative.util;

import java.awt.*;

public class ColorUtil {
	private ColorUtil() {
		// this class has all static methods, so no constructor for you
	}
	
	public static int blend(int c1, int c2, double frac) {
		int a1 = (c1 & 0xFF000000) >> 24;
		int r1 = (c1 & 0x00FF0000) >> 16;
		int g1 = (c1 & 0x0000FF00) >> 8;
		int b1 = (c1 & 0x000000FF);
		int a2 = (c2 & 0xFF000000) >> 24;
		int r2 = (c2 & 0x00FF0000) >> 16;
		int g2 = (c2 & 0x0000FF00) >> 8;
		int b2 = (c2 & 0x000000FF);
		int a = a1 + (int)((a2-a1)*frac);
		int r = r1 + (int)((r2-r1)*frac);
		int g = g1 + (int)((g2-g1)*frac);
		int b = b1 + (int)((b2-b1)*frac);
		return (a << 24) | (r << 16) | (g << 8) | (b);
	}
	
	public static Color blend(Color c1, Color c2, double frac) {
		int a = c1.getAlpha() + (int)((c2.getAlpha()-c1.getAlpha())*frac);
		int r = c1.getRed() + (int)((c2.getRed()-c1.getRed())*frac);
		int g = c1.getGreen() + (int)((c2.getGreen()-c1.getGreen())*frac);
		int b = c1.getBlue() + (int)((c2.getBlue()-c1.getBlue())*frac);
		return new Color(r,g,b,a);
	}
	
	public static int gradientPoint(int[] grad, double x) {
		double m = (double)(grad.length-1)*x;
		double i = Math.floor(m);
		double d = m-i;
		if (m<=0) return grad[0];
		else if (m>=grad.length-1) return grad[grad.length-1];
		else if (d<=0) return grad[(int)i];
		else return blend(grad[(int)i], grad[(int)i+1], d);
	}
	
	public static Color gradientPoint(Color[] grad, double x) {
		double m = (double)(grad.length-1)*x;
		double i = Math.floor(m);
		double d = m-i;
		if (m<=0) return grad[0];
		else if (m>=grad.length-1) return grad[grad.length-1];
		else if (d<=0) return grad[(int)i];
		else return blend(grad[(int)i], grad[(int)i+1], d);
	}
	
	public static int gradientPoint(int[] grad, int left, int width, int x) {
		double m = (double)(grad.length-1)*(double)(x-left)/(double)(width-1);
		double i = Math.floor(m);
		double d = m-i;
		if (m<=0) return grad[0];
		else if (m>=grad.length-1) return grad[grad.length-1];
		else if (d<=0) return grad[(int)i];
		else return blend(grad[(int)i], grad[(int)i+1], d);
	}
	
	public static Color gradientPoint(Color[] grad, int left, int width, int x) {
		double m = (double)(grad.length-1)*(double)(x-left)/(double)(width-1);
		double i = Math.floor(m);
		double d = m-i;
		if (m<=0) return grad[0];
		else if (m>=grad.length-1) return grad[grad.length-1];
		else if (d<=0) return grad[(int)i];
		else return blend(grad[(int)i], grad[(int)i+1], d);
	}
}
