package com.kreative.resplendence.infobox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.kreative.ksfl.*;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;
import com.kreative.util.*;

public class ResourceInfoBox extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static ResourceInfoBox getInfoBoxFor(ActionEvent ae) {
		Object o = ae.getSource();
		if (o instanceof Component) {
			Component c = (Component)o;
			while (c != null && !(c instanceof ResourceInfoBox)) c = c.getParent();
			if (c instanceof ResourceInfoBox) {
				return (ResourceInfoBox)c;
			}
		}
		return null;
	}
	
	private MacResource info;
	
	public ResourceInfoBox(ActionListener al) {
		this(new MacResource(KSFLConstants.DATA, (short)128, new byte[0]), al);
	}
	
	public ResourceInfoBox(MacResource info, ActionListener al) {
		super("MacResource Info");
		this.info = info;
		ResplMain.registerWindow(this);
		makeGUI(al);
	}
	
	private JPanel main, checkboxes, buttons;
	private JLabel typeLabel, idLabel, otLabel, oidLabel, sidLabel, nameLabel;
	private JTextField typeField, idField, oidField, sidField, nameField;
	private DefaultTableModel typeModel;
	private JTable typeTable;
	private JScrollPane typePane;
	private JComboBox otMenu;
	private JCheckBox sysheapBox, purgeableBox, lockedBox;
	private JCheckBox protectedBox, preloadBox, compressedBox;
	private JCheckBox changedBox, reservedBox;
	private JButton okButton, cancelButton;
	
	boolean eventHappening = false;
	
	private void makeGUI(ActionListener al) {
		typeModel = new DefaultTableModel(new Object[]{"Type", "Description"}, 0);
		VectorMap<Integer,String> typeRef = ResplRsrcs.getSymbolReference("ResType#", 0, (int)0);
		for (Map.Entry<Integer,String> e : typeRef.entrySet()) {
			typeModel.addRow(new Object[]{KSFLUtilities.fccs(e.getKey()), e.getValue()});
		}
		typeTable = new JTable(typeModel);
		typeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		typeTable.setRowSelectionAllowed(true);
		typeTable.setColumnSelectionAllowed(false);
		typeTable.setFocusable(false);
		typeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!eventHappening) {
					eventHappening = true;
					int idx = typeTable.getSelectedRow();
					if (idx >= 0) {
						String type = typeModel.getValueAt(idx, 0).toString();
						typeField.setText(type);
						info.type = KSFLUtilities.fcc(type);
					}
					eventHappening = false;
				}
			}
		});
		typePane = new JScrollPane(typeTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		typePane.setPreferredSize(typePane.getMinimumSize());
		typePane.setFocusable(false);
		
		typeLabel = new JLabel("Type:");
		typeLabel.setHorizontalAlignment(JLabel.RIGHT);
		typeField = new JTextField(KSFLUtilities.fccs(info.type), 4);
		typeField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if (!eventHappening) {
					eventHappening = true;
					String txt = typeField.getText();
					for (int i=0; i<typeModel.getRowCount(); i++) {
						if (typeModel.getValueAt(i, 0).equals(txt)) {
							typeTable.setRowSelectionInterval(i, i);
							typeTable.scrollRectToVisible(typeTable.getCellRect(i, 0, false));
						}
					}
					info.type = KSFLUtilities.fcc(txt);
					eventHappening = false;
				}
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
		});
		
		idLabel = new JLabel("ID:");
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		idField = new JTextField(Short.toString(info.id), 6);
		idField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if (!eventHappening) {
					eventHappening = true;
					try {
						info.id = Short.parseShort(idField.getText());
						otMenu.setSelectedIndex(info.getOwnerType());
						oidField.setText(Integer.toString(info.getOwnerID()));
						sidField.setText(Integer.toString(info.getSubID()));
					} catch (NumberFormatException nfe) {}
					eventHappening = false;
				}
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
		});
		
		otLabel = new JLabel("Owner Type:");
		otLabel.setHorizontalAlignment(JLabel.RIGHT);
		otMenu = new JComboBox(new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23",
				"DRVR", "WDEF", "MDEF", "CDEF",
				"PDEF", "PACK", "RSV1", "RSV2"
		});
		otMenu.setEditable(false);
		otMenu.setMaximumRowCount(32);
		otMenu.setSelectedIndex(info.getOwnerType());
		otMenu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (!eventHappening && ie.getStateChange() == ItemEvent.SELECTED) {
					eventHappening = true;
					info.setOwnerType(otMenu.getSelectedIndex());
					idField.setText(Short.toString(info.id));
					eventHappening = false;
				}
			}
		});
		
		oidLabel = new JLabel("Owner ID:");
		oidLabel.setHorizontalAlignment(JLabel.RIGHT);
		oidField = new JTextField(Integer.toString(info.getOwnerID()), 2);
		oidField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				if (!eventHappening) {
					eventHappening = true;
					try {
						info.setOwnerID(Integer.parseInt(oidField.getText()));
						idField.setText(Short.toString(info.id));
					} catch (NumberFormatException nfe) {}
					eventHappening = false;
				}
			}
			public void insertUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
			public void removeUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
		});
		
		sidLabel = new JLabel("Sub ID:");
		sidLabel.setHorizontalAlignment(JLabel.RIGHT);
		sidField = new JTextField(Integer.toString(info.getSubID()), 2);
		sidField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				if (!eventHappening) {
					eventHappening = true;
					try {
						info.setSubID(Integer.parseInt(sidField.getText()));
						idField.setText(Short.toString(info.id));
					} catch (NumberFormatException nfe) {}
					eventHappening = false;
				}
			}
			public void insertUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
			public void removeUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
		});
		
		nameLabel = new JLabel("Name:");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameField = new JTextField(info.name);
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				info.name = nameField.getText();
			}
			public void insertUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
			public void removeUpdate(DocumentEvent arg0) {
				changedUpdate(arg0);
			}
		});
		
		checkboxes = new JPanel(new GridLayout(2,4,4,4));
		reservedBox = new JCheckBox("Reserved");
		reservedBox.setSelected(info.reserved);
		reservedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.reserved = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(reservedBox);
		sysheapBox = new JCheckBox("System Heap");
		sysheapBox.setSelected(info.sysheap);
		sysheapBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.sysheap = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(sysheapBox);
		purgeableBox = new JCheckBox("Purgeable");
		purgeableBox.setSelected(info.purgeable);
		purgeableBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.purgeable = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(purgeableBox);
		lockedBox = new JCheckBox("Locked");
		lockedBox.setSelected(info.locked);
		lockedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.locked = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(lockedBox);
		protectedBox = new JCheckBox("Protected");
		protectedBox.setSelected(info.protect);
		protectedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.protect = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(protectedBox);
		preloadBox = new JCheckBox("Preload");
		preloadBox.setSelected(info.preload);
		preloadBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.preload = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(preloadBox);
		changedBox = new JCheckBox("Changed");
		changedBox.setSelected(info.changed);
		changedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.changed = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(changedBox);
		compressedBox = new JCheckBox("Compressed");
		compressedBox.setSelected(info.compressed);
		compressedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.compressed = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(compressedBox);
		
		buttons = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(al);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttons.add(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		buttons.add(cancelButton);
		
		//time to bring out the GridBag
		main = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4,4,4,4);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.0; gbc.weighty = 1.0;
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(typeLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(typeField, gbc);
		gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(idLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(idField, gbc);
		gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 5;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(typePane, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(otLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(otMenu, gbc);
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(oidLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(oidField, gbc);
		gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(sidLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(sidField, gbc);
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(nameLabel, gbc);
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; gbc.gridheight = 1;
		main.add(nameField, gbc);
		gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3; gbc.gridheight = 1;
		main.add(checkboxes, gbc);
		gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3; gbc.gridheight = 1;
		main.add(buttons, gbc);
		main.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
		
		this.setContentPane(main);
		this.getRootPane().setDefaultButton(okButton);
		ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public MacResource getInfo() {
		return info;
	}
}
