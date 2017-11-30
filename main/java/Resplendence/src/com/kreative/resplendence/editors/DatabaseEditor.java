package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import com.kreative.resplendence.*;
import com.kreative.swing.*;

public class DatabaseEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("DATABASE");
	}

	public String name() {
		return "Database Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WTablePicker(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isContainerType() && ro.getType().equals(ResplendenceObject.TYPE_DATABASE_DATABASE)) {
			return PREFERRED_EDITOR;
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Database";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("DATABASE"));
	}
	
	public static class WTablePicker extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1L;
		private static final TableCellRenderer myRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof ResplendenceObject) {
					return super.getTableCellRendererComponent(table, ((ResplendenceObject)value).getTitleForIcons(), isSelected, hasFocus, row, column);
				} else {
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			}
		};
		private Connection conn;
		private JIconList thumbList;
		private JScrollPane thumbScroll;
		private DefaultListModel thumbModel;
		private Map<Object,Icon> thumbIcons;
		private JTable listTable;
		private JScrollPane listScroll;
		private DefaultTableModel listModel;
		private JPanel main;
		private CardLayout layout;
		private String arrangeBy = "type";
		
		public WTablePicker(ResplendenceObject ro) {
			super(ro, true);
			setTitle(ro.getTitleForWindows());
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_REFRESH |
					//ResplMain.MENUS_NEW_ITEM | // TODO CREATE TABLE
					ResplMain.MENUS_OPEN_ITEM |
					//ResplMain.MENUS_REMOVE_ITEM | // TODO DROP TABLE
					ResplMain.MENUS_ARRANGE_BY_NAME |
					ResplMain.MENUS_ARRANGE_BY_NUM |
					ResplMain.MENUS_LIST_THUMB_VIEW |
					ResplMain.MENUS_SAVE_REVERT
			);
			conn = (Connection)ro.getProvider();
			thumbModel = new DefaultListModel();
			thumbIcons = new HashMap<Object,Icon>();
			thumbList = new JIconList(thumbModel, thumbIcons){
				private static final long serialVersionUID = 1L;
				public String getToolTipText(MouseEvent me) {
					int index = locationToIndex(me.getPoint());
					if (index >= 0) {
						Object obj = getModel().getElementAt(index);
						if (obj instanceof ResplendenceObject) {
							ResplendenceObject robj = (ResplendenceObject)obj;
							return "<html>&nbsp;count: "+Integer.toString(robj.getChildCount())+"&nbsp;</html>";
						}
					}
					return super.getToolTipText(me);
				}
			};
			thumbScroll = new JScrollPane(thumbList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			thumbList.setListAlias(new JListAlias() {
				public Object getListAlias(JList list, Object value, int index) {
					if (value instanceof ResplendenceObject) {
						return ((ResplendenceObject)value).getTitleForIcons();
					} else {
						return value;
					}
				}
			});
			thumbList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ev) {
					if (ev.getClickCount() == 2) {
						int[] rows = thumbList.getSelectedIndices();
						for (int row : rows) {
							WTablePicker.this.resplOpen((ResplendenceObject)thumbModel.get(row));
						}
					}
				}
			});
			listModel = new DefaultTableModel(new String[]{"Type", "Count"}, 0);
			listTable = new JTable(listModel) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int col) {
					return false;
				}
				public TableCellRenderer getCellRenderer(int row, int col) {
					return myRenderer;
				}
			};
			listTable.setRowSelectionAllowed(true);
			listTable.setColumnSelectionAllowed(false);
			listTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ev) {
					if (ev.getClickCount() == 2) {
						int[] rows = listTable.getSelectedRows();
						for (int row : rows) {
							WTablePicker.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
						}
					}
				}
			});
			listScroll = new JScrollPane(listTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			main = new JPanel(layout = new CardLayout());
			if (thumbScroll != null) main.add(thumbScroll, "thumb");
			if (listScroll != null) main.add(listScroll, "list");
			setContentPane(main);
			update();
			pack();
			ResplUtils.sizeWindow(this, 3, 5);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void update() {
			ResplendenceObject[] things = getResplendenceObject().getChildren();
			Arrays.sort(things, new MyComparator());
			thumbModel.removeAllElements();
			thumbIcons.clear();
			for (ResplendenceObject ro : things) {
				thumbModel.addElement(ro);
				thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("DBTABLE", ro.getTitleForIcons())));
			}
			while (listModel.getRowCount() > 0) listModel.removeRow(0);
			for (ResplendenceObject ro : things) {
				listModel.addRow(new Object[]{
						ro,
						ro.getChildCount()
				});
			}
		}
		
		public void save(ResplendenceObject ro) {
			try {
				conn.commit();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Could not commit database.", "", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public void revert(ResplendenceObject ro) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Could not roll back database.", "", JOptionPane.ERROR_MESSAGE);
			}
			update();
		}
		
		private ResplendenceObject[] getSel() {
			if (listScroll.isVisible()) {
				int[] rows = listTable.getSelectedRows();
				ResplendenceObject[] ros = new ResplendenceObject[rows.length];
				for (int i=0; i<rows.length && i<ros.length; i++) {
					ros[i] = (ResplendenceObject)listModel.getValueAt(rows[i], 0);
				}
				return ros;
			} else if (thumbScroll.isVisible()) {
				int[] rows = thumbList.getSelectedIndices();
				ResplendenceObject[] ros = new ResplendenceObject[rows.length];
				for (int i=0; i<rows.length && i<ros.length; i++) {
					ros[i] = (ResplendenceObject)thumbModel.get(rows[i]);
				}
				return ros;
			} else {
				return null;
			}
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return getSel();
			case ResplendenceEvent.NEW_ITEM:
				// TODO CREATE TABLE
				break;
			case ResplendenceEvent.REMOVE_ITEM:
				// TODO DROP TABLE
				break;
			case ResplendenceEvent.REFRESH:
				update();
				break;
			case ResplendenceEvent.LIST_VIEW:
				layout.show(main, "list");
				break;
			case ResplendenceEvent.THUMBNAIL_VIEW:
				layout.show(main, "thumb");
				break;
			case ResplendenceEvent.ARRANGE_BY:
				arrangeBy = e.getString().toLowerCase();
				update();
				break;
			case ResplendenceEvent.SELECT_ALL:
				if (thumbList != null) thumbList.setSelectionInterval(0, thumbModel.getSize());
				if (listTable != null) listTable.selectAll();
				this.repaint();
				break;
			}
			return null;
		}
		
		private class MyComparator implements Comparator<ResplendenceObject> {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(ResplendenceObject arg0, ResplendenceObject arg1) {
				if (arrangeBy.equals("name") || arrangeBy.equals("type")) {
					String l0 = arg0.getTitleForIcons();
					String l1 = arg1.getTitleForIcons();
					return l0.compareToIgnoreCase(l1);
				} else if (arrangeBy.equals("number") || arrangeBy.equals("size")) {
					Integer l0 = arg0.getChildCount();
					Integer l1 = arg1.getChildCount();
					return l0.compareTo(l1);
				} else {
					Object o0 = arg0.getProperty(arrangeBy);
					Object o1 = arg1.getProperty(arrangeBy);
					if (o0 instanceof Comparable && o1 instanceof Comparable) {
						try {
							return ((Comparable)o0).compareTo(o1);
						} catch (Exception e) {
							//could be type safety violation
							return 0;
						}
					} else {
						return 0;
					}
				}
			}
		}
	}
}
