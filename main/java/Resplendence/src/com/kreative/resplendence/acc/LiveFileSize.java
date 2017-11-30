package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.util.List;
import java.io.File;
import javax.swing.*;
import com.kreative.resplendence.*;

public class LiveFileSize implements AccessoryWindow {
	public String category(int i) {
		return null;
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}

	public String name(int i) {
		return "LiveFileSize";
	}

	public void open(int i) {
		WLiveFileSize instance = new WLiveFileSize();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WLiveFileSize extends JFrame implements DropTargetListener {
		private static final long serialVersionUID = 1;
		private JLabel fileNameLabel;
		private JLabel fileSizeLabel;
		private File theFile;
		private FileSizeUpdater theUpdater;
		public WLiveFileSize() {
			super("LiveFileSize");
			ResplMain.registerWindow(this);
			fileNameLabel = new JLabel("Drop File Here");
			fileSizeLabel = new JLabel("Size Will Appear Here");
			theFile = null;
			theUpdater = new FileSizeUpdater();
			JPanel main = new JPanel(new BorderLayout(20, 20));
			main.add(fileNameLabel, BorderLayout.CENTER);
			main.add(fileSizeLabel, BorderLayout.SOUTH);
			main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			new DropTarget(main, this);
			setContentPane(main);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setResizable(true);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
			theUpdater.start();
		}
		public void dispose() {
			theUpdater.interrupt();
			super.dispose();
		}
		public void drop(DropTargetDropEvent dtde) {
			try {
				int action = dtde.getDropAction();
				dtde.acceptDrop(action);
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					List<?> list = (List<?>)t.getTransferData(DataFlavor.javaFileListFlavor);
					if (list.size() == 1) {
						File f = (File)list.get(0);
						fileNameLabel.setText(f.getName());
						fileSizeLabel.setText(ResplUtils.describeSize(f.length()));
						theFile = f;
						dtde.dropComplete(true);
					}
				}
			} catch (Exception e) {}
			dtde.dropComplete(false);
		}
		public void dragEnter(DropTargetDragEvent dtde) {}
		public void dragExit(DropTargetEvent dte) {}
		public void dragOver(DropTargetDragEvent dtde) {}
		public void dropActionChanged(DropTargetDragEvent dtde) {}
		private class FileSizeUpdater extends Thread {
			public void run() {
				try {
					while (!Thread.interrupted()) {
						if (theFile != null) {
							fileSizeLabel.setText(ResplUtils.describeSize(theFile.length()));
						}
						Thread.sleep(500);
					}
				} catch (InterruptedException ie) {}
			}
		}
	}
}
