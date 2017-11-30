package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class ScreenRuler implements AccessoryWindow {
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "Screen Ruler";
	}

	public void open(int i) {
		new WScreenRuler();
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WScreenRuler extends JFrame {
		private static final long serialVersionUID = 1;
		
		private JPanel v; // TransparentComponent v;
		private RulerComponent r;
		private int u = 0;
		
		public WScreenRuler() {
			super("Screen Ruler");
			ResplMain.registerWindow(this);
			//this.setUndecorated(true);
			this.setBackground(new Color(255,255,200,ResplMain.RUNNING_ON_MAC_OS?128:255));
			v = new JPanel(new BorderLayout()); // new TransparentComponent(this, new BorderLayout());
			//v.setOpaque(true); v.setBackground(new Color(255,255,200,0));
			r = new RulerComponent();
			v.add(r, BorderLayout.CENTER);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setContentPane(v);
			this.addComponentListener(new ComponentListener() {
				public void componentHidden(ComponentEvent arg0) {}
				public void componentMoved(ComponentEvent arg0) {}
				public void componentResized(ComponentEvent arg0) {
					int sw = WScreenRuler.this.getWidth();
					int sh = WScreenRuler.this.getHeight();
					int wd = sw - r.getWidth();
					int hd = sh - r.getHeight();
					if (sw-wd < 30) {
						if (sw-wd != 24) WScreenRuler.this.setSize(24+wd, sh);
					} else if (sw-wd < 50) {
						if (sw-wd != 48) WScreenRuler.this.setSize(48+wd, sh);
					} else if (sh-hd < 30) {
						if (sh-hd != 16) WScreenRuler.this.setSize(sw, 16+hd);
					} else if (sh-hd < 50) {
						if (sh-hd != 32) WScreenRuler.this.setSize(sw, 32+hd);
					}
					r.setShowAuto();
				}
				public void componentShown(ComponentEvent arg0) {
					r.setShowAuto();
				}
			});
			this.setSize(384, 50);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			r.setFont(r.getFont().deriveFont(8.0f));
			r.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					u = (u+1)%7;
					switch (u) {
					case 0: r.setParameters(PIXELS); break;
					case 1: r.setParameters(INCHES_72DPI); break;
					case 2: r.setParameters(INCHES_96DPI); break;
					case 3: r.setParameters(INCHES_100DPI); break;
					case 4: r.setParameters(CENTIMETERS_28DPCM); break;
					case 5: r.setParameters(CENTIMETERS_38DPCM); break;
					case 6: r.setParameters(CENTIMETERS_40DPCM); break;
					}
				}
			});
		}
	}
	
	private static class RulerComponent extends JComponent implements MouseMotionListener, MouseListener {
		private static final long serialVersionUID = 1;
		
		private RulerParameters p;
		private boolean showTop, showLeft, showBottom, showRight;
		
		private static String myDoubleToString(double d) {
			if (Math.floor(d) == d) {
				return Integer.toString((int)d);
			} else {
				String s = Double.toString(d);
				if (s.contains(".")) {
					s += "000";
					return s.substring(0, s.indexOf(".")+4);
				} else {
					return s;
				}
			}
		}
		
		public RulerComponent() {
			p = new RulerParameters();
			showTop = false;
			showLeft = false;
			showBottom = true;
			showRight = false;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
		
		/*
		public RulerComponent(RulerParameters p) {
			this.p = p;
			showTop = false;
			showLeft = false;
			showBottom = true;
			showRight = false;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
		*/
		
		private void drawRuler(Graphics g, int x1, int x2, int y, boolean vert, boolean tl) {
			/*
			 * Ported from RealBasic source code from Kreative PowerWidgets.
			 * I actually have no idea what this code is doing.
			 * Hahahahaha bad programming practices...
			 */
			int m,x,l;
			String d;
			if (p.tick > 0) {
				m = (int)Math.ceil(((x2-x1)*p.tick)/p.ppu);
				for (int i=0; i<=m; i++) {
					x = (int)Math.ceil((i*p.ppu)/p.tick);
					l = 0;
					for (int j=p.maxl; j>=1; j--) {
						if ((i % Math.pow(p.maxlbase, j-1)) == 0) {
							l = j*p.maxlmul-1;
							break;
						}
					}
					if (vert) {
						if (tl) {
							g.drawLine(y, x1+x, y+l, x1+x);
						} else {
							g.drawLine(y-l, x1+x, y, x1+x);
						}
					} else {
						if (tl) {
							g.drawLine(x1+x, y, x1+x, y+l);
						} else {
							g.drawLine(x1+x, y-l, x1+x, y);
						}
					}
				}
			}
			if (p.measurement > 0) {
				g.setFont(getFont());
				FontMetrics fm = g.getFontMetrics();
				m = (int)Math.ceil((x2-x1)/(p.measurement*p.ppu));
				for (int i=0; i<=m; i++) {
					x = (int)Math.ceil(i*p.ppu*p.measurement);
					d = Integer.toString(i*p.measurement);
					if (vert) {
						if (tl) {
							g.drawString(d, y+p.maxl*p.maxlmul+1, x1+x+1);
						} else {
							g.drawString(d, y-p.maxl*p.maxlmul-fm.stringWidth(d)-1, x1+x+1);
						}
					} else {
						if (tl) {
							g.drawString(d, x1+x-fm.stringWidth(d)+2, y+p.maxl*p.maxlmul+fm.getAscent());
						} else {
							g.drawString(d, x1+x-fm.stringWidth(d)+2, y-p.maxl*p.maxlmul-fm.getDescent());
						}
					}
				}
			}
		}
		
		protected void paintComponent(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			g.clearRect(0, 0, w, h);
			if (isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, w, h);
			}
			g.setColor(getForeground());
			if (showTop) drawRuler(g, 0, w, 0, false, true);
			if (showLeft) drawRuler(g, 0, h, 0, true, true);
			if (showBottom) drawRuler(g, 0, w, h-1, false, false);
			if (showRight) drawRuler(g, 0, h, w-1, true, false);
		}

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {
			mouseMoved(arg0);
		}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {
			repaint();
		}

		public void mouseDragged(MouseEvent arg0) {
			mouseMoved(arg0);
		}
		public void mouseMoved(MouseEvent arg0) {
			String s = "";
			if (showTop || showBottom) s += ","+myDoubleToString(arg0.getX()/p.ppu);
			if (showLeft || showRight) s += ","+myDoubleToString(arg0.getY()/p.ppu);
			if (s.length() > 1) {
				s = s.substring(1);
				Graphics g = getGraphics();
				g.setFont(getFont());
				FontMetrics fm = g.getFontMetrics();
				int bx = getWidth()/2;
				int by = getHeight()/2;
				int ml = fm.stringWidth(s);
				g.setColor(Color.yellow);
				g.fillRect(bx-ml/2-6, by-fm.getHeight()/2-1, ml+12, fm.getHeight()+2);
				g.setColor(Color.black);
				g.drawString(s, bx-ml/2, by-fm.getHeight()/2+fm.getAscent());
			}
		}
		
		/*
		public RulerParameters getParameters() {
			return p;
		}
		*/
		public void setParameters(RulerParameters parameters) {
			this.p = parameters;
			repaint();
		}
		public void setShowAuto() {
			int w = getWidth(); int h = getHeight();
			showBottom = (w>50);
			showTop = showBottom && (h>30);
			showRight = (h>50);
			showLeft = showRight && (w>30);
			repaint();
		}
		/*
		public boolean isShowBottom() {
			return showBottom;
		}
		public void setShowBottom(boolean showBottom) {
			this.showBottom = showBottom;
			repaint();
		}
		public boolean isShowLeft() {
			return showLeft;
		}
		public void setShowLeft(boolean showLeft) {
			this.showLeft = showLeft;
			repaint();
		}
		public boolean isShowRight() {
			return showRight;
		}
		public void setShowRight(boolean showRight) {
			this.showRight = showRight;
			repaint();
		}
		public boolean isShowTop() {
			return showTop;
		}
		public void setShowTop(boolean showTop) {
			this.showTop = showTop;
			repaint();
		}
		*/
	}
	
	public static final RulerParameters PIXELS = new RulerParameters(5, 2, 1, 32, 1.0, 0.5);
	public static final RulerParameters INCHES_72DPI = new RulerParameters(6, 2, 1, 1, 72, 32);
	public static final RulerParameters INCHES_96DPI = new RulerParameters(6, 2, 1, 1, 96, 32);
	public static final RulerParameters INCHES_100DPI = new RulerParameters(6, 2, 1, 1, 100, 32);
	public static final RulerParameters CENTIMETERS_28DPCM = new RulerParameters(2, 10, 3, 1, 28, 10);
	public static final RulerParameters CENTIMETERS_38DPCM = new RulerParameters(2, 10, 3, 1, 38, 10);
	public static final RulerParameters CENTIMETERS_40DPCM = new RulerParameters(2, 10, 3, 1, 40, 10);
	
	private static class RulerParameters {
		public int maxl, maxlbase, maxlmul;
		public int measurement;
		public double ppu, tick;
		
		public RulerParameters() {
			this(5, 2, 1, 32, 1.0, 0.5);
		}
		
		/*
		public RulerParameters(double dpi) {
			this(6, 2, 1, 1, dpi, 32);
		}
		
		public RulerParameters(double dpu, boolean metric) {
			this(metric?2:6, metric?10:2, metric?3:1, 1, dpu, metric?10:32);
		}
		*/
		
		public RulerParameters(int maxl, int maxlbase, int maxlmul, int measurement, double ppu, double tick) {
			this.maxl = maxl;
			this.maxlbase = maxlbase;
			this.maxlmul = maxlmul;
			this.measurement = measurement;
			this.ppu = ppu;
			this.tick = tick;
		}
	}
}
