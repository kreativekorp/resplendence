package com.kreative.resplendence.menus;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import com.kreative.dff.DFFResource;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;

public class MiniScreenMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1L;
	
	public MiniScreenMenu() {
		super("Miniscreen");
		setMnemonic(KeyEvent.VK_M);
		JMenuItem mi;
		for (MiniScreenDimension d : getScreens()) {
			int x = d.width;
			int y = d.height;
			for (int z=Math.min(x,y); z > 1; z--) {
				if (x % z == 0 && y % z == 0) {
					x /= z;
					y /= z;
				}
			}
			String s = d.width + "x" + d.height + " [" + x + ":" + y + "]";
			if (d.name != null) s += " - " + d.name;
			mi = new JMenuItem(s);
			mi.addActionListener(new MiniscreenMenuAction(d));
			if (x == 16 && y == 9) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.magenta.darker()));
			} else if (x == 5 && y == 3) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.orange.darker()));
			} else if (x == 8 && y == 5) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.green));
			} else if (x == 3 && y == 2) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.yellow));
			} else if (x == 4 && y == 3) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.red));
			} else if (x == 5 && y == 4) {
				mi.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.blue));
			} else {
				mi.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
			}
			add(mi);
		}
		addSeparator();
		mi = new JMenuItem("Other...");
		mi.setMnemonic(KeyEvent.VK_O);
		mi.setAccelerator(KeyStroke.getKeyStroke('M', META_ALT_SHIFT_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.GET_MINISCREEN_SIZE,"Other...",null));
				if (o instanceof Dimension) {
					Dimension d = (Dimension)o;
					o = d.width+"x"+d.height;
				}
				o = JOptionPane.showInputDialog(
						null,
						"Enter screen size:",
						"Other MiniScreen Size",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						o
				);
				if (o != null && o.toString().length() > 0) {
					try {
						String[] ss = o.toString().replace('x', ',').split(",");
						Dimension d = new Dimension(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
						ResplMain.setFrontmostResplendenceListener(l);
						ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_MINISCREEN_SIZE,"Other...",d));
					} catch (Exception e) {}
				}
			}
		});
		add(mi);
	}
	
	private static Vector<MiniScreenDimension> screens = null;
	private static Vector<MiniScreenDimension> getScreens() {
		if (screens != null) return screens;
		screens = new Vector<MiniScreenDimension>();
		DFFResource o = ResplRsrcs.getAppDFFResourceProvider().get(KSFLConstants.MiniScn$, 0);
		int p = 0;
		while (p+5 < o.data.length) {
			int w = KSFLUtilities.getShort(o.data, p);
			int h = KSFLUtilities.getShort(o.data, p+2);
			String n = KSFLUtilities.getPString(o.data, p+4);
			if (n == null || n.length() < 1) {
				screens.add(new MiniScreenDimension(w, h));
			} else {
				screens.add(new MiniScreenDimension(w, h, n));
			}
			p += 5+(o.data[p+4] & 0xFF);
			if ((p & 1) == 1) p++;
		}
		return screens;
	}

	private static class MiniscreenMenuAction implements ActionListener {
		private Dimension d;
		public MiniscreenMenuAction(Dimension d) {
			this.d = d;
		}
		public void actionPerformed(ActionEvent ae) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_MINISCREEN_SIZE,Integer.toString(d.width)+"x"+Integer.toString(d.height),d));
		}
	}
	
	private static class MiniScreenDimension extends Dimension {
		private static final long serialVersionUID = 1L;
		public String name;
		public MiniScreenDimension(int w, int h) {
			super(w,h);
			this.name = null;
		}
		public MiniScreenDimension(int w, int h, String name) {
			super(w,h);
			this.name = name;
		}
	}
}
