package com.kreative.resplendence.editors;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.rsrc.FontResource;

public class NFNTViewer implements ResplendenceEditor { // change class name when viewer becomes editor
	public Image largeIcon() {
		return ResplRsrcs.getPNG("RSRC", "NFNT");
	}

	public String name() {
		return "NFNT Viewer"; // change string when viewer becomes editor
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		if (
				ro.getSize() == 0l
				&& ro.getUDTI().equals("FONT")
				&& ro.getProperty("id") instanceof Number
				&& (((Number)ro.getProperty("id")).intValue() % 128) == 0
		) {
			return new WFontNameWindow(ro);
		} else {
			return new WNFNTWindow(ro);
		}
	}

	public int recognizes(ResplendenceObject ro) {
		if (!ro.isDataType()) {
			return DOES_NOT_RECOGNIZE;
		} else if (FontResource.isMyType(KSFLUtilities.fcc(ro.getUDTI()))) {
			return PREFERRED_EDITOR-256; // upgrade to PREFERRED when viewer becomes editor
		} else {
			return CAN_EDIT_IF_REQUESTED;
		}
	}

	public String shortName() {
		return "NFNT";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("RSRC", "NFNT"));
	}
	
	public static class WFontNameWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		private JTextField nameField;
		
		public WFontNameWindow(ResplendenceObject obj) {
			super(obj, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT
			);
			nameField = new JTextField(obj.getProperty("name").toString());
			nameField.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {
					setChangesMade();
				}
				public void insertUpdate(DocumentEvent e) {
					setChangesMade();
				}
				public void removeUpdate(DocumentEvent e) {
					setChangesMade();
				}
			});
			JPanel main = new JPanel(new BorderLayout(8,8));
			main.add(new JLabel("Font Name:"), BorderLayout.PAGE_START);
			main.add(nameField, BorderLayout.CENTER);
			main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			setContentPane(main);
			pack();
			int h = getHeight();
			setMinimumSize(new Dimension(getMinimumSize().width, h));
			setPreferredSize(new Dimension(getPreferredSize().width, h));
			setMaximumSize(new Dimension(getMaximumSize().width, h));
			setSize(300, h);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			ro.setProperty("name", nameField.getText());
		}
		
		public void revert(ResplendenceObject ro) {
			nameField.setText(ro.getProperty("name").toString());
		}
	}
	
	public static class WNFNTWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		private FontResource.FontInfo myFont;
		private JLabel bitmapView;
		private JComponent alphaPanel;
		private JTextArea textArea;
		private JComponent textPanel;
		
		public WNFNTWindow(ResplendenceObject obj) {
			super(obj, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT
			);
			myFont = new FontResource((short)0, obj.getData()).getInfo();
			
			bitmapView = new JLabel(new ImageIcon(myFont.bitmapImg));
			alphaPanel = new JComponent() {
				private static final long serialVersionUID = 1L;
				protected void paintComponent(Graphics g) {
					Insets i = getInsets();
					int x = i.left;
					int y = i.top;
					int w = getWidth()-i.left-i.right;
					int h = getHeight()-i.top-i.bottom;
					g.setColor(Color.white);
					g.fillRect(x, y, w, h);
					if (myFont != null) {
						g.setColor(Color.black);
						myFont.drawAlphabet(g, x, y+myFont.ascent, w);
					}
				}
			};
			textArea = new JTextArea("The quick brown fox jumped over the lazy dogs with a razorback-jumping frog that could level six piqued gymnasts who speak Latin: Lorem ipsum dolor sit amet...");
			textArea.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {
					if (textPanel != null) textPanel.repaint();
				}
				public void insertUpdate(DocumentEvent arg0) {
					if (textPanel != null) textPanel.repaint();
				}
				public void removeUpdate(DocumentEvent arg0) {
					if (textPanel != null) textPanel.repaint();
				}
			});
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textPanel = new JComponent() {
				private static final long serialVersionUID = 1L;
				protected void paintComponent(Graphics g) {
					Insets i = getInsets();
					int x = i.left;
					int y = i.top;
					int w = getWidth()-i.left-i.right;
					int h = getHeight()-i.top-i.bottom;
					g.setColor(Color.white);
					g.fillRect(x, y, w, h);
					if (myFont != null && textArea != null) {
						g.setColor(Color.black);
						myFont.drawString(g, x, y+myFont.ascent, textArea.getText(), "MACROMAN", w);
					}
				}
			};
			
			JPanel main = new JPanel(new GridLayout(3,1,10,10));
			main.add(alphaPanel);
			main.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			main.add(textPanel);
			JPanel main2 = new JPanel(new BorderLayout(10,10));
			main2.add(new JScrollPane(bitmapView, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.NORTH);
			main2.add(main, BorderLayout.CENTER);
			main2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			setContentPane(main2);
			
			setSize(512, 342);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			// when viewer becomes editor
		}
		
		public void revert(ResplendenceObject ro) {
			// when viewer becomes editor
		}
	}
}
