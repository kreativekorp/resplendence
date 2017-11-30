package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TFEditorCoordinates extends JPanel implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private int nc;
	private boolean rev;
	public TFEditorInteger x, y, z, x2, y2;
	
	public TFEditorCoordinates(int bitWidth, boolean littleEndian, int numCoords, boolean reverse) {
		nc = numCoords;
		rev = reverse;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,0,0,5);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 0;
		switch (nc) {
		case 2:
			add(x = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(y = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			break;
		case 3:
			add(x = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(y = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(z = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			break;
		case 4:
			add(x = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(y = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(x2 = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			add(y2 = new TFEditorInteger(bitWidth, 10, false, littleEndian), c); c.gridx++;
			JButton r = new JButton("Set");
			r.setFont(mono);
			r.setPreferredSize(new Dimension(50,x.getPreferredSize().height));
			r.setMaximumSize(new Dimension(50,x.getPreferredSize().height));
			r.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final RectangleSetWindow f = new RectangleSetWindow();
					f.addWindowListener(new WindowListener() {
						public void windowActivated(WindowEvent arg0) {}
						public void windowClosed(WindowEvent arg0) {
							windowClosing(arg0);
						}
						public void windowClosing(WindowEvent arg0) {
							x.setText(Integer.toString(f.x1));
							y.setText(Integer.toString(f.y1));
							x2.setText(Integer.toString(f.x2));
							y2.setText(Integer.toString(f.y2));
						}
						public void windowDeactivated(WindowEvent arg0) {
							windowClosing(arg0);
						}
						public void windowDeiconified(WindowEvent arg0) {}
						public void windowIconified(WindowEvent arg0) {}
						public void windowOpened(WindowEvent arg0) {}
					});
				}
			});
			add(r, c); c.gridx++;
			JButton s = new JButton("Edit");
			s.setFont(mono);
			s.setPreferredSize(new Dimension(60,x.getPreferredSize().height));
			s.setMaximumSize(new Dimension(60,x.getPreferredSize().height));
			s.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int x1 = (int)parseLong(x.getText());
					int y1 = (int)parseLong(y.getText());
					int w1 = (int)parseLong(x2.getText())-x1;
					int h1 = (int)parseLong(y2.getText())-y1;
					JFrame f = new RectangleEditWindow(new Rectangle(x1, y1, w1, h1));
					f.addComponentListener(new ComponentListener() {
						public void componentHidden(ComponentEvent ce) {}
						public void componentShown(ComponentEvent ce) {}
						public void componentMoved(ComponentEvent ce) {
							Rectangle r = ce.getComponent().getBounds();
							x.setText(Integer.toString(r.x));
							y.setText(Integer.toString(r.y));
							x2.setText(Integer.toString(r.x+r.width));
							y2.setText(Integer.toString(r.y+r.height));
						}
						public void componentResized(ComponentEvent ce) {
							Rectangle r = ce.getComponent().getBounds();
							x.setText(Integer.toString(r.x));
							y.setText(Integer.toString(r.y));
							x2.setText(Integer.toString(r.x+r.width));
							y2.setText(Integer.toString(r.y+r.height));
						}
					});
				}
			});
			add(s, c); c.gridx++;
			break;
		default:
			throw new IllegalArgumentException("Invalid Number of Coordinates");
		}
	}
	
	private long parseLong(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public Position readValue(byte[] data, Position pos) {
		switch (nc) {
		case 2:
			if (rev) {
				pos = y.readValue(data, pos);
				pos = x.readValue(data, pos);
			} else {
				pos = x.readValue(data, pos);
				pos = y.readValue(data, pos);
			}
			break;
		case 3:
			if (rev) {
				pos = z.readValue(data, pos);
				pos = y.readValue(data, pos);
				pos = x.readValue(data, pos);
			} else {
				pos = x.readValue(data, pos);
				pos = y.readValue(data, pos);
				pos = z.readValue(data, pos);
			}
			break;
		case 4:
			if (rev) {
				pos = y.readValue(data, pos);
				pos = x.readValue(data, pos);
				pos = y2.readValue(data, pos);
				pos = x2.readValue(data, pos);
			} else {
				pos = x.readValue(data, pos);
				pos = y.readValue(data, pos);
				pos = x2.readValue(data, pos);
				pos = y2.readValue(data, pos);
			}
			break;
		}
		return pos;
	}

	public Position writeValue(byte[] data, Position pos) {
		switch (nc) {
		case 2:
			if (rev) {
				pos = y.writeValue(data, pos);
				pos = x.writeValue(data, pos);
			} else {
				pos = x.writeValue(data, pos);
				pos = y.writeValue(data, pos);
			}
			break;
		case 3:
			if (rev) {
				pos = z.writeValue(data, pos);
				pos = y.writeValue(data, pos);
				pos = x.writeValue(data, pos);
			} else {
				pos = x.writeValue(data, pos);
				pos = y.writeValue(data, pos);
				pos = z.writeValue(data, pos);
			}
			break;
		case 4:
			if (rev) {
				pos = y.writeValue(data, pos);
				pos = x.writeValue(data, pos);
				pos = y2.writeValue(data, pos);
				pos = x2.writeValue(data, pos);
			} else {
				pos = x.writeValue(data, pos);
				pos = y.writeValue(data, pos);
				pos = x2.writeValue(data, pos);
				pos = y2.writeValue(data, pos);
			}
			break;
		}
		return pos;
	}

	public Position writeValue(Position pos) {
		switch (nc) {
		case 2:
			pos = x.writeValue(pos);
			pos = y.writeValue(pos);
			break;
		case 3:
			pos = x.writeValue(pos);
			pos = y.writeValue(pos);
			pos = z.writeValue(pos);
			break;
		case 4:
			pos = x.writeValue(pos);
			pos = y.writeValue(pos);
			pos = x2.writeValue(pos);
			pos = y2.writeValue(pos);
			break;
		}
		return pos;
	}
}
