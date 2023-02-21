package com.kreative.resplendence.menus;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.*;

public class FileMenu extends JMenu implements MenuConstants {
	private static final long serialVersionUID = 1L;
	
	private static String lastOpenDirectory = null;
	private static String lastSaveDirectory = null;
	
	private JMenu recentMenu, specialMenu;
	
	public FileMenu(long features) {
		super("File");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_F);
		
		JMenuItem mi;
		if (mac || (features & MENUS_GLOBAL)>0) {
			mi = new JMenuItem("New File...");
			mi.setMnemonic(KeyEvent.VK_N);
			mi.setAccelerator(KeyStroke.getKeyStroke('N', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "New File", FileDialog.SAVE);
					if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					final File f = new File((lastSaveDirectory = ds), fs);
					Window w = new NewFileWindow(f, null);
					w.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent we) {
							ResplMain.resplOpen(f);
						}
					});
				}
			});
			add(mi);
			mi = new JMenuItem("New Folder...");
			mi.setMnemonic(KeyEvent.VK_W);
			mi.setAccelerator(KeyStroke.getKeyStroke('N', META_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "New Folder", FileDialog.SAVE);
					if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastSaveDirectory = ds), fs);
					try {
						file.mkdir();
						ResplMain.resplOpen(file);
					} catch (Exception ioe) {
						JOptionPane.showMessageDialog(null, "The folder could not be created.", "New Folder", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open File...");
			mi.setMnemonic(KeyEvent.VK_O);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "Open File", FileDialog.LOAD);
					if (lastOpenDirectory != null) fd.setDirectory(lastOpenDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastOpenDirectory = ds), fs);
					ResplMain.resplOpen(file);
				}
			});
			add(mi);
			mi = new JMenuItem("Open Folder...");
			mi.setMnemonic(KeyEvent.VK_D);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_ALT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.putClientProperty("JFileChooser.packageIsTraversable", "always");
					fc.putClientProperty("JFileChooser.appBundleIsTraversable", "always");
					if (lastOpenDirectory != null) fc.setCurrentDirectory(new File(lastOpenDirectory));
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setFileHidingEnabled(true);
					fc.setMultiSelectionEnabled(false);
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						lastOpenDirectory = fc.getCurrentDirectory().getPath();
						ResplMain.resplOpen(fc.getSelectedFile());
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open Anything...");
			mi.setMnemonic(KeyEvent.VK_H);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_ALT_SHIFT_MASK));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.putClientProperty("JFileChooser.packageIsTraversable", "always");
					fc.putClientProperty("JFileChooser.appBundleIsTraversable", "always");
					if (lastOpenDirectory != null) fc.setCurrentDirectory(new File(lastOpenDirectory));
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.setFileHidingEnabled(false);
					fc.setMultiSelectionEnabled(false);
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						lastOpenDirectory = fc.getCurrentDirectory().getPath();
						ResplMain.resplOpen(fc.getSelectedFile());
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
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "Import", FileDialog.LOAD);
					if (lastOpenDirectory != null) fd.setDirectory(lastOpenDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastOpenDirectory = ds), fs);
					ResplMain.sendResplendenceEventToFrontmost(
						new ResplendenceEvent(
							e.getSource(),
							ResplendenceEvent.IMPORT_FILE,
							"Import File...",
							file
						)
					);
				}
			});
			add(mi);
			mi = new JMenuItem("Export...");
			mi.setMnemonic(KeyEvent.VK_E);
			mi.setAccelerator(KeyStroke.getKeyStroke('E', META_ALT_MASK));
			mi.setEnabled((features & MENUS_IMPORT_EXPORT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "Export", FileDialog.SAVE);
					if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastSaveDirectory = ds), fs);
					ResplMain.sendResplendenceEventToFrontmost(
						new ResplendenceEvent(
							e.getSource(),
							ResplendenceEvent.EXPORT_FILE,
							"Export File...",
							file
						)
					);
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
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE);
					if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastSaveDirectory = ds), fs);
					ResplMain.sendResplendenceEventToFrontmost(
						new ResplendenceEvent(
							e.getSource(),
							ResplendenceEvent.SAVE_AS,
							"Save As...",
							file
						)
					);
				}
			});
			add(mi);
			mi = new JMenuItem("Save a Copy...");
			mi.setMnemonic(KeyEvent.VK_Y);
			mi.setEnabled((features & MENUS_SAVE_REVERT)>0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog fd = new FileDialog(frame, "Save a Copy", FileDialog.SAVE);
					if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
					fd.setVisible(true);
					String ds = fd.getDirectory(), fs = fd.getFile();
					fd.dispose();
					frame.dispose();
					if (ds == null || fs == null) return;
					File file = new File((lastSaveDirectory = ds), fs);
					ResplMain.sendResplendenceEventToFrontmost(
						new ResplendenceEvent(
							e.getSource(),
							ResplendenceEvent.SAVE_A_COPY,
							"Save a Copy...",
							file
						)
					);
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
