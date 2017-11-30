package com.kreative.resplendence.template;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import com.kreative.openxion.*;
import com.kreative.openxion.xom.*;
import com.kreative.openxion.xom.type.*;
import com.kreative.openxion.xom.inst.*;
import com.kreative.swing.JHexEditorSuite;
import com.kreative.swing.event.HexDataChangeEvent;
import com.kreative.swing.event.HexDataChangeListener;

public class TFEditorIfStructure extends JPanel implements TFEditor {
	private static final long serialVersionUID = 1;
	
	private TFEditorTemplate root;
	private List<TemplateIfBranch> branches;
	
	public TFEditorIfStructure(int indent, String tenc, List<TemplateIfBranch> branches, TFEditorTemplate root) {
		this.root = root;
		this.branches = branches;
		this.setLayout(new CardLayout());
		Iterator<TemplateIfBranch> i = branches.iterator();
		while (i.hasNext()) {
			TemplateIfBranch br = i.next();
			br.pane = br.createEditor(indent, tenc);
			if (br.pane instanceof Component)
				this.add((Component)br.pane, br.name);
			else
				this.add(Box.createRigidArea(new Dimension(0,0)), br.name);
		}
		this.add(Box.createRigidArea(new Dimension(0,0)), "\uFFFD");
	}
	
	public Position readValue(byte[] data, Position pos) {
		return updateDisplay().pane.readValue(data, pos);
	}

	public Position writeValue(byte[] data, Position pos) {
		return updateDisplay().pane.writeValue(data, pos);
	}

	public Position writeValue(Position pos) {
		return updateDisplay().pane.writeValue(pos);
	}
	
	public TemplateIfBranch updateDisplay() {
		CardLayout c = (CardLayout)this.getLayout();
		Iterator<TemplateIfBranch> i = branches.iterator();
		while (i.hasNext()) {
			TemplateIfBranch br = i.next();
			if (TemplateField.isElseType(br.type) || evaluate(br.name)) {
				c.show(this, br.name);
				return br;
			}
		}
		c.show(this, "\uFFFD");
		return null;
	}
	
	private boolean evaluate(String s) {
		final XNUI ui = new XNNullUI();
		final XNContext ctx = new XNContext(ui);
		ctx.loadModule(XNStandardModule.instance());
		ctx.loadModule(XNExtendedModule.instance());
		ctx.loadModule(XNAudioModule.instance());
		final XNInterpreter interp = new XNInterpreter(ctx);
		Iterator<Map.Entry<String,TFEditor>> i = root.fieldsByName.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<String,TFEditor> e = i.next();
			TFEditor t = e.getValue();
			if (t instanceof JTextComponent) {
				ctx.getVariableMap(e.getKey()).declareVariable(ctx, e.getKey(), XOMStringType.instance, new XOMString(((JTextComponent)t).getText()));
			} else if (t instanceof JLabel) {
				ctx.getVariableMap(e.getKey()).declareVariable(ctx, e.getKey(), XOMStringType.instance, new XOMString(((JLabel)t).getText()));
			} else if (t instanceof AbstractButton) {
				ctx.getVariableMap(e.getKey()).declareVariable(ctx, e.getKey(), XOMBooleanType.instance, ((AbstractButton)t).isSelected() ? XOMBoolean.TRUE : XOMBoolean.FALSE);
			} else if (t instanceof JComboBox) {
				ctx.getVariableMap(e.getKey()).declareVariable(ctx, e.getKey(), XOMStringType.instance, new XOMString(((JComboBox)t).getSelectedItem().toString()));
			}
		}
		XOMVariant r = interp.evaluateExpressionString(s);
		try {
			return XOMBooleanType.instance.canMakeInstanceFrom(ctx, r)
			    && XOMBooleanType.instance.makeInstanceFrom(ctx, r).toBoolean();
		} catch (Exception e) {
			return false;
		}
	}
	
	protected void addListeners(Map<String,TFEditor> m, List<?> p) {
		Iterator<?> i = p.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof List) {
				addListeners(m, (List<?>)o);
			} else if (m.containsKey(o.toString())) {
				addListeners(m.get(o.toString()));
			}
		}
	}
	
	public void addListeners(TFEditor tfe) {
		if (tfe instanceof JTextComponent) {
			((JTextComponent)tfe).getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {}
				public void insertUpdate(DocumentEvent arg0) { updateDisplay(); }
				public void removeUpdate(DocumentEvent arg0) { updateDisplay(); }
			});
		} else if (tfe instanceof AbstractButton) {
			((AbstractButton)tfe).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) { updateDisplay(); }
			});
		} else if (tfe instanceof JComboBox) {
			((JComboBox)tfe).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) { updateDisplay(); }
			});
		} else if (tfe instanceof JHexEditorSuite) {
			((JHexEditorSuite)tfe).addHexDataChangeListener(new HexDataChangeListener() {
				public void hexDataChanged(HexDataChangeEvent e) { updateDisplay(); }
			});
		} else if (tfe instanceof TFEditorCoordinates) {
			DocumentListener dl = new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {}
				public void insertUpdate(DocumentEvent arg0) { updateDisplay(); }
				public void removeUpdate(DocumentEvent arg0) { updateDisplay(); }
			};
			TFEditorCoordinates cc = (TFEditorCoordinates)tfe;
			if (cc.x != null) cc.x.getDocument().addDocumentListener(dl);
			if (cc.y != null) cc.y.getDocument().addDocumentListener(dl);
			if (cc.z != null) cc.z.getDocument().addDocumentListener(dl);
			if (cc.x2 != null) cc.x2.getDocument().addDocumentListener(dl);
			if (cc.y2 != null) cc.y2.getDocument().addDocumentListener(dl);
		} else if (tfe instanceof TFEditorTemplate) {
			Iterator<TFEditor> i = ((TFEditorTemplate)tfe).getTFEditorIterator();
			while (i.hasNext()) addListeners(i.next());
		} else if (tfe instanceof Component) {
			((Component)tfe).addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) { updateDisplay(); }
				public void mouseEntered(MouseEvent e) { updateDisplay(); }
				public void mouseExited(MouseEvent e) { updateDisplay(); }
				public void mousePressed(MouseEvent e) { updateDisplay(); }
				public void mouseReleased(MouseEvent e) { updateDisplay(); }
			});
		}
	}
}
