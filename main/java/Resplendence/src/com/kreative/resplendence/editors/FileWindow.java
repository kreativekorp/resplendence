package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.filecodec.*;
import com.kreative.swing.*;

public class FileWindow implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNGnow("FILE");
	}

	public String name() {
		return "File Window";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WFileWindow(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			if (ro.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) {
				return ONLY_EDITOR;
			} else {
				return REFUSE_TO_EDIT;
			}
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
	
	public String shortName() {
		return "File";
	}

	public Image smallIcon() {
		return ResplUtils.shrinkNow(ResplRsrcs.getPNGnow("FILE"));
	}
	
	public static class WFileWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		
		private RWCFile workingCopy;
		private FileCodec codec;
		private FileCodec[] codecs;
		private JPanel body;
		private boolean eventDisable = false;
		
		private JComponent sax(JComponent c, float a) {
			c.setAlignmentX(a); return c;
		}
		
		public WFileWindow(ResplendenceObject ro) {
			super(ro, true);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT |
					ResplMain.MENUS_OPEN_ITEM |
					ResplMain.MENUS_NEW_ITEM |
					ResplMain.MENUS_DUPLICATE_ITEM |
					ResplMain.MENUS_REMOVE_ITEM |
					ResplMain.MENUS_ITEM_INFO |
					ResplMain.MENUS_REFRESH
			);
			try {
				workingCopy = RWCFile.createTempRWCFile(ro.getNativeFile());
			} catch (IOException ioe) {
				workingCopy = null;
				JOptionPane.showMessageDialog(null, "Could not create a working copy of \""+ro.getNativeFile().getName()+",\" so this file window won't work.", "Open", JOptionPane.ERROR_MESSAGE);
			}
			codec = ResplMain.getFileCodec(ro.getNativeFile());
			if (codec == null) {
				JOptionPane.showMessageDialog(null, "Could not find a codec for \""+ro.getNativeFile().getName()+",\" so this file window won't work. This shouldn't happen, because NativeFileCodec should catch it. Dammit!", "Open", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					codec.decode(ro.getNativeFile(), workingCopy);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "The codec for \""+ro.getNativeFile().getName()+"\" could not decode the file, so this file window won't work. If I were you, I wouldn't touch anything; just close it.", "Open", JOptionPane.ERROR_MESSAGE);
				}
			}
			codecs = ResplMain.getFileCodecList();
			if (codecs == null) {
				JOptionPane.showMessageDialog(null, "Apparently there aren't any codecs at all. What the hell!?", "Open", JOptionPane.ERROR_MESSAGE);
			}
			JPanel main = new JPanel(new BorderLayout());
			
			JPanel header = new JPanel(new BorderLayout(12,12));
			Icon icon = ResplUtils.getFileIcon(ro.getNativeFile(), true);
			JLabel iconLabel = new JLabel(icon);
			iconLabel.setAlignmentY(JLabel.TOP_ALIGNMENT);
			iconLabel.setVerticalAlignment(JLabel.TOP);
			iconLabel.setVerticalTextPosition(JLabel.TOP);
			header.add(new JLabel(icon), BorderLayout.LINE_START);
			JPanel info = new JPanel();
			info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));
			JLabel nameLabel = new JLabel(ro.getTitleForWindows());
			nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
			info.add(nameLabel);
			JLabel pathLabel = new JLabel(ro.getNativeFile().getAbsolutePath());
			pathLabel.setFont(pathLabel.getFont().deriveFont(9.0f));
			info.add(pathLabel);
			info.setAlignmentY(JLabel.TOP_ALIGNMENT);
			header.add(info, BorderLayout.CENTER);
			DefaultComboBoxModel codecList = new DefaultComboBoxModel();
			for (int i=0; i<codecs.length; i++) codecList.addElement(codecs[i].name());
			JComboBox codecMenu = new JComboBox(codecList);
			codecMenu.setMaximumRowCount(32);
			codecMenu.setEditable(false);
			codecMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
			codecMenu.setSelectedItem(codec.name());
			codecMenu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (eventDisable) return;
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (Warnings.warn(Warnings.SIGNATURE_CODEC_CONVERT, Warnings.MESSAGE_CODEC_CONVERT)) {
							codec = codecs[((JComboBox)e.getSource()).getSelectedIndex()];
						} else {
							eventDisable = true;
							((JComboBox)e.getSource()).setSelectedItem(codec.name());
							eventDisable = false;
						}
					}
				}
			});
			JPanel codecPanel = new JPanel();
			codecPanel.setLayout(new BoxLayout(codecPanel, BoxLayout.PAGE_AXIS));
			codecPanel.add(sax(new JLabel("Encoding:"),JLabel.LEFT_ALIGNMENT));
			codecPanel.add(Box.createVerticalStrut(4));
			codecPanel.add(codecMenu);
			codecPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
			header.add(codecPanel, BorderLayout.LINE_END);
			header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			main.add(header, BorderLayout.PAGE_START);
			body = new JPanel();
			updateBody();
			JPanel bodyp = new JBodyPanel(new BorderLayout());
			bodyp.add(body, BorderLayout.NORTH);
			JScrollPane bodys = new JScrollPane(bodyp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			main.add(bodys, BorderLayout.CENTER);
			if (ResplMain.RUNNING_ON_MAC_OS) {
				main.add(Box.createVerticalStrut(12), BorderLayout.PAGE_END);
			}
			setContentPane(main);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(this);
			pack();
			setSize(600, 400);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			try {
				codec.encode(ro.getNativeFile(), workingCopy);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "The codec for \""+ro.getNativeFile().getName()+"\" could not encode the file, so the file was not saved.", "Save", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public void revert(ResplendenceObject ro) {
			try {
				codec.decode(ro.getNativeFile(), workingCopy);
				updateBody();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "The codec for \""+ro.getNativeFile().getName()+"\" could not decode the file, so the file was not reverted.", "Revert", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				for (Component c : body.getComponents()) {
					if (c instanceof ForkView) {
						ForkView f = (ForkView)c;
						if (f.isSelected()) return f.getObject();
					} else if (c instanceof MetaView) {
						MetaView f = (MetaView)c;
						if (f.isSelected()) return f.getObject();
					}
				}
				break;
			case ResplendenceEvent.REFRESH:
				updateBody();
				break;
			case ResplendenceEvent.NEW_ITEM:
				new NewItemDialog(this);
				break;
			case ResplendenceEvent.DUPLICATE_ITEM:
				for (Component c : body.getComponents()) {
					if (c instanceof ForkView) {
						ForkView f = (ForkView)c;
						if (f.isSelected()) {
							new DupItemDialog(this, f.getObject().getNativeFile());
						}
					}
				}
				break;
			case ResplendenceEvent.REMOVE_ITEM:
				if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove these items?", "Remove Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					for (Component c : body.getComponents()) {
						if (c instanceof ForkView) {
							ForkView f = (ForkView)c;
							if (f.isSelected()) f.getObject().getNativeFile().delete();
						} else if (c instanceof MetaView) {
							MetaView f = (MetaView)c;
							if (f.isSelected()) f.getObject().getNativeFile().delete();
						}
					}
					setChangesMade();
					updateBody();
				}
				break;
			case ResplendenceEvent.ITEM_INFO:
				for (Component c : body.getComponents()) {
					if (c instanceof ForkView) {
						ForkView f = (ForkView)c;
						if (f.isSelected()) {
							new ItemInfoDialog(this, f.getObject().getNativeFile());
						}
					}
				}
				break;
			}
			return null;
		}
		
		public void updateBody() {
			MouseListener m = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					select((Component)e.getSource());
				}
			};
			body.removeAll();
			String[] forx = workingCopy.getForkHandle().list();
			body.setLayout(new GridLayout(forx.length+1,1));
			for (int i=0; i<forx.length; i++) {
				ForkView f = new ForkView(this, this, getResplendenceObject(), workingCopy, forx[i]);
				f.addMouseListener(m);
				body.add(f);
			}
			MetaView f = new MetaView(this, this, getResplendenceObject(), workingCopy);
			f.addMouseListener(m);
			body.add(f);
			body.updateUI();
		}
		
		public void select(Component comp) {
			Component[] c = body.getComponents();
			for (int i=0; i<c.length; i++) {
				if (c[i] instanceof ForkView) {
					ForkView f = (ForkView)c[i];
					f.setSelected(c[i] == comp || f.getLabel() == comp || f.getList() == comp);
				} else if (c[i] instanceof MetaView) {
					MetaView f = (MetaView)c[i];
					f.setSelected(c[i] == comp || f.getLabel() == comp || f.getList() == comp);
				}
			}
		}
		
		public void selectFork(String s) {
			Component[] c = body.getComponents();
			for (int i=0; i<c.length; i++) {
				if (c[i] instanceof ForkView) {
					ForkView f = (ForkView)c[i];
					f.setSelected(f.getForkName().equals(s));
				} else if (c[i] instanceof MetaView) {
					MetaView f = (MetaView)c[i];
					f.setSelected(false);
				}
			}
		}
		
		public void selectMeta() {
			Component[] c = body.getComponents();
			for (int i=0; i<c.length; i++) {
				if (c[i] instanceof ForkView) {
					ForkView f = (ForkView)c[i];
					f.setSelected(false);
				} else if (c[i] instanceof MetaView) {
					MetaView f = (MetaView)c[i];
					f.setSelected(true);
				}
			}
		}
		
		public String getSelectedFork() {
			Component[] c = body.getComponents();
			for (int i=0; i<c.length; i++) {
				if (c[i] instanceof ForkView) {
					ForkView f = (ForkView)c[i];
					if (f.isSelected()) return f.getForkName();
				}
			}
			return null;
		}
		
		public boolean getSelectedMeta() {
			Component[] c = body.getComponents();
			for (int i=0; i<c.length; i++) {
				if (c[i] instanceof MetaView) {
					MetaView f = (MetaView)c[i];
					if (f.isSelected()) return f.isSelected();
				}
			}
			return false;
		}
	}
	
	private static class JBodyPanel extends JPanel implements Scrollable {
		private static final long serialVersionUID = 1;
		
		private int maxUnitIncrement = 80;
		
		/*
		public JBodyPanel() {
			super();
		}
		*/
		
		public JBodyPanel(LayoutManager lm) {
			super(lm);
		}
		
		public Dimension getPreferredScrollableViewportSize() {
			return this.getPreferredSize();
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			//Get the current position.
		    int currentPosition = 0;
		    if (orientation == SwingConstants.HORIZONTAL) {
		        currentPosition = visibleRect.x;
		    } else {
		        currentPosition = visibleRect.y;
		    }
		    
		    //Return the number of pixels between currentPosition
		    //and the nearest tick mark in the indicated direction.
		    if (direction < 0) {
		        int newPosition = currentPosition -
		                         (currentPosition / maxUnitIncrement)
		                          * maxUnitIncrement;
		        return (newPosition == 0) ? maxUnitIncrement : newPosition;
		    } else {
		        return ((currentPosition / maxUnitIncrement) + 1)
		                 * maxUnitIncrement
		                 - currentPosition;
		    }
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			if (orientation == SwingConstants.HORIZONTAL)
		        return visibleRect.width - maxUnitIncrement;
		    else
		        return visibleRect.height - maxUnitIncrement;
		}
	}
	
	private static class ForkView extends JPanel {
		private static final long serialVersionUID = 1;
		private WFileWindow par;
		private JLabel label;
		private JIconList list;
		private String fname;
		private ResplendenceObject obj;
		private ResplendenceEditor[] ed;
		
		public Color labelBgColor() {
			return new Color(
					fname.equals("data")?0xCCEEFF:
						fname.equals("rsrc")?0xFFCCFF:
							0xFFFFCC
			);
		}
		
		public Color labelSelBgColor() {
			return new Color(
					fname.equals("data")?0x40CCFF:
						fname.equals("rsrc")?0xFF40FF:
							0xFFFF40
			);
		}
		
		public boolean isSelected() {
			return (label.getBackground().equals(labelSelBgColor()));
		}
		
		public void setSelected(boolean b) {
			label.setBackground(b?labelSelBgColor():labelBgColor());
			if (b) list.requestFocusInWindow();
			else list.clearSelection();
		}
		
		public String getForkName() {
			return fname;
		}
		
		public ResplendenceObject getObject() {
			return obj;
		}
		
		public JLabel getLabel() {
			return label;
		}
		
		public JIconList getList() {
			return list;
		}
		
		public ForkView(WFileWindow parent, ResplendenceListener rl, ResplendenceObject orig, RWCFile workingCopy, String fname) {
			super(new BorderLayout());
			this.par = parent;
			this.fname = fname;
			obj = new ForkObject(parent, orig.getTitleForWindows(), workingCopy, fname);
			ed = ResplMain.getEditors(obj);
			if (ed == null) ed = new ResplendenceEditor[0];
			add(label = makeSideLabel(fname, labelBgColor()), BorderLayout.LINE_START);
			DefaultListModel model = new DefaultListModel();
			Map<Object,Icon> icons = new HashMap<Object,Icon>();
			for (int i=0; i<ed.length; i++) {
				if (ed[i].recognizes(obj) >= 0) {
					model.addElement(ed[i]);
					Image img = ed[i].largeIcon();
					if (img != null) icons.put(ed[i], new ImageIcon(img));
					else icons.put(ed[i], new ImageIcon());
				}
			}
			list = new JIconList(model, icons);
			list.setFixedCellWidth(100);
			list.setVisibleRowCount(1);
			list.setListAlias(new JListAlias() {
				public Object getListAlias(JList list, Object value, int index) {
					if (value instanceof ResplendenceEditor) {
						return ((ResplendenceEditor)value).shortName();
					} else {
						return value;
					}
				}
			});
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ev) {
					if (ev.getClickCount() == 2) {
						Object o = list.getSelectedValue();
						if (o instanceof ResplendenceEditor) {
							par.resplOpen((ResplendenceEditor)o, obj);
						}
					}
				}
			});
			JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			add(scroll, BorderLayout.CENTER);
			setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray));
		}
		
		private JLabel makeSideLabel(String s, Color c) {
			String h = "";
			CharacterIterator i = new StringCharacterIterator(s);
			for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) {
				switch (ch) {
				case '<': h+="<br>&lt;";  break;
				case '>': h+="<br>&gt;";  break;
				case '&': h+="<br>&amp;"; break;
				default : h+="<br>"+ch;   break;
				}
			}
			h = "<html><code>"+((h.length()<4)?h:h.substring(4))+"</code></html>";
			JLabel l = new JLabel(h);
			l.setOpaque(true);
			l.setBackground(c);
			l.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
			return l;
		}
		
		public void addMouseListener(MouseListener ml) {
			super.addMouseListener(ml);
			label.addMouseListener(ml);
			list.addMouseListener(ml);
		}
		
		public void removeMouseListener(MouseListener ml) {
			super.removeMouseListener(ml);
			label.removeMouseListener(ml);
			list.removeMouseListener(ml);
		}
	}
	
	private static class MetaView extends JPanel {
		private static final long serialVersionUID = 1;
		private static final long META_TABLE = KSFLConstants.MetaTyp$;
		private WFileWindow par;
		private JLabel label;
		private JIconList list;
		private ResplendenceObject[] obj;
		
		public Color labelBgColor() {
			return new Color(0xCCFFCC);
		}
		
		public Color labelSelBgColor() {
			return new Color(0x40FF40);
		}
		
		public boolean isSelected() {
			return (label.getBackground().equals(labelSelBgColor()));
		}
		
		public void setSelected(boolean b) {
			label.setBackground(b?labelSelBgColor():labelBgColor());
			if (b) list.requestFocusInWindow();
			else list.clearSelection();
		}
		
		public JLabel getLabel() {
			return label;
		}
		
		public JIconList getList() {
			return list;
		}
		
		public ResplendenceObject getObject() {
			int i = list.getSelectedIndex();
			if (i >= 0 && i < obj.length) return obj[i];
			else return null;
		}
		
		public MetaView(WFileWindow parent, ResplendenceListener rl, ResplendenceObject orig, RWCFile f) {
			super(new BorderLayout());
			this.par = parent;
			DefaultListModel model = new DefaultListModel();
			Map<Object,Icon> icons = new HashMap<Object,Icon>();
			String[] metae = f.getMetaHandle().list();
			obj = new ResplendenceObject[metae.length+1];
			obj[0] = new AttrObject(parent, orig.getTitleForWindows(), f);
			model.addElement("Attributes");
			icons.put("Attributes", new ImageIcon(ResplRsrcs.getPNG("META", "attr")));
			for (int i=0; i<metae.length; i++) {
				obj[i+1] = new MetaObject(parent, orig.getTitleForWindows(), f, metae[i]);
				String s = ResplRsrcs.getSymbolDescription(META_TABLE, 0, KSFLUtilities.fcc(metae[i]));
				model.addElement(s);
				icons.put(s, new ImageIcon(ResplRsrcs.getPNG("META", metae[i])));
			}
			add(label = makeSideLabel("meta", labelBgColor()), BorderLayout.LINE_START);
			list = new JIconList(model, icons);
			list.setFixedCellWidth(100);
			list.setVisibleRowCount(1);
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ev) {
					if (ev.getClickCount() == 2) {
						int i = list.getSelectedIndex();
						if (i >= 0 && i < obj.length) {
							par.resplOpen(obj[i]);
						}
					}
				}
			});
			JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			add(scroll, BorderLayout.CENTER);
			setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray));
		}
		
		private JLabel makeSideLabel(String s, Color c) {
			String h = "";
			CharacterIterator i = new StringCharacterIterator(s);
			for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) {
				switch (ch) {
				case '<': h+="<br>&lt;";  break;
				case '>': h+="<br>&gt;";  break;
				case '&': h+="<br>&amp;"; break;
				default : h+="<br>"+ch;   break;
				}
			}
			h = "<html><code>"+((h.length()<4)?h:h.substring(4))+"</code></html>";
			JLabel l = new JLabel(h);
			l.setOpaque(true);
			l.setBackground(c);
			l.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
			return l;
		}
		
		public void addMouseListener(MouseListener ml) {
			super.addMouseListener(ml);
			label.addMouseListener(ml);
			list.addMouseListener(ml);
		}
		
		public void removeMouseListener(MouseListener ml) {
			super.removeMouseListener(ml);
			label.removeMouseListener(ml);
			list.removeMouseListener(ml);
		}
	}
	
	private static class NewItemDialog extends JFrame {
		private static final long serialVersionUID = 1L;
		private static final long META_TABLE = KSFLConstants.MetaTyp$;
		private WFileWindow fw;
		private JRadioButton forkButton, metaButton;
		private JTextField field;
		private String[] templates;
		private JComboBox forkTmplMenu;
		private JComboBox metas;
		public NewItemDialog(WFileWindow fwin) {
			super("New Item");
			this.fw = fwin;
			ResplMain.registerWindow(this);
			JPanel main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
			JLabel label = new JLabel("Create New");
			label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			main.add(label);
			
			main.add(Box.createVerticalStrut(8));
			ButtonGroup bg = new ButtonGroup();
			
			JPanel forkPanel = new JPanel();
			forkPanel.setLayout(new BoxLayout(forkPanel, BoxLayout.LINE_AXIS));
			forkButton = new JRadioButton("Fork");
			forkButton.setSelected(true);
			forkButton.setMinimumSize(new Dimension(60, forkButton.getMinimumSize().height));
			forkButton.setPreferredSize(new Dimension(60, forkButton.getPreferredSize().height));
			forkButton.setMaximumSize(new Dimension(60, forkButton.getMaximumSize().height));
			bg.add(forkButton);
			forkPanel.add(forkButton);
			field = new JTextField("name", 4);
			forkPanel.add(field);
			forkPanel.add(Box.createHorizontalStrut(4));
			String[] tmp = ResplRsrcs.getAppDFFResourceProvider().getNames(KSFLConstants.ForkTmpl);
			Arrays.sort(tmp);
			templates = new String[tmp.length+1];
			templates[0] = "Empty";
			for (int i=0; i<tmp.length; i++) templates[i+1] = tmp[i];
			forkTmplMenu = new JComboBox(templates);
			forkTmplMenu.setMaximumRowCount(32);
			forkTmplMenu.setEditable(false);
			forkTmplMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
			forkTmplMenu.setSelectedItem("Empty");
			forkPanel.add(forkTmplMenu);
			main.add(forkPanel);
			
			main.add(Box.createVerticalStrut(8));
			
			JPanel metaPanel = new JPanel();
			metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.LINE_AXIS));
			metaButton = new JRadioButton("Meta");
			metaButton.setMinimumSize(new Dimension(60, metaButton.getMinimumSize().height));
			metaButton.setPreferredSize(new Dimension(60, metaButton.getPreferredSize().height));
			metaButton.setMaximumSize(new Dimension(60, metaButton.getMaximumSize().height));
			bg.add(metaButton);
			metaPanel.add(metaButton);
			metas = new JComboBox();
			Map<Integer,String> metalist = ResplRsrcs.getSymbolReference(META_TABLE, 0, 0);
			Integer[] metatypes = metalist.keySet().toArray(new Integer[0]);
			Arrays.sort(metatypes);
			for (int type : metatypes) {
				metas.addItem(KSFLUtilities.fccs(type) + " - " + metalist.get(type));
			}
			metaPanel.add(metas);
			main.add(metaPanel);
			
			JPanel buttons = new JPanel();
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (Warnings.warn(Warnings.SIGNATURE_CREATE_FORK, Warnings.MESSAGE_CREATE_FORK)) {
						fw.setChangesMade();
						if (forkButton.isSelected()) {
							try {
								File fork = fw.workingCopy.getNamedFork(field.getText());
								fork.createNewFile();
								if (forkTmplMenu.getSelectedIndex() > 0) {
									byte[] b = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.ForkTmpl, forkTmplMenu.getSelectedItem().toString());
									RandomAccessFile r = new RandomAccessFile(fork, "rwd");
									r.write(b);
									r.close();
								}
							} catch (IOException ioe) {
								JOptionPane.showMessageDialog(NewItemDialog.this, "Could not create fork.", "New Item", JOptionPane.ERROR_MESSAGE);
							}
						} else if (metaButton.isSelected()) {
							try {
								fw.workingCopy.getNamedMeta(metas.getSelectedItem().toString().substring(0, 4)).createNewFile();
							} catch (IOException ioe) {
								JOptionPane.showMessageDialog(NewItemDialog.this, "Could not create metadata.", "New Item", JOptionPane.ERROR_MESSAGE);
							}
						}
						fw.updateBody();
					}
					dispose();
				}
			});
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			buttons.add(okButton);
			buttons.add(cancelButton);
			main.add(buttons);
			
			main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.pack();
			this.setLocationRelativeTo(fwin);
			this.setVisible(true);
		}
	}
	
	private static class DupItemDialog extends JFrame {
		private static final long serialVersionUID = 1L;
		private WFileWindow fw;
		private File fork;
		private JTextField field;
		public DupItemDialog(WFileWindow fwin, File fork) {
			super("Duplicate Item");
			this.fw = fwin;
			this.fork = fork;
			ResplMain.registerWindow(this);
			JPanel main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
			
			JPanel line = new JPanel();
			line.setLayout(new BoxLayout(line, BoxLayout.LINE_AXIS));
			line.add(new JLabel("Name of Duplicate:"));
			line.add(Box.createHorizontalStrut(4));
			line.add(field = new JTextField(fork.getName(), 4));
			main.add(line);
			
			JPanel buttons = new JPanel();
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (!DupItemDialog.this.fork.getName().equals(field.getText())) {
						if (Warnings.warn(Warnings.SIGNATURE_CREATE_FORK, Warnings.MESSAGE_CREATE_FORK)) {
							fw.setChangesMade();
							try {
								ResplUtils.copyFile(DupItemDialog.this.fork, new File(DupItemDialog.this.fork.getParentFile(), field.getText()));
							} catch (IOException ioe) {
								JOptionPane.showMessageDialog(DupItemDialog.this, "Could not duplicate fork.", "Duplicate Info", JOptionPane.ERROR_MESSAGE);
							}
							fw.updateBody();
						}
					}
					dispose();
				}
			});
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			buttons.add(okButton);
			buttons.add(cancelButton);
			main.add(buttons);
			
			main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.pack();
			this.setLocationRelativeTo(fwin);
			this.setVisible(true);
		}
	}
	
	private static class ItemInfoDialog extends JFrame {
		private static final long serialVersionUID = 1L;
		private WFileWindow fw;
		private File fork;
		private ButtonGroup bg;
		private JRadioButton renameBtn, swapBtn, tmplBtn;
		private JTextField field;
		private JComboBox swapMenu;
		private String[] templates;
		private JComboBox forkTmplMenu;
		public ItemInfoDialog(WFileWindow fwin, File fork) {
			super("Item Info");
			this.fw = fwin;
			this.fork = fork;
			ResplMain.registerWindow(this);
			JPanel main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
			
			bg = new ButtonGroup();
			bg.add(renameBtn = new JRadioButton("Rename to:"));
			bg.add(swapBtn = new JRadioButton("Swap with:"));
			bg.add(tmplBtn = new JRadioButton("Replace with:"));
			renameBtn.setSelected(true);
			renameBtn.setMinimumSize(new Dimension(120, renameBtn.getMinimumSize().height));
			renameBtn.setPreferredSize(new Dimension(120, renameBtn.getPreferredSize().height));
			renameBtn.setMaximumSize(new Dimension(120, renameBtn.getMaximumSize().height));
			swapBtn.setMinimumSize(new Dimension(120, swapBtn.getMinimumSize().height));
			swapBtn.setPreferredSize(new Dimension(120, swapBtn.getPreferredSize().height));
			swapBtn.setMaximumSize(new Dimension(120, swapBtn.getMaximumSize().height));
			tmplBtn.setMinimumSize(new Dimension(120, tmplBtn.getMinimumSize().height));
			tmplBtn.setPreferredSize(new Dimension(120, tmplBtn.getPreferredSize().height));
			tmplBtn.setMaximumSize(new Dimension(120, tmplBtn.getMaximumSize().height));
			
			JPanel renamePanel = new JPanel();
			renamePanel.setLayout(new BoxLayout(renamePanel, BoxLayout.LINE_AXIS));
			renamePanel.add(renameBtn);
			renamePanel.add(field = new JTextField(fork.getName(), 4));
			main.add(renamePanel);
			
			main.add(Box.createVerticalStrut(4));
			
			JPanel swapPanel = new JPanel();
			swapPanel.setLayout(new BoxLayout(swapPanel, BoxLayout.LINE_AXIS));
			swapPanel.add(swapBtn);
			String[] files = fork.getParentFile().list();
			Arrays.sort(files);
			swapMenu = new JComboBox(files);
			swapMenu.setMaximumRowCount(32);
			swapMenu.setEditable(false);
			swapMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
			swapMenu.setSelectedItem(fork.getName());
			swapPanel.add(swapMenu);
			main.add(swapPanel);
			
			main.add(Box.createVerticalStrut(4));
			
			JPanel tmplPanel = new JPanel();
			tmplPanel.setLayout(new BoxLayout(tmplPanel, BoxLayout.LINE_AXIS));
			tmplPanel.add(tmplBtn);
			String[] tmp = ResplRsrcs.getAppDFFResourceProvider().getNames(KSFLConstants.ForkTmpl);
			Arrays.sort(tmp);
			templates = new String[tmp.length+1];
			templates[0] = "Empty";
			for (int i=0; i<tmp.length; i++) templates[i+1] = tmp[i];
			forkTmplMenu = new JComboBox(templates);
			forkTmplMenu.setMaximumRowCount(32);
			forkTmplMenu.setEditable(false);
			forkTmplMenu.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
			forkTmplMenu.setSelectedItem("Empty");
			tmplPanel.add(forkTmplMenu);
			main.add(tmplPanel);
			
			JPanel buttons = new JPanel();
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fw.setChangesMade();
					if (renameBtn.isSelected()) {
						if (!ItemInfoDialog.this.fork.getName().equals(field.getText())) {
							ItemInfoDialog.this.fork.renameTo(new File(ItemInfoDialog.this.fork.getParentFile(), field.getText()));
						}
					} else if (swapBtn.isSelected()) {
						if (!ItemInfoDialog.this.fork.getName().equals(swapMenu.getSelectedItem().toString())) {
							File a = ItemInfoDialog.this.fork;
							File b = new File(ItemInfoDialog.this.fork.getParentFile(), swapMenu.getSelectedItem().toString());
							File t = new File(ItemInfoDialog.this.fork.getParentFile(), "...temp...");
							a.renameTo(t);
							b.renameTo(a);
							t.renameTo(b);
						}
					} else if (tmplBtn.isSelected()) {
						if (forkTmplMenu.getSelectedIndex() > 0) {
							try {
								byte[] b = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.ForkTmpl, forkTmplMenu.getSelectedItem().toString());
								RandomAccessFile r = new RandomAccessFile(ItemInfoDialog.this.fork, "rwd");
								r.write(b);
								r.close();
							} catch (IOException ioe) {
								JOptionPane.showMessageDialog(ItemInfoDialog.this, "Could not edit fork.", "Item Info", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					dispose();
					fw.updateBody();
				}
			});
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			buttons.add(okButton);
			buttons.add(cancelButton);
			main.add(buttons);
			
			main.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(this.getRootPane(), cancelButton);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.pack();
			this.setLocationRelativeTo(fwin);
			this.setVisible(true);
		}
	}
	
	private static class FWObject extends FSObject {
		private WFileWindow parent;
		
		public FWObject(WFileWindow w, File f) {
			super(f);
			parent = w;
		}
		
		@Override
		public Object getProvider() {
			parent.setChangesMade();
			return super.getProvider();
		}
		
		@Override
		public RandomAccessFile getRandomAccessData(String mode) {
			if (!mode.equals("r")) parent.setChangesMade();
			return super.getRandomAccessData(mode);
		}
		
		@Override
		public boolean setData(byte[] stuff) {
			parent.setChangesMade();
			return super.setData(stuff);
		}
		
		@Override
		public boolean addChild(ResplendenceObject rn) {
			parent.setChangesMade();
			return super.addChild(rn);
		}
		
		@Override
		public boolean replaceChild(int i, ResplendenceObject rn) {
			parent.setChangesMade();
			return super.replaceChild(i, rn);
		}
		
		@Override
		public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
			parent.setChangesMade();
			return super.replaceChild(ro, rn);
		}
		
		@Override
		public ResplendenceObject removeChild(int i) {
			parent.setChangesMade();
			return super.removeChild(i);
		}
		
		@Override
		public ResplendenceObject removeChild(ResplendenceObject ro) {
			parent.setChangesMade();
			return super.removeChild(ro);
		}
		
		@Override
		public boolean setProperty(String key, Object value) {
			parent.setChangesMade();
			return super.setProperty(key, value);
		}
	}
	
	private static class ForkObject extends FWObject {
		private RWCFile workingCopy;
		private String originalName;
		
		public ForkObject(WFileWindow parent, String origName, RWCFile workingCopy, File fork) {
			super(parent, fork);
			this.workingCopy = workingCopy;
			this.originalName = origName;
		}
		
		public ForkObject(WFileWindow parent, String origName, RWCFile workingCopy, String forkName) {
			super(parent, workingCopy.getNamedFork(forkName));
			this.workingCopy = workingCopy;
			this.originalName = origName;
		}

		@Override
		public String getType() {
			return ResplendenceObject.TYPE_FORK;
		}

		@Override
		public RWCFile getWorkingCopy() {
			return workingCopy;
		}
		
		@Override
		public String getTitleForWindows() {
			return originalName + " (" + this.getNativeFile().getName() + " fork)";
		}
		
		@Override
		public String getTitleForWindowMenu() {
			return this.getNativeFile().getName() + " fork";
		}
		
		@Override
		public String getUDTI() {
			return ".fork-"+this.getNativeFile().getName();
		}
	}
	
	private static class MetaObject extends FWObject {
		private RWCFile workingCopy;
		private String originalName;
		
		public MetaObject(WFileWindow parent, String origName, RWCFile workingCopy, File meta) {
			super(parent, meta);
			this.workingCopy = workingCopy;
			this.originalName = origName;
		}
		
		public MetaObject(WFileWindow parent, String origName, RWCFile workingCopy, String metaName) {
			super(parent, workingCopy.getNamedMeta(metaName));
			this.workingCopy = workingCopy;
			this.originalName = origName;
		}

		@Override
		public String getType() {
			return ResplendenceObject.TYPE_META;
		}

		@Override
		public RWCFile getWorkingCopy() {
			return workingCopy;
		}
		
		@Override
		public String getTitleForWindows() {
			return originalName + " (" + this.getNativeFile().getName() + ")";
		}
		
		@Override
		public String getTitleForWindowMenu() {
			return this.getNativeFile().getName();
		}
		
		@Override
		public String getUDTI() {
			return ".meta-"+this.getNativeFile().getName();
		}
	}
	
	private static class AttrObject extends FWObject {
		private RWCFile workingCopy;
		private String originalName;
		
		public AttrObject(WFileWindow parent, String origName, RWCFile workingCopy) {
			super(parent, workingCopy.getAttributeHandle());
			this.workingCopy = workingCopy;
			this.originalName = origName;
		}
		
		@Override
		public String getType() {
			return ResplendenceObject.TYPE_ATTR;
		}

		@Override
		public RWCFile getWorkingCopy() {
			return workingCopy;
		}
		
		@Override
		public String getTitleForWindows() {
			return originalName + " (attributes)";
		}
		
		@Override
		public String getTitleForWindowMenu() {
			return "attributes";
		}
		
		@Override
		public String getUDTI() {
			return ".attr";
		}
	}
}
