package com.kreative.resplendence.datafilter;

import javax.swing.KeyStroke;

public class EncodeDecodeASCII85 implements DataFilter {
	public String category(int i) {
		return (i==0)?"Encode":"Decode";
	}
	
	public byte[] filter(int w, byte[] b) {
		if (w == 0) {
			byte[] r = new byte[(b.length*5)/4+10];
			int rp = 0;
			r[rp++] = '<';
			r[rp++] = '~';
			for (int i=0, n=b.length; i<b.length; i+=4, n-=4) {
				long d = 0;
				d <<= 8; if (n>0) d |= (b[i+0] & 0xFF);
				d <<= 8; if (n>1) d |= (b[i+1] & 0xFF);
				d <<= 8; if (n>2) d |= (b[i+2] & 0xFF);
				d <<= 8; if (n>3) d |= (b[i+3] & 0xFF);
				if (n>=4 && d==0) r[rp++] = 'z';
				else {
					int b5 = (int)(d%85); d /= 85;
					int b4 = (int)(d%85); d /= 85;
					int b3 = (int)(d%85); d /= 85;
					int b2 = (int)(d%85); d /= 85;
					int b1 = (int)(d%85); d /= 85;
					r[rp++] = (byte)('!'+b1);
					if (n>0) r[rp++] = (byte)('!'+b2);
					if (n>1) r[rp++] = (byte)('!'+b3);
					if (n>2) r[rp++] = (byte)('!'+b4);
					if (n>3) r[rp++] = (byte)('!'+b5);
				}
			}
			r[rp++] = '~';
			r[rp++] = '>';
			byte[] rr = new byte[rp];
			while((rp--)>0) rr[rp]=r[rp];
			return rr;
		} else {
			try {
				byte[] r = new byte[b.length*4];
				int rp = 0;
				int bp = 0;
				while (b[bp]<33 || b[bp]>126) bp++;
				if (b[bp]=='<' && b[bp+1]=='~') bp+=2;
				int d=0; int n=0;
				for (; bp<b.length; bp++) {
					if (b[bp] == '~') break;
					else if (b[bp] == 'z') {
						r[rp++]=0; r[rp++]=0; r[rp++]=0; r[rp++]=0;
					}
					else if (b[bp]>32 && b[bp]<127){
						d *= 85;
						d += b[bp]-'!';
						n++;
						if (n==5) {
							r[rp++] = (byte)(d >>> 24);
							r[rp++] = (byte)(d >>> 16);
							r[rp++] = (byte)(d >>>  8);
							r[rp++] = (byte)(d >>>  0);
							d=0; n=0;
						}
					}
				}
				if (n>0) {
					d*=85; d+=85;
					for (int m=n+1; m<5; m++) d*=85;
					if (n>1) r[rp++] = (byte)(d >>> 24);
					if (n>2) r[rp++] = (byte)(d >>> 16);
					if (n>3) r[rp++] = (byte)(d >>>  8);
					if (n>4) r[rp++] = (byte)(d >>>  0);
				}
				byte[] rr = new byte[rp];
				while((rp--)>0) rr[rp]=r[rp];
				return rr;
			} catch (ArrayIndexOutOfBoundsException e) {
				return b;
			}
		}
	}

	public String name(int i) {
		return "ASCII85";
	}
	
	public KeyStroke keystroke(int i) {
		return null;
	}
	
	public int numberOfFilters() {
		return 2;
	}
}
