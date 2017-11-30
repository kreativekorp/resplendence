package com.kreative.resplendence.acc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.kreative.awt.FractionalSizeGridLayout;
import com.kreative.resplendence.ResplMain;

public class KeyCodeChart implements AccessoryWindow {
	private static WKeyCodeChart instance;
	
	public String category(int i) {
		return null;
	}
	
	public String name(int i) {
		return "Key Codes";
	}

	public void open(int i) {
		if (instance == null) instance = new WKeyCodeChart();
		instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		instance.setVisible(true);
	}
	
	public KeyStroke keystroke(int i) {
		return KeyStroke.getKeyStroke('K', ResplMain.META_ALT_MASK);
	}
	
	public int numberOfWindows() {
		return 1;
	}
	
	private static class WKeyCodeChart extends JFrame implements AWTEventListener {
		private static final long serialVersionUID = 1;
		
		private static final int[] KEY_MAP_MACINTOSH = new int[]{
			KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F,
			KeyEvent.VK_H, KeyEvent.VK_G, KeyEvent.VK_Z, KeyEvent.VK_X,
			KeyEvent.VK_C, KeyEvent.VK_V, 0, KeyEvent.VK_B,
			KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R,
			
			KeyEvent.VK_Y, KeyEvent.VK_T, KeyEvent.VK_1, KeyEvent.VK_2,
			KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_6, KeyEvent.VK_5,
			KeyEvent.VK_EQUALS, KeyEvent.VK_9, KeyEvent.VK_7, KeyEvent.VK_MINUS,
			KeyEvent.VK_8, KeyEvent.VK_0, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_O,
			
			KeyEvent.VK_U, KeyEvent.VK_OPEN_BRACKET, KeyEvent.VK_I, KeyEvent.VK_P,
			KeyEvent.VK_ENTER, KeyEvent.VK_L, KeyEvent.VK_J, KeyEvent.VK_QUOTE,
			KeyEvent.VK_K, KeyEvent.VK_SEMICOLON, KeyEvent.VK_BACK_SLASH, KeyEvent.VK_COMMA,
			KeyEvent.VK_SLASH, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_PERIOD,
			
			KeyEvent.VK_TAB, KeyEvent.VK_SPACE, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_BACK_SPACE,
			0, KeyEvent.VK_ESCAPE, 0, KeyEvent.VK_META,
			KeyEvent.VK_SHIFT, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_ALT, KeyEvent.VK_CONTROL,
			KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, 0,
			
			0, KeyEvent.VK_DECIMAL, 0, KeyEvent.VK_MULTIPLY,
			0, KeyEvent.VK_ADD, 0, KeyEvent.VK_CLEAR,
			0, 0, 0, KeyEvent.VK_DIVIDE,
			KeyEvent.VK_ENTER, KeyEvent.VK_SEPARATOR, KeyEvent.VK_SUBTRACT, 0,
			
			0, KeyEvent.VK_EQUALS, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1,
			KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5,
			KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, 0, KeyEvent.VK_NUMPAD8,
			KeyEvent.VK_NUMPAD9, 0, 0, 0,
			
			KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F3,
			KeyEvent.VK_F8, KeyEvent.VK_F9, 0, KeyEvent.VK_F11,
			0, KeyEvent.VK_F13, 0, KeyEvent.VK_F14,
			0, KeyEvent.VK_F10, 0, KeyEvent.VK_F12,
			
			0, KeyEvent.VK_F15, KeyEvent.VK_HELP, KeyEvent.VK_HOME,
			KeyEvent.VK_PAGE_UP, KeyEvent.VK_DELETE, KeyEvent.VK_F4, KeyEvent.VK_END,
			KeyEvent.VK_F2, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_F1, KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_UP, 0
		};
		
		private static final int[] KEY_LOC_MACINTOSH = new int[]{
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 0, 0, 0,
			0, 0, 0, 0,
			KeyEvent.KEY_LOCATION_STANDARD, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 0, 0, 0,
			KeyEvent.KEY_LOCATION_STANDARD, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 0, 0, 0,
			0, 0, 0, 0,
			KeyEvent.KEY_LOCATION_LEFT, 0, KeyEvent.KEY_LOCATION_LEFT, KeyEvent.KEY_LOCATION_LEFT,
			KeyEvent.KEY_LOCATION_RIGHT, KeyEvent.KEY_LOCATION_RIGHT, KeyEvent.KEY_LOCATION_RIGHT, 0,
			
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			KeyEvent.KEY_LOCATION_NUMPAD, 0, 0, 0,
			
			0, KeyEvent.KEY_LOCATION_NUMPAD, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0
		};
		
		private static final int[] KEY_MAP_JAVA = new int[]{
			KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3,
			KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7,
			KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_A, KeyEvent.VK_B,
			KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_F,
			
			KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J,
			KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N,
			KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R,
			KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_U, KeyEvent.VK_V,
			
			KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z,
			KeyEvent.VK_AMPERSAND, KeyEvent.VK_ASTERISK, KeyEvent.VK_AT, KeyEvent.VK_BACK_QUOTE,
			KeyEvent.VK_BACK_SLASH, KeyEvent.VK_BRACELEFT, KeyEvent.VK_BRACERIGHT, KeyEvent.VK_OPEN_BRACKET,
			KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_CIRCUMFLEX, KeyEvent.VK_COLON, KeyEvent.VK_COMMA,
			
			KeyEvent.VK_DOLLAR, KeyEvent.VK_EQUALS, KeyEvent.VK_EURO_SIGN, KeyEvent.VK_INVERTED_EXCLAMATION_MARK,
			KeyEvent.VK_EXCLAMATION_MARK, KeyEvent.VK_LESS, KeyEvent.VK_GREATER, KeyEvent.VK_MINUS,
			KeyEvent.VK_NUMBER_SIGN, KeyEvent.VK_LEFT_PARENTHESIS, KeyEvent.VK_RIGHT_PARENTHESIS, KeyEvent.VK_PERIOD,
			KeyEvent.VK_PLUS, KeyEvent.VK_QUOTE, KeyEvent.VK_QUOTEDBL, KeyEvent.VK_SEMICOLON,
			
			KeyEvent.VK_SLASH, KeyEvent.VK_UNDERSCORE, 0, KeyEvent.VK_SPACE,
			KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_TAB, KeyEvent.VK_ENTER,
			KeyEvent.VK_DELETE, KeyEvent.VK_INSERT, KeyEvent.VK_HOME, KeyEvent.VK_PAGE_UP,
			KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_END, KeyEvent.VK_PRINTSCREEN, KeyEvent.VK_PAUSE,
			
			KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_ALT, KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_CONTROL,
			KeyEvent.VK_META, KeyEvent.VK_NUM_LOCK, KeyEvent.VK_SCROLL_LOCK, KeyEvent.VK_SHIFT,
			KeyEvent.VK_KP_UP, KeyEvent.VK_KP_DOWN, KeyEvent.VK_KP_LEFT, KeyEvent.VK_KP_RIGHT,
			
			KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3,
			KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7,
			KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_DECIMAL, KeyEvent.VK_SEPARATOR,
			KeyEvent.VK_ADD, KeyEvent.VK_SUBTRACT, KeyEvent.VK_MULTIPLY, KeyEvent.VK_DIVIDE,
			
			KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4,
			KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8,
			KeyEvent.VK_F9, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12,
			KeyEvent.VK_F13, KeyEvent.VK_F14, KeyEvent.VK_F15, KeyEvent.VK_F16,
			
			KeyEvent.VK_F17, KeyEvent.VK_F18, KeyEvent.VK_F19, KeyEvent.VK_F20,
			KeyEvent.VK_F21, KeyEvent.VK_F22, KeyEvent.VK_F23, KeyEvent.VK_F24,
			KeyEvent.VK_HELP, KeyEvent.VK_UNDO, KeyEvent.VK_CUT, KeyEvent.VK_COPY,
			KeyEvent.VK_PASTE, KeyEvent.VK_CLEAR, KeyEvent.VK_FIND, KeyEvent.VK_AGAIN,
			
			KeyEvent.VK_DEAD_ABOVEDOT, KeyEvent.VK_DEAD_ABOVERING, KeyEvent.VK_DEAD_ACUTE, KeyEvent.VK_DEAD_BREVE,
			KeyEvent.VK_DEAD_CARON, KeyEvent.VK_DEAD_CEDILLA, KeyEvent.VK_DEAD_CIRCUMFLEX, KeyEvent.VK_DEAD_DIAERESIS,
			KeyEvent.VK_DEAD_DOUBLEACUTE, KeyEvent.VK_DEAD_GRAVE, KeyEvent.VK_DEAD_IOTA, KeyEvent.VK_DEAD_MACRON,
			KeyEvent.VK_DEAD_OGONEK, KeyEvent.VK_DEAD_TILDE, KeyEvent.VK_DEAD_SEMIVOICED_SOUND, KeyEvent.VK_DEAD_VOICED_SOUND,
			
			KeyEvent.VK_INPUT_METHOD_ON_OFF, KeyEvent.VK_ROMAN_CHARACTERS, KeyEvent.VK_FULL_WIDTH, KeyEvent.VK_HALF_WIDTH,
			KeyEvent.VK_KANA, KeyEvent.VK_KANA_LOCK, KeyEvent.VK_HIRAGANA, KeyEvent.VK_KATAKANA,
			KeyEvent.VK_KANJI, KeyEvent.VK_JAPANESE_ROMAN, KeyEvent.VK_JAPANESE_HIRAGANA, KeyEvent.VK_JAPANESE_KATAKANA,
			KeyEvent.VK_ALL_CANDIDATES, KeyEvent.VK_PREVIOUS_CANDIDATE, KeyEvent.VK_ACCEPT, KeyEvent.VK_MODECHANGE,
			
			KeyEvent.VK_CONVERT, KeyEvent.VK_NONCONVERT, KeyEvent.VK_FINAL, KeyEvent.VK_ALPHANUMERIC,
			KeyEvent.VK_CODE_INPUT,
			KeyEvent.VK_CANCEL, KeyEvent.VK_COMPOSE, KeyEvent.VK_STOP, KeyEvent.VK_PROPS
		};
		
		private String vkName(int vk) {
			switch (vk) {
			case KeyEvent.VK_0: return "VK_0";
			case KeyEvent.VK_1: return "VK_1";
			case KeyEvent.VK_2: return "VK_2";
			case KeyEvent.VK_3: return "VK_3";
			case KeyEvent.VK_4: return "VK_4";
			case KeyEvent.VK_5: return "VK_5";
			case KeyEvent.VK_6: return "VK_6";
			case KeyEvent.VK_7: return "VK_7";
			case KeyEvent.VK_8: return "VK_8";
			case KeyEvent.VK_9: return "VK_9";
			case KeyEvent.VK_A: return "VK_A";
			case KeyEvent.VK_ACCEPT: return "VK_ACCEPT";
			case KeyEvent.VK_ADD: return "VK_ADD";
			case KeyEvent.VK_AGAIN: return "VK_AGAIN";
			case KeyEvent.VK_ALL_CANDIDATES: return "VK_ALL_CANDIDATES";
			case KeyEvent.VK_ALPHANUMERIC: return "VK_ALPHANUMERIC";
			case KeyEvent.VK_ALT: return "VK_ALT";
			case KeyEvent.VK_ALT_GRAPH: return "VK_ALT_GRAPH";
			case KeyEvent.VK_AMPERSAND: return "VK_AMPERSAND";
			case KeyEvent.VK_ASTERISK: return "VK_ASTERISK";
			case KeyEvent.VK_AT: return "VK_AT";
			case KeyEvent.VK_B: return "VK_B";
			case KeyEvent.VK_BACK_QUOTE: return "VK_BACK_QUOTE";
			case KeyEvent.VK_BACK_SLASH: return "VK_BACK_SLASH";
			case KeyEvent.VK_BACK_SPACE: return "VK_BACK_SPACE";
			case KeyEvent.VK_BRACELEFT: return "VK_BRACELEFT";
			case KeyEvent.VK_BRACERIGHT: return "VK_BRACERIGHT";
			case KeyEvent.VK_C: return "VK_C";
			case KeyEvent.VK_CANCEL: return "VK_CANCEL";
			case KeyEvent.VK_CAPS_LOCK: return "VK_CAPS_LOCK";
			case KeyEvent.VK_CIRCUMFLEX: return "VK_CIRCUMFLEX";
			case KeyEvent.VK_CLEAR: return "VK_CLEAR";
			case KeyEvent.VK_CLOSE_BRACKET: return "VK_CLOSE_BRACKET";
			case KeyEvent.VK_CODE_INPUT: return "VK_CODE_INPUT";
			case KeyEvent.VK_COLON: return "VK_COLON";
			case KeyEvent.VK_COMMA: return "VK_COMMA";
			case KeyEvent.VK_COMPOSE: return "VK_COMPOSE";
			case KeyEvent.VK_CONTROL: return "VK_CONTROL";
			case KeyEvent.VK_CONVERT: return "VK_CONVERT";
			case KeyEvent.VK_COPY: return "VK_COPY";
			case KeyEvent.VK_CUT: return "VK_CUT";
			case KeyEvent.VK_D: return "VK_D";
			case KeyEvent.VK_DEAD_ABOVEDOT: return "VK_DEAD_ABOVEDOT";
			case KeyEvent.VK_DEAD_ABOVERING: return "VK_DEAD_ABOVERING";
			case KeyEvent.VK_DEAD_ACUTE: return "VK_DEAD_ACUTE";
			case KeyEvent.VK_DEAD_BREVE: return "VK_DEAD_BREVE";
			case KeyEvent.VK_DEAD_CARON: return "VK_DEAD_CARON";
			case KeyEvent.VK_DEAD_CEDILLA: return "VK_DEAD_CEDILLA";
			case KeyEvent.VK_DEAD_CIRCUMFLEX: return "VK_DEAD_CIRCUMFLEX";
			case KeyEvent.VK_DEAD_DIAERESIS: return "VK_DEAD_DIAERESIS";
			case KeyEvent.VK_DEAD_DOUBLEACUTE: return "VK_DEAD_DOUBLEACUTE";
			case KeyEvent.VK_DEAD_GRAVE: return "VK_DEAD_GRAVE";
			case KeyEvent.VK_DEAD_IOTA: return "VK_DEAD_IOTA";
			case KeyEvent.VK_DEAD_MACRON: return "VK_DEAD_MACRON";
			case KeyEvent.VK_DEAD_OGONEK: return "VK_DEAD_OGONEK";
			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND: return "VK_DEAD_SEMIVOICED_SOUND";
			case KeyEvent.VK_DEAD_TILDE: return "VK_DEAD_TILDE";
			case KeyEvent.VK_DEAD_VOICED_SOUND: return "VK_DEAD_VOICED_SOUND";
			case KeyEvent.VK_DECIMAL: return "VK_DECIMAL";
			case KeyEvent.VK_DELETE: return "VK_DELETE";
			case KeyEvent.VK_DIVIDE: return "VK_DIVIDE";
			case KeyEvent.VK_DOLLAR: return "VK_DOLLAR";
			case KeyEvent.VK_DOWN: return "VK_DOWN";
			case KeyEvent.VK_E: return "VK_E";
			case KeyEvent.VK_END: return "VK_END";
			case KeyEvent.VK_ENTER: return "VK_ENTER";
			case KeyEvent.VK_EQUALS: return "VK_EQUALS";
			case KeyEvent.VK_ESCAPE: return "VK_ESCAPE";
			case KeyEvent.VK_EURO_SIGN: return "VK_EURO_SIGN";
			case KeyEvent.VK_EXCLAMATION_MARK: return "VK_EXCLAMATION_MARK";
			case KeyEvent.VK_F: return "VK_F";
			case KeyEvent.VK_F1: return "VK_F1";
			case KeyEvent.VK_F10: return "VK_F10";
			case KeyEvent.VK_F11: return "VK_F11";
			case KeyEvent.VK_F12: return "VK_F12";
			case KeyEvent.VK_F13: return "VK_F13";
			case KeyEvent.VK_F14: return "VK_F14";
			case KeyEvent.VK_F15: return "VK_F15";
			case KeyEvent.VK_F16: return "VK_F16";
			case KeyEvent.VK_F17: return "VK_F17";
			case KeyEvent.VK_F18: return "VK_F18";
			case KeyEvent.VK_F19: return "VK_F19";
			case KeyEvent.VK_F2: return "VK_F2";
			case KeyEvent.VK_F20: return "VK_F20";
			case KeyEvent.VK_F21: return "VK_F21";
			case KeyEvent.VK_F22: return "VK_F22";
			case KeyEvent.VK_F23: return "VK_F23";
			case KeyEvent.VK_F24: return "VK_F24";
			case KeyEvent.VK_F3: return "VK_F3";
			case KeyEvent.VK_F4: return "VK_F4";
			case KeyEvent.VK_F5: return "VK_F5";
			case KeyEvent.VK_F6: return "VK_F6";
			case KeyEvent.VK_F7: return "VK_F7";
			case KeyEvent.VK_F8: return "VK_F8";
			case KeyEvent.VK_F9: return "VK_F9";
			case KeyEvent.VK_FINAL: return "VK_FINAL";
			case KeyEvent.VK_FIND: return "VK_FIND";
			case KeyEvent.VK_FULL_WIDTH: return "VK_FULL_WIDTH";
			case KeyEvent.VK_G: return "VK_G";
			case KeyEvent.VK_GREATER: return "VK_GREATER";
			case KeyEvent.VK_H: return "VK_H";
			case KeyEvent.VK_HALF_WIDTH: return "VK_HALF_WIDTH";
			case KeyEvent.VK_HELP: return "VK_HELP";
			case KeyEvent.VK_HIRAGANA: return "VK_HIRAGANA";
			case KeyEvent.VK_HOME: return "VK_HOME";
			case KeyEvent.VK_I: return "VK_I";
			case KeyEvent.VK_INPUT_METHOD_ON_OFF: return "VK_INPUT_METHOD_ON_OFF";
			case KeyEvent.VK_INSERT: return "VK_INSERT";
			case KeyEvent.VK_INVERTED_EXCLAMATION_MARK: return "VK_INVERTED_EXCLAMATION_MARK";
			case KeyEvent.VK_J: return "VK_J";
			case KeyEvent.VK_JAPANESE_HIRAGANA: return "VK_JAPANESE_HIRAGANA";
			case KeyEvent.VK_JAPANESE_KATAKANA: return "VK_JAPANESE_KATAKANA";
			case KeyEvent.VK_JAPANESE_ROMAN: return "VK_JAPANESE_ROMAN";
			case KeyEvent.VK_K: return "VK_K";
			case KeyEvent.VK_KANA: return "VK_KANA";
			case KeyEvent.VK_KANA_LOCK: return "VK_KANA_LOCK";
			case KeyEvent.VK_KANJI: return "VK_KANJI";
			case KeyEvent.VK_KATAKANA: return "VK_KATAKANA";
			case KeyEvent.VK_KP_DOWN: return "VK_KP_DOWN";
			case KeyEvent.VK_KP_LEFT: return "VK_KP_LEFT";
			case KeyEvent.VK_KP_RIGHT: return "VK_KP_RIGHT";
			case KeyEvent.VK_KP_UP: return "VK_KP_UP";
			case KeyEvent.VK_L: return "VK_L";
			case KeyEvent.VK_LEFT: return "VK_LEFT";
			case KeyEvent.VK_LEFT_PARENTHESIS: return "VK_LEFT_PARENTHESIS";
			case KeyEvent.VK_LESS: return "VK_LESS";
			case KeyEvent.VK_M: return "VK_M";
			case KeyEvent.VK_META: return "VK_META";
			case KeyEvent.VK_MINUS: return "VK_MINUS";
			case KeyEvent.VK_MODECHANGE: return "VK_MODECHANGE";
			case KeyEvent.VK_MULTIPLY: return "VK_MULTIPLY";
			case KeyEvent.VK_N: return "VK_N";
			case KeyEvent.VK_NONCONVERT: return "VK_NONCONVERT";
			case KeyEvent.VK_NUM_LOCK: return "VK_NUM_LOCK";
			case KeyEvent.VK_NUMBER_SIGN: return "VK_NUMBER_SIGN";
			case KeyEvent.VK_NUMPAD0: return "VK_NUMPAD0";
			case KeyEvent.VK_NUMPAD1: return "VK_NUMPAD1";
			case KeyEvent.VK_NUMPAD2: return "VK_NUMPAD2";
			case KeyEvent.VK_NUMPAD3: return "VK_NUMPAD3";
			case KeyEvent.VK_NUMPAD4: return "VK_NUMPAD4";
			case KeyEvent.VK_NUMPAD5: return "VK_NUMPAD5";
			case KeyEvent.VK_NUMPAD6: return "VK_NUMPAD6";
			case KeyEvent.VK_NUMPAD7: return "VK_NUMPAD7";
			case KeyEvent.VK_NUMPAD8: return "VK_NUMPAD8";
			case KeyEvent.VK_NUMPAD9: return "VK_NUMPAD9";
			case KeyEvent.VK_O: return "VK_O";
			case KeyEvent.VK_OPEN_BRACKET: return "VK_OPEN_BRACKET";
			case KeyEvent.VK_P: return "VK_P";
			case KeyEvent.VK_PAGE_DOWN: return "VK_PAGE_DOWN";
			case KeyEvent.VK_PAGE_UP: return "VK_PAGE_UP";
			case KeyEvent.VK_PASTE: return "VK_PASTE";
			case KeyEvent.VK_PAUSE: return "VK_PAUSE";
			case KeyEvent.VK_PERIOD: return "VK_PERIOD";
			case KeyEvent.VK_PLUS: return "VK_PLUS";
			case KeyEvent.VK_PREVIOUS_CANDIDATE: return "VK_PREVIOUS_CANDIDATE";
			case KeyEvent.VK_PRINTSCREEN: return "VK_PRINTSCREEN";
			case KeyEvent.VK_PROPS: return "VK_PROPS";
			case KeyEvent.VK_Q: return "VK_Q";
			case KeyEvent.VK_QUOTE: return "VK_QUOTE";
			case KeyEvent.VK_QUOTEDBL: return "VK_QUOTEDBL";
			case KeyEvent.VK_R: return "VK_R";
			case KeyEvent.VK_RIGHT: return "VK_RIGHT";
			case KeyEvent.VK_RIGHT_PARENTHESIS: return "VK_RIGHT_PARENTHESIS";
			case KeyEvent.VK_ROMAN_CHARACTERS: return "VK_ROMAN_CHARACTERS";
			case KeyEvent.VK_S: return "VK_S";
			case KeyEvent.VK_SCROLL_LOCK: return "VK_SCROLL_LOCK";
			case KeyEvent.VK_SEMICOLON: return "VK_SEMICOLON";
			case KeyEvent.VK_SEPARATOR: return "VK_SEPARATOR";
			case KeyEvent.VK_SHIFT: return "VK_SHIFT";
			case KeyEvent.VK_SLASH: return "VK_SLASH";
			case KeyEvent.VK_SPACE: return "VK_SPACE";
			case KeyEvent.VK_STOP: return "VK_STOP";
			case KeyEvent.VK_SUBTRACT: return "VK_SUBTRACT";
			case KeyEvent.VK_T: return "VK_T";
			case KeyEvent.VK_TAB: return "VK_TAB";
			case KeyEvent.VK_U: return "VK_U";
			case KeyEvent.VK_UNDEFINED: return "VK_UNDEFINED";
			case KeyEvent.VK_UNDERSCORE: return "VK_UNDERSCORE";
			case KeyEvent.VK_UNDO: return "VK_UNDO";
			case KeyEvent.VK_UP: return "VK_UP";
			case KeyEvent.VK_V: return "VK_V";
			case KeyEvent.VK_W: return "VK_W";
			case KeyEvent.VK_X: return "VK_X";
			case KeyEvent.VK_Y: return "VK_Y";
			case KeyEvent.VK_Z: return "VK_Z";
			default: return "";
			}
		}
		
		private String vkKeyCap(int vk) {
			switch (vk) {
			case KeyEvent.VK_0: return "0";
			case KeyEvent.VK_1: return "1";
			case KeyEvent.VK_2: return "2";
			case KeyEvent.VK_3: return "3";
			case KeyEvent.VK_4: return "4";
			case KeyEvent.VK_5: return "5";
			case KeyEvent.VK_6: return "6";
			case KeyEvent.VK_7: return "7";
			case KeyEvent.VK_8: return "8";
			case KeyEvent.VK_9: return "9";
			case KeyEvent.VK_A: return "A";
			case KeyEvent.VK_ACCEPT: return "Acc";
			case KeyEvent.VK_ADD: return "N+";
			case KeyEvent.VK_AGAIN: return "Again";
			case KeyEvent.VK_ALL_CANDIDATES: return "All";
			case KeyEvent.VK_ALPHANUMERIC: return "Alpha";
			case KeyEvent.VK_ALT: return "\u2325";
			case KeyEvent.VK_ALT_GRAPH: return "AltGr";
			case KeyEvent.VK_AMPERSAND: return "&";
			case KeyEvent.VK_ASTERISK: return "*";
			case KeyEvent.VK_AT: return "@";
			case KeyEvent.VK_B: return "B";
			case KeyEvent.VK_BACK_QUOTE: return "`";
			case KeyEvent.VK_BACK_SLASH: return "\\";
			case KeyEvent.VK_BACK_SPACE: return "\u232B";
			case KeyEvent.VK_BRACELEFT: return "{";
			case KeyEvent.VK_BRACERIGHT: return "}";
			case KeyEvent.VK_C: return "C";
			case KeyEvent.VK_CANCEL: return "Can";
			case KeyEvent.VK_CAPS_LOCK: return "\u21EA";
			case KeyEvent.VK_CIRCUMFLEX: return "^";
			case KeyEvent.VK_CLEAR: return "Clear";
			case KeyEvent.VK_CLOSE_BRACKET: return "]";
			case KeyEvent.VK_CODE_INPUT: return "Code";
			case KeyEvent.VK_COLON: return ":";
			case KeyEvent.VK_COMMA: return ",";
			case KeyEvent.VK_COMPOSE: return "Comp";
			case KeyEvent.VK_CONTROL: return "\u2303";
			case KeyEvent.VK_CONVERT: return "Cvt";
			case KeyEvent.VK_COPY: return "Copy";
			case KeyEvent.VK_CUT: return "Cut";
			case KeyEvent.VK_D: return "D";
			case KeyEvent.VK_DEAD_ABOVEDOT: return "\u02D9";
			case KeyEvent.VK_DEAD_ABOVERING: return "\u02DA";
			case KeyEvent.VK_DEAD_ACUTE: return "\u02CA";
			case KeyEvent.VK_DEAD_BREVE: return "\u02D8";
			case KeyEvent.VK_DEAD_CARON: return "\u02C7";
			case KeyEvent.VK_DEAD_CEDILLA: return "\u00B8";
			case KeyEvent.VK_DEAD_CIRCUMFLEX: return "\u02C6";
			case KeyEvent.VK_DEAD_DIAERESIS: return "\u00A8";
			case KeyEvent.VK_DEAD_DOUBLEACUTE: return "\u02DD";
			case KeyEvent.VK_DEAD_GRAVE: return "\u02CB";
			case KeyEvent.VK_DEAD_IOTA: return "\u037A";
			case KeyEvent.VK_DEAD_MACRON: return "\u02C9";
			case KeyEvent.VK_DEAD_OGONEK: return "\u02DB";
			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND: return "\u309C";
			case KeyEvent.VK_DEAD_TILDE: return "\u02DC";
			case KeyEvent.VK_DEAD_VOICED_SOUND: return "\u309B";
			case KeyEvent.VK_DECIMAL: return "N.";
			case KeyEvent.VK_DELETE: return "\u2326";
			case KeyEvent.VK_DIVIDE: return "N/";
			case KeyEvent.VK_DOLLAR: return "$";
			case KeyEvent.VK_DOWN: return "\u2193";
			case KeyEvent.VK_E: return "E";
			case KeyEvent.VK_END: return "\u21F2";
			case KeyEvent.VK_ENTER: return "\u2324";
			case KeyEvent.VK_EQUALS: return "=";
			case KeyEvent.VK_ESCAPE: return "\u238B";
			case KeyEvent.VK_EURO_SIGN: return "\u20AC";
			case KeyEvent.VK_EXCLAMATION_MARK: return "!";
			case KeyEvent.VK_F: return "F";
			case KeyEvent.VK_F1: return "F1";
			case KeyEvent.VK_F10: return "F10";
			case KeyEvent.VK_F11: return "F11";
			case KeyEvent.VK_F12: return "F12";
			case KeyEvent.VK_F13: return "F13";
			case KeyEvent.VK_F14: return "F14";
			case KeyEvent.VK_F15: return "F15";
			case KeyEvent.VK_F16: return "F16";
			case KeyEvent.VK_F17: return "F17";
			case KeyEvent.VK_F18: return "F18";
			case KeyEvent.VK_F19: return "F19";
			case KeyEvent.VK_F2: return "F2";
			case KeyEvent.VK_F20: return "F20";
			case KeyEvent.VK_F21: return "F21";
			case KeyEvent.VK_F22: return "F22";
			case KeyEvent.VK_F23: return "F23";
			case KeyEvent.VK_F24: return "F24";
			case KeyEvent.VK_F3: return "F3";
			case KeyEvent.VK_F4: return "F4";
			case KeyEvent.VK_F5: return "F5";
			case KeyEvent.VK_F6: return "F6";
			case KeyEvent.VK_F7: return "F7";
			case KeyEvent.VK_F8: return "F8";
			case KeyEvent.VK_F9: return "F9";
			case KeyEvent.VK_FINAL: return "Final";
			case KeyEvent.VK_FIND: return "Find";
			case KeyEvent.VK_FULL_WIDTH: return "FW";
			case KeyEvent.VK_G: return "G";
			case KeyEvent.VK_GREATER: return ">";
			case KeyEvent.VK_H: return "H";
			case KeyEvent.VK_HALF_WIDTH: return "HW";
			case KeyEvent.VK_HELP: return "Help";
			case KeyEvent.VK_HIRAGANA: return "Hira";
			case KeyEvent.VK_HOME: return "\u21F1";
			case KeyEvent.VK_I: return "I";
			case KeyEvent.VK_INPUT_METHOD_ON_OFF: return "IM";
			case KeyEvent.VK_INSERT: return "\u2386";
			case KeyEvent.VK_INVERTED_EXCLAMATION_MARK: return "\u00A1";
			case KeyEvent.VK_J: return "J";
			case KeyEvent.VK_JAPANESE_HIRAGANA: return "JHira";
			case KeyEvent.VK_JAPANESE_KATAKANA: return "JKata";
			case KeyEvent.VK_JAPANESE_ROMAN: return "JRom";
			case KeyEvent.VK_K: return "K";
			case KeyEvent.VK_KANA: return "Kana";
			case KeyEvent.VK_KANA_LOCK: return "KanaLk";
			case KeyEvent.VK_KANJI: return "Kanji";
			case KeyEvent.VK_KATAKANA: return "Kata";
			case KeyEvent.VK_KP_DOWN: return "N\u2193";
			case KeyEvent.VK_KP_LEFT: return "N\u2190";
			case KeyEvent.VK_KP_RIGHT: return "N\u2192";
			case KeyEvent.VK_KP_UP: return "N\u2191";
			case KeyEvent.VK_L: return "L";
			case KeyEvent.VK_LEFT: return "\u2190";
			case KeyEvent.VK_LEFT_PARENTHESIS: return "(";
			case KeyEvent.VK_LESS: return "<";
			case KeyEvent.VK_M: return "M";
			case KeyEvent.VK_META: return "\u2318";
			case KeyEvent.VK_MINUS: return "-";
			case KeyEvent.VK_MODECHANGE: return "Mode";
			case KeyEvent.VK_MULTIPLY: return "N*";
			case KeyEvent.VK_N: return "N";
			case KeyEvent.VK_NONCONVERT: return "Ncvt";
			case KeyEvent.VK_NUM_LOCK: return "NumLk";
			case KeyEvent.VK_NUMBER_SIGN: return "#";
			case KeyEvent.VK_NUMPAD0: return "N0";
			case KeyEvent.VK_NUMPAD1: return "N1";
			case KeyEvent.VK_NUMPAD2: return "N2";
			case KeyEvent.VK_NUMPAD3: return "N3";
			case KeyEvent.VK_NUMPAD4: return "N4";
			case KeyEvent.VK_NUMPAD5: return "N5";
			case KeyEvent.VK_NUMPAD6: return "N6";
			case KeyEvent.VK_NUMPAD7: return "N7";
			case KeyEvent.VK_NUMPAD8: return "N8";
			case KeyEvent.VK_NUMPAD9: return "N9";
			case KeyEvent.VK_O: return "O";
			case KeyEvent.VK_OPEN_BRACKET: return "[";
			case KeyEvent.VK_P: return "P";
			case KeyEvent.VK_PAGE_DOWN: return "\u21DF";
			case KeyEvent.VK_PAGE_UP: return "\u21DE";
			case KeyEvent.VK_PASTE: return "Paste";
			case KeyEvent.VK_PAUSE: return "Pause";
			case KeyEvent.VK_PERIOD: return ".";
			case KeyEvent.VK_PLUS: return "+";
			case KeyEvent.VK_PREVIOUS_CANDIDATE: return "Prev";
			case KeyEvent.VK_PRINTSCREEN: return "PrtSc";
			case KeyEvent.VK_PROPS: return "Props";
			case KeyEvent.VK_Q: return "Q";
			case KeyEvent.VK_QUOTE: return "\'";
			case KeyEvent.VK_QUOTEDBL: return "\"";
			case KeyEvent.VK_R: return "R";
			case KeyEvent.VK_RIGHT: return "\u2192";
			case KeyEvent.VK_RIGHT_PARENTHESIS: return ")";
			case KeyEvent.VK_ROMAN_CHARACTERS: return "Rom";
			case KeyEvent.VK_S: return "S";
			case KeyEvent.VK_SCROLL_LOCK: return "ScrLk";
			case KeyEvent.VK_SEMICOLON: return ";";
			case KeyEvent.VK_SEPARATOR: return "N,";
			case KeyEvent.VK_SHIFT: return "\u21E7";
			case KeyEvent.VK_SLASH: return "/";
			case KeyEvent.VK_SPACE: return "\u2423";
			case KeyEvent.VK_STOP: return "Stop";
			case KeyEvent.VK_SUBTRACT: return "N-";
			case KeyEvent.VK_T: return "T";
			case KeyEvent.VK_TAB: return "\u21E5";
			case KeyEvent.VK_U: return "U";
			case KeyEvent.VK_UNDERSCORE: return "_";
			case KeyEvent.VK_UNDO: return "Undo";
			case KeyEvent.VK_UP: return "\u2191";
			case KeyEvent.VK_V: return "V";
			case KeyEvent.VK_W: return "W";
			case KeyEvent.VK_X: return "X";
			case KeyEvent.VK_Y: return "Y";
			case KeyEvent.VK_Z: return "Z";
			default: return "";
			}
		}
		
		private String macKeyCap(int i) {
			switch (i) {
			case 10: return "\u00A7";
			case 36: return "\u21A9";
			case 63: return "fn";
			case 71: return "N\u238B";
			case 81: return "N=";
			case 114: return "?\u20DD";
			default: return vkKeyCap(KEY_MAP_MACINTOSH[i]);
			}
		}
		
		private JLabel[] macLabels;
		private JLabel[] javaLabels;
		private JPanel macChart;
		private JPanel javaChart;
		
		public WKeyCodeChart() {
			super("Key Codes");
			ResplMain.registerWindow(this);
			
			macChart = new JPanel(new FractionalSizeGridLayout(0,16,-1,-1));
			macChart.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
			macLabels = new JLabel[KEY_MAP_MACINTOSH.length];
			for (int i=0; i<macLabels.length; i++) {
				macLabels[i] = new JLabel(macKeyCap(i));
				macLabels[i].setToolTipText("0x"+Integer.toHexString(i|256).toUpperCase().substring(1)+"   "+Integer.toString(i)+"   "+vkName(KEY_MAP_MACINTOSH[i]));
				//macLabels[i].setFont(macLabels[i].getFont().deriveFont(10.0f));
				macLabels[i].setOpaque(true);
				macLabels[i].setBackground(Color.white);
				macLabels[i].setForeground(Color.black);
				macLabels[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
				macLabels[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
				macLabels[i].setAlignmentY(JLabel.CENTER_ALIGNMENT);
				macLabels[i].setHorizontalAlignment(JLabel.CENTER);
				macLabels[i].setVerticalAlignment(JLabel.CENTER);
				macLabels[i].setHorizontalTextPosition(JLabel.CENTER);
				macLabels[i].setVerticalTextPosition(JLabel.CENTER);
				macChart.add(macLabels[i]);
			}
			javaChart = new JPanel(new FractionalSizeGridLayout(0,16,-1,-1));
			javaChart.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
			javaLabels = new JLabel[KEY_MAP_JAVA.length];
			for (int i=0; i<javaLabels.length; i++) {
				javaLabels[i] = new JLabel(vkKeyCap(KEY_MAP_JAVA[i]));
				javaLabels[i].setToolTipText("0x"+Integer.toHexString(KEY_MAP_JAVA[i]|65536).toUpperCase().substring(1)+"   "+Integer.toString(KEY_MAP_JAVA[i])+"   "+vkName(KEY_MAP_JAVA[i]));
				if (javaLabels[i].getText().length()>2)
					javaLabels[i].setFont(javaLabels[i].getFont().deriveFont(10.0f));
				javaLabels[i].setOpaque(true);
				javaLabels[i].setBackground(Color.white);
				javaLabels[i].setForeground(Color.black);
				javaLabels[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
				javaLabels[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
				javaLabels[i].setAlignmentY(JLabel.CENTER_ALIGNMENT);
				javaLabels[i].setHorizontalAlignment(JLabel.CENTER);
				javaLabels[i].setVerticalAlignment(JLabel.CENTER);
				javaLabels[i].setHorizontalTextPosition(JLabel.CENTER);
				javaLabels[i].setVerticalTextPosition(JLabel.CENTER);
				javaChart.add(javaLabels[i]);
			}
			
			final JPanel center = new JPanel(new CardLayout());
			center.add(javaChart, "java");
			center.add(macChart, "mac");
			final JPanel header = new JPanel();
			header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
			header.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
			ButtonGroup bg = new ButtonGroup();
			JRadioButton jBtn = new JRadioButton("Java");
			jBtn.setSelected(true);
			jBtn.setFocusable(false);
			jBtn.setRequestFocusEnabled(false);
			jBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CardLayout l = (CardLayout)(center.getLayout());
					l.show(center, "java");
				}
			});
			bg.add(jBtn);
			header.add(jBtn);
			header.add(Box.createHorizontalStrut(10));
			JRadioButton mBtn = new JRadioButton("Mac OS");
			mBtn.setSelected(false);
			mBtn.setFocusable(false);
			mBtn.setRequestFocusEnabled(false);
			mBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CardLayout l = (CardLayout)(center.getLayout());
					l.show(center, "mac");
				}
			});
			bg.add(mBtn);
			header.add(mBtn);
			final JPanel main = new JPanel(new BorderLayout());
			main.add(header, BorderLayout.PAGE_START);
			main.add(center, BorderLayout.CENTER);
			setContentPane(main);
			
			getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pack();
			setSize(new Dimension(520,300));
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		public void dispose() {
			getToolkit().removeAWTEventListener(this);
			super.dispose();
		}

		public void eventDispatched(AWTEvent event) {
			if (event instanceof KeyEvent) {
				KeyEvent ke = (KeyEvent)event;
				if (ke.getID() == KeyEvent.KEY_PRESSED) {
					int vk = ke.getKeyCode();
					int vl = ke.getKeyLocation();
					for (int i=0; i<macLabels.length; i++) {
						if (KEY_MAP_MACINTOSH[i] == vk && (KEY_LOC_MACINTOSH[i] == 0 || KEY_LOC_MACINTOSH[i] == vl)) {
							macLabels[i].setBackground(SystemColor.textHighlight);
							macLabels[i].setForeground(SystemColor.textHighlightText);
						}
					}
					for (int i=0; i<javaLabels.length; i++) {
						if (KEY_MAP_JAVA[i] == vk) {
							javaLabels[i].setBackground(SystemColor.textHighlight);
							javaLabels[i].setForeground(SystemColor.textHighlightText);
						}
					}
				} else if (ke.getID() == KeyEvent.KEY_RELEASED) {
					int vk = ke.getKeyCode();
					int vl = ke.getKeyLocation();
					for (int i=0; i<macLabels.length; i++) {
						if (KEY_MAP_MACINTOSH[i] == vk && (KEY_LOC_MACINTOSH[i] == 0 || KEY_LOC_MACINTOSH[i] == vl)) {
							macLabels[i].setBackground(Color.white);
							macLabels[i].setForeground(Color.black);
						}
					}
					for (int i=0; i<javaLabels.length; i++) {
						if (KEY_MAP_JAVA[i] == vk) {
							javaLabels[i].setBackground(Color.white);
							javaLabels[i].setForeground(Color.black);
						}
					}
				}
			}
		}
	}
}
