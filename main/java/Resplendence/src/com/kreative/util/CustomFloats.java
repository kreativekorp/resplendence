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
 * The <code>CustomFloats</code> class contains static methods
 * for handling custom IEEE-compatible floating-point formats.
 * Using these methods, you can, for example, read and write
 * nVidia/Pixar 1-5-10 half-precision floating point numbers
 * or ATI 1-7-16 three-quarter-precision floating point numbers.
 * <p>
 * The general contract of the <code>CustomFloats</code> class is that
 * <code>customFloatToRawBits(bitsToCustomFloat(i,e,m),e,m) == i</code> and
 * <code>customDoubleToRawBits(bitsToCustomDouble(i,e,m),e,m) == i</code>
 * for all 0 &le; <code>i</code> &lt; 2<sup>(1+e+m)</sup>
 * (or -2<sup>(e+m)</sup> &le; <code>i</code> &lt; 2<sup>(e+m)</sup>),
 * <code>e</code> &ge; 0, and <code>m</code> &ge; 0,
 * with exceptions made for <code>float</code> or <code>double</code>
 * NaN values being converted to other NaN values.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class CustomFloats {
	private CustomFloats() {
		//everything is static; no constructor for you
	}
	
	/**
	 * The optimal number of exponent bits that can be used to fit a floating-point number in a nibble, 2.
	 * This allows the values of positive and negative 0, 0.5, 1, 1.5, 2, 3, and infinity, and two NaNs.
	 */
	public static final int EIGHTH_PRECISION_EXPONENT_BITS = 2;
	/**
	 * The optimal number of mantissa bits that can be used to fit a floating-point number in a nibble, 1.
	 * This allows the values of positive and negative 0, 0.5, 1, 1.5, 2, 3, and infinity, and two NaNs.
	 */
	public static final int EIGHTH_PRECISION_MANTISSA_BITS = 1;
	/**
	 * The optimal number of exponent bits that can be used to fit a floating-point number in a byte, 4.
	 * This allows the values of positive and negative 0, 0.001953125, 0.00390625, 0.005859375, 0.0078125,
	 * 0.009765625, 0.01171875, 0.013671875, 0.015625, 0.017578125, 0.01953125, 0.021484375, 0.0234375,
	 * 0.025390625, 0.02734375, 0.029296875, 0.03125, 0.03515625, 0.0390625, 0.04296875, 0.046875,
	 * 0.05078125, 0.0546875, 0.05859375, 0.0625, 0.0703125, 0.078125, 0.0859375, 0.09375, 0.1015625,
	 * 0.109375, 0.1171875, 0.125, 0.140625, 0.15625, 0.171875, 0.1875, 0.203125, 0.21875, 0.234375,
	 * 0.25, 0.28125, 0.3125, 0.34375, 0.375, 0.40625, 0.4375, 0.46875, 0.5, 0.5625, 0.625, 0.6875,
	 * 0.75, 0.8125, 0.875, 0.9375, 1, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2, 2.25, 2.5, 2.75,
	 * 3, 3.25, 3.5, 3.75, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 20,
	 * 22, 24, 26, 28, 30, 32, 36, 40, 44, 48, 52, 56, 60, 64, 72, 80, 88, 96, 104, 112, 120, 128, 144,
	 * 160, 176, 192, 208, 224, 240, and infinity, six quiet NaNs, and eight signalling NaNs.
	 */
	public static final int QUARTER_PRECISION_EXPONENT_BITS = 4;
	/**
	 * The optimal number of mantissa bits that can be used to fit a floating-point number in a byte, 3.
	 * This allows the values of positive and negative 0, 0.001953125, 0.00390625, 0.005859375, 0.0078125,
	 * 0.009765625, 0.01171875, 0.013671875, 0.015625, 0.017578125, 0.01953125, 0.021484375, 0.0234375,
	 * 0.025390625, 0.02734375, 0.029296875, 0.03125, 0.03515625, 0.0390625, 0.04296875, 0.046875,
	 * 0.05078125, 0.0546875, 0.05859375, 0.0625, 0.0703125, 0.078125, 0.0859375, 0.09375, 0.1015625,
	 * 0.109375, 0.1171875, 0.125, 0.140625, 0.15625, 0.171875, 0.1875, 0.203125, 0.21875, 0.234375,
	 * 0.25, 0.28125, 0.3125, 0.34375, 0.375, 0.40625, 0.4375, 0.46875, 0.5, 0.5625, 0.625, 0.6875,
	 * 0.75, 0.8125, 0.875, 0.9375, 1, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2, 2.25, 2.5, 2.75,
	 * 3, 3.25, 3.5, 3.75, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 20,
	 * 22, 24, 26, 28, 30, 32, 36, 40, 44, 48, 52, 56, 60, 64, 72, 80, 88, 96, 104, 112, 120, 128, 144,
	 * 160, 176, 192, 208, 224, 240, and infinity, six quiet NaNs, and eight signalling NaNs.
	 */
	public static final int QUARTER_PRECISION_MANTISSA_BITS = 3;
	/**
	 * The number of exponent bits in an IEEE-754, nVidia, or Pixar half-precision floating-point number, 5.
	 */
	public static final int HALF_PRECISION_EXPONENT_BITS = 5;
	/**
	 * The number of mantissa bits in an IEEE-754, nVidia, or Pixar half-precision floating-point number, 10.
	 */
	public static final int HALF_PRECISION_MANTISSA_BITS = 10;
	/**
	 * The number of exponent bits in an ATI three-quarter-precision floating-point number, 7.
	 */
	public static final int THREE_QUARTER_PRECISION_EXPONENT_BITS = 7;
	/**
	 * The number of mantissa bits in an ATI three-quarter-precision floating-point number, 16.
	 */
	public static final int THREE_QUARTER_PRECISION_MANTISSA_BITS = 16;
	/**
	 * The number of exponent bits in an IEEE-754 single-precision floating-point number, 8.
	 */
	public static final int SINGLE_PRECISION_EXPONENT_BITS = 8;
	/**
	 * The number of mantissa bits in an IEEE-754 single-precision floating-point number, 23.
	 */
	public static final int SINGLE_PRECISION_MANTISSA_BITS = 23;
	/**
	 * A number of exponent bits that can be used to fit a floating-point number in six bytes, 10.
	 * This is interpolated from the IEEE-754 single-precision and double-precision formats.
	 */
	public static final int THREE_HALF_PRECISION_EXPONENT_BITS = 10;
	/**
	 * A number of mantissa bits that can be used to fit a floating-point number in six bytes, 37.
	 * This is interpolated from the IEEE-754 single-precision and double-precision formats.
	 */
	public static final int THREE_HALF_PRECISION_MANTISSA_BITS = 37;
	/**
	 * The number of exponent bits in an IEEE-754 double-precision floating-point number, 11.
	 */
	public static final int DOUBLE_PRECISION_EXPONENT_BITS = 11;
	/**
	 * The number of mantissa bits in an IEEE-754 double-precision floating-point number, 52.
	 */
	public static final int DOUBLE_PRECISION_MANTISSA_BITS = 52;
	
	/**
	 * Returns the floating point number corresponding to a given bit representation as a <code>float</code>.
	 * <p>
	 * If <code>expWidth</code> is 8 and <code>mantWidth</code> is 23, this method calls <code>Float.intBitsToFloat(bits)</code>.
	 * If <code>expWidth</code> is 11 and <code>mantWidth</code> is 52, this method calls <code>Double.longBitsToDouble(bits)</code>.
	 * Otherwise, <code>bits</code> is dissected and reconstructed to create the bit representation of
	 * an IEEE-754 single-precision floating-point number and converted to a <code>float</code> with <code>Float.intBitsToFloat()</code>.
	 * @param bits any <code>long</code> integer.
	 * @param expWidth the number of bits used to represent the exponent.
	 * @param mantWidth the number of bits used to represent the mantissa.
	 * @return the floating-point value with the specified bit pattern.
	 */
	public static float bitsToCustomFloat(long bits, int expWidth, int mantWidth) {
		     if (expWidth ==  8 && mantWidth == 23) return Float.intBitsToFloat((int)bits);
		else if (expWidth == 11 && mantWidth == 52) return (float)Double.longBitsToDouble(bits);
		long emax = (1L << (long)expWidth)-1L;
		long bias = (1L << ((long)expWidth-1L))-1L;
		long mantMask = (1L << (long)mantWidth)-1L;
		long mantMsbMask = 1L << ((long)mantWidth-1L);
		long expMask = ((1L << (long)expWidth)-1L) << (long)mantWidth;
		long signMask = 1L << ((long)expWidth+(long)mantWidth);
		long s = (bits & signMask) >>> ((long)expWidth+(long)mantWidth);
		long e = (bits & expMask) >>> (long)mantWidth;
		long m = (bits & mantMask);
		int floatBits = (int)(s << 31); // preserve sign
		if (e == 0) {
			// zero or subnormal
			if (m != 0) {
				// subnormal; must be normalized
				long ne = 1L-bias+127L;
				long nm = m << (23L-(long)mantWidth);
				while ((nm & 0x800000L) == 0) {
					nm <<= 1L;
					ne--;
				}
				floatBits |= (ne & 0xFFL) << 23L;
				floatBits |= (nm & 0x7FFFFFL);
			}
		} else if (e == emax) {
			// infinity or nan
			floatBits |= 0x7F800000L;
			floatBits |= (m & mantMsbMask) << (23L-(long)mantWidth); // preserve distinction between quiet nan and signaling nan
			floatBits |= (m & (mantMsbMask-1L));
		} else {
			// normal number
			long ne = e-bias+127L;
			long nm = m << (23L-(long)mantWidth);
			floatBits |= (ne & 0xFFL) << 23L;
			floatBits |= (nm & 0x7FFFFFL);
		}
		return Float.intBitsToFloat(floatBits);
	}
	
	/**
	 * Returns a representation of the specified floating-point value.
	 * <p>
	 * If <code>expWidth</code> is 8 and <code>mantWidth</code> is 23, this method calls <code>Float.floatToRawIntBits(f)</code>.
	 * If <code>expWidth</code> is 11 and <code>mantWidth</code> is 52, this method calls <code>Double.doubleToRawLongBits(f)</code>.
	 * Otherwise, <code>f</code> is converted to a bit representation with <code>Float.floatToRawIntBits(f)</code>
	 * and dissected and reconstructed to create the desired bit representation.
	 * @param f a single-precision floating-point number.
	 * @param expWidth the number of bits used to represent the exponent.
	 * @param mantWidth the number of bits used to represent the mantissa.
	 * @return the bits that represent the floating-point number.
	 */
	public static long customFloatToRawBits(float f, int expWidth, int mantWidth) {
		     if (expWidth ==  8 && mantWidth == 23) return Float.floatToRawIntBits(f);
		else if (expWidth == 11 && mantWidth == 52) return Double.doubleToRawLongBits(f);
		long floatBits = Float.floatToRawIntBits(f);
		long s = (floatBits & 0x80000000L) >>> 31;
		long e = (floatBits & 0x7F800000L) >>> 23;
		long m = (floatBits & 0x007FFFFFL);
		long emax = (1L << (long)expWidth)-1L;
		long bias = (1L << ((long)expWidth-1L))-1L;
		long mantMask = (1L << (long)mantWidth)-1L;
		long mantMsbMask = 1L << ((long)mantWidth-1L);
		long expMask = ((1L << (long)expWidth)-1L) << (long)mantWidth;
		long bits = (s << ((long)expWidth + (long)mantWidth)); // preserve sign
		if (e == 0) {
			// zero or subnormal
			// we'll take a shortcut here since subnormal double-precision floats
			// are not likely to be anywhere near the range for minifloats
		} else if (e == 255) {
			// infinity or nan
			bits |= expMask;
			bits |= (m & 0x400000L) >>> (23L-(long)mantWidth); // preserve distinction between quiet nan and signaling nan
			bits |= (m & (mantMsbMask-1L));
		} else {
			// normal number
			long ne = e-127L+bias;
			long nm = m >>> (23L-(long)mantWidth);
			if (ne >= emax) {
				// overflow; substitute infinity
				bits |= expMask;
			} else if (ne <= 0) {
				// must be subnormalized
				nm |= 1L << (long)mantWidth;
				nm >>>= 1L-ne;
				bits |= (nm & mantMask);
			} else {
				// perfectly comfy
				bits |= (ne << (long)mantWidth) & expMask;
				bits |= (nm & mantMask);
			}
		}
		return bits;
	}
	
	/**
	 * Returns the floating point number corresponding to a given bit representation as a <code>double</code>.
	 * <p>
	 * If <code>expWidth</code> is 8 and <code>mantWidth</code> is 23, this method calls <code>Float.intBitsToFloat(bits)</code>.
	 * If <code>expWidth</code> is 11 and <code>mantWidth</code> is 52, this method calls <code>Double.longBitsToDouble(bits)</code>.
	 * Otherwise, <code>bits</code> is dissected and reconstructed to create the bit representation of
	 * an IEEE-754 double-precision floating-point number and converted to a <code>double</code> with <code>Double.longBitsToDouble()</code>.
	 * @param bits any <code>long</code> integer.
	 * @param expWidth the number of bits used to represent the exponent.
	 * @param mantWidth the number of bits used to represent the mantissa.
	 * @return the floating-point value with the specified bit pattern.
	 */
	public static double bitsToCustomDouble(long bits, int expWidth, int mantWidth) {
		     if (expWidth ==  8 && mantWidth == 23) return Float.intBitsToFloat((int)bits);
		else if (expWidth == 11 && mantWidth == 52) return Double.longBitsToDouble(bits);
		long emax = (1L << (long)expWidth)-1L;
		long bias = (1L << ((long)expWidth-1L))-1L;
		long mantMask = (1L << (long)mantWidth)-1L;
		long mantMsbMask = 1L << ((long)mantWidth-1L);
		long expMask = ((1L << (long)expWidth)-1L) << (long)mantWidth;
		long signMask = 1L << ((long)expWidth+(long)mantWidth);
		long s = (bits & signMask) >>> ((long)expWidth+(long)mantWidth);
		long e = (bits & expMask) >>> (long)mantWidth;
		long m = (bits & mantMask);
		long doubleBits = (s << 63); // preserve sign
		if (e == 0) {
			// zero or subnormal
			if (m != 0) {
				// subnormal; must be normalized
				long ne = 1L-bias+1023L;
				long nm = m << (52L-(long)mantWidth);
				while ((nm & 0x10000000000000L) == 0) {
					nm <<= 1L;
					ne--;
				}
				doubleBits |= (ne & 0x7FFL) << 52L;
				doubleBits |= (nm & 0xFFFFFFFFFFFFFL);
			}
		} else if (e == emax) {
			// infinity or nan
			doubleBits |= 0x7FF0000000000000L;
			doubleBits |= (m & mantMsbMask) << (52L-(long)mantWidth); // preserve distinction between quiet nan and signaling nan
			doubleBits |= (m & (mantMsbMask-1L));
		} else {
			// normal number
			long ne = e-bias+1023L;
			long nm = m << (52L-(long)mantWidth);
			doubleBits |= (ne & 0x7FFL) << 52L;
			doubleBits |= (nm & 0xFFFFFFFFFFFFFL);
		}
		return Double.longBitsToDouble(doubleBits);
	}
	
	/**
	 * Returns a representation of the specified floating-point value.
	 * <p>
	 * If <code>expWidth</code> is 8 and <code>mantWidth</code> is 23, this method calls <code>Float.floatToRawIntBits(d)</code>.
	 * If <code>expWidth</code> is 11 and <code>mantWidth</code> is 52, this method calls <code>Double.doubleToRawLongBits(d)</code>.
	 * Otherwise, <code>d</code> is converted to a bit representation with <code>Double.doubleToRawLongBits(d)</code>
	 * and dissected and reconstructed to create the desired bit representation.
	 * @param d a double-precision floating-point number.
	 * @param expWidth the number of bits used to represent the exponent.
	 * @param mantWidth the number of bits used to represent the mantissa.
	 * @return the bits that represent the floating-point number.
	 */
	public static long customDoubleToRawBits(double d, int expWidth, int mantWidth) {
		     if (expWidth ==  8 && mantWidth == 23) return Float.floatToRawIntBits((float)d);
		else if (expWidth == 11 && mantWidth == 52) return Double.doubleToRawLongBits(d);
		long doubleBits = Double.doubleToRawLongBits(d);
		long s = (doubleBits & 0x8000000000000000L) >>> 63;
		long e = (doubleBits & 0x7FF0000000000000L) >>> 52;
		long m = (doubleBits & 0x000FFFFFFFFFFFFFL);
		long emax = (1L << (long)expWidth)-1L;
		long bias = (1L << ((long)expWidth-1L))-1L;
		long mantMask = (1L << (long)mantWidth)-1L;
		long mantMsbMask = 1L << ((long)mantWidth-1L);
		long expMask = ((1L << (long)expWidth)-1L) << (long)mantWidth;
		long bits = (s << ((long)expWidth + (long)mantWidth)); // preserve sign
		if (e == 0) {
			// zero or subnormal
			// we'll take a shortcut here since subnormal double-precision floats
			// are not likely to be anywhere near the range for minifloats
		} else if (e == 2047) {
			// infinity or nan
			bits |= expMask;
			bits |= (m & 0x8000000000000L) >>> (52L-(long)mantWidth); // preserve distinction between quiet nan and signaling nan
			bits |= (m & (mantMsbMask-1L));
		} else {
			// normal number
			long ne = e-1023L+bias;
			long nm = m >>> (52L-(long)mantWidth);
			if (ne >= emax) {
				// overflow; substitute infinity
				bits |= expMask;
			} else if (ne <= 0) {
				// must be subnormalized
				nm |= 1L << (long)mantWidth;
				nm >>>= 1L-ne;
				bits |= (nm & mantMask);
			} else {
				// perfectly comfy
				bits |= (ne << (long)mantWidth) & expMask;
				bits |= (nm & mantMask);
			}
		}
		return bits;
	}
}
