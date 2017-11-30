package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.kreative.resplendence.ResplMain;

public class SystemProperties implements AccessoryWindow {
	private WSystemProperties instance;
	
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "System Properties";
	}

	public void open(int i) {
		if (instance == null) instance = new WSystemProperties();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WSystemProperties extends JFrame {
		private static final long serialVersionUID = 1;
		//private static final Font mono = new Font("Monospaced", Font.PLAIN, 12);
		private static final int NUM_NON_PROPERTIES = 2;
		
		private JTable t;
		
		public WSystemProperties() {
			super("System Properties");
			ResplMain.registerWindow(this);
			Properties p = System.getProperties();
			Object[][] junk = new Object[p.size()+NUM_NON_PROPERTIES][2];
			Iterator<Map.Entry<Object,Object>> ps = p.entrySet().iterator();
			for (int i=NUM_NON_PROPERTIES; i<junk.length && ps.hasNext(); i++) {
				Map.Entry<Object,Object> en = ps.next();
				junk[i][0] = en.getKey();
				junk[i][1] = en.getValue();
			}
			junk[0][0] = "InetAddress.getLocalHost().getHostAddress()"; junk[0][1] = "";
			junk[1][0] = "InetAddress.getLocalHost().getHostName()"; junk[1][1] = "";
			try {
				junk[0][1] = InetAddress.getLocalHost().getHostAddress();
				junk[1][1] = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException uhe) {}
			t = new ReadOnlyJTable(new DefaultTableModel(junk, new Object[] { "Name", "Value" }));
			//t.setFont(mono);
			t.setColumnSelectionAllowed(false);
			t.setRowSelectionAllowed(true);
			t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			t.setIntercellSpacing(new Dimension(0,0));
			t.getTableHeader().addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e) {
					sortAllRowsBy((DefaultTableModel)t.getModel(), t.getTableHeader().columnAtPoint(e.getPoint()), true);
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});
			sortAllRowsBy((DefaultTableModel)t.getModel(), 0, true);
			JLabel l = new JLabel((junk.length-NUM_NON_PROPERTIES)+" properties, "+NUM_NON_PROPERTIES+" other things");
			l.setFont(l.getFont().deriveFont(10.0f));
			l.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
			this.add(l, BorderLayout.PAGE_END);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(600,300));
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		private void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
	        Vector<?> data = model.getDataVector();
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
