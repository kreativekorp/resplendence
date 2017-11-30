package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import javax.swing.*;
import com.kreative.resplendence.*;

public class OpenSpecialWindow extends JFrame {
	private static final long serialVersionUID = 1l;
	
	private static OpenSpecialWindow instance;
	public static OpenSpecialWindow getInstance() {
		if (instance == null) instance = new OpenSpecialWindow();
		return instance;
	}
	public static void showInstance() {
		if (instance == null) instance = new OpenSpecialWindow();
		instance.setVisible(true);
	}
	
	private DefaultListModel model;
	private JList list;
	public OpenSpecialWindow() {
		super("Modify Special Menu");
		ResplMain.registerWindow(this);
		JPanel main = new JPanel(new BorderLayout(12,4));
		model = new DefaultListModel();
		list = new JList(model);
		JScrollPane spane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		main.add(spane, BorderLayout.CENTER);
		java.util.List<File> ss = ResplPrefs.getFiles("Special Files");
		if (ss != null) for (File f : ss) model.addElement(f);
		JPanel sbuttons = new JPanel(new GridLayout(5,1,4,4));
		JButton addButton = new JButton("Add File...");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(OpenSpecialWindow.this, "Add File", FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getFile() != null) {
					String s = System.getProperty("file.separator");
					String path = fd.getDirectory();
					if (!path.endsWith(s)) path += s;
					path += fd.getFile();
					model.addElement(new File(path));
				}
			}
		});
		JButton addFButton = new JButton("Add Folder...");
		addFButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setMultiSelectionEnabled(false);
				if (fc.showOpenDialog(OpenSpecialWindow.this) == JFileChooser.APPROVE_OPTION) {
					model.addElement(fc.getSelectedFile());
				}
			}
		});
		JButton remButton = new JButton("Remove");
		remButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() >= 0) {
					model.remove(list.getSelectedIndex());
				}
			}
		});
		JButton upButton = new JButton("Move Up");
		JButton downButton = new JButton("Move Down");
		sbuttons.add(addButton);
		sbuttons.add(addFButton);
		sbuttons.add(remButton);
		sbuttons.add(upButton);
		sbuttons.add(downButton);
		JPanel sbw = new JPanel(new BorderLayout());
		sbw.add(sbuttons, BorderLayout.PAGE_START);
		main.add(sbw, BorderLayout.LINE_END);
		JPanel bbuttons = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<File> ss = new Vector<File>();
				for (int i=0; i<model.size(); i++) ss.add((File)model.getElementAt(i));
				ResplPrefs.setFiles("Special Files", ss);
				ResplMain.updateFileMenu();
				OpenSpecialWindow.this.setVisible(false);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenSpecialWindow.this.setVisible(false);
			}
		});
		bbuttons.add(okButton);
		bbuttons.add(cancelButton);
		main.add(bbuttons, BorderLayout.PAGE_END);
		main.setBorder(BorderFactory.createEmptyBorder(14, 20, 8, 20));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setContentPane(main);
		getRootPane().setDefaultButton(okButton);
		ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
		pack();
		setSize(500,getHeight());
		setLocationRelativeTo(null);
	}
}
