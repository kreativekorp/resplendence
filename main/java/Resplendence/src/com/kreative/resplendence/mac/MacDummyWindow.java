package com.kreative.resplendence.mac;

import java.awt.*;
import javax.swing.*;

public class MacDummyWindow extends JFrame {
	public static final long serialVersionUID = 1L;
	
	public MacDummyWindow() {
		setUndecorated(true);
		setResizable(false);
		setMinimumSize(new Dimension(0,0));
		setPreferredSize(new Dimension(0,0));
		setMaximumSize(new Dimension(0,0));
		setSize(new Dimension(0,0));
		setLocation(-1000000, -1000000);
		setVisible(true);
	}
	
	public MacDummyWindow(JMenuBar mb) {
		setJMenuBar(mb);
		setUndecorated(true);
		setResizable(false);
		setMinimumSize(new Dimension(0,0));
		setPreferredSize(new Dimension(0,0));
		setMaximumSize(new Dimension(0,0));
		setSize(new Dimension(0,0));
		setLocation(-1000000, -1000000);
		setVisible(true);
	}
}
