package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.kreative.awt.*;
import com.kreative.swing.*;
import com.kreative.resplendence.*;

public class RectangleEditWindow extends JFrame {
	private static final long serialVersionUID = 1;
	
	public RectangleEditWindow(Rectangle r) {
		super("Rectangle");
		ResplMain.registerWindow(this);
		this.setBackground(new Color(0x10FFFFFF, ResplMain.RUNNING_ON_MAC_OS));
		JComponent v = ResplMain.RUNNING_ON_MAC_OS ?
				new MyJPanel(new FractionalSizeGridLayout(3,3)) :
					new MyTransparentComponent(this, new FractionalSizeGridLayout(3,3));
		GrabHandle nw = new GrabHandle(GrabHandle.NORTHWEST, 9);
		GrabHandle n  = new GrabHandle(GrabHandle.NORTH, 9);
		GrabHandle ne = new GrabHandle(GrabHandle.NORTHEAST, 9);
		GrabHandle w  = new GrabHandle(GrabHandle.WEST, 9);
		GrabHandle c  = new GrabHandle(GrabHandle.CENTER, 9);
		GrabHandle e  = new GrabHandle(GrabHandle.EAST, 9);
		GrabHandle sw = new GrabHandle(GrabHandle.SOUTHWEST, 9);
		GrabHandle s  = new GrabHandle(GrabHandle.SOUTH, 9);
		GrabHandle se = new GrabHandle(GrabHandle.SOUTHEAST, 9);
		v.add(nw); v.add(n ); v.add(ne);
		v.add(w ); v.add(c ); v.add(e );
		v.add(sw); v.add(s ); v.add(se);
		new GrabHandleHookup(this, nw, GrabHandleHookup.NORTHWEST);
		new GrabHandleHookup(this, n , GrabHandleHookup.NORTH);
		new GrabHandleHookup(this, ne, GrabHandleHookup.NORTHEAST);
		new GrabHandleHookup(this, w , GrabHandleHookup.WEST);
		new GrabHandleHookup(this, c , GrabHandleHookup.CENTER);
		new GrabHandleHookup(this, e , GrabHandleHookup.EAST);
		new GrabHandleHookup(this, sw, GrabHandleHookup.SOUTHWEST);
		new GrabHandleHookup(this, s , GrabHandleHookup.SOUTH);
		new GrabHandleHookup(this, se, GrabHandleHookup.SOUTHEAST);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setContentPane(v);
		this.setUndecorated(true);
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_ESCAPE:
					dispose();
					break;
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		this.setBounds(r);
		this.setVisible(true);
	}
	
	private static class MyJPanel extends JPanel {
		private static final long serialVersionUID = 2; //just to mix it up
		//public MyJPanel() { super(); }
		public MyJPanel(LayoutManager l) { super(l); }
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int w = getWidth();
			int h = getHeight();
			g.setColor(Color.black);
			g.drawRect(0, 0, w-1, h-1);
			g.setColor(Color.magenta);
			g.drawRect(1, 1, w-3, h-3);
		}
	}
	
	private static class MyTransparentComponent extends TransparentComponent {
		private static final long serialVersionUID = 2; //just to mix it up
		public MyTransparentComponent(Window f) { super(f); }
		public MyTransparentComponent(Window f, LayoutManager l) { super(f,l); }
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int w = getWidth();
			int h = getHeight();
			g.setColor(Color.black);
			g.drawRect(0, 0, w-1, h-1);
			g.setColor(Color.magenta);
			g.drawRect(1, 1, w-3, h-3);
		}
	}
}
