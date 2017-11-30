package com.kreative.resplendence.editors;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.ksfl.KSFLUtilities;
import com.kreative.resplendence.*;
import com.kreative.resplendence.template.*;

public class FinderInfoEditor implements ResplendenceEditor {
	private static final int PRODOS_CREATOR = 0x70646F73;
	private static final ArrayList<String> PRODOS_TYPES = new ProDOSFileTypeList();
	
	public Image largeIcon() {
		return ResplRsrcs.getPNG("META", "finf");
	}

	public String name() {
		return "Finder Info";
	}

	public ResplendenceEditorWindow openEditor(ResplendenceObject ro) {
		return new WFinderInfoWindow(ro);
	}

	public int recognizes(ResplendenceObject ro) {
		if (!ro.isDataType()) {
			return DOES_NOT_RECOGNIZE;
		} else if (ro.getType().equals(ResplendenceObject.TYPE_META) && ro.getNativeFile().getName().equals("finf")) {
			return PREFERRED_EDITOR;
		} else if (ro.getSize() <= 32) {
			return CAN_EDIT_IF_REQUESTED;
		} else {
			return REFUSE_TO_EDIT;
		}
	}

	public String shortName() {
		return "Finder Info";
	}

	public Image smallIcon() {
		return ResplUtils.shrink(ResplRsrcs.getPNG("META", "finf"));
	}
	
	public static class WFinderInfoWindow extends ResplendenceEditorWindow {
		private static final long serialVersionUID = 1;
		
		private byte[] info = new byte[32];
		
		private JLabel semLabel = new JLabel("Semantics:");
		private ButtonGroup semType = new ButtonGroup();
		private JRadioButton semFile = new JRadioButton("File");
		private JRadioButton semFolder = new JRadioButton("Folder");
		private ButtonGroup semVersion = new ButtonGroup();
		private JRadioButton semSix = new JRadioButton("1.0-6.0.8");
		private JRadioButton semSeven = new JRadioButton("7.0-X");
		private JLabel typeLabel = new JLabel("File Type:");
		private JLabel creatorLabel = new JLabel("Creator:");
		private JTextField typeField = new JTextField("", 4);
		private JTextField creatorField = new JTextField("", 4);
		private JButton typeClear = new JButton("Clear");
		private JButton creatorClear = new JButton("Clear");
		private JButton creatorProdos = new JButton("ProDOSify");
		private JLabel pdtypeLabel = new JLabel("ProDOS File Type:");
		private JLabel pdauxLabel = new JLabel("Auxiliary Type:");
		private JTextField pdtypeField = new JTextField("00", 2);
		private JTextField pdmneuField = new JTextField("UNK", 3);
		private JTextField pdauxField = new JTextField("0000", 4);
		private JLabel winRectLabel = new JLabel("Window Rect:");
		private JTextField winRectT = new JTextField("0", 6);
		private JTextField winRectL = new JTextField("0", 6);
		private JTextField winRectB = new JTextField("0", 6);
		private JTextField winRectR = new JTextField("0", 6);
		private JButton winRectSet = new JButton("Set");
		private JButton winRectEdit = new JButton("Edit");
		private JCheckBox flagOnDesk = new JCheckBox("On Desktop");
		private JLabel flagLabelLabel = new JLabel("Label:");
		private DefaultComboBoxModel flagLabelModel = new DefaultComboBoxModel(new LabelString[] {
				LabelString.NONE,
				LabelString.ESSENTIAL,
				LabelString.HOT,
				LabelString.INPROGRESS,
				LabelString.COOL,
				LabelString.PERSONAL,
				LabelString.PROJECT1,
				LabelString.PROJECT2
		});
		private JComboBox flagLabelPopup = new JComboBox(flagLabelModel);
		private JCheckBox flagHideExt = new JCheckBox("Hide Extension");
		private JCheckBox flagSwLaunch = new JCheckBox("SwitchLaunch");
		private JCheckBox flagShared = new JCheckBox("Shared");
		private JCheckBox flagNoInits = new JCheckBox("No INITs");
		private JCheckBox flagInited = new JCheckBox("Inited");
		private JCheckBox flagChanged = new JCheckBox("Changed");
		private JCheckBox flagCustIcon = new JCheckBox("Custom Icon");
		private JCheckBox flagStationery = new JCheckBox("Stationery");
		private JCheckBox flagNameLock = new JCheckBox("Name Locked");
		private JCheckBox flagHasBNDL = new JCheckBox("Has BNDL");
		private JCheckBox flagInvisible = new JCheckBox("Invisible");
		private JCheckBox flagAlias = new JCheckBox("Alias");
		private JLabel iconLocLabel = new JLabel("Icon Location:");
		private JTextField iconLocX = new JTextField("0", 6);
		private JTextField iconLocY = new JTextField("0", 6);
		private JLabel dirIDLabel = new JLabel("Dir ID:");
		private JTextField dirIDField = new JTextField("0", 6);
		private JLabel iconIDLabel = new JLabel("Icon ID:");
		private JTextField iconIDField = new JTextField("0", 6);
		private JLabel rsvdLabel = new JLabel("Reserved:");
		private JTextField rsvd1 = new JTextField("0", 6);
		private JTextField rsvd2 = new JTextField("0", 6);
		private JTextField rsvd3 = new JTextField("0", 6);
		private JLabel scrollLabel = new JLabel("Scroll Pos:");
		private JTextField scrollX = new JTextField("0", 6);
		private JTextField scrollY = new JTextField("0", 6);
		private JLabel ochainLabel = new JLabel("Open Chain:");
		private JTextField ochainField = new JTextField("0", 11);
		private JLabel scriptIDLabel = new JLabel("Script ID:");
		private JTextField scriptIDField = new JTextField("0", 4);
		private JLabel rflagLabel = new JLabel("Reserved:");
		private JTextField rflagField = new JTextField("0", 4);
		private JCheckBox xflag0 = new JCheckBox("Reserved");
		private JCheckBox xflag1 = new JCheckBox("Reserved");
		private JCheckBox xflagRoutInfo = new JCheckBox("Has Routing Info");
		private JCheckBox xflag3 = new JCheckBox("Reserved");
		private JCheckBox xflag4 = new JCheckBox("Reserved");
		private JCheckBox xflag5 = new JCheckBox("Reserved");
		private JCheckBox xflag6 = new JCheckBox("Reserved");
		private JCheckBox xflagBusy = new JCheckBox("Object Busy");
		private JCheckBox xflagCustBadge = new JCheckBox("Custom Badge");
		private JCheckBox xflag9 = new JCheckBox("Reserved");
		private JCheckBox xflag10 = new JCheckBox("Reserved");
		private JCheckBox xflag11 = new JCheckBox("Reserved");
		private JCheckBox xflag12 = new JCheckBox("Reserved");
		private JCheckBox xflag13 = new JCheckBox("Reserved");
		private JCheckBox xflag14 = new JCheckBox("Reserved");
		private JCheckBox xflagInvalid = new JCheckBox("XFlags Invalid");
		private JLabel commentIDLabel = new JLabel("Comment ID:");
		private JTextField commentIDField = new JTextField("0", 6);
		private JLabel putAwayIDLabel = new JLabel("Put Away Dir ID:");
		private JTextField putAwayIDField = new JTextField("0", 11);
		
		private JPanel main;
		private JPanel semPanel;
		private JPanel typeCreatorPanel;
		private JPanel pdtypePanel;
		private JPanel winRectPanel;
		private JPanel flagPanel;
		private JPanel iconLocDirIDPanel;
		private JPanel iconIDPanel;
		private JPanel scrollOchainPanel;
		private JPanel scriptIDPanel;
		private JPanel xflagPanel;
		private JPanel commentIDPutAwayPanel;
		
		private boolean updatingGUI = false;
		
		public WFinderInfoWindow(ResplendenceObject obj) {
			super(obj, false);
			register(
					ResplMain.MENUS_GLOBAL |
					ResplMain.MENUS_SAVE_REVERT
			);
			info = obj.getData();
			semFile.setSelected(true);
			semFolder.setSelected(false);
			semSix.setSelected(false);
			semSeven.setSelected(true);
			semType.add(semFile);
			semType.add(semFolder);
			semVersion.add(semSix);
			semVersion.add(semSeven);
			update();
			semFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fileSemantics();
				}
			});
			semFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					folderSemantics();
				}
			});
			semSix.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					sys6Semantics();
				}
			});
			semSeven.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					sys7Semantics();
				}
			});
			typeField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						KSFLUtilities.putInt(info, 0, KSFLUtilities.fcc(typeField.getText()));
						updatePDType();
						updateWinRect();
						unlockGUI();
					}
				}
			});
			creatorField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						KSFLUtilities.putInt(info, 4, KSFLUtilities.fcc(creatorField.getText()));
						updatePDType();
						updateWinRect();
						unlockGUI();
					}
				}
			});
			typeClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					if (lockGUI()) {
						setChangesMade();
						KSFLUtilities.putInt(info, 0, 0);
						typeField.setText("");
						updatePDType();
						updateWinRect();
						unlockGUI();
					}
				}
			});
			creatorClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					if (lockGUI()) {
						setChangesMade();
						KSFLUtilities.putInt(info, 4, 0);
						creatorField.setText("");
						updatePDType();
						updateWinRect();
						unlockGUI();
					}
				}
			});
			creatorProdos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					if (!pdShouldBeVisible()) {
						if (lockGUI()) {
							setChangesMade();
							info[0] = 'p';
							info[1] = 0x06;
							KSFLUtilities.putShort(info, 2, (short)0x2000);
							KSFLUtilities.putInt(info, 4, PRODOS_CREATOR);
							updateTypeCreator();
							updatePDType();
							updateWinRect();
							unlockGUI();
						}
					}
				}
			});
			pdtypeField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							info[1] = (byte)Integer.parseInt(pdtypeField.getText(), 16);
							info[0] = 'p';
							KSFLUtilities.putInt(info, 4, PRODOS_CREATOR);
							pdmneuField.setText(PRODOS_TYPES.get(info[1] & 0xFF));
							updateTypeCreator();
							updateWinRect();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			pdmneuField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						int idx;
						String ts = pdmneuField.getText();
						if (ts.startsWith("$")) {
							try {
								idx = Integer.parseInt(ts.substring(1), 16);
							} catch (NumberFormatException nfe) {
								idx = 0;
							}
						} else if (ts.matches("[Uu][Dd][1-8]")) {
							try {
								idx = 0xF0 + Integer.parseInt(ts.substring(2));
							} catch (NumberFormatException nfe) {
								idx = 0;
							}
						} else {
							idx = PRODOS_TYPES.indexOf(ts.toUpperCase());
						}
						if (idx < 0 || idx >= 256) idx = 0;
						info[0] = 'p';
						info[1] = (byte)idx;
						KSFLUtilities.putInt(info, 4, PRODOS_CREATOR);
						String s = "00"+Integer.toHexString(idx).toUpperCase();
						pdtypeField.setText(s.substring(s.length()-2));
						updateTypeCreator();
						updateWinRect();
						unlockGUI();
					}
				}
			});
			pdauxField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 2, (short)Integer.parseInt(pdauxField.getText(), 16));
							info[0] = 'p';
							KSFLUtilities.putInt(info, 4, PRODOS_CREATOR);
							updateTypeCreator();
							updateWinRect();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			winRectT.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 0, (short)Integer.parseInt(winRectT.getText()));
							updateTypeCreator();
							updatePDType();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			winRectL.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 2, (short)Integer.parseInt(winRectL.getText()));
							updateTypeCreator();
							updatePDType();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			winRectB.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 4, (short)Integer.parseInt(winRectB.getText()));
							updateTypeCreator();
							updatePDType();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			winRectR.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 6, (short)Integer.parseInt(winRectR.getText()));
							updateTypeCreator();
							updatePDType();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			winRectSet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final RectangleSetWindow f = new RectangleSetWindow();
					f.addWindowListener(new WindowListener() {
						public void windowActivated(WindowEvent arg0) {}
						public void windowClosed(WindowEvent arg0) {
							windowClosing(arg0);
						}
						public void windowClosing(WindowEvent arg0) {
							winRectL.setText(Integer.toString(f.x1));
							winRectT.setText(Integer.toString(f.y1));
							winRectR.setText(Integer.toString(f.x2));
							winRectB.setText(Integer.toString(f.y2));
						}
						public void windowDeactivated(WindowEvent arg0) {
							windowClosing(arg0);
						}
						public void windowDeiconified(WindowEvent arg0) {}
						public void windowIconified(WindowEvent arg0) {}
						public void windowOpened(WindowEvent arg0) {}
					});
				}
			});
			winRectEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int x1=100, y1=100, w1=100, h1=100;
					try {
						x1 = (int)Long.parseLong(winRectL.getText());
						y1 = (int)Long.parseLong(winRectT.getText());
						w1 = (int)Long.parseLong(winRectR.getText())-x1;
						h1 = (int)Long.parseLong(winRectB.getText())-y1;
					} catch (NumberFormatException nfe) {}
					JFrame f = new RectangleEditWindow(new Rectangle(x1, y1, w1, h1));
					f.addComponentListener(new ComponentListener() {
						public void componentHidden(ComponentEvent ce) {}
						public void componentShown(ComponentEvent ce) {}
						public void componentMoved(ComponentEvent ce) {
							Rectangle r = ce.getComponent().getBounds();
							winRectL.setText(Integer.toString(r.x));
							winRectT.setText(Integer.toString(r.y));
							winRectR.setText(Integer.toString(r.x+r.width));
							winRectB.setText(Integer.toString(r.y+r.height));
						}
						public void componentResized(ComponentEvent ce) {
							Rectangle r = ce.getComponent().getBounds();
							winRectL.setText(Integer.toString(r.x));
							winRectT.setText(Integer.toString(r.y));
							winRectR.setText(Integer.toString(r.x+r.width));
							winRectB.setText(Integer.toString(r.y+r.height));
						}
					});
				}
			});
			flagOnDesk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(9, 0x01, flagOnDesk.isSelected());
						unlockGUI();
					}
				}
			});
			flagLabelPopup.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						if (arg0.getStateChange() == ItemEvent.SELECTED) {
							info[9] &= 0xF1;
							info[9] |= ((flagLabelPopup.getSelectedIndex() << 1) & 0x0E);
						}
						unlockGUI();
					}
				}
			});
			flagHideExt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(9, 0x10, flagHideExt.isSelected());
						unlockGUI();
					}
				}
			});
			flagSwLaunch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(9, 0x20, flagSwLaunch.isSelected());
						unlockGUI();
					}
				}
			});
			flagShared.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(9, 0x40, flagShared.isSelected());
						unlockGUI();
					}
				}
			});
			flagNoInits.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(9, 0x80, flagNoInits.isSelected());
						unlockGUI();
					}
				}
			});
			flagInited.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x01, flagInited.isSelected());
						unlockGUI();
					}
				}
			});
			flagChanged.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x02, flagChanged.isSelected());
						unlockGUI();
					}
				}
			});
			flagCustIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x04, flagCustIcon.isSelected());
						unlockGUI();
					}
				}
			});
			flagStationery.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x08, flagStationery.isSelected());
						unlockGUI();
					}
				}
			});
			flagNameLock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x10, flagNameLock.isSelected());
						unlockGUI();
					}
				}
			});
			flagHasBNDL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x20, flagHasBNDL.isSelected());
						unlockGUI();
					}
				}
			});
			flagInvisible.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x40, flagInvisible.isSelected());
						unlockGUI();
					}
				}
			});
			flagAlias.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(8, 0x80, flagAlias.isSelected());
						unlockGUI();
					}
				}
			});
			iconLocY.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 10, (short)Integer.parseInt(iconLocY.getText()));
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			iconLocX.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 12, (short)Integer.parseInt(iconLocX.getText()));
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			dirIDField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 14, (short)Integer.parseInt(dirIDField.getText()));
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			iconIDField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 16, (short)Integer.parseInt(iconIDField.getText()));
							updateScrollOchain();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			rsvd1.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 18, (short)Integer.parseInt(rsvd1.getText()));
							updateScrollOchain();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			rsvd2.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 20, (short)Integer.parseInt(rsvd2.getText()));
							updateScrollOchain();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			rsvd3.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 22, (short)Integer.parseInt(rsvd3.getText()));
							updateScrollOchain();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			scrollY.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 16, (short)Integer.parseInt(scrollY.getText()));
							updateIconID();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			scrollX.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 18, (short)Integer.parseInt(scrollX.getText()));
							updateIconID();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			ochainField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putInt(info, 20, Integer.parseInt(ochainField.getText()));
							updateIconID();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			scriptIDField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							info[24] = (byte)Integer.parseInt(scriptIDField.getText());
							updateXFlags();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			rflagField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							info[25] = (byte)Integer.parseInt(rflagField.getText());
							updateXFlags();
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			xflag0.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x01, xflag0.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x02, xflag1.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflagRoutInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x04, xflagRoutInfo.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x08, xflag3.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x10, xflag4.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x20, xflag5.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x40, xflag6.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflagBusy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(25, 0x80, xflagBusy.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflagCustBadge.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x01, xflagCustBadge.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x02, xflag9.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x04, xflag10.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x08, xflag11.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x10, xflag12.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag13.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x20, xflag13.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflag14.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x40, xflag14.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			xflagInvalid.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (lockGUI()) {
						setChangesMade();
						setFlag(24, 0x80, xflagInvalid.isSelected());
						updateScriptID();
						unlockGUI();
					}
				}
			});
			commentIDField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putShort(info, 26, (short)Integer.parseInt(commentIDField.getText()));
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			putAwayIDField.getDocument().addDocumentListener(new MyDocumentListener() {
				public void doIt() {
					if (lockGUI()) {
						setChangesMade();
						try {
							KSFLUtilities.putInt(info, 28, Integer.parseInt(putAwayIDField.getText()));
						} catch (NumberFormatException nfe) {}
						unlockGUI();
					}
				}
			});
			
			semPanel = new JPanel(new GridLayout(1,5,4,4));
			semPanel.add(semLabel);
			semPanel.add(semFile);
			semPanel.add(semFolder);
			semPanel.add(semSix);
			semPanel.add(semSeven);
			
			typeCreatorPanel = new JPanel();
			typeCreatorPanel.setLayout(new BoxLayout(typeCreatorPanel, BoxLayout.LINE_AXIS));
			typeCreatorPanel.add(typeLabel);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(typeField);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(typeClear);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(creatorLabel);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(creatorField);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(creatorClear);
			typeCreatorPanel.add(Box.createRigidArea(new Dimension(8,0)));
			typeCreatorPanel.add(creatorProdos);
			typeClear.putClientProperty("JButton.buttonType", "bevel");
			typeClear.setMinimumSize(new Dimension(60, typeField.getMinimumSize().height));
			typeClear.setPreferredSize(new Dimension(60, typeField.getPreferredSize().height));
			creatorClear.putClientProperty("JButton.buttonType", "bevel");
			creatorClear.setMinimumSize(new Dimension(60, creatorField.getMinimumSize().height));
			creatorClear.setPreferredSize(new Dimension(60, creatorField.getPreferredSize().height));
			creatorProdos.putClientProperty("JButton.buttonType", "bevel");
			creatorProdos.setMinimumSize(new Dimension(100, creatorField.getMinimumSize().height));
			creatorProdos.setPreferredSize(new Dimension(100, creatorField.getPreferredSize().height));
			
			JPanel pdtypePanel2 = new JPanel();
			pdtypePanel2.setLayout(new BoxLayout(pdtypePanel2, BoxLayout.LINE_AXIS));
			pdtypePanel2.add(pdtypeLabel);
			pdtypePanel2.add(Box.createRigidArea(new Dimension(8,0)));
			pdtypePanel2.add(pdtypeField);
			pdtypePanel2.add(Box.createRigidArea(new Dimension(8,0)));
			pdtypePanel2.add(pdmneuField);
			pdtypePanel2.add(Box.createRigidArea(new Dimension(8,0)));
			pdtypePanel2.add(pdauxLabel);
			pdtypePanel2.add(Box.createRigidArea(new Dimension(8,0)));
			pdtypePanel2.add(pdauxField);
			pdtypePanel = new JPanel(new BorderLayout());
			pdtypePanel.add(Box.createRigidArea(new Dimension(0,8)), BorderLayout.NORTH);
			pdtypePanel.add(pdtypePanel2, BorderLayout.CENTER);
			
			winRectPanel = new JPanel();
			winRectPanel.setLayout(new BoxLayout(winRectPanel, BoxLayout.LINE_AXIS));
			winRectPanel.add(winRectLabel);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectL);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectT);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectR);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectB);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectSet);
			winRectPanel.add(Box.createRigidArea(new Dimension(8,0)));
			winRectPanel.add(winRectEdit);
			winRectSet.putClientProperty("JButton.buttonType", "bevel");
			winRectSet.setMinimumSize(new Dimension(50, winRectT.getMinimumSize().height));
			winRectSet.setPreferredSize(new Dimension(50, winRectT.getPreferredSize().height));
			winRectEdit.putClientProperty("JButton.buttonType", "bevel");
			winRectEdit.setMinimumSize(new Dimension(50, winRectT.getMinimumSize().height));
			winRectEdit.setPreferredSize(new Dimension(50, winRectT.getPreferredSize().height));
			
			flagPanel = new JPanel(new GridLayout(1,4));
			JPanel fp1 = new JPanel(new BorderLayout(4,4));
			JPanel fp1a = new JPanel(new BorderLayout(4,4));
			fp1.add(flagOnDesk, BorderLayout.NORTH);
			fp1a.add(flagLabelLabel, BorderLayout.SOUTH);
			fp1.add(fp1a, BorderLayout.CENTER);
			fp1.add(flagLabelPopup, BorderLayout.SOUTH);
			flagPanel.add(fp1);
			JPanel fp2 = new JPanel(new GridLayout(4,1));
			fp2.add(flagHideExt);
			fp2.add(flagSwLaunch);
			fp2.add(flagShared);
			fp2.add(flagNoInits);
			flagPanel.add(fp2);
			JPanel fp3 = new JPanel(new GridLayout(4,1));
			fp3.add(flagInited);
			fp3.add(flagChanged);
			fp3.add(flagCustIcon);
			fp3.add(flagStationery);
			flagPanel.add(fp3);
			JPanel fp4 = new JPanel(new GridLayout(4,1));
			fp4.add(flagNameLock);
			fp4.add(flagHasBNDL);
			fp4.add(flagInvisible);
			fp4.add(flagAlias);
			flagPanel.add(fp4);
			flagLabelPopup.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1l;
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean sel, boolean focus) {
					Component c = super.getListCellRendererComponent(list, value, index, sel, focus);
					if (value instanceof LabelString && c instanceof JLabel) {
						LabelString ls = (LabelString)value;
						JLabel l = (JLabel)c;
						l.setText(ls.toString(semSix.isSelected()));
						l.setIcon(ls.getIcon());
						l.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
						l.setIconTextGap(6);
					}
					return c;
				}
			});
			
			iconLocDirIDPanel = new JPanel();
			iconLocDirIDPanel.setLayout(new BoxLayout(iconLocDirIDPanel, BoxLayout.LINE_AXIS));
			iconLocDirIDPanel.add(iconLocLabel);
			iconLocDirIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconLocDirIDPanel.add(iconLocX);
			iconLocDirIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconLocDirIDPanel.add(iconLocY);
			iconLocDirIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconLocDirIDPanel.add(dirIDLabel);
			iconLocDirIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconLocDirIDPanel.add(dirIDField);
			
			iconIDPanel = new JPanel();
			iconIDPanel.setLayout(new BoxLayout(iconIDPanel, BoxLayout.LINE_AXIS));
			iconIDPanel.add(iconIDLabel);
			iconIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconIDPanel.add(iconIDField);
			iconIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconIDPanel.add(rsvdLabel);
			iconIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconIDPanel.add(rsvd1);
			iconIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconIDPanel.add(rsvd2);
			iconIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			iconIDPanel.add(rsvd3);
			
			scrollOchainPanel = new JPanel();
			scrollOchainPanel.setLayout(new BoxLayout(scrollOchainPanel, BoxLayout.LINE_AXIS));
			scrollOchainPanel.add(scrollLabel);
			scrollOchainPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scrollOchainPanel.add(scrollX);
			scrollOchainPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scrollOchainPanel.add(scrollY);
			scrollOchainPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scrollOchainPanel.add(ochainLabel);
			scrollOchainPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scrollOchainPanel.add(ochainField);
			
			scriptIDPanel = new JPanel();
			scriptIDPanel.setLayout(new BoxLayout(scriptIDPanel, BoxLayout.LINE_AXIS));
			scriptIDPanel.add(scriptIDLabel);
			scriptIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scriptIDPanel.add(scriptIDField);
			scriptIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scriptIDPanel.add(rflagLabel);
			scriptIDPanel.add(Box.createRigidArea(new Dimension(8,0)));
			scriptIDPanel.add(rflagField);
			
			xflagPanel = new JPanel(new GridLayout(4,4));
			xflagPanel.add(xflag0);
			xflagPanel.add(xflag4);
			xflagPanel.add(xflagCustBadge);
			xflagPanel.add(xflag12);
			xflagPanel.add(xflag1);
			xflagPanel.add(xflag5);
			xflagPanel.add(xflag9);
			xflagPanel.add(xflag13);
			xflagPanel.add(xflagRoutInfo);
			xflagPanel.add(xflag6);
			xflagPanel.add(xflag10);
			xflagPanel.add(xflag14);
			xflagPanel.add(xflag3);
			xflagPanel.add(xflagBusy);
			xflagPanel.add(xflag11);
			xflagPanel.add(xflagInvalid);
			
			commentIDPutAwayPanel = new JPanel();
			commentIDPutAwayPanel.setLayout(new BoxLayout(commentIDPutAwayPanel, BoxLayout.LINE_AXIS));
			commentIDPutAwayPanel.add(commentIDLabel);
			commentIDPutAwayPanel.add(Box.createRigidArea(new Dimension(8,0)));
			commentIDPutAwayPanel.add(commentIDField);
			commentIDPutAwayPanel.add(Box.createRigidArea(new Dimension(8,0)));
			commentIDPutAwayPanel.add(putAwayIDLabel);
			commentIDPutAwayPanel.add(Box.createRigidArea(new Dimension(8,0)));
			commentIDPutAwayPanel.add(putAwayIDField);
			
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
			main.add(semPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(new JSeparator());
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(typeCreatorPanel);
			main.add(pdtypePanel);
			main.add(winRectPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(flagPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(iconLocDirIDPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(new JSeparator());
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(iconIDPanel);
			main.add(scrollOchainPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(scriptIDPanel);
			main.add(xflagPanel);
			main.add(Box.createRigidArea(new Dimension(0,8)));
			main.add(commentIDPutAwayPanel);
			main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			setContentPane(main);
			fileSemantics();
			sys7Semantics();
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void save(ResplendenceObject ro) {
			ro.setData(info);
		}
		
		public void revert(ResplendenceObject ro) {
			info = ro.getData();
			update();
		}
		
		private void update() {
			if (lockGUI()) {
				updateTypeCreator();
				updatePDType();
				updateWinRect();
				updateFlags();
				updateIconLocDirID();
				updateIconID();
				updateScrollOchain();
				updateScriptID();
				updateXFlags();
				updateCommentIDPutAway();
				unlockGUI();
			}
		}
		
		private boolean lockGUI() {
			if (updatingGUI) return false;
			return (updatingGUI = true);
		}
		
		private boolean unlockGUI() {
			if (!updatingGUI) return false;
			return !(updatingGUI = false);
		}
		
		private void fileSemantics() {
			typeCreatorPanel.setVisible(true);
			pdtypePanel.setVisible(pdShouldBeVisible());
			winRectPanel.setVisible(false);
			iconIDPanel.setVisible(true);
			scrollOchainPanel.setVisible(false);
			pack();
		}
		
		private void folderSemantics() {
			typeCreatorPanel.setVisible(false);
			pdtypePanel.setVisible(false);
			winRectPanel.setVisible(true);
			iconIDPanel.setVisible(false);
			scrollOchainPanel.setVisible(true);
			pack();
		}
		
		private void sys7Semantics() {
			scriptIDPanel.setVisible(false);
			xflagPanel.setVisible(true);
			flagLabelLabel.setText("Label:");
			flagHideExt.setText("Hide Extension");
			flagCustIcon.setText("Custom Icon");
			flagStationery.setText("Stationery");
			flagAlias.setText("Alias");
			flagLabelPopup.repaint();
			pack();
		}
		
		private void sys6Semantics() {
			scriptIDPanel.setVisible(true);
			xflagPanel.setVisible(false);
			flagLabelLabel.setText("Color:");
			flagHideExt.setText("Color Reserved");
			flagCustIcon.setText("Busy");
			flagStationery.setText("Don't Copy");
			flagAlias.setText("Locked");
			flagLabelPopup.repaint();
			pack();
		}
		
		private void updateTypeCreator() {
			typeField.setText(KSFLUtilities.fccs(KSFLUtilities.getInt(info, 0)));
			creatorField.setText(KSFLUtilities.fccs(KSFLUtilities.getInt(info, 4)));
		}
		
		private void updatePDType() {
			String s;
			s = "00"+Integer.toHexString(info[1] & 0xFF).toUpperCase();
			pdtypeField.setText(s.substring(s.length()-2));
			pdmneuField.setText(PRODOS_TYPES.get(info[1] & 0xFF));
			s = "0000"+Integer.toHexString(KSFLUtilities.getShort(info, 2) & 0xFFFF).toUpperCase();
			pdauxField.setText(s.substring(s.length()-4));
			if (pdtypePanel != null) {
				boolean pv = pdtypePanel.isVisible();
				boolean nv = pdShouldBeVisible();
				pdtypePanel.setVisible(nv);
				if (nv != pv) pack();
			}
		}
		
		private void updateWinRect() {
			winRectT.setText(Short.toString(KSFLUtilities.getShort(info, 0)));
			winRectL.setText(Short.toString(KSFLUtilities.getShort(info, 2)));
			winRectB.setText(Short.toString(KSFLUtilities.getShort(info, 4)));
			winRectR.setText(Short.toString(KSFLUtilities.getShort(info, 6)));
		}
		
		private void updateFlags() {
			flagOnDesk.setSelected((info[9] & 0x01) != 0);
			flagLabelPopup.setSelectedIndex((info[9] & 0x0E) >>> 1);
			flagHideExt.setSelected((info[9] & 0x10) != 0);
			flagSwLaunch.setSelected((info[9] & 0x20) != 0);
			flagShared.setSelected((info[9] & 0x40) != 0);
			flagNoInits.setSelected((info[9] & 0x80) != 0);
			flagInited.setSelected((info[8] & 0x01) != 0);
			flagChanged.setSelected((info[8] & 0x02) != 0);
			flagCustIcon.setSelected((info[8] & 0x04) != 0);
			flagStationery.setSelected((info[8] & 0x08) != 0);
			flagNameLock.setSelected((info[8] & 0x10) != 0);
			flagHasBNDL.setSelected((info[8] & 0x20) != 0);
			flagInvisible.setSelected((info[8] & 0x40) != 0);
			flagAlias.setSelected((info[8] & 0x80) != 0);
		}
		
		private void updateIconLocDirID() {
			iconLocY.setText(Short.toString(KSFLUtilities.getShort(info, 10)));
			iconLocX.setText(Short.toString(KSFLUtilities.getShort(info, 12)));
			dirIDField.setText(Short.toString(KSFLUtilities.getShort(info, 14)));
		}
		
		private void updateIconID() {
			iconIDField.setText(Short.toString(KSFLUtilities.getShort(info, 16)));
			rsvd1.setText(Short.toString(KSFLUtilities.getShort(info, 18)));
			rsvd2.setText(Short.toString(KSFLUtilities.getShort(info, 20)));
			rsvd3.setText(Short.toString(KSFLUtilities.getShort(info, 22)));
		}
		
		private void updateScrollOchain() {
			scrollY.setText(Short.toString(KSFLUtilities.getShort(info, 16)));
			scrollX.setText(Short.toString(KSFLUtilities.getShort(info, 18)));
			ochainField.setText(Integer.toString(KSFLUtilities.getInt(info, 20)));
		}
		
		private void updateScriptID() {
			scriptIDField.setText(Byte.toString(info[24]));
			rflagField.setText(Byte.toString(info[25]));
		}
		
		private void updateXFlags() {
			xflag0.setSelected((info[25] & 0x01) != 0);
			xflag1.setSelected((info[25] & 0x02) != 0);
			xflagRoutInfo.setSelected((info[25] & 0x04) != 0);
			xflag3.setSelected((info[25] & 0x08) != 0);
			xflag4.setSelected((info[25] & 0x10) != 0);
			xflag5.setSelected((info[25] & 0x20) != 0);
			xflag6.setSelected((info[25] & 0x40) != 0);
			xflagBusy.setSelected((info[25] & 0x80) != 0);
			xflagCustBadge.setSelected((info[24] & 0x01) != 0);
			xflag9.setSelected((info[24] & 0x02) != 0);
			xflag10.setSelected((info[24] & 0x04) != 0);
			xflag11.setSelected((info[24] & 0x08) != 0);
			xflag12.setSelected((info[24] & 0x10) != 0);
			xflag13.setSelected((info[24] & 0x20) != 0);
			xflag14.setSelected((info[24] & 0x40) != 0);
			xflagInvalid.setSelected((info[24] & 0x80) != 0);
		}
		
		private void updateCommentIDPutAway() {
			commentIDField.setText(Short.toString(KSFLUtilities.getShort(info, 26)));
			putAwayIDField.setText(Integer.toString(KSFLUtilities.getInt(info, 28)));
		}
		
		private void setFlag(int pos, int mask, boolean val) {
			if (val) {
				info[pos] |= mask;
			} else {
				info[pos] &= ~mask;
			}
		}
		
		private boolean pdShouldBeVisible() {
			return semFile.isSelected() && KSFLUtilities.getInt(info, 4) == PRODOS_CREATOR;
		}
		
		private abstract class MyDocumentListener implements DocumentListener {
			public abstract void doIt();
			public void changedUpdate(DocumentEvent arg0) {
				doIt();
			}
			public void insertUpdate(DocumentEvent arg0) {
				doIt();
			}
			public void removeUpdate(DocumentEvent arg0) {
				doIt();
			}
		}
		
		private enum LabelString {
			NONE		("None",		"Black",	0x000000),
			ESSENTIAL	("Essential",	"Orange",	0xFF6400),
			HOT			("Hot",			"Red",		0xE01600),
			INPROGRESS	("In Progress",	"Magenta",	0xF32780),
			COOL		("Cool",		"Cyan",		0x00ADE9),
			PERSONAL	("Personal",	"Blue",		0x012ED1),
			PROJECT1	("Project 1",	"Green",	0x145F1A),
			PROJECT2	("Project 2",	"Brown",	0x582A06);
			private String name, colorname;
			private Color color;
			private LabelString(String name, String colorname, int colorvalue) {
				this.name = name;
				this.colorname = colorname;
				this.color = new Color(colorvalue, false);
			}
			public String toString() {
				return name;
			}
			public String toString(boolean sys6) {
				if (sys6) {
					return colorname;
				} else {
					return name;
				}
			}
			/*
			public Color getColor() {
				return color;
			}
			*/
			public Icon getIcon() {
				BufferedImage bi = new BufferedImage(20, 14, BufferedImage.TYPE_INT_ARGB);
				Graphics g = bi.getGraphics();
				g.setColor(color);
				g.fillRect(0, 0, 20, 14);
				return new ImageIcon(bi);
			}
		}
	}
	
	private static final class ProDOSFileTypeList extends ArrayList<String> {
		private static final long serialVersionUID = 1L;
		public ProDOSFileTypeList() {
			super(256);
			add("UNK"); add("BAD"); add("PCD"); add("PTX"); add("TXT"); add("PDA"); add("BIN"); add("FNT"); add("FOT"); add("BA3"); add("DA3"); add("WPF"); add("SOS"); add("$0D"); add("$0E"); add("DIR");
			add("RPD"); add("RPI"); add("AFD"); add("AFM"); add("AFR"); add("SCL"); add("PFS"); add("$17"); add("$18"); add("ADB"); add("AWP"); add("ASP"); add("$1C"); add("$1D"); add("$1E"); add("$1F");
			add("TDM"); add("IPS"); add("UPV"); add("$23"); add("$24"); add("$25"); add("$26"); add("$27"); add("$28"); add("3SD"); add("8SC"); add("8OB"); add("8IC"); add("8LD"); add("P8C"); add("$2F");
			add("$30"); add("$31"); add("$32"); add("$33"); add("$34"); add("$35"); add("$36"); add("$37"); add("$38"); add("$39"); add("$3A"); add("$3B"); add("$3C"); add("$3D"); add("$3E"); add("$3F");
			add("$40"); add("OCR"); add("FTD"); add("$43"); add("$44"); add("$45"); add("$46"); add("$47"); add("$48"); add("$49"); add("$4A"); add("$4B"); add("$4C"); add("$4D"); add("$4E"); add("$4F");
			add("GWP"); add("GSS"); add("GDB"); add("DRW"); add("GDP"); add("HMD"); add("EDU"); add("STN"); add("HLP"); add("COM"); add("CFG"); add("ANM"); add("MUM"); add("ENT"); add("DVU"); add("$5F");
			add("PRE"); add("$61"); add("$62"); add("$63"); add("$64"); add("$65"); add("NCF"); add("$67"); add("$68"); add("$69"); add("$6A"); add("BIO"); add("$6C"); add("DVR"); add("PRE"); add("HDV");
			add("SN2"); add("KMT"); add("DSR"); add("BAN"); add("CG7"); add("TNJ"); add("SA7"); add("KES"); add("JAP"); add("CSL"); add("TME"); add("TLB"); add("MR7"); add("MLR"); add("MMM"); add("JCP");
			add("GES"); add("GEA"); add("GEO"); add("GED"); add("GEF"); add("GEP"); add("GEI"); add("GEX"); add("GE_"); add("GEV"); add("GE_"); add("GEC"); add("GEK"); add("GEW"); add("GE_"); add("GE_");
			add("$90"); add("$91"); add("$92"); add("$93"); add("$94"); add("$95"); add("$96"); add("$97"); add("$98"); add("$99"); add("$9A"); add("$9B"); add("$9C"); add("$9D"); add("$9E"); add("$9F");
			add("WP "); add("$A1"); add("$A2"); add("$A3"); add("$A4"); add("$A5"); add("$A6"); add("$A7"); add("$A8"); add("$A9"); add("$AA"); add("GSB"); add("TDF"); add("BDF"); add("$AE"); add("$AF");
			add("SRC"); add("OBJ"); add("LIB"); add("S16"); add("RTL"); add("EXE"); add("PIF"); add("TIF"); add("NDA"); add("CDA"); add("TOL"); add("DRV"); add("LDF"); add("FST"); add("$BE"); add("DOC");
			add("PNT"); add("PIC"); add("ANI"); add("PAL"); add("$C4"); add("OOG"); add("SCR"); add("CDV"); add("FON"); add("FND"); add("ICN"); add("$CB"); add("$CC"); add("$CD"); add("$CE"); add("$CF");
			add("$D0"); add("$D1"); add("$D2"); add("$D3"); add("$D4"); add("MUS"); add("INS"); add("MDI"); add("SND"); add("$D9"); add("$DA"); add("DBM"); add("$DC"); add("$DD"); add("$DE"); add("$DF");
			add("LBR"); add("$E1"); add("ATK"); add("$E3"); add("$E4"); add("$E5"); add("$E6"); add("$E7"); add("$E8"); add("$E9"); add("$EA"); add("$EB"); add("$EC"); add("$ED"); add("R16"); add("PAR");
			add("CMD"); add("OVL"); add("UD2"); add("UD3"); add("UD4"); add("BAT"); add("UD6"); add("UD7"); add("PRG"); add("P16"); add("INT"); add("IVR"); add("BAS"); add("VAR"); add("REL"); add("SYS");
		}
	}
}
