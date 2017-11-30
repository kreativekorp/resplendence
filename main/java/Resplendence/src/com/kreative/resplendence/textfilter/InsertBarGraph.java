package com.kreative.resplendence.textfilter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class InsertBarGraph implements TextFilter {
	public String category(int i) {
		return "Insert";
	}

	public String filter(int i, String s) {
		new BarGraphWindow();
		return null;
	}

	public boolean insert(int i) {
		return true;
	}

	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "BarGraph...";
	}
	
	public int numberOfFilters() {
		return 1;
	}
	
	private class BarGraphWindow extends JFrame {
		private static final long serialVersionUID = 1;
		private ResplendenceListener lastlis = null;
		private JTextField title, upc, maxwid, ch;
		private JTextArea values;
		public BarGraphWindow() {
			super("BarGraph");
			lastlis = ResplMain.getFrontmostResplendenceListener();
			ResplMain.registerWindow(this);
			JPanel main = new JPanel(new BorderLayout());
			JPanel form = new JPanel(new GridBagLayout());
			JPanel buttons = new JPanel(new FlowLayout());
			GridBagConstraints left = new GridBagConstraints();
			left.insets = new Insets(0,0,5,5);
			left.anchor = GridBagConstraints.EAST;
			left.gridx = 0;
			GridBagConstraints right = new GridBagConstraints();
			right.insets = new Insets(0,0,5,5);
			right.anchor = GridBagConstraints.WEST;
			right.gridx = 1;
			left.gridy = right.gridy = 0;
			form.add(new JLabel("Title:"), left);
			form.add(title = new JTextField(30), right);
			left.gridy = right.gridy = 1;
			form.add(new JLabel("Units/Char:"), left);
			form.add(upc = new JTextField("10", 6), right);
			left.gridy = right.gridy = 2;
			form.add(new JLabel("Max Width:"), left);
			form.add(maxwid = new JTextField("80", 6), right);
			left.gridy = right.gridy = 3;
			form.add(new JLabel("Character:"), left);
			form.add(ch = new JTextField("*", 1), right);
			left.gridy = right.gridy = 4;
			form.add(new JLabel("Values:"), left);
			form.add(new JScrollPane(values = new JTextArea(4, 29), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), right);
			values.setLineWrap(true);
			values.setWrapStyleWord(true);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					BarGraphWindow.this.dispose();
				}
			});
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplMain.sendResplendenceEventTo(lastlis, new ResplendenceEvent(this, ResplendenceEvent.INSERT_TEXT, "BarGraph", makeGraph()));
					BarGraphWindow.this.dispose();
				}
			});
			buttons.add(cancelButton);
			buttons.add(okButton);
			form.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 7));
			buttons.setBorder(BorderFactory.createEmptyBorder(5, 12, 8, 12));
			if ((ResplUtils.getModifiersEx() & InputEvent.ALT_DOWN_MASK) > 0) {
				JLabel ll = new JLabel("Welcome to KES Gammasoft OnrayBarGrapher v5.00!!!");
				ll.setFont(ll.getFont().deriveFont(Font.BOLD, 14.0f));
				ll.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				ll.setHorizontalAlignment(JLabel.CENTER);
				ll.setHorizontalTextPosition(JLabel.CENTER);
				ll.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				main.add(ll, BorderLayout.PAGE_START);
			}
			main.add(form, BorderLayout.CENTER);
			main.add(buttons, BorderLayout.PAGE_END);
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		private int parseInt(String s) {
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				return 0;
			}
		}
		private double parseDouble(String s) {
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException nfe) {
				return 0.0;
			}
		}
		public String makeGraph() {
			String t = title.getText().trim();
			double u = parseDouble(upc.getText());
			int m = parseInt(maxwid.getText());
			if (u <= 0.0 && m <= 0) { u = 1.0; m = Integer.MAX_VALUE; }
			else if (u <= 0.0) u = (double)m;
			else if (m <= 0) m = Integer.MAX_VALUE;
			char c = (ch.getText().length()<1)?'*':ch.getText().charAt(0);
			String[] va = values.getText().split("[\n\r,;:]+");
			int b = 0, e = va.length;
			while (b<e && va[b].length()<1) b++;
			while (e>b && va[e-1].length()<1) e--;
			double[] v = new double[e-b];
			double mv = Double.NEGATIVE_INFINITY;
			for (int i=b, j=0; i<e && j<v.length; i++, j++) {
				v[j] = parseDouble(va[i])/u;
				if (v[j] > mv) mv = v[j];
			}
			if (mv > m) {
				double a = m/mv;
				for (int i=0; i<v.length; i++) {
					v[i] *= a;
				}
				mv = m;
			}
			String g = "";
			if (t.length() > 0) g += t+"\n";
			String stars = ""+c;
			while (stars.length() < mv) stars += stars;
			for (int i=0; i<v.length; i++) {
				g += stars.substring(0, (int)Math.round(v[i]))+"\n";
			}
			if (g.endsWith("\n")) g = g.substring(0, g.length()-1);
			return g;
		}
	}
}
