package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.kreative.ksfl.*;
import com.kreative.resplendence.*;

public class FontList implements AccessoryWindow {
	private static WFontList instance;
	
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "Font List";
	}

	public void open(int i) {
		if (instance == null) instance = new WFontList();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WFontList extends JFrame {
		private static final long serialVersionUID = 1;
		
		private ResplendenceListener ll = null;
		private ResplendenceListener myrl;
		private WindowListener mywl;
		private JTable t;
		private boolean eventexec = false;
		
		public WFontList() {
			super("Font List");
			ll = ResplMain.getFrontmostResplendenceListener();
			ResplMain.registerWindow(this, ResplMain.MENUS_FORMAT);
			myrl = new ResplendenceListener(){
				public Object respondToResplendenceEvent(ResplendenceEvent e) {
					switch (e.getID()) {
					case ResplendenceEvent.GET_FONT:
					case ResplendenceEvent.SET_FONT:
					case ResplendenceEvent.GET_COLOR:
					case ResplendenceEvent.SET_COLOR:
						Object r = ResplMain.sendResplendenceEventTo(ll, e);
						updateSelection(e.getSource());
						return r;
					}
					return null;
				}
			};
			ResplMain.addResplendenceListener(myrl, this);
			mywl = new WindowListener(){
				public void windowActivated(WindowEvent e) {
					ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
					if (l != myrl) ll = l;
					updateSelection(e.getSource());
				}
				public void windowOpened(WindowEvent e) {
					ResplendenceListener l = ResplMain.getFrontmostResplendenceListener();
					if (l != myrl) ll = l;
					updateSelection(e.getSource());
				}
				public void windowClosed(WindowEvent e) {}
				public void windowClosing(WindowEvent e) {}
				public void windowDeactivated(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
			};
			ResplMain.addWindowListener(mywl);
			
			byte[] fontcat = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.FontCtlg, 0);
			Map<Integer,String> idToNameMap = new HashMap<Integer,String>();
			Map<String,Integer> nameToIdMap = new HashMap<String,Integer>();
			for (int i=1; i<fontcat.length;) {
				int id = (fontcat[i] << 8) | (fontcat[i+1] & 0xFF);
				int nl = (fontcat[i+2] & 0xFF);
				String n = new String(fontcat, i+3, nl);
				i += 4+nl;
				if (n.length() > 0) {
					if (!idToNameMap.containsKey(id)) idToNameMap.put(id, n);
					if (!nameToIdMap.containsKey(n)) nameToIdMap.put(n, id);
				}
			}
			String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			Object[][] junk = new Object[fonts.length][3];
			for (int i=0; i<fonts.length; i++) {
				junk[i][0] = i;
				junk[i][1] = nameToIdMap.containsKey(fonts[i])?nameToIdMap.get(fonts[i]):"";
				junk[i][2] = fonts[i];
			}
			t = new ReadOnlyJTable(new DefaultTableModel(junk, new Object[] { "Index", "ID#", "Font Name" }));
			t.setColumnSelectionAllowed(false);
			t.setRowSelectionAllowed(true);
			t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			t.setIntercellSpacing(new Dimension(0,0));
			t.setRowHeight(36);
			t.setAutoCreateColumnsFromModel(false);
			t.getColumnModel().getColumn(0).setMaxWidth(40);
			t.getColumnModel().getColumn(0).setCellRenderer(new CenteredCellRenderer());
			t.getColumnModel().getColumn(1).setMaxWidth(70);
			t.getColumnModel().getColumn(1).setCellRenderer(new CenteredCellRenderer());
			t.getColumnModel().getColumn(2).setCellRenderer(new SelfReflectingCellRenderer());
			t.getTableHeader().addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e) {
					sortAllRowsBy((DefaultTableModel)t.getModel(), t.getTableHeader().columnAtPoint(e.getPoint()), true);
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});
			ListSelectionListener tlsl = new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!(eventexec || e.getValueIsAdjusting())) {
						String fname = t.getModel().getValueAt(t.getSelectedRow(),2).toString();
						Object o = ResplMain.sendResplendenceEventTo(ll, new ResplendenceEvent(e.getSource(),ResplendenceEvent.GET_FONT,fname,null));
						if (o instanceof Font) {
							Font f = (Font)o;
							Font nf = new Font(fname, f.getStyle(), f.getSize());
							ResplMain.sendResplendenceEventTo(ll, new ResplendenceEvent(e.getSource(),ResplendenceEvent.SET_FONT,fname,nf));
						}
					}
				}
			};
			t.getSelectionModel().addListSelectionListener(tlsl);
			t.getColumnModel().getSelectionModel().addListSelectionListener(tlsl);
			JLabel l = new JLabel(fonts.length+" fonts");
			l.setFont(l.getFont().deriveFont(10.0f));
			l.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
			this.add(l, BorderLayout.PAGE_END);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(560,500));
			setLocationRelativeTo(null);
			setVisible(true);
			updateSelection(this);
		}
		
		public void dispose() {
			ResplMain.removeResplendenceListener(myrl);
			ResplMain.removeWindowListener(mywl);
			super.dispose();
		}
		
		public void updateSelection(Object src) {
			eventexec = true;
			Object o = ResplMain.sendResplendenceEventTo(ll, new ResplendenceEvent(src,ResplendenceEvent.GET_FONT,"Font List",null));
			if (o instanceof Font) {
				String fname = ((Font)o).getName();
				if (t != null) {
					boolean found = false;
					int m = t.getModel().getRowCount();
					for (int j=0; j<m; j++) {
						if (t.getModel().getValueAt(j,2).equals(fname)) {
							t.setRowSelectionInterval(j,j);
							found = true;
							break;
						}
					}
					if (!found) {
						t.setRowSelectionInterval(-1,-1);
					}
				}
			}
			eventexec = false;
		}
		
		@SuppressWarnings("unchecked")
		private void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
	        Vector<Comparable<?>> data = (Vector<Comparable<?>>)model.getDataVector();
	        Collections.sort(data, new ColumnSorter(colIndex, ascending));
	        model.fireTableStructureChanged();
	    }
		
		private class ReadOnlyJTable extends JTable {
			private static final long serialVersionUID = 1;
			public ReadOnlyJTable(TableModel m) {
				super(m);
			}
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		}
		
		private class CenteredCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1;
			public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
				Component c = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				if (c instanceof JLabel) {
					((JLabel)c).setAlignmentX(JLabel.CENTER_ALIGNMENT);
					((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
					((JLabel)c).setHorizontalTextPosition(JLabel.CENTER);
				}
				return c;
			}
		}
		
		private class SelfReflectingCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1;
			public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
				Component c = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				c.setFont(new Font(arg1.toString(), Font.PLAIN, 24));
				if (c instanceof JComponent) ((JComponent)c).setToolTipText(arg1.toString());
				return c;
			}
		}
		
		@SuppressWarnings("unchecked")
		public class ColumnSorter implements Comparator<Object> {
	        int colIndex;
	        boolean ascending;
	        ColumnSorter(int colIndex, boolean ascending) {
	            this.colIndex = colIndex;
	            this.ascending = ascending;
	        }
			@SuppressWarnings("rawtypes")
			public int compare(Object a, Object b) {
	            Vector<?> v1 = (Vector<?>)a;
	            Vector<?> v2 = (Vector<?>)b;
	            Object o1 = v1.get(colIndex);
	            Object o2 = v2.get(colIndex);
	            // Treat empty strains like nulls
	            if (o1 instanceof String && ((String)o1).length() == 0) {
	                o1 = null;
	            }
	            if (o2 instanceof String && ((String)o2).length() == 0) {
	                o2 = null;
	            }
	            // Sort nulls so they appear last, regardless
	            // of sort order
	            if (o1 == null && o2 == null) {
	                return 0;
	            } else if (o1 == null) {
	                return 1;
	            } else if (o2 == null) {
	                return -1;
	            } else if (o1 instanceof Comparable) {
	                if (ascending) {
	                    return ((Comparable)o1).compareTo(o2);
	                } else {
	                    return ((Comparable)o2).compareTo(o1);
	                }
	            } else {
	                if (ascending) {
	                    return o1.toString().compareTo(o2.toString());
	                } else {
	                    return o2.toString().compareTo(o1.toString());
	                }
	            }
	        }
	    }
	}
}
