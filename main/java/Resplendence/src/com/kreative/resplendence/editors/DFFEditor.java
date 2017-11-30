package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.infobox.*;
import com.kreative.resplendence.menus.ResplendenceKeystrokeAdapter;
import com.kreative.resplendence.misc.IDConflictDialog;
import com.kreative.swing.*;

public class DFFEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("FILE", "DFF");
	}

	public String name() {
		return "DFF Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WDFFTypePicker(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			try {
				new DFFResourceArray(ro.getData());
				return PREFERRED_EDITOR;
			} catch (Exception ioe) {
				return DOES_NOT_RECOGNIZE;
			}
		} else if (ro.getType().equals(ResplendenceObject.TYPE_DATABASE_TABLE)) {
			try {
				new DFFResourceDatabase((Connection)ro.getProvider(), ro.getTitleForExportedFile());
				return PREFERRED_EDITOR;
			} catch (Exception ee) {
				return DOES_NOT_RECOGNIZE;
			}
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "DFF";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("FILE", "DFF"));
	}
	
	public static class WDFFTypePicker extends ResplendenceEditorWindow implements ActionListener {
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
		private DFFResourceProvider rp;
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
		
		public WDFFTypePicker(ResplendenceObject ro) {
			super(ro, true);
			setTitle(ro.getTitleForWindows());
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_IMPORT_EXPORT |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_REFRESH |
					ResplMain.MENUS_NEW_ITEM |
					ResplMain.MENUS_OPEN_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_ARRANGE_BY_NAME |
					ResplMain.MENUS_ARRANGE_BY_NUM |
					ResplMain.MENUS_ARRANGE_BY_KIND |
					ResplMain.MENUS_LIST_THUMB_VIEW |
					ResplMain.MENUS_SAVE_REVERT |
					ResplMain.MENUS_CUT_COPY_PASTE
			);
			if (ro.isDataType()) {
				try {
					rp = new DFFResourceArray(ro.getData());
				} catch (IOException ioe) {
					try {
						rp = new DFFResourceArray(3);
					} catch (IOException ioe2) {}
				}
			} else if (ro.getType().equals(ResplendenceObject.TYPE_DATABASE_TABLE)) {
				try {
					rp = new DFFResourceDatabase((Connection)ro.getProvider(), ro.getTitleForExportedFile());
				} catch (Exception ee) {
					try {
						rp = new DFFResourceArray(3);
					} catch (IOException ioe2) {}
				}
			}
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
							String d = getSymbDesc((Long)robj.getProperty("type"));
							if (d.length() < 1) {
								return "<html>&nbsp;count: "+Integer.toString(robj.getChildCount())+"&nbsp;</html>";
							} else {
								return "<html>&nbsp;"+d+"&nbsp;<br>&nbsp;count: "+Integer.toString(robj.getChildCount())+"&nbsp;</html>";
							}
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
							WDFFTypePicker.this.resplOpen((ResplendenceObject)thumbModel.get(row));
						}
					}
				}
			});
			ResplendenceKeystrokeAdapter.getInstance().addCutCopyPasteAction(thumbList);
			listModel = new DefaultTableModel(new String[]{"Type", "Count", "Kind"}, 0);
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
							WDFFTypePicker.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
						}
					}
				}
			});
			ResplendenceKeystrokeAdapter.getInstance().addCutCopyPasteAction(listTable);
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
			long[] types = rp.getTypes();
			ResplendenceObject[] things = new ResplendenceObject[types.length];
			for (int i=0; i<types.length; i++) {
				things[i] = new DFFTypeObject(this, rp, types[i]);
			}
			Arrays.sort(things, new MyComparator());
			thumbModel.removeAllElements();
			thumbIcons.clear();
			for (ResplendenceObject ro : things) {
				thumbModel.addElement(ro);
				String s = ro.getTitleForIcons();
				if (s.startsWith("Mac ") || s.startsWith("Mac_")) {
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("RSRC", s.substring(4))));
				} else if (s.startsWith("Palm") || s.startsWith("PRC ") || s.startsWith("PRC_")) {
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("PRC", s.substring(4))));
				} else {
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("DFF", s)));
				}
			}
			while (listModel.getRowCount() > 0) listModel.removeRow(0);
			for (ResplendenceObject ro : things) {
				listModel.addRow(new Object[]{
						ro,
						ro.getChildCount(),
						getSymbDesc((Long)ro.getProperty("type"))
				});
			}
		}
		
		public void save(ResplendenceObject ro) {
			rp.flush();
			if (rp instanceof DFFResourceArray) ro.setData(((DFFResourceArray)rp).getBytes());
		}
		
		public void revert(ResplendenceObject ro) {
			if (ro.isDataType()) {
				try {
					rp = new DFFResourceArray(ro.getData());
				} catch (IOException ioe) {}
			} else if (ro.getType().equals(ResplendenceObject.TYPE_DATABASE_TABLE)) {
				try {
					rp = new DFFResourceDatabase((Connection)ro.getProvider(), ro.getTitleForExportedFile());
				} catch (Exception ee) {}
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
		
		public void actionPerformed(ActionEvent ev) {
			DFFResource r = DFFInfoBox.getInfoBoxFor(ev).getInfo();
			if (r != null) {
				if (r.data == null) r.data = new byte[0];
				try {
					rp.add(r);
					setChangesMade();
					update();
				} catch (DFFResourceAlreadyExistsException raee) {
					JOptionPane.showMessageDialog(null, "An object of that type and ID already exists.");
				}
			}
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			ResplendenceObject[] ros; Vector<DFFResource> v;
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return getSel();
			case ResplendenceEvent.NEW_ITEM:
				new DFFInfoBox(new DFFResource(KSFLConstants.Data_Bin, rp.getNextAvailableID(KSFLConstants.Data_Bin), new byte[0]), this);
				break;
			case ResplendenceEvent.REMOVE_ITEM:
			case ResplendenceEvent.CLEAR:
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							long type = ((Number)ro.getProperty("type")).longValue();
							int[] ids = rp.getIDs(type);
							for (int id : ids) {
								rp.remove(type, id);
							}
						}
					}
				}
				update();
				break;
			case ResplendenceEvent.CUT:
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					v = new Vector<DFFResource>();
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							long type = ((Number)ro.getProperty("type")).longValue();
							int[] ids = rp.getIDs(type);
							for (int id : ids) {
								v.add(rp.get(type, id));
								rp.remove(type, id);
							}
						}
					}
					ResplScrap.setScrap(v);
				}
				update();
				break;
			case ResplendenceEvent.COPY:
				ros = getSel();
				if (ros != null) {
					v = new Vector<DFFResource>();
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							long type = ((Number)ro.getProperty("type")).longValue();
							int[] ids = rp.getIDs(type);
							for (int id : ids) {
								v.add(rp.get(type, id));
							}
						}
					}
					ResplScrap.setScrap(v);
				}
				break;
			case ResplendenceEvent.PASTE:
			case ResplendenceEvent.PASTE_AFTER:
				Collection<DFFResource> pasted = ResplScrap.getScrapDFFResources(rp);
				int pastemode = IDConflictDialog.SKIP;
				for (DFFResource dfo : pasted) {
					if (rp.contains(dfo.type, dfo.id)) {
						pastemode = IDConflictDialog.showIDConflictDialog();
						break;
					}
				}
				if (pastemode != IDConflictDialog.CANCEL) {
					setChangesMade();
					for (DFFResource dfo : pasted) {
						try {
							rp.add(dfo);
						} catch (DFFResourceAlreadyExistsException dfoaex) {
							switch (pastemode) {
							case IDConflictDialog.OVERWRITE:
								rp.remove(dfo.type, dfo.id);
								try { rp.add(dfo); } catch (DFFResourceAlreadyExistsException dfoaexx) { dfoaexx.printStackTrace(); }
								break;
							case IDConflictDialog.RENUMBER:
								dfo.id = rp.getNextAvailableID(dfo.type, dfo.id);
								try { rp.add(dfo); } catch (DFFResourceAlreadyExistsException dfoaexx) { dfoaexx.printStackTrace(); }
								break;
							}
						}
					}
					update();
				}
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
			case ResplendenceEvent.IMPORT_FILE:
				File in = e.getFile();
				if (in != null) {
					DFFResource r = importObject(rp, KSFLConstants.Data_Bin, rp.getNextAvailableID(KSFLConstants.Data_Bin), in);
					if (r != null) new DFFInfoBox(r, this);
				}
				break;
			case ResplendenceEvent.EXPORT_FILE:
				File out = e.getFile();
				if (out != null) {
					if (out.exists() && out.isFile()) out.delete();
					if (!out.exists()) out.mkdir();
					ros = getSel();
					if (ros != null) {
						for (ResplendenceObject ro : ros) {
							if (ro.getProperty("type") instanceof Number) {
								long type = ((Number)ro.getProperty("type")).longValue();
								File tout = new File(out, KSFLUtilities.eccs(type));
								if (tout.exists() && tout.isFile()) tout.delete();
								if (!tout.exists()) tout.mkdir();
								int[] ids = rp.getIDs(type);
								for (int id : ids) {
									DFFResource r = rp.get(type, id);
									String name = Integer.toString(r.id);
									if (r.name != null && r.name.length() > 0) {
										name += " - " + r.name;
									}
									File rout = new File(tout, name);
									exportObject(r, rout);
								}
							}
						}
					}
				}
				break;
			}
			return null;
		}
		
		private Map<Long,String> symbDescMap = new HashMap<Long,String>();
		private String getSymbDesc(long type) {
			if (symbDescMap.containsKey(type)) {
				return symbDescMap.get(type);
			} else {
				String d;
				int firstHalf = (int)((type >>> 32) & 0xFFFFFFFFl);
				if (firstHalf == 0x4D616320 || firstHalf == 0x4D61635F) {
					d = ResplRsrcs.getSymbolDescription("ResType#", 0, (int)(type & 0xFFFFFFFF));
				} else if (firstHalf == 0x50524320 || firstHalf == 0x5052435F || firstHalf == 0x50616C6D) {
					d = ResplRsrcs.getSymbolDescription("PRCType#", 0, (int)(type & 0xFFFFFFFF));
				} else {
					d = ResplRsrcs.getSymbolDescription("DFFType#", 0, type);
				}
				symbDescMap.put(type, d);
				return d;
			}
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
				} else if (arrangeBy.equals("kind")) {
					String l0 = getSymbDesc((Long)arg0.getProperty("type"));
					String l1 = getSymbDesc((Long)arg1.getProperty("type"));
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
	
	private static class DFFTypeObject extends ResplendenceObject {
		private WDFFTypePicker orig;
		private DFFResourceProvider rp;
		private long type;
		
		public DFFTypeObject(WDFFTypePicker orig, DFFResourceProvider rp, long type) {
			this.orig = orig;
			this.rp = rp;
			this.type = type;
		}

		@Override
		public boolean addChild(ResplendenceObject rn) {
			orig.setChangesMade();
			if (rn.isDataType()) {
				DFFResource nr = new DFFResource(type, rp.getNextAvailableID(type), rn.getTitleForExportedFile(), rn.getData());
				if (rn.getProperty("id") instanceof Number) nr.id = ((Number)rn.getProperty("id")).intValue();
				if (rn.getProperty("datatype") instanceof Number) nr.datatype = ((Number)rn.getProperty("datatype")).shortValue();
				if (rn.getProperty("name") != null) nr.name = rn.getProperty("name").toString();
				if (rn.getProperty("attributes") instanceof Number) nr.setAttributes(((Number)rn.getProperty("attributes")).shortValue());
				try {
					return rp.add(nr);
				} catch (Exception e) {}
			}
			return false;
		}

		@Override
		public ResplendenceObject getChild(int i) {
			return new DFFResourceObject(orig, rp, type, rp.getID(type, i));
		}

		@Override
		public int getChildCount() {
			return rp.getResourceCount(type);
		}

		@Override
		public ResplendenceObject[] getChildren() {
			int[] ids = rp.getIDs(type);
			ResplendenceObject[] objs = new ResplendenceObject[ids.length];
			for (int i=0; i<ids.length; i++) {
				objs[i] = new DFFResourceObject(orig, rp, type, ids[i]);
			}
			return objs;
		}

		@Override
		public byte[] getData() {
			return null;
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public String getTitleForIcons() {
			return KSFLUtilities.eccs(type);
		}

		@Override
		public String getTitleForExportedFile() {
			return KSFLUtilities.eccs(type);
		}

		@Override
		public Object getProperty(String key) {
			if (key.equals("type")) {
				return type;
			} else {
				return null;
			}
		}

		@Override
		public Object getProvider() {
			return rp;
		}

		@Override
		public RandomAccessFile getRandomAccessData(String mode) {
			return null;
		}

		@Override
		public long getSize() {
			return 0;
		}

		@Override
		public String getType() {
			return TYPE_DFF_TYPE;
		}

		@Override
		public String getTitleForWindowMenu() {
			return KSFLUtilities.eccs(type)+"s";
		}

		@Override
		public String getTitleForWindows() {
			return KSFLUtilities.eccs(type)+"s from "+orig.getTitle();
		}
		
		@Override
		public String getUDTI() {
			String s = KSFLUtilities.eccs(type);
			if (s.startsWith("Mac ") || s.startsWith("Mac_")) {
				return s.substring(4);
			} else if (s.startsWith("Palm") || s.startsWith("PRC ") || s.startsWith("PRC_")) {
				return "PRC "+s.substring(4);
			} else {
				return s;
			}
		}

		@Override
		public RWCFile getWorkingCopy() {
			return null;
		}

		@Override
		public boolean isContainerType() {
			return true;
		}

		@Override
		public boolean isDataType() {
			return false;
		}

		@Override
		public ResplendenceObject removeChild(int i) {
			orig.setChangesMade();
			int id = rp.getID(type, i);
			if (rp.remove(type, id)) {
				return new DFFResourceObject(orig, rp, type, id);
			} else {
				return null;
			}
		}

		@Override
		public ResplendenceObject removeChild(ResplendenceObject ro) {
			orig.setChangesMade();
			if (ro.getProperty("type").equals(type) && ro.getProperty("id") instanceof Number) {
				if (rp.remove(type, ((Number)ro.getProperty("id")).intValue())) {
					return ro;
				}
			}
			return null;
		}

		@Override
		public boolean replaceChild(int i, ResplendenceObject rn) {
			orig.setChangesMade();
			return (removeChild(i) != null) && addChild(rn);
		}

		@Override
		public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
			orig.setChangesMade();
			return (removeChild(ro) != null) && addChild(rn);
		}

		@Override
		public boolean setData(byte[] data) {
			return false;
		}

		@Override
		public boolean setProperty(String key, Object value) {
			return false;
		}
	}
	
	private static class DFFResourceObject extends ResplendenceObject {
		private WDFFTypePicker orig;
		private DFFResourceProvider rp;
		private long type;
		private int id;
		
		public DFFResourceObject(WDFFTypePicker orig, DFFResourceProvider rp, long type, int id) {
			this.orig = orig;
			this.rp = rp;
			this.type = type;
			this.id = id;
		}
		
		@Override
		public boolean addChild(ResplendenceObject rn) {
			return false;
		}

		@Override
		public ResplendenceObject getChild(int i) {
			return null;
		}

		@Override
		public int getChildCount() {
			return 0;
		}

		@Override
		public ResplendenceObject[] getChildren() {
			return null;
		}

		@Override
		public byte[] getData() {
			return rp.getData(type, id);
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public String getTitleForIcons() {
			return Integer.toString(id);
		}

		@Override
		public String getTitleForExportedFile() {
			String s = Integer.toString(id);
			String n = rp.getNameFromID(type, id);
			if (n != null && n.length() > 0) {
				return s + " - " + n;
			} else {
				return s;
			}
		}

		@Override
		public Object getProperty(String key) {
			if (key.equals("type")) {
				return type;
			} else if (key.equals("id") || key.equals("number")) {
				return id;
			} else if (key.equals("datatype") || key.equals("appuse")) {
				return rp.get(type, id).datatype;
			} else if (key.equals("name")) {
				try {
					return rp.getNameFromID(type, id);
				} catch (UnsupportedOperationException uoe) {
					return null;
				}
			} else if (key.equals("attributes")) {
				return rp.get(type, id).getAttributes();
			} else if (key.equals("multilingual")) {
				return rp.get(type, id).multilingual;
			} else if (key.equals("compressed")) {
				return rp.get(type, id).compressed;
			} else if (key.equals("fixed")) {
				return rp.get(type, id).fixed;
			} else if (key.equals("preload")) {
				return rp.get(type, id).preload;
			} else if (key.equals("disabled")) {
				return rp.get(type, id).disabled;
			} else if (key.equals("protect") || key.equals("protected")) {
				return rp.get(type, id).protect;
			} else if (key.equals("locked") || key.equals("readonly")) {
				return rp.get(type, id).readonly;
			} else if (key.equals("purgable") || key.equals("purgeable")) {
				return rp.get(type, id).purgeable;
			} else if (key.equals("system") || key.equals("sysheap")) {
				return rp.get(type, id).system;
			} else if (key.equals("hidden") || key.equals("invisible")) {
				return rp.get(type, id).invisible;
			} else if (key.equals("fromfile")) {
				return rp.get(type, id).fromfile;
			} else if (key.equals("fromrsrc") || key.equals("fromresource")) {
				return rp.get(type, id).fromrsrc;
			} else if (key.equals("appuse1")) {
				return rp.get(type, id).appuse1;
			} else if (key.equals("appuse2")) {
				return rp.get(type, id).appuse2;
			} else if (key.equals("appuse3")) {
				return rp.get(type, id).appuse3;
			} else if (key.equals("appuse4")) {
				return rp.get(type, id).appuse4;
			} else {
				return null;
			}
		}

		@Override
		public Object getProvider() {
			return rp;
		}

		@Override
		public RandomAccessFile getRandomAccessData(String mode) {
			return null;
		}

		@Override
		public long getSize() {
			return rp.getLength(type, id);
		}

		@Override
		public String getType() {
			return TYPE_DFF_OBJECT;
		}

		@Override
		public String getTitleForWindowMenu() {
			String s = Integer.toString(id);
			String n = rp.getNameFromID(type, id);
			if (n != null && n.length() > 0) {
				return KSFLUtilities.eccs(type)+" "+s + " \"" + n + "\"";
			} else {
				return KSFLUtilities.eccs(type)+" "+s;
			}
		}

		@Override
		public String getTitleForWindows() {
			String s = Integer.toString(id);
			String n = rp.getNameFromID(type, id);
			if (n != null && n.length() > 0) {
				return KSFLUtilities.eccs(type)+" "+s+" \""+n+"\""+" from "+orig.getTitle();
			} else {
				return KSFLUtilities.eccs(type)+" "+s+" from "+orig.getTitle();
			}
		}
		
		@Override
		public String getUDTI() {
			String s = KSFLUtilities.eccs(type);
			if (s.startsWith("Mac ") || s.startsWith("Mac_")) {
				return s.substring(4);
			} else if (s.startsWith("Palm") || s.startsWith("PRC ") || s.startsWith("PRC_")) {
				return "PRC "+s.substring(4);
			} else {
				return s;
			}
		}

		@Override
		public RWCFile getWorkingCopy() {
			return null;
		}

		@Override
		public boolean isContainerType() {
			return false;
		}

		@Override
		public boolean isDataType() {
			return true;
		}

		@Override
		public ResplendenceObject removeChild(int i) {
			return null;
		}

		@Override
		public ResplendenceObject removeChild(ResplendenceObject ro) {
			return null;
		}

		@Override
		public boolean replaceChild(int i, ResplendenceObject rn) {
			return false;
		}

		@Override
		public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
			return false;
		}

		@Override
		public boolean setData(byte[] data) {
			orig.setChangesMade();
			return rp.setData(type, id, data);
		}

		@Override
		public boolean setProperty(String key, Object value) {
			orig.setChangesMade();
			try {
				DFFResource r = rp.get(type, id);
				if (key.equals("type")) {
					if (value instanceof Number) {
						r.type = ((Number)value).longValue();
					} else {
						r.type = KSFLUtilities.ecc(value.toString());
					}
					return rp.setAttributes(type, id, r);
				} else if (key.equals("id") || key.equals("number")) {
					if (value instanceof Number) {
						r.id = ((Number)value).intValue();
						return rp.setAttributes(type, id, r);
					}
				} else if (key.equals("appuse") || key.equals("datatype")) {
					if (value instanceof Number) {
						r.datatype = ((Number)value).shortValue();
						return rp.setAttributes(type, id, r);
					}
				} else if (key.equals("name")) {
					r.name = value.toString();
					return rp.setAttributes(type, id, r);
				} else if (key.equals("attributes")) {
					if (value instanceof Number) {
						r.setAttributes(((Number)value).shortValue());
						return rp.setAttributes(type, id, r);
					}
				} else if (key.equals("multilingual")) {
					r.multilingual = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("compressed")) {
					r.compressed = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("fixed")) {
					r.fixed = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("preload")) {
					r.preload = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("disabled")) {
					r.disabled = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("locked") || key.equals("readonly")) {
					r.readonly = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("protect") || key.equals("protected")) {
					r.protect = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("purgable") || key.equals("purgeable")) {
					r.purgeable = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("system") || key.equals("sysheap")) {
					r.system = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("hidden") || key.equals("invisible")) {
					r.invisible = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("fromfile")) {
					r.fromfile = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("fromrsrc") || key.equals("fromresource")) {
					r.fromrsrc = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("appuse1")) {
					r.appuse1 = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("appuse2")) {
					r.appuse2 = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("appuse3")) {
					r.appuse3 = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				} else if (key.equals("appuse4")) {
					r.appuse4 = (value instanceof Boolean && (Boolean)value);
					return rp.setAttributes(type, id, r);
				}
			} catch (Exception e) {}
			return false;
		}
	}
	
	protected static DFFResource importObject(DFFResourceProvider rp, long deftype, int defid, File in) {
		try {
			DFFResource r = new DFFResource(deftype, rp.getNextAvailableID(deftype, defid), in.getName(), new byte[0]);
			RandomAccessFile raf = new RandomAccessFile(in, "r");
			r.data = new byte[(int)Math.min(raf.length(), Integer.MAX_VALUE)];
			raf.read(r.data);
			raf.close();
			File attr = new File(in.getParentFile(), in.getName()+".nfo");
			if (attr.exists() && attr.isFile()) try {
				Scanner sc = new Scanner(attr);
				if (sc.hasNextLine() && sc.nextLine().equals("-- DFF Info --")) {
					if (sc.hasNextLine()) {
						r.type = KSFLUtilities.ecc(sc.nextLine());
					}
					if (sc.hasNextLine()) {
						r.id = rp.getNextAvailableID(r.type, Integer.parseInt(sc.nextLine()));
					}
					if (sc.hasNextLine()) {
						r.datatype = (short)Integer.parseInt(sc.nextLine());
					}
					if (sc.hasNextLine()) {
						r.name = sc.nextLine();
					}
					while (sc.hasNextLine()) {
						String s = sc.nextLine();
						if (s.equalsIgnoreCase("readonly")) r.readonly = true;
						if (s.equalsIgnoreCase("system")) r.system = true;
						if (s.equalsIgnoreCase("preload")) r.preload = true;
						if (s.equalsIgnoreCase("purgeable")) r.purgeable = true;
						if (s.equalsIgnoreCase("fromfile")) r.fromfile = true;
						if (s.equalsIgnoreCase("fromrsrc")) r.fromrsrc = true;
						if (s.equalsIgnoreCase("invisible")) r.invisible = true;
						if (s.equalsIgnoreCase("disabled")) r.disabled = true;
						if (s.equalsIgnoreCase("protected")) r.protect = true;
						if (s.equalsIgnoreCase("fixed")) r.fixed = true;
						if (s.equalsIgnoreCase("multilingual")) r.multilingual = true;
						if (s.equalsIgnoreCase("compressed")) r.compressed = true;
						if (s.equalsIgnoreCase("appuse1")) r.appuse1 = true;
						if (s.equalsIgnoreCase("appuse2")) r.appuse2 = true;
						if (s.equalsIgnoreCase("appuse3")) r.appuse3 = true;
						if (s.equalsIgnoreCase("appuse4")) r.appuse4 = true;
					}
				}
				sc.close();
			} catch (Exception ex) {}
			return r;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Could not import "+in.getName()+".");
			return null;
		}
	}
	
	protected static void exportObject(DFFResource r, File rout) {
		try {
			RandomAccessFile raf = new RandomAccessFile(rout, "rwd");
			raf.seek(0);
			raf.setLength(0);
			raf.write(r.data);
			raf.close();
			File aout = new File(rout.getParentFile(), rout.getName()+".nfo");
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(aout));
				pw.println("-- DFF Info --");
				pw.println(KSFLUtilities.eccs(r.type));
				pw.println(Integer.toString(r.id));
				pw.println(Short.toString(r.datatype));
				pw.println(r.name);
				if (r.readonly) pw.println("readonly");
				if (r.system) pw.println("system");
				if (r.preload) pw.println("preload");
				if (r.purgeable) pw.println("purgeable");
				if (r.fromfile) pw.println("fromfile");
				if (r.fromrsrc) pw.println("fromrsrc");
				if (r.invisible) pw.println("invisible");
				if (r.disabled) pw.println("disabled");
				if (r.protect) pw.println("protected");
				if (r.fixed) pw.println("fixed");
				if (r.multilingual) pw.println("multilingual");
				if (r.compressed) pw.println("compressed");
				if (r.appuse1) pw.println("appuse1");
				if (r.appuse2) pw.println("appuse2");
				if (r.appuse3) pw.println("appuse3");
				if (r.appuse4) pw.println("appuse4");
				pw.close();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Could not export attributes for "+KSFLUtilities.eccs(r.type)+" #"+Integer.toString(r.id)+".");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Could not export "+KSFLUtilities.eccs(r.type)+" #"+Integer.toString(r.id)+".");
		}
	}
}
