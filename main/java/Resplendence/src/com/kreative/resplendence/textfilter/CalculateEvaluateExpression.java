package com.kreative.resplendence.textfilter;

import javax.swing.*;
import com.kreative.resplendence.*;
import com.kreative.openxion.*;

public class CalculateEvaluateExpression implements TextFilter {
	public String category(int i) {
		return "Calculate";
	}
	
	public String filter(int i, String s) {
		final XNUI ui = new XNNullUI();
		final XNContext ctx = new XNContext(ui);
		ctx.loadModule(XNStandardModule.instance());
		ctx.loadModule(XNExtendedModule.instance());
		ctx.loadModule(XNAudioModule.instance());
		final XNInterpreter interp = new XNInterpreter(ctx);
		return interp.evaluateExpressionString(s).toTextString(ctx);
	}

	public String name(int i) {
		return "Evaluate XION Expression";
	}
	
	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke(';', ResplMain.META_MASK);
	}
	
	public boolean insert(int i) {
		return false;
	}

	public int numberOfFilters() {
		return 1;
	}
}
