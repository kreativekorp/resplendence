package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class OpenDatabaseWindow extends JFrame implements ItemListener, ActionListener {
	private static final long serialVersionUID = 1l;
	
	private JPanel main;
	private JComboBox driverPop;
	private JTextField driverFld, protocolFld, serverFld, databaseFld, userFld, passwordFld;
	private JPanel drivPopPnl, driverPnl, protocolPnl, serverPnl, databasePnl, userPnl, passwordPnl;
	private JButton connBtn, quitBtn;
	
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
		p.setMaximumSize(new Dimension(p.getMaximumSize().width, p.getPreferredSize().height));
		return p;
	}
	
	private static Connection connect(String driver, String url, String user, String password) {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenDatabaseWindow() {
		super("Open Database");
		ResplMain.registerWindow(this);
		main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		driverPop = new JComboBox();
		driverPop.setEditable(false);
		driverPop.setMaximumRowCount(32);
		driverPop.addItem("MySQL");
		driverPop.addItem("Other");
		driverPop.addItemListener(this);
		main.add(drivPopPnl = makeLine("System:", 70, driverPop));
		drivPopPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		driverFld = new JTextField("com.mysql.jdbc.Driver");
		main.add(driverPnl = makeLine("Driver:", 70, driverFld));
		driverPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		driverPnl.setVisible(false);
		protocolFld = new JTextField("mysql");
		main.add(protocolPnl = makeLine("Protocol:", 70, protocolFld));
		protocolPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		protocolPnl.setVisible(false);
		serverFld = new JTextField("localhost");
		main.add(serverPnl = makeLine("Server:", 70, serverFld));
		serverPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		databaseFld = new JTextField();
		main.add(databasePnl = makeLine("Database:", 70, databaseFld));
		databasePnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		userFld = new JTextField();
		main.add(userPnl = makeLine("User:", 70, userFld));
		userPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		passwordFld = new JPasswordField();
		main.add(passwordPnl = makeLine("Password:", 70, passwordFld));
		passwordPnl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		JPanel buttons = new JPanel();
		buttons.add(connBtn = new JButton("Connect"));
		connBtn.addActionListener(this);
		buttons.add(quitBtn = new JButton("Cancel"));
		quitBtn.addActionListener(this);
		main.add(buttons);
		main.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
		main.setMinimumSize(new Dimension(480, main.getMinimumSize().height));
		main.setPreferredSize(new Dimension(480, main.getPreferredSize().height));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setContentPane(main);
		getRootPane().setDefaultButton(connBtn);
		ResplUtils.setCancelButton(this.getRootPane(), quitBtn);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getItem();
		if ("MySQL".equals(o)) {
			driverPnl.setVisible(false);
			protocolPnl.setVisible(false);
			driverFld.setText("com.mysql.jdbc.Driver");
			protocolFld.setText("mysql");
		} else if ("Other".equals(o)) {
			driverPnl.setVisible(true);
			protocolPnl.setVisible(true);
		}
		main.setMinimumSize(null);
		main.setPreferredSize(null);
		main.setMinimumSize(new Dimension(480, main.getMinimumSize().height));
		main.setPreferredSize(new Dimension(480, main.getPreferredSize().height));
		this.pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == quitBtn) {
			this.dispose();
		} else {
			Connection conn = connect(driverFld.getText(), "jdbc:"+protocolFld.getText()+"://"+serverFld.getText()+"/"+databaseFld.getText(), userFld.getText(), passwordFld.getText());
			if (conn == null) {
				JOptionPane.showMessageDialog(this, "Could not connect to database.", "", JOptionPane.ERROR_MESSAGE);
			} else {
				this.dispose();
				ResplMain.resplOpen(new DBObject(conn));
			}
		}
	}
}
