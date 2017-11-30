package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import com.kreative.resplendence.*;

public class AboutBox extends JFrame {
	private static final long serialVersionUID = 1l;
	
	private static AboutBox instance = null;
	public static AboutBox getInstance() {
		if (instance == null) instance = new AboutBox();
		return instance;
	}
	public static void showInstance() {
		if (instance == null) instance = new AboutBox();
		instance.setVisible(true);
	}
	public static AboutBox getInstance(ActionEvent e) {
		if (instance == null) instance = new AboutBox(e);
		return instance;
	}
	public static void showInstance(ActionEvent e) {
		if (instance == null) instance = new AboutBox(e);
		instance.setVisible(true);
	}
	public static AboutBox getInstance(InputEvent e) {
		if (instance == null) instance = new AboutBox(e);
		return instance;
	}
	public static void showInstance(InputEvent e) {
		if (instance == null) instance = new AboutBox(e);
		instance.setVisible(true);
	}
	
	private Clip pig = null;
	private static boolean pigMode = false;
	private static PigThread pigThread = null;
	
	public AboutBox() {
		this(0);
	}
	
	public AboutBox(ActionEvent e) {
		this(
				((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) > 0 && (e.getModifiers() & ActionEvent.ALT_MASK) > 0)?(((e.getModifiers() & ActionEvent.SHIFT_MASK) > 0)?(pigMode?3:2):1):0
		);
	}
	
	public AboutBox(InputEvent e) {
		this(
				((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) > 0 && (e.getModifiersEx() & InputEvent.ALT_DOWN_MASK) > 0)?(((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) > 0)?(pigMode?3:2):1):0
		);
	}
	
	public AboutBox(int i) {
		super("About Resplendence");
		ResplMain.registerWindow(this);
		if (ResplMain.RUNNING_ON_MAC_OS) this.setBackground(new Color(0,true));
		Image img;
		switch (i) {
		case 3: img = this.getToolkit().createImage(this.getClass().getResource("PigModeOff.png")); break;
		case 2: img = this.getToolkit().createImage(this.getClass().getResource("PigMode.png")); break;
		case 1: img = this.getToolkit().createImage(this.getClass().getResource("AboutAlt.png")); break;
		default: img = this.getToolkit().createImage(this.getClass().getResource(ResplMain.RUNNING_ON_MAC_OS ? "AboutAlpha.png" : "About.png")); break;
		}
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
		this.setContentPane(p);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final int initializationInt = i;
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (pig != null) pig.close();
				if (initializationInt == 2 && e.getX() > 157 && e.getY() > 69 && e.getX() < 216 && e.getY() < 90) {
					pigMode = true;
					if (pigThread == null) {
						pigThread = new PigThread();
						pigThread.start();
					}
				}
				else if (initializationInt == 3 && e.getX() > 157 && e.getY() > 69 && e.getX() < 216 && e.getY() < 90) {
					pigMode = false;
					if (pigThread != null) {
						pigThread.exit();
						pigThread = null;
					}
				}
				dispose();
			}
			
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		if (i == 2 || i == 3) {
			try {
				AudioInputStream st = AudioSystem.getAudioInputStream(this.getClass().getResource("PigMode.wav"));
				AudioFormat fm = st.getFormat();
				DataLine.Info inf = new DataLine.Info(Clip.class, fm, ((int)st.getFrameLength()*fm.getFrameSize()));
				pig = (Clip)AudioSystem.getLine(inf);
				pig.open(st);
				pig.start();
			} catch (Exception e) {}
		}
	}
	
	public void dispose() {
		instance = null;
		super.dispose();
	}
	
	private static class PigThread extends Thread {
		public boolean running = false;
		
		public void run() {
			int p = ResplPrefs.getInt("Oink");
			if (p < 1) p = 5;
			running = true;
			while (running) {
				try {
					Thread.sleep(p);
				} catch (Exception e) {}
				System.gc();
			}
		}
		
		public void exit() {
			running = false;
		}
	}
}
