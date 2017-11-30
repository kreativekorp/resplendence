package com.kreative.resplendence.infobox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.util.*;

public class DFFInfoBox extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static DFFInfoBox getInfoBoxFor(ActionEvent ae) {
		Object o = ae.getSource();
		if (o instanceof Component) {
			Component c = (Component)o;
			while (c != null && !(c instanceof DFFInfoBox)) c = c.getParent();
			if (c instanceof DFFInfoBox) {
				return (DFFInfoBox)c;
			}
		}
		return null;
	}
	
	private DFFResource info;
	
	public DFFInfoBox(ActionListener al) {
		this(new DFFResource(KSFLConstants.Data_Bin, 0, new byte[0]), al);
	}
	
	public DFFInfoBox(DFFResource info, ActionListener al) {
		super("DFF Object Info");
		this.info = info;
		ResplMain.registerWindow(this);
		makeGUI(al);
	}
	
	private JPanel main, checkboxes, buttons;
	private JLabel typeLabel, idLabel, dtLabel, nameLabel;
	private JTextField typeField, idField, dtField, nameField;
	private DefaultTableModel typeModel;
	private JTable typeTable;
	private JScrollPane typePane;
	private JCheckBox readOnlyBox, systemBox, preloadBox, purgeableBox;
	private JCheckBox fromFileBox, fromRsrcBox, hiddenBox, disabledBox;
	private JCheckBox protectedBox, fixedBox, multilingualBox, compressedBox;
	private JCheckBox appUse1Box, appUse2Box, appUse3Box, appUse4Box;
	private JButton okButton, cancelButton;
	
	boolean eventHappening = false;
	
	private void makeGUI(ActionListener al) {
		typeModel = new DefaultTableModel(new Object[]{"Type", "Description"}, 0);
		VectorMap<Long,String> typeRef = ResplRsrcs.getSymbolReference("DFFType#", 0, (long)0);
		for (Map.Entry<Long,String> e : typeRef.entrySet()) {
			typeModel.addRow(new Object[]{KSFLUtilities.eccs(e.getKey()), e.getValue()});
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
						info.type = KSFLUtilities.ecc(type);
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
		typeField = new JTextField(KSFLUtilities.eccs(info.type), 8);
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
					info.type = KSFLUtilities.ecc(txt);
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
		idField = new JTextField(Integer.toString(info.id), 11);
		idField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if (!eventHappening) {
					eventHappening = true;
					try {
						info.id = Integer.parseInt(idField.getText());
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
		
		dtLabel = new JLabel("App Use:");
		dtLabel.setHorizontalAlignment(JLabel.RIGHT);
		dtField = new JTextField(Short.toString(info.datatype), 6);
		dtField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if (!eventHappening) {
					eventHappening = true;
					try {
						info.datatype = Short.parseShort(dtField.getText());
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
		
		checkboxes = new JPanel(new GridLayout(4,4,4,4));
		readOnlyBox = new JCheckBox("Read Only");
		readOnlyBox.setSelected(info.readonly);
		readOnlyBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.readonly = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(readOnlyBox);
		systemBox = new JCheckBox("System");
		systemBox.setSelected(info.system);
		systemBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.system = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(systemBox);
		preloadBox = new JCheckBox("Preload");
		preloadBox.setSelected(info.preload);
		preloadBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.preload = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(preloadBox);
		purgeableBox = new JCheckBox("Purgeable");
		purgeableBox.setSelected(info.purgeable);
		purgeableBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.purgeable = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(purgeableBox);
		fromFileBox = new JCheckBox("From File");
		fromFileBox.setSelected(info.fromfile);
		fromFileBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.fromfile = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(fromFileBox);
		fromRsrcBox = new JCheckBox("From Resource");
		fromRsrcBox.setSelected(info.fromrsrc);
		fromRsrcBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.fromrsrc = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(fromRsrcBox);
		hiddenBox = new JCheckBox("Invisible");
		hiddenBox.setSelected(info.invisible);
		hiddenBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.invisible = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(hiddenBox);
		disabledBox = new JCheckBox("Disabled");
		disabledBox.setSelected(info.disabled);
		disabledBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.disabled = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(disabledBox);
		protectedBox = new JCheckBox("Protected");
		protectedBox.setSelected(info.protect);
		protectedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.protect = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(protectedBox);
		fixedBox = new JCheckBox("Fixed");
		fixedBox.setSelected(info.fixed);
		fixedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.fixed = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(fixedBox);
		multilingualBox = new JCheckBox("Multilingual");
		multilingualBox.setSelected(info.multilingual);
		multilingualBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.multilingual = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(multilingualBox);
		compressedBox = new JCheckBox("Compressed");
		compressedBox.setSelected(info.compressed);
		compressedBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.compressed = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(compressedBox);
		appUse1Box = new JCheckBox("App Use 1");
		appUse1Box.setSelected(info.appuse1);
		appUse1Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.appuse1 = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(appUse1Box);
		appUse2Box = new JCheckBox("App Use 2");
		appUse2Box.setSelected(info.appuse2);
		appUse2Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.appuse2 = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(appUse2Box);
		appUse3Box = new JCheckBox("App Use 3");
		appUse3Box.setSelected(info.appuse3);
		appUse3Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.appuse3 = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(appUse3Box);
		appUse4Box = new JCheckBox("App Use 4");
		appUse4Box.setSelected(info.appuse4);
		appUse4Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				info.appuse4 = ((JCheckBox)ae.getSource()).isSelected();
			}
		});
		checkboxes.add(appUse4Box);
		
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
		gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(typePane, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(dtLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		main.add(dtField, gbc);
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		main.add(nameLabel, gbc);
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; gbc.gridheight = 1;
		main.add(nameField, gbc);
		gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.gridheight = 1;
		main.add(checkboxes, gbc);
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3; gbc.gridheight = 1;
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
	
	public DFFResource getInfo() {
		return info;
	}
}
