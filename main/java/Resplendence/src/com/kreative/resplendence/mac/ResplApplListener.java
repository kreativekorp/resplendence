package com.kreative.resplendence.mac;

import java.awt.event.InputEvent;
import java.io.File;
import javax.swing.SwingUtilities;

import com.apple.eawt.*;
import com.kreative.resplendence.*;
import com.kreative.resplendence.misc.*;

@SuppressWarnings("deprecation")
public class ResplApplListener implements ApplicationListener {
	public void register() {
		Application a = Application.getApplication();
		a.addAboutMenuItem();
		a.setEnabledAboutMenu(true);
		a.addPreferencesMenuItem();
		a.setEnabledPreferencesMenu(true);
		a.addApplicationListener(this);
	}
	
	public void handleAbout(ApplicationEvent ae) {
		InputEvent ie = ResplUtils.getLastInputEvent();
		if (ie == null)
			AboutBox.showInstance();
		else
			AboutBox.showInstance(ie);
		ae.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent ae) {
		ae.setHandled(true);
	}

	public void handleOpenFile(ApplicationEvent ae) {
		System.out.println(ae.getFilename());
		ResplMain.resplOpen(new File(ae.getFilename()));
		ae.setHandled(true);
	}

	public void handlePreferences(ApplicationEvent ae) {
		PreferencesWindow.showInstance();
		ae.setHandled(true);
	}

	public void handlePrintFile(ApplicationEvent ae) {
		ResplMain.resplOpen(new File(ae.getFilename()));
		ae.setHandled(true);
	}

	public void handleQuit(ApplicationEvent ae) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ResplMain.resplExit();
			}
		});
		ae.setHandled(false);
	}

	public void handleReOpenApplication(ApplicationEvent ae) {
		ae.setHandled(true);
	}
}
