package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.kreative.resplendence.*;
import com.kreative.resplendence.editors.*;

public class OpenWithEditorWindow extends ResplendenceEditorWindow {
	private static final long serialVersionUID = 1L;
	private JList list;
	public OpenWithEditorWindow(ResplendenceObject ro) {
		super(ro, false);
		setTitle("Open with Editor");
		register();
		
		JPanel main = new JPanel(new BorderLayout(12,12));
		main.add(new JLabel("Select an editor to open \""+ro.getTitleForIcons()+"\":"), BorderLayout.PAGE_START);
		list = new JList(ResplMain.getEditors(ro));
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (c instanceof JLabel && value instanceof ResplendenceEditor) {
					((JLabel)c).setText(((ResplendenceEditor)value).name());
					((JLabel)c).setIcon(new ImageIcon(((ResplendenceEditor)value).smallIcon()));
				}
				return c;
			}
		});
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ae) {
				if (ae.getClickCount() != 2 || list.getSelectedValue() == null) return;
				resplOpen(((ResplendenceEditor)list.getSelectedValue()), getResplendenceObject());
				dispose();
			}
		});
		main.add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		JPanel buttons = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (list.getSelectedValue() == null) return;
				resplOpen(((ResplendenceEditor)list.getSelectedValue()), getResplendenceObject());
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
		main.add(buttons, BorderLayout.PAGE_END);
		
		main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		this.setContentPane(main);
		this.getRootPane().setDefaultButton(okButton);
		ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
