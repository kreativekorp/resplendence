package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

public class EncodeDecodeBase64 implements DataFilter {
	private static final byte[] b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
	
	public String category(int i) {
		return (i==0)?"Encode":"Decode";
	}
	
	private boolean b64v(byte b) {
		return ((b >= 'A' && b <= 'Z') || (b >= 'a' && b <= 'z') || (b >= '0' && b <= '9') || (b == '+') || (b == '/') || (b == '='));
	}
	
	private byte b64r(byte b) {
		if (b >= 'A' && b <= 'Z') return (byte)(b-'A');
		else if (b >= 'a' && b <= 'z') return (byte)(b-'a'+26);
		else if (b >= '0' && b <= '9') return (byte)(b-'0'+52);
		else switch (b) {
		case '+': return 62;
		case '/': return 63;
		case '=': return 0;
		default: return -1;
		}
	}

	public byte[] filter(int i, byte[] b) {
		if (i == 0) {
			byte[] r = new byte[4*((b.length+2)/3)];
			for (int c = 0, d = 0; c < b.length; c += 3) {
				int                 n  = (b[c  ]&0xFF) << 16;
				if (c+1 < b.length) n |= (b[c+1]&0xFF) <<  8;
				if (c+2 < b.length) n |= (b[c+2]&0xFF)      ;
				r[d++] = b64[(n>>>18)&0x3F];
				r[d++] = b64[(n>>>12)&0x3F];
				r[d++] = b64[(n>>> 6)&0x3F];
				r[d++] = b64[ n      &0x3F];
			}
			if ((b.length % 3) > 0)
				for (int c = b.length % 3, d = r.length; c < 3; c++) r[--d] = '=';
			return r;
		} else {
			byte[] bb = new byte[b.length];
			int bc = 0;
			for (int bi=0; bi<b.length; bi++) {
				if (b64v(b[bi])) bb[bc++]=b[bi];
			}
			byte[] r = new byte[3*((bc+3)/4)-((bc>0 && bb[bc-1]=='=')?(bc>1 && bb[bc-2]=='=')?2:1:0)];
			for (int c = 0, d = 0; c < bc; c += 4) {
				int           n  = b64r(bb[c  ])<<18;
				if (c+1 < bc) n |= b64r(bb[c+1])<<12;
				if (c+2 < bc) n |= b64r(bb[c+2])<< 6;
				if (c+3 < bc) n |= b64r(bb[c+3])    ;
				if (d < r.length) r[d++] = (byte)(n >>> 16);
				if (d < r.length) r[d++] = (byte)(n >>>  8);
				if (d < r.length) r[d++] = (byte)(n       );
			}
			return r;
		}
	}

	public String name(int i) {
		return "Base64";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 2;
	}
}
