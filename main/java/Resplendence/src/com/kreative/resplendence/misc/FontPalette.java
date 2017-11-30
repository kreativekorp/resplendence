package com.kreative.resplendence.misc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.awt.*;
import com.kreative.dff.DFFResource;
import com.kreative.ksfl.KSFLConstants;
import com.kreative.resplendence.*;
import com.kreative.rsrc.StringListResource;
import com.kreative.swing.JPalette;

public class FontPalette extends JPalette implements WindowListener, ListSelectionListener {
	private static final long serialVersionUID = 1;
	
	private static FontPalette instance = null;
	public static FontPalette getInstance() {
		if (instance == null) instance = new FontPalette();
		return instance;
	}
	public static void showInstance() {
		if (instance == null) instance = new FontPalette();
		instance.setVisible(true);
	}
	
	private static final int[] sizes = new int[]{ 7, 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 36, 48, 56, 64, 72, 84, 96, 100, 128, 200, 250, 256, 300, 400, 500, 512, 750 };
	private static final String[] styleNames = new String[]{ "Plain", "Bold", "Italic", "Bold Italic" };
	private static final int[] styles = new int[]{ Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD|Font.ITALIC };
	
	private boolean eventexec = false;
	private Map<String,Vector<String>> fontLists2;
	private Vector<String> allFonts;
	private DefaultListModel collectionList;
	private JList collectionView;
	private DefaultListModel fontList;
	private JList fontView;
	private DefaultListModel styleList;
	private JList styleView;
	private DefaultListModel sizeList;
	private JList sizeView;
	
	public FontPalette() {
		fontLists2 = new HashMap<String,Vector<String>>();
		allFonts = new Vector<String>();
		fontLists2.put("All Fonts", allFonts);
		for (int id : ResplRsrcs.getAppDFFResourceProvider().getIDs(KSFLConstants.FontList)) {
			DFFResource r = ResplRsrcs.getAppDFFResourceProvider().get(KSFLConstants.FontList, id);
			StringListResource slr = new StringListResource((short)0, r.data);
			Vector<String> s = new Vector<String>();
			s.addAll(Arrays.asList(slr.getStrings()));
			fontLists2.put(r.name, s);
		}
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String font : fonts) {
			allFonts.add(font);
			for (Map.Entry<String,Vector<String>> fontList : fontLists2.entrySet()) {
				if (fontList.getValue().contains(font)) {
					fontLists2.get(fontList.getKey()).add(font);
				}
			}
		}
		
		collectionList = new DefaultListModel();
		Vector<String> colls = new Vector<String>();
		for (Map.Entry<String, Vector<String>> fontList2 : fontLists2.entrySet()) {
			if (!fontList2.getValue().isEmpty()) colls.add(fontList2.getKey());
		}
		Collections.sort(colls);
		for (String coll : colls) collectionList.addElement(coll);
		collectionView = new JList(collectionList);
		JScrollPane collectionPane = new JScrollPane(collectionView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		fontList = new DefaultListModel();
		fontView = new JList(fontList);
		JScrollPane fontPane = new JScrollPane(fontView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		styleList = new DefaultListModel();
		for (String style : styleNames) styleList.addElement(style);
		styleView = new JList(styleList);
		JScrollPane stylePane = new JScrollPane(styleView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		sizeList = new DefaultListModel();
		for (int size : sizes) sizeList.addElement(size);
		sizeView = new JList(sizeList);
		JScrollPane sizePane = new JScrollPane(sizeView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		collectionView.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				boolean oldeventexec = eventexec;
				eventexec = true;
				fontList.removeAllElements();
				Object cn = collectionView.getSelectedValue();
				if (fontLists2.containsKey(cn)) {
					Vector<String> fonts = fontLists2.get(cn);
					for (String font : fonts) fontList.addElement(font);
				}
				Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(collectionView,ResplendenceEvent.GET_FONT,"valueChanged",null));
				if (o instanceof Font) {
					fontView.setSelectedValue(((Font)o).getFamily(), true);
				}
				eventexec = oldeventexec;
			}
		});
		
		fontView.addListSelectionListener(this);
		styleView.addListSelectionListener(this);
		sizeView.addListSelectionListener(this);
		
		JPanel fontPanel = new JPanel(new FractionalSizeGridLayout(1,3));
		fontPanel.add(collectionPane);
		fontPanel.add(fontPane);
		fontPanel.add(stylePane);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(fontPanel, BorderLayout.CENTER);
		main.add(sizePane, BorderLayout.EAST);
		
		this.setTitle("Fonts");
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setContentPane(main);
		this.pack();
		this.setSize(480, 320);
		this.setLocationRelativeTo(null);
		
		ResplMain.addWindowListener(this);
		updateSelection(this);
	}
	
	public void dispose() {
		ResplMain.removeWindowListener(this);
		super.dispose();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (!eventexec) {
			eventexec = true;
			try {
				String fn = fontView.getSelectedValue().toString();
				int st = styles[styleView.getSelectedIndex()];
				int sz = ((Number)sizeView.getSelectedValue()).intValue();
				Font font = new Font(fn, st, sz);
				ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(sizeView,ResplendenceEvent.SET_FONT,"valueChanged",font));
			} catch (Exception ex) {}
			eventexec = false;
		}
	}
	
	public void updateSelection(Object src) {
		Object o = ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(src,ResplendenceEvent.GET_FONT,"updateSelection",null));
		if (o instanceof Font) {
			eventexec = true;
			Font f = (Font)o;
			if (
					collectionView.getSelectedValue() == null
					|| !fontList.contains(f.getFamily())
			) {
				collectionView.setSelectedValue("All Fonts", true);
			}
			fontView.setSelectedValue(f.getFamily(), true);
			int style = f.getStyle();
			for (int i=0; i<styleNames.length && i<styles.length; i++) {
				if (styles[i] == style) styleView.setSelectedValue(styleNames[i], true);
			}
			sizeView.setSelectedValue(f.getSize(), true);
			eventexec = false;
		}
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
