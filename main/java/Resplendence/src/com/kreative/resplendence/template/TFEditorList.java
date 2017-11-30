package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import com.kreative.resplendence.*;

public class TFEditorList extends JPanel implements TFEditor {
	private static final long serialVersionUID = 1;
	
	public static final int EOF_TERMINATED = 0;
	public static final int ZERO_TERMINATED = 1;
	public static final int COUNTED = 2;
	
	private int i;
	private String t;
	private List<TemplateField> f;
	private String s;
	private int l;
	private TFEditorListCount c;
	
	private Image addimg = ResplRsrcs.getPNG(2000);
	//private Image dupimg = ResplendenceMain.getPNG(2001);
	private Image delimg = ResplRsrcs.getPNG(2002);
	
	public TFEditorList(int indent, String textEncoding, List<TemplateField> fields, String listStr, int listType, TFEditorListCount listCount) {
		i = indent;
		t = textEncoding;
		f = fields;
		s = listStr;
		l = listType;
		c = listCount;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new ListSeparatorBar(0), 0);
	}
	
	private void clear() {
		Component[] cc = this.getComponents();
		for (int ii=0; ii<cc.length; ii++) {
			if (cc[ii] instanceof ListItem) this.remove(cc[ii]);
		}
	}
	
	private void reindex() {
		Component[] cc = this.getComponents();
		for (int ii=0; ii<cc.length; ii++) {
			if (cc[ii] instanceof ListSeparatorBar)
				((ListSeparatorBar)cc[ii]).setIndex(ii);
			else if (cc[ii] instanceof ListItem)
				((ListItem)cc[ii]).setIndex(ii);
		}
		if (c != null) c.setCount(cc.length-1);
	}

	public Position readValue(byte[] data, Position pos) {
		int n=0;
		clear();
		switch (l) {
		case EOF_TERMINATED:
			while (pos.bytepos() < data.length) {
				ListItem li = new ListItem(n);
				pos = li.readValue(data, pos);
				add(li, n++);
			}
			break;
		case ZERO_TERMINATED:
			byte[] v = new byte[1];
			while (true) {
				Position p2 = pos.getBytes(data, v);
				if (v[0] == 0) {
					pos = p2;
					break;
				} else {
					ListItem li = new ListItem(n);
					pos = li.readValue(data, pos);
					add(li, n++);
				}
			}
			break;
		case COUNTED:
			if (c != null) {
				long m = c.getCount();
				for (n=0; n<m; n++) {
					ListItem li = new ListItem(n);
					pos = li.readValue(data, pos);
					add(li, n);
				}
			}
			break;
		}
		reindex();
		return pos;
	}

	public Position writeValue(byte[] data, Position pos) {
		Component[] cc = this.getComponents();
		for (int ii=0; ii<cc.length; ii++) {
			if (cc[ii] instanceof TFEditor)
				pos = ((TFEditor)cc[ii]).writeValue(data, pos);
		}
		if (l == ZERO_TERMINATED) pos = pos.setBytes(data, new byte[]{0});
		return pos;
	}

	public Position writeValue(Position pos) {
		Component[] cc = this.getComponents();
		for (int ii=0; ii<cc.length; ii++) {
			if (cc[ii] instanceof TFEditor)
				pos = ((TFEditor)cc[ii]).writeValue(pos);
		}
		if (l == ZERO_TERMINATED) pos = pos.skipBytes(1);
		return pos;
	}
	
	private class ListSeparatorBar extends JPanel {
		private static final long serialVersionUID = 1;
		private int idx;
		private JButton add, /*dup,*/ del;
		private JLabel lbl;
		public ListSeparatorBar(int index) {
			super(new BorderLayout(8,8));
			JPanel buttons = new JPanel(new GridLayout(1,3,-1,-1));
			buttons.add(add = new JButton(new ImageIcon(addimg)));
			//buttons.add(dup = new JButton(new ImageIcon(dupimg)));
			buttons.add(del = new JButton(new ImageIcon(delimg)));
			Dimension d = new Dimension(17,17);
			add.putClientProperty("JButton.buttonType", "toolbar"); add.setPreferredSize(d);
			//dup.putClientProperty("JButton.buttonType", "toolbar"); dup.setPreferredSize(d);
			del.putClientProperty("JButton.buttonType", "toolbar"); del.setPreferredSize(d);
			JPanel buttons2 = new JPanel(new BorderLayout());
			buttons2.add(buttons, BorderLayout.EAST);
			buttons2.setPreferredSize(new Dimension(i+10, d.height));
			add(buttons2, BorderLayout.WEST);
			add(lbl = new JLabel((index+1)+" "+s));
			lbl.setFont(mono);
			idx = index;
			
			add.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TFEditorList.this.add(new ListItem(idx), idx);
					TFEditorList.this.reindex();
				}
			});
			del.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (TFEditorList.this.getComponent(idx) instanceof ListItem) {
						TFEditorList.this.remove(idx);
						TFEditorList.this.reindex();
					}
				}
			});
		}
		/*
		public JButton addButton() {
			return add;
		}
		//public JButton duplicateButton() {
			//return dup;
		//}
		public JButton deleteButton() {
			return del;
		}
		public int getIndex() {
			return idx;
		}
		*/
		public void setIndex(int index) {
			lbl.setText((index+1)+" "+s);
			idx = index;
		}
	}
	
	private class ListItem extends JPanel implements TFEditor {
		private static final long serialVersionUID = 1;
		private ListSeparatorBar bar;
		private TFEditorTemplate edit;
		public ListItem(int index) {
			super(new BorderLayout(8,8));
			add(bar = new ListSeparatorBar(index), BorderLayout.NORTH);
			add(edit = new TFEditorTemplate(i+20,t,f), BorderLayout.CENTER);
		}
		/*
		public int getIndex() {
			return bar.getIndex();
		}
		*/
		public void setIndex(int index) {
			bar.setIndex(index);
		}
		public Position readValue(byte[] data, Position pos) {
			return edit.readValue(data, pos);
		}
		public Position writeValue(byte[] data, Position pos) {
			return edit.writeValue(data, pos);
		}
		public Position writeValue(Position pos) {
			return edit.writeValue(pos);
		}
	}
}
