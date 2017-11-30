package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.kreative.resplendence.ResplUtils;

public class IDConflictDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final int SKIP = 0;
	public static final int RENUMBER = 1;
	public static final int OVERWRITE = 2;
	public static final int CANCEL = 3;
	private int result;
	
	public static int showIDConflictDialog() {
		IDConflictDialog d = new IDConflictDialog();
		d.setVisible(true);
		return d.result;
	}
	
	private IDConflictDialog() {
		super((Frame)null, "ID Conflict", true);
		JPanel main = new JPanel(new BorderLayout(20,20));
		JLabel msg = new JLabel("<html>Some of the items to be added have ID numbers that conflict with existing items. Would you like to overwrite the existing items, renumber the conflicting items, or skip the conflicting items?</html>");
		msg.setVerticalAlignment(JLabel.TOP);
		msg.setVerticalTextPosition(JLabel.TOP);
		main.add(msg, BorderLayout.CENTER);
		main.add(new JLabel(new JOptionPane("", JOptionPane.WARNING_MESSAGE).getIcon()), BorderLayout.LINE_START);
		JPanel buttons = new JPanel();
		JButton btn1 = new JButton("Overwrite");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = OVERWRITE;
				dispose();
			}
		});
		buttons.add(btn1);
		JButton btn2 = new JButton("Renumber");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = RENUMBER;
				dispose();
			}
		});
		buttons.add(btn2);
		JButton btn3 = new JButton("Skip");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = SKIP;
				dispose();
			}
		});
		buttons.add(btn3);
		JButton btn4 = new JButton("Cancel");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = CANCEL;
				dispose();
			}
		});
		buttons.add(btn4);
		main.add(buttons, BorderLayout.PAGE_END);
		main.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
		setContentPane(main);
		getRootPane().setDefaultButton(btn2);
		ResplUtils.setCancelButton(getRootPane(), btn4);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(480,180));
		setLocationRelativeTo(null);
	}
}
