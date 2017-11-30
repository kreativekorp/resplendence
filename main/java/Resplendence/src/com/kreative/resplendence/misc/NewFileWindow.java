package com.kreative.resplendence.misc;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.kreative.ksfl.KSFLConstants;
import com.kreative.resplendence.*;
import com.kreative.resplendence.filecodec.*;

public class NewFileWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private File f;
	private FileCodec[] codecs;
	private JComboBox codecMenu;
	private String[] templates;
	private JComboBox dataTmplMenu;
	private JComboBox rsrcTmplMenu;
	
	public NewFileWindow(File file, Window win) {
		super("New File Options");
		this.f = file;
		ResplMain.registerWindow(this);
		
		codecs = ResplMain.getFileCodecList();
		DefaultComboBoxModel codecList = new DefaultComboBoxModel();
		for (int i=0; i<codecs.length; i++) codecList.addElement(codecs[i].name());
		codecMenu = new JComboBox(codecList);
		codecMenu.setMaximumRowCount(32);
		codecMenu.setEditable(false);
		codecMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		codecMenu.setSelectedItem("No Encoding");
		
		String[] tmp = ResplRsrcs.getAppDFFResourceProvider().getNames(KSFLConstants.ForkTmpl);
		Arrays.sort(tmp);
		templates = new String[tmp.length+1];
		templates[0] = "Empty";
		for (int i=0; i<tmp.length; i++) templates[i+1] = tmp[i];
		dataTmplMenu = new JComboBox(templates);
		dataTmplMenu.setMaximumRowCount(32);
		dataTmplMenu.setEditable(false);
		dataTmplMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		dataTmplMenu.setSelectedItem("Empty");
		rsrcTmplMenu = new JComboBox(templates);
		rsrcTmplMenu.setMaximumRowCount(32);
		rsrcTmplMenu.setEditable(false);
		rsrcTmplMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		rsrcTmplMenu.setSelectedItem("Empty");
		
		JPanel menus = new JPanel();
		menus.setLayout(new BoxLayout(menus, BoxLayout.Y_AXIS));
		menus.add(makeLine("Encoding:", 120, codecMenu));
		menus.add(makeLine("Data Fork:", 120, dataTmplMenu));
		menus.add(makeLine("Resource Fork:", 120, rsrcTmplMenu));
		
		JPanel buttons = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					f.createNewFile();
					RWCFile rwc = RWCFile.createTempRWCFile(f);
					if (dataTmplMenu.getSelectedIndex() > 0) {
						byte[] b = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.ForkTmpl, dataTmplMenu.getSelectedItem().toString());
						rwc.getDataFork().createNewFile();
						RandomAccessFile r = new RandomAccessFile(rwc.getDataFork(), "rwd");
						r.write(b);
						r.close();
					}
					if (rsrcTmplMenu.getSelectedIndex() > 0) {
						byte[] b = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.ForkTmpl, rsrcTmplMenu.getSelectedItem().toString());
						rwc.getResourceFork().createNewFile();
						RandomAccessFile r = new RandomAccessFile(rwc.getResourceFork(), "rwd");
						r.write(b);
						r.close();
					}
					codecs[codecMenu.getSelectedIndex()].encode(f, rwc);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "Could not create a new file.", "New File", JOptionPane.ERROR_MESSAGE);
					ioe.printStackTrace();
				}
				dispose();
			}
		});
		buttons.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		buttons.add(cancelButton);
		
		JPanel main = new JPanel(new BorderLayout(12,12));
		main.add(menus, BorderLayout.CENTER);
		main.add(buttons, BorderLayout.PAGE_END);
		main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		this.setContentPane(main);
		this.getRootPane().setDefaultButton(okButton);
		ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(win);
		this.setVisible(true);
	}
	
	private static JLabel makeFixedLabel(String s, int width) {
		JLabel l = new JLabel(s);
		l.setSize(width, l.getHeight());
		l.setMinimumSize(new Dimension(width, l.getMinimumSize().height));
		l.setPreferredSize(new Dimension(width, l.getPreferredSize().height));
		l.setMaximumSize(new Dimension(width, l.getMaximumSize().height));
		l.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		l.setHorizontalAlignment(JLabel.RIGHT);
		l.setHorizontalTextPosition(JLabel.RIGHT);
		return l;
	}
	
	private static JPanel makeLine(String s, int width, Component c) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(makeFixedLabel(s, width));
		p.add(Box.createHorizontalStrut(8));
		p.add(c);
		return p;
	}
}
