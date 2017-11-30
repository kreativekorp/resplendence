package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.swing.*;

public class PreferencesWindow extends JFrame {
	private static final long serialVersionUID = 1l;
	
	private static PreferencesWindow instance;
	public static PreferencesWindow getInstance() {
		if (instance == null) instance = new PreferencesWindow();
		return instance;
	}
	public static void showInstance() {
		if (instance == null) instance = new PreferencesWindow();
		instance.setVisible(true);
	}
	
	private JPanel main;
	private JPanel spPanel;
	private JTextField spPath;
	private JButton spButton;
	private JTextField mrField;
	private JComboBox csPopup;
	private JButton cwButton;
	private JTextField pmField;
	private JButton opfButton;
	private JTextArea unidbField;
	private JButton unidbButton;
	
	private static final int WIDTH = 180;
	
	public PreferencesWindow() {
		super("Preferences");
		ResplMain.registerWindow(this);
		main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		if (!ResplMain.RUNNING_ON_MAC_OS) {
			spPanel = new JPanel(new BorderLayout(8,8));
			spPath = new JTextField(ResplPrefs.getString("Startup Path"));
			spPath.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					ResplPrefs.setString("Startup Path", spPath.getText());
				}
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
			});
			spButton = new JButton("Browse...");
			spButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setMultiSelectionEnabled(false);
					if (fc.showOpenDialog(PreferencesWindow.this) == JFileChooser.APPROVE_OPTION) {
						spPath.setText(fc.getSelectedFile().getAbsolutePath());
						ResplPrefs.setString("Startup Path", fc.getSelectedFile().getAbsolutePath());
					}
				}
			});
			spPanel.add(spPath, BorderLayout.CENTER);
			spPanel.add(spButton, BorderLayout.EAST);
			main.add(makeLine("Startup Path:", WIDTH, spPanel));
		}
		
		mrField = new JTextField(Integer.toString(ResplPrefs.getInt("Max Recent")), 5);
		mrField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				try {
					ResplPrefs.setInt("Max Recent", Integer.parseInt(mrField.getText()));
				} catch (NumberFormatException nfe) {}
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
		});
		main.add(makeLine("Max Recent Items:", WIDTH, wrapBorder(mrField)));
		
		csPopup = new JComboBox(JHexEditorColorSchemes.COLOR_SCHEME_NAMES);
		csPopup.setEditable(false);
		csPopup.setMaximumRowCount(50);
		String csName = ResplPrefs.getString("Hex Color Scheme");
		if (csName == null || csName.length() < 1) csName = "Aqua";
		csPopup.setSelectedItem(csName);
		csPopup.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String csName = e.getItem().toString();
					ResplPrefs.setString("Hex Color Scheme", csName);
				}
			}
		});
		main.add(makeLine("Hex Editor Color Scheme:", WIDTH, wrapBorder(csPopup)));
		
		java.util.List<String> unidbList = ResplPrefs.getStrings("UnicodeDBServers");
		if (unidbList == null || unidbList.isEmpty()) {
			unidbList = new java.util.Vector<String>();
			unidbList.add("http://www.unicode.org/Public/UNIDATA/");
			unidbList.add("http://www.kreativekorp.com/ucsur/UNIDATA/");
		}
		String unidbStr = ""; for (String s : unidbList) unidbStr += s + "\n";
		unidbField = new JTextArea(unidbStr);
		unidbField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				String[] s = unidbField.getText().split("\\n|\\r\\n?");
				ResplPrefs.setStrings("UnicodeDBServers", Arrays.asList(s));
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
		});
		main.add(makeLine("Unicode Database Servers:", WIDTH, wrapBorder(wrapScroll(unidbField))));
		
		unidbButton = new JButton("Clear Unicode Database Cache");
		unidbButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DFFResourceProvider dp = ResplPrefs.getPrefsDFFResourceProvider();
				dp.remove(KSFLConstants.Text_Pln, 2);
				dp.remove(KSFLConstants.Text_Pln, 3);
			}
		});
		main.add(makeLine("Unicode Database Cache:", WIDTH, wrapBorder(unidbButton)));
		
		cwButton = new JButton("Reset Ignored Warnings");
		cwButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplPrefs.resetLongs("Ignored Warnings");
			}
		});
		main.add(makeLine("Warnings:", WIDTH, wrapBorder(cwButton)));
		
		pmField = new JTextField(Integer.toString(ResplPrefs.getInt("Oink")), 5);
		pmField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				try {
					ResplPrefs.setInt("Oink", Integer.parseInt(pmField.getText()));
				} catch (NumberFormatException nfe) {}
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
		});
		main.add(makeLine("Pig Mode Interval:", WIDTH, wrapBorder(pmField)));
		
		opfButton = new JButton("Open Preferences File");
		opfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ResplMain.resplOpen(ResplPrefs.getPreferencesFile());
			}
		});
		main.add(makeLine("Advanced:", WIDTH, wrapBorder(opfButton)));
		
		JPanel main2 = new JPanel(new BorderLayout());
		main2.add(main, BorderLayout.NORTH);
		main2.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
		setContentPane(main2);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
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
	
	private static JPanel wrapBorder(Component c) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(c, BorderLayout.WEST);
		return p;
	}
	
	private static JScrollPane wrapScroll(Component c) {
		return new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
}
