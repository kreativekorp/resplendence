package com.kreative.resplendence.infobox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.kreative.ksfl.*;
import com.kreative.prc.*;
import com.kreative.resplendence.*;
import com.kreative.util.*;

public class PRCInfoBox extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static PRCInfoBox getInfoBoxFor(ActionEvent ae) {
		Object o = ae.getSource();
		if (o instanceof Component) {
			Component c = (Component)o;
			while (c != null && !(c instanceof PRCInfoBox)) c = c.getParent();
			if (c instanceof PRCInfoBox) {
				return (PRCInfoBox)c;
			}
		}
		return null;
	}
	
	private PalmResource info;
	
	public PRCInfoBox(ActionListener al) {
		this(new PalmResource(KSFLConstants.DATA, (short)0, new byte[0]), al);
	}
	
	public PRCInfoBox(PalmResource info, ActionListener al) {
		super("PRC Resource Info");
		this.info = info;
		ResplMain.registerWindow(this);
		makeGUI(al);
	}
	
	private JPanel main, buttons;
	private JLabel typeLabel, idLabel;
	private JTextField typeField, idField;
	private DefaultTableModel typeModel;
	private JTable typeTable;
	private JScrollPane typePane;
	private JButton okButton, cancelButton;
	
	boolean eventHappening = false;
	
	private void makeGUI(ActionListener al) {
		typeModel = new DefaultTableModel(new Object[]{"Type", "Description"}, 0);
		VectorMap<Integer,String> typeRef = ResplRsrcs.getSymbolReference("PRCType#", 0, (int)0);
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
		typePane.setPreferredSize(new Dimension(200, typePane.getMinimumSize().height));
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
		gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(typePane, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.gridheight = 1;
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
	
	public PalmResource getInfo() {
		return info;
	}
}
