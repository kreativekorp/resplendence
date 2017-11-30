package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.kreative.ksfl.*;
import com.kreative.rsrc.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.infobox.ResourceInfoBox;
import com.kreative.resplendence.menus.ResplendenceKeystrokeAdapter;
import com.kreative.resplendence.misc.IDConflictDialog;
import com.kreative.resplendence.pickers.*;
import com.kreative.swing.*;

public class MacOSResourcesPickerWindow implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("RSRC", "PICK");
	}

	public String name() {
		return "Mac OS Resources Picker Window";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WMacOSResourcesPickerWindow(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isContainerType() && ro.getType().equals(ResplendenceObject.TYPE_MAC_RESOURCE_TYPE)) {
			return ResplendenceEditor.SPECIALTY_DEFAULT_EDITOR;
		} else {
			return ResplendenceEditor.DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Res Picker";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC", "PICK"));
	}
	
	public static class WMacOSResourcesPickerWindow extends ResplendenceEditorWindow implements ActionListener {
		private static final long serialVersionUID = 1L;
		private static final TableCellRenderer myRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof ResplendenceObject) {
					ResplendenceObject ro = (ResplendenceObject)value;
					if (ro.getType().equals(ResplendenceObject.TYPE_MAC_RESOURCE_OBJECT)) {
						switch (column) {
						case 0:
							return super.getTableCellRendererComponent(table, ro.getProperty("id"), isSelected, hasFocus, row, column);
						case 1:
							return super.getTableCellRendererComponent(table, ro.getProperty("name"), isSelected, hasFocus, row, column);
						}
					}
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
		public WMacOSResourcesPickerWindow(ResplendenceObject ro) {
			super(ro, false);
			setTitle(ro.getTitleForWindows());
			pick = ResplMain.getPicker(ro);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_IMPORT_EXPORT |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_REFRESH |
					ResplMain.MENUS_NEW_ITEM |
					ResplMain.MENUS_DUPLICATE_ITEM |
					ResplMain.MENUS_OPEN_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_ITEM_INFO |
					ResplMain.MENUS_ARRANGE_BY_NAME |
					ResplMain.MENUS_ARRANGE_BY_NUM |
					ResplMain.MENUS_ARRANGE_BY_SIZE |
					ResplMain.MENUS_CUT_COPY_PASTE |
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
				thumbList = new JIconList(thumbModel, thumbIcons){
					private static final long serialVersionUID = 1L;
					public String getToolTipText(MouseEvent me) {
						int index = locationToIndex(me.getPoint());
						if (index >= 0) {
							Object obj = getModel().getElementAt(index);
							if (obj instanceof ResplendenceObject) {
								ResplendenceObject robj = (ResplendenceObject)obj;
								String d = robj.getProperty("name").toString();
								if (d.length() < 1) {
									return "<html>&nbsp;size: "+ResplUtils.describeSize(robj.getSize())+"&nbsp;</html>";
								} else {
									return "<html>&nbsp;"+d+"&nbsp;<br>&nbsp;size: "+ResplUtils.describeSize(robj.getSize())+"&nbsp;</html>";
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
								WMacOSResourcesPickerWindow.this.resplOpen((ResplendenceObject)thumbModel.get(row));
							}
						}
					}
				});
				ResplendenceKeystrokeAdapter.getInstance().addCutCopyPasteAction(thumbList);
			}
			listModel = new DefaultTableModel(new String[]{"ID", "Name", "Size"}, 0);
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
							WMacOSResourcesPickerWindow.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
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
							ro, ro,
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
		
		private Set<ResourceInfoBox> editedSet = new HashSet<ResourceInfoBox>();
		public void actionPerformed(ActionEvent ev) {
			ResourceInfoBox rib = ResourceInfoBox.getInfoBoxFor(ev);
			if (rib != null) {
				MacResource r = rib.getInfo();
				if (r != null) {
					getParentEditor().setChangesMade();
					if (editedSet.contains(rib) && r.data != null && r.data.length == 6) {
						int type = KSFLUtilities.getInt(r.data, 0);
						short id = KSFLUtilities.getShort(r.data, 4);
						try {
							((MacResourceProvider)getResplendenceObject().getProvider()).setAttributes(type, id, r);
							update();
						} catch (MacResourceAlreadyExistsException raee) {
							JOptionPane.showMessageDialog(null, "A resource of that type and ID already exists.");
						}
					} else {
						if (r.data == null) r.data = new byte[0];
						try {
							((MacResourceProvider)getResplendenceObject().getProvider()).add(r);
							update();
						} catch (MacResourceAlreadyExistsException raee) {
							JOptionPane.showMessageDialog(null, "A resource of that type and ID already exists.");
						}
					}
				}
				editedSet.remove(rib);
			}
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			ResplendenceObject[] ros; Vector<MacResource> v;
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return getSel();
			case ResplendenceEvent.NEW_ITEM:
				try {
					MacResourceProvider rp = ((MacResourceProvider)getResplendenceObject().getProvider());
					int type = ((Number)getResplendenceObject().getProperty("type")).intValue();
					short id = rp.getNextAvailableID(type);
					new ResourceInfoBox(new MacResource(type, id, new byte[0]), this);
				} catch (Exception ex) {
					new ResourceInfoBox(this);
				}
				break;
			case ResplendenceEvent.DUPLICATE_ITEM:
				getParentEditor().setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (
								ro.getProperty("type") instanceof Number
								&& ro.getProperty("id") instanceof Number
								&& ro.getProvider() instanceof MacResourceProvider
						) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short id = ((Number)ro.getProperty("id")).shortValue();
							try {
								MacResourceProvider rp = ((MacResourceProvider)getResplendenceObject().getProvider());
								MacResource r = ((MacResourceProvider)ro.getProvider()).get(type, id).deepCopy();
								r.id = rp.getNextAvailableID(type, id);
								rp.add(r);
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "Could not duplicate "+KSFLUtilities.fccs(type)+" "+id+".");
							}
						}
					}
				}
				update();
				break;
			case ResplendenceEvent.REMOVE_ITEM:
			case ResplendenceEvent.CLEAR:
				getParentEditor().setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (
								ro.getProperty("type") instanceof Number
								&& ro.getProperty("id") instanceof Number
								&& ro.getProvider() instanceof MacResourceProvider
						) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short id = ((Number)ro.getProperty("id")).shortValue();
							((MacResourceProvider)ro.getProvider()).remove(type, id);
						}
					}
				}
				update();
				break;
			case ResplendenceEvent.ITEM_INFO:
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (
								ro.getProperty("type") instanceof Number
								&& ro.getProperty("id") instanceof Number
								&& ro.getProvider() instanceof MacResourceProvider
						) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short id = ((Number)ro.getProperty("id")).shortValue();
							MacResource r = ((MacResourceProvider)ro.getProvider()).get(type, id).deepCopy();
							r.data = new byte[6];
							KSFLUtilities.putInt(r.data, 0, type);
							KSFLUtilities.putShort(r.data, 4, id);
							editedSet.add(new ResourceInfoBox(r, this));
						}
					}
				}
				break;
			case ResplendenceEvent.CUT:
				getParentEditor().setChangesMade();
				ros = getSel();
				if (ros != null) {
					v = new Vector<MacResource>();
					for (ResplendenceObject ro : ros) {
						if (
								ro.getProperty("type") instanceof Number
								&& ro.getProperty("id") instanceof Number
								&& ro.getProvider() instanceof MacResourceProvider
						) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short id = ((Number)ro.getProperty("id")).shortValue();
							v.add(((MacResourceProvider)ro.getProvider()).get(type, id));
							((MacResourceProvider)ro.getProvider()).remove(type, id);
						}
					}
					ResplScrap.setScrap(v);
				}
				update();
				break;
			case ResplendenceEvent.COPY:
				ros = getSel();
				if (ros != null) {
					v = new Vector<MacResource>();
					for (ResplendenceObject ro : ros) {
						if (
								ro.getProperty("type") instanceof Number
								&& ro.getProperty("id") instanceof Number
								&& ro.getProvider() instanceof MacResourceProvider
						) {
							int type = ((Number)ro.getProperty("type")).intValue();
							short id = ((Number)ro.getProperty("id")).shortValue();
							v.add(((MacResourceProvider)ro.getProvider()).get(type, id));
						}
					}
					ResplScrap.setScrap(v);
				}
				break;
			case ResplendenceEvent.PASTE:
			case ResplendenceEvent.PASTE_AFTER:
			{
				MacResourceProvider rp = ((MacResourceProvider)getResplendenceObject().getProvider());
				int mytype = (Integer)getResplendenceObject().getProperty("type");
				Collection<MacResource> pasted = ResplScrap.getScrapResources(rp);
				int pastemode = IDConflictDialog.SKIP;
				for (MacResource res : pasted) {
					if (res.type == mytype && rp.contains(res.type, res.id)) {
						pastemode = IDConflictDialog.showIDConflictDialog();
						break;
					}
				}
				if (pastemode != IDConflictDialog.CANCEL) {
					getParentEditor().setChangesMade();
					for (MacResource res : pasted) {
						if (res.type == mytype) {
							try {
								rp.add(res);
							} catch (MacResourceAlreadyExistsException resaex) {
								switch (pastemode) {
								case IDConflictDialog.OVERWRITE:
									rp.remove(res.type, res.id);
									try { rp.add(res); } catch (MacResourceAlreadyExistsException resaexx) { resaexx.printStackTrace(); }
									break;
								case IDConflictDialog.RENUMBER:
									res.id = rp.getNextAvailableID(res.type, res.id);
									try { rp.add(res); } catch (MacResourceAlreadyExistsException resaexx) { resaexx.printStackTrace(); }
									break;
								}
							}
						}
					}
					update();
				}
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
					MacResourceProvider rp = ((MacResourceProvider)getResplendenceObject().getProvider());
					int type = ((Number)getResplendenceObject().getProperty("type")).intValue();
					MacResource r = MacOSResourcesEditor.importResource(rp, type, rp.getNextAvailableID(type), in);
					if (r != null) new ResourceInfoBox(r, this);
				}
				break;
			case ResplendenceEvent.EXPORT_FILE:
				File out = e.getFile();
				if (out != null) {
					ros = getSel();
					if (ros != null) {
						if (ros.length == 1) {
							ResplendenceObject ro = ros[0];
							if (
									ro.getProperty("type") instanceof Number
									&& ro.getProperty("id") instanceof Number
									&& ro.getProvider() instanceof MacResourceProvider
							) {
								int type = ((Number)ro.getProperty("type")).intValue();
								short id = ((Number)ro.getProperty("id")).shortValue();
								MacResource r = ((MacResourceProvider)ro.getProvider()).get(type, id);
								MacOSResourcesEditor.exportResource(r, out);
							}
						} else {
							if (out.exists() && out.isFile()) out.delete();
							if (!out.exists()) out.mkdir();
							for (ResplendenceObject ro : ros) {
								if (
										ro.getProperty("type") instanceof Number
										&& ro.getProperty("id") instanceof Number
										&& ro.getProvider() instanceof MacResourceProvider
								) {
									int type = ((Number)ro.getProperty("type")).intValue();
									short id = ((Number)ro.getProperty("id")).shortValue();
									MacResource r = ((MacResourceProvider)ro.getProvider()).get(type, id);
									String name = Short.toString(r.id);
									if (r.name != null && r.name.length() > 0) {
										name += " - " + r.name;
									}
									File rout = new File(out, name);
									MacOSResourcesEditor.exportResource(r, rout);
								}
							}
						}
					}
				}
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
					String l0 = arg0.getProperty("name").toString();
					String l1 = arg1.getProperty("name").toString();
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
