/*
 * Copyright &copy; 2007-2009 Rebecca G. Bettencourt / Kreative Software
 * <p>
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <a href="http://www.mozilla.org/MPL/">http://www.mozilla.org/MPL/</a>
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Alternatively, the contents of this file may be used under the terms
 * of the GNU Lesser General Public License (the "LGPL License"), in which
 * case the provisions of LGPL License are applicable instead of those
 * above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL License and not to allow others to use
 * your version of this file under the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and
 * other provisions required by the LGPL License. If you do not delete
 * the provisions above, a recipient may use your version of this file
 * under either the MPL or the LGPL License.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */

package com.kreative.swing;

import java.awt.*;

public interface JHexEditorColorSchemes {
	public static final Color[] COLOR_SCHEME_SYSTEM = new Color[]{
		SystemColor.text,				// 0 address background
		SystemColor.textText,			// 1 address text
		SystemColor.gray,				// 2 divider between address and hex
		SystemColor.text,				// 3 hex background even rows
		SystemColor.text,				// 4 hex background odd rows
		SystemColor.textText,			// 5 hex text even rows
		SystemColor.textText,			// 6 hex text odd rows
		SystemColor.textInactiveText,	// 7 hex text unprintable chars even rows
		SystemColor.textInactiveText,	// 8 hex text unprintable chars odd rows
		SystemColor.gray,				// 9 divider between words
		SystemColor.black,				// 10 cursor
		SystemColor.red,				// 11 cursor in mid-byte
		SystemColor.gray,				// 12 cursor on inactive side
		SystemColor.gray,				// 13 cursor in mid-byte on inactive side
		SystemColor.textHighlight,		// 14 highlight background even rows
		SystemColor.textHighlight,		// 15 highlight background odd rows
		SystemColor.textHighlight,		// 16 highlight background on inactive side even rows
		SystemColor.textHighlight,		// 17 highlight background on inactive side odd rows
		SystemColor.textHighlightText,	// 18 hex text selected even rows
		SystemColor.textHighlightText,	// 19 hex text selected odd rows
		SystemColor.textHighlightText,	// 20 hex text selected on inactive side even rows
		SystemColor.textHighlightText,	// 21 hex text selected on inactive side odd rows
		SystemColor.textInactiveText,	// 22 hex text selected unprintable chars even rows
		SystemColor.textInactiveText,	// 23 hex text selected unprintable chars odd rows
		SystemColor.textInactiveText,	// 24 hex text selected unprintable chars on inactive side even rows
		SystemColor.textInactiveText,	// 25 hex text selected unprintable chars on inactive side odd rows
		SystemColor.control,			// 26 header background
		SystemColor.controlText,		// 27 header text
		SystemColor.gray,				// 28 header divider
	};
	
	public static final Color[] COLOR_SCHEME_MONO = new Color[]{
		Color.white,		// 0 address background
		Color.black,		// 1 address text
		Color.black,		// 2 divider between address and hex
		Color.white,		// 3 hex background even rows
		Color.white,		// 4 hex background odd rows
		Color.black,		// 5 hex text even rows
		Color.black,		// 6 hex text odd rows
		Color.lightGray,	// 7 hex text unprintable chars even rows
		Color.lightGray,	// 8 hex text unprintable chars odd rows
		Color.black,		// 9 divider between words
		Color.black,		// 10 cursor
		Color.red,			// 11 cursor in mid-byte
		Color.gray,			// 12 cursor on inactive side
		Color.gray,			// 13 cursor in mid-byte on inactive side
		Color.black,		// 14 highlight background even rows
		Color.black,		// 15 highlight background odd rows
		Color.gray,			// 16 highlight background on inactive side even rows
		Color.gray,			// 17 highlight background on inactive side odd rows
		Color.white,		// 18 hex text selected even rows
		Color.white,		// 19 hex text selected odd rows
		Color.white,		// 20 hex text selected on inactive side even rows
		Color.white,		// 21 hex text selected on inactive side odd rows
		Color.darkGray,		// 22 hex text selected unprintable chars even rows
		Color.darkGray,		// 23 hex text selected unprintable chars odd rows
		Color.darkGray,		// 24 hex text selected unprintable chars on inactive side even rows
		Color.darkGray,		// 25 hex text selected unprintable chars on inactive side odd rows
		Color.white,		// 26 header background
		Color.black,		// 27 header text
		Color.black,		// 28 header divider
	};
	
	public static final Color[] COLOR_SCHEME_AMBER = new Color[]{
		new Color(0xFF443300,true),	// 0 address background
		new Color(0xFFFFEE00,true),	// 1 address text
		new Color(0xFF554400,true),	// 2 divider between address and hex
		new Color(0xFF000000,true),	// 3 hex background even rows
		new Color(0xFF332200,true),	// 4 hex background odd rows
		new Color(0xFFFFEE00,true),	// 5 hex text even rows
		new Color(0xFFFFEE00,true),	// 6 hex text odd rows
		new Color(0x40FFEE00,true),	// 7 hex text unprintable chars even rows
		new Color(0x40FFEE00,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF554400,true),	// 9 divider between words
		new Color(0xFFFFEE00,true),	// 10 cursor
		new Color(0xFFFFFF99,true),	// 11 cursor in mid-byte
		new Color(0xFF807700,true),	// 12 cursor on inactive side
		new Color(0xFF80804C,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFFEE00,true),	// 14 highlight background even rows
		new Color(0xFFFFEE00,true),	// 15 highlight background odd rows
		new Color(0x66FFEE00,true),	// 16 highlight background on inactive side even rows
		new Color(0x66FFEE00,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF443300,true),	// 26 header background
		new Color(0xFFFFEE00,true),	// 27 header text
		new Color(0xFF554400,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_AMERICA = new Color[]{
		new Color(0xFFFFDDDD,true),	// 0 address background
		new Color(0xFF0000CC,true),	// 1 address text
		new Color(0xFFDD0000,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFE6E6FF,true),	// 4 hex background odd rows
		new Color(0xFF0000CC,true),	// 5 hex text even rows
		new Color(0xFF0000CC,true),	// 6 hex text odd rows
		new Color(0x400000CC,true),	// 7 hex text unprintable chars even rows
		new Color(0x400000CC,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFDD0000,true),	// 9 divider between words
		new Color(0xFFFF0000,true),	// 10 cursor
		new Color(0xFFFF00FF,true),	// 11 cursor in mid-byte
		new Color(0xFFFF8080,true),	// 12 cursor on inactive side
		new Color(0xFFFF80FF,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFF9999,true),	// 14 highlight background even rows
		new Color(0xFFFF9999,true),	// 15 highlight background odd rows
		new Color(0x66FF9999,true),	// 16 highlight background on inactive side even rows
		new Color(0x66FF9999,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF0000CC,true),	// 18 hex text selected even rows
		new Color(0xFF0000CC,true),	// 19 hex text selected odd rows
		new Color(0xFF0000CC,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF0000CC,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x400000CC,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x400000CC,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x400000CC,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x400000CC,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFFFDDDD,true),	// 26 header background
		new Color(0xFF0000CC,true),	// 27 header text
		new Color(0xFFDD0000,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_AQUA = new Color[]{
		new Color(0xFFE6E6E6,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFDEDEDE,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFE6E6FF,true),	// 4 hex background odd rows
		new Color(0xFF32323D,true),	// 5 hex text even rows
		new Color(0xFF32323D,true),	// 6 hex text odd rows
		new Color(0x4032323D,true),	// 7 hex text unprintable chars even rows
		new Color(0x4032323D,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFBFBFBF,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF89B8FC,true),	// 14 highlight background even rows
		new Color(0xFF89B8FC,true),	// 15 highlight background odd rows
		new Color(0x6689B8FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x6689B8FC,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF32323D,true),	// 18 hex text selected even rows
		new Color(0xFF32323D,true),	// 19 hex text selected odd rows
		new Color(0xFF32323D,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF32323D,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x4032323D,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x4032323D,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x4032323D,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x4032323D,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFE6E6E6,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFF32323D,true),	// 28 header divider
	};
	
	// 1 guacamole = Avocado's number
	public static final Color[] COLOR_SCHEME_AVOCADO = new Color[]{
		new Color(0xFFBFE658,true),	// 0 address background
		new Color(0xFF5B8B23,true),	// 1 address text
		new Color(0xFF8ACA7A,true),	// 2 divider between address and hex
		new Color(0xFFD6FFAF,true),	// 3 hex background even rows
		new Color(0xFFD6EAAF,true),	// 4 hex background odd rows
		new Color(0xFF546020,true),	// 5 hex text even rows
		new Color(0xFF546020,true),	// 6 hex text odd rows
		new Color(0x40546020,true),	// 7 hex text unprintable chars even rows
		new Color(0x40546020,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF90BF03,true),	// 9 divider between words
		new Color(0xFF290050,true),	// 10 cursor
		new Color(0xFFE00050,true),	// 11 cursor in mid-byte
		new Color(0xFF9580A8,true),	// 12 cursor on inactive side
		new Color(0xFFF080A8,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF8DC83D,true),	// 14 highlight background even rows
		new Color(0xFF8DC83D,true),	// 15 highlight background odd rows
		new Color(0x668DC83D,true),	// 16 highlight background on inactive side even rows
		new Color(0x668DC83D,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF546020,true),	// 18 hex text selected even rows
		new Color(0xFF546020,true),	// 19 hex text selected odd rows
		new Color(0xFF546020,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF546020,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40546020,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40546020,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40546020,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40546020,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFD7FE58,true),	// 26 header background
		new Color(0xFF0B421E,true),	// 27 header text
		new Color(0xFFB5CA2F,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_BLACK_AND_WHITE = new Color[]{
		new Color(0xFFEEEEEE,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFCBCBCB,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFF2F2F2,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF000000,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFC6C6C6,true),	// 14 highlight background even rows
		new Color(0xFFC6C6C6,true),	// 15 highlight background odd rows
		new Color(0x99C6C6C6,true),	// 16 highlight background on inactive side even rows
		new Color(0x99C6C6C6,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFEEEEEE,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFF555555,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_BLUE = new Color[]{
		new Color(0xFFCAEBFF,true),	// 0 address background
		new Color(0xFF2A0E78,true),	// 1 address text
		new Color(0xFFA8D6E6,true),	// 2 divider between address and hex
		new Color(0xFFD6F8FF,true),	// 3 hex background even rows
		new Color(0xFFCFF2FF,true),	// 4 hex background odd rows
		new Color(0xFF230B63,true),	// 5 hex text even rows
		new Color(0xFF230B63,true),	// 6 hex text odd rows
		new Color(0x40230B63,true),	// 7 hex text unprintable chars even rows
		new Color(0x40230B63,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF7575FF,true),	// 9 divider between words
		new Color(0xFF300D00,true),	// 10 cursor
		new Color(0xFFD00D00,true),	// 11 cursor in mid-byte
		new Color(0xFF988680,true),	// 12 cursor on inactive side
		new Color(0xFFCC8680,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF89B8FC,true),	// 14 highlight background even rows
		new Color(0xFF89B8FC,true),	// 15 highlight background odd rows
		new Color(0x6689B8FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x6689B8FC,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF230B63,true),	// 18 hex text selected even rows
		new Color(0xFF230B63,true),	// 19 hex text selected odd rows
		new Color(0xFF230B63,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF230B63,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40230B63,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40230B63,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40230B63,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40230B63,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFADDDFA,true),	// 26 header background
		new Color(0xFF270D6E,true),	// 27 header text
		new Color(0xFF7AADC5,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_CAL_POLY = new Color[]{
		new Color(0xFF1E431B,true),	// 0 address background
		new Color(0xFFFFFFFF,true),	// 1 address text
		new Color(0xFF374E34,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFF2E9D0,true),	// 4 hex background odd rows
		new Color(0xFF004400,true),	// 5 hex text even rows
		new Color(0xFF004400,true),	// 6 hex text odd rows
		new Color(0x40004400,true),	// 7 hex text unprintable chars even rows
		new Color(0x40004400,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFA0B29F,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFD7AE45,true),	// 14 highlight background even rows
		new Color(0xFFD7AE45,true),	// 15 highlight background odd rows
		new Color(0x66D7AE45,true),	// 16 highlight background on inactive side even rows
		new Color(0x66D7AE45,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF004400,true),	// 18 hex text selected even rows
		new Color(0xFF004400,true),	// 19 hex text selected odd rows
		new Color(0xFF004400,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF004400,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40004400,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40004400,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40004400,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40004400,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFD7AE45,true),	// 26 header background
		new Color(0xFF003700,true),	// 27 header text
		new Color(0xFF374E34,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_DARK_BLUE = new Color[]{
		new Color(0xFF6C18B0,true),	// 0 address background
		new Color(0xFFFFC7D2,true),	// 1 address text
		new Color(0xFF8154D1,true),	// 2 divider between address and hex
		new Color(0xFF1822CD,true),	// 3 hex background even rows
		new Color(0xFF1116AE,true),	// 4 hex background odd rows
		new Color(0xFFFAFA11,true),	// 5 hex text even rows
		new Color(0xFFFAFA11,true),	// 6 hex text odd rows
		new Color(0x40FAFA11,true),	// 7 hex text unprintable chars even rows
		new Color(0x40FAFA11,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF02007D,true),	// 9 divider between words
		new Color(0xFFE7DD32,true),	// 10 cursor
		new Color(0xFFFF6600,true),	// 11 cursor in mid-byte
		new Color(0xFF736E19,true),	// 12 cursor on inactive side
		new Color(0xFF803B06,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFAFA11,true),	// 14 highlight background even rows
		new Color(0xFFFAFA11,true),	// 15 highlight background odd rows
		new Color(0x66FAFA11,true),	// 16 highlight background on inactive side even rows
		new Color(0x66FAFA11,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF1822CD,true),	// 18 hex text selected even rows
		new Color(0xFF1116AE,true),	// 19 hex text selected odd rows
		new Color(0xFF1822CD,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF1116AE,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x401822CD,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x401116AE,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x401822CD,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x401116AE,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF4B45A6,true),	// 26 header background
		new Color(0xFFEEEEEE,true),	// 27 header text
		new Color(0xFF402ECC,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_GRAY = new Color[]{
		new Color(0xFFCCCCCC,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFF888888,true),	// 2 divider between address and hex
		new Color(0xFFEEEEEE,true),	// 3 hex background even rows
		new Color(0xFFD9D9D9,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFBFBFBF,true),	// 9 divider between words
		new Color(0xFF111111,true),	// 10 cursor
		new Color(0xFFDD1111,true),	// 11 cursor in mid-byte
		new Color(0xFF888888,true),	// 12 cursor on inactive side
		new Color(0xFFEE8888,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF9E9E9E,true),	// 14 highlight background even rows
		new Color(0xFF9E9E9E,true),	// 15 highlight background odd rows
		new Color(0x669E9E9E,true),	// 16 highlight background on inactive side even rows
		new Color(0x669E9E9E,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFAAAAAA,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFF555555,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_GREEN = new Color[]{
		new Color(0xFF43D66F,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFF00BF00,true),	// 2 divider between address and hex
		new Color(0xFFDCFFDC,true),	// 3 hex background even rows
		new Color(0xFFC7FFC7,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF7AFF9C,true),	// 9 divider between words
		new Color(0xFF230023,true),	// 10 cursor
		new Color(0xFFD00023,true),	// 11 cursor in mid-byte
		new Color(0xFF928092,true),	// 12 cursor on inactive side
		new Color(0xFFF08092,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF5DF124,true),	// 14 highlight background even rows
		new Color(0xFF5DF124,true),	// 15 highlight background odd rows
		new Color(0x665DF124,true),	// 16 highlight background on inactive side even rows
		new Color(0x665DF124,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF7CEE8D,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFF00A100,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_GREENSCREEN = new Color[]{
		new Color(0xFF003300,true),	// 0 address background
		new Color(0xFF00FF00,true),	// 1 address text
		new Color(0xFF004400,true),	// 2 divider between address and hex
		new Color(0xFF000000,true),	// 3 hex background even rows
		new Color(0xFF002200,true),	// 4 hex background odd rows
		new Color(0xFF00FF00,true),	// 5 hex text even rows
		new Color(0xFF00FF00,true),	// 6 hex text odd rows
		new Color(0x4000FF00,true),	// 7 hex text unprintable chars even rows
		new Color(0x4000FF00,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF004400,true),	// 9 divider between words
		new Color(0xFF00EE00,true),	// 10 cursor
		new Color(0xFF99FF99,true),	// 11 cursor in mid-byte
		new Color(0xFF007700,true),	// 12 cursor on inactive side
		new Color(0xFF4C804C,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF00FF00,true),	// 14 highlight background even rows
		new Color(0xFF00FF00,true),	// 15 highlight background odd rows
		new Color(0x6600FF00,true),	// 16 highlight background on inactive side even rows
		new Color(0x6600FF00,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF003300,true),	// 26 header background
		new Color(0xFF00FF00,true),	// 27 header text
		new Color(0xFF004400,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_GREY = new Color[]{
		new Color(0xFFD6D6D6,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFC5C5C5,true),	// 2 divider between address and hex
		new Color(0xFFEEEEEE,true),	// 3 hex background even rows
		new Color(0xFFDADADA,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFBFBFBF,true),	// 9 divider between words
		new Color(0xFF111111,true),	// 10 cursor
		new Color(0xFFDD1111,true),	// 11 cursor in mid-byte
		new Color(0xFF888888,true),	// 12 cursor on inactive side
		new Color(0xFFEE8888,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFB0B7C0,true),	// 14 highlight background even rows
		new Color(0xFFB0B7C0,true),	// 15 highlight background odd rows
		new Color(0x66B0B7C0,true),	// 16 highlight background on inactive side even rows
		new Color(0x66B0B7C0,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFDDDDE8,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFFAAAAAA,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_LAVENDER = new Color[]{
		new Color(0xFFE4E0F6,true),	// 0 address background
		new Color(0xFF3A004E,true),	// 1 address text
		new Color(0xFFD3C3EF,true),	// 2 divider between address and hex
		new Color(0xFFF6EAFF,true),	// 3 hex background even rows
		new Color(0xFFE2DEF6,true),	// 4 hex background odd rows
		new Color(0xFF3A004E,true),	// 5 hex text even rows
		new Color(0xFF3A004E,true),	// 6 hex text odd rows
		new Color(0x403A004E,true),	// 7 hex text unprintable chars even rows
		new Color(0x403A004E,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFDDB3FF,true),	// 9 divider between words
		new Color(0xFF091500,true),	// 10 cursor
		new Color(0xFFD01500,true),	// 11 cursor in mid-byte
		new Color(0xFF848C80,true),	// 12 cursor on inactive side
		new Color(0xFFF08C80,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFCB88FC,true),	// 14 highlight background even rows
		new Color(0xFFCB88F6,true),	// 15 highlight background odd rows
		new Color(0x66CB88FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x66CB88F6,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF3A004E,true),	// 18 hex text selected even rows
		new Color(0xFF3A004E,true),	// 19 hex text selected odd rows
		new Color(0xFF3A004E,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF3A004E,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x403A004E,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x403A004E,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x403A004E,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x403A004E,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFD9D9F3,true),	// 26 header background
		new Color(0xFF3A034E,true),	// 27 header text
		new Color(0xFFC1ABE9,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_NEW_TECH = new Color[]{
		new Color(0xFF662A85,true),	// 0 address background
		new Color(0xFFFFFFFF,true),	// 1 address text
		new Color(0xFF331542,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFE9E2F8,true),	// 4 hex background odd rows
		new Color(0xFF110018,true),	// 5 hex text even rows
		new Color(0xFF110018,true),	// 6 hex text odd rows
		new Color(0x40110018,true),	// 7 hex text unprintable chars even rows
		new Color(0x40110018,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF994068,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFCC66FF,true),	// 14 highlight background even rows
		new Color(0xFFCC66FF,true),	// 15 highlight background odd rows
		new Color(0x66CC66FF,true),	// 16 highlight background on inactive side even rows
		new Color(0x66CC66FF,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF110018,true),	// 18 hex text selected even rows
		new Color(0xFF110018,true),	// 19 hex text selected odd rows
		new Color(0xFF110018,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF110018,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40110018,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40110018,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40110018,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40110018,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF662A85,true),	// 26 header background
		new Color(0xFFFFFFFF,true),	// 27 header text
		new Color(0xFF331542,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_OCEAN = new Color[]{
		new Color(0xFFBDE5E6,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFC7E3E7,true),	// 2 divider between address and hex
		new Color(0xFFE6FBFA,true),	// 3 hex background even rows
		new Color(0xFFD6F1F1,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFA3D1D9,true),	// 9 divider between words
		new Color(0xFF190405,true),	// 10 cursor
		new Color(0xFFD00405,true),	// 11 cursor in mid-byte
		new Color(0xFF8C8283,true),	// 12 cursor on inactive side
		new Color(0xFFF08283,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF89B8FA,true),	// 14 highlight background even rows
		new Color(0xFF89B8F1,true),	// 15 highlight background odd rows
		new Color(0x6689B8FA,true),	// 16 highlight background on inactive side even rows
		new Color(0x6689B8F1,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFB4DAE0,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFFC9E6EA,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_PASTELS = new Color[]{
		new Color(0xFFB8CBFA,true),	// 0 address background
		new Color(0xFF511285,true),	// 1 address text
		new Color(0xFF6876E7,true),	// 2 divider between address and hex
		new Color(0xFFFBFAC7,true),	// 3 hex background even rows
		new Color(0xFFD9FDD4,true),	// 4 hex background odd rows
		new Color(0xFF15544F,true),	// 5 hex text even rows
		new Color(0xFF15544F,true),	// 6 hex text odd rows
		new Color(0x4015544F,true),	// 7 hex text unprintable chars even rows
		new Color(0x4015544F,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF7D9E7E,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFBB18C,true),	// 14 highlight background even rows
		new Color(0xFFD9B18C,true),	// 15 highlight background odd rows
		new Color(0x66FBB18C,true),	// 16 highlight background on inactive side even rows
		new Color(0x66D9B18C,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF15544F,true),	// 18 hex text selected even rows
		new Color(0xFF15544F,true),	// 19 hex text selected odd rows
		new Color(0xFF15544F,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF15544F,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x4015544F,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x4015544F,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x4015544F,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x4015544F,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFD0BFEE,true),	// 26 header background
		new Color(0xFF37129C,true),	// 27 header text
		new Color(0xFF6876E7,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_PINK = new Color[]{
		new Color(0xFFF3D7D9,true),	// 0 address background
		new Color(0xFF550000,true),	// 1 address text
		new Color(0xFFFFB8CA,true),	// 2 divider between address and hex
		new Color(0xFFFFEEEE,true),	// 3 hex background even rows
		new Color(0xFFF2D6D6,true),	// 4 hex background odd rows
		new Color(0xFF550000,true),	// 5 hex text even rows
		new Color(0xFF550000,true),	// 6 hex text odd rows
		new Color(0x40550000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40550000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFD3ABDE,true),	// 9 divider between words
		new Color(0xFF001111,true),	// 10 cursor
		new Color(0xFFCC1111,true),	// 11 cursor in mid-byte
		new Color(0xFF808888,true),	// 12 cursor on inactive side
		new Color(0xFFE68888,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFF19EBD,true),	// 14 highlight background even rows
		new Color(0xFFF19EBD,true),	// 15 highlight background odd rows
		new Color(0x66F19EBD,true),	// 16 highlight background on inactive side even rows
		new Color(0x66F19EBD,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF550000,true),	// 18 hex text selected even rows
		new Color(0xFF550000,true),	// 19 hex text selected odd rows
		new Color(0xFF550000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF550000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40550000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40550000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40550000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40550000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFF3D7D9,true),	// 26 header background
		new Color(0xFF7D0000,true),	// 27 header text
		new Color(0xFFFFBBCF,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_RED = new Color[]{
		new Color(0xFFFF8080,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFFF0000,true),	// 2 divider between address and hex
		new Color(0xFFFFE6E6,true),	// 3 hex background even rows
		new Color(0xFFFFBDBD,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFFF9199,true),	// 9 divider between words
		new Color(0xFF001919,true),	// 10 cursor
		new Color(0xFFCC1919,true),	// 11 cursor in mid-byte
		new Color(0xFF808C8C,true),	// 12 cursor on inactive side
		new Color(0xFFE68C8C,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFED715F,true),	// 14 highlight background even rows
		new Color(0xFFED715F,true),	// 15 highlight background odd rows
		new Color(0x66ED715F,true),	// 16 highlight background on inactive side even rows
		new Color(0x66ED715F,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFFF8080,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFFFF0000,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_RED_PASTEL = new Color[]{
		new Color(0xFFFBC6C8,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFC893A5,true),	// 2 divider between address and hex
		new Color(0xFFFFDCFF,true),	// 3 hex background even rows
		new Color(0xFFFDD6D6,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFE6AEEB,true),	// 9 divider between words
		new Color(0xFF002300,true),	// 10 cursor
		new Color(0xFFCC2300,true),	// 11 cursor in mid-byte
		new Color(0xFF809280,true),	// 12 cursor on inactive side
		new Color(0xFFE69280,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFF19B8F,true),	// 14 highlight background even rows
		new Color(0xFFF19B8F,true),	// 15 highlight background odd rows
		new Color(0x99F19B8F,true),	// 16 highlight background on inactive side even rows
		new Color(0x99F19B8F,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFF6ADAE,true),	// 26 header background
		new Color(0xFF59000E,true),	// 27 header text
		new Color(0xFFCC7F88,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_SAGE = new Color[]{
		new Color(0xFFE6E6E6,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFDEDEDE,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFE6F3D9,true),	// 4 hex background odd rows
		new Color(0xFF32323D,true),	// 5 hex text even rows
		new Color(0xFF32323D,true),	// 6 hex text odd rows
		new Color(0x4032323D,true),	// 7 hex text unprintable chars even rows
		new Color(0x4032323D,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFAEB8BF,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFCEF696,true),	// 14 highlight background even rows
		new Color(0xFFCEF396,true),	// 15 highlight background odd rows
		new Color(0x99CEF696,true),	// 16 highlight background on inactive side even rows
		new Color(0x99CEF396,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF32323D,true),	// 18 hex text selected even rows
		new Color(0xFF32323D,true),	// 19 hex text selected odd rows
		new Color(0xFF32323D,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF32323D,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x4032323D,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x4032323D,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x4032323D,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x4032323D,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFE6E6E6,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFF32323D,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_SFSU = new Color[]{
		new Color(0xFF232176,true),	// 0 address background
		new Color(0xFFFFFFFF,true),	// 1 address text
		new Color(0xFF232176,true),	// 2 divider between address and hex
		new Color(0xFFFFFFFF,true),	// 3 hex background even rows
		new Color(0xFFFDF0D8,true),	// 4 hex background odd rows
		new Color(0xFF232176,true),	// 5 hex text even rows
		new Color(0xFF232176,true),	// 6 hex text odd rows
		new Color(0x40232176,true),	// 7 hex text unprintable chars even rows
		new Color(0x40232176,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF43428A,true),	// 9 divider between words
		new Color(0xFF000000,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF808080,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFE5C050,true),	// 14 highlight background even rows
		new Color(0xFFE2AF2D,true),	// 15 highlight background odd rows
		new Color(0x66E5C050,true),	// 16 highlight background on inactive side even rows
		new Color(0x66E2AF2D,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF232176,true),	// 18 hex text selected even rows
		new Color(0xFF232176,true),	// 19 hex text selected odd rows
		new Color(0xFF232176,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF232176,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40232176,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40232176,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40232176,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40232176,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF43428A,true),	// 26 header background
		new Color(0xFFFFFFFF,true),	// 27 header text
		new Color(0xFF43428A,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_SKY = new Color[]{
		new Color(0xFFE1EDFF,true),	// 0 address background
		new Color(0xFF000055,true),	// 1 address text
		new Color(0xFFA4D3EB,true),	// 2 divider between address and hex
		new Color(0xFFDEF8FF,true),	// 3 hex background even rows
		new Color(0xFFE0EAFF,true),	// 4 hex background odd rows
		new Color(0xFF000055,true),	// 5 hex text even rows
		new Color(0xFF000055,true),	// 6 hex text odd rows
		new Color(0x40000055,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000055,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF9EB5FF,true),	// 9 divider between words
		new Color(0xFF210700,true),	// 10 cursor
		new Color(0xFFDC0700,true),	// 11 cursor in mid-byte
		new Color(0xFF908480,true),	// 12 cursor on inactive side
		new Color(0xFFF68480,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF89B8FC,true),	// 14 highlight background even rows
		new Color(0xFF89B8FC,true),	// 15 highlight background odd rows
		new Color(0x6689B8FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x6689B8FC,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000055,true),	// 18 hex text selected even rows
		new Color(0xFF000055,true),	// 19 hex text selected odd rows
		new Color(0xFF000055,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000055,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000055,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000055,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000055,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000055,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFDEEEFF,true),	// 26 header background
		new Color(0xFF00007D,true),	// 27 header text
		new Color(0xFF9CB6CE,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_THIS_IS_BECKIES_HEX_EDITOR = new Color[]{
		new Color(0xFFCC00CC,true),	// 0 address background
		new Color(0xFFFFFF00,true),	// 1 address text
		new Color(0xFFCC9966,true),	// 2 divider between address and hex
		new Color(0xFFFF00FF,true),	// 3 hex background even rows
		new Color(0xFFEE00EE,true),	// 4 hex background odd rows
		new Color(0xFFFFFF00,true),	// 5 hex text even rows
		new Color(0xFFFFFF00,true),	// 6 hex text odd rows
		new Color(0x80FFFF00,true),	// 7 hex text unprintable chars even rows
		new Color(0x80FFFF00,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFCC9966,true),	// 9 divider between words
		new Color(0xFF666600,true),	// 10 cursor
		new Color(0xFFCC0000,true),	// 11 cursor in mid-byte
		new Color(0xFF999980,true),	// 12 cursor on inactive side
		new Color(0xFFE68080,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFFFF66,true),	// 14 highlight background even rows
		new Color(0xFFFFFF66,true),	// 15 highlight background odd rows
		new Color(0x99FFFF66,true),	// 16 highlight background on inactive side even rows
		new Color(0x99FFFF66,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFFCC00CC,true),	// 18 hex text selected even rows
		new Color(0xFFCC00CC,true),	// 19 hex text selected odd rows
		new Color(0xFFCC00CC,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFFCC00CC,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x80CC00CC,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x80CC00CC,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x80CC00CC,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x80CC00CC,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFCC00CC,true),	// 26 header background
		new Color(0xFFFFFF00,true),	// 27 header text
		new Color(0xFFCC9966,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_TURQUOISE = new Color[]{
		new Color(0xFFC3EAEA,true),	// 0 address background
		new Color(0xFF003A00,true),	// 1 address text
		new Color(0xFF61AF88,true),	// 2 divider between address and hex
		new Color(0xFFEEFFEE,true),	// 3 hex background even rows
		new Color(0xFFC3EAEA,true),	// 4 hex background odd rows
		new Color(0xFF001F00,true),	// 5 hex text even rows
		new Color(0xFF001F00,true),	// 6 hex text odd rows
		new Color(0x40001F00,true),	// 7 hex text unprintable chars even rows
		new Color(0x40001F00,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF99BFCA,true),	// 9 divider between words
		new Color(0xFF110011,true),	// 10 cursor
		new Color(0xFFCC0011,true),	// 11 cursor in mid-byte
		new Color(0xFF888088,true),	// 12 cursor on inactive side
		new Color(0xFFE68088,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF65EEE0,true),	// 14 highlight background even rows
		new Color(0xFF65EAE0,true),	// 15 highlight background odd rows
		new Color(0x6665EEE0,true),	// 16 highlight background on inactive side even rows
		new Color(0x6665EAE0,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF001F00,true),	// 18 hex text selected even rows
		new Color(0xFF001F00,true),	// 19 hex text selected odd rows
		new Color(0xFF001F00,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF001F00,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40001F00,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40001F00,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40001F00,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40001F00,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFC3EAEA,true),	// 26 header background
		new Color(0xFF003A00,true),	// 27 header text
		new Color(0xFF61AF88,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_VIOLET = new Color[]{
		new Color(0xFFDFC0FF,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFBD7FE6,true),	// 2 divider between address and hex
		new Color(0xFFF9F0FF,true),	// 3 hex background even rows
		new Color(0xFFEEE0FB,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFD8ABFF,true),	// 9 divider between words
		new Color(0xFF060F00,true),	// 10 cursor
		new Color(0xFFCF0F00,true),	// 11 cursor in mid-byte
		new Color(0xFF838880,true),	// 12 cursor on inactive side
		new Color(0xFFE98880,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFCB89FC,true),	// 14 highlight background even rows
		new Color(0xFFCB89FB,true),	// 15 highlight background odd rows
		new Color(0x66CB89FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x66CB89FB,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFDFC0FF,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFFBF7FE6,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_WHITE_ON_BLACK = new Color[]{
		new Color(0xFF333333,true),	// 0 address background
		new Color(0xFFFFFFFF,true),	// 1 address text
		new Color(0xFF444444,true),	// 2 divider between address and hex
		new Color(0xFF000000,true),	// 3 hex background even rows
		new Color(0xFF222222,true),	// 4 hex background odd rows
		new Color(0xFFFFFFFF,true),	// 5 hex text even rows
		new Color(0xFFFFFFFF,true),	// 6 hex text odd rows
		new Color(0x40FFFFFF,true),	// 7 hex text unprintable chars even rows
		new Color(0x40FFFFFF,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF444444,true),	// 9 divider between words
		new Color(0xFFEEEEEE,true),	// 10 cursor
		new Color(0xFFCCCCFF,true),	// 11 cursor in mid-byte
		new Color(0xFF777777,true),	// 12 cursor on inactive side
		new Color(0xFF666688,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFFFFFF,true),	// 14 highlight background even rows
		new Color(0xFFFFFFFF,true),	// 15 highlight background odd rows
		new Color(0x66FFFFFF,true),	// 16 highlight background on inactive side even rows
		new Color(0x66FFFFFF,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF333333,true),	// 26 header background
		new Color(0xFFFFFFFF,true),	// 27 header text
		new Color(0xFF444444,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_WHITE_ON_BLUE = new Color[]{
		new Color(0xFF0000CC,true),	// 0 address background
		new Color(0xFFFFFFFF,true),	// 1 address text
		new Color(0xFF0000BB,true),	// 2 divider between address and hex
		new Color(0xFF0000FF,true),	// 3 hex background even rows
		new Color(0xFF0000DD,true),	// 4 hex background odd rows
		new Color(0xFFFFFFFF,true),	// 5 hex text even rows
		new Color(0xFFFFFFFF,true),	// 6 hex text odd rows
		new Color(0x80FFFFFF,true),	// 7 hex text unprintable chars even rows
		new Color(0x80FFFFFF,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF0000DD,true),	// 9 divider between words
		new Color(0xFFEEEEFF,true),	// 10 cursor
		new Color(0xFFFFFF66,true),	// 11 cursor in mid-byte
		new Color(0xFF777788,true),	// 12 cursor on inactive side
		new Color(0xFF888833,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFFFFFFF,true),	// 14 highlight background even rows
		new Color(0xFFFFFFFF,true),	// 15 highlight background odd rows
		new Color(0x99FFFFFF,true),	// 16 highlight background on inactive side even rows
		new Color(0x99FFFFFF,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF0000FF,true),	// 18 hex text selected even rows
		new Color(0xFF0000FF,true),	// 19 hex text selected odd rows
		new Color(0xFF0000FF,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF0000FF,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x800000FF,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x800000FF,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x800000FF,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x800000FF,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF0000CC,true),	// 26 header background
		new Color(0xFFFFFFFF,true),	// 27 header text
		new Color(0xFF0000BB,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_WILD_BLUE = new Color[]{
		new Color(0xFF71CAD0,true),	// 0 address background
		new Color(0xFF550000,true),	// 1 address text
		new Color(0xFF5DBBCB,true),	// 2 divider between address and hex
		new Color(0xFFD9F3FF,true),	// 3 hex background even rows
		new Color(0xFFCBEEEE,true),	// 4 hex background odd rows
		new Color(0xFF590059,true),	// 5 hex text even rows
		new Color(0xFF590059,true),	// 6 hex text odd rows
		new Color(0x40590059,true),	// 7 hex text unprintable chars even rows
		new Color(0x40590059,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFF6F77BF,true),	// 9 divider between words
		new Color(0xFF260C00,true),	// 10 cursor
		new Color(0xFFE00C00,true),	// 11 cursor in mid-byte
		new Color(0xFF938680,true),	// 12 cursor on inactive side
		new Color(0xFFF08680,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFF89B8FC,true),	// 14 highlight background even rows
		new Color(0xFF89B8EE,true),	// 15 highlight background odd rows
		new Color(0x6689B8FC,true),	// 16 highlight background on inactive side even rows
		new Color(0x6689B8EE,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF590059,true),	// 18 hex text selected even rows
		new Color(0xFF590059,true),	// 19 hex text selected odd rows
		new Color(0xFF590059,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF590059,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40590059,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40590059,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40590059,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40590059,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFF5DBACA,true),	// 26 header background
		new Color(0xFF6C18B0,true),	// 27 header text
		new Color(0xFF184B81,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_WILD_RED = new Color[]{
		new Color(0xFFE56278,true),	// 0 address background
		new Color(0xFF004E00,true),	// 1 address text
		new Color(0xFFB8377E,true),	// 2 divider between address and hex
		new Color(0xFFFFDCFF,true),	// 3 hex background even rows
		new Color(0xFFFDCACA,true),	// 4 hex background odd rows
		new Color(0xFF00004E,true),	// 5 hex text even rows
		new Color(0xFF00004E,true),	// 6 hex text odd rows
		new Color(0x4000004E,true),	// 7 hex text unprintable chars even rows
		new Color(0x4000004E,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFCC9DD4,true),	// 9 divider between words
		new Color(0xFF002300,true),	// 10 cursor
		new Color(0xFFCC2300,true),	// 11 cursor in mid-byte
		new Color(0xFF809180,true),	// 12 cursor on inactive side
		new Color(0xFFE69180,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFEC6746,true),	// 14 highlight background even rows
		new Color(0xFFEC6746,true),	// 15 highlight background odd rows
		new Color(0x66EC6746,true),	// 16 highlight background on inactive side even rows
		new Color(0x66EC6746,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000046,true),	// 18 hex text selected even rows
		new Color(0xFF000046,true),	// 19 hex text selected odd rows
		new Color(0xFF000046,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000046,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000046,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000046,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000046,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000046,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFE6949C,true),	// 26 header background
		new Color(0xFF7C2D7D,true),	// 27 header text
		new Color(0xFFCC7F88,true),	// 28 header divider
	};

	public static final Color[] COLOR_SCHEME_YELLOW = new Color[]{
		new Color(0xFFE6E658,true),	// 0 address background
		new Color(0xFF000000,true),	// 1 address text
		new Color(0xFFF6F3B2,true),	// 2 divider between address and hex
		new Color(0xFFF0F095,true),	// 3 hex background even rows
		new Color(0xFFEAEA80,true),	// 4 hex background odd rows
		new Color(0xFF000000,true),	// 5 hex text even rows
		new Color(0xFF000000,true),	// 6 hex text odd rows
		new Color(0x40000000,true),	// 7 hex text unprintable chars even rows
		new Color(0x40000000,true),	// 8 hex text unprintable chars odd rows
		new Color(0xFFCCC16E,true),	// 9 divider between words
		new Color(0xFF0F0F6A,true),	// 10 cursor
		new Color(0xFFCF0F6A,true),	// 11 cursor in mid-byte
		new Color(0xFF8888B5,true),	// 12 cursor on inactive side
		new Color(0xFFE888B5,true),	// 13 cursor in mid-byte on inactive side
		new Color(0xFFD7C63D,true),	// 14 highlight background even rows
		new Color(0xFFD7C63D,true),	// 15 highlight background odd rows
		new Color(0x66D7C63D,true),	// 16 highlight background on inactive side even rows
		new Color(0x66D7C63D,true),	// 17 highlight background on inactive side odd rows
		new Color(0xFF000000,true),	// 18 hex text selected even rows
		new Color(0xFF000000,true),	// 19 hex text selected odd rows
		new Color(0xFF000000,true),	// 20 hex text selected on inactive side even rows
		new Color(0xFF000000,true),	// 21 hex text selected on inactive side odd rows
		new Color(0x40000000,true),	// 22 hex text selected unprintable chars even rows
		new Color(0x40000000,true),	// 23 hex text selected unprintable chars odd rows
		new Color(0x40000000,true),	// 24 hex text selected unprintable chars on inactive side even rows
		new Color(0x40000000,true),	// 25 hex text selected unprintable chars on inactive side odd rows
		new Color(0xFFEBE98A,true),	// 26 header background
		new Color(0xFF000000,true),	// 27 header text
		new Color(0xFFFCFA92,true),	// 28 header divider
	};
	
	public static final String[] COLOR_SCHEME_NAMES = new String[] {
			"System",
			"Mono",
			"Amber",
			"America",
			"Aqua",
			"Avocado",
			"Black & White",
			"Blue",
			"Cal Poly",
			"Dark Blue",
			"Gray",
			"Green",
			"Greenscreen",
			"Grey",
			"Lavender",
			"New Tech",
			"Ocean",
			"Pastels",
			"Pink",
			"Red",
			"Red Pastel",
			"Sage",
			"SFSU",
			"Sky",
			"This is Beckie's hex editor!",
			"Turquoise",
			"Violet",
			"White on Black",
			"White on Blue",
			"Wild Blue",
			"Wild Red",
			"Yellow",
	};
	
	public static final Color[][] COLOR_SCHEMES = new Color[][]{
			COLOR_SCHEME_SYSTEM,
			COLOR_SCHEME_MONO,
			COLOR_SCHEME_AMBER,
			COLOR_SCHEME_AMERICA,
			COLOR_SCHEME_AQUA,
			COLOR_SCHEME_AVOCADO,
			COLOR_SCHEME_BLACK_AND_WHITE,
			COLOR_SCHEME_BLUE,
			COLOR_SCHEME_CAL_POLY,
			COLOR_SCHEME_DARK_BLUE,
			COLOR_SCHEME_GRAY,
			COLOR_SCHEME_GREEN,
			COLOR_SCHEME_GREENSCREEN,
			COLOR_SCHEME_GREY,
			COLOR_SCHEME_LAVENDER,
			COLOR_SCHEME_NEW_TECH,
			COLOR_SCHEME_OCEAN,
			COLOR_SCHEME_PASTELS,
			COLOR_SCHEME_PINK,
			COLOR_SCHEME_RED,
			COLOR_SCHEME_RED_PASTEL,
			COLOR_SCHEME_SAGE,
			COLOR_SCHEME_SFSU,
			COLOR_SCHEME_SKY,
			COLOR_SCHEME_THIS_IS_BECKIES_HEX_EDITOR,
			COLOR_SCHEME_TURQUOISE,
			COLOR_SCHEME_VIOLET,
			COLOR_SCHEME_WHITE_ON_BLACK,
			COLOR_SCHEME_WHITE_ON_BLUE,
			COLOR_SCHEME_WILD_BLUE,
			COLOR_SCHEME_WILD_RED,
			COLOR_SCHEME_YELLOW,
	};
	
	public static final int COLOR_SCHEME_COUNT = COLOR_SCHEMES.length;
}
