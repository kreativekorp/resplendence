package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.menus.ResplendenceKeystrokeAdapter;
import com.kreative.resplendence.misc.*;
import com.kreative.swing.*;
import com.kreative.paint.datatransfer.FileListSelection;

public class FolderWindow implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNGnow("DIR");
	}
	
	public String name() {
		return "Folder Window";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WFolderWindow(ro);
	}
	
	public int recognizes(ResplendenceObject ro) {
		if (ro.isContainerType()) {
			if (ro.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) {
				return ONLY_EDITOR;
			} else if (ro.getType().equals(ResplendenceObject.TYPE_MY_COMPUTER)) {
				return ONLY_EDITOR;
			} else {
				return REFUSE_TO_EDIT;
			}
		} else {
			return DOES_NOT_RECOGNIZE;
		}
	}
	
	public String shortName() {
		return "Folder";
	}
	
	public Image smallIcon() {
		return ResplUtils.shrinkNow(ResplRsrcs.getPNGnow("DIR"));
	}

	public static class WFolderWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		
		private JIconList jill;
		private DefaultListModel jim;
		private Map<Object,Icon> jack;
		
		public WFolderWindow(ResplendenceObject ro) {
			super(ro, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_REFRESH |
					ResplMain.MENUS_OPEN_ITEM |
					((ro instanceof MyComputerObject)?0:
						ResplMain.MENUS_NEW_ITEM |
						ResplMain.MENUS_DUPLICATE_ITEM |
						ResplMain.MENUS_REMOVE_ITEM |
						ResplMain.MENUS_CUT_COPY_PASTE)
			);
			JPanel main = new JPanel(new BorderLayout());
			
			JPanel header = new JPanel(new BorderLayout(12,12));
			Icon icon = (ro instanceof MyComputerObject)?
					new ImageIcon(ResplRsrcs.getPNG("COMP")):
						ResplUtils.getFileIcon(ro.getNativeFile(), true);
			header.add(new JLabel(icon), BorderLayout.LINE_START);
			JPanel info = new JPanel();
			info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));
			JLabel nameLabel = new JLabel(ro.getTitleForWindows());
			nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
			info.add(nameLabel);
			if (ro.getNativeFile() != null) {
				JLabel pathLabel = new JLabel(ro.getNativeFile().getAbsolutePath());
				pathLabel.setFont(pathLabel.getFont().deriveFont(9.0f));
				info.add(pathLabel);
			}
			header.add(info, BorderLayout.CENTER);
			header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			main.add(header, BorderLayout.PAGE_START);
			
			jim = new DefaultListModel();
			jack = new HashMap<Object,Icon>();
			jill = new JIconList(jim, jack);
			jill.setFixedCellWidth(140);
			jill.setListAlias(new JListAlias() {
				public Object getListAlias(JList list, Object value, int index) {
					String s;
					if (value instanceof ResplendenceObject) s = ((ResplendenceObject)value).getTitleForIcons();
					else s = value.toString();
					if (s.length() < 1) s = " ";
					return s;
				}
			});
			ResplendenceKeystrokeAdapter.getInstance().addCutCopyPasteAction(jill);
			update();
			jill.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ev) {
					if (ev.getClickCount() == 2) {
						try {
							Object[] oo = ((JList)ev.getSource()).getSelectedValues();
							for (Object o : oo) resplOpen((ResplendenceObject)o);
						} catch (Exception ex) {}
					}
				}
			});
			JScrollPane joss = new JScrollPane(jill, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			main.add(joss, BorderLayout.CENTER);
			
			setContentPane(main);
			pack();
			setSize(600, 400);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			Vector<File> vf;
			switch (e.getID()) {
			case ResplendenceEvent.GET_SELECTED_RESPL_OBJECT:
				return jill.getSelectedValues();
			case ResplendenceEvent.REFRESH:
				update();
				break;
			case ResplendenceEvent.NEW_ITEM:
				new NewItemDialog(this);
				break;
			case ResplendenceEvent.DUPLICATE_ITEM:
				if (Warnings.warn(Warnings.SIGNATURE_FILE_DUPLICATE, Warnings.MESSAGE_FILE_DUPLICATE)) {
					for (Object o : jill.getSelectedValues()) {
						if (o instanceof ResplendenceObject) {
							ResplendenceObject ro = (ResplendenceObject)o;
							if (ro.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) {
								try {
									File src = ro.getNativeFile();
									File dst = new File(src.getParentFile(), "Copy of "+src.getName());
									ResplUtils.copyFile(src, dst);
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(this, "Could not duplicate object \""+((ResplendenceObject)o).getTitleForWindows()+".\"", "Duplicate Item", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								if (!getResplendenceObject().addChild(ro)) {
									JOptionPane.showMessageDialog(this, "Could not duplicate object \""+((ResplendenceObject)o).getTitleForWindows()+".\"", "Duplicate Item", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
					update();
				}
				break;
			case ResplendenceEvent.REMOVE_ITEM:
			case ResplendenceEvent.CLEAR:
				if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove these items?", "Remove Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					for (Object o : jill.getSelectedValues()) {
						if (o instanceof ResplendenceObject) {
							if (getResplendenceObject().removeChild((ResplendenceObject)o) == null) {
								JOptionPane.showMessageDialog(this, "Could not remove object \""+((ResplendenceObject)o).getTitleForWindows()+".\"", "Remove Item", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					update();
				}
				break;
			case ResplendenceEvent.CUT:
				vf = new Vector<File>();
				for (Object o : jill.getSelectedValues()) {
					if (o instanceof ResplendenceObject) {
						vf.add(((ResplendenceObject)o).getNativeFile());
					}
				}
				ResplScrap.setScrap(new FileListSelection(vf, true));
				break;
			case ResplendenceEvent.COPY:
				vf = new Vector<File>();
				for (Object o : jill.getSelectedValues()) {
					if (o instanceof ResplendenceObject) {
						vf.add(((ResplendenceObject)o).getNativeFile());
					}
				}
				ResplScrap.setScrap(new FileListSelection(vf, false));
				break;
			case ResplendenceEvent.PASTE:
			case ResplendenceEvent.PASTE_AFTER:
				if ((ResplScrap.getScrapTransferable() instanceof FileListSelection) && ((FileListSelection)ResplScrap.getScrapTransferable()).isCutOperation()) {
					// cut
					Collection<File> files = ResplScrap.getScrapFiles();
					if (files != null) for (File f : files) {
						f.renameTo(new File(getResplendenceObject().getNativeFile(), f.getName()));
					}
				} else {
					// copy
					Collection<File> files = ResplScrap.getScrapFiles();
					if (files != null) for (File f : files) {
						try {
							ResplUtils.copyFile(f, new File(getResplendenceObject().getNativeFile(), f.getName()));
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}
				update();
				break;
			}
			return null;
		}

		public void update() {
			jim.removeAllElements();
			jack.clear();
			ResplendenceObject[] ros = getResplendenceObject().getChildren();
			for (int i=0; i<ros.length; i++) {
				jim.addElement(ros[i]);
				Icon icon2 = ResplUtils.getFileIcon(ros[i].getNativeFile(), true);
				jack.put(ros[i], icon2);
			}
		}
	}

	private static class NewItemDialog extends JFrame {
		private static final long serialVersionUID = 1L;
		private WFolderWindow fw;
		private JRadioButton folderButton, fileButton;
		private JTextField field;
		public NewItemDialog(WFolderWindow fwin) {
			super("New Item");
			this.fw = fwin;
			ResplMain.registerWindow(this);
			JPanel main = new JPanel(new BorderLayout(12,12));
			JPanel radios = new JPanel();
			ButtonGroup bg = new ButtonGroup();
			radios.add(new JLabel("Create New"));
			folderButton = new JRadioButton("Folder");
			folderButton.setSelected(true);
			radios.add(folderButton);
			bg.add(folderButton);
			fileButton = new JRadioButton("File");
			radios.add(fileButton);
			bg.add(fileButton);
			main.add(radios, BorderLayout.PAGE_START);
			field = new JTextField("Untitled");
			main.add(field, BorderLayout.CENTER);
			JPanel buttons = new JPanel();
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					File nf = new File(fw.getResplendenceObject().getNativeFile(), field.getText());
					if (folderButton.isSelected()) {
						nf.mkdir();
						fw.update();
						dispose();
					}
					else if (fileButton.isSelected()) {
						dispose();
						Window w = new NewFileWindow(nf, fw);
						w.addWindowListener(new WindowAdapter() {
							public void windowClosed(WindowEvent ev) {
								fw.update();
							}
						});
					}
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
			main.add(buttons, BorderLayout.PAGE_END);
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

	public static class MyComputerObject extends ResplendenceObject {
		@Override
		public boolean addChild(ResplendenceObject rn) {
			return false;
		}

		@Override
		public ResplendenceObject getChild(int i) {
			return new FSObject(File.listRoots()[i]);
		}

		@Override
		public int getChildCount() {
			return File.listRoots().length;
		}

		@Override
		public ResplendenceObject[] getChildren() {
			File[] lf = File.listRoots();
			ResplendenceObject[] ro = new ResplendenceObject[lf.length];
			for (int i=0; i<lf.length; i++) {
				ro[i] = new FSObject(lf[i]);
			}
			return ro;
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
			return "My Computer";
		}
		
		@Override
		public String getTitleForIcons() {
			return "My Computer";
		}

		@Override
		public String getTitleForWindowMenu() {
			return "My Computer";
		}
		
		@Override
		public String getTitleForWindows() {
			return "My Computer";
		}
		
		@Override
		public String getType() {
			return TYPE_MY_COMPUTER;
		}
		
		@Override
		public String getUDTI() {
			return ".my-computer";
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
			return false;
		}
	}
}
