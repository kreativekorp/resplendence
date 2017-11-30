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

package com.kreative.util;

/**
 * The <code>KArrays</code> class contains a variety of useful
 * static methods to be used on arrays.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class KArrays {
	private KArrays() {
		//static methods; no constructor for you
	}
	
	/**
	 * Demotes an array of <code>Boolean</code> objects
	 * to an array of <code>boolean</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static boolean[] demote(Boolean[] a) {
		if (a == null) return null;
		boolean[] b = new boolean[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Character</code> objects
	 * to an array of <code>char</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static char[] demote(Character[] a) {
		if (a == null) return null;
		char[] b = new char[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Byte</code> objects
	 * to an array of <code>byte</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static byte[] demote(Byte[] a) {
		if (a == null) return null;
		byte[] b = new byte[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Short</code> objects
	 * to an array of <code>short</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static short[] demote(Short[] a) {
		if (a == null) return null;
		short[] b = new short[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Integer</code> objects
	 * to an array of <code>int</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static int[] demote(Integer[] a) {
		if (a == null) return null;
		int[] b = new int[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Long</code> objects
	 * to an array of <code>long</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static long[] demote(Long[] a) {
		if (a == null) return null;
		long[] b = new long[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Float</code> objects
	 * to an array of <code>float</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static float[] demote(Float[] a) {
		if (a == null) return null;
		float[] b = new float[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Demotes an array of <code>Double</code> objects
	 * to an array of <code>double</code> primitives.
	 * @param a the array of objects.
	 * @return the array of primitives.
	 */
	public static double[] demote(Double[] a) {
		if (a == null) return null;
		double[] b = new double[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>boolean</code> primitives
	 * to an array of <code>Boolean</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Boolean[] promote(boolean[] a) {
		if (a == null) return null;
		Boolean[] b = new Boolean[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>char</code> primitives
	 * to an array of <code>Character</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Character[] promote(char[] a) {
		if (a == null) return null;
		Character[] b = new Character[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>byte</code> primitives
	 * to an array of <code>Byte</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Byte[] promote(byte[] a) {
		if (a == null) return null;
		Byte[] b = new Byte[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>short</code> primitives
	 * to an array of <code>Short</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Short[] promote(short[] a) {
		if (a == null) return null;
		Short[] b = new Short[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>int</code> primitives
	 * to an array of <code>Integer</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Integer[] promote(int[] a) {
		if (a == null) return null;
		Integer[] b = new Integer[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>long</code> primitives
	 * to an array of <code>Long</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Long[] promote(long[] a) {
		if (a == null) return null;
		Long[] b = new Long[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>float</code> primitives
	 * to an array of <code>Float</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Float[] promote(float[] a) {
		if (a == null) return null;
		Float[] b = new Float[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Promotes an array of <code>double</code> primitives
	 * to an array of <code>Double</code> objects.
	 * @param a the array of primitives.
	 * @return the array of object.
	 */
	public static Double[] promote(double[] a) {
		if (a == null) return null;
		Double[] b = new Double[a.length];
		for (int i=0; i<a.length; i++) b[i] = a[i];
		return b;
	}
	
	/**
	 * Unpacks integers of 8 bits or fewer from a packed byte array.
	 * For example:
	 * <p><code>unpack({0x42}, 1) == {0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00}</code>
	 * <p><code>unpack({0x8E}, 2) == {0x02, 0x00, 0x03, 0x02}</code>
	 * <p><code>unpack({0x79, 0x2E}, 3) == {0x07, 0x01, 0x02, 0x06}</code>
	 * <p><code>unpack({0x79, 0x2E}, 4) == {0x07, 0x09, 0x02, 0x0E}</code>
	 * @param data the packed data
	 * @param bpp the number of bits in each packed integer
	 * @return the unpacked integers
	 */
	public static byte[] unpack(byte[] data, int bpp) {
		int m = (1 << bpp) - 1;
		if (bpp <= 0) {
			return null;
		} else if (bpp <= 1) {
			byte[] nd = new byte[data.length*8];
			for (int s=0, d=0; s<data.length && d < nd.length; s++, d+=8) {
				nd[d+0] = (byte)((data[s] >> 7) & m);
				nd[d+1] = (byte)((data[s] >> 6) & m);
				nd[d+2] = (byte)((data[s] >> 5) & m);
				nd[d+3] = (byte)((data[s] >> 4) & m);
				nd[d+4] = (byte)((data[s] >> 3) & m);
				nd[d+5] = (byte)((data[s] >> 2) & m);
				nd[d+6] = (byte)((data[s] >> 1) & m);
				nd[d+7] = (byte)((data[s] >> 0) & m);
			}
			return nd;
		} else if (bpp <= 2) {
			byte[] nd = new byte[data.length*4];
			for (int s=0, d=0; s<data.length && d < nd.length; s++, d+=4) {
				nd[d+0] = (byte)((data[s] >> 6) & m);
				nd[d+1] = (byte)((data[s] >> 4) & m);
				nd[d+2] = (byte)((data[s] >> 2) & m);
				nd[d+3] = (byte)((data[s] >> 0) & m);
			}
			return nd;
		} else if (bpp <= 4) {
			byte[] nd = new byte[data.length*2];
			for (int s=0, d=0; s<data.length && d < nd.length; s++, d+=2) {
				nd[d+0] = (byte)((data[s] >> 4) & m);
				nd[d+1] = (byte)((data[s] >> 0) & m);
			}
			return nd;
		} else if (bpp <= 8) {
			byte[] nd = new byte[data.length];
			for (int i=0; i<data.length && i<nd.length; i++) {
				nd[i] = (byte)(data[i] & m);
			}
			return nd;
		} else {
			return null;
		}
	}
	
	/**
	 * Cuts a segment of data out of an array.
	 * Two arrays are returned: the new contents of the array,
	 * and the cut out segment.
	 * For instance, if your array is
	 * <br><br>
	 * <code>{05, 23, 75, A8, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * then <code>arrayCut(arr, 4, 3)</code> will return
	 * <br><br>
	 * <code>{{05, 23, 75, A8, DB, 3C, 01, A9, 99, 80}, {FF, 2E, 09}}</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the array, inclusive.
	 * Other values may give unexpected results.
	 * @param arr the array to cut from.
	 * @param offset the index of the first element to cut.
	 * @param bytesToCut the number of elements to cut.
	 * @return the new contents of the array, and the cut out segment of the array.
	 */
	public static byte[][] cut(byte[] arr, int offset, int bytesToCut) {
		byte[] stuff = new byte[bytesToCut];
		for (int s=offset, d=0; d<bytesToCut && s<arr.length; s++, d++) {
			stuff[d] = arr[s];
		}
		byte[] junk = new byte[Math.max(arr.length-bytesToCut, offset)];
		for (int s=0, d=0; d<offset && s<offset && d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		for (int s=offset+bytesToCut, d=offset; d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		return new byte[][]{junk,stuff};
	}
	
	/**
	 * Copies a segment of data out of an array. Returns the copied segment.
	 * For instance, if your array is
	 * <br><br>
	 * <code>{05, 23, 75, A8, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * then <code>arrayCopy(arr, 4, 3)</code> will return
	 * <br><br>
	 * <code>{FF, 2E, 09}</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the array, inclusive.
	 * Other values may give unexpected results.
	 * @param arr the array to copy from.
	 * @param offset the index of the first element to copy.
	 * @param bytesToCopy the number of elements to copy.
	 * @return the copied segment of the array.
	 */
	public static byte[] copy(byte[] arr, int offset, int bytesToCopy) {
		byte[] stuff = new byte[bytesToCopy];
		for (int s=offset, d=0; d<bytesToCopy && s<arr.length; s++, d++) {
			stuff[d] = arr[s];
		}
		return stuff;
	}
	
	/**
	 * Pastes a segment of zero bytes into an array of bytes.
	 * The bytes after the specified offset are moved to higher indexes.
	 * Returns the new contents of the array.
	 * For instance, if your array is
	 * <br><br>
	 * <code>{05, 23, 75, A8, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * then <code>arrayPaste(arr, 4, 3)</code> will return
	 * <br><br>
	 * <code>{05, 23, 75, A8, 00, 00, 00, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the array, inclusive.
	 * Other values may give unexpected results.
	 * @param arr the array to paste into.
	 * @param offset the index where the first zero byte will be pasted.
	 * @param bytesToPaste the number of zero bytes to paste.
	 * @return the new contents of the array.
	 */
	public static byte[] paste(byte[] arr, int offset, int bytesToPaste) {
		byte[] junk = new byte[arr.length+bytesToPaste];
		for (int s=0, d=0; d<offset && s<offset && d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		for (int s=offset, d=offset+bytesToPaste; d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		return junk;
	}
	
	/**
	 * Pastes one array of bytes into another array of bytes.
	 * The bytes after the specified offset are moved to higher indexes.
	 * Returns the new contents of the array.
	 * For instance, if your array is
	 * <br><br>
	 * <code>{05, 23, 75, A8, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * then <code>arrayPaste(arr, 4, new byte[]{0x55, 0xEE, 0xB4})</code> will return
	 * <br><br>
	 * <code>{05, 23, 75, A8, 55, EE, B4, FF, 2E, 09, DB, 3C, 01, A9, 99, 80}</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the array, inclusive.
	 * Other values may give unexpected results.
	 * @param arr the array to paste into.
	 * @param offset the index where the first byte of <code>stuff</code> will be pasted.
	 * @param stuff the array of bytes to paste.
	 * @return the new contents of the array.
	 */
	public static byte[] paste(byte[] arr, int offset, byte[] stuff) {
		byte[] junk = new byte[arr.length+stuff.length];
		for (int s=0, d=0; d<offset && s<offset && d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		for (int s=0, d=offset; s<stuff.length && d<junk.length; s++, d++) {
			junk[d] = stuff[s];
		}
		for (int s=offset, d=offset+stuff.length; d<junk.length && s<arr.length; s++, d++) {
			junk[d] = arr[s];
		}
		return junk;
	}
	
	/**
	 * Get a short integer value out of an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @return the corresponding short integer.
	 */
	public static short getShort(byte[] b, int i, boolean le) {
		if (le) {
			return (short)(((b[i+1] & 0xFF) << 8) | (b[i] & 0xFF));
		} else {
			return (short)(((b[i] & 0xFF) << 8) | (b[i+1] & 0xFF));
		}
	}
	
	/**
	 * Get an integer value out of an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @return the corresponding integer.
	 */
	public static int getInt(byte[] b, int i, boolean le) {
		if (le) {
			return (int)(((b[i+3] & 0xFF) << 24) | ((b[i+2] & 0xFF) << 16) | ((b[i+1] & 0xFF) << 8) | (b[i] & 0xFF));
		} else {
			return (int)(((b[i] & 0xFF) << 24) | ((b[i+1] & 0xFF) << 16) | ((b[i+2] & 0xFF) << 8) | (b[i+3] & 0xFF));
		}
	}
	
	/**
	 * Get a long integer value out of an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @return the corresponding long integer.
	 */
	public static long getLong(byte[] b, int i, boolean le) {
		if (le) {
			return (long)(((b[i+7] & 0xFFl) << 56) | ((b[i+6] & 0xFFl) << 48) | ((b[i+5] & 0xFFl) << 40) | ((b[i+4] & 0xFFl) << 32) | ((b[i+3] & 0xFFl) << 24) | ((b[i+2] & 0xFFl) << 16) | ((b[i+1] & 0xFFl) << 8) | (b[i] & 0xFFl));
		} else {
			return (long)(((b[i] & 0xFFl) << 56) | ((b[i+1] & 0xFFl) << 48) | ((b[i+2] & 0xFFl) << 40) | ((b[i+3] & 0xFFl) << 32) | ((b[i+4] & 0xFFl) << 24) | ((b[i+5] & 0xFFl) << 16) | ((b[i+6] & 0xFFl) << 8) | (b[i+7] & 0xFFl));
		}
	}
	
	/**
	 * Put a short integer value into an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @param s the short integer value.
	 * @return <code>s</code>
	 */
	public static short putShort(byte[] b, int i, boolean le, short s) {
		if (le) {
			b[i+1] = (byte)((s >> 8) & 0xFF);
			b[i] = (byte)(s & 0xFF);
		} else {
			b[i] = (byte)((s >> 8) & 0xFF);
			b[i+1] = (byte)(s & 0xFF);
		}
		return s;
	}
	
	/**
	 * Put an integer value into an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @param s the integer value.
	 * @return <code>s</code>
	 */
	public static int putInt(byte[] b, int i, boolean le, int s) {
		if (le) {
			b[i+3] = (byte)((s >> 24) & 0xFF);
			b[i+2] = (byte)((s >> 16) & 0xFF);
			b[i+1] = (byte)((s >> 8) & 0xFF);
			b[i] = (byte)(s & 0xFF);
		} else {
			b[i] = (byte)((s >> 24) & 0xFF);
			b[i+1] = (byte)((s >> 16) & 0xFF);
			b[i+2] = (byte)((s >> 8) & 0xFF);
			b[i+3] = (byte)(s & 0xFF);
		}
		return s;
	}
	
	/**
	 * Put a long integer value into an array of bytes.
	 * @param b the array of bytes.
	 * @param i the index of the first byte of the integer.
	 * @param le true if the data is in little-endian format, false if the data is in big-endian format.
	 * @param s the long integer value.
	 * @return <code>s</code>
	 */
	public static long putLong(byte[] b, int i, boolean le, long s) {
		if (le) {
			b[i+7] = (byte)((s >> 56) & 0xFF);
			b[i+6] = (byte)((s >> 48) & 0xFF);
			b[i+5] = (byte)((s >> 40) & 0xFF);
			b[i+4] = (byte)((s >> 32) & 0xFF);
			b[i+3] = (byte)((s >> 24) & 0xFF);
			b[i+2] = (byte)((s >> 16) & 0xFF);
			b[i+1] = (byte)((s >> 8) & 0xFF);
			b[i] = (byte)(s & 0xFF);
		} else {
			b[i] = (byte)((s >> 56) & 0xFF);
			b[i+1] = (byte)((s >> 48) & 0xFF);
			b[i+2] = (byte)((s >> 40) & 0xFF);
			b[i+3] = (byte)((s >> 32) & 0xFF);
			b[i+4] = (byte)((s >> 24) & 0xFF);
			b[i+5] = (byte)((s >> 16) & 0xFF);
			b[i+6] = (byte)((s >> 8) & 0xFF);
			b[i+7] = (byte)(s & 0xFF);
		}
		return s;
	}
}
