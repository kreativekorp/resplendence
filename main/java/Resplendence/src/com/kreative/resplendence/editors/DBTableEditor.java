package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.resplendence.*;

public class DBTableEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("DBTABLE");
	}
	
	public String name() {
		return "Database Table Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		try {
			return new WTableEditor(ro);
		} catch (SQLException se) {
			return null;
		}
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isContainerType() && ro.getType().equals(ResplendenceObject.TYPE_DATABASE_TABLE)) {
			return DEFAULT_EDITOR+1;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Table";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("DBTABLE"));
	}
	
	public static class WTableEditor extends ResplendenceEditorWindow implements RecordScrollListener, MouseListener, ActionListener {
		private static final long serialVersionUID = 1L;
		
		private final JList listProps = new JList();
		private Connection conn;
		private ResultSetMetaData rmd;
		private String table;
		private JPanel main;
		private JPanel left;
		private JAWDBWidget wid;
		private JMySeparator sep1, sep2;
		private JLabel recl, recnl, recsl, recnsl;
		private Component recss;
		private JPanel buttons;
		private JButton insBtn, delBtn, updBtn, revBtn;
		private Vector<DisplayedRecord> rsa;
		private JPanel rsl, rsl2;
		private JScrollPane center;
		private int selCnt = 0;
		
		public WTableEditor(ResplendenceObject ro) throws SQLException {
			super(ro, true);
			setTitle(ro.getTitleForWindows());
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_NEW_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_SAVE_REVERT
			);
			
			this.conn = (Connection)ro.getProvider();
			this.table = ro.getTitleForExportedFile();
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM "+table);
			this.rmd = rs.getMetaData();
			main = new JPanel(new BorderLayout());
			
			rs.last();
			left = new JPanel();
			left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
			wid = new JAWDBWidget(0, rs.getRow());
			wid.setBorder(BorderFactory.createEmptyBorder(2,7,9,4));
			wid.setOpaque(true);
			wid.setAlignmentX(JAWDBWidget.LEFT_ALIGNMENT);
			wid.addRecordScrollListener(this);
			left.add(wid);
			sep1 = new JMySeparator();
			sep1.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
			sep1.setAlignmentX(JAWDBWidget.LEFT_ALIGNMENT);
			left.add(sep1);
			recl = new JLabel("Records:");
			recl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			recl.setHorizontalAlignment(JLabel.LEFT);
			recl.setHorizontalTextPosition(JLabel.LEFT);
			recl.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
			recl.setFont(recl.getFont().deriveFont(10.0f));
			left.add(recl);
			recnl = new JLabel(Integer.toString(rs.getRow()));
			recnl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			recnl.setHorizontalAlignment(JLabel.LEFT);
			recnl.setHorizontalTextPosition(JLabel.LEFT);
			recnl.setBorder(BorderFactory.createEmptyBorder(0, 3, 1, 3));
			recnl.setFont(recnl.getFont().deriveFont(10.0f));
			left.add(recnl);
			recss = Box.createRigidArea(new Dimension(1,6));
			recss.setVisible(selCnt>0);
			left.add(recss);
			recsl = new JLabel("Selected:");
			recsl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			recsl.setHorizontalAlignment(JLabel.LEFT);
			recsl.setHorizontalTextPosition(JLabel.LEFT);
			recsl.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
			recsl.setFont(recsl.getFont().deriveFont(10.0f));
			recsl.setVisible(selCnt>0);
			left.add(recsl);
			recnsl = new JLabel(Integer.toString(selCnt));
			recnsl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			recnsl.setHorizontalAlignment(JLabel.LEFT);
			recnsl.setHorizontalTextPosition(JLabel.LEFT);
			recnsl.setBorder(BorderFactory.createEmptyBorder(0, 3, 1, 3));
			recnsl.setFont(recnsl.getFont().deriveFont(10.0f));
			recnsl.setVisible(selCnt>0);
			left.add(recnsl);
			sep2 = new JMySeparator();
			sep2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
			sep2.setAlignmentX(JAWDBWidget.LEFT_ALIGNMENT);
			left.add(sep2);
			buttons = new JPanel();
			buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
			insBtn = new JButton(new ImageIcon(ResplRsrcs.getPNG(2000)));
			insBtn.setToolTipText("Insert Record");
			insBtn.addActionListener(this);
			buttons.add(insBtn);
			delBtn = new JButton(new ImageIcon(ResplRsrcs.getPNG(2002)));
			delBtn.setToolTipText("Delete Records");
			delBtn.addActionListener(this);
			buttons.add(delBtn);
			updBtn = new JButton(new ImageIcon(ResplRsrcs.getPNG(2004)));
			updBtn.setToolTipText("Update Records");
			updBtn.addActionListener(this);
			buttons.add(updBtn);
			revBtn = new JButton(new ImageIcon(ResplRsrcs.getPNG(2003)));
			revBtn.setToolTipText("Revert Records");
			revBtn.addActionListener(this);
			buttons.add(revBtn);
			buttons.setBackground(new Color(0xFFEEEEEE));
			buttons.setMaximumSize(buttons.getPreferredSize());
			JPanel buttonp = new JPanel();
			buttonp.add(buttons);
			buttonp.setBackground(new Color(0xFFEEEEEE));
			buttonp.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			buttonp.setMinimumSize(new Dimension(56, buttonp.getPreferredSize().height));
			buttonp.setPreferredSize(new Dimension(56, buttonp.getPreferredSize().height));
			buttonp.setMaximumSize(new Dimension(56, buttonp.getPreferredSize().height));
			left.add(buttonp);
			left.setBackground(new Color(0xFFEEEEEE));
			left.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
			left.setMinimumSize(new Dimension(56, left.getMinimumSize().height));
			left.setPreferredSize(new Dimension(56, left.getPreferredSize().height));
			left.setMaximumSize(new Dimension(56, left.getMaximumSize().height));
			main.add(left, BorderLayout.WEST);
			
			rsa = new Vector<DisplayedRecord>();
			rsl = new JPanel();
			rsl.setLayout(new BoxLayout(rsl, BoxLayout.Y_AXIS));
			rsl.setBackground(Color.white);
			int idx = 0;
			rs.beforeFirst();
			while (rs.next()) {
				JResultSetEditor rse = new JResultSetEditor(rmd);
				rse.setBorder(BorderFactory.createEmptyBorder(4, 36, 4, 20));
				rse.setOpaque(true);
				rse.setBackground(listProps.getBackground());
				rse.setForeground(listProps.getForeground());
				try {
					rse.loadResultSet(rs);
				} catch (SQLException ex) {
					rse.clear();
				}
				rse.setMaximumSize(new Dimension(rse.getMaximumSize().width, rse.getPreferredSize().height));
				MyStrut rss = new MyStrut(6);
				rss.setOpaque(true);
				rss.setBackground(listProps.getBackground());
				rss.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
				JPanel rsw = new JPanel(new BorderLayout());
				rsw.add(rss, BorderLayout.WEST);
				rsw.add(rse, BorderLayout.CENTER);
				rsw.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
				rsw.setMaximumSize(new Dimension(rsw.getMaximumSize().width, rsw.getPreferredSize().height));
				DisplayedRecord dr = new DisplayedRecord();
				dr.arrayIndex = idx++;
				dr.update = false;
				dr.insert = false;
				dr.panel = rsw;
				dr.strut = rss;
				dr.editor = rse;
				rse.privdata = dr;
				rse.addResultSetEditedListener(new MyResultSetEditedListener(dr));
				rse.addMouseListener(this);
				rsa.add(dr);
				rsl.add(rsw);
			}
			MyStrut rss2 = new MyStrut(6);
			rss2.setOpaque(true);
			rss2.setBackground(listProps.getBackground());
			rss2.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
			JPanel rss3 = new JPanel(new BorderLayout());
			rss3.add(rss2, BorderLayout.WEST);
			rss3.setOpaque(true);
			rss3.setBackground(listProps.getBackground());
			rss3.setForeground(listProps.getForeground());
			rss3.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					selectIndex(-1);
					setSelCnt(0);
				}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
			});
			rsl2 = new JPanel(new BorderLayout());
			rsl2.add(rsl, BorderLayout.NORTH);
			rsl2.add(rss3, BorderLayout.CENTER);
			center = new JScrollPane(rsl2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			center.getVerticalScrollBar().setUnitIncrement(20);
			main.add(center, BorderLayout.CENTER);
			
			main.add(Box.createVerticalStrut(14), BorderLayout.SOUTH);
			
			main.setBackground(Color.white);
			setContentPane(main);
			rs.close();
			
			pack();
			ResplUtils.sizeWindow(this, 3, 4);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			updateAllRecords();
		}
		
		public void revert(ResplendenceObject ro) {
			revertAllRecords();
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.NEW_ITEM:
				insertRecord();
				break;
			case ResplendenceEvent.REMOVE_ITEM:
				deleteRecords();
				break;
			case ResplendenceEvent.SELECT_ALL:
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord dr = ri.next();
					dr.setSelected(true);
				}
				break;
			}
			return null;
		}
		
		public void recordScrolled(RecordScrollEvent e) {
			scrollIndex(e.getCurrentRecord());
			int cw = rsa.get(e.getCurrentRecord()).editor.getWidth();
			int ch = rsa.get(e.getCurrentRecord()).editor.getHeight();
			int ctop = ch*e.getCurrentRecord();
			Rectangle r = new Rectangle(0, ctop, cw, ch);
			rsl2.scrollRectToVisible(r);
		}
		
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() instanceof JResultSetEditor) {
				JResultSetEditor rse = (JResultSetEditor)e.getSource();
				DisplayedRecord rcd = (DisplayedRecord)rse.privdata;
				int mask = (System.getProperty("os.name").toUpperCase().contains("MAC OS"))?(InputEvent.META_DOWN_MASK):(InputEvent.CTRL_DOWN_MASK);
				if ((e.getModifiersEx() & mask) > 0) {
					rcd.setSelected(!rcd.isSelected());
				} else {
					selectIndex(rcd.arrayIndex);
				}
			}
		}
		
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if (o == insBtn) {
				insertRecord();
			} else if (o == delBtn) {
				deleteRecords();
			} else if (o == updBtn) {
				updateRecords();
			} else if (o == revBtn) {
				revertRecords();
			}
		}
		
		
		private void insertRecord() {
			try {
				JResultSetEditor rse = new JResultSetEditor(rmd);
				rse.setBorder(BorderFactory.createEmptyBorder(4, 36, 4, 20));
				rse.setOpaque(true);
				rse.setBackground(listProps.getBackground());
				rse.setForeground(listProps.getForeground());
				rse.clear();
				rse.setMaximumSize(new Dimension(rse.getMaximumSize().width, rse.getPreferredSize().height));
				MyStrut rss = new MyStrut(6);
				rss.setOpaque(true);
				rss.setBackground(listProps.getBackground());
				rss.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.cyan));
				JPanel rsw = new JPanel(new BorderLayout());
				rsw.add(rss, BorderLayout.WEST);
				rsw.add(rse, BorderLayout.CENTER);
				rsw.setMaximumSize(new Dimension(rsw.getMaximumSize().width, rsw.getPreferredSize().height));
				DisplayedRecord dr = new DisplayedRecord();
				dr.arrayIndex = -1;
				dr.update = false;
				dr.insert = true;
				dr.panel = rsw;
				dr.strut = rss;
				dr.editor = rse;
				rse.privdata = dr;
				rse.addResultSetEditedListener(new MyResultSetEditedListener(dr));
				rse.addMouseListener(this);
				int idx = getSelectedIndex();
				if (idx < 0) {
					rsa.add(dr);
					rsl.add(rsw);
				} else {
					rsa.add(idx, dr);
					rsl.add(rsw, idx);
				}
				wid.setRecordCountWithoutEvents(wid.getRecordCount()+1);
				recnl.setText(Integer.toString(wid.getRecordCount()));
				renumber();
				rsl.validate();
				center.validate();
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not create a new record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void deleteRecords() {
			try {
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord rcd = ri.next();
					if (rcd.isSelected()) {
						if (!rcd.insert) {
							rcd.editor.deleteStatement(conn, table).executeUpdate();
						}
						rsa.remove(rcd.arrayIndex);
						rsl.remove(rcd.panel);
						wid.setRecordCountWithoutEvents(wid.getRecordCount()-1);
						recnl.setText(Integer.toString(wid.getRecordCount()));
						renumber();
						ri = rsa.iterator();
					}
				}
				rsl.validate();
				center.validate();
				setSelCnt(0);
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not delete a record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void updateRecords() {
			try {
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord rcd = ri.next();
					if (rcd.isSelected() && rcd.update) {
						if (rcd.insert) {
							rcd.editor.insertStatement(conn, table).executeUpdate();
						} else {
							rcd.editor.updateStatement(conn, table).executeUpdate();
						}
						rcd.editor.loadInitValues();
						ResultSet trs = rcd.editor.selectStatement(conn, table).executeQuery();
						if (trs.next()) rcd.editor.loadResultSet(trs);
						rcd.insert = false;
						rcd.update = false;
						rcd.updateBorder();
					}
				}
				rsl.validate();
				center.validate();
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not update or insert a record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void updateAllRecords() {
			try {
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord rcd = ri.next();
					if (rcd.update) {
						if (rcd.insert) {
							rcd.editor.insertStatement(conn, table).executeUpdate();
						} else {
							rcd.editor.updateStatement(conn, table).executeUpdate();
						}
						rcd.editor.loadInitValues();
						ResultSet trs = rcd.editor.selectStatement(conn, table).executeQuery();
						if (trs.next()) rcd.editor.loadResultSet(trs);
						rcd.insert = false;
						rcd.update = false;
						rcd.updateBorder();
					}
				}
				rsl.validate();
				center.validate();
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not update or insert a record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void revertRecords() {
			try {
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord rcd = ri.next();
					if (rcd.isSelected()) {
						if (rcd.insert) {
							rcd.editor.clear();
						} else {
							ResultSet trs = rcd.editor.selectStatement(conn, table).executeQuery();
							if (trs.next()) rcd.editor.loadResultSet(trs);
						}
						rcd.update = false;
						rcd.updateBorder();
					}
				}
				rsl.validate();
				center.validate();
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not revert a record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void revertAllRecords() {
			try {
				Iterator<DisplayedRecord> ri = rsa.iterator();
				while (ri.hasNext()) {
					DisplayedRecord rcd = ri.next();
					if (rcd.insert) {
						rsa.remove(rcd.arrayIndex);
						rsl.remove(rcd.panel);
						wid.setRecordCountWithoutEvents(wid.getRecordCount()-1);
						recnl.setText(Integer.toString(wid.getRecordCount()));
						renumber();
						ri = rsa.iterator();
					} else {
						ResultSet trs = rcd.editor.selectStatement(conn, table).executeQuery();
						if (trs.next()) rcd.editor.loadResultSet(trs);
						rcd.update = false;
						rcd.updateBorder();
					}
				}
				rsl.validate();
				center.validate();
			} catch (SQLException se) {
				JOptionPane.showMessageDialog(this, "Could not revert a record because a database error occurred: "+se.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void selectIndex(int i) {
			Iterator<DisplayedRecord> ri = rsa.iterator();
			while (ri.hasNext()) {
				DisplayedRecord dr = ri.next();
				dr.setScrolled(dr.arrayIndex == i);
				dr.setSelected(dr.arrayIndex == i);
			}
			wid.setCurrentRecordWithoutEvents(i);
			setSelCnt(1);
		}
		
		private void scrollIndex(int i) {
			Iterator<DisplayedRecord> ri = rsa.iterator();
			while (ri.hasNext()) {
				DisplayedRecord dr = ri.next();
				dr.setScrolled(dr.arrayIndex == i);
			}
			wid.setCurrentRecordWithoutEvents(i);
		}
		
		private int getSelectedIndex() {
			Iterator<DisplayedRecord> ri = rsa.iterator();
			while (ri.hasNext()) {
				DisplayedRecord dr = ri.next();
				if (dr.isSelected()) return dr.arrayIndex;
			}
			return -1;
		}
		
		private void setSelCnt(int n) {
			selCnt = n;
			recnsl.setText(Integer.toString(n));
			recss.setVisible(n>0);
			recsl.setVisible(n>0);
			recnsl.setVisible(n>0);
		}
		
		private void renumber() {
			Iterator<DisplayedRecord> ri = rsa.iterator();
			int i = 0;
			while (ri.hasNext()) ri.next().arrayIndex = i++;
		}
		
		private class MyResultSetEditedListener implements ResultSetEditedListener {
			private DisplayedRecord rcd;
			
			public MyResultSetEditedListener(DisplayedRecord rcd) {
				this.rcd = rcd;
			}
			
			public void resultSetEdited(ResultSetEditedEvent e) {
				if (rcd != null) {
					rcd.update = true;
					rcd.updateBorder();
				}
			}
		}
		
		private class DisplayedRecord {
			public int arrayIndex;
			public boolean update;
			public boolean insert;
			public JPanel panel;
			public MyStrut strut;
			public JResultSetEditor editor;
			public Color borderColor() {
				if (insert && update) return Color.green;
				else if (insert) return Color.cyan;
				else if (update) return Color.magenta;
				else return Color.black;
			}
			public void updateBorder() {
				strut.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, borderColor()));
			}
			public boolean isSelected() {
				return editor.getBackground().equals(listProps.getSelectionBackground());
			}
			/*
			public boolean isScrolled() {
				return strut.getBackground().equals(listProps.getSelectionBackground());
			}
			*/
			public void setSelected(boolean s) {
				boolean as = editor.getBackground().equals(listProps.getSelectionBackground());
				setSelCnt((s && !as)?(selCnt+1):(as && !s)?(selCnt-1):selCnt);
				editor.setBackground(s?listProps.getSelectionBackground():listProps.getBackground());
				editor.setForeground(s?listProps.getSelectionForeground():listProps.getForeground());
			}
			public void setScrolled(boolean s) {
				strut.setBackground(s?listProps.getSelectionBackground():listProps.getBackground());
				strut.setForeground(s?listProps.getSelectionForeground():listProps.getForeground());
			}
		}
	}
	
	private static class JAWDBWidget extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
		private static final long serialVersionUID = 1;
		
		private Image base, top, bottom, selector;
		private int curr, num;
		private EventListenerList myListeners = new EventListenerList();
		
		public JAWDBWidget(int curr, int num) {
			base = ResplRsrcs.getPNG(2006);
			top = ResplRsrcs.getPNG(2007);
			bottom = ResplRsrcs.getPNG(2008);
			selector = ResplRsrcs.getPNG(2009);
			this.curr = curr;
			this.num = num;
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}
		
		public int getRecordCount() {
			return num;
		}
		
		public void setRecordCountWithoutEvents(int i) {
			num = i;
			if (curr >= num) curr = num-1;
			repaint();
		}
		
		/*
		public void setRecordCount(int i) {
			num = i;
			if (curr >= num) curr = num-1;
			repaint();
			
			RecordScrollEvent ee = new RecordScrollEvent(this, 1, "setCurrentRecord", i);
			RecordScrollListener[] l = myListeners.getListeners(RecordScrollListener.class);
			for (int j=0; j<l.length; j++) l[j].recordScrolled(ee);
		}
		
		public int getCurrentRecord() {
			return curr;
		}
		*/
		
		public void setCurrentRecordWithoutEvents(int i) {
			if (i < 0) i = 0;
			if (i >= num) i = num-1;
			curr = i;
			repaint();
		}
		
		public void setCurrentRecord(int i) {
			if (i < 0) i = 0;
			if (i >= num) i = num-1;
			curr = i;
			repaint();
			
			RecordScrollEvent ee = new RecordScrollEvent(this, 1, "setCurrentRecord", i);
			RecordScrollListener[] l = myListeners.getListeners(RecordScrollListener.class);
			for (int j=0; j<l.length; j++) l[j].recordScrolled(ee);
		}
		
		protected void paintComponent(Graphics g) {
			Insets i = getInsets();
			int x = i.left;
			int y = i.top;
			Color c = g.getColor();
			if (isOpaque()) {
				g.setColor(new Color(0xFFEEEEEE));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			g.drawImage(base, x, y, this);
			if (curr > 0) g.drawImage(top, x, y, this);
			if (curr < num-1) g.drawImage(bottom, x, y+26, this);
			if (num > 1) {
				g.drawImage(selector, x+33, y+2+(43*curr/(num-1)), this);
			}
			g.setColor(Color.black);
			g.setFont(g.getFont().deriveFont(10.0f));
			String ts = Integer.toString(curr+1);
			FontMetrics fm = g.getFontMetrics();
			int tx = (int)(x+36-fm.getStringBounds(ts,g).getWidth());
			int ty = y+59-(fm.getAscent()+fm.getDescent())/2+fm.getAscent();
			g.drawString(ts, tx, ty);
			g.setColor(c);
		}
		
		public Dimension getMinimumSize() {
			Insets i = getInsets();
			return new Dimension(44+i.left+i.right,66+i.top+i.bottom);
		}
		
		public Dimension getPreferredSize() {
			Insets i = getInsets();
			return new Dimension(44+i.left+i.right,66+i.top+i.bottom);
		}
		
		public Dimension getMaximumSize() {
			Insets i = getInsets();
			return new Dimension(44+i.left+i.right,66+i.top+i.bottom);
		}
		
		//public static final int CLICKED_RECORD_INVALID = -1;
		public static final int CLICKED_RECORD_SPECIFY = -2;
		//public static final int CLICKED_RECORD_PREVIOUS = -3;
		//public static final int CLICKED_RECORD_NEXT = -4;
		public static final int CLICKED_RECORD_SELECTOR = -5;
		
		/*
		public int clickedRegion(Point p) {
			return clickedRegion(p, true);
		}
		*/
		
		public int clickedRegion(Point p, boolean accountForInsets) {
			int x = p.x;
			int y = p.y;
			if (accountForInsets) {
				Insets i = getInsets();
				x -= i.left;
				y -= i.top;
			}
			if (x < 0 || y < 0 || x >= 44 || y >= 66) {
				return -1;
			} else if (y >= 52) {
				return -2;
			} else if (x >= 33) {
				return -5;
			} else if (y >= 26) {
				return -4;
			} else {
				return -3;
			}
		}
		
		/*
		public int clickedRecord(Point p) {
			return clickedRecord(p, true);
		}
		*/
		
		public int clickedRecord(Point p, boolean accountForInsets) {
			int x = p.x;
			int y = p.y;
			if (accountForInsets) {
				Insets i = getInsets();
				x -= i.left;
				y -= i.top;
			}
			if (x < 0 || y < 0 || x >= 44 || y >= 66) {
				return -1;
			} else if (y >= 52) {
				return -2;
			} else if (x >= 33) {
				int i = num * (y-6) / 43;
				if (i < 0) i = 0;
				if (i >= num) i = num-1;
				return i;
			} else if (y >= 26) {
				int i = curr+1;
				if (i >= num) i = num-1;
				return i;
			} else {
				int i = curr-1;
				if (i < 0) i = 0;
				return i;
			}
		}
		
		public void mouseClicked(MouseEvent e) {
			int i = clickedRecord(e.getPoint(), true);
			if (i >= 0) setCurrentRecord(i);
			else if (i == CLICKED_RECORD_SPECIFY) {
				String nn = JOptionPane.showInputDialog(this, "Go to record: (1-"+num+")", Integer.toString(curr+1));
				if (nn != null) try {
					int ii = Integer.parseInt(nn)-1;
					if (ii < 0 || ii >= num) {
						JOptionPane.showMessageDialog(this, "The record number entered was not in the correct range.", "", JOptionPane.ERROR_MESSAGE);
					} else {
						setCurrentRecord(ii);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "The record number entered was not a valid integer.", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		public void mouseDragged(MouseEvent e) {
			if (clickedRegion(e.getPoint(), true) == CLICKED_RECORD_SELECTOR) {
				int i = clickedRecord(e.getPoint(), true);
				if (i >= 0) setCurrentRecord(i);
			}
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				int i = curr + e.getUnitsToScroll();
				if (i < 0) i = 0;
				if (i >= num) i = num-1;
				setCurrentRecord(i);
			} else if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
				int i = curr + e.getWheelRotation()*((num>=20)?(num/20):1);
				if (i < 0) i = 0;
				if (i >= num) i = num-1;
				setCurrentRecord(i);
			}
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
		
		public void addRecordScrollListener(RecordScrollListener l) {
			myListeners.add(RecordScrollListener.class, l);
		}
		
		/*
		public void removeRecordScrollListener(RecordScrollListener l) {
			myListeners.remove(RecordScrollListener.class, l);
		}
		
		public RecordScrollListener[] getRecordScrollListeners() {
			return myListeners.getListeners(RecordScrollListener.class);
		}
		*/
	}
	
	private static class RecordScrollEvent extends ActionEvent {
		private static final long serialVersionUID = 1;
		
		private int r;
		
		public RecordScrollEvent(Object arg0, int arg1, String arg2, int rcd) {
			super(arg0, arg1, arg2);
			r = rcd;
		}

		public RecordScrollEvent(Object arg0, int arg1, String arg2, int arg3, int rcd) {
			super(arg0, arg1, arg2, arg3);
			r = rcd;
		}

		public RecordScrollEvent(Object arg0, int arg1, String arg2, long arg3, int arg4, int rcd) {
			super(arg0, arg1, arg2, arg3, arg4);
			r = rcd;
		}
		
		public int getCurrentRecord() {
			return r;
		}
	}
	
	private static interface RecordScrollListener extends EventListener {
		public void recordScrolled(RecordScrollEvent e);
	}
	
	private static class JResultSetEditor extends JPanel {
		private static final long serialVersionUID = 1;
		
		private Map<String,JComponent> fldNameToComp = new HashMap<String,JComponent>();
		private ArrayList<JComponent> fldNumToComp = new ArrayList<JComponent>();
		private ArrayList<JPanel> fldNumToPanel = new ArrayList<JPanel>();
		private Map<String,Object> initValues;
		private EventListenerList myListeners = new EventListenerList();
		public Object privdata;
		
		private static JLabel makeFixedLabel(String s, int width) {
			JLabel l = new JLabel(s);
			l.setFont(l.getFont().deriveFont(10.0f));
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
		
		public JResultSetEditor(ResultSetMetaData rsmd) throws SQLException {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			for (int i=1; i<=rsmd.getColumnCount(); i++) {
				String name = rsmd.getColumnLabel(i);
				int type = rsmd.getColumnType(i);
				JComponent jc;
				JPanel line;
				switch (type) {
				default:
					jc = new JTextField();
					((JTextField)jc).getDocument().addDocumentListener(new DocumentListener() {
						public void changedUpdate(DocumentEvent e) { triggerResultSetEditedEvent(); }
						public void insertUpdate(DocumentEvent e) { triggerResultSetEditedEvent(); }
						public void removeUpdate(DocumentEvent e) { triggerResultSetEditedEvent(); }
					});
				}
				line = makeLine(name, 100, jc);
				line.setBackground(this.getBackground());
				line.setOpaque(this.isOpaque());
				if (i>1) this.add(Box.createVerticalStrut(4));
				this.add(line);
				fldNameToComp.put(name, jc);
				fldNumToComp.add(jc);
				fldNumToPanel.add(line);
			}
		}
		
		public void setOpaque(boolean isOpaque) {
			super.setOpaque(isOpaque);
			if (fldNumToPanel == null) return;
			Iterator<JPanel> pi = fldNumToPanel.iterator();
			while (pi.hasNext()) pi.next().setOpaque(isOpaque);
		}
		
		public void setBackground(Color bg) {
			super.setBackground(bg);
			if (fldNumToPanel == null) return;
			Iterator<JPanel> pi = fldNumToPanel.iterator();
			while (pi.hasNext()) pi.next().setBackground(bg);
		}
		
		public void setForeground(Color fg) {
			super.setForeground(fg);
			if (fldNumToPanel == null) return;
			Iterator<JPanel> pi = fldNumToPanel.iterator();
			while (pi.hasNext()) pi.next().setForeground(fg);
		}
		
		public void clear() {
			initValues = null;
			Iterator<JComponent> ci = fldNameToComp.values().iterator();
			while (ci.hasNext()) {
				JComponent comp = ci.next();
				if (comp instanceof JTextField) {
					((JTextField)comp).setText("");
				}
			}
		}
		
		public void loadResultSet(ResultSet rs) throws SQLException {
			initValues = new HashMap<String,Object>();
			Iterator<String> fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				JComponent comp = fldNameToComp.get(name);
				initValues.put(name, rs.getObject(name));
				if (comp instanceof JTextField) {
					((JTextField)comp).setText(rs.getString(name));
				}
			}
		}
		
		/*
		public ResultSet updateResultSet(ResultSet rs) throws SQLException {
			Iterator<String> fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				JComponent comp = fldNameToComp.get(name);
				if (comp instanceof JTextField) {
					rs.updateString(name, ((JTextField)comp).getText());
				}
			}
			return rs;
		}
		*/
		
		public void loadInitValues() {
			initValues = new HashMap<String,Object>();
			Iterator<String> fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				JComponent comp = fldNameToComp.get(name);
				if (comp instanceof JTextField) {
					initValues.put(name, ((JTextField)comp).getText());
				}
			}
		}
		
		public PreparedStatement insertStatement(Connection conn, String table) throws SQLException {
			String a = "INSERT INTO "+table+" (";
			String b = "";
			String c = ") VALUES (";
			String d = "";
			String e = ")";
			Iterator<String> fi;
			int i;
			PreparedStatement ps;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				b += ", "+fi.next();
				d += ", ?";
			}
			ps = conn.prepareStatement(a+b.substring(2)+c+d.substring(2)+e, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			i=0;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				JComponent comp = fldNameToComp.get(name);
				if (comp instanceof JTextField) {
					ps.setString(++i, ((JTextField)comp).getText());
				}
			}
			return ps;
		}
		
		public PreparedStatement updateStatement(Connection conn, String table) throws SQLException {
			String a = "UPDATE "+table+" SET ";
			String b = "";
			String c = " WHERE ";
			String d = "";
			String e = " LIMIT 1";
			Iterator<String> fi;
			int i;
			PreparedStatement ps;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				b += ", "+name+"=?";
				d += " AND "+name+"=?";
			}
			ps = conn.prepareStatement(a+b.substring(2)+c+d.substring(5)+e, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			i=0;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				JComponent comp = fldNameToComp.get(name);
				if (comp instanceof JTextField) {
					ps.setString(++i, ((JTextField)comp).getText());
				}
			}
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				ps.setObject(++i, initValues.get(name));
			}
			return ps;
		}
		
		public PreparedStatement selectStatement(Connection conn, String table) throws SQLException {
			String a = "SELECT * FROM "+table+" WHERE ";
			String b = "";
			String c = " LIMIT 1";
			Iterator<String> fi;
			int i;
			PreparedStatement ps;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				b += " AND "+name+"=?";
			}
			ps = conn.prepareStatement(a+b.substring(5)+c, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			i=0;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				ps.setObject(++i, initValues.get(name));
			}
			return ps;
		}
		
		public PreparedStatement deleteStatement(Connection conn, String table) throws SQLException {
			String a = "DELETE FROM "+table+" WHERE ";
			String b = "";
			String c = " LIMIT 1";
			Iterator<String> fi;
			int i;
			PreparedStatement ps;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				b += " AND "+name+"=?";
			}
			ps = conn.prepareStatement(a+b.substring(5)+c, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			i=0;
			fi = fldNameToComp.keySet().iterator();
			while (fi.hasNext()) {
				String name = fi.next();
				ps.setObject(++i, initValues.get(name));
			}
			return ps;
		}
		
		public void addResultSetEditedListener(ResultSetEditedListener l) {
			myListeners.add(ResultSetEditedListener.class, l);
		}
		
		/*
		public void removeResultSetEditedListener(ResultSetEditedListener l) {
			myListeners.remove(ResultSetEditedListener.class, l);
		}
		
		public ResultSetEditedListener[] getResultSetEditedListeners() {
			return myListeners.getListeners(ResultSetEditedListener.class);
		}
		*/
		
		protected void triggerResultSetEditedEvent() {
			ResultSetEditedEvent ee = new ResultSetEditedEvent(this, 1, "triggerResultSetEditedEvent");
			ResultSetEditedListener[] l = myListeners.getListeners(ResultSetEditedListener.class);
			for (int j=0; j<l.length; j++) l[j].resultSetEdited(ee);
		}
	}
	
	private static class ResultSetEditedEvent extends ActionEvent {
		private static final long serialVersionUID = 1;
		
		public ResultSetEditedEvent(Object arg0, int arg1, String arg2) {
			super(arg0, arg1, arg2);
		}

		public ResultSetEditedEvent(Object arg0, int arg1, String arg2, int arg3) {
			super(arg0, arg1, arg2, arg3);
		}

		public ResultSetEditedEvent(Object arg0, int arg1, String arg2, long arg3, int arg4) {
			super(arg0, arg1, arg2, arg3, arg4);
		}
	}
	
	private static interface ResultSetEditedListener extends EventListener {
		public void resultSetEdited(ResultSetEditedEvent e);
	}
	
	private static class JMySeparator extends JComponent {
		private static final long serialVersionUID = 1;
		
		protected void paintComponent(Graphics g) {
			Insets i = getInsets();
			Color c = g.getColor();
			if (isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(c);
			}
			g.setColor(new Color(0xFF555555));
			g.drawLine(i.left, i.top, getWidth()-i.right-1, i.top);
			g.setColor(new Color(0xFFFFFFFF));
			g.drawLine(i.left, i.top+1, getWidth()-i.right-1, i.top+1);
			g.setColor(c);
		}
		
		public Dimension getMinimumSize() {
			Insets i = getInsets();
			return new Dimension(1+i.left+i.right,2+i.top+i.bottom);
		}
		
		public Dimension getPreferredSize() {
			Insets i = getInsets();
			return new Dimension(40+i.left+i.right,2+i.top+i.bottom);
		}
		
		public Dimension getMaximumSize() {
			Insets i = getInsets();
			return new Dimension(32000,2+i.top+i.bottom);
		}
	}
	
	private static class MyStrut extends JPanel {
		private static final long serialVersionUID = 1;
		private int ww;
		public MyStrut(int w) {
			ww = w;
		}
		public Dimension getMinimumSize() {
			return new Dimension(ww, super.getMinimumSize().height);
		}
		public Dimension getPreferredSize() {
			return new Dimension(ww, super.getPreferredSize().height);
		}
		public Dimension getMaximumSize() {
			return new Dimension(ww, super.getMaximumSize().height);
		}
	}
}
