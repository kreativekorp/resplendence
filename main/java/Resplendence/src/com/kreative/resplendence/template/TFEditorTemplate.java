package com.kreative.resplendence.template;

import java.awt.*;
import java.io.StringReader;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.kreative.swing.*;
import com.kreative.openxion.*;

public class TFEditorTemplate extends JPanel implements TFEditor {
	private static final long serialVersionUID = 1;
	
	protected List<TFEditor> fields = new ArrayList<TFEditor>();
	protected Map<String,TFEditor> fieldsByName = new HashMap<String,TFEditor>();
	
	public TFEditorTemplate(int indent, String tenc, List<TemplateField> tmpl) {
		super(new GridBagLayout());
		GridBagConstraints left = new GridBagConstraints();
		GridBagConstraints right = new GridBagConstraints();
		GridBagConstraints both = new GridBagConstraints();
		left.anchor = GridBagConstraints.EAST; right.anchor = both.anchor = GridBagConstraints.WEST;
		left.insets = right.insets = both.insets = new Insets(0,0,8,8);
		left.gridx = both.gridx = 0; right.gridx = 1;
		left.gridy = right.gridy = both.gridy = 0;
		left.gridwidth = right.gridwidth = 1; both.gridwidth = 2;
		left.weightx = 0.0; right.weightx = both.weightx = 1.0;
		left.weighty = right.weighty = both.weighty = 0.0;
		TFEditorListCount llc = null;
		Iterator<TemplateField> i = tmpl.iterator();
		while (i.hasNext()) {
			TemplateField tf = i.next();
			TFEditor te = null;
			if (TemplateField.isCommentType(tf.getType())) {
				te = null;
			} else if (TemplateField.isMetaType(tf.getType())) {
				te = null;
				// interpret any meta stuff here
			} else if (TemplateField.isHeaderType(tf.getType())) {
				te = null;
				JLabel l = new JLabel(tf.getName());
				l.setFont(mono.deriveFont(18.0f));
				this.add(l, both);
			} else if (TemplateField.isLabelType(tf.getType())) {
				te = null;
				JLabel l = new JLabel(tf.getName());
				l.setFont(mono);
				this.add(l, both);
			} else if (TemplateField.isSeparatorType(tf.getType())) {
				te = null;
				JSeparator s = new JSeparator();
				this.add(s, both);
			} else if (TemplateField.isTextEncodingType(tf.getType())) {
				te = null;
				tenc = tf.getName();
			} else if (tf instanceof TemplateList) {
				te = ((TemplateList)tf).createEditor(llc, indent, tenc);
				if (te instanceof TFEditorTemplate) {
					Map<String,TFEditor> fbn = ((TFEditorTemplate)te).fieldsByName;
					Iterator<Map.Entry<String,TFEditor>> ii = fbn.entrySet().iterator();
					while (ii.hasNext()) {
						Map.Entry<String,TFEditor> ee = ii.next();
						this.fieldsByName.put(ee.getKey(), ee.getValue());
					}
				}
				if (te instanceof Component) this.add((Component)te, both);
			} else if (tf instanceof TemplateIfBranch) {
				te = ((TemplateIfBranch)tf).createEditor(indent, tenc);
				if (te instanceof TFEditorTemplate) {
					Map<String,TFEditor> fbn = ((TFEditorTemplate)te).fieldsByName;
					Iterator<Map.Entry<String,TFEditor>> ii = fbn.entrySet().iterator();
					while (ii.hasNext()) {
						Map.Entry<String,TFEditor> ee = ii.next();
						this.fieldsByName.put(ee.getKey(), ee.getValue());
					}
				}
				if (te instanceof Component) this.add((Component)te, both);
			} else if (tf instanceof TemplateIfStructure) {
				te = ((TemplateIfStructure)tf).createEditor(this, indent, tenc);
				if (te instanceof TFEditorTemplate) {
					Map<String,TFEditor> fbn = ((TFEditorTemplate)te).fieldsByName;
					Iterator<Map.Entry<String,TFEditor>> ii = fbn.entrySet().iterator();
					while (ii.hasNext()) {
						Map.Entry<String,TFEditor> ee = ii.next();
						this.fieldsByName.put(ee.getKey(), ee.getValue());
					}
				}
				if (te instanceof Component) this.add((Component)te, both);
				if (te instanceof TFEditorIfStructure) {
					List<String> ids = new Vector<String>();
					XNLexer lex = new XNLexer(tf, new StringReader(tf.name));
					while (true) {
						try {
							XNToken tkn = lex.getToken();
							if (tkn.kind == XNToken.LINE_TERM && tkn.image.length() == 0) break;
							else if (tkn.kind == XNToken.ID && this.fieldsByName.containsKey(tkn.image)) ids.add(tkn.image);
						} catch (Exception e) {
							break;
						}
					}
					((TFEditorIfStructure)te).addListeners(this.fieldsByName, ids);
				}
			} else {
				te = tf.createEditor(tenc);
				if (te instanceof TFEditorListCount) llc = (TFEditorListCount)te;
				if (te instanceof TFEditorTemplate) {
					Map<String,TFEditor> fbn = ((TFEditorTemplate)te).fieldsByName;
					Iterator<Map.Entry<String,TFEditor>> ii = fbn.entrySet().iterator();
					while (ii.hasNext()) {
						Map.Entry<String,TFEditor> ee = ii.next();
						this.fieldsByName.put(ee.getKey(), ee.getValue());
					}
				}
				if (te instanceof Component) {
					JLabel l = new MyLabel(tf.getName(), indent);
					l.setLabelFor((Component)te);
					this.add(l, left);
					if (te instanceof JTextArea) {
						((JTextArea)te).setLineWrap(true);
						((JTextArea)te).setWrapStyleWord(true);
						right.fill = GridBagConstraints.HORIZONTAL;
						this.add(new JScrollPane((JTextArea)te, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), right);
					} else if (te instanceof JHexEditorSuite) {
						right.fill = GridBagConstraints.HORIZONTAL;
						this.add((JHexEditorSuite)te, right);
					} else {
						right.fill = GridBagConstraints.NONE;
						this.add((Component)te, right);
					}
				}
			}
			if (te != null) {
				this.fields.add(te);
				this.fieldsByName.put(tf.getName(), te);
			}
			left.gridy++; right.gridy++; both.gridy++;
		}
		both.weighty = 1.0; both.insets = new Insets(0,0,0,0);
		this.add(Box.createRigidArea(new Dimension(0,0)), both);
	}
	
	public List<TFEditor> getTFEditorList() {
		return fields;
	}
	
	public Iterator<TFEditor> getTFEditorIterator() {
		return fields.iterator();
	}
	
	public TFEditor[] getTFEditors() {
		return fields.toArray(new TFEditor[0]);
	}
	
	public TFEditor getTFEditor(String fieldName) {
		return fieldsByName.get(fieldName);
	}
	
	public List<Component> getTFEditorComponentList() {
		List<Component> eds = new ArrayList<Component>();
		Iterator<TFEditor> i = fields.iterator();
		while (i.hasNext()) {
			TFEditor ed = i.next();
			if (ed instanceof Component) eds.add((Component)ed);
		}
		return eds;
	}
	
	public Iterator<Component> getTFEditorComponentIterator() {
		return getTFEditorComponentList().iterator();
	}
	
	public Component[] getTFEditorComponents() {
		return getTFEditorComponentList().toArray(new Component[0]);
	}

	public Position readValue(byte[] data, Position pos) {
		Iterator<TFEditor> i = fields.iterator();
		while (i.hasNext()) pos = i.next().readValue(data, pos);
		return pos;
	}

	public Position writeValue(byte[] data, Position pos) {
		Iterator<TFEditor> i = fields.iterator();
		while (i.hasNext()) pos = i.next().writeValue(data, pos);
		return pos;
	}

	public Position writeValue(Position pos) {
		Iterator<TFEditor> i = fields.iterator();
		while (i.hasNext()) pos = i.next().writeValue(pos);
		return pos;
	}
	
	private static class MyLabel extends JLabel {
		private static final long serialVersionUID = 0x4A41564153555841L;
		private int indent;
		public MyLabel(String s, int i) {
			super("<html><p align=\"right\">"+s+"</p></html>");
			this.indent = i;
			this.setFont(mono);
			this.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
			this.setHorizontalAlignment(JLabel.RIGHT);
			this.setHorizontalTextPosition(JLabel.RIGHT);
		}
		public Dimension getPreferredSize() {
			return new Dimension(indent, super.getPreferredSize().height);
		}
	}
}
