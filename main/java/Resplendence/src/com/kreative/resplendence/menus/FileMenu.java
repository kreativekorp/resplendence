package com.kreative.resplendence.menus;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.*;

public class FileMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;
	private static final JFileChooser fchoos = new JFileChooser();
	
	private JMenu recentMenu, specialMenu;

	public FileMenu(long features) {
		super("File");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (mac) {
			fchoos.putClientProperty("JFileChooser.packageIsTraversable", "always");
			fchoos.putClientProperty("JFileChooser.appBundleIsTraversable", "always");
		}
		else setMnemonic(KeyEvent.VK_F);
		
		JMenuItem mi;
		if (mac || (features & MENUS_GLOBAL)>0) {
			mi = new JMenuItem("New File...");
			mi.setMnemonic(KeyEvent.VK_N);
			mi.setAccelerator(KeyStroke.getKeyStroke('N', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "New File", FileDialog.SAVE);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						final File f = new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile());
						Window w = new NewFileWindow(f, null);
						w.addWindowListener(new WindowAdapter() {
							public void windowClosed(WindowEvent we) {
								ResplMain.resplOpen(f);
							}
						});
					}
				}
			});
			add(mi);
			mi = new JMenuItem("New Folder...");
			mi.setMnemonic(KeyEvent.VK_W);
			mi.setAccelerator(KeyStroke.getKeyStroke('N', META_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "New Folder", FileDialog.SAVE);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						File f = new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile());
						try {
							f.mkdir();
							ResplMain.resplOpen(f);
						} catch (Exception ioe) {
							JOptionPane.showMessageDialog(null, "The folder could not be created.", "New Folder", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open File...");
			mi.setMnemonic(KeyEvent.VK_O);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "Open File", FileDialog.LOAD);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						File f = new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile());
						ResplMain.resplOpen(f);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open Folder...");
			mi.setMnemonic(KeyEvent.VK_D);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_ALT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fchoos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fchoos.setFileHidingEnabled(true);
					fchoos.setMultiSelectionEnabled(false);
					if (fchoos.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File f = fchoos.getSelectedFile();
						ResplMain.resplOpen(f);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open Anything...");
			mi.setMnemonic(KeyEvent.VK_H);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_ALT_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fchoos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fchoos.setFileHidingEnabled(false);
					fchoos.setMultiSelectionEnabled(false);
					if (fchoos.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File f = fchoos.getSelectedFile();
						ResplMain.resplOpen(f);
					}
				}
			});
			add(mi);
			recentMenu = new JMenu("Open Recent");
			recentMenu.setMnemonic(KeyEvent.VK_C);
			add(recentMenu);
			specialMenu = new JMenu("Open Special");
			specialMenu.setMnemonic(KeyEvent.VK_L);
			add(specialMenu);
			mi = new JMenuItem("Open Database...");
			mi.setMnemonic(KeyEvent.VK_B);
			mi.setAccelerator(KeyStroke.getKeyStroke('B', META_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new OpenDatabaseWindow();
				}
			});
			add(mi);
			update();
		}
		if (mac || (features & MENUS_IMPORT_EXPORT) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Import...");
			mi.setMnemonic(KeyEvent.VK_I);
			mi.setAccelerator(KeyStroke.getKeyStroke('I', META_ALT_MASK));
			mi.setEnabled((features & MENUS_IMPORT_EXPORT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "Import", FileDialog.LOAD);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						ResplMain.sendResplendenceEventToFrontmost(
								new ResplendenceEvent(
										e.getSource(),
										ResplendenceEvent.IMPORT_FILE,
										"Import File...",
										new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile())
								)
						);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Export...");
			mi.setMnemonic(KeyEvent.VK_E);
			mi.setAccelerator(KeyStroke.getKeyStroke('E', META_ALT_MASK));
			mi.setEnabled((features & MENUS_IMPORT_EXPORT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "Export", FileDialog.SAVE);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						ResplMain.sendResplendenceEventToFrontmost(
								new ResplendenceEvent(
										e.getSource(),
										ResplendenceEvent.EXPORT_FILE,
										"Export File...",
										new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile())
								)
						);
					}
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_SAVE_REVERT)>0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Save");
			mi.setMnemonic(KeyEvent.VK_S);
			mi.setAccelerator(KeyStroke.getKeyStroke('S', META_MASK));
			mi.setEnabled((features & MENUS_SAVE_REVERT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(
							new ResplendenceEvent(
									e.getSource(), ResplendenceEvent.SAVE, "Save", null
							)
					);
				}
			});
			add(mi);
			mi = new JMenuItem("Save As...");
			mi.setMnemonic(KeyEvent.VK_A);
			mi.setAccelerator(KeyStroke.getKeyStroke('S', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_SAVE_REVERT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "Save As", FileDialog.SAVE);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						ResplMain.sendResplendenceEventToFrontmost(
								new ResplendenceEvent(
										e.getSource(), ResplendenceEvent.SAVE_AS, "Save As...", new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile())
								)
						);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Save a Copy...");
			mi.setMnemonic(KeyEvent.VK_Y);
			mi.setEnabled((features & MENUS_SAVE_REVERT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(new Frame(), "Save a Copy", FileDialog.SAVE);
					fd.setVisible(true);
					if (fd.getFile() != null) {
						ResplMain.sendResplendenceEventToFrontmost(
								new ResplendenceEvent(
										e.getSource(), ResplendenceEvent.SAVE_A_COPY, "Save a Copy...", new File(fd.getDirectory()+System.getProperty("file.separator")+fd.getFile())
								)
						);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Revert");
			mi.setMnemonic(KeyEvent.VK_R);
			mi.setAccelerator(KeyStroke.getKeyStroke('R', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_SAVE_REVERT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(
							new ResplendenceEvent(
									e.getSource(), ResplendenceEvent.REVERT, "Revert", null
							)
					);
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_PRINT)>0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Page Setup...");
			mi.setMnemonic(KeyEvent.VK_U);
			mi.setAccelerator(KeyStroke.getKeyStroke('P', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_PRINT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(
							new ResplendenceEvent(
									e.getSource(), ResplendenceEvent.PAGE_SETUP, "Page Setup...", null
							)
					);
				}
			});
			add(mi);
			mi = new JMenuItem("Print...");
			mi.setMnemonic(KeyEvent.VK_P);
			mi.setAccelerator(KeyStroke.getKeyStroke('P', META_MASK));
			mi.setEnabled((features & MENUS_PRINT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(
							new ResplendenceEvent(
									e.getSource(), ResplendenceEvent.PRINT, "Print...", null
							)
					);
				}
			});
			add(mi);
		}
		if (!mac && (features & MENUS_GLOBAL)>0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Preferences...");
			mi.setMnemonic(KeyEvent.VK_F);
			mi.setAccelerator(KeyStroke.getKeyStroke(',', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PreferencesWindow.showInstance();
				}
			});
			add(mi);
			addSeparator();
			mi = new JMenuItem("Exit");
			mi.setMnemonic(KeyEvent.VK_X);
			mi.setAccelerator(KeyStroke.getKeyStroke('Q', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.resplExit();
				}
			});
			add(mi);
		}
	}
	
	public void update() {
		//recent
		recentMenu.removeAll();
		java.util.List<File> rp = ResplPrefs.getFiles("Recent Files");
		if (rp != null) {
			for (File f : rp) {
				JMenuItem mi = new JMenuItem(f.getName());
				mi.addActionListener(new OpenMenuAction(f));
				recentMenu.add(mi);
			}
		}
		//special
		specialMenu.removeAll();
		JMenuItem mmi = new JMenuItem("Modify This Menu...");
		mmi.setMnemonic(KeyEvent.VK_M);
		mmi.setAccelerator(KeyStroke.getKeyStroke('S', META_ALT_SHIFT_MASK));
		mmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				OpenSpecialWindow.showInstance();
			}
		});
		specialMenu.add(mmi);
		java.util.List<File> sp = ResplPrefs.getFiles("Special Files");
		if (sp != null && sp.size() > 0) {
			specialMenu.addSeparator();
			for (File f : sp) {
				JMenuItem mi = new JMenuItem(f.getName());
				mi.addActionListener(new OpenMenuAction(f));
				specialMenu.add(mi);
			}
		}
	}
	
	private static class OpenMenuAction implements ActionListener {
		private File f;
		public OpenMenuAction(File f) {
			this.f = f;
		}
		public void actionPerformed(ActionEvent ae) {
			ResplMain.resplOpen(f);
		}
	}
}
