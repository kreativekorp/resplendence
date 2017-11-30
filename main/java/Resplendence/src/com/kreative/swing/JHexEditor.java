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
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.swing.event.*;

public class JHexEditor extends JComponent
implements Scrollable,
KeyListener, MouseListener, MouseMotionListener,
ClipboardOwner,
JHexEditorColorSchemes {
	private static final long serialVersionUID = 1L;
	
	private static final String[] LOOKUP_HEX = new String[] {
		"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
		"10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
		"20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
		"30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
		"40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
		"50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
		"60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
		"70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
		"80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
		"90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
		"A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
		"B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
		"C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
		"D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
		"E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
		"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"
	};
	
	private static final Font mono = new Font("Monospaced", Font.PLAIN, 12);
	
	private int ca = -1; // character ascent
	private int ch = -1; // character height
	private int cw = -1; // character width
	private Cursor curs = null;
	private Dimension minsize = null;
	private Dimension prefsize = null;
	private EventListenerList myListeners = new EventListenerList();
	private JPopupMenu menu = null;
	
	private byte[] data;
	private boolean dataSide = false; // vs. hexSide
	private int selStart = 0;
	private int selEnd = 0;
	private boolean midByte = false; // first hex digit of a byte has been entered; waiting for second
	private boolean readOnly = false;
	private boolean le = false;
	private boolean overtype = false;
	private boolean decAddr = false;
	private String textEncoding = "ISO-8859-1"; // for displaying data side
	private char[] textEncodingLookup = new char[256];
	private boolean useCSBackground = false;
	private Color[] colorScheme = COLOR_SCHEME_AQUA;
	private boolean ignoreControlKeys = false;
	
	private long acc = 0L;
	private Random rand = new Random();
	
	public JHexEditor() {
		this.data = new byte[0];
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(boolean littleEndian) {
		this.data = new byte[0];
		this.le = littleEndian;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(String textEncoding) {
		this.data = new byte[0];
		this.textEncoding = textEncoding;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(String textEncoding, boolean littleEndian) {
		this.data = new byte[0];
		this.le = littleEndian;
		this.textEncoding = textEncoding;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(byte[] data) {
		this.data = data;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(byte[] data, boolean littleEndian) {
		this.data = data;
		this.le = littleEndian;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(byte[] data, String textEncoding) {
		this.data = data;
		this.textEncoding = textEncoding;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	public JHexEditor(byte[] data, String textEncoding, boolean littleEndian) {
		this.data = data;
		this.le = littleEndian;
		this.textEncoding = textEncoding;
		prepTextEncoding();
		super.setFont(mono);
		this.setAutoscrolls(true);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		makePopupMenu();
	}
	
	private void prepTextEncoding() {
		if (textEncoding.startsWith("XX")) {
			if (textEncoding.equalsIgnoreCase("XX-HighASCII")) {
				for (int i = 0; i < 0x80; i++) {
					textEncodingLookup[i] = (char)i;
					textEncodingLookup[i+0x80] = (char)i;
				}
			} else if (textEncoding.equalsIgnoreCase("XX-AppleII-Primary")) {
				for (int i = 0; i < 0x80; i++) {
					textEncodingLookup[i] = (char)(0x20+((i ^ 0x20) & 0x3F));
					textEncodingLookup[i+0x80] = (char)i;
				}
			} else if (textEncoding.equalsIgnoreCase("XX-AppleII-Alternate")) {
				for (int i = 0; i < 0x20; i++) {
					textEncodingLookup[i] = (char)(0x40+i);
				}
				char[] alt = new char[]{
						'\uF8FF', '\uF8FF', '\uF790', '\u231B', '\u2713', '\u2713', '\u21B5', '\u2263',
						'\u2190', '\u2026', '\u2193', '\u2191', '\u2594', '\u21B5', '\u2589', '\u21E6',
						'\u21E8', '\u21E9', '\u21E7', '\u2500', '\u221F', '\u2192', '\u2592', '\u2592',
						'\uF793', '\uF793', '\u2595', '\u25C6', '\u2550', '\u256C', '\u2290', '\u258F'
				};
				for (int i = 0x20; i < 0x40; i++) {
					textEncodingLookup[i] = (char)i;
				}
				for (int i = 0; i < alt.length; i++) {
					textEncodingLookup[i+0x40] = alt[i];
				}
				for (int i = 0x60; i < 0x80; i++) {
					textEncodingLookup[i] = (char)i;
				}
				for (int i = 0; i < 0x80; i++) {
					textEncodingLookup[i+0x80] = (char)i;
				}
			} else {
				for (int i = 0; i < 0x100; i++) {
					textEncodingLookup[i] = (char)i;
				}
			}
		} else try {
			for (int i = 0; i < 0x100; i++) {
				String s = new String(new byte[]{(byte)i}, textEncoding);
				textEncodingLookup[i] = (s.length() < 1) ? 0xFFFD : s.charAt(0);
			}
		} catch (UnsupportedEncodingException uee) {
			for (int i = 0; i < 0x100; i++) {
				textEncodingLookup[i] = (char)i;
			}
		}
	}
	
	private void guiChanged() {
		// this hackish piece of code makes sure everything
		// updates properly when the height of the hex editor
		// changes (either length of data changes or font changes)
		if (getParent() instanceof JViewport) {
			// this one makes scroll panes update properly
			int h = Math.max(getParent().getHeight(), calculateHeight());
			setSize(getWidth(), h);
			setPreferredSize(new Dimension(getWidth(), h));
			// this one makes the scroll pane redraw the JHexEditor
			revalidate();
		}
		// this one redraws the JHexEditor outside of any scroll panes
		repaint();
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
		selStart = selEnd = 0;
		midByte = false;
		guiChanged();
		fireHexDataChanged();
		fireHexSelectionChanged();
	}
	
	public boolean isLittleEndian() {
		return le;
	}
	
	public boolean isBigEndian() {
		return !le;
	}
	
	public void setLittleEndian(boolean littleEndian) {
		le = littleEndian;
		fireHexModeChanged(HexModeChangeEvent.MODE_ENDIANNESS);
	}
	
	public void setBigEndian(boolean bigEndian) {
		le = !bigEndian;
		fireHexModeChanged(HexModeChangeEvent.MODE_ENDIANNESS);
	}
	
	public boolean getOvertypeMode() {
		return overtype;
	}
	
	public void setOvertypeMode(boolean overtype) {
		this.overtype = overtype;
		fireHexModeChanged(HexModeChangeEvent.MODE_OVERTYPE);
	}
	
	public boolean getDecimalAddresses() {
		return decAddr;
	}
	
	public void setDecimalAddresses(boolean decimalAddresses) {
		decAddr = decimalAddresses;
		repaint();
		fireHexModeChanged(HexModeChangeEvent.MODE_DECIMAL_ADDRESSES);
	}
	
	public String getTextEncoding() {
		return textEncoding;
	}
	
	public void setTextEncoding(String textEncoding) {
		this.textEncoding = textEncoding;
		prepTextEncoding();
		repaint();
		fireHexDisplayChanged();
	}
	
	public Color[] getColorScheme() {
		return colorScheme;
	}
	
	public void setColorScheme(Color[] cs) {
		if (cs.length >= 29) {
			colorScheme = cs;
			repaint();
			fireHexDisplayChanged();
		}
	}
	
	public boolean usesColorSchemeBackground() {
		return useCSBackground;
	}
	
	public void useColorSchemeBackground(boolean useCSBg) {
		useCSBackground = useCSBg;
		repaint();
	}
	
	public boolean ignoreControlKeys() {
		return ignoreControlKeys;
	}
	
	public void setIgnoreControlKeys(boolean b) {
		ignoreControlKeys = b;
	}
	
	public long getAccumulator() {
		return acc;
	}
	
	public void setAccumulator(long a) {
		acc = a;
		fireHexAccumulatorChanged();
	}
	
	public boolean isOnDataSide() {
		return dataSide;
	}
	
	public boolean isOnHexSide() {
		return !dataSide;
	}
	
	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		curs = cursor;
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		Graphics g = getGraphics();
		if (g != null) recalcFontMetrics(g.getFontMetrics(font));
		guiChanged();
		fireHexDisplayChanged();
	}
	
	private void recalcFontMetrics(FontMetrics fm) {
		ca = fm.getAscent()+1;
		ch = fm.getHeight()+1;
		cw = fm.charWidth(' ');
		for (char d='0'; d<='9'; d++) {
			int dw = fm.charWidth(d);
			if (dw > cw) cw = dw;
		}
		for (char d='A'; d<='Z'; d++) {
			int dw = fm.charWidth(d);
			if (dw > cw) cw = dw;
		}
		for (char d='a'; d<='z'; d++) {
			int dw = fm.charWidth(d);
			if (dw > cw) cw = dw;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Color c = g.getColor();
		Insets i = getInsets();
		int x = i.left;
		int y = i.top;
		int w = getWidth()-i.left-i.right;
		int h = getHeight()-i.top-i.bottom;
		char[] d = new char[1];
		g.setFont(getFont());
		if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(g.getFontMetrics());
		if (isOpaque()) {
			g.setColor(useCSBackground?colorScheme[3]:getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		int bpr = (w - 11*cw)/(4*cw);
		if (bpr < 1) {
			g.setColor(useCSBackground?colorScheme[5]:getForeground());
			g.drawString("Not wide", x, y+ca);
			g.drawString("enough  ", x, y+ca+ch);
			g.drawString("to draw ", x, y+ca+ch+ch);
			g.drawString("control.", x, y+ca+ch+ch+ch);
		} else {
			if (bpr>4) bpr = 4*(bpr/4);
			int ss = Math.min(selStart, selEnd);
			int se = Math.max(selStart, selEnd);
			if (useCSBackground) {
				g.setColor(colorScheme[3]);
				g.fillRect(0, 0, getWidth(), getHeight());
			} else {
				g.setColor(colorScheme[3]);
				g.fillRect(x, y, w, h);
			}
			// address field
			if (useCSBackground) {
				g.setColor(colorScheme[0]);
				g.fillRect(0, 0, cw*9+i.left, h+i.top+i.bottom);
				g.setColor(colorScheme[2]);
				g.drawLine(x+cw*9, y, x+cw*9, y+h-1);
			} else {
				g.setColor(colorScheme[0]);
				g.fillRect(x, y, cw*9, h);
				g.setColor(colorScheme[2]);
				g.drawLine(x+cw*9, y, x+cw*9, y+h-1);
			}
			// rows
			int miny = -ch;
			int maxy = h+i.top+i.bottom+ch;
			if (getParent() instanceof JViewport) {
				// this trick is the key to making this not crazy-slow!
				Rectangle vr = ((JViewport)getParent()).getViewRect();
				miny = vr.y - ch;
				maxy = vr.y + vr.height + ch;
			}
			int j,r,ty;
			for (j=0, r=0, ty=y+ca; j<data.length; j+=bpr, r++, ty+=ch) {
				// this trick is the key to making this not crazy-slow!
				if (ty < miny) continue;
				else if (ty > maxy) break;
				// background
				g.setColor(colorScheme[3+(r&1)]);
				g.fillRect(x+cw*9+1, y+r*ch, w-cw*9-1, ch);
				// selection highlighting
				if (ss < j+bpr && se > j) {
					if (ss == se) {
						int s = ss-j;
						int hs = x+cw*(9+3*s)+3*cw/4;
						int ds = x+cw*(11+3*bpr+s);
						g.setColor(colorScheme[midByte?(dataSide?13:11):(dataSide?12:10)]);
						g.fillRect(hs, y+r*ch, 2, ch);
						g.setColor(colorScheme[midByte?(dataSide?11:13):(dataSide?10:12)]);
						g.fillRect(ds, y+r*ch, 2, ch);
					} else {
						int s = Math.max(ss-j,0);
						int e = Math.min(se-j,bpr);
						int hs = x+cw*(9+3*s)+cw/2;
						int he = x+cw*(9+3*e)+cw/2;
						int ds = x+cw*(11+3*bpr+s);
						int de = x+cw*(11+3*bpr+e);
						g.setColor(colorScheme[(dataSide?16:14)+(r&1)]);
						g.fillRect(hs, y+r*ch, he-hs, ch);
						g.setColor(colorScheme[(dataSide?14:16)+(r&1)]);
						g.fillRect(ds, y+r*ch, de-ds, ch);
					}
				} else if (ss == j+bpr && ss == se) {
					int hs = x+cw*(9+3*bpr)+cw/4;
					int ds = x+cw*(11+4*bpr);
					g.setColor(colorScheme[midByte?(dataSide?13:11):(dataSide?12:10)]);
					g.fillRect(hs, y+r*ch, 2, ch);
					g.setColor(colorScheme[midByte?(dataSide?11:13):(dataSide?10:12)]);
					g.fillRect(ds, y+r*ch, 2, ch);
				} else if (se == j && ss == se) {
					int hs = x+cw*9+3*cw/4;
					int ds = x+cw*(11+3*bpr);
					g.setColor(colorScheme[midByte?(dataSide?13:11):(dataSide?12:10)]);
					g.fillRect(hs, y+r*ch, 2, ch);
					g.setColor(colorScheme[midByte?(dataSide?11:13):(dataSide?10:12)]);
					g.fillRect(ds, y+r*ch, 2, ch);
				}
				// address
				g.setColor(colorScheme[1]);
				String a;
				if (decAddr) {
					a = "        "+Integer.toString(j);
				} else {
					a = "00000000"+Integer.toHexString(j).toUpperCase();
				}
				a = a.substring(a.length()-8)+":";
				g.drawString(a, x, ty);
				// text
				for (int k=j, hx=x+cw*10, dx=x+cw*(11+3*bpr); k<(j+bpr) && k<data.length; k++, hx+=cw*3, dx+=cw) {
					// hex digits
					g.setColor(colorScheme[((k >= ss && k < se)?(dataSide?20:18):5)+(r&1)]);
					g.drawString(LOOKUP_HEX[data[k]&0xFF], hx, ty);
					// character
					d[0] = textEncodingLookup[data[k]&0xFF];
					if ((d[0] < 0) || (d[0] >= 0xFFFD)) {
						g.setColor(colorScheme[((k >= ss && k < se)?(dataSide?22:24):7)+(r&1)]);
						d[0] = '.';
					} else if (d[0] < 0x20) {
						g.setColor(colorScheme[((k >= ss && k < se)?(dataSide?22:24):7)+(r&1)]);
						d[0] += 0x40;
					} else if (d[0] >= 0x7F && d[0] <= 0x9F) {
						g.setColor(colorScheme[((k >= ss && k < se)?(dataSide?22:24):7)+(r&1)]);
						if (d[0] == 0x7F) d[0] = '?';
						else if (d[0] >= 0x80 && d[0] <= 0x9E) d[0] -= 0x20;
						else if (d[0] == 0x9F) d[0] = '/';
					} else {
						g.setColor(colorScheme[((k >= ss && k < se)?(dataSide?18:20):5)+(r&1)]);
					}
					g.drawChars(d, 0, 1, dx, ty);
				}
			}
			// selection highlighting on first blank line if applicable
			if (se == j && ss == se) {
				int hs = x+cw*9+3*cw/4;
				int ds = x+cw*(11+3*bpr);
				g.setColor(colorScheme[midByte?(dataSide?13:11):(dataSide?12:10)]);
				g.fillRect(hs, y+r*ch, 2, ch);
				g.setColor(colorScheme[midByte?(dataSide?11:13):(dataSide?10:12)]);
				g.fillRect(ds, y+r*ch, 2, ch);
			}
			// word separators
			g.setColor(colorScheme[9]);
			for (j=4; j<bpr; j+=4) {
				g.drawLine(x+cw*(9+3*j)+cw/2, y, x+cw*(9+3*j)+cw/2, y+h-1);
			}
			g.drawLine(x+cw*(10+3*bpr), y, x+cw*(10+3*bpr), y+h-1);
		}
		g.setColor(c);
	}
	
	@Override
	public void setMinimumSize(Dimension minimumSize) {
		super.setMinimumSize(minsize = minimumSize);
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(prefsize = preferredSize);
	}
	
	@Override
	public Dimension getMinimumSize() {
		if (minsize != null) return minsize;
		else {
			if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
			Insets i = getInsets();
			int w = 28*cw;
			int h = ch;
			return new Dimension(w+i.left+i.right,h+i.top+i.bottom);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (prefsize != null) return prefsize;
		else {
			if (getParent() instanceof JViewport) {
				// this ugly hack is needed to make JScrollPanes scroll properly
				return new Dimension(getWidth(), Math.max(getParent().getHeight(), calculateHeight()));
			}
			if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
			Insets i = getInsets();
			int w = 76*cw;
			int h = ((data.length+15)/16)*ch;
			return new Dimension(w+i.left+i.right,h+i.top+i.bottom);
		}
	}

	public Dimension getPreferredScrollableViewportSize() {
		if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
		Insets i = getInsets();
		int w = 44*cw;
		int h = ((data.length+7)/8)*ch;
		return new Dimension(w+i.left+i.right,h+i.top+i.bottom);
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
		Insets i = getInsets();
		if (orientation == SwingConstants.HORIZONTAL) {
	        int cp = visibleRect.x-i.left;
	        if (direction < 0) {
		    	int np = i.left + cp - (cp / cw) * cw;
		    	return (np == 0)?cw:np;
		    } else {
		    	return i.left + ((cp / cw) + 1) * cw - cp;
		    }
	    } else {
	        int cp = visibleRect.y-i.top;
		    if (direction < 0) {
		    	int np = i.top + cp - (cp / ch) * ch;
		    	return (np == 0)?ch:np;
		    } else {
		    	return i.top + ((cp / ch) + 1) * ch - cp;
		    }
	    }
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
		if (orientation == SwingConstants.HORIZONTAL)
	        return visibleRect.width - cw;
	    else
	        return visibleRect.height - ch;
	}
	
	// all these overrides are to make JScrollPanes work
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		if (getParent() instanceof JViewport) bounds.height = Math.max(getParent().getHeight(), calculateHeight());
		return bounds;
	}
	
	@Override
	public Rectangle getBounds(Rectangle rv) {
		rv = super.getBounds(rv);
		if (getParent() instanceof JViewport) rv.height = Math.max(getParent().getHeight(), calculateHeight());
		return rv;
	}
	
	@Deprecated
	@Override
	public Rectangle bounds() {
		Rectangle bounds = super.bounds();
		if (getParent() instanceof JViewport) bounds.height = Math.max(getParent().getHeight(), calculateHeight());
		return bounds;
	}
	
	@Override
	public Dimension getSize() {
		Dimension size = super.getSize();
		if (getParent() instanceof JViewport) size.height = Math.max(getParent().getHeight(), calculateHeight());
		return size;
	}
	
	@Override
	public Dimension getSize(Dimension rv) {
		rv = super.getSize(rv);
		if (getParent() instanceof JViewport) rv.height = Math.max(getParent().getHeight(), calculateHeight());
		return rv;
	}
	
	@Deprecated
	@Override
	public Dimension size() {
		Dimension size = super.size();
		if (getParent() instanceof JViewport) size.height = Math.max(getParent().getHeight(), calculateHeight());
		return size;
	}
	
	@Override
	public int getHeight() {
		if (getParent() instanceof JViewport) {
			return Math.max(getParent().getHeight(), calculateHeight());
		} else {
			return super.getHeight();
		}
	}
	
	public int calculateHeight() {
		if (ca < 0 || ch < 0 || cw < 0) {
			Graphics g = getGraphics();
			if (g == null) g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
			recalcFontMetrics(g.getFontMetrics(getFont()));
		}
		Insets i = getInsets();
		int h;
		int w = getWidth()-i.left-i.right;
		int bpr = (w - 11*cw)/(4*cw);
		if (bpr < 1) {
			h = i.top+i.bottom+4*ch;
		} else {
			if (bpr>4) bpr = 4*(bpr/4);
			h = i.top+i.bottom+((data.length+bpr-1)/bpr)*ch;
		}
		return h;
	}
	
	public int calculateWidth(int numBytes) {
		if (ca < 0 || ch < 0 || cw < 0) {
			Graphics g = getGraphics();
			if (g == null) g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
			recalcFontMetrics(g.getFontMetrics(getFont()));
		}
		Insets i = getInsets();
		return i.left+i.right+cw*(12+numBytes*4);
	}
	
	public void setWidth(int numBytes) {
		int nw = calculateWidth(numBytes);
		if (nw > 0) {
			Component c = getTopLevelAncestor();
			int diff = c.getWidth()-this.getWidth();
			c.setSize(nw+diff, c.getHeight());
		}
	}
	
	private int sanesel(int s) {
		if (s < 0) return 0;
		if (s > data.length) return data.length;
		return s;
	}
	
	// these events are unused
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void lostOwnership(Clipboard c, Transferable t) {}

	public void mouseClicked(MouseEvent e) {
		requestFocusInWindow();
	}

	public void mouseReleased(MouseEvent e) {
		requestFocusInWindow();
		if (e.isPopupTrigger()) {
			menu.show(e.getComponent(), e.getX()+5, e.getY()+5);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		requestFocusInWindow();
		if (e.isPopupTrigger()) {
			menu.show(e.getComponent(), e.getX()+5, e.getY()+5);
		} else {
			if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
			Insets i = getInsets();
			Point p = e.getPoint();
			p.x -= i.left;
			p.y -= i.top;
			int w = getWidth()-i.left-i.right;
			int bpr = (w - 11*cw)/(4*cw);
			if (bpr > 0) {
				if (bpr>4) bpr = 4*(bpr/4);
				int divAH = cw*9+1;
				int divHD = cw*(10+3*bpr);
				if (p.x < divAH) {
					// in the address area
					midByte = false;
					dataSide = false;
					if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0) selStart = sanesel((p.y / ch)*bpr);
					selEnd = sanesel(((p.y / ch)+1)*bpr);
					repaint();
					fireHexSelectionChanged();
				} else if (p.x < divHD) {
					// in the hex area
					midByte = false;
					dataSide = false;
					selEnd = sanesel( ((p.y / ch)*bpr) + Math.max(0, Math.min(bpr, (((p.x / cw)-9)/3) )) );
					if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0) selStart = selEnd;
					repaint();
					fireHexSelectionChanged();
				} else if (p.x < w) {
					// in the data area
					midByte = false;
					dataSide = true;
					selEnd = sanesel( ((p.y / ch)*bpr) + Math.max(0, Math.min(bpr, ((p.x / cw)-(11+bpr*3)) )) );
					if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0) selStart = selEnd;
					repaint();
					fireHexSelectionChanged();
				}
				e.consume();
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (!e.isPopupTrigger()) {
			if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
			Insets i = getInsets();
			Point p = e.getPoint();
			p.x -= i.left;
			p.y -= i.top;
			int w = getWidth()-i.left-i.right;
			int bpr = (w - 11*cw)/(4*cw);
			if (bpr > 0) {
				if (bpr>4) bpr = 4*(bpr/4);
				int divAH = cw*9+1;
				int divHD = cw*(10+3*bpr);
				if (p.x < divAH) {
					// in the address area
					midByte = false;
					dataSide = false;
					selEnd = ((p.y / ch)+1)*bpr;
					if (selEnd <= selStart) selEnd -= bpr;
					selEnd = sanesel(selEnd);
					repaint();
					fireHexSelectionChanged();
				} else if (p.x < divHD) {
					// in the hex area
					midByte = false;
					dataSide = false;
					selEnd = sanesel( ((p.y / ch)*bpr) + Math.max(0, Math.min(bpr, (((p.x / cw)-9)/3) )) );
					repaint();
					fireHexSelectionChanged();
				} else if (p.x < w) {
					// in the data area
					midByte = false;
					dataSide = true;
					selEnd = sanesel( ((p.y / ch)*bpr) + Math.max(0, Math.min(bpr, ((p.x / cw)-(11+bpr*3)) )) );
					repaint();
					fireHexSelectionChanged();
				}
				e.consume();
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if (menu != null && menu.isShowing()) {
			super.setCursor(null);
		} else if (curs == null) {
			if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
			Insets i = getInsets();
			Point p = e.getPoint();
			p.x -= i.left;
			p.y -= i.top;
			int w = getWidth()-i.left-i.right;
			int bpr = (w - 11*cw)/(4*cw);
			if (bpr > 0) {
				if (bpr>4) bpr = 4*(bpr/4);
				int divAH = cw*9+1;
				if (p.x >= divAH && p.x < w) {
					super.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
				} else {
					super.setCursor(null);
				}
				e.consume();
			}
		}
	}
	
	private int charToByte(char d) {
		if (textEncoding.startsWith("XX")) {
			if (textEncoding.equalsIgnoreCase("XX-HighASCII")) {
				if (d >= 0x80) return -1;
				else return (d & 0xFF);
			} else if (textEncoding.equalsIgnoreCase("XX-AppleII-Primary") || textEncoding.equalsIgnoreCase("XX-AppleII-Alternate")) {
				if (d >= 0x80) return -1;
				else return ((d & 0xFF) | 0x80);
			} else {
				if (d >= 0x100) return -1;
				else return (d & 0xFF);
			}
		} else try {
			String s = new String(new char[]{d});
			byte[] b = s.getBytes(textEncoding);
			if (b.length < 1) return -1;
			return (b[0] & 0xFF);
		} catch (UnsupportedEncodingException uee) {
			if (d >= 0x100) return -1;
			else return (d & 0xFF);
		}
	}
	
	private byte[] hexStringToBytes(String s) {
		StringBuffer h = new StringBuffer();
		CharacterIterator i = new StringCharacterIterator(s);
		for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) {
			if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f')) {
				h.append(ch);
			} else if ((ch >= 0x21 && ch <= 0x7E) || (ch >= 0xA1)) {
				return null;
			}
		}
		if ((h.length() & 1) > 0) h.insert(0, '0');
		byte[] b = new byte[h.length()/2];
		for (int j=0; j<b.length; j++) {
			char c1 = h.charAt(j+j);
			char c2 = h.charAt(j+j+1);
			int v1 = (c1 >= '0' && c1 <= '9')?(c1-'0'):(c1 >= 'A' && c1 <= 'F')?(c1-'A'+10):(c1 >= 'a' && c1 <= 'f')?(c1-'a'+10):0;
			int v2 = (c2 >= '0' && c2 <= '9')?(c2-'0'):(c2 >= 'A' && c2 <= 'F')?(c2-'A'+10):(c2 >= 'a' && c2 <= 'f')?(c2-'a'+10):0;
			b[j] = (byte)((v1 << 4) | v2);
		}
		return b;
	}

	public void keyPressed(KeyEvent e) {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			if (readOnly) return;
			if (ss == se) {
				if (ss > 0) {
					if (overtype) {
						data[ss-1] = 0;
					} else {
						data = arrayCut(data, ss-1, 1)[0];
					}
					selStart = selEnd = se = ss-1;
					midByte = false;
					fireHexDataChanged();
					fireHexSelectionChanged();
				}
			} else {
				data = arrayCut(data, ss, se-ss)[0];
				selStart = selEnd = se = ss;
				midByte = false;
				fireHexDataChanged();
				fireHexSelectionChanged();
			}
			break;
		case KeyEvent.VK_DELETE:
			if (readOnly) return;
			if (ss == se) {
				if (ss < data.length) {
					if (overtype) {
						data[ss] = 0;
						selStart = selEnd = se = ss+1;
					} else {
						data = arrayCut(data, ss, 1)[0];
					}
					midByte = false;
					fireHexDataChanged();
					fireHexSelectionChanged();
				}
			} else {
				data = arrayCut(data, ss, se-ss)[0];
				selStart = selEnd = se = ss;
				midByte = false;
				fireHexDataChanged();
				fireHexSelectionChanged();
			}
			break;
		case KeyEvent.VK_LEFT:
			if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0) {
				selEnd = sanesel(selEnd-1);
				midByte = false;
				fireHexSelectionChanged();
			} else {
				selStart = selEnd = sanesel(selStart-1);
				midByte = false;
				fireHexSelectionChanged();
			}
			break;
		case KeyEvent.VK_RIGHT:
			if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0) {
				selEnd = sanesel(selEnd+1);
				midByte = false;
				fireHexSelectionChanged();
			} else {
				selStart = selEnd = sanesel(selStart+1);
				midByte = false;
				fireHexSelectionChanged();
			}
			break;
		case KeyEvent.VK_UP:
			if (true) {
				if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
				Insets i = getInsets();
				int w = getWidth()-i.left-i.right;
				int bpr = (w - 11*cw)/(4*cw);
				if (bpr > 0) {
					if (bpr>4) bpr = 4*(bpr/4);
					if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0) {
						selEnd = sanesel(selEnd-bpr);
						midByte = false;
						fireHexSelectionChanged();
					} else {
						selStart = selEnd = sanesel(selStart-bpr);
						midByte = false;
						fireHexSelectionChanged();
					}
				}
			}
			break;
		case KeyEvent.VK_DOWN:
			if (true) {
				if (ca < 0 || ch < 0 || cw < 0) recalcFontMetrics(getGraphics().getFontMetrics(getFont()));
				Insets i = getInsets();
				int w = getWidth()-i.left-i.right;
				int bpr = (w - 11*cw)/(4*cw);
				if (bpr > 0) {
					if (bpr>4) bpr = 4*(bpr/4);
					if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0) {
						selEnd = sanesel(selEnd+bpr);
						midByte = false;
						fireHexSelectionChanged();
					} else {
						selStart = selEnd = sanesel(selStart+bpr);
						midByte = false;
						fireHexSelectionChanged();
					}
				}
			}
			break;
		case KeyEvent.VK_ENTER:
			dataSide = !dataSide;
			break;
		case KeyEvent.VK_INSERT:
			overtype = !overtype;
			fireHexModeChanged(HexModeChangeEvent.MODE_OVERTYPE);
			break;
		case KeyEvent.VK_X:
			if (ignoreControlKeys) return;
			if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0);
			else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
			else return;
		case KeyEvent.VK_CUT:
			if (readOnly) return;
			cut();
			break;
		case KeyEvent.VK_C:
			if (ignoreControlKeys) return;
			if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0);
			else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
			else return;
		case KeyEvent.VK_COPY:
			copy();
			break;
		case KeyEvent.VK_V:
			if (ignoreControlKeys) return;
			if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0);
			else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
			else return;
		case KeyEvent.VK_PASTE:
			if (readOnly) return;
			paste();
			break;
		case KeyEvent.VK_A:
			if (ignoreControlKeys) return;
			if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0);
			else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
			else return;
			selectAll();
			break;
		case KeyEvent.VK_J:
			if (ignoreControlKeys) return;
			if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0);
			else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
			else return;
			String r = (ss == se)?(decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())
					:((decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())+":"+(decAddr?Integer.toString(se):Integer.toHexString(se).toUpperCase()));
			Object o = JOptionPane.showInputDialog(
					JHexEditor.this,
					"Enter address range:",
					"Jump to Address",
					JOptionPane.QUESTION_MESSAGE,
					null,
					null,
					r
			);
			if (o != null && o.toString().length() > 0) try {
				r = o.toString();
				String[] rr = r.split(":");
				if (rr.length > 1) {
					selStart = Integer.parseInt(rr[0],decAddr?10:16);
					selEnd = Integer.parseInt(rr[1],decAddr?10:16);
				} else {
					selStart = selEnd = Integer.parseInt(r,decAddr?10:16);
				}
				repaint();
				fireHexSelectionChanged();
			} catch (Exception ex) {}
			break;
		default:
			return;
		}
		guiChanged();
		e.consume();
	}

	public void keyTyped(KeyEvent e) {
		if ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) > 0) return;
		else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0) return;
		char d = e.getKeyChar();
		if (d >= 0x20 && d != 0x7F) {
			int ss = sanesel(Math.min(selStart, selEnd));
			int se = sanesel(Math.max(selStart, selEnd));
			if (dataSide) {
				if (readOnly) return;
				int b = charToByte(d);
				if (b >= 0) {
					if (ss != se) {
						data = arrayCut(data, ss, se-ss)[0];
						selStart = selEnd = se = ss;
						data = arrayPaste(data, ss, new byte[]{(byte)b})[0];
					} else if (overtype && ss < data.length) {
						data[ss] = (byte)b;
					} else {
						data = arrayPaste(data, ss, new byte[]{(byte)b})[0];
					}
					selStart++;
					selEnd++;
					midByte = false;
					fireHexDataChanged();
					fireHexSelectionChanged();
				}
			} else {
				int v = -1;
				if (d >= '0' && d <= '9') v = (d-'0');
				else if (d >= 'A' && d <= 'F') v = (d-'A'+10);
				else if (d >= 'a' && d <= 'f') v = (d-'a'+10);
				if (v >= 0) {
					if (readOnly) return;
					if (ss != se) {
						data = arrayCut(data, ss, se-ss)[0];
						selStart = selEnd = se = ss;
						data = arrayPaste(data, ss, new byte[]{(byte)v})[0];
						selStart++;
						selEnd++;
						midByte = true;
						fireHexDataChanged();
						fireHexSelectionChanged();
					} else if (midByte && ss>0) {
						data[ss-1] = (byte)((data[ss-1] << 4) | v);
						midByte = false;
						fireHexDataChanged();
					} else if (overtype && ss < data.length) {
						data[ss] = (byte)v;
						selStart++;
						selEnd++;
						midByte = true;
						fireHexDataChanged();
						fireHexSelectionChanged();
					} else {
						data = arrayPaste(data, ss, new byte[]{(byte)v})[0];
						selStart++;
						selEnd++;
						midByte = true;
						fireHexDataChanged();
						fireHexSelectionChanged();
					}
				} else {
					switch (d) {
					case 'I': case 'i':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(~data[ss-1]);
						} else {
							for (int j=ss; j<se; j++)
								data[j] = (byte)(~data[j]);
						}
						fireHexDataChanged();
						break;
					case '<':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)((data[ss-1] & 0xFF) << 1);
						} else {
							if (le) {
								for (int j=se-1; j>ss; j--)
									data[j] = (byte)(((data[j] & 0xFF) << 1) | ((data[j-1] & 0xFF) >>> 7));
								data[ss] = (byte)((data[ss] & 0xFF) << 1);
							} else {
								for (int j=ss; j<(se-1); j++)
									data[j] = (byte)(((data[j] & 0xFF) << 1) | ((data[j+1] & 0xFF) >>> 7));
								data[se-1] = (byte)((data[se-1] & 0xFF) << 1);
							}
						}
						fireHexDataChanged();
						break;
					case '>':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)((data[ss-1] & 0xFF) >>> 1);
						} else {
							if (le) {
								for (int j=ss; j<(se-1); j++)
									data[j] = (byte)(((data[j] & 0xFF) >>> 1) | ((data[j+1] & 0xFF) << 7));
								data[se-1] = (byte)((data[se-1] & 0xFF) >>> 1);
							} else {
								for (int j=se-1; j>ss; j--)
									data[j] = (byte)(((data[j] & 0xFF) >>> 1) | ((data[j-1] & 0xFF) << 7));
								data[ss] = (byte)((data[ss] & 0xFF) >>> 1);
							}
						}
						fireHexDataChanged();
						break;
					case 'L': case 'l':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(((data[ss-1] & 0xFF) << 1) | ((data[ss-1] & 0xFF) >>> 7));
						} else {
							if (le) {
								int f = ((data[se-1] & 0xFF) >>> 7);
								for (int j=se-1; j>ss; j--)
									data[j] = (byte)(((data[j] & 0xFF) << 1) | ((data[j-1] & 0xFF) >>> 7));
								data[ss] = (byte)(((data[ss] & 0xFF) << 1) | f);
							} else {
								int f = ((data[ss] & 0xFF) >>> 7);
								for (int j=ss; j<(se-1); j++)
									data[j] = (byte)(((data[j] & 0xFF) << 1) | ((data[j+1] & 0xFF) >>> 7));
								data[se-1] = (byte)(((data[se-1] & 0xFF) << 1) | f);
							}
						}
						fireHexDataChanged();
						break;
					case 'R': case 'r':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(((data[ss-1] & 0xFF) >>> 1) | ((data[ss-1] & 0xFF) << 7));
						} else {
							if (le) {
								int f = ((data[ss] & 0xFF) << 7);
								for (int j=ss; j<(se-1); j++)
									data[j] = (byte)(((data[j] & 0xFF) >>> 1) | ((data[j+1] & 0xFF) << 7));
								data[se-1] = (byte)(((data[se-1] & 0xFF) >>> 1) | f);
							} else {
								int f = ((data[se-1] & 0xFF) << 7);
								for (int j=se-1; j>ss; j--)
									data[j] = (byte)(((data[j] & 0xFF) >>> 1) | ((data[j-1] & 0xFF) << 7));
								data[ss] = (byte)(((data[ss] & 0xFF) >>> 1) | f);
							}
						}
						fireHexDataChanged();
						break;
					case 'S': case 's':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(Integer.reverse(data[ss-1] & 0xFF) >>> 24);
						} else {
							int end = ss+(se-ss+1)/2;
							for (int j=ss; j<end; j++) {
								int other = se-(j-ss)-1;
								byte tmp = (byte)(Integer.reverse(data[other] & 0xFF) >>> 24);
								data[other] = (byte)(Integer.reverse(data[j] & 0xFF) >>> 24);
								data[j] = tmp;
							}
						}
						fireHexDataChanged();
						break;
					case 'T': case 't':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0 && ss < data.length) {
								byte tmp = data[ss-1];
								data[ss-1] = data[ss];
								data[ss] = tmp;
							}
						} else {
							int end = ss+(se-ss)/2;
							for (int j=ss; j<end; j++) {
								int other = se-(j-ss)-1;
								byte tmp = data[other];
								data[other] = data[j];
								data[j] = tmp;
							}
						}
						fireHexDataChanged();
						break;
					case ']':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(data[ss-1] + 1);
							fireHexDataChanged();
						} else {
							replaceSelection(getSelectedValue() + 1);
						}
						break;
					case '[':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(data[ss-1] - 1);
							fireHexDataChanged();
						} else {
							replaceSelection(getSelectedValue() - 1);
						}
						break;
					case ',':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(data[ss-1] ^ 0x80);
						} else if (le) {
							data[se-1] = (byte)(data[se-1] ^ 0x80);
						} else {
							data[ss] = (byte)(data[ss] ^ 0x80);
						}
						fireHexDataChanged();
						break;
					case '.':
						if (readOnly) return;
						if (ss == se) {
							if (ss > 0)
								data[ss-1] = (byte)(data[ss-1] ^ 0x01);
						} else if (le) {
							data[ss] = (byte)(data[ss] ^ 0x01);
						} else {
							data[se-1] = (byte)(data[se-1] ^ 0x01);
						}
						fireHexDataChanged();
						break;
					case '}':
						acc++;
						fireHexAccumulatorChanged();
						break;
					case '{':
						acc--;
						fireHexAccumulatorChanged();
						break;
					case ')':
						acc >>>= 1;
						fireHexAccumulatorChanged();
						break;
					case '(':
						acc <<= 1;
						fireHexAccumulatorChanged();
						break;
					case 'Z': case 'z':
						if (readOnly) return;
						if (ss == se) {
							if (overtype && ss < data.length) {
								data[ss] = 0;
							} else {
								data = arrayPaste(data, ss, new byte[]{0})[0];
							}
							selStart++;
							selEnd++;
							midByte = false;
							fireHexDataChanged();
							fireHexSelectionChanged();
						} else {
							for (int j=ss; j<se; j++)
								data[j] = 0;
							fireHexDataChanged();
						}
						break;
					case 'X': case 'x':
						if (readOnly) return;
						if (ss == se) {
							if (overtype && ss < data.length) {
								data[ss] = -1;
							} else {
								data = arrayPaste(data, ss, new byte[]{-1})[0];
							}
							selStart++;
							selEnd++;
							midByte = false;
							fireHexDataChanged();
							fireHexSelectionChanged();
						} else {
							for (int j=ss; j<se; j++)
								data[j] = -1;
							fireHexDataChanged();
						}
						break;
					case 'Y': case 'y':
						if (readOnly) return;
						if (ss == se) {
							byte[] a = new byte[1];
							rand.nextBytes(a);
							if (overtype && ss < data.length) {
								data[ss] = a[0];
							} else {
								data = arrayPaste(data, ss, a)[0];
							}
							selStart++;
							selEnd++;
							midByte = false;
							fireHexDataChanged();
							fireHexSelectionChanged();
						} else {
							byte[] a = new byte[se-ss];
							rand.nextBytes(a);
							for (int j=ss, k=0; j<se; j++, k++)
								data[j] = a[k];
							fireHexDataChanged();
						}
						break;
					case 'V': case 'v':
						if (readOnly) return;
						if (ss == se) {
							if (overtype) {
								int j=0;
								for (j=0; j<256 && (ss+j)<data.length; j++) {
									data[ss+j] = (byte)j;
								}
								if (j < 256) {
									byte[] a = new byte[256-j];
									for (int k=0; k<a.length && j<256; j++, k++) {
										a[k] = (byte)j;
									}
									data = arrayPaste(data, data.length, a)[0];
								}
								selStart += 256;
								selEnd += 256;
								midByte = false;
								fireHexDataChanged();
								fireHexSelectionChanged();
							} else {
								byte[] a = new byte[256];
								for (int j=0; j<256; j++)
									a[j] = (byte)j;
								data = arrayPaste(data, ss, a)[0];
								selStart += 256;
								selEnd += 256;
								midByte = false;
								fireHexDataChanged();
								fireHexSelectionChanged();
							}
						} else {
							for (int j=ss, k=0; j<se; j++, k++)
								data[j] = (byte)k;
							fireHexDataChanged();
						}
						break;
					case '\'':
						if (readOnly) return;
						if (se-ss < 256) {
							if (overtype && ss > 0) {
								data[ss-1] = (byte)(se-ss);
							} else {
								data = arrayPaste(data, ss, new byte[]{(byte)(se-ss)})[0];
								selStart++;
								selEnd++;
								fireHexSelectionChanged();
							}
							midByte = false;
							fireHexDataChanged();
						}
						break;
					case '\"':
						if (readOnly) return;
						if (se-ss < 65536) {
							byte[] dd;
							if (le) {
								dd = new byte[]{(byte)(se-ss),(byte)((se-ss)>>8)};
							} else {
								dd = new byte[]{(byte)((se-ss)>>8),(byte)(se-ss)};
							}
							if (overtype && ss > 1) {
								data[ss-2] = dd[0];
								data[ss-1] = dd[1];
							} else {
								data = arrayPaste(data, ss, dd)[0];
								selStart+=2;
								selEnd+=2;
								fireHexSelectionChanged();
							}
							midByte = false;
							fireHexDataChanged();
						}
						break;
					case '`':
						if (ss == se) {
							acc = 0;
							fireHexAccumulatorChanged();
						} else {
							acc = getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '+':
						if (ss != se) {
							acc += getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '-':
						if (ss == se) {
							acc = -acc;
							fireHexAccumulatorChanged();
						} else {
							acc -= getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '*':
						if (ss != se) {
							acc *= getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '/':
						if (ss == se) {
							acc = (acc == 0)?1:0;
							fireHexAccumulatorChanged();
						} else {
							try {
								acc /= getSelectedValue();
							} catch (ArithmeticException ae) {
								acc = 0;
							}
							fireHexAccumulatorChanged();
						}
						break;
					case '%':
						if (ss != se) {
							try {
								acc %= getSelectedValue();
							} catch (ArithmeticException ae) {
								acc = 0;
							}
							fireHexAccumulatorChanged();
						}
						break;
					case '_':
						if (ss == se) {
							acc = -acc;
							fireHexAccumulatorChanged();
						} else {
							acc = getSelectedValue() - acc;
							fireHexAccumulatorChanged();
						}
						break;
					case '\\':
						if (ss == se) {
							acc = (acc == 0)?1:0;
							fireHexAccumulatorChanged();
						} else {
							try {
								acc = getSelectedValue() / acc;
							} catch (ArithmeticException ae) {
								acc = 0;
							}
							fireHexAccumulatorChanged();
						}
						break;
					case '$':
						if (ss != se) {
							try {
								acc = getSelectedValue() % acc;
							} catch (ArithmeticException ae) {
								acc = 0;
							}
							fireHexAccumulatorChanged();
						}
						break;
					case '&':
						if (ss != se) {
							acc &= getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '^':
						if (ss != se) {
							acc ^= getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '|':
						if (ss != se) {
							acc |= getSelectedValue();
							fireHexAccumulatorChanged();
						}
						break;
					case '~':
						acc = ~acc;
						fireHexAccumulatorChanged();
						break;
					case '=':
						if (readOnly) return;
						if (ss != se) {
							replaceSelection(acc);
						}
						break;
					case 'J': case 'j':
						String r = (ss == se)?(decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())
								:((decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())+":"+(decAddr?Integer.toString(se):Integer.toHexString(se).toUpperCase()));
						Object o = JOptionPane.showInputDialog(
								JHexEditor.this,
								"Enter address range:",
								"Jump to Address",
								JOptionPane.QUESTION_MESSAGE,
								null,
								null,
								r
						);
						if (o != null && o.toString().length() > 0) try {
							r = o.toString();
							String[] rr = r.split(":");
							if (rr.length > 1) {
								selStart = Integer.parseInt(rr[0],decAddr?10:16);
								selEnd = Integer.parseInt(rr[1],decAddr?10:16);
							} else {
								selStart = selEnd = Integer.parseInt(r,decAddr?10:16);
							}
							repaint();
							fireHexSelectionChanged();
						} catch (Exception ex) {}
						break;
					case 'Q': case 'q':
						textEncoding = "ISO-8859-1";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'W': case 'w':
						textEncoding = "CP1252";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'G': case 'g':
						textEncoding = "US-ASCII";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'H': case 'h':
						textEncoding = "XX-HIGHASCII";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'N': case 'n':
						textEncoding = "CP437";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'M': case 'm':
						textEncoding = "MACROMAN";
						prepTextEncoding();
						repaint();
						fireHexDisplayChanged();
						break;
					case 'U': case 'u':
						le = !le;
						fireHexModeChanged(HexModeChangeEvent.MODE_ENDIANNESS);
						break;
					case 'O': case 'o':
						overtype = !overtype;
						fireHexModeChanged(HexModeChangeEvent.MODE_OVERTYPE);
						break;
					case 'P': case 'p':
						decAddr = !decAddr;
						repaint();
						fireHexModeChanged(HexModeChangeEvent.MODE_DECIMAL_ADDRESSES);
						break;
					case '?':
						{
							Graphics g = getGraphics();
							Insets i = getInsets();
							int w = getWidth()-i.left-i.right;
							int h = getHeight()-i.top-i.bottom;
							g.setColor(colorScheme[26]);
							g.fillRect(i.left+16, i.top+16, w-32, h-32);
							g.setColor(colorScheme[28]);
							g.drawRect(i.left+16, i.top+16, w-32, h-32);
							g.setFont(getFont());
							g.setColor(colorScheme[27]);
							int ca = g.getFontMetrics().getAscent();
							int ch = g.getFontMetrics().getHeight();
							g.drawString("JHexEditor - 1.0", i.left+32, i.top+32+ca);
							g.drawString("\u00A9 2007 Kreative Software", i.left+32, i.top+32+ca+ch);
							if (colorScheme == COLOR_SCHEME_CAL_POLY) {
								g.drawString("Ride High You Mustangs", i.left+32, i.top+32+ca+ch*3);
								g.drawString("Kick the Frost Out, Burn the Breeze", i.left+32, i.top+32+ca+ch*4);
								g.drawString("Ride High You Mustangs", i.left+32, i.top+32+ca+ch*5);
								g.drawString("The Bow Wows We'll Knock to Their Knees", i.left+32, i.top+32+ca+ch*6);
								g.drawString("Hi Ki Yi", i.left+32, i.top+32+ca+ch*7);
								g.drawString("Ride High You Mustangs", i.left+32, i.top+32+ca+ch*8);
								g.drawString("Chin the Moon and Do It Right", i.left+32, i.top+32+ca+ch*9);
								g.drawString("Ride High and Cut a Rusty", i.left+32, i.top+32+ca+ch*10);
								g.drawString("Fight Fight Fight", i.left+32, i.top+32+ca+ch*11);
								g.drawString("Million-dollar reward * to the first person", i.left+32, i.top+32+ca+ch*13);
								g.drawString("who can explain what the hell these lines mean.", i.left+32, i.top+32+ca+ch*14);
								g.drawString("* Not really.", i.left+32, i.top+32+ca+ch*20);
							} else {
								g.drawString("Special Keys When Editing Hex", i.left+32, i.top+32+ca+ch*3);
								g.drawString("(Letter Keys Are Case-Insensitive):", i.left+32, i.top+32+ca+ch*4);
								g.drawString("I - Invert selected bits", i.left+32, i.top+32+ca+ch*5);
								g.drawString("Z - Clear selected bits", i.left+32, i.top+32+ca+ch*6);
								g.drawString("X - Set selected bits", i.left+32, i.top+32+ca+ch*7);
								g.drawString("< - Shift selected bits left", i.left+32, i.top+32+ca+ch*8);
								g.drawString("> - Shift selected bits right", i.left+32, i.top+32+ca+ch*9);
								g.drawString("L - Rotate selected bits left", i.left+32, i.top+32+ca+ch*10);
								g.drawString("R - Rotate selected bits right", i.left+32, i.top+32+ca+ch*11);
								g.drawString("S - Swap selected bits", i.left+32, i.top+32+ca+ch*12);
								g.drawString("T - Swap selected bytes (reverse endianness)", i.left+32, i.top+32+ca+ch*13);
								g.drawString("V - Fill selection with progression", i.left+32, i.top+32+ca+ch*14);
								g.drawString("Y - Fill selection with random data", i.left+32, i.top+32+ca+ch*15);
								g.drawString("] - Increment selected value", i.left+32, i.top+32+ca+ch*16);
								g.drawString("[ - Decrement selected value", i.left+32, i.top+32+ca+ch*17);
								g.drawString(", - Invert most significant bit", i.left+32, i.top+32+ca+ch*18);
								g.drawString(". - Invert least significant bit", i.left+32, i.top+32+ca+ch*19);
								g.drawString("\' - Prefix with length byte", i.left+32, i.top+32+ca+ch*20);
								g.drawString("\" - Prefix with length short", i.left+32, i.top+32+ca+ch*21);
								g.drawString("` - A = X (Set accumulator)", i.left+32, i.top+32+ca+ch*22);
								g.drawString("+ - A = A + X (Add to accumulator)", i.left+32, i.top+32+ca+ch*23);
								g.drawString("- - A = A - X (Subtract from accumulator)", i.left+32, i.top+32+ca+ch*24);
								g.drawString("* - A = A * X", i.left+32, i.top+32+ca+ch*25);
								g.drawString("/ - A = A / X", i.left+32, i.top+32+ca+ch*26);
								g.drawString("% - A = A % X", i.left+32, i.top+32+ca+ch*27);
								g.drawString("_ - A = X - A", i.left+32, i.top+32+ca+ch*28);
								g.drawString("\\ - A = X / A", i.left+32, i.top+32+ca+ch*29);
								g.drawString("$ - A = X % A", i.left+32, i.top+32+ca+ch*30);
								g.drawString("& - A = A & X", i.left+32, i.top+32+ca+ch*31);
								g.drawString("| - A = A | X", i.left+32, i.top+32+ca+ch*32);
								g.drawString("^ - A = A ^ X", i.left+32, i.top+32+ca+ch*33);
								g.drawString("~ - A = ~A", i.left+32, i.top+32+ca+ch*34);
								g.drawString(") - A = A >> 1", i.left+32, i.top+32+ca+ch*35);
								g.drawString("( - A = A << 1", i.left+32, i.top+32+ca+ch*36);
								g.drawString("} - A = A + 1", i.left+32, i.top+32+ca+ch*37);
								g.drawString("{ - A = A - 1", i.left+32, i.top+32+ca+ch*38);
								g.drawString("= - X = A", i.left+32, i.top+32+ca+ch*39);
								g.drawString("J - Jump to address", i.left+32, i.top+32+ca+ch*40);
								g.drawString("U - Toggle big/little-endian", i.left+32, i.top+32+ca+ch*41);
								g.drawString("O - Toggle insert/overtype mode", i.left+32, i.top+32+ca+ch*42);
								g.drawString("P - Toggle hex/decimal addresses", i.left+32, i.top+32+ca+ch*43);
								g.drawString("Q - Use ISO Latin 1", i.left+32, i.top+32+ca+ch*44);
								g.drawString("W - Use Windows ANSI (CP1252)", i.left+32, i.top+32+ca+ch*45);
								g.drawString("G - Use US ASCII", i.left+32, i.top+32+ca+ch*46);
								g.drawString("H - Use High ASCII", i.left+32, i.top+32+ca+ch*47);
								g.drawString("N - Use MS-DOS Latin (CP437)", i.left+32, i.top+32+ca+ch*48);
								g.drawString("M - Use MacRoman", i.left+32, i.top+32+ca+ch*49);
								g.drawString("? - Show this screen", i.left+32, i.top+32+ca+ch*50);
							}
							e.consume();
							return;
						}
					}
				}
			}
			guiChanged();
			e.consume();
		}
	}
	
	public void cut() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		if (ss != se) {
			byte[][] stuff = arrayCut(data, ss, se-ss);
			if (dataSide) {
				try {
					String s = new String(stuff[1], textEncoding);
					StringSelection ssel = new StringSelection(s);
					Clipboard cb = getToolkit().getSystemClipboard();
					cb.setContents(ssel, this);
				} catch (UnsupportedEncodingException uee) {
					String s = new String(stuff[1]);
					StringSelection ssel = new StringSelection(s);
					Clipboard cb = getToolkit().getSystemClipboard();
					cb.setContents(ssel, this);
				}
			} else {
				StringBuffer s = new StringBuffer((se-ss)*3);
				for (int j=ss; j<se; j++) {
					s.append(LOOKUP_HEX[data[j]&0xFF]);
					s.append(" ");
				}
				if (s.length() > 0) s = s.deleteCharAt(s.length()-1);
				StringSelection ssel = new StringSelection(s.toString());
				Clipboard cb = getToolkit().getSystemClipboard();
				cb.setContents(ssel, this);
			}
			data = stuff[0];
			selStart = selEnd = se = ss;
			midByte = false;
			guiChanged();
			fireHexDataChanged();
			fireHexSelectionChanged();
		}
	}
	
	public void copy() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		if (ss != se) {
			byte[][] stuff = arrayCopy(data, ss, se-ss);
			if (dataSide) {
				try {
					String s = new String(stuff[1], textEncoding);
					StringSelection ssel = new StringSelection(s);
					Clipboard cb = getToolkit().getSystemClipboard();
					cb.setContents(ssel, this);
				} catch (UnsupportedEncodingException uee) {
					String s = new String(stuff[1]);
					StringSelection ssel = new StringSelection(s);
					Clipboard cb = getToolkit().getSystemClipboard();
					cb.setContents(ssel, this);
				}
			} else {
				StringBuffer s = new StringBuffer((se-ss)*3);
				for (int j=ss; j<se; j++) {
					s.append(LOOKUP_HEX[data[j]&0xFF]);
					s.append(" ");
				}
				if (s.length() > 0) s = s.deleteCharAt(s.length()-1);
				StringSelection ssel = new StringSelection(s.toString());
				Clipboard cb = getToolkit().getSystemClipboard();
				cb.setContents(ssel, this);
			}
			guiChanged();
		}
	}
	
	public void paste() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		Clipboard cb = getToolkit().getSystemClipboard();
		Transferable t = cb.getContents(null);
		if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				String s = (String)t.getTransferData(DataFlavor.stringFlavor);
				if (dataSide) {
					if (ss != se) {
						data = arrayCut(data, ss, se-ss)[0];
						selStart = selEnd = se = ss;
						midByte = false;
					}
					try {
						byte[] stuff = s.getBytes(textEncoding);
						data = arrayPaste(data, ss, stuff)[0];
						selStart = selEnd = se = ss + stuff.length;
						midByte = false;
					} catch (UnsupportedEncodingException uue) {
						byte[] stuff = s.getBytes();
						data = arrayPaste(data, ss, stuff)[0];
						selStart = selEnd = se = ss + stuff.length;
						midByte = false;
					}
				} else {
					byte[] stuff = hexStringToBytes(s);
					if (stuff != null) {
						if (ss != se) {
							data = arrayCut(data, ss, se-ss)[0];
							selStart = selEnd = se = ss;
							midByte = false;
						}
						data = arrayPaste(data, ss, stuff)[0];
						selStart = selEnd = se = ss + stuff.length;
						midByte = false;
					}
				}
				guiChanged();
				fireHexDataChanged();
				fireHexSelectionChanged();
			} catch (Exception ex) {}
		}
	}
	
	public int getSelectionStart() {
		return selStart;
	}
	
	public int getSelectionEnd() {
		return selEnd;
	}
	
	public int getSelectionLength() {
		return Math.abs(selEnd-selStart);
	}
	
	public void setSelectionStart(int start) {
		if (start < 0) start = 0;
		if (start > data.length) start = data.length;
		selStart = start;
		midByte = false;
		repaint();
		fireHexSelectionChanged();
	}
	
	public void setSelectionEnd(int end) {
		if (end < 0) end = 0;
		if (end > data.length) end = data.length;
		selEnd = end;
		midByte = false;
		repaint();
		fireHexSelectionChanged();
	}
	
	public void select(int start, int end) {
		if (start < 0) start = 0;
		if (end < 0) end = 0;
		if (start > data.length) start = data.length;
		if (end > data.length) end = data.length;
		selStart = start;
		selEnd = end;
		midByte = false;
		repaint();
		fireHexSelectionChanged();
	}
	
	public void selectAll() {
		selStart = 0;
		selEnd = data.length;
		midByte = false;
		repaint();
		fireHexSelectionChanged();
	}
	
	public byte[] getSelectedData() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		return arrayCopy(data, ss, se-ss)[1];
	}
	
	public String getSelectedHex() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		StringBuffer s = new StringBuffer((se-ss)*3);
		for (int j=ss; j<se; j++) {
			s.append(LOOKUP_HEX[data[j]&0xFF]);
			s.append(" ");
		}
		if (s.length() > 0) s = s.deleteCharAt(s.length()-1);
		return s.toString();
	}
	
	public long getSelectedValue() {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		long l = 0L;
		if (le) {
			for (int j=se-1; j>=ss; j--)
				l = (l << 8) | (data[j] & 0xFF);
		} else {
			for (int j=ss; j<se; j++)
				l = (l << 8) | (data[j] & 0xFF);
		}
		return l;
	}
	
	public void replaceSelection(byte[] data) {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		if (ss != se) {
			this.data = arrayCut(this.data, ss, se-ss)[0];
			selStart = selEnd = se = ss;
			midByte = false;
		}
		this.data = arrayPaste(this.data, ss, data)[0];
		selEnd = se = ss + data.length;
		midByte = false;
		guiChanged();
		fireHexDataChanged();
		fireHexSelectionChanged();
	}
	
	public void replaceSelection(String hex) {
		byte[] stuff = hexStringToBytes(hex);
		if (stuff != null) {
			int ss = sanesel(Math.min(selStart, selEnd));
			int se = sanesel(Math.max(selStart, selEnd));
			if (ss != se) {
				data = arrayCut(data, ss, se-ss)[0];
				selStart = selEnd = se = ss;
				midByte = false;
			}
			data = arrayPaste(data, ss, stuff)[0];
			selStart = selEnd = se = ss + stuff.length;
			midByte = false;
			guiChanged();
			fireHexDataChanged();
			fireHexSelectionChanged();
		}
	}
	
	public void replaceSelection(long l) {
		int ss = sanesel(Math.min(selStart, selEnd));
		int se = sanesel(Math.max(selStart, selEnd));
		if (le) {
			for (int j=ss; j<se; j++) {
				data[j] = (byte)(l & 0xFF);
				l >>>= 8;
			}
		} else {
			for (int j=se-1; j>=ss; j--) {
				data[j] = (byte)(l & 0xFF);
				l >>>= 8;
			}
		}
		repaint();
		fireHexDataChanged();
	}
	
	public boolean isEditable() {
		return !readOnly;
	}
	
	public void setEditable(boolean b) {
		readOnly = !b;
	}
	
	public void addHexDataChangeListener(HexDataChangeListener l) {
		myListeners.add(HexDataChangeListener.class, l);
	}
	
	public void removeHexDataChangeListener(HexDataChangeListener l) {
		myListeners.remove(HexDataChangeListener.class, l);
	}
	
	public HexDataChangeListener[] getHexDataChangeListeners() {
		return myListeners.getListeners(HexDataChangeListener.class);
	}
	
	protected void fireHexDataChanged() {
		HexDataChangeEvent ee = new HexDataChangeEvent(this, 1, "fireHexDataChanged", data);
		HexDataChangeListener[] l = myListeners.getListeners(HexDataChangeListener.class);
		for (int i=0; i<l.length; i++) l[i].hexDataChanged(ee);
	}
	
	public void addHexSelectionChangeListener(HexSelectionChangeListener l) {
		myListeners.add(HexSelectionChangeListener.class, l);
	}
	
	public void removeHexSelectionChangeListener(HexSelectionChangeListener l) {
		myListeners.remove(HexSelectionChangeListener.class, l);
	}
	
	public HexSelectionChangeListener[] getHexSelectionChangeListeners() {
		return myListeners.getListeners(HexSelectionChangeListener.class);
	}
	
	protected void fireHexSelectionChanged() {
		HexSelectionChangeEvent ee = new HexSelectionChangeEvent(this, 1, "fireHexSelectionChanged", selStart, selEnd);
		HexSelectionChangeListener[] l = myListeners.getListeners(HexSelectionChangeListener.class);
		for (int i=0; i<l.length; i++) l[i].hexSelectionChanged(ee);
	}
	
	public void addHexAccumulatorListener(HexAccumulatorListener l) {
		myListeners.add(HexAccumulatorListener.class, l);
	}
	
	public void removeHexAccumulatorListener(HexAccumulatorListener l) {
		myListeners.remove(HexAccumulatorListener.class, l);
	}
	
	public HexAccumulatorListener[] getHexAccumulatorListeners() {
		return myListeners.getListeners(HexAccumulatorListener.class);
	}
	
	protected void fireHexAccumulatorChanged() {
		HexAccumulatorEvent ee = new HexAccumulatorEvent(this, 1, "fireHexAccumulatorChanged", acc);
		HexAccumulatorListener[] l = myListeners.getListeners(HexAccumulatorListener.class);
		for (int i=0; i<l.length; i++) l[i].hexAccumulatorChanged(ee);
	}
	
	public void addHexDisplayChangeListener(HexDisplayChangeListener l) {
		myListeners.add(HexDisplayChangeListener.class, l);
	}
	
	public void removeHexDisplayChangeListener(HexDisplayChangeListener l) {
		myListeners.remove(HexDisplayChangeListener.class, l);
	}
	
	public HexDisplayChangeListener[] getHexDisplayChangeListeners() {
		return myListeners.getListeners(HexDisplayChangeListener.class);
	}
	
	protected void fireHexDisplayChanged() {
		HexDisplayChangeEvent ee = new HexDisplayChangeEvent(this, 1, "fireHexDisplayChanged");
		HexDisplayChangeListener[] l = myListeners.getListeners(HexDisplayChangeListener.class);
		for (int i=0; i<l.length; i++) l[i].hexDisplayChanged(ee);
	}
	
	public void addHexModeChangeListener(HexModeChangeListener l) {
		myListeners.add(HexModeChangeListener.class, l);
	}
	
	public void removeHexModeChangeListener(HexModeChangeListener l) {
		myListeners.remove(HexModeChangeListener.class, l);
	}
	
	public HexModeChangeListener[] getHexModeChangeListeners() {
		return myListeners.getListeners(HexModeChangeListener.class);
	}
	
	protected void fireHexModeChanged(int id) {
		HexModeChangeEvent ee = new HexModeChangeEvent(this, id, "fireHexModeChanged");
		HexModeChangeListener[] l = myListeners.getListeners(HexModeChangeListener.class);
		for (int i=0; i<l.length; i++) l[i].hexModeChanged(ee);
	}
	
	private static byte[][] arrayCut(byte[] arr, int offset, int bytesToCut) {
		byte[] stuff = new byte[bytesToCut];
		for (int s=offset, d=0; d<bytesToCut && s<arr.length; s++, d++) {
			stuff[d] = arr[s];
		}
		byte[] junk = new byte[Math.max(arr.length-bytesToCut, offset)];
		for (int s=0, d=0; d<offset && s<offset && d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		for (int s=offset+bytesToCut, d=offset; d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		return new byte[][]{junk,stuff};
	}
	
	private static byte[][] arrayCopy(byte[] arr, int offset, int bytesToCopy) {
		byte[] stuff = new byte[bytesToCopy];
		for (int s=offset, d=0; d<bytesToCopy && s<arr.length; s++, d++) {
			stuff[d] = arr[s];
		}
		return new byte[][]{arr,stuff};
	}
	
	private static byte[][] arrayPaste(byte[] arr, int offset, byte[] stuff) {
		byte[] junk = new byte[arr.length+stuff.length];
		for (int s=0, d=0; d<offset && s<offset && d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		for (int s=0, d=offset; s<stuff.length && d<junk.length; s++, d++) {
			junk[d] = stuff[s];
		}
		for (int s=offset, d=offset+stuff.length; d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		return new byte[][]{junk,stuff};
	}
	
	private void makePopupMenu() {
		JMenuItem mi;
		menu = new JPopupMenu();
		mi = new JMenuItem("Cut");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.cut();
			}
		});
		menu.add(mi);
		mi = new JMenuItem("Copy");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.copy();
			}
		});
		menu.add(mi);
		mi = new JMenuItem("Paste");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.paste();
			}
		});
		menu.add(mi);
		mi = new JMenuItem("Select All");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.selectAll();
			}
		});
		menu.add(mi);
		mi = new JMenuItem("Jump to Address...");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ss = sanesel(Math.min(selStart, selEnd));
				int se = sanesel(Math.max(selStart, selEnd));
				String r = (ss == se)?(decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())
						:((decAddr?Integer.toString(ss):Integer.toHexString(ss).toUpperCase())+":"+(decAddr?Integer.toString(se):Integer.toHexString(se).toUpperCase()));
				Object o = JOptionPane.showInputDialog(
						JHexEditor.this,
						"Enter address range:",
						"Jump to Address",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						r
				);
				if (o != null && o.toString().length() > 0) try {
					r = o.toString();
					String[] rr = r.split(":");
					if (rr.length > 1) {
						selStart = Integer.parseInt(rr[0],decAddr?10:16);
						selEnd = Integer.parseInt(rr[1],decAddr?10:16);
					} else {
						selStart = selEnd = Integer.parseInt(r,decAddr?10:16);
					}
					repaint();
					fireHexSelectionChanged();
				} catch (Exception ex) {}
			}
		});
		menu.add(mi);
		menu.addSeparator();
		
		JMenu modes = new JMenu("Mode");
		mi = new JMenuItem("Big Endian");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setLittleEndian(false);
			}
		});
		modes.add(mi);
		mi = new JMenuItem("Little Endian");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setLittleEndian(true);
			}
		});
		modes.add(mi);
		modes.addSeparator();
		mi = new JMenuItem("Insert Mode");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setOvertypeMode(false);
			}
		});
		modes.add(mi);
		mi = new JMenuItem("Overtype Mode");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setOvertypeMode(true);
			}
		});
		modes.add(mi);
		modes.addSeparator();
		mi = new JMenuItem("Hex Addresses");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setDecimalAddresses(false);
			}
		});
		modes.add(mi);
		mi = new JMenuItem("Decimal Addresses");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setDecimalAddresses(true);
			}
		});
		modes.add(mi);
		menu.add(modes);
		
		JMenu sizes = new JMenu("Bytes Per Row");
		for (int i=4; i<=48; i+=4) {
			mi = new JMenuItem(Integer.toString(i));
			mi.addActionListener(new BPRActionListener(i));
			sizes.add(mi);
		}
		menu.add(sizes);
		
		JMenu fonts = new JMenu("Font");
		mi = new JMenuItem("9");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(9.0f));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("10");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(10.0f));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("12");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(12.0f));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("14");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(14.0f));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("16");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(16.0f));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("18");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(JHexEditor.this.getFont().deriveFont(18.0f));
			}
		});
		fonts.add(mi);
		fonts.addSeparator();
		mi = new JMenuItem("Monospaced");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Monospaced", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Andale Mono");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Andale Mono", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("C Colon Backslash");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("C Colon Backslash", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Courier");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Courier", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Courier New");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Courier New", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Lucida Console");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Lucida Console", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Lucida Sans Typewriter");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		mi = new JMenuItem("Monaco");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setFont(new Font("Monaco", Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		fonts.addSeparator();
		mi = new JMenuItem("Other...");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object o = JOptionPane.showInputDialog(
						JHexEditor.this,
						"Enter font name:",
						"Other Font",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						JHexEditor.this.getFont().getName()
				);
				if (o != null && o.toString().length() > 0) JHexEditor.this.setFont(new Font(o.toString(), Font.PLAIN, JHexEditor.this.getFont().getSize()));
			}
		});
		fonts.add(mi);
		menu.add(fonts);
		
		JMenu colors = new JMenu("Color Scheme");
		for (int i=0; i<COLOR_SCHEME_COUNT; i++) {
			mi = new JMenuItem(COLOR_SCHEME_NAMES[i]);
			mi.addActionListener(new ColorSchemeActionListener(COLOR_SCHEMES[i]));
			colors.add(mi);
		}
		menu.add(colors);
		
		JMenu encs = new JMenu("Text Encoding");
		mi = new JMenuItem("ISO Latin 1");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-1");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("ISO Latin 2");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-2");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("ISO Latin 4");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-4");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("ISO Latin 5");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-9");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("ISO Latin 7");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-13");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("ISO Latin 9");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-15");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("Windows Latin (CP1252)");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP1252");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("Windows Eastern European (CP1250)");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP1250");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MS-DOS Latin / CP437");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP437");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MS-DOS Latin / CP850");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP850");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MS-DOS Latin / CP852");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP852");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("MacRoman");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("MACROMAN");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MacIcelandic");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("MACICELANDIC");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("ISO Greek");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-7");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("Windows Greek (CP1253)");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP1253");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MacGreek");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("MACGREEK");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("ISO Cyrillic");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("ISO-8859-5");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("Windows Cyrillic (CP1251)");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("CP1251");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("KOI8-R");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("KOI8-R");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("KOI8-U");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("KOI8-U");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("MacCyrillic");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("MacCyrillic");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("US ASCII");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("US-ASCII");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("US ASCII (Ignoring High Bit)");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("XX-HIGHASCII");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("Apple II Primary");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("XX-APPLEII-PRIMARY");
			}
		});
		encs.add(mi);
		mi = new JMenuItem("Apple II Alternate");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JHexEditor.this.setTextEncoding("XX-APPLEII-ALTERNATE");
			}
		});
		encs.add(mi);
		encs.addSeparator();
		mi = new JMenuItem("Other...");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object o = JOptionPane.showInputDialog(
						JHexEditor.this,
						"Enter text encoding name:",
						"Other Text Encoding",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						JHexEditor.this.getTextEncoding()
				);
				if (o != null && o.toString().length() > 0) JHexEditor.this.setTextEncoding(o.toString());
			}
		});
		encs.add(mi);
		menu.add(encs);
	}
	
	private class BPRActionListener implements ActionListener {
		private int i;
		public BPRActionListener(int i) {
			this.i = i;
		}
		public void actionPerformed(ActionEvent ae) {
			setWidth(i);
		}
	}
	
	private class ColorSchemeActionListener implements ActionListener {
		private Color[] cs;
		public ColorSchemeActionListener(Color[] cs) {
			this.cs = cs;
		}
		public void actionPerformed(ActionEvent ae) {
			setColorScheme(cs);
		}
	}
}
