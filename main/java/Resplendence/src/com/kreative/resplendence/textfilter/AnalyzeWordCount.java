package com.kreative.resplendence.textfilter;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import com.kreative.resplendence.*;

public class AnalyzeWordCount implements TextFilter {
	public String category(int i) {
		return "Analyze";
	}

	public String filter(int i, String s) {
		CharacterIterator it = new StringCharacterIterator(s);
		int numChars = s.length();
		int numCharsNoSpaces = numChars;
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next())
			if (Character.isWhitespace(ch))
				numCharsNoSpaces--;
		int numWords = 0;
		boolean insideWord = false;
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (Character.isWhitespace(ch)) {
				insideWord = false;
			} else if (!insideWord) {
				numWords++;
				insideWord = true;
			}
		}
		int numSentences = 0;
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == '.' || ch == '?' || ch == '!') numSentences++;
		}
		int numLines = 0;
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == '\r') {
				if (it.next() != '\n') it.previous();
				numLines++;
			} else if (ch == '\n') {
				numLines++;
			}
		}
		if (!(s.endsWith("\n") || s.endsWith("\r"))) numLines++;
		final JFrame f = new JFrame("Word Count");
		ResplMain.registerWindow(f);
		JPanel p = new JPanel(new BorderLayout());
		JPanel cp = new JPanel(new GridLayout(0,2,5,5));
		cp.add(new JLabel("Characters:"));
		cp.add(new JLabel(Integer.toString(numChars)));
		cp.add(new JLabel("Characters (No Spaces):"));
		cp.add(new JLabel(Integer.toString(numCharsNoSpaces)));
		cp.add(new JLabel("Words:"));
		cp.add(new JLabel(Integer.toString(numWords)));
		cp.add(new JLabel("Sentences (Approx):"));
		cp.add(new JLabel(Integer.toString(numSentences)));
		cp.add(new JLabel("Lines:"));
		cp.add(new JLabel(Integer.toString(numLines)));
		cp.setBorder(BorderFactory.createEmptyBorder(14, 20, 0, 20));
		p.add(cp, BorderLayout.CENTER);
		JPanel bp = new JPanel(new FlowLayout());
		JButton b = new JButton("OK");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.dispose();
			}
		});
		bp.add(b);
		bp.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		p.add(bp, BorderLayout.PAGE_END);
		f.setContentPane(p);
		f.getRootPane().setDefaultButton(b);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		return s;
	}

	public String name(int i) {
		return "Word Count";
	}
	
	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke('U', ResplMain.META_SHIFT_MASK);
	}
	
	public boolean insert(int i) {
		return false;
	}

	public int numberOfFilters() {
		return 1;
	}
}
