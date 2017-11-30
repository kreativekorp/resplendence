package com.kreative.resplendence.template;

import java.io.*;
import java.util.*;

public class Template extends ArrayList<TemplateField> {
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	public Template() {
		super();
	}
	
	/**
	 * Constructs a list containing the elements of the specified
	 * collection, in the order they are returned by the collection's
	 * iterator. The ArrayList instance has an initial capacity
	 * of 110% the size of the specified collection.
	 * @param c the collection whose elements are to be placed into this list.
	 * @throws NullPointerException if the specified collection is null.
	 */
	public Template(Collection<TemplateField> c) {
		super(c);
	}
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 * @param initialCapacity the initial capacity of the list.
	 * @throws IllegalArgumentException if the specified initial capacity is negative
	 */
	public Template(int initialCapacity) {
		super(initialCapacity);
	}
	
	/**
	 * Constructs a template using the raw binary data of a TMPL resource.
	 * @param tmpl the raw binary data of a TMPL resource.
	 * @throws IllegalArgumentException if the template is somehow invalid.
	 */
	public Template(byte[] tmpl) {
		ArrayList<TemplateField> sstack = new ArrayList<TemplateField>();
		TemplateEnumeration lastenum = null;
		TemplateField lastcounter = null;
		boolean hexdFound = false;
		boolean insideLstb = false;
		int p=0;
		while (p<tmpl.length) {
			String name; int type;
			try {
				name = new String(tmpl, p+1, tmpl[p]&0xFF, "MACROMAN");
			} catch (UnsupportedEncodingException e) {
				name = new String(tmpl, p+1, tmpl[p]&0xFF);
			}
			p += (tmpl[p]&0xFF)+1;
			type = ((tmpl[p]&0xFF)<<24) | ((tmpl[p+1]&0xFF)<<16) | ((tmpl[p+2]&0xFF)<<8) | (tmpl[p+3]&0xFF);
			p += 4;
			if (TemplateField.isCommentType(type)) {
				//skip entirely
			} else if (hexdFound) {
				throw new IllegalArgumentException("This template is invalid. It contains a HEXD field that is not the last non-comment field.");
			} else if (TemplateField.isEnumOptionType(type)) {
				if (lastenum == null) throw new IllegalArgumentException("This template is invalid. It contains an EOPT field that is not preceded by an enumeration field.");
				else lastenum.options.add(new TemplateField(type,name));
			} else if (TemplateField.isEOFTerminatedListType(type)) {
				if (insideLstb) throw new IllegalArgumentException("This template is invalid. It contains a LSTB field inside another LSTB field.");
				insideLstb = true;
				TemplateList thing = new TemplateList(type,name);
				if (sstack.isEmpty()) {
					this.add(thing);
				} else if (sstack.get(0) instanceof TemplateList) {
					((TemplateList)sstack.get(0)).subfields.add(thing);
				} else if (sstack.get(0) instanceof TemplateIfBranch) {
					((TemplateIfBranch)sstack.get(0)).subfields.add(thing);
				} else {
					throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
				}
				sstack.add(0,thing);
			} else if (TemplateField.isZeroTerminatedListType(type)) {
				TemplateList thing = new TemplateList(type,name);
				if (sstack.isEmpty()) {
					this.add(thing);
				} else if (sstack.get(0) instanceof TemplateList) {
					((TemplateList)sstack.get(0)).subfields.add(thing);
				} else if (sstack.get(0) instanceof TemplateIfBranch) {
					((TemplateIfBranch)sstack.get(0)).subfields.add(thing);
				} else {
					throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
				}
				sstack.add(0,thing);
			} else if (TemplateField.isCountedListType(type)) {
				if (lastcounter == null) throw new IllegalArgumentException("This template is invalid. It contains a LSTC field that is not preceded by a ZCNT or OCNT field.");
				TemplateList thing = new TemplateList(type,name);
				thing.counter = lastcounter;
				lastcounter = null;
				if (sstack.isEmpty()) {
					this.add(thing);
				} else if (sstack.get(0) instanceof TemplateList) {
					((TemplateList)sstack.get(0)).subfields.add(thing);
				} else if (sstack.get(0) instanceof TemplateIfBranch) {
					((TemplateIfBranch)sstack.get(0)).subfields.add(thing);
				} else {
					throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
				}
				sstack.add(0,thing);
			} else if (TemplateField.isListEndType(type)) {
				if (sstack.isEmpty() || !(sstack.get(0) instanceof TemplateList))
					throw new IllegalArgumentException("This template is invalid. It contains a LSTE field that does not close a LSTB, LSTC, or LSTZ field.");
				if (TemplateField.isEOFTerminatedListType(sstack.get(0).getType()))
					insideLstb = false;
				sstack.remove(0);
			} else if (TemplateField.isIfType(type)) {
				TemplateIfStructure thing1 = new TemplateIfStructure(type,name);
				TemplateIfBranch thing2 = new TemplateIfBranch(type,name);
				thing1.branches.add(thing2);
				if (sstack.isEmpty()) {
					this.add(thing1);
				} else if (sstack.get(0) instanceof TemplateList) {
					((TemplateList)sstack.get(0)).subfields.add(thing1);
				} else if (sstack.get(0) instanceof TemplateIfBranch) {
					((TemplateIfBranch)sstack.get(0)).subfields.add(thing1);
				} else {
					throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
				}
				sstack.add(0,thing1);
				sstack.add(0,thing2);
			} else if (TemplateField.isElseIfType(type) || TemplateField.isElseType(type)) {
				if (sstack.isEmpty() || !(sstack.get(0) instanceof TemplateIfBranch))
					throw new IllegalArgumentException("This template is invalid. It contains an ELIF or ELSE field that is not preceded by an IF or ELIF field.");
				sstack.remove(0);
				if (sstack.isEmpty() || !(sstack.get(0) instanceof TemplateIfStructure))
					throw new IllegalArgumentException("This template is invalid. It contains an ELIF or ELSE field that is not preceded by an IF or ELIF field.");
				TemplateIfBranch thing = new TemplateIfBranch(type,name);
				((TemplateIfStructure)sstack.get(0)).branches.add(thing);
				sstack.add(0,thing);
			} else if (TemplateField.isEndIfType(type)) {
				if (sstack.isEmpty() || !(sstack.get(0) instanceof TemplateIfBranch))
					throw new IllegalArgumentException("This template is invalid. It contains an ENIF field that does not close an IF field.");
				sstack.remove(0);
				if (sstack.isEmpty() || !(sstack.get(0) instanceof TemplateIfStructure))
					throw new IllegalArgumentException("This template is invalid. It contains an ENIF field that does not close an IF field.");
				sstack.remove(0);
			} else {
				TemplateField thing;
				if (TemplateField.isHexDumpType(type)) {
					hexdFound = true;
					thing = new TemplateField(type,name);
				} else if (TemplateField.isListCounterType(type)) {
					if (lastcounter != null) throw new IllegalArgumentException("This template is invalid. It contains a ZCNT or OCNT field that is not followed by a LSTC field.");
					thing = lastcounter = new TemplateField(type,name);
				} else if (TemplateField.isEnumerationType(type)) {
					thing = lastenum = new TemplateEnumeration(type,name);
				} else {
					thing = new TemplateField(type,name);
				}
				if (sstack.isEmpty()) {
					this.add(thing);
				} else if (sstack.get(0) instanceof TemplateList) {
					((TemplateList)sstack.get(0)).subfields.add(thing);
				} else if (sstack.get(0) instanceof TemplateIfBranch) {
					((TemplateIfBranch)sstack.get(0)).subfields.add(thing);
				} else {
					throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
				}
			}
		}
		if (lastcounter != null) throw new IllegalArgumentException("This template is invalid. It contains a ZCNT or OCNT field that is not followed by a LSTC field.");
		if (!sstack.isEmpty()) {
			if (sstack.get(0) instanceof TemplateList)
				throw new IllegalArgumentException("This template is invalid. It contains a LSTB, LSTC, or LSTZ field that is not closed by a LSTE field.");
			else if (sstack.get(0) instanceof TemplateIfBranch)
				throw new IllegalArgumentException("This template is invalid. It contains an IF field that is not closed by an ENIF field.");
			else
				throw new IllegalArgumentException("This template is invalid. Somehow something got put on the sstack that shouldn't have been.");
		}
	}
}
