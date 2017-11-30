package com.kreative.resplendence.menus;

import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class BytesPerRowMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1L;
	
	public BytesPerRowMenu() {
		super("Bytes Per Row");
		setMnemonic(KeyEvent.VK_B);
		for (int i=4; i<=48; i+=4) {
			JMenuItem mi = new JMenuItem(Integer.toString(i));
			mi.addActionListener(new BPRMenuAction(i));
			add(mi);
		}
	}

	private static class BPRMenuAction implements ActionListener {
		private int bpr;
		public BPRMenuAction(int bpr) {
			this.bpr = bpr;
		}
		public void actionPerformed(ActionEvent ae) {
			ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(ae.getSource(),ResplendenceEvent.SET_BYTES_PER_ROW,Integer.toString(bpr),bpr));
		}
	}
}
