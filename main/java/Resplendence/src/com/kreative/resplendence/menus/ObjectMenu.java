package com.kreative.resplendence.menus;

import java.awt.event.*;
import javax.swing.*;

import com.kreative.resplendence.*;
import com.kreative.resplendence.editors.*;
import com.kreative.resplendence.misc.*;

public class ObjectMenu extends JMenu implements MenuConstants {
	public static final long serialVersionUID = 1L;
	
	public ObjectMenu(long features) {
		super("Object");
		boolean mac = ResplMain.RUNNING_ON_MAC_OS;
		if (!mac) setMnemonic(KeyEvent.VK_O);
		
		JMenuItem mi;
		if (mac || (features & MENUS_NEW_ITEM) > 0) {
			mi = new JMenuItem("New Item");
			mi.setMnemonic(KeyEvent.VK_N);
			mi.setAccelerator(KeyStroke.getKeyStroke('K', META_MASK));
			mi.setEnabled((features & MENUS_NEW_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.NEW_ITEM, "New Item", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_DUPLICATE_ITEM) > 0) {
			mi = new JMenuItem("Duplicate Item");
			mi.setMnemonic(KeyEvent.VK_D);
			mi.setAccelerator(KeyStroke.getKeyStroke('D', META_MASK));
			mi.setEnabled((features & MENUS_DUPLICATE_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.DUPLICATE_ITEM, "Duplicate Item", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_REMOVE_ITEM) > 0) {
			mi = new JMenuItem("Remove Item");
			mi.setMnemonic(KeyEvent.VK_R);
			mi.setAccelerator(KeyStroke.getKeyStroke('M', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_REMOVE_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REMOVE_ITEM, "Remove Item", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_ITEM_INFO) > 0) {
			mi = new JMenuItem("Item Info...");
			mi.setMnemonic(KeyEvent.VK_I);
			mi.setAccelerator(KeyStroke.getKeyStroke('I', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_ITEM_INFO) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.ITEM_INFO, "Item Info...", null));
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_OPEN_ITEM) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Open Item");
			mi.setMnemonic(KeyEvent.VK_O);
			mi.setAccelerator(KeyStroke.getKeyStroke('O', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_OPEN_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplendenceListener fm = ResplMain.getFrontmostResplendenceListener();
					Object o = ResplMain.sendResplendenceEventTo(fm, new ResplendenceEvent(
							ae.getSource(),
							ResplendenceEvent.GET_SELECTED_RESPL_OBJECT,
							"Open Item",
							null
					));
					if (fm instanceof ResplendenceEditorWindow) {
						if (o instanceof ResplendenceObject) {
							((ResplendenceEditorWindow)fm).resplOpen((ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									((ResplendenceEditorWindow)fm).resplOpen((ResplendenceObject)so);
								}
							}
						}
					} else {
						if (o instanceof ResplendenceObject) {
							ResplMain.resplOpen((ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									ResplMain.resplOpen((ResplendenceObject)so);
								}
							}
						}
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open with Editor...");
			mi.setMnemonic(KeyEvent.VK_E);
			mi.setAccelerator(KeyStroke.getKeyStroke('E', META_MASK));
			mi.setEnabled((features & MENUS_OPEN_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplendenceListener fm = ResplMain.getFrontmostResplendenceListener();
					Object o = ResplMain.sendResplendenceEventTo(fm, new ResplendenceEvent(
							ae.getSource(),
							ResplendenceEvent.GET_SELECTED_RESPL_OBJECT,
							"Open with Editor",
							null
					));
					if (fm instanceof ResplendenceEditorWindow) {
						if (o instanceof ResplendenceObject) {
							((ResplendenceEditorWindow)fm).resplOpen(new OpenWithEditorWindow((ResplendenceObject)o));
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									((ResplendenceEditorWindow)fm).resplOpen(new OpenWithEditorWindow((ResplendenceObject)so));
								}
							}
						}
					} else {
						if (o instanceof ResplendenceObject) {
							ResplMain.resplOpen(new OpenWithEditorWindow((ResplendenceObject)o));
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									ResplMain.resplOpen(new OpenWithEditorWindow((ResplendenceObject)so));
								}
							}
						}
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open with Hex Editor");
			mi.setMnemonic(KeyEvent.VK_H);
			mi.setAccelerator(KeyStroke.getKeyStroke('H', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_OPEN_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplendenceListener fm = ResplMain.getFrontmostResplendenceListener();
					Object o = ResplMain.sendResplendenceEventTo(fm, new ResplendenceEvent(
							ae.getSource(),
							ResplendenceEvent.GET_SELECTED_RESPL_OBJECT,
							"Open with Hex Editor",
							null
					));
					HexEditor he = new HexEditor();
					if (fm instanceof ResplendenceEditorWindow) {
						if (o instanceof ResplendenceObject) {
							((ResplendenceEditorWindow)fm).resplOpen(he, (ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									((ResplendenceEditorWindow)fm).resplOpen(he, (ResplendenceObject)so);
								}
							}
						}
					} else {
						if (o instanceof ResplendenceObject) {
							ResplMain.resplOpen(he, (ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									ResplMain.resplOpen(he, (ResplendenceObject)so);
								}
							}
						}
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open with Template...");
			mi.setMnemonic(KeyEvent.VK_T);
			mi.setAccelerator(KeyStroke.getKeyStroke('T', META_ALT_MASK));
			mi.setEnabled((features & MENUS_OPEN_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplendenceListener fm = ResplMain.getFrontmostResplendenceListener();
					Object o = ResplMain.sendResplendenceEventTo(fm, new ResplendenceEvent(
							ae.getSource(),
							ResplendenceEvent.GET_SELECTED_RESPL_OBJECT,
							"Open with Template",
							null
					));
					TemplateBasedEditor te = new TemplateBasedEditor();
					if (fm instanceof ResplendenceEditorWindow) {
						if (o instanceof ResplendenceObject) {
							if (te.recognizes((ResplendenceObject)o) != ResplendenceEditor.DOES_NOT_RECOGNIZE) {
								((ResplendenceEditorWindow)fm).resplOpen(te.openEditor((ResplendenceObject)o, null));
							} else {
								JOptionPane.showMessageDialog(null, te.name()+" cannot be used to edit this object.", "Open", JOptionPane.ERROR_MESSAGE);
							}
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									if (te.recognizes((ResplendenceObject)so) != ResplendenceEditor.DOES_NOT_RECOGNIZE) {
										((ResplendenceEditorWindow)fm).resplOpen(te.openEditor((ResplendenceObject)so, null));
									} else {
										JOptionPane.showMessageDialog(null, te.name()+" cannot be used to edit this object.", "Open", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						}
					} else {
						if (o instanceof ResplendenceObject) {
							if (te.recognizes((ResplendenceObject)o) != ResplendenceEditor.DOES_NOT_RECOGNIZE) {
								ResplMain.resplOpen(te.openEditor((ResplendenceObject)o, null));
							} else {
								JOptionPane.showMessageDialog(null, te.name()+" cannot be used to edit this object.", "Open", JOptionPane.ERROR_MESSAGE);
							}
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									if (te.recognizes((ResplendenceObject)so) != ResplendenceEditor.DOES_NOT_RECOGNIZE) {
										ResplMain.resplOpen(te.openEditor((ResplendenceObject)so, null));
									} else {
										JOptionPane.showMessageDialog(null, te.name()+" cannot be used to edit this object.", "Open", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						}
					}
				}
			});
			add(mi);
			mi = new JMenuItem("Open with Application...");
			mi.setMnemonic(KeyEvent.VK_A);
			mi.setAccelerator(KeyStroke.getKeyStroke('A', META_ALT_MASK));
			mi.setEnabled((features & MENUS_OPEN_ITEM) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					ResplendenceListener fm = ResplMain.getFrontmostResplendenceListener();
					Object o = ResplMain.sendResplendenceEventTo(fm, new ResplendenceEvent(
							ae.getSource(),
							ResplendenceEvent.GET_SELECTED_RESPL_OBJECT,
							"Open with Application",
							null
					));
					OpenWithApplicationWindow owae = new OpenWithApplicationWindow();
					if (fm instanceof ResplendenceEditorWindow) {
						if (o instanceof ResplendenceObject) {
							((ResplendenceEditorWindow)fm).resplOpen(owae, (ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									((ResplendenceEditorWindow)fm).resplOpen(owae, (ResplendenceObject)so);
								}
							}
						}
					} else {
						if (o instanceof ResplendenceObject) {
							ResplMain.resplOpen(owae, (ResplendenceObject)o);
						} else if (o instanceof Object[]) {
							for (Object so : (Object[])o) {
								if (so instanceof ResplendenceObject) {
									ResplMain.resplOpen(owae, (ResplendenceObject)so);
								}
							}
						}
					}
				}
			});
			add(mi);
		}
		if (mac || (features & MENUS_REARRANGE) > 0) {
			if (getItemCount() > 0) addSeparator();
			mi = new JMenuItem("Send to Back");
			mi.setMnemonic(KeyEvent.VK_B);
			mi.setAccelerator(KeyStroke.getKeyStroke('-', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_REARRANGE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REARRANGE, "Send to Back", Integer.MIN_VALUE));
				}
			});
			add(mi);
			mi = new JMenuItem("Send Backward");
			mi.setMnemonic(KeyEvent.VK_K);
			mi.setAccelerator(KeyStroke.getKeyStroke('-', META_MASK));
			mi.setEnabled((features & MENUS_REARRANGE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REARRANGE, "Send Backward", -1));
				}
			});
			add(mi);
			mi = new JMenuItem("Bring Forward");
			mi.setMnemonic(KeyEvent.VK_W);
			mi.setAccelerator(KeyStroke.getKeyStroke('=', META_MASK));
			mi.setEnabled((features & MENUS_REARRANGE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REARRANGE, "Bring Forward", +1));
				}
			});
			add(mi);
			mi = new JMenuItem("Bring to Front");
			mi.setMnemonic(KeyEvent.VK_F);
			mi.setAccelerator(KeyStroke.getKeyStroke('=', META_SHIFT_MASK));
			mi.setEnabled((features & MENUS_REARRANGE) > 0);
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResplMain.sendResplendenceEventToFrontmost(new ResplendenceEvent(e.getSource(), ResplendenceEvent.REARRANGE, "Bring to Front", Integer.MAX_VALUE));
				}
			});
			add(mi);
		}
	}
}
