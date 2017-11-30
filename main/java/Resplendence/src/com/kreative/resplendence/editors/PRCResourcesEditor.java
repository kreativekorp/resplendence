package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.kreative.ksfl.*;
import com.kreative.prc.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.infobox.PRCInfoBox;
import com.kreative.resplendence.menus.ResplendenceKeystrokeAdapter;
import com.kreative.resplendence.misc.IDConflictDialog;
import com.kreative.swing.*;

public class PRCResourcesEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("FILE", "PRC");
	}

	public String name() {
		return "PRC Resources Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WPRCTypePicker(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			try {
				PalmResourceArray ra = new PalmResourceArray(ro.getData());
				byte[] h = ra.getPRCHeader();
				if (
						h[0x20] != 0
						|| (h[0x21] != 0x01 && h[0x21] != 0x41)
						|| h[0x22] != 0
						|| h[0x23] != 1
				) return DOES_NOT_RECOGNIZE;
				for (int i=0x30; i<0x3C; i++) {
					if (h[i] != 0) return DOES_NOT_RECOGNIZE;
				}
				for (int type : ra.getTypes()) {
					for (short id : ra.getIDs(type)) {
						ra.get(type, id);
					}
				}
				return DECENT_EDITOR;
			} catch (Exception e) {
				return DOES_NOT_RECOGNIZE;
			}
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "PRC";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("FILE", "PRC"));
	}
	
	public static class WPRCTypePicker extends ResplendenceEditorWindow implements ActionListener {
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
		private PalmResourceArray rp;
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
		
		public WPRCTypePicker(ResplendenceObject ro) {
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
			rp = new PalmResourceArray(ro.getData());
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
							if (robj instanceof PRCHeaderObject) {
								return "PRC header";
							} else {
								String d = getSymbDesc((Integer)robj.getProperty("type"));
								if (d != null && d.length() < 1) {
									return "<html>&nbsp;count: "+Integer.toString(robj.getChildCount())+"&nbsp;</html>";
								} else {
									return "<html>&nbsp;"+d+"&nbsp;<br>&nbsp;count: "+Integer.toString(robj.getChildCount())+"&nbsp;</html>";
								}
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
							WPRCTypePicker.this.resplOpen((ResplendenceObject)thumbModel.get(row));
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
							WPRCTypePicker.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
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
			int[] types = rp.getTypes();
			ResplendenceObject[] things = new ResplendenceObject[types.length];
			for (int i=0; i<types.length; i++) {
				things[i] = new PRCTypeObject(this, rp, types[i]);
			}
			Arrays.sort(things, new MyComparator());
			ResplendenceObject headerthing = new PRCHeaderObject(this, rp);
			thumbModel.removeAllElements();
			thumbIcons.clear();
			thumbModel.addElement(headerthing);
			thumbIcons.put(headerthing, new ImageIcon(ResplRsrcs.getPNG("FILE", "PRC")));
			for (ResplendenceObject ro : things) {
				thumbModel.addElement(ro);
				thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("PRC", ro.getTitleForIcons())));
			}
			while (listModel.getRowCount() > 0) listModel.removeRow(0);
			listModel.addRow(new Object[]{
					headerthing,
					0,
					"PRC header"
			});
			for (ResplendenceObject ro : things) {
				listModel.addRow(new Object[]{
						ro,
						ro.getChildCount(),
						getSymbDesc((Integer)ro.getProperty("type"))
				});
			}
		}
		
		public void save(ResplendenceObject ro) {
			ro.setData(rp.getBytes());
		}
		
		public void revert(ResplendenceObject ro) {
			rp = new PalmResourceArray(ro.getData());
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
			PalmResource r = PRCInfoBox.getInfoBoxFor(ev).getInfo();
			if (r != null) {
				if (r.data == null) r.data = new byte[0];
				try {
					rp.add(r);
					setChangesMade();
					update();
				} catch (PalmResourceAlreadyExistsException raee) {
					JOptionPane.showMessageDialog(null, "A resource of that type and ID already exists.");
				}
			}
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			ResplendenceObject[] ros; Vector<PalmResource> v;
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return getSel();
			case ResplendenceEvent.NEW_ITEM:
				new PRCInfoBox(new PalmResource(KSFLConstants.DATA, rp.getNextAvailableID(KSFLConstants.DATA), new byte[0]), this);
				break;
			case ResplendenceEvent.REMOVE_ITEM:
			case ResplendenceEvent.CLEAR:
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short[] ids = rp.getIDs(type);
							for (short id : ids) {
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
					v = new Vector<PalmResource>();
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short[] ids = rp.getIDs(type);
							for (short id : ids) {
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
					v = new Vector<PalmResource>();
					for (ResplendenceObject ro : ros) {
						if (ro.getProperty("type") instanceof Number) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short[] ids = rp.getIDs(type);
							for (short id : ids) {
								v.add(rp.get(type, id));
							}
						}
					}
					ResplScrap.setScrap(v);
				}
				break;
			case ResplendenceEvent.PASTE:
			case ResplendenceEvent.PASTE_AFTER:
				Collection<PalmResource> pasted = ResplScrap.getScrapPalmResources(rp);
				int pastemode = IDConflictDialog.SKIP;
				for (PalmResource res : pasted) {
					if (rp.contains(res.type, res.id)) {
						pastemode = IDConflictDialog.showIDConflictDialog();
						break;
					}
				}
				if (pastemode != IDConflictDialog.CANCEL) {
					setChangesMade();
					for (PalmResource res : pasted) {
						try {
							rp.add(res);
						} catch (PalmResourceAlreadyExistsException resaex) {
							switch (pastemode) {
							case IDConflictDialog.OVERWRITE:
								rp.remove(res.type, res.id);
								try { rp.add(res); } catch (PalmResourceAlreadyExistsException resaexx) { resaexx.printStackTrace(); }
								break;
							case IDConflictDialog.RENUMBER:
								res.id = rp.getNextAvailableID(res.type, res.id);
								try { rp.add(res); } catch (PalmResourceAlreadyExistsException resaexx) { resaexx.printStackTrace(); }
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
					PalmResource r = importResource(rp, KSFLConstants.DATA, rp.getNextAvailableID(KSFLConstants.DATA), in);
					if (r != null) new PRCInfoBox(r, this);
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
								int type = ((Number)ro.getProperty("type")).intValue();
								File tout = new File(out, KSFLUtilities.fccs(type));
								if (tout.exists() && tout.isFile()) tout.delete();
								if (!tout.exists()) tout.mkdir();
								short[] ids = rp.getIDs(type);
								for (short id : ids) {
									PalmResource r = rp.get(type, id);
									String name = Short.toString(r.id);
									File rout = new File(tout, name);
									exportResource(r, rout);
								}
							}
						}
					}
				}
				break;
			}
			return null;
		}
		
		private Map<Integer,String> symbDescMap = new HashMap<Integer,String>();
		private String getSymbDesc(int type) {
			if (symbDescMap.containsKey(type)) {
				return symbDescMap.get(type);
			} else {
				String d = ResplRsrcs.getSymbolDescription("PRCType#", 0, type);
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
					String l0 = getSymbDesc((Integer)arg0.getProperty("type"));
					String l1 = getSymbDesc((Integer)arg1.getProperty("type"));
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
	
	private static class PRCHeaderObject extends ResplendenceObject {
		private WPRCTypePicker orig;
		private PalmResourceProvider rp;
		
		public PRCHeaderObject(WPRCTypePicker orig, PalmResourceProvider rp) {
			this.orig = orig;
			this.rp = rp;
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
			return rp.getPRCHeader();
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public String getTitleForIcons() {
			return "Header";
		}

		@Override
		public String getTitleForExportedFile() {
			return "PRC Header";
		}

		@Override
		public Object getProperty(String key) {
			return null;
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
			return rp.getPRCHeader().length;
		}

		@Override
		public String getType() {
			return ResplendenceObject.TYPE_PRC_HEADER;
		}

		@Override
		public String getTitleForWindowMenu() {
			return "PRC header";
		}

		@Override
		public String getTitleForWindows() {
			return "PRC header from "+orig.getTitle();
		}
		
		@Override
		public String getUDTI() {
			return ".prc-header";
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
			rp.setPRCHeader(data);
			return true;
		}

		@Override
		public boolean setProperty(String key, Object value) {
			return false;
		}
	}
	
	private static class PRCTypeObject extends ResplendenceObject {
		private WPRCTypePicker orig;
		private PalmResourceProvider rp;
		private int type;
		
		public PRCTypeObject(WPRCTypePicker orig, PalmResourceProvider rp, int type) {
			this.orig = orig;
			this.rp = rp;
			this.type = type;
		}

		@Override
		public boolean addChild(ResplendenceObject rn) {
			orig.setChangesMade();
			if (rn.isDataType()) {
				PalmResource nr = new PalmResource(type, rp.getNextAvailableID(type), rn.getData());
				if (rn.getProperty("id") instanceof Number) nr.id = ((Number)rn.getProperty("id")).shortValue();
				try {
					return rp.add(nr);
				} catch (Exception e) {}
			}
			return false;
		}

		@Override
		public ResplendenceObject getChild(int i) {
			return new PRCObject(orig, rp, type, rp.getID(type, i));
		}

		@Override
		public int getChildCount() {
			return rp.getResourceCount(type);
		}

		@Override
		public ResplendenceObject[] getChildren() {
			short[] ids = rp.getIDs(type);
			ResplendenceObject[] objs = new ResplendenceObject[ids.length];
			for (int i=0; i<ids.length; i++) {
				objs[i] = new PRCObject(orig, rp, type, ids[i]);
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
			return KSFLUtilities.fccs(type);
		}

		@Override
		public String getTitleForExportedFile() {
			return KSFLUtilities.fccs(type);
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
			return TYPE_PRC_RESOURCE_TYPE;
		}

		@Override
		public String getTitleForWindowMenu() {
			return KSFLUtilities.fccs(type)+"s";
		}

		@Override
		public String getTitleForWindows() {
			return KSFLUtilities.fccs(type)+"s from "+orig.getTitle();
		}
		
		@Override
		public String getUDTI() {
			return "PRC "+KSFLUtilities.fccs(type);
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
			short id = rp.getID(type, i);
			if (rp.remove(type, id)) {
				return new PRCObject(orig, rp, type, id);
			} else {
				return null;
			}
		}

		@Override
		public ResplendenceObject removeChild(ResplendenceObject ro) {
			orig.setChangesMade();
			if (ro.getProperty("type").equals(type) && ro.getProperty("id") instanceof Number) {
				if (rp.remove(type, ((Number)ro.getProperty("id")).shortValue())) {
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
	
	private static class PRCObject extends ResplendenceObject {
		private WPRCTypePicker orig;
		private PalmResourceProvider rp;
		private int type;
		private short id;
		
		public PRCObject(WPRCTypePicker orig, PalmResourceProvider rp, int type, short id) {
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
			return rp.get(type, id).data;
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public String getTitleForIcons() {
			return Short.toString(id);
		}

		@Override
		public String getTitleForExportedFile() {
			return Short.toString(id);
		}

		@Override
		public Object getProperty(String key) {
			if (key.equals("type")) {
				return type;
			} else if (key.equals("id") || key.equals("number")) {
				return id;
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
			return rp.get(type, id).data.length;
		}

		@Override
		public String getType() {
			return TYPE_PRC_RESOURCE_OBJECT;
		}

		@Override
		public String getTitleForWindowMenu() {
			return KSFLUtilities.fccs(type)+" "+Short.toString(id);
		}

		@Override
		public String getTitleForWindows() {
			return KSFLUtilities.fccs(type)+" "+Short.toString(id)+" from "+orig.getTitle();
		}
		
		@Override
		public String getUDTI() {
			return "PRC "+KSFLUtilities.fccs(type);
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
				PalmResource r = rp.get(type, id);
				if (key.equals("type")) {
					if (value instanceof Number) {
						r.type = ((Number)value).intValue();
					} else {
						String s = value.toString();
						if (s.length() > 4 && (s.startsWith("PRC ") || s.startsWith("PRC_") || s.startsWith("Palm"))) {
							s = s.substring(4);
						}
						r.type = KSFLUtilities.fcc(value.toString());
					}
					return rp.setAttributes(type, id, r);
				} else if (key.equals("id") || key.equals("number")) {
					if (value instanceof Number) {
						r.id = ((Number)value).shortValue();
						return rp.setAttributes(type, id, r);
					}
				}
			} catch (Exception e) {}
			return false;
		}
	}
	
	protected static PalmResource importResource(PalmResourceProvider rp, int deftype, short defid, File in) {
		try {
			PalmResource r = new PalmResource(deftype, rp.getNextAvailableID(deftype, defid), new byte[0]);
			RandomAccessFile raf = new RandomAccessFile(in, "r");
			r.data = new byte[(int)Math.min(raf.length(), Integer.MAX_VALUE)];
			raf.read(r.data);
			raf.close();
			File attr = new File(in.getParentFile(), in.getName()+".nfo");
			if (attr.exists() && attr.isFile()) try {
				Scanner sc = new Scanner(attr);
				if (sc.hasNextLine() && sc.nextLine().equals("-- PRC Info --")) {
					if (sc.hasNextLine()) {
						r.type = KSFLUtilities.fcc(sc.nextLine());
					}
					if (sc.hasNextLine()) {
						r.id = rp.getNextAvailableID(r.type, (short)Integer.parseInt(sc.nextLine()));
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
	
	protected static void exportResource(PalmResource r, File rout) {
		try {
			RandomAccessFile raf = new RandomAccessFile(rout, "rwd");
			raf.seek(0);
			raf.setLength(0);
			raf.write(r.data);
			raf.close();
			File aout = new File(rout.getParentFile(), rout.getName()+".nfo");
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(aout));
				pw.println("-- PRC Info --");
				pw.println(KSFLUtilities.fccs(r.type));
				pw.println(Short.toString(r.id));
				pw.close();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Could not export attributes for "+KSFLUtilities.fccs(r.type)+" #"+Short.toString(r.id)+".");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Could not export "+KSFLUtilities.fccs(r.type)+" #"+Short.toString(r.id)+".");
		}
	}
}
