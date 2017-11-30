package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

public class EncodeDecodeHex implements DataFilter {
	private static final byte[] a = "0123456789ABCDEF".getBytes();
	
	public String category(int i) {
		return (i==0)?"Encode":"Decode";
	}

	public byte[] filter(int i, byte[] b) {
		if (i==0) {
			byte[] r = new byte[b.length<<1];
			for (int c=0; c<b.length; c++) {
				r[(c<<1)  ] = a[(b[c]>>>4)&0xF];
				r[(c<<1)|1] = a[(b[c]    )&0xF];
			}
			return r;
		} else {
			byte[] bb = new byte[b.length];
			int bc = 0;
			for (int bi=0; bi<b.length; bi++) {
				if ((b[bi] >= '0' && b[bi] <= '9')
						|| (b[bi] >= 'A' && b[bi] <= 'F')
						|| (b[bi] >= 'a' && b[bi] <= 'f')) bb[bc++]=b[bi];
			}
			byte[] r = new byte[(bc+1)>>1];
			for (int c=0; c<bc; c+=2) {
				r[c>>1] = (byte)((
						(bb[c] >= '0' && bb[c] <= '9')?(bb[c]-'0'):
							(bb[c] >= 'A' && bb[c] <= 'F')?(bb[c]-'A'+10):
								(bb[c] >= 'a' && bb[c] <= 'f')?(bb[c]-'a'+10):0
				) << 4);
				if (c+1 < bc)
					r[c>>1] |= (byte)((
							(bb[c+1] >= '0' && bb[c+1] <= '9')?(bb[c+1]-'0'):
								(bb[c+1] >= 'A' && bb[c+1] <= 'F')?(bb[c+1]-'A'+10):
									(bb[c+1] >= 'a' && bb[c+1] <= 'f')?(bb[c+1]-'a'+10):0
					) & 0xF);
			}
			return r;
		}
	}

	public String name(int i) {
		return "Hex";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 2;
	}
}
