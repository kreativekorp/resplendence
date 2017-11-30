package com.kreative.resplendence;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.kreative.util.*;

public class AttributeFile extends File implements Map<Integer,Object> {
	private static final long serialVersionUID = 1L;
	
	private static final short TYPE_BOOLEAN = 0;
	private static final short TYPE_BYTE = 1;
	private static final short TYPE_SHORT = 2;
	private static final short TYPE_INT = 3;
	private static final short TYPE_LONG = 4;
	private static final short TYPE_FLOAT = 5;
	private static final short TYPE_DOUBLE = 6;
	private static final short TYPE_CHAR = 7;
	private static final short TYPE_STRING = 8;
	private static final short TYPE_COLOR = 12;
	private static final short TYPE_POINT = 13;
	private static final short TYPE_RECTANGLE = 14;
	private static final short TYPE_DATE = 15;
	private static final short TYPE_OBJECT = Short.MAX_VALUE;
	
	private static final short TYPE_BOOLEAN_ARRAY = -1;
	private static final short TYPE_BYTE_ARRAY = -2;
	private static final short TYPE_SHORT_ARRAY = -3;
	private static final short TYPE_INT_ARRAY = -4;
	private static final short TYPE_LONG_ARRAY = -5;
	private static final short TYPE_FLOAT_ARRAY = -6;
	private static final short TYPE_DOUBLE_ARRAY = -7;
	private static final short TYPE_CHAR_ARRAY = -8;
	private static final short TYPE_STRING_ARRAY = -9;
	private static final short TYPE_COLOR_ARRAY = -13;
	private static final short TYPE_POINT_ARRAY = -14;
	private static final short TYPE_RECTANGLE_ARRAY = -15;
	private static final short TYPE_DATE_ARRAY = -16;
	private static final short TYPE_OBJECT_ARRAY = Short.MIN_VALUE;
	
	public AttributeFile(File f) {
		super(f.getAbsolutePath());
	}

	public AttributeFile(File parent, String child) {
		super(parent, child);
	}
	
	public AttributeFile(String pathname) {
		super(pathname);
	}
	
	public AttributeFile(String parent, String child) {
		super(parent, child);
	}
	
	public AttributeFile(URI uri) {
		super(uri);
	}
	
	private class Location {
		public RandomAccessFile raf;
		public long p;
		public int l;
		public int t;
		//public int k;
		public long p2;
		public int l2;
		public Location(RandomAccessFile raf, long p, int l, int t, int k, long p2, int l2) {
			this.raf = raf; this.p = p; this.l = l; this.t = t; /*this.k = k;*/ this.p2 = p2; this.l2 = l2;
		}
	}
	
	private Location locate(int key, String mode) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(this, mode);
			long p; long fl = raf.length();
			while ((p = raf.getFilePointer()) < fl) {
				int l = raf.readShort();
				int t = raf.readShort();
				int k = raf.readInt();
				if (k == key) {
					raf.close();
					return new Location(raf, p, l, t, k, p+8l, l-8);
				} else {
					raf.skipBytes(l-8);
				}
			}
			raf.close();
			return null;
		} catch (IOException ioe) {
			try { if (raf != null) raf.close(); }
			catch (Exception e) {}
			return null;
		}
	}
	
	private Object read(Location l) {
		if (l == null) return null;
		else try {
			l.raf.seek(l.p2);
			switch (l.t) {
			case TYPE_BOOLEAN:
				return l.raf.readBoolean();
			case TYPE_BYTE:
				return l.raf.readByte();
			case TYPE_SHORT:
				return l.raf.readShort();
			case TYPE_INT:
				return l.raf.readInt();
			case TYPE_LONG:
				return l.raf.readLong();
			case TYPE_FLOAT:
				return l.raf.readFloat();
			case TYPE_DOUBLE:
				return l.raf.readDouble();
			case TYPE_CHAR:
				return l.raf.readChar();
			case TYPE_STRING:
				byte[] st = new byte[l.l2]; l.raf.read(st);
				try {
					return new String(st, "UTF-8");
				} catch (UnsupportedEncodingException uee) {
					return new String(st);
				}
			case TYPE_COLOR:
				return new Color(l.raf.readInt(), true);
			case TYPE_POINT:
				int px = l.raf.readInt();
				int py = l.raf.readInt();
				return new Point(px, py);
			case TYPE_RECTANGLE:
				int rx = l.raf.readInt();
				int ry = l.raf.readInt();
				int rw = l.raf.readInt();
				int rh = l.raf.readInt();
				return new Rectangle(rx, ry, rw, rh);
			case TYPE_DATE:
				long da = l.raf.readLong();
				int dm = l.raf.readShort();
				Calendar dc = new GregorianCalendar();
				dc.setTimeInMillis(da*1000l + dm);
				return dc;
			case TYPE_OBJECT:
				byte[] ob = new byte[l.l2]; l.raf.read(ob);
				ByteArrayInputStream bis = new ByteArrayInputStream(ob);
				ObjectInput obi = new ObjectInputStream(bis);
				try {
					return obi.readObject();
				} catch (Exception e) {
					return null;
				}
			case TYPE_BOOLEAN_ARRAY:
				boolean[] boa = new boolean[l.l2];
				for (int i=0; i<boa.length; i++) boa[i] = l.raf.readBoolean();
				return boa;
			case TYPE_BYTE_ARRAY:
				byte[] bya = new byte[l.l2];
				for (int i=0; i<bya.length; i++) bya[i] = l.raf.readByte();
				return bya;
			case TYPE_SHORT_ARRAY:
				short[] sha = new short[l.l2/2];
				for (int i=0; i<sha.length; i++) sha[i] = l.raf.readShort();
				return sha;
			case TYPE_INT_ARRAY:
				int[] ina = new int[l.l2/4];
				for (int i=0; i<ina.length; i++) ina[i] = l.raf.readInt();
				return ina;
			case TYPE_LONG_ARRAY:
				long[] loa = new long[l.l2/8];
				for (int i=0; i<loa.length; i++) loa[i] = l.raf.readLong();
				return loa;
			case TYPE_FLOAT_ARRAY:
				float[] fla = new float[l.l2/4];
				for (int i=0; i<fla.length; i++) fla[i] = l.raf.readFloat();
				return fla;
			case TYPE_DOUBLE_ARRAY:
				double[] doa = new double[l.l2/8];
				for (int i=0; i<doa.length; i++) doa[i] = l.raf.readDouble();
				return doa;
			case TYPE_CHAR_ARRAY:
				char[] cha = new char[l.l2/2];
				for (int i=0; i<cha.length; i++) cha[i] = l.raf.readChar();
				return cha;
			case TYPE_STRING_ARRAY:
				int ns = l.raf.readShort();
				String[] sta = new String[ns];
				for (int i=0; i<sta.length; i++) {
					int sl = l.raf.readShort();
					byte[] sb = new byte[sl];
					l.raf.read(sb);
					try {
						sta[i] = new String(sb, "UTF-8");
					} catch (UnsupportedEncodingException uee) {
						sta[i] = new String(sb);
					}
				}
				return sta;
			case TYPE_COLOR_ARRAY:
				Color[] cla = new Color[l.l2/4];
				for (int i=0; i<cla.length; i++) cla[i] = new Color(l.raf.readInt(), true);
				return cla;
			case TYPE_POINT_ARRAY:
				Point[] poa = new Point[l.l2/8];
				for (int i=0; i<poa.length; i++) {
					int x = l.raf.readInt();
					int y = l.raf.readInt();
					poa[i] = new Point(x,y);
				}
				return poa;
			case TYPE_RECTANGLE_ARRAY:
				Rectangle[] rea = new Rectangle[l.l2/16];
				for (int i=0; i<rea.length; i++) {
					int a = l.raf.readInt();
					int b = l.raf.readInt();
					int c = l.raf.readInt();
					int d = l.raf.readInt();
					rea[i] = new Rectangle(a,b,c,d);
				}
				return rea;
			case TYPE_DATE_ARRAY:
				Calendar[] daa = new Calendar[l.l2/10];
				for (int i=0; i<daa.length; i++) {
					long d = l.raf.readLong();
					short m = l.raf.readShort();
					daa[i] = new GregorianCalendar();
					daa[i].setTimeInMillis(d*1000l + m);
				}
				return daa;
			case TYPE_OBJECT_ARRAY:
				byte[] oba = new byte[l.l2]; l.raf.read(oba);
				ByteArrayInputStream bisa = new ByteArrayInputStream(oba);
				ObjectInput obia = new ObjectInputStream(bisa);
				ArrayList<Object> obaa = new ArrayList<Object>();
				while (true) {
					try {
						Object o = obia.readObject();
						if (o == null) break;
						else obaa.add(o);
					} catch (Exception e) {
						break;
					}
				}
				return obaa.toArray();
			}
		} catch (IOException ioe) {}
		return null;
	}
	
	public boolean containsKey(int key) {
		Location l = locate(key, "r");
		if (l == null) return false;
		try { l.raf.close(); } catch (Exception e) {}
		return true;
	}

	public boolean containsKey(Object k) {
		if (k instanceof Number) return containsKey(((Number)k).intValue());
		else return false;
	}
	
	public Object get(int key) {
		Location l = locate(key, "r");
		if (l == null) return null;
		Object o = read(l);
		try { l.raf.close(); } catch (Exception e) {}
		return o;
	}

	public Object get(Object k) {
		if (k instanceof Number) return get(((Number)k).intValue());
		else return null;
	}
	
	public Object remove(int key) {
		Location l = locate(key, "rwd");
		if (l == null) return null;
		Object o = read(l);
		try { KFiles.cut(l.raf, l.p, l.l); } catch (IOException ioe) {}
		try { l.raf.close(); } catch (Exception e) {}
		return o;
	}

	public Object remove(Object k) {
		if (k instanceof Number) return remove(((Number)k).intValue());
		else return null;
	}
	
	public Object put(Integer key, Object v) {
		RandomAccessFile raf = null;
		try {
			Location l = locate(key, "rwd");
			if (l == null) {
				raf = new RandomAccessFile(this, "rwd");
			} else {
				KFiles.cut(l.raf, l.p, l.l);
				raf = l.raf;
			}
			raf.seek(raf.length());
			if (v instanceof Boolean) {
				raf.writeShort(9);
				raf.writeShort(TYPE_BOOLEAN);
				raf.writeInt(key);
				raf.writeBoolean((Boolean)v);
			} else if (v instanceof Byte) {
				raf.writeShort(9);
				raf.writeShort(TYPE_BYTE);
				raf.writeInt(key);
				raf.writeByte((Byte)v);
			} else if (v instanceof Short) {
				raf.writeShort(10);
				raf.writeShort(TYPE_SHORT);
				raf.writeInt(key);
				raf.writeShort((Short)v);
			} else if (v instanceof Integer) {
				raf.writeShort(12);
				raf.writeShort(TYPE_INT);
				raf.writeInt(key);
				raf.writeInt((Integer)v);
			} else if (v instanceof Long) {
				raf.writeShort(16);
				raf.writeShort(TYPE_LONG);
				raf.writeInt(key);
				raf.writeLong((Long)v);
			} else if (v instanceof Float) {
				raf.writeShort(12);
				raf.writeShort(TYPE_FLOAT);
				raf.writeInt(key);
				raf.writeFloat((Float)v);
			} else if (v instanceof Double) {
				raf.writeShort(16);
				raf.writeShort(TYPE_DOUBLE);
				raf.writeInt(key);
				raf.writeDouble((Double)v);
			} else if (v instanceof Character) {
				raf.writeShort(10);
				raf.writeShort(TYPE_CHAR);
				raf.writeInt(key);
				raf.writeChar((Character)v);
			} else if (v instanceof String) {
				byte[] vb;
				try {
					vb = ((String)v).getBytes("UTF-8");
				} catch (UnsupportedEncodingException uee) {
					vb = ((String)v).getBytes();
				}
				raf.writeShort(8+vb.length);
				raf.writeShort(TYPE_STRING);
				raf.writeInt(key);
				raf.write(vb);
			} else if (v instanceof Color) {
				raf.writeShort(12);
				raf.writeShort(TYPE_COLOR);
				raf.writeInt(key);
				raf.writeInt(((Color)v).getRGB());
			} else if (v instanceof Point) {
				raf.writeShort(16);
				raf.writeShort(TYPE_POINT);
				raf.writeInt(key);
				raf.writeInt(((Point)v).x);
				raf.writeInt(((Point)v).y);
			} else if (v instanceof Rectangle) {
				raf.writeShort(24);
				raf.writeShort(TYPE_RECTANGLE);
				raf.writeInt(key);
				raf.writeInt(((Rectangle)v).x);
				raf.writeInt(((Rectangle)v).y);
				raf.writeInt(((Rectangle)v).width);
				raf.writeInt(((Rectangle)v).height);
			} else if (v instanceof Calendar) {
				raf.writeShort(18);
				raf.writeShort(TYPE_DATE);
				raf.writeInt(key);
				long ll = ((Calendar)v).getTimeInMillis();
				raf.writeLong(ll / 1000);
				raf.writeShort((short)(ll % 1000));
			} else if (v instanceof Date) {
				raf.writeShort(18);
				raf.writeShort(TYPE_DATE);
				raf.writeInt(key);
				long ll = ((Date)v).getTime();
				raf.writeLong(ll / 1000);
				raf.writeShort((short)(ll % 1000));
			} else if (v instanceof boolean[]) {
				raf.writeShort(8+((boolean[])v).length);
				raf.writeShort(TYPE_BOOLEAN_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((boolean[])v).length; i++) raf.writeBoolean(((boolean[])v)[i]);
			} else if (v instanceof Boolean[]) {
				raf.writeShort(8+((Boolean[])v).length);
				raf.writeShort(TYPE_BOOLEAN_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Boolean[])v).length; i++) raf.writeBoolean(((Boolean[])v)[i]);
			} else if (v instanceof byte[]) {
				raf.writeShort(8+((byte[])v).length);
				raf.writeShort(TYPE_BYTE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((byte[])v).length; i++) raf.writeByte(((byte[])v)[i]);
			} else if (v instanceof Byte[]) {
				raf.writeShort(8+((Byte[])v).length);
				raf.writeShort(TYPE_BYTE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Byte[])v).length; i++) raf.writeByte(((Byte[])v)[i]);
			} else if (v instanceof short[]) {
				raf.writeShort(8+((short[])v).length*2);
				raf.writeShort(TYPE_SHORT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((short[])v).length; i++) raf.writeShort(((short[])v)[i]);
			} else if (v instanceof Short[]) {
				raf.writeShort(8+((Short[])v).length*2);
				raf.writeShort(TYPE_SHORT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Short[])v).length; i++) raf.writeShort(((Short[])v)[i]);
			} else if (v instanceof int[]) {
				raf.writeShort(8+((int[])v).length*4);
				raf.writeShort(TYPE_INT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((int[])v).length; i++) raf.writeInt(((int[])v)[i]);
			} else if (v instanceof Integer[]) {
				raf.writeShort(8+((Integer[])v).length*4);
				raf.writeShort(TYPE_INT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Integer[])v).length; i++) raf.writeInt(((Integer[])v)[i]);
			} else if (v instanceof long[]) {
				raf.writeShort(8+((long[])v).length*8);
				raf.writeShort(TYPE_LONG_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((long[])v).length; i++) raf.writeLong(((long[])v)[i]);
			} else if (v instanceof Long[]) {
				raf.writeShort(8+((Long[])v).length*8);
				raf.writeShort(TYPE_LONG_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Long[])v).length; i++) raf.writeLong(((Long[])v)[i]);
			} else if (v instanceof float[]) {
				raf.writeShort(8+((float[])v).length*4);
				raf.writeShort(TYPE_FLOAT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((float[])v).length; i++) raf.writeFloat(((float[])v)[i]);
			} else if (v instanceof Float[]) {
				raf.writeShort(8+((Float[])v).length*4);
				raf.writeShort(TYPE_FLOAT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Float[])v).length; i++) raf.writeFloat(((Float[])v)[i]);
			} else if (v instanceof double[]) {
				raf.writeShort(8+((double[])v).length*8);
				raf.writeShort(TYPE_DOUBLE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((double[])v).length; i++) raf.writeDouble(((double[])v)[i]);
			} else if (v instanceof Double[]) {
				raf.writeShort(8+((Double[])v).length*8);
				raf.writeShort(TYPE_DOUBLE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Double[])v).length; i++) raf.writeDouble(((Double[])v)[i]);
			} else if (v instanceof char[]) {
				raf.writeShort(8+((char[])v).length*2);
				raf.writeShort(TYPE_CHAR_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((char[])v).length; i++) raf.writeChar(((char[])v)[i]);
			} else if (v instanceof Character[]) {
				raf.writeShort(8+((Character[])v).length*2);
				raf.writeShort(TYPE_CHAR_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Character[])v).length; i++) raf.writeShort(((Character[])v)[i]);
			} else if (v instanceof String[]) {
				byte[][] strs = new byte[((String[])v).length][];
				try {
					for (int i=0; i<strs.length; i++) {
						strs[i] = ((String[])v)[i].getBytes("UTF-8");
					}
				} catch (UnsupportedEncodingException uee) {
					for (int i=0; i<strs.length; i++) {
						strs[i] = ((String[])v)[i].getBytes();
					}
				}
				int len = 0;
				for (int i=0; i<strs.length; i++) len += strs[i].length;
				raf.writeShort(8+2+2*strs.length+len);
				raf.writeShort(TYPE_STRING_ARRAY);
				raf.writeInt(key);
				raf.writeShort(strs.length);
				for (int i=0; i<strs.length; i++) {
					raf.writeShort(strs[i].length);
					raf.write(strs[i]);
				}
			} else if (v instanceof Color[]) {
				raf.writeShort(8+((Color[])v).length*4);
				raf.writeShort(TYPE_COLOR_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Color[])v).length; i++) raf.writeInt(((Color[])v)[i].getRGB());
			} else if (v instanceof Point[]) {
				raf.writeShort(8+((Point[])v).length*8);
				raf.writeShort(TYPE_POINT_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Point[])v).length; i++) {
					Point c = ((Point[])v)[i];
					raf.writeInt(c.x);
					raf.writeInt(c.y);
				}
			} else if (v instanceof Rectangle[]) {
				raf.writeShort(8+((Rectangle[])v).length*16);
				raf.writeShort(TYPE_RECTANGLE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Rectangle[])v).length; i++) {
					Rectangle c = ((Rectangle[])v)[i];
					raf.writeInt(c.x);
					raf.writeInt(c.y);
					raf.writeInt(c.width);
					raf.writeInt(c.height);
				}
			} else if (v instanceof Calendar[]) {
				raf.writeShort(8+((Calendar[])v).length*10);
				raf.writeShort(TYPE_DATE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Calendar[])v).length; i++) {
					long c = ((Calendar[])v)[i].getTimeInMillis();
					raf.writeLong(c / 1000);
					raf.writeShort((short)(c % 1000));
				}
			} else if (v instanceof Date[]) {
				raf.writeShort(8+((Date[])v).length*10);
				raf.writeShort(TYPE_DATE_ARRAY);
				raf.writeInt(key);
				for (int i=0; i<((Date[])v).length; i++) {
					long c = ((Date[])v)[i].getTime();
					raf.writeLong(c / 1000);
					raf.writeShort((short)(c % 1000));
				}
			} else if (v instanceof Object[]) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput obo = new ObjectOutputStream(bos);
				for (int i=0; i<((Object[])v).length; i++) obo.writeObject(((Object[])v)[i]);
				obo.close();
				byte[] ob = bos.toByteArray();
				raf.writeShort(8+ob.length);
				raf.writeShort(TYPE_OBJECT);
				raf.writeInt(key);
				raf.write(ob);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput obo = new ObjectOutputStream(bos);
				obo.writeObject(v);
				obo.close();
				byte[] ob = bos.toByteArray();
				raf.writeShort(8+ob.length);
				raf.writeShort(TYPE_OBJECT);
				raf.writeInt(key);
				raf.write(ob);
			}
			raf.close();
			return v;
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
			return null;
		}
	}

	public void putAll(Map<? extends Integer, ? extends Object> map) {
		Iterator<? extends Integer> i = map.keySet().iterator();
		while (i.hasNext()) {
			Integer x = i.next();
			put(x, map.get(x));
		}
	}
	
	public boolean getBoolean(int key) {
		Object o = get(key);
		return (o != null && o instanceof Boolean && (Boolean)o);
	}
	
	public byte getByte(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).byteValue();
	}
	
	public short getShort(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).shortValue();
	}
	
	public int getInt(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).intValue();
	}
	
	public long getLong(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).longValue();
	}
	
	public float getFloat(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).floatValue();
	}
	
	public double getDouble(int key) {
		Object o = get(key);
		return (o == null || !(o instanceof Number))?0:((Number)o).doubleValue();
	}
	
	public char getChar(int key) {
		Object o = get(key);
		return (o instanceof Character)?(Character)o:(o instanceof Number)?(char)((Number)o).intValue():0;
	}
	
	public String getString(int key) {
		Object o = get(key);
		return (o == null)?null:
			(o instanceof String)?(String)o:
				o.toString();
	}
	
	public Color getColor(int key) {
		Object o = get(key);
		return (o == null)?null:
			(o instanceof Color)?(Color)o:
				(o instanceof Integer)?new Color((Integer)o, true):
					null;
	}
	
	public Point getPoint(int key) {
		Object o = get(key);
		return (o instanceof Point)?(Point)o:null;
	}
	
	public Rectangle getRectangle(int key) {
		Object o = get(key);
		return (o instanceof Rectangle)?(Rectangle)o:null;
	}
	
	public Calendar getCalendar(int key) {
		Object o = get(key);
		if (o instanceof Calendar) {
			return (Calendar)o;
		} else if (o instanceof Date) {
			Calendar c = new GregorianCalendar();
			c.setTime((Date)o);
			return c;
		} else return null;
	}
	
	public Date getDate(int key) {
		Object o = get(key);
		return (o instanceof Date)?(Date)o:(o instanceof Calendar)?((Calendar)o).getTime():null;
	}
	
	public Object getObject(int key) {
		return get(key);
	}
	
	public boolean[] getBooleanArray(int key) {
		Object o = get(key);
		if (o instanceof boolean[]) return (boolean[])o;
		else return null;
	}
	
	public byte[] getByteArray(int key) {
		Object o = get(key);
		if (o instanceof byte[]) return (byte[])o;
		else return null;
	}
	
	public short[] getShortArray(int key) {
		Object o = get(key);
		if (o instanceof short[]) return (short[])o;
		else return null;
	}
	
	public int[] getIntArray(int key) {
		Object o = get(key);
		if (o instanceof int[]) return (int[])o;
		else return null;
	}
	
	public long[] getLongArray(int key) {
		Object o = get(key);
		if (o instanceof long[]) return (long[])o;
		else return null;
	}
	
	public float[] getFloatArray(int key) {
		Object o = get(key);
		if (o instanceof float[]) return (float[])o;
		else return null;
	}
	
	public double[] getDoubleArray(int key) {
		Object o = get(key);
		if (o instanceof double[]) return (double[])o;
		else return null;
	}
	
	public char[] getCharArray(int key) {
		Object o = get(key);
		if (o instanceof char[]) return (char[])o;
		else return null;
	}
	
	public String[] getStringArray(int key) {
		Object o = get(key);
		if (o instanceof String[]) return (String[])o;
		else return null;
	}
	
	public Color[] getColorArray(int key) {
		Object o = get(key);
		if (o instanceof Color[]) return (Color[])o;
		else return null;
	}
	
	public Point[] getPointArray(int key) {
		Object o = get(key);
		if (o instanceof Point[]) return (Point[])o;
		else return null;
	}
	
	public Rectangle[] getRectangleArray(int key) {
		Object o = get(key);
		if (o instanceof Rectangle[]) return (Rectangle[])o;
		else return null;
	}
	
	public Calendar[] getCalendarArray(int key) {
		Object o = get(key);
		if (o instanceof Calendar[]) return (Calendar[])o;
		else if (o instanceof Date[]) {
			Calendar[] cc = new Calendar[((Date[])o).length];
			for (int i=0; i<cc.length; i++) {
				cc[i] = new GregorianCalendar();
				cc[i].setTime(((Date[])o)[i]);
			}
			return cc;
		}
		else return null;
	}
	
	public Date[] getDateArray(int key) {
		Object o = get(key);
		if (o instanceof Date[]) return (Date[])o;
		else if (o instanceof Calendar[]) {
			Date[] dd = new Date[((Calendar[])o).length];
			for (int i=0; i<dd.length; i++) {
				dd[i] = ((Calendar[])o)[i].getTime();
			}
			return dd;
		}
		else return null;
	}
	
	public Object[] getObjectArray(int key) {
		Object o = get(key);
		if (o instanceof Object[]) return (Object[])o;
		else return null;
	}
	
	public boolean putBoolean(int key, boolean v) { return put(key, v) != null; }
	public boolean putByte(int key, byte v) { return put(key, v) != null; }
	public boolean putShort(int key, short v) { return put(key, v) != null; }
	public boolean putInt(int key, int v) { return put(key, v) != null; }
	public boolean putLong(int key, long v) { return put(key, v) != null; }
	public boolean putFloat(int key, float v) { return put(key, v) != null; }
	public boolean putDouble(int key, double v) { return put(key, v) != null; }
	public boolean putChar(int key, char v) { return put(key, v) != null; }
	public boolean putString(int key, String v) { return put(key, v) != null; }
	public boolean putColor(int key, Color v) { return put(key, v) != null; }
	public boolean putPoint(int key, Point v) { return put(key, v) != null; }
	public boolean putRectangle(int key, Rectangle v) { return put(key, v) != null; }
	public boolean putCalendar(int key, Calendar v) { return put(key, v) != null; }
	public boolean putDate(int key, Date v) { return put(key, v) != null; }
	public boolean putObject(int key, Object v) { return put(key, v) != null; }
	public boolean putBooleanArray(int key, boolean[] v) { return put(key, v) != null; }
	public boolean putByteArray(int key, byte[] v) { return put(key, v) != null; }
	public boolean putShortArray(int key, short[] v) { return put(key, v) != null; }
	public boolean putIntArray(int key, int[] v) { return put(key, v) != null; }
	public boolean putLongArray(int key, long[] v) { return put(key, v) != null; }
	public boolean putFloatArray(int key, float[] v) { return put(key, v) != null; }
	public boolean putDoubleArray(int key, double[] v) { return put(key, v) != null; }
	public boolean putCharArray(int key, char[] v) { return put(key, v) != null; }
	public boolean putStringArray(int key, String[] v) { return put(key, v) != null; }
	public boolean putColorArray(int key, Color[] v) { return put(key, v) != null; }
	public boolean putPointArray(int key, Point[] v) { return put(key, v) != null; }
	public boolean putRectangleArray(int key, Rectangle[] v) { return put(key, v) != null; }
	public boolean putCalendarArray(int key, Calendar[] v) { return put(key, v) != null; }
	public boolean putDateArray(int key, Date[] v) { return put(key, v) != null; }
	public boolean putObjectArray(int key, Object[] v) { return put(key, v) != null; }

	public void clear() {
		try {
			RandomAccessFile raf = new RandomAccessFile(this, "rwd");
			raf.setLength(0);
			raf.close();
		} catch (IOException ioe) {}
	}

	public boolean containsValue(Object obj) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(this, "r");
			long p; long fl = raf.length();
			while ((p = raf.getFilePointer()) < fl) {
				int l = raf.readShort();
				int t = raf.readShort();
				int k = raf.readInt();
				raf.skipBytes(l-8);
				long sav = raf.getFilePointer();
				Location ll = new Location(raf, p, l, t, k, p+8l, l-8);
				Object o = read(ll);
				raf.seek(sav);
				if (obj.equals(o)) {
					try { if (raf != null) raf.close(); } catch (Exception e) {}
					return true;
				}
			}
			raf.close();
			return false;
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
			return false;
		}
	}

	public Set<java.util.Map.Entry<Integer, Object>> entrySet() {
		Set<Map.Entry<Integer, Object>> s = new HashSet<Map.Entry<Integer, Object>>();
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(this, "r");
			long p; long fl = raf.length();
			while ((p = raf.getFilePointer()) < fl) {
				int l = raf.readShort();
				int t = raf.readShort();
				int k = raf.readInt();
				raf.skipBytes(l-8);
				long sav = raf.getFilePointer();
				Location ll = new Location(raf, p, l, t, k, p+8l, l-8);
				s.add(new AttrFileEntry(k, read(ll)));
				raf.seek(sav);
			}
			raf.close();
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
		}
		return s;
	}

	public boolean isEmpty() {
		try {
			RandomAccessFile raf = new RandomAccessFile(this, "r");
			boolean ret = (raf.length() == 0);
			raf.close();
			return ret;
		} catch (IOException ioe) {
			return true;
		}
	}

	public Set<Integer> keySet() {
		try {
			Set<Integer> s = new HashSet<Integer>();
			RandomAccessFile raf = new RandomAccessFile(this, "r");
			long fl = raf.length();
			while (raf.getFilePointer() < fl) {
				int l = raf.readShort();
				raf.readShort();
				int i = raf.readInt();
				raf.skipBytes(l-8);
				s.add(i);
			}
			raf.close();
			return s;
		} catch (IOException ioe) {
			return null;
		}
	}

	public int size() {
		try {
			RandomAccessFile raf = new RandomAccessFile(this, "r");
			long fl = raf.length();
			int n = 0;
			while (raf.getFilePointer() < fl) {
				int l = raf.readShort();
				raf.skipBytes(l-2);
				n++;
			}
			raf.close();
			return n;
		} catch (IOException ioe) {
			return 0;
		}
	}

	public Collection<Object> values() {
		Collection<Object> coll = new ArrayList<Object>();
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(this, "r");
			long p; long fl = raf.length();
			while ((p = raf.getFilePointer()) < fl) {
				int l = raf.readShort();
				int t = raf.readShort();
				int k = raf.readInt();
				raf.skipBytes(l-8);
				long sav = raf.getFilePointer();
				Location ll = new Location(raf, p, l, t, k, p+8l, l-8);
				coll.add(read(ll));
				raf.seek(sav);
			}
			raf.close();
		} catch (IOException ioe) {
			try {
				if (raf != null) raf.close();
			} catch (Exception e) {}
		}
		return coll;
	}
	
	private class AttrFileEntry implements Map.Entry<Integer, Object> {
		private int key;
		private Object value;
		public AttrFileEntry(int k, Object v) {
			key = k; value = v;
		}
		public Integer getKey() {
			return key;
		}
		public Object getValue() {
			return value;
		}
		public Object setValue(Object v) {
			return value = v;
		}
	}
}
