package com.kreative.resplendence.template;

public class Position {
	private long bitpos;
	
	public Position() {
		this.bitpos = 0;
	}
	
	public Position(int bytepos) {
		this.bitpos = (long)bytepos << 3L;
	}
	
	public Position(long bitpos) {
		this.bitpos = bitpos;
	}
	
	public Position(int bytepos, int bitpos) {
		this.bitpos = ((long)bytepos << 3L) + (long)bitpos;
	}
	
	public long bytepos() {
		return (bitpos >> 3L);
	}
	
	public int bitpos() {
		return (int)(bitpos & 7L);
	}
	
	public long bytelength() {
		if ((bitpos & 7L) == 0) {
			return (bitpos >> 3L);
		} else {
			return (bitpos >> 3L)+1;
		}
	}
	
	public long bitlength() {
		return bitpos;
	}
	
	public Position skipBytes(int numbytes) {
		return new Position(bitpos + ((long)numbytes << 3L));
	}
	
	public Position skipBits(int numbits) {
		return new Position(bitpos + (long)numbits);
	}
	
	public Position skip(int numbytes, int numbits) {
		return new Position(bitpos + ((long)numbytes << 3L) + (long)numbits);
	}
	
	public Position alignToNibble() {
		if ((bitpos % 4L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 4L) + 4L);
		}
	}
	
	public Position alignToByte() {
		if ((bitpos % 8L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 8L) + 8L);
		}
	}
	
	public Position alignToShort() {
		if ((bitpos % 16L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 16L) + 16L);
		}
	}
	
	public Position alignToThreeInt() {
		if ((bitpos % 24L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 24L) + 24L);
		}
	}
	
	public Position alignToInt() {
		if ((bitpos % 32L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 32L) + 32L);
		}
	}
	
	public Position alignToSixInt() {
		if ((bitpos % 48L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 48L) + 48L);
		}
	}
	
	public Position alignToLong() {
		if ((bitpos % 64L) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % 64L) + 64L);
		}
	}
	
	public Position alignToByteMultiple(int m) {
		long mod = (long)m << 3L;
		if ((bitpos % mod) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % mod) + mod);
		}
	}
	
	public Position alignToBitMultiple(int m) {
		long mod = (long)m;
		if ((bitpos % mod) == 0) {
			return this;
		} else {
			return new Position(bitpos - (bitpos % mod) + mod);
		}
	}
	
	public Position getBits(byte[] src, boolean[] dest) {
		return getBits(src, dest, 0, dest.length);
	}
	
	public Position getBits(byte[] src, boolean[] dest, int dstart, int dlength) {
		int mask[] = new int[]{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
		int byteidx = (int)(bitpos >> 3L);
		int bitidx = (int)(bitpos & 7L);
		int cnt = 0;
		while (byteidx < src.length && dstart < dest.length && cnt < dlength) {
			dest[dstart++] = ((src[byteidx] & mask[bitidx]) != 0);
			bitidx++;
			if (bitidx >= 8) {
				bitidx = 0;
				byteidx++;
			}
			cnt++;
		}
		while (dstart < dest.length && cnt < dlength) {
			dest[dstart++] = false;
			cnt++;
		}
		return new Position(bitpos + (long)cnt);
	}
	
	public Position setBits(byte[] dest, boolean[] src) {
		return setBits(dest, src, 0, src.length);
	}
	
	public Position setBits(byte[] dest, boolean[] src, int sstart, int slength) {
		int mask[] = new int[]{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
		int byteidx = (int)(bitpos >> 3L);
		int bitidx = (int)(bitpos & 7L);
		int cnt = 0;
		while (byteidx < dest.length && sstart < src.length && cnt < slength) {
			if (src[sstart++]) {
				dest[byteidx] |= (byte)mask[bitidx];
			} else {
				dest[byteidx] &= (byte)(~mask[bitidx]);
			}
			bitidx++;
			if (bitidx >= 8) {
				bitidx = 0;
				byteidx++;
			}
			cnt++;
		}
		return new Position(bitpos + (long)cnt);
	}
	
	public Position getBytes(byte[] src, byte[] dest) {
		return getBytes(src, dest, 0, dest.length);
	}
	
	public Position getBytes(byte[] src, byte[] dest, int dstart, int dlength) {
		if ((bitpos % 8L) == 0) {
			//the easy part
			int sstart = (int)(bitpos >> 3L);
			int cnt = 0;
			while (sstart < src.length && dstart < dest.length && cnt < dlength) {
				dest[dstart++] = src[sstart++];
				cnt++;
			}
			while (dstart < dest.length && cnt < dlength) {
				dest[dstart++] = 0;
				cnt++;
			}
			return new Position(bitpos + (((long)cnt) << 3L));
		} else {
			//the hard part
			int sstart = (int)(bitpos >> 3L);
			int shift = 8 - (int)(bitpos & 7L);
			int cnt = 0;
			while (sstart+1 < src.length && dstart < dest.length && cnt < dlength) {
				dest[dstart++] = (byte)((((src[sstart]&0xFF) << 8) | (src[sstart+1]&0xFF)) >>> shift);
				sstart++;
				cnt++;
			}
			while (sstart < src.length && dstart < dest.length && cnt < dlength) {
				dest[dstart++] = (byte)(((src[sstart]&0xFF) << 8) >>> shift);
				sstart++;
				cnt++;
			}
			while (dstart < dest.length && cnt < dlength) {
				dest[dstart++] = 0;
				cnt++;
			}
			return new Position(bitpos + (((long)cnt) << 3L));
		}
	}
	
	public Position setBytes(byte[] dest, byte[] src) {
		return setBytes(dest, src, 0, src.length);
	}
	
	public Position setBytes(byte[] dest, byte[] src, int sstart, int slength) {
		if ((bitpos % 8L) == 0) {
			//the easy part
			int dstart = (int)(bitpos >> 3L);
			int cnt = 0;
			while (sstart < src.length && dstart < dest.length && cnt < slength) {
				dest[dstart++] = src[sstart++];
				cnt++;
			}
			return new Position(bitpos + (((long)cnt) << 3L));
		} else {
			//the hard part
			int dstart = (int)(bitpos >> 3L);
			int shift = (int)(bitpos & 7L);
			int cnt = 0;
			if (dstart < dest.length && sstart < src.length && cnt < slength) {
				dest[dstart] &= (byte)(0xFF << (8-shift));
				dest[dstart] |= (byte)((src[sstart]&0xFF) >>> shift);
				dstart++;
			}
			while (dstart < dest.length && sstart+1 < src.length && cnt < slength) {
				dest[dstart++] = (byte)((((src[sstart]&0xFF) << 8) | (src[sstart+1]&0xFF)) >>> shift);
				sstart++;
				cnt++;
			}
			while (dstart < dest.length && sstart < src.length && cnt < slength) {
				dest[dstart] &= (byte)(0xFF >>> shift);
				dest[dstart] |= (byte)((src[sstart]&0xFF) << (8-shift));
				dstart++;
				sstart++;
				cnt++;
			}
			return new Position(bitpos + (((long)cnt) << 3L));
		}
	}
}
