package com.kreative.resplendence;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.kreative.ksfl.KSFLUtilities;

public class Warnings {
	private Warnings() {}
	
	public static final long SIGNATURE_DFF_CONVERT = KSFLUtilities.ecc("dff-conv");
	public static final long SIGNATURE_DITL_REARRANGE = KSFLUtilities.ecc("ditlrrge");
	public static final long SIGNATURE_CODEC_REOPEN = KSFLUtilities.ecc("cdecopen");
	public static final long SIGNATURE_CODEC_CONVERT = KSFLUtilities.ecc("cdecconv");
	public static final long SIGNATURE_FILE_DUPLICATE = KSFLUtilities.ecc("filedupl");
	public static final long SIGNATURE_CREATE_FORK = KSFLUtilities.ecc("creafork");
	
	public static final String MESSAGE_DFF_CONVERT = "Converting this file to DFF will make it unreadable to other applications. Are you sure you want to do this?";
	public static final String MESSAGE_DITL_REARRANGE = "Removing or rearranging items in a preexisting DITL resource may crash the application using it. Are you sure you want to do this?";
	public static final String MESSAGE_CODEC_REOPEN = "The encoding selected may not be able to handle this file. If this is the case, No Encoding will be selected.";
	public static final String MESSAGE_CODEC_CONVERT = "Converting this file from one encoding to another may result in loss of information. Are you sure you want to do this?";
	public static final String MESSAGE_FILE_DUPLICATE = "Duplicating a file or folder in Resplendence will not necessarily copy all the metadata associated with the file or folder. For a genuine duplicate, use your operating system's own copy command.";
	public static final String MESSAGE_CREATE_FORK = "Not all encodings support all kinds of forks or metadata. Your new fork or metadata may or may not get saved. Verify that you can save it before you rely on it.";
	
	public static boolean isWarningIgnored(long wsig) {
		return ResplPrefs.getLongs("Ignored Warnings").contains(wsig);
	}
	
	public static void setWarningIgnored(long wsig, boolean ign) {
		java.util.List<Long> ignored = ResplPrefs.getLongs("Ignored Warnings");
		if (ign) { //add to list
			if (ignored == null) ignored = new Vector<Long>();
			ignored.add(wsig);
			ResplPrefs.setLongs("Ignored Warnings", ignored);
		} else { //remove from list
			if (ignored == null) return;
			ignored.remove(wsig);
			ResplPrefs.setLongs("Ignored Warnings", ignored);
		}
	}
	
	public static void clearWarningsIgnored() {
		ResplPrefs.resetLongs("Ignored Warnings");
	}
	
	private static class WarnDlgParms {
		public long wsig;
		public JDialog d;
		public JCheckBox ign;
		public boolean r;
		public WarnDlgParms(long wsig) { this.wsig = wsig; }
	}
	
	public static boolean warn(long wsig, String wmsg) {
		if (isWarningIgnored(wsig)) return true;
		else {
			final WarnDlgParms p = new WarnDlgParms(wsig);
			p.d = new JDialog((Frame)null, "WARNING!", true);
			JPanel main = new JPanel(new BorderLayout(20,20));
			JLabel msg = new JLabel("<html>"+wmsg+"</html>");
			msg.setVerticalAlignment(JLabel.TOP);
			msg.setVerticalTextPosition(JLabel.TOP);
			main.add(msg, BorderLayout.CENTER);
			main.add(new JLabel(new JOptionPane("", JOptionPane.WARNING_MESSAGE).getIcon()), BorderLayout.LINE_START);
			JPanel buttons = new JPanel(new BorderLayout());
			p.ign = new JCheckBox("Don't Show Again");
			JPanel okcButtons = new JPanel();
			JButton okButton = new JButton("OK");
			JButton cancelButton = new JButton("Cancel");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (p.ign.isSelected()) setWarningIgnored(p.wsig, true);
					p.r = true;
					p.d.dispose();
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					p.r = false;
					p.d.dispose();
				}
			});
			okcButtons.add(okButton);
			okcButtons.add(cancelButton);
			buttons.add(p.ign, BorderLayout.LINE_START);
			buttons.add(okcButtons, BorderLayout.LINE_END);
			main.add(buttons, BorderLayout.PAGE_END);
			main.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
			p.d.setContentPane(main);
			p.d.getRootPane().setDefaultButton(okButton);
			ResplUtils.setCancelButton(p.d.getRootPane(), cancelButton);
			p.d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			p.d.setResizable(false);
			p.d.setSize(new Dimension(400,180));
			p.d.setLocationRelativeTo(null);
			p.d.setVisible(true);
			return p.r;
		}
	}
}
