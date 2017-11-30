package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.pickers.*;
import com.kreative.swing.*;

public class GenericPickerWindow implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("RSRC", "PICK");
	}

	public String name() {
		return "Generic Picker Window";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WGenericPickerWindow(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isContainerType()) {
			return ResplendenceEditor.DEFAULT_EDITOR;
		} else {
			return ResplendenceEditor.DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Picker";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC", "PICK"));
	}
	
	public static class WGenericPickerWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1L;
		private static final TableCellRenderer myRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof ResplendenceObject) {
					return super.getTableCellRendererComponent(table, ((ResplendenceObject)value).getTitleForWindowMenu(), isSelected, hasFocus, row, column);
				} else {
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			}
		};
		private ResplendencePicker pick;
		private JIconList thumbList;
		private JScrollPane thumbScroll;
		private DefaultListModel thumbModel;
		private Map<Object,Icon> thumbIcons;
		private JTable listTable;
		private JScrollPane listScroll;
		private DefaultTableModel listModel;
		private JPanel main;
		private CardLayout layout;
		private String arrangeBy = "id";
		public WGenericPickerWindow(ResplendenceObject ro) {
			super(ro, false);
			setTitle(ro.getTitleForWindows());
			pick = ResplMain.getPicker(ro);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_REFRESH |
					ResplMain.MENUS_OPEN_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_ARRANGE_BY_NAME |
					ResplMain.MENUS_ARRANGE_BY_SIZE |
					((pick != null)?ResplMain.MENUS_LIST_THUMB_VIEW:0)
			);
			if (pick == null) {
				thumbModel = null;
				thumbIcons = null;
				thumbList = null;
				thumbScroll = null;
			} else {
				thumbModel = new DefaultListModel();
				thumbIcons = new HashMap<Object,Icon>();
				thumbList = new JIconList(thumbModel, thumbIcons);
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
								WGenericPickerWindow.this.resplOpen((ResplendenceObject)thumbModel.get(row));
							}
						}
					}
				});
			}
			listModel = new DefaultTableModel(new String[]{"Name", "Size"}, 0);
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
							WGenericPickerWindow.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
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
			if (thumbModel != null && thumbIcons != null && pick != null) {
				thumbModel.removeAllElements();
				thumbIcons.clear();
				for (ResplendenceObject ro : things) {
					thumbModel.addElement(ro);
					thumbIcons.put(ro, new ImageIcon(pick.imageFor(ro)));
				}
			}
			if (listModel != null) {
				while (listModel.getRowCount() > 0) listModel.removeRow(0);
				for (ResplendenceObject ro : things) {
					listModel.addRow(new Object[]{
							ro,
							ResplUtils.describeSize(ro.getSize())
					});
				}
			}
		}
		
		private ResplendenceObject[] getSel() {
			if (listScroll != null && listScroll.isVisible() && listTable != null) {
				int[] rows = listTable.getSelectedRows();
				ResplendenceObject[] ros = new ResplendenceObject[rows.length];
				for (int i=0; i<rows.length && i<ros.length; i++) {
					ros[i] = (ResplendenceObject)listModel.getValueAt(rows[i], 0);
				}
				return ros;
			} else if (thumbScroll != null && thumbScroll.isVisible() && thumbList != null) {
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
			case ResplendenceEvent.REMOVE_ITEM:
				getParentEditor().setChangesMade();
				ResplendenceObject[] ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (getResplendenceObject().removeChild(ro) == null) {
							JOptionPane.showMessageDialog(null, "Could not remove "+ro.getTitleForWindowMenu()+".", "Remove Item", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				update();
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
			}
			return null;
		}
		
		private class MyComparator implements Comparator<ResplendenceObject> {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(ResplendenceObject arg0, ResplendenceObject arg1) {
				if (arrangeBy.equals("size")) {
					Long l0 = arg0.getSize();
					Long l1 = arg1.getSize();
					return l0.compareTo(l1);
				} else if (arrangeBy.equals("name")) {
					String l0 = arg0.getTitleForWindowMenu();
					String l1 = arg1.getTitleForWindowMenu();
					return l0.compareToIgnoreCase(l1);
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
