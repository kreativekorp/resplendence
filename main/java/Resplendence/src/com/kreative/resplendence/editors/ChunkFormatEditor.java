package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import com.kreative.cff.*;
import com.kreative.dff.*;
import com.kreative.ksfl.KSFLConstants;
import com.kreative.resplendence.*;
import com.kreative.resplendence.menus.ResplendenceKeystrokeAdapter;
import com.kreative.swing.*;
import com.kreative.util.KArrays;

public class ChunkFormatEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("FILE", "Chunk");
	}

	public String name() {
		return "Chunk Format Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WChunkFormatEditor(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			return REFUSE_TO_EDIT;
			/*
			int rec = REFUSE_TO_EDIT;
			byte[] data = ro.getData();
			//System.out.println("\n\n"+ro.getTitleForWindows().toUpperCase());
			for (ChunkFormat cf : getChunkFormats()) {
				//System.out.println("\nTrying "+cf.name);
				int thisRec = cf.recognizes(data);
				//if (thisRec != DOES_NOT_RECOGNIZE) System.out.println("Recognized by "+cf.name+" "+thisRec); else System.out.println("Not Recognized by "+cf.name);
				if (thisRec > rec) rec = thisRec;
			}
			return rec;
			*/
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Chunk";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("FILE", "Chunk"));
	}
	
	private static Vector<ChunkFormat> formats = null;
	public static Vector<ChunkFormat> getChunkFormats() {
		if (formats == null) {
			formats = new Vector<ChunkFormat>();
			DFFResourceProvider dp = ResplRsrcs.getAppDFFResourceProvider();
			int[] ids = dp.getIDs(KSFLConstants.ChunkFmt);
			for (int id : ids) {
				byte[] stuff = dp.getData(KSFLConstants.ChunkFmt, id);
				if (stuff != null) {
					for (int pos = 0; pos < stuff.length; pos += 0x40) {
						try {
							formats.add(new ChunkFormat(stuff, pos));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return formats;
	}
	
	public static class ChunkFormat {
		public String name;
		public String namespace;
		public ChunkFileSpec format;
		public long magic;
		public int magicRec;
		public int nonmagicRec;
		
		public ChunkFormat(byte[] b, int pos) {
			try {
				name = new String(b, pos+0x01, b[pos+0x00] & 0xFF, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				name = new String(b, pos+0x01, b[pos+0x00] & 0xFF);
			}
			try {
				namespace = new String(b, pos+0x11, b[pos+0x10] & 0xFF, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				namespace = new String(b, pos+0x11, b[pos+0x10] & 0xFF);
			}
			format = new ChunkFileSpec(b, pos+0x20);
			magic = KArrays.getLong(b, pos+0x30, false);
			magicRec = KArrays.getInt(b, pos+0x38, false);
			nonmagicRec = KArrays.getInt(b, pos+0x3C, false);
		}
		
		public int recognizes(byte[] b) {
			try {
				//System.out.println("Calculating Sizes");
				int fhts = 0; long fhtm = 0L;
				int chts = 0; long chtm = 0L;
				for (FieldSpec fs : format.fileHeaderSpec()) {
					if (fs.type() == FieldType.CHARACTER_TYPE) {
						fhts = fs.byteCount();
						fhtm = (1L << fs.bitCount()) - 1L;
					}
				}
				for (FieldSpec fs : format.chunkHeaderSpec()) {
					if (fs.type() == FieldType.CHARACTER_TYPE) {
						chts = fs.byteCount();
						chtm = (1L << fs.bitCount()) - 1L;
					}
				}
				
				//System.out.println("Creating ChunkFile Object");
				boolean magicFound = false;
				boolean magical = (b.length > 0 && b.length >= format.fileHeaderSpec().byteCount());
				ChunkFile cf = format.readChunkHeaders(new DataInputStream(new ByteArrayInputStream(b)));
				
				//System.out.println("Checking Header Readability & Magic");
				Header fh = cf.getHeader();
				if (fh.containsKey(FieldType.CHARACTER_TYPE)) {
					long fts = fh.get(FieldType.CHARACTER_TYPE).longValue();
					if (!magicFound) {
						magicFound = true;
						if (magic != 0 && (fts & fhtm) != (magic & fhtm)) magical = false;
					}
					for (int i = 0; i < fhts; i++, fts >>>= 8) {
						if ((fts & 0xFF) < 0x20 || (fts & 0xFF) >= 0x7F) return REFUSE_TO_EDIT;
					}
				}
				if (!magicFound && fh.containsKey(FieldType.INTEGER_TYPE)) {
					long fts = fh.get(FieldType.INTEGER_TYPE).longValue();
					magicFound = true;
					if (magic != 0 && (fts & fhtm) != (magic & fhtm)) magical = false;
				}
				
				for (Chunk c : cf) {
					//System.out.println("Checking Chunk Readability & Magic");
					Header ch = c.getHeader();
					if (ch.containsKey(FieldType.CHARACTER_TYPE)) {
						long cts = ch.get(FieldType.CHARACTER_TYPE).longValue();
						if (!magicFound) {
							magicFound = true;
							if (magic != 0 && (cts & chtm) != (magic & chtm)) magical = false;
						}
						for (int i = 0; i < chts; i++, cts >>>= 8) {
							if ((cts & 0xFF) < 0x20 || (cts & 0xFF) >= 0x7F) return REFUSE_TO_EDIT;
						}
					}
					if (!magicFound && ch.containsKey(FieldType.INTEGER_TYPE)) {
						long cts = ch.get(FieldType.INTEGER_TYPE).longValue();
						magicFound = true;
						if (magic != 0 && (cts & chtm) != (magic & chtm)) magical = false;
					}
				}
				
				return magical ? magicRec : nonmagicRec;
			} catch (Exception e) {
				return REFUSE_TO_EDIT;
			}
		}
	}
	
	public static class WChunkFormatEditor extends ResplendenceEditorWindow {
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
		
		private ChunkFormat fmt = null;
		private ChunkFileSpec spec = null;
		private ChunkFile chunks = null;
		
		private JIconList thumbList;
		private JScrollPane thumbScroll;
		private DefaultListModel thumbModel;
		private Map<Object,Icon> thumbIcons;
		private JTable listTable;
		private JScrollPane listScroll;
		private DefaultTableModel listModel;
		private JPanel main;
		private CardLayout layout;
		
		public WChunkFormatEditor(ResplendenceObject ro) {
			super(ro, true);
			setTitle(ro.getTitleForWindows());
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT |
					ResplMain.MENUS_CUT_COPY_PASTE |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_REFRESH |
					ResplMain.MENUS_NEW_ITEM |
					ResplMain.MENUS_DUPLICATE_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_ITEM_INFO |
					ResplMain.MENUS_OPEN_ITEM |
					ResplMain.MENUS_REARRANGE |
					ResplMain.MENUS_LIST_THUMB_VIEW
			);
			
			int rec = REFUSE_TO_EDIT;
			byte[] data = ro.getData();
			for (ChunkFormat cf : getChunkFormats()) {
				int thisRec = cf.recognizes(data);
				if (thisRec > rec) {
					rec = thisRec;
					fmt = cf;
					spec = cf.format;
				}
			}
			if (spec != null) {
				try {
					chunks = spec.readChunkFile(new DataInputStream(new ByteArrayInputStream(data)));
				} catch (IOException ioe) {
					chunks = new ChunkFile(spec.fileHeaderSpec().createHeader());
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
							ResplendenceObject ro = (ResplendenceObject)obj;
							String d = ((ro instanceof ChunkHeaderObject) ? "chunk format header" :
								(ro.getProperty("CHARACTER_TYPE") instanceof Number) ?
										getSymbDesc(((Number)ro.getProperty("CHARACTER_TYPE")).longValue()) :
											(ro.getProperty("INTEGER_TYPE") instanceof Number) ?
													getSymbDesc(((Number)ro.getProperty("INTEGER_TYPE")).longValue()) : "");
							if (d.length() < 1) {
								return super.getToolTipText(me);
							} else {
								return "<html>&nbsp;"+d+"&nbsp;</html>";
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
							WChunkFormatEditor.this.resplOpen((ResplendenceObject)thumbModel.get(row));
						}
					}
				}
			});
			ResplendenceKeystrokeAdapter.getInstance().addCutCopyPasteAction(thumbList);
			listModel = new DefaultTableModel(new String[]{"Object", "Kind"}, 0);
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
							WChunkFormatEditor.this.resplOpen((ResplendenceObject)listModel.getValueAt(row, 0));
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
			if (getWidth() < 300) setSize(300, getHeight());
			ResplUtils.sizeWindow(this, 3, 5);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void update() {
			int cts = 0;
			for (FieldSpec fs : spec.chunkHeaderSpec()) {
				if (fs.type() == FieldType.CHARACTER_TYPE) cts = fs.byteCount();
			}
			
			ResplendenceObject[] things = new ResplendenceObject[spec.fileHeaderSpec().isEmpty() ? chunks.size() : (chunks.size()+1)];
			things[0] = new ChunkHeaderObject(this, fmt, chunks);
			Iterator<Chunk> i = chunks.iterator(); int j = (spec.fileHeaderSpec().isEmpty() ? 0 : 1);
			while (i.hasNext() && j < things.length) {
				things[j++] = new ChunkObject(this, fmt, i.next());
			}
			
			thumbModel.removeAllElements();
			thumbIcons.clear();
			for (ResplendenceObject ro : things) {
				thumbModel.addElement(ro);
				if (ro instanceof ChunkHeaderObject) {
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("FILE", "Chunk")));
				} else if (ro.getProperty("CHARACTER_TYPE") instanceof Number) {
					String s = "";
					long ct = ((Number)ro.getProperty("CHARACTER_TYPE")).longValue();
					for (int k = 0; k < cts; k++, ct >>>= 8) {
						s = (char)(ct & 0xFF)+s;
					}
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG(fmt.namespace, s)));
				} else if (ro.getProperty("INTEGER_TYPE") instanceof Number) {
					String s = Long.toHexString(((Number)ro.getProperty("INTEGER_TYPE")).longValue()).toUpperCase();
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG(fmt.namespace, s)));
				} else {
					thumbIcons.put(ro, new ImageIcon(ResplRsrcs.getPNG("RSRC")));
				}
			}
			while (listModel.getRowCount() > 0) listModel.removeRow(0);
			for (ResplendenceObject ro : things) {
				listModel.addRow(new Object[]{
						ro,
						((ro instanceof ChunkHeaderObject) ? "chunk format header" :
							(ro.getProperty("CHARACTER_TYPE") instanceof Number) ?
									getSymbDesc(((Number)ro.getProperty("CHARACTER_TYPE")).longValue()) :
										(ro.getProperty("INTEGER_TYPE") instanceof Number) ?
												getSymbDesc(((Number)ro.getProperty("INTEGER_TYPE")).longValue()) : 0)
				});
			}
		}
		
		public void save(ResplendenceObject ro) {
			if (spec != null) try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				DataOutputStream dout = new DataOutputStream(bout);
				spec.writeChunkFile(dout, chunks);
				ro.setData(bout.toByteArray());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		public void revert(ResplendenceObject ro) {
			if (spec != null) {
				try {
					chunks = spec.readChunkFile(new DataInputStream(new ByteArrayInputStream(ro.getData())));
				} catch (IOException ioe) {
					chunks = new ChunkFile(spec.fileHeaderSpec().createHeader());
				}
				update();
			}
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
			ResplendenceObject[] ros; Vector<Chunk> v;
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return getSel();
			case ResplendenceEvent.CUT:
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					v = new Vector<Chunk>();
					for (ResplendenceObject ro : ros) {
						if (ro instanceof ChunkObject) {
							ChunkObject cho = (ChunkObject)ro;
							v.add(cho.chunk);
							chunks.remove(cho.chunk);
						}
					}
					ResplScrap.setScrap(v);
				}
				update();
				break;
			case ResplendenceEvent.COPY:
				ros = getSel();
				if (ros != null) {
					v = new Vector<Chunk>();
					for (ResplendenceObject ro : ros) {
						if (ro instanceof ChunkObject) {
							ChunkObject cho = (ChunkObject)ro;
							v.add(cho.chunk);
						}
					}
					ResplScrap.setScrap(v);
				}
				break;
			case ResplendenceEvent.PASTE:
			{
				Collection<Chunk> pasted = ResplScrap.getScrapChunks(spec);
				int index = (listScroll.isVisible()) ? listTable.getSelectedRow() : thumbList.getSelectedIndex();
				if (index < 0) index = 0;
				setChangesMade();
				for (Chunk ch : pasted) {
					chunks.add(index, ch);
				}
				update();
			}
				break;
			case ResplendenceEvent.PASTE_AFTER:
			{
				Collection<Chunk> pasted = ResplScrap.getScrapChunks(spec);
				int index = -1;
				for (int idx : (listScroll.isVisible()) ? listTable.getSelectedRows() : thumbList.getSelectedIndices()) {
					if (idx > index) index = idx;
				}
				index++;
				setChangesMade();
				for (Chunk ch : pasted) {
					chunks.add(index, ch);
				}
				update();
			}
				break;
			case ResplendenceEvent.SELECT_ALL:
				if (thumbList != null) thumbList.setSelectionInterval(0, thumbModel.getSize());
				if (listTable != null) listTable.selectAll();
				this.repaint();
				break;
			case ResplendenceEvent.REFRESH:
				update();
				break;
			case ResplendenceEvent.NEW_ITEM:
				// TODO new item
				break;
			case ResplendenceEvent.DUPLICATE_ITEM:
			{
				int index = -1;
				for (int idx : (listScroll.isVisible()) ? listTable.getSelectedRows() : thumbList.getSelectedIndices()) {
					if (idx > index) index = idx;
				}
				index++;
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (ro instanceof ChunkObject) {
							ChunkObject cho = (ChunkObject)ro;
							chunks.add(index++, cho.chunk);
						}
					}
				}
				update();
			}
				break;
			case ResplendenceEvent.REMOVE_ITEM:
			case ResplendenceEvent.CLEAR:
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					for (ResplendenceObject ro : ros) {
						if (ro instanceof ChunkObject) {
							ChunkObject cho = (ChunkObject)ro;
							chunks.remove(cho.chunk);
						}
					}
				}
				update();
				break;
			case ResplendenceEvent.ITEM_INFO:
				// TODO item info
				break;
			case ResplendenceEvent.REARRANGE:
			{
				setChangesMade();
				ros = getSel();
				if (ros != null) {
					int diff = e.getInt();
					if (diff <= Integer.MIN_VALUE) {
						// to beginning
						int index = 0;
						for (ResplendenceObject ro : ros) {
							if (ro instanceof ChunkObject) {
								ChunkObject cho = (ChunkObject)ro;
								chunks.remove(cho.chunk);
								chunks.add(index++, cho.chunk);
							}
						}
					}
					else if (diff < 0) {
						// back one
						int index = (listScroll.isVisible()) ? listTable.getSelectedRow() : thumbList.getSelectedIndex();
						index--; if (index < 0) index = 0;
						for (ResplendenceObject ro : ros) {
							if (ro instanceof ChunkObject) {
								ChunkObject cho = (ChunkObject)ro;
								chunks.remove(cho.chunk);
								chunks.add(index++, cho.chunk);
							}
						}
					}
					else if (diff >= Integer.MAX_VALUE) {
						// to end
						for (ResplendenceObject ro : ros) {
							if (ro instanceof ChunkObject) {
								ChunkObject cho = (ChunkObject)ro;
								chunks.remove(cho.chunk);
								chunks.add(cho.chunk);
							}
						}
					}
					else if (diff > 0) {
						// forward one
						int[] indices = (listScroll.isVisible()) ? listTable.getSelectedRows() : thumbList.getSelectedIndices();
						int max = (listScroll.isVisible()) ? listModel.getRowCount() : thumbModel.size();
						int index = -1;
						for (int idx : indices) {
							if (idx > index) index = idx;
						}
						index += 2;
						if (index > max) index = max;
						for (ResplendenceObject ro : ros) {
							if (ro instanceof ChunkObject) {
								ChunkObject cho = (ChunkObject)ro;
								chunks.remove(cho.chunk); index--;
							}
						}
						for (ResplendenceObject ro : ros) {
							if (ro instanceof ChunkObject) {
								ChunkObject cho = (ChunkObject)ro;
								chunks.add(index++, cho.chunk);
							}
						}
					}
				}
				update();
			}
				break;
			case ResplendenceEvent.LIST_VIEW:
				layout.show(main, "list");
				break;
			case ResplendenceEvent.THUMBNAIL_VIEW:
				layout.show(main, "thumb");
				break;
			}
			return null;
		}
		
		private Map<Long,String> symbDescMap = new HashMap<Long,String>();
		private String getSymbDesc(long type) {
			if (symbDescMap.containsKey(type)) {
				return symbDescMap.get(type);
			} else {
				int size = 0;
				for (FieldSpec fs : spec.chunkHeaderSpec()) {
					if (fs.type() == FieldType.CHARACTER_TYPE) {
						size = fs.size().byteCount();
						break;
					}
					else if (fs.type() == FieldType.INTEGER_TYPE) {
						size = fs.size().byteCount();
						//this line intentionally left break;
					}
				}
				
				String d = "";
				switch (size) {
				case 4:
					d = ResplRsrcs.getSymbolDescription("ChunkTp#", fmt.name, (int)type);
					break;
				case 8:
					d = ResplRsrcs.getSymbolDescription("ChunkTp#", fmt.name, (long)type);
					break;
				}
				symbDescMap.put(type, d);
				return d;
			}
		}
	}
	
	private static class ChunkHeaderObject extends ResplendenceObject {
		private WChunkFormatEditor orig;
		private ChunkFormat fmt;
		private ChunkFile chunkfile;
		
		public ChunkHeaderObject(WChunkFormatEditor orig, ChunkFormat fmt, ChunkFile chunkfile) {
			this.orig = orig;
			this.fmt = fmt;
			this.chunkfile = chunkfile;
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
			return null;
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public Object getProperty(String key) {
			for (FieldType ft : FieldType.values()) {
				if (ft.name().equalsIgnoreCase(key)) return chunkfile.getHeader().get(ft);
			}
			return null;
		}

		@Override
		public Object getProvider() {
			return null;
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
		public String getTitleForExportedFile() {
			return "Header";
		}

		@Override
		public String getTitleForIcons() {
			return "Header";
		}

		@Override
		public String getTitleForWindowMenu() {
			return "Header";
		}

		@Override
		public String getTitleForWindows() {
			return "Header from "+orig.getTitle();
		}

		@Override
		public String getType() {
			return TYPE_CHUNK_HEADER;
		}

		@Override
		public String getUDTI() {
			return ".chunk-"+fmt.name.replaceAll("\\s", "").toLowerCase()+"-header";
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
			return false;
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
			return false;
		}

		@Override
		public boolean setProperty(String key, Object value) {
			if (!(value instanceof Number)) {
				try {
					value = Long.parseLong(value.toString());
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
			if (value instanceof Number) {
				orig.setChangesMade();
				for (FieldType ft : FieldType.values()) {
					if (ft.name().equalsIgnoreCase(key)) {
						chunkfile.getHeader().put(ft, (Number)value);
						return true;
					}
				}
			}
			return false;
		}
	}
	
	private static class ChunkObject extends ResplendenceObject {
		private WChunkFormatEditor orig;
		private ChunkFormat fmt;
		private Chunk chunk;
		
		public ChunkObject(WChunkFormatEditor orig, ChunkFormat fmt, Chunk chunk) {
			this.orig = orig;
			this.fmt = fmt;
			this.chunk = chunk;
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
			return chunk.getData();
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public Object getProperty(String key) {
			for (FieldType ft : FieldType.values()) {
				if (ft.name().equalsIgnoreCase(key)) return chunk.getHeader().get(ft);
			}
			return null;
		}

		@Override
		public Object getProvider() {
			return null;
		}

		@Override
		public RandomAccessFile getRandomAccessData(String mode) {
			return null;
		}

		@Override
		public long getSize() {
			return chunk.getData().length;
		}

		@Override
		public String getTitleForExportedFile() {
			return getTitleForWindowMenu();
		}

		@Override
		public String getTitleForIcons() {
			return getTitleForWindowMenu();
		}

		@Override
		public String getTitleForWindowMenu() {
			String u = "";
			
			int cts = 0;
			for (FieldSpec fs : fmt.format.chunkHeaderSpec()) {
				if (fs.type() == FieldType.CHARACTER_TYPE) cts = fs.byteCount();
			}
			
			Header h = chunk.getHeader();
			if (h.containsKey(FieldType.CHARACTER_TYPE)) {
				String t = "";
				long ct = h.get(FieldType.CHARACTER_TYPE).longValue();
				for (int i = 0; i < cts; i++, ct >>>= 8) {
					t = (char)(ct & 0xFF)+t;
				}
				u += t;
			} else if (h.containsKey(FieldType.INTEGER_TYPE)) {
				u += Long.toHexString(h.get(FieldType.INTEGER_TYPE).longValue()).toUpperCase();
			}
			if (h.containsKey(FieldType.ID_NUMBER)) {
				u += " #"+Long.toString(h.get(FieldType.ID_NUMBER).longValue());
			}
			
			return u;
		}

		@Override
		public String getTitleForWindows() {
			return getTitleForWindowMenu() + " from " + orig.getTitle();
		}

		@Override
		public String getType() {
			return TYPE_CHUNK_OBJECT;
		}

		@Override
		public String getUDTI() {
			String u = ".chunk-"+fmt.name.replaceAll("\\s", "").toLowerCase();
			
			int cts = 0;
			for (FieldSpec fs : fmt.format.chunkHeaderSpec()) {
				if (fs.type() == FieldType.CHARACTER_TYPE) cts = fs.byteCount();
			}
			
			Header h = chunk.getHeader();
			if (h.containsKey(FieldType.CHARACTER_TYPE)) {
				String t = "";
				long ct = h.get(FieldType.CHARACTER_TYPE).longValue();
				for (int i = 0; i < cts; i++, ct >>>= 8) {
					t = (char)(ct & 0xFF)+t;
				}
				u += "-"+t;
			} else if (h.containsKey(FieldType.INTEGER_TYPE)) {
				u += "-"+Long.toHexString(h.get(FieldType.INTEGER_TYPE).longValue()).toUpperCase();
			}
			
			return u;
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
			chunk.setData(data);
			return true;
		}

		@Override
		public boolean setProperty(String key, Object value) {
			if (!(value instanceof Number)) {
				try {
					value = Long.parseLong(value.toString());
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
			if (value instanceof Number) {
				orig.setChangesMade();
				for (FieldType ft : FieldType.values()) {
					if (ft.name().equalsIgnoreCase(key)) {
						chunk.getHeader().put(ft, (Number)value);
						return true;
					}
				}
			}
			return false;
		}
	}
}
