package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.template.*;
import com.kreative.swing.JHexEditorSuite;
import com.kreative.swing.event.*;

public class TemplateBasedEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("RSRC", "TMPL");
	}

	public String name() {
		return "Template-Based Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return openEditor(ro, ro.getUDTI());
	}
	
	public ResplendenceEditorWindow openEditor(ResplendenceObject ro, String tmplName) {
		if (tmplName == null || !ResplRsrcs.hasTMPL(tmplName)) {
			return new WTemplateChooser(ro);
		} else {
			return new WTemplateEditor(ro, tmplName);
		}
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			String o = ro.getUDTI();
			if (o != null) {
				if (ResplRsrcs.hasTMPL(o)) {
					return ResplendenceEditor.TEMPLATE_EDITOR;
				}
			}
			return ResplendenceEditor.CAN_EDIT_IF_REQUESTED;
		} else {
			return ResplendenceEditor.DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Template";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC", "TMPL"));
	}
	
	public static class WTemplateChooser extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1L;
		private JList list;
		public WTemplateChooser(ResplendenceObject ro) {
			super(ro, false);
			setTitle("Open with Template");
			register();
			
			JPanel main = new JPanel(new BorderLayout(12,12));
			main.add(new JLabel("Select a template to open \""+ro.getTitleForIcons()+"\":"), BorderLayout.PAGE_START);
			String[] tmplList = ResplRsrcs.getAppDFFResourceProvider().getNames(KSFLConstants.Mac_TMPL);
			Arrays.sort(tmplList, new Comparator<String>() {
				public int compare(String a, String b) {
					if (a.startsWith(".") != b.startsWith(".")) {
						return b.compareToIgnoreCase(a);
					} else {
						return a.compareToIgnoreCase(b);
					}
				}
			});
			list = new JList(tmplList);
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ae) {
					String tmplName = list.getSelectedValue().toString();
					resplOpen(new WTemplateEditor(getResplendenceObject(), tmplName));
					dispose();
				}
			});
			main.add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
			JPanel buttons = new JPanel();
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String tmplName = list.getSelectedValue().toString();
					resplOpen(new WTemplateEditor(getResplendenceObject(), tmplName));
					dispose();
				}
			});
			buttons.add(okButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
			buttons.add(cancelButton);
			main.add(buttons, BorderLayout.PAGE_END);
			
			main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
			this.setResizable(false);
			this.pack();
			ResplUtils.sizeWindow(this, 3, 4);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
	}
	
	public static class WTemplateEditor extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1L;
		private TFEditorTemplate te;
		public WTemplateEditor(ResplendenceObject ro, String tmplName) {
			super(ro, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_IMPORT_EXPORT |
					ResplMain.MENUS_SAVE_REVERT |
					ResplMain.MENUS_FORMAT
			);
			
			te = new TFEditorTemplate(120,"MACROMAN",ResplRsrcs.getTMPL(tmplName));
			te.readValue(ro.getData(), new Position(0));
			addListeners(this, te);
			te.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 4));
			JScrollPane sp = new JScrollPane(te, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			this.setContentPane(sp);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			Position p = te.writeValue(new Position(0));
			byte[] b = new byte[(int)p.bytelength()];
			te.writeValue(b, new Position(0));
			ro.setData(b);
		}
		
		public void revert(ResplendenceObject ro) {
			te.readValue(ro.getData(), new Position(0));
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.EXPORT_FILE:
				try {
					RandomAccessFile raf = new RandomAccessFile(e.getFile(), "rwd");
					raf.seek(0);
					raf.setLength(0);
					Position p = te.writeValue(new Position(0));
					byte[] b = new byte[(int)p.bytelength()];
					te.writeValue(b, new Position(0));
					raf.write(b);
					raf.close();
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "Could not export because an I/O error occurred.", "Export", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case ResplendenceEvent.IMPORT_FILE:
				try {
					RandomAccessFile raf = new RandomAccessFile(e.getFile(), "r");
					byte[] stuff = new byte[(int)Math.min(raf.length(), Integer.MAX_VALUE)];
					raf.seek(0);
					raf.read(stuff);
					raf.close();
					te.readValue(stuff, new Position(0));
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "Could not import because an I/O error occurred.", "Import", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case ResplendenceEvent.GET_FONT:
				return te.getFont();
			case ResplendenceEvent.SET_FONT:
				setFonts(te, e.getFont());
				break;
			}
			return null;
		}
	}
	
	private static void setFonts(Component c, Font f) {
		c.setFont(f);
		if (c instanceof JComponent) {
			for (Component cc : ((JComponent)c).getComponents()) {
				setFonts(cc, f);
			}
		}
	}
	
	private static void addListeners(ResplendenceEditorWindow rew, TFEditor tfe) {
		final ResplendenceEditorWindow reww = rew;
		if (tfe instanceof JTextComponent) {
			((JTextComponent)tfe).getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {}
				public void insertUpdate(DocumentEvent arg0) {
					reww.setChangesMade();
				}
				public void removeUpdate(DocumentEvent arg0) {
					reww.setChangesMade();
				}
			});
		} else if (tfe instanceof AbstractButton) {
			((AbstractButton)tfe).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					reww.setChangesMade();
				}
			});
		} else if (tfe instanceof JComboBox) {
			((JComboBox)tfe).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					reww.setChangesMade();
				}
			});
		} else if (tfe instanceof JHexEditorSuite) {
			((JHexEditorSuite)tfe).addHexDataChangeListener(new HexDataChangeListener() {
				public void hexDataChanged(HexDataChangeEvent e) {
					reww.setChangesMade();
				}
			});
		} else if (tfe instanceof TFEditorCoordinates) {
			DocumentListener dl = new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {}
				public void insertUpdate(DocumentEvent arg0) {
					reww.setChangesMade();
				}
				public void removeUpdate(DocumentEvent arg0) {
					reww.setChangesMade();
				}
			};
			TFEditorCoordinates cc = (TFEditorCoordinates)tfe;
			if (cc.x != null) cc.x.getDocument().addDocumentListener(dl);
			if (cc.y != null) cc.y.getDocument().addDocumentListener(dl);
			if (cc.z != null) cc.z.getDocument().addDocumentListener(dl);
			if (cc.x2 != null) cc.x2.getDocument().addDocumentListener(dl);
			if (cc.y2 != null) cc.y2.getDocument().addDocumentListener(dl);
		} else if (tfe instanceof TFEditorTemplate) {
			Iterator<TFEditor> i = ((TFEditorTemplate)tfe).getTFEditorIterator();
			while (i.hasNext()) addListeners(rew, i.next());
		} else if (tfe instanceof Component) {
			((Component)tfe).addMouseListener(new MouseListener() {
				public void doIt(MouseEvent e) {
					reww.setChangesMade();
				}
				public void mouseClicked(MouseEvent e) { doIt(e); }
				public void mouseEntered(MouseEvent e) { doIt(e); }
				public void mouseExited(MouseEvent e) { doIt(e); }
				public void mousePressed(MouseEvent e) { doIt(e); }
				public void mouseReleased(MouseEvent e) { doIt(e); }
			});
		}
	}
}
