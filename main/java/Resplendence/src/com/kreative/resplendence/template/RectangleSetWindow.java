package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import com.kreative.resplendence.*;

public class RectangleSetWindow extends JFrame implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1;
	
	private Image bg=null;
	public int x1=0, y1=0, x2=0, y2=0;
	
	public RectangleSetWindow() {
		super("Rectangle");
		ResplMain.registerWindow(this);
		Dimension dim = getToolkit().getScreenSize();
		Rectangle scr = new Rectangle(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
		try {
			bg = new Robot().createScreenCapture(scr);
		} catch (Exception e) {}
		this.setLayout(new BorderLayout());
		this.add(new Backdrop(), BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setUndecorated(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBounds(scr);
		this.setVisible(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	private class Backdrop extends JComponent {
		private static final long serialVersionUID = 1;
		protected void paintComponent(Graphics g) {
			g.drawImage(bg, 0, 0, this);
		}
	}

	public void mouseClicked(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		dispose();
	}

	public void mouseDragged(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		Graphics g = getGraphics();
		g.drawImage(bg, 0, 0, this);
		g.setColor(Color.blue);
		g.drawRect(x1, y1, x2-x1, y2-y1);
	}

	public void mouseMoved(MouseEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
}
