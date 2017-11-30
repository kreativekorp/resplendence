package com.kreative.resplendence.menus;

import java.awt.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class ResplendenceMenuBar extends JMenuBar implements MenuConstants {
	private static final long serialVersionUID = 1L;
	
	private FileMenu fm = null;
	private EditMenu em = null;
	private ViewMenu vm = null;
	private ObjectMenu om = null;
	private FormatMenu mm = null;
	private TextMenu tm = null;
	private WindowMenu wm = null;
	
	public ResplendenceMenuBar(long features, JMenu[] extra) {
		if (ResplMain.RUNNING_ON_MAC_OS) {
			//everything!!!
			add(fm = new FileMenu(features));
			add(em = new EditMenu(features));
			add(vm = new ViewMenu(features));
			add(om = new ObjectMenu(features));
			add(mm = new FormatMenu(features));
			add(tm = new TextMenu(features));
			if (extra != null) for (JMenu m : extra) {
				if (m.getText().equals("Edit")) {
					if (em.getItemCount() > 0) em.addSeparator();
					for (Component c : m.getMenuComponents()) em.add(c);
				} else if (m.getText().equals("View")) {
					if (vm.getItemCount() > 0) vm.addSeparator();
					for (Component c : m.getMenuComponents()) vm.add(c);
				} else if (m.getText().equals("Object")) {
					if (om.getItemCount() > 0) om.addSeparator();
					for (Component c : m.getMenuComponents()) om.add(c);
				} else if (m.getText().equals("Format")) {
					if (mm.getItemCount() > 0) mm.addSeparator();
					for (Component c : m.getMenuComponents()) mm.add(c);
				} else if (m.getText().equals("Text")) {
					if (tm.getItemCount() > 0) tm.addSeparator();
					for (Component c : m.getMenuComponents()) tm.add(c);
				} else {
					add(m);
				}
			}
			add(new SpecialMenu());
			add(wm = new WindowMenu());
			add(new HelpMenu());
		} else {
			//only what we requested
			if (features == 0 && extra == null) return;
			addw(fm = new FileMenu(features));
			addw(em = new EditMenu(features));
			addw(vm = new ViewMenu(features));
			addw(om = new ObjectMenu(features));
			addw(mm = new FormatMenu(features));
			addw(tm = new TextMenu(features));
			if (extra != null) for (JMenu m : extra) {
				if (m.getText().equals("Edit")) {
					if (em.getItemCount() > 0) em.addSeparator();
					for (Component c : m.getMenuComponents()) em.add(c);
					recheck(em);
				} else if (m.getText().equals("View")) {
					if (vm.getItemCount() > 0) vm.addSeparator();
					for (Component c : m.getMenuComponents()) vm.add(c);
					recheck(vm);
				} else if (m.getText().equals("Object")) {
					if (om.getItemCount() > 0) om.addSeparator();
					for (Component c : m.getMenuComponents()) om.add(c);
					recheck(om);
				} else if (m.getText().equals("Format")) {
					if (mm.getItemCount() > 0) mm.addSeparator();
					for (Component c : m.getMenuComponents()) mm.add(c);
					recheck(mm);
				} else if (m.getText().equals("Text")) {
					if (tm.getItemCount() > 0) tm.addSeparator();
					for (Component c : m.getMenuComponents()) tm.add(c);
					recheck(tm);
				} else {
					addw(m);
				}
			}
			if ((features & MENUS_GLOBAL)>0) {
				addw(new SpecialMenu());
				addw(wm = new WindowMenu());
				addw(new HelpMenu());
			}
		}
	}
	
	private void addw(JMenu m) {
		add(m); m.setVisible(m.getItemCount() > 0);
	}
	
	private void recheck(JMenu m) {
		m.setVisible(m.getItemCount() > 0);
	}
	
	public FileMenu getFileMenu() {
		return fm;
	}
	
	public WindowMenu getWindowMenu() {
		return wm;
	}
}
