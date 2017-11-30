package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import com.kreative.resplendence.*;

public class OpenWithApplicationWindow implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("APP");
	}

	public String name() {
		return "Open with Application";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WOpenWithApplication(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			return ResplendenceEditor.CAN_EDIT_IF_REQUESTED;
		} else {
			return ResplendenceEditor.DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Application";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("APP"));
	}
	
	public static class WOpenWithApplication extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1L;
		private File ext;
		private JFileChooser choose;
		public WOpenWithApplication(ResplendenceObject obj) {
			super(obj, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT
			);
			if (obj.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) {
				ext = obj.getNativeFile();
			} else {
				try {
					ext = File.createTempFile(obj.getTitleForExportedFile()+"-", "");
					RandomAccessFile dst = new RandomAccessFile(ext, "rwd");
					dst.seek(0);
					dst.setLength(0);
					RandomAccessFile src = obj.getRandomAccessData("r");
					if (src != null) {
						src.seek(0);
						byte[] buf = new byte[1048576];
						while (src.getFilePointer() < src.length()) {
							int l = src.read(buf);
							dst.write(buf, 0, l);
						}
						src.close();
					} else {
						dst.write(obj.getData());
					}
					dst.close();
				} catch (IOException ioe) {
					ext = null;
				}
			}
			
			JPanel main = new JPanel(new BorderLayout(12,12));
			main.add(new JLabel("<html>Select an application to open this object.<br>Keep this window open until you're done editing, then save.</html>"), BorderLayout.PAGE_START);
			main.add(choose = new JFileChooser(), BorderLayout.CENTER);
			choose.setControlButtonsAreShown(false);
			if (ResplMain.RUNNING_ON_MAC_OS) {
				choose.putClientProperty("JFileChooser.packageIsTraversable", "always");
				choose.putClientProperty("JFileChooser.appBundleIsTraversable", "always");
			}
			JPanel buttons = new JPanel();
			JButton openButton = new JButton("Open");
			openButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					WOpenWithApplication.this.setChangesMade();
					String exepath = choose.getSelectedFile().getAbsolutePath();
					String docpath = ext.getAbsolutePath();
					try {
						Runtime.getRuntime().exec(new String[] {exepath, docpath});
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(WOpenWithApplication.this, "Could not open the application.", "Open with Application", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			buttons.add(openButton);
			main.add(buttons, BorderLayout.PAGE_END);
			main.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
			
			this.setContentPane(main);
			this.getRootPane().setDefaultButton(openButton);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
		public void save(ResplendenceObject obj) {
			if (obj == getResplendenceObject() && obj.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) return;
			try {
				RandomAccessFile src = new RandomAccessFile(ext, "r");
				src.seek(0);
				RandomAccessFile dst = obj.getRandomAccessData("rwd");
				if (dst != null) {
					dst.seek(0);
					dst.setLength(0);
					byte[] buf = new byte[1048576];
					while (src.getFilePointer() < src.length()) {
						int l = src.read(buf);
						dst.write(buf, 0, l);
					}
					dst.close();
				} else {
					byte[] b = new byte[(int)Math.min(src.length(), Integer.MAX_VALUE)];
					src.read(b);
					obj.setData(b);
				}
				src.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		public void revert(ResplendenceObject obj) {
			if (obj == getResplendenceObject() && obj.getType().equals(ResplendenceObject.TYPE_FSOBJECT)) return;
			try {
				RandomAccessFile dst = new RandomAccessFile(ext, "rwd");
				dst.seek(0);
				dst.setLength(0);
				RandomAccessFile src = obj.getRandomAccessData("r");
				if (src != null) {
					src.seek(0);
					byte[] buf = new byte[1048576];
					while (src.getFilePointer() < src.length()) {
						int l = src.read(buf);
						dst.write(buf, 0, l);
					}
					src.close();
				} else {
					dst.write(obj.getData());
				}
				dst.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
