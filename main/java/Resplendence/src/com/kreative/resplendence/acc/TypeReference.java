package com.kreative.resplendence.acc;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.util.VectorMap;

public class TypeReference implements AccessoryWindow {
	private static WTypeReference[] instance = new WTypeReference[6];
	
	public String category(int i) {
		return "Type Reference";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		switch (i) {
		case 0: return "Resource Type Reference";
		case 1: return "DFF Type Reference";
		case 2: return "PRC Type Reference";
		case 3: return "PNG Chunk Type Reference";
		case 4: return "MIDI Chunk Type Reference";
		case 5: return "HyperCard Chunk Type Reference";
		default: return null;
		}
	}

	public void open(int i) {
		if (instance[i] == null) {
			switch (i) {
			case 0:
				instance[i] = new WTypeReference("Resource", KSFLConstants.ResType$, 0, 4, "RSRC");
				break;
			case 1:
				instance[i] = new WTypeReference("DFF", KSFLConstants.DFFType$, 0, 8, "DFF");
				break;
			case 2:
				instance[i] = new WTypeReference("PRC", KSFLConstants.PRCType$, 0, 4, "PRC");
				break;
			case 3:
				instance[i] = new WTypeReference("PNG Chunk", KSFLConstants.ChunkTp$, "PNG", 4, "PNG");
				break;
			case 4:
				instance[i] = new WTypeReference("MIDI Chunk", KSFLConstants.ChunkTp$, "MIDI", 4, "MIDI");
				break;
			case 5:
				instance[i] = new WTypeReference("HyperCard Chunk", KSFLConstants.ChunkTp$, "HyperCard", 4, "STAK");
				break;
			}
		}
		instance[i].setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance[i].setVisible(true);
	}
	
	public int numberOfWindows() {
		return 6;
	}
	
	private static class WTypeReference extends JFrame {
		private static final long serialVersionUID = 1;
		private static final Font mono = new Font("Monospaced", Font.PLAIN, 12);
		private int tlen_;
		private String namespace_;
		
		public WTypeReference(String type, long dtype, int did, int tlen, String namespace) {
			super(type+" Type Reference");
			ResplMain.registerWindow(this);
			
			VectorMap<? extends Number,String> stuff;
			switch (tlen) {
			case 4: stuff = ResplRsrcs.getSymbolReference(dtype, did, (int)0); break;
			case 8: stuff = ResplRsrcs.getSymbolReference(dtype, did, (long)0L); break;
			default: throw new IllegalArgumentException();
			}
			Object[] typa = stuff.keySet().toArray();
			Object[] da = stuff.values().toArray();
			Object[][] junk = new Object[typa.length][3];
			for (int i=0; i<typa.length; i++) {
				junk[i][0] = typa[i];
				junk[i][1] = typa[i];
				junk[i][2] = da[i];
			}
			tlen_ = tlen;
			namespace_ = namespace;
			JTable t = new JRefTable(junk, new Object[] { "", "Type", "Description" });
			JLabel l = new JLabel(typa.length+" types");
			l.setFont(l.getFont().deriveFont(10.0f));
			l.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
			this.add(l, BorderLayout.PAGE_END);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.pack();
			this.setSize(500,360);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
		public WTypeReference(String type, long dtype, String dname, int tlen, String namespace) {
			super(type+" Type Reference");
			ResplMain.registerWindow(this);
			
			VectorMap<? extends Number,String> stuff;
			switch (tlen) {
			case 4: stuff = ResplRsrcs.getSymbolReference(dtype, dname, (int)0); break;
			case 8: stuff = ResplRsrcs.getSymbolReference(dtype, dname, (long)0L); break;
			default: throw new IllegalArgumentException();
			}
			Object[] typa = stuff.keySet().toArray();
			Object[] da = stuff.values().toArray();
			Object[][] junk = new Object[typa.length][3];
			for (int i=0; i<typa.length; i++) {
				junk[i][0] = typa[i];
				junk[i][1] = typa[i];
				junk[i][2] = da[i];
			}
			tlen_ = tlen;
			namespace_ = namespace;
			JTable t = new JRefTable(junk, new Object[] { "", "Type", "Description" });
			JLabel l = new JLabel(typa.length+" types");
			l.setFont(l.getFont().deriveFont(10.0f));
			l.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
			this.add(l, BorderLayout.PAGE_END);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.pack();
			this.setSize(500,360);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
		private class JRefTable extends JTable {
			private static final long serialVersionUID = 1;
			public JRefTable(Object[][] table, Object[] header) {
				super(table, header);
				setColumnSelectionAllowed(false);
				setRowSelectionAllowed(true);
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setIntercellSpacing(new Dimension(0,0));
				setRowHeight(36);
				getColumnModel().getColumn(0).setMaxWidth(38);
				getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());
				getColumnModel().getColumn(1).setMaxWidth(tlen_*8+16);
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
				c.setFont(mono);
				if (c instanceof JLabel) {
					((JLabel)c).setAlignmentX(JLabel.CENTER_ALIGNMENT);
					((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
					((JLabel)c).setHorizontalTextPosition(JLabel.CENTER);
					switch (tlen_) {
					case 4: ((JLabel)c).setText(KSFLUtilities.fccs(((Number)arg1).intValue())); break;
					case 8: ((JLabel)c).setText(KSFLUtilities.eccs(((Number)arg1).longValue())); break;
					}
				}
				return c;
			}
		}

		private Map<Number,Image> iconMap = new HashMap<Number,Image>();
		private class IconCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1;
			public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
				Component c = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				if (c instanceof JLabel) {
					((JLabel)c).setAlignmentX(JLabel.CENTER_ALIGNMENT);
					((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
					((JLabel)c).setHorizontalTextPosition(JLabel.CENTER);
					((JLabel)c).setText(null);
					if (iconMap.containsKey(arg1)) {
						((JLabel)c).setIcon(new ImageIcon(iconMap.get(arg1)));
					} else {
						String s="";
						switch (tlen_) {
						case 4: s=(KSFLUtilities.fccs(((Number)arg1).intValue())); break;
						case 8: s=(KSFLUtilities.eccs(((Number)arg1).longValue())); break;
						}
						Image i = ResplRsrcs.getPNG(getToolkit(), namespace_, s);
						iconMap.put((Number)arg1, i);
						((JLabel)c).setIcon(new ImageIcon(i));
					}
				}
				return c;
			}
		}
	}
}
