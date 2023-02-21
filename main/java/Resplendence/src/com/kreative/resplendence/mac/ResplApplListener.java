package com.kreative.resplendence.mac;

import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenFilesHandler;
import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;
import java.awt.desktop.PrintFilesEvent;
import java.awt.desktop.PrintFilesHandler;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.awt.event.InputEvent;
import java.io.File;
import java.lang.reflect.Method;
import com.kreative.resplendence.ResplMain;
import com.kreative.resplendence.ResplUtils;
import com.kreative.resplendence.misc.AboutBox;
import com.kreative.resplendence.misc.PreferencesWindow;

public class ResplApplListener {
	private static final String[][] classAndMethodNames = {
		{ "java.awt.Desktop", "getDesktop" },
		{ "com.kreative.ual.eawt.NewApplicationAdapter", "getInstance" },
		{ "com.kreative.ual.eawt.OldApplicationAdapter", "getInstance" },
	};
	
	public void register() {
		for (String[] classAndMethodName : classAndMethodNames) {
			try {
				Class<?> cls = Class.forName(classAndMethodName[0]);
				Method getInstance = cls.getMethod(classAndMethodName[1]);
				Object instance = getInstance.invoke(null);
				cls.getMethod("setAboutHandler", AboutHandler.class).invoke(instance, about);
				cls.getMethod("setOpenFileHandler", OpenFilesHandler.class).invoke(instance, open);
				cls.getMethod("setPreferencesHandler", PreferencesHandler.class).invoke(instance, preferences);
				cls.getMethod("setPrintFileHandler", PrintFilesHandler.class).invoke(instance, print);
				cls.getMethod("setQuitHandler", QuitHandler.class).invoke(instance, quit);
				System.out.println("Registered app event handlers through " + classAndMethodName[0]);
				return;
			} catch (Exception e) {
				System.out.println("Failed to register app event handlers through " + classAndMethodName[0] + ": " + e);
			}
		}
	}
	
	private final AboutHandler about = new AboutHandler() {
		@Override
		public void handleAbout(final AboutEvent e) {
			new Thread() {
				public void run() {
					InputEvent ie = ResplUtils.getLastInputEvent();
					if (ie == null)
						AboutBox.showInstance();
					else
						AboutBox.showInstance(ie);
				}
			}.start();
		}
	};
	
	private final OpenFilesHandler open = new OpenFilesHandler() {
		@Override
		public void openFiles(final OpenFilesEvent e) {
			new Thread() {
				public void run() {
					for (Object o : e.getFiles()) {
						ResplMain.resplOpen((File)o);
					}
				}
			}.start();
		}
	};
	
	private final PreferencesHandler preferences = new PreferencesHandler() {
		@Override
		public void handlePreferences(final PreferencesEvent e) {
			new Thread() {
				public void run() {
					PreferencesWindow.showInstance();
				}
			}.start();
		}
	};
	
	private final PrintFilesHandler print = new PrintFilesHandler() {
		@Override
		public void printFiles(final PrintFilesEvent e) {
			new Thread() {
				public void run() {
					for (Object o : e.getFiles()) {
						ResplMain.resplOpen((File)o);
					}
				}
			}.start();
		}
	};
	
	private final QuitHandler quit = new QuitHandler() {
		@Override
		public void handleQuitRequestWith(final QuitEvent e, final QuitResponse r) {
			new Thread() {
				public void run() {
					ResplMain.resplExit();
					r.cancelQuit();
				}
			}.start();
		}
	};
}
