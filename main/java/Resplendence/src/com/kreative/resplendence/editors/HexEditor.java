package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.swing.*;
import com.kreative.swing.event.*;

public class HexEditor implements ResplendenceEditor {
	public Image largeIcon() {
		return ResplRsrcs.getPNG("RSRC");
	}

	public String name() {
		return "Hex Editor";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WHexEditor(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (ro.isDataType()) {
			return ResplendenceEditor.DEFAULT_EDITOR;
		} else {
			return ResplendenceEditor.DOES_NOT_RECOGNIZE;
		}
	}

	public String shortName() {
		return "Hex";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC"));
	}
	
	public static class WHexEditor extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		private JHexEditorSuite hex;
		public WHexEditor(ResplendenceObject obj) {
			super(obj, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_IMPORT_EXPORT |
					ResplMain.MENUS_SAVE_REVERT |
					ResplMain.MENUS_CUT_COPY_PASTE |
					ResplMain.MENUS_SELECT_ALL |
					ResplMain.MENUS_GO_TO |
					ResplMain.MENUS_FORMAT |
					ResplMain.MENUS_TEXT_ENCODING |
					ResplMain.MENUS_COLOR_SCHEME |
					ResplMain.MENUS_BYTES_PER_ROW |
					ResplMain.MENUS_TEXT_FILTERS |
					ResplMain.MENUS_DATA_FILTERS,
					new JMenu[]{ new HexEditorViewExtra(this) }
			);
			setContentPane(hex = new JHexEditorSuite(obj.getData()));
			pack();
			hex.setWidth(16);
			hex.setIgnoreControlKeys(true);
			hex.addHexDataChangeListener(new HexDataChangeListener() {
				public void hexDataChanged(HexDataChangeEvent e) {
					setChangesMade();
				}
			});
			String s = ResplPrefs.getString("Hex Color Scheme");
			if (s != null && s.length() > 0) {
				for (int i=0; i<JHexEditor.COLOR_SCHEME_COUNT; i++) {
					if (JHexEditor.COLOR_SCHEME_NAMES[i].equalsIgnoreCase(s)) {
						hex.setColorScheme(JHexEditor.COLOR_SCHEMES[i]);
						break;
					}
				}
			}
			this.setSize(getWidth(), 300);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			ro.setData(hex.getData());
		}
		
		public void revert(ResplendenceObject ro) {
			hex.setData(ro.getData());
		}
		
		public Object myRespondToResplendenceEvent(ResplendenceEvent e) {
			switch (e.getID()) {
			case ResplendenceEvent.EXPORT_FILE:
				try {
					RandomAccessFile raf = new RandomAccessFile(e.getFile(), "rwd");
					raf.seek(0);
					raf.setLength(0);
					raf.write(hex.getData());
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
					hex.setData(stuff);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "Could not import because an I/O error occurred.", "Import", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case ResplendenceEvent.CUT:
				if (hex.isOnDataSide()) {
					ResplScrap.setScrap(hex.getSelectedData());
				} else {
					ResplScrap.setScrap(hex.getSelectedHex());
				}
				hex.replaceSelection(new byte[0]);
				break;
			case ResplendenceEvent.COPY:
				if (hex.isOnDataSide()) {
					ResplScrap.setScrap(hex.getSelectedData());
				} else {
					ResplScrap.setScrap(hex.getSelectedHex());
				}
				break;
			case ResplendenceEvent.PASTE:
				if (hex.isOnDataSide()) {
					byte[] data = ResplScrap.getScrapData();
					if (data != null) hex.replaceSelection(data);
				} else {
					String s = ResplScrap.getScrapString();
					if (s != null) hex.replaceSelection(s);
				}
				int cursorIsAt1 = Math.max(hex.getSelectionStart(), hex.getSelectionEnd());
				hex.select(cursorIsAt1, cursorIsAt1);
				break;
			case ResplendenceEvent.PASTE_AFTER:
				if (hex.isOnDataSide()) {
					byte[] data = ResplScrap.getScrapData();
					if (data != null) hex.replaceSelection(data);
				} else {
					String s = ResplScrap.getScrapString();
					if (s != null) hex.replaceSelection(s);
				}
				int cursorIsAt2 = Math.min(hex.getSelectionStart(), hex.getSelectionEnd());
				hex.select(cursorIsAt2, cursorIsAt2);
				break;
			case ResplendenceEvent.CLEAR:
				hex.replaceSelection(new byte[0]);
				break;
			case ResplendenceEvent.SELECT_ALL:
				hex.selectAll();
				break;
			case ResplendenceEvent.GO_TO:
				hex.setIgnoreControlKeys(false);
				hex.getHexEditor().keyPressed(new KeyEvent(
						this,
						KeyEvent.KEY_PRESSED,
						0l,
						KeyEvent.META_DOWN_MASK,
						KeyEvent.VK_J,
						KeyEvent.CHAR_UNDEFINED
				));
				hex.setIgnoreControlKeys(true);
				break;
			case ResplendenceEvent.GET_FONT:
				return hex.getFont();
			case ResplendenceEvent.SET_FONT:
				hex.setFont(e.getFont());
				break;
			case ResplendenceEvent.GET_TEXT_ENCODING:
				return hex.getTextEncoding();
			case ResplendenceEvent.SET_TEXT_ENCODING:
				hex.setTextEncoding(e.getString());
				break;
			case ResplendenceEvent.GET_COLOR_SCHEME:
				return hex.getColorScheme();
			case ResplendenceEvent.SET_COLOR_SCHEME:
				hex.setColorScheme((Color[])e.getObject());
				break;
			case ResplendenceEvent.GET_BYTES_PER_ROW:
				return 0;
			case ResplendenceEvent.SET_BYTES_PER_ROW:
				hex.setWidth(e.getInt());
				break;
			case ResplendenceEvent.GET_SELECTED_DATA:
				return (hex.getSelectionStart()==hex.getSelectionEnd())?hex.getData():hex.getSelectedData();
			case ResplendenceEvent.SET_SELECTED_DATA:
				if (hex.getSelectionStart()==hex.getSelectionEnd()) {
					int ss = hex.getSelectionStart();
					hex.setData(e.getData());
					hex.setSelectionStart(ss);
					hex.setSelectionEnd(ss);
				} else {
					int ss = hex.getSelectionStart();
					hex.replaceSelection(e.getData());
					hex.setSelectionStart(ss);
				}
				return null;
			case ResplendenceEvent.GET_SELECTED_TEXT:
				byte[] outdata = (hex.getSelectionStart()==hex.getSelectionEnd())?hex.getData():hex.getSelectedData();
				try {
					return new String(outdata, hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					return new String(outdata);
				}
			case ResplendenceEvent.SET_SELECTED_TEXT:
				byte[] indata;
				try {
					indata = e.getString().getBytes(hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					indata = e.getString().getBytes();
				}
				if (hex.getSelectionStart()==hex.getSelectionEnd()) {
					int ss = hex.getSelectionStart();
					hex.setData(indata);
					hex.setSelectionStart(ss);
					hex.setSelectionEnd(ss);
				} else {
					int ss = hex.getSelectionStart();
					hex.replaceSelection(indata);
					hex.setSelectionStart(ss);
				}
				return null;
			case ResplendenceEvent.INSERT_TEXT:
				byte[] insdata;
				try {
					insdata = e.getString().getBytes(hex.getTextEncoding());
				} catch (UnsupportedEncodingException uee) {
					insdata = e.getString().getBytes();
				}
				{
					int ss = hex.getSelectionStart();
					hex.replaceSelection(insdata);
					hex.setSelectionStart(ss+insdata.length);
					hex.setSelectionEnd(ss+insdata.length);
				}
				return null;
			case ResplendenceEvent.GET_CONTROL_PROCID:
			case ResplendenceEvent.GET_WINDOW_PROCID:
				if (hex.getSelectionLength() == 2) {
					byte[] b = hex.getSelectedData();
					return hex.isLittleEndian()?(((b[1]&0xFF)<<8)|(b[0]&0xFF)):(((b[0]&0xFF)<<8)|(b[1]&0xFF));
				}
				return null;
			case ResplendenceEvent.SET_CONTROL_PROCID:
			case ResplendenceEvent.SET_WINDOW_PROCID:
				if (e.getObject() instanceof Number && hex.getSelectionLength() == 2) {
					int i = ((Number)e.getObject()).intValue();
					byte[] b = hex.isLittleEndian()?(new byte[]{(byte)(i&0xFF),(byte)((i>>8)&0xFF)}):(new byte[]{(byte)((i>>8)&0xFF),(byte)(i&0xFF)});
					hex.replaceSelection(b);
				}
				return null;
			}
			return null;
		}
	}
	
	private static class HexEditorViewExtra extends JMenu {
		private static final long serialVersionUID = 1L;
		private WHexEditor hex;
		public HexEditorViewExtra(WHexEditor hexeditor) {
			super("View");
			this.hex = hexeditor;
			
			JMenu mEndianness = new JMenu("Endianness");
			JMenuItem mEndiannessBig = new JMenuItem("Big");
			mEndiannessBig.setMnemonic(KeyEvent.VK_B);
			mEndiannessBig.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setBigEndian(true); }});
			mEndianness.add(mEndiannessBig);
			JMenuItem mEndiannessLil = new JMenuItem("Little");
			mEndiannessLil.setMnemonic(KeyEvent.VK_L);
			mEndiannessLil.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setBigEndian(false); }});
			mEndianness.add(mEndiannessLil);
			add(mEndianness);
			
			JMenu mInsert = new JMenu("Insert Mode");
			JMenuItem mInsertInsert = new JMenuItem("Insert");
			mInsertInsert.setMnemonic(KeyEvent.VK_I);
			mInsertInsert.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setOvertypeMode(false); }});
			mInsert.add(mInsertInsert);
			JMenuItem mInsertOvertype = new JMenuItem("Overtype");
			mInsertOvertype.setMnemonic(KeyEvent.VK_O);
			mInsertOvertype.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setOvertypeMode(true); }});
			mInsert.add(mInsertOvertype);
			add(mInsert);
			
			JMenu mAddresses = new JMenu("Addresses");
			JMenuItem mAddressesHex = new JMenuItem("Hexadecimal");
			mAddressesHex.setMnemonic(KeyEvent.VK_H);
			mAddressesHex.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setDecimalAddresses(false); }});
			mAddresses.add(mAddressesHex);
			JMenuItem mAddressesDec = new JMenuItem("Decimal");
			mAddressesDec.setMnemonic(KeyEvent.VK_D);
			mAddressesDec.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { hex.hex.setDecimalAddresses(true); }});
			mAddresses.add(mAddressesDec);
			add(mAddresses);
		}
	}
}
