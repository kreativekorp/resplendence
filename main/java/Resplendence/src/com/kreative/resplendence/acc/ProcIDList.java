package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;

import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.swing.JPalette;

public class ProcIDList implements AccessoryWindow {
	private static WProcIDList cdefinstance = null;
	private static WProcIDList wdefinstance = null;
	
	public String category(int i) {
		return "ProcID List";
	}
	
	public KeyStroke keystroke(int i) {
		switch (i) {
		case 0: return KeyStroke.getKeyStroke('C', ResplMain.META_ALT_MASK);
		case 1: return KeyStroke.getKeyStroke('W', ResplMain.META_ALT_MASK);
		default: return null;
		}
	}

	public String name(int i) {
		switch (i) {
		case 0: return "Control ProcID List";
		case 1: return "Window ProcID List";
		default: return null;
		}
	}

	public void open(int i) {
		switch (i) {
		case 0: 
			if (cdefinstance == null) cdefinstance = new WProcIDList("Control", 1, ResplendenceEvent.GET_CONTROL_PROCID, ResplendenceEvent.SET_CONTROL_PROCID, "CDEF");
			cdefinstance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			cdefinstance.setVisible(true);
			break;
		case 1:
			if (wdefinstance == null) wdefinstance = new WProcIDList("Window", 0, ResplendenceEvent.GET_WINDOW_PROCID, ResplendenceEvent.SET_WINDOW_PROCID, "WDEF");
			wdefinstance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			wdefinstance.setVisible(true);
			break;
		}
	}
	
	public int numberOfWindows() {
		return 2;
	}
	
	private static class WProcIDList extends JPalette implements WindowListener {
		private static final long serialVersionUID = 1;
		private boolean eventexec = false;
		private int geventID_;
		private int eventID_;
		private String namespace_;
		private JTable t;
		
		public WProcIDList(String type, int prcIDLstID, int geventID, int eventID, String namespace) {
			super(type+" ProcIDs");
			ArrayList<Integer> procids = new ArrayList<Integer>();
			ArrayList<String> ds = new ArrayList<String>();
			byte[] stuff = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.PrcIDLst, prcIDLstID);
			int sp = 0;
			while (sp < stuff.length) {
				procids.add((int)KSFLUtilities.getShort(stuff, sp)); sp += 2;
				ds.add(KSFLUtilities.getPString(stuff, sp)); sp += (stuff[sp] & 0xFF)+1;
			}
			Object[] procida = procids.toArray();
			Object[] da = ds.toArray();
			Object[][] junk = new Object[procida.length][3];
			for (int i=0; i<procida.length; i++) {
				junk[i][0] = procida[i];
				junk[i][1] = procida[i];
				junk[i][2] = da[i];
			}
			geventID_ = geventID;
			eventID_ = eventID;
			namespace_ = namespace;
			t = new JxDEFTable(junk, new Object[] { "", "ID", "Description" });
			ListSelectionListener tlsl = new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!(eventexec || e.getValueIsAdjusting())) {
						Object pid = t.getModel().getValueAt(t.getSelectedRow(), 1);
						ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), eventID_, "WProcIDList", pid));
					}
				}
			};
			t.getSelectionModel().addListSelectionListener(tlsl);
			t.getColumnModel().getSelectionModel().addListSelectionListener(tlsl);
			this.setContentPane(new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.pack();
			this.setSize(380,196);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			ResplMain.addWindowListener(this);
			updateSelection(this);
		}
		
		public void dispose() {
			ResplMain.removeWindowListener(this);
			super.dispose();
		}
		
		private Map<Integer,Image> imageMap = new HashMap<Integer,Image>();
		private Map<Integer,Image> iconMap = new HashMap<Integer,Image>();
		private Image getProcIDIcon(int pid, ImageObserver io) {
			if (iconMap.containsKey(pid)) {
				return iconMap.get(pid);
			} else {
				Image i;
				if (imageMap.containsKey(pid/16)) {
					i = imageMap.get(pid/16);
				} else {
					i = ResplRsrcs.getPNG(namespace_, ""+(pid/16));
					while (!this.getToolkit().prepareImage(i, -1, -1, null));
					imageMap.put(pid/16, i);
				}
				int iw = i.getWidth(io);
				int ih = i.getHeight(io);
				int x = (16*(pid%4))%iw;
				int y = (16*((pid/4)%4))%ih;
				Image ci = this.getToolkit().createImage(new FilteredImageSource(i.getSource(), new CropImageFilter(x, y, 16, 16)));
				iconMap.put(pid, ci);
				return ci;
			}
		}
		
		private class JxDEFTable extends JTable {
			private static final long serialVersionUID = 1;
			public JxDEFTable(Object[][] table, Object[] header) {
				super(table, header);
				setColumnSelectionAllowed(false);
				setRowSelectionAllowed(true);
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setIntercellSpacing(new Dimension(0,0));
				setRowHeight(20);
				getColumnModel().getColumn(0).setMaxWidth(22);
				getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());
				getColumnModel().getColumn(1).setMaxWidth(50);
				getColumnModel().getColumn(1).setCellRenderer(new CenteredCellRenderer());
			}
			public boolean isCellEditable(int row, int col) {
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
		
		private class IconCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1;
			public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
				Component c = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				if (c instanceof JLabel) {
					((JLabel)c).setAlignmentX(JLabel.CENTER_ALIGNMENT);
					((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
					((JLabel)c).setHorizontalTextPosition(JLabel.CENTER);
					((JLabel)c).setText(null);
					((JLabel)c).setIcon(new ImageIcon(getProcIDIcon((Integer)arg1, c)));
				}
				return c;
			}
		}
		
		public void updateSelection(Object src) {
			eventexec = true;
			Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(src, geventID_, "updateSelection", null));
			if (o instanceof Number) {
				Integer i = ((Number)o).intValue();
				boolean found = false;
				int m = t.getModel().getRowCount();
				for (int j=0; j<m; j++) {
					if (t.getModel().getValueAt(j,0).equals(i)) {
						t.setRowSelectionInterval(j,j);
						found = true;
						break;
					}
				}
				if (!found) {
					t.setRowSelectionInterval(-1,-1);
				}
			}
			eventexec = false;
		}

		public void windowActivated(WindowEvent e) {
			updateSelection(e.getSource());
		}

		public void windowOpened(WindowEvent e) {
			updateSelection(e.getSource());
		}

		public void windowClosed(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
	}
}
