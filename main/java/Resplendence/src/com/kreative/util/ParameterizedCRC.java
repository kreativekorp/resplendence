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

import java.util.zip.*;

/**
 * A class that can be used to compute any CRC covered by the
 * Rocksoft&trade; parameterized model CRC algorithm. This
 * algorithm is described in Ross Williams' "A Painless Guide
 * to CRC Error Detection Algorithms" and generalizes most
 * CRC algorithms.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class ParameterizedCRC implements Checksum {
	// Parameters of the CRC Algorithm
	private long bitWidth;
	private long polynomial;
	private long initialValue;
	private boolean reflectIn;
	private boolean reflectOut;
	private long xorValue;
	private long check;
	
	// Cached Values Used Internally
	private long lrshift;
	private long bshift;
	private long msbmask;
	private long widmask;
	
	// Current Value of the Checksum
	private long currValue;
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by XMODEM, ZMODEM, Acorn MOS 1.20, and BinHex.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by XMODEM, ZMODEM, Acorn MOS 1.20, and BinHex.
	 */
	public static ParameterizedCRC createCRC16XMODEM() {
		return new ParameterizedCRC(16, (short)0x1021, 0, false, false, 0, (short)0x31C3);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the original CRC-16/CCITT used by Kermit.
	 * @return a new ParameterizedCRC object for computing the original CRC-16/CCITT used by Kermit.
	 */
	public static ParameterizedCRC createCRC16KERMIT() {
		return new ParameterizedCRC(16, (short)0x1021, 0, true, true, 0, (short)0x2189);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by the ARC, LHA, and ZOO formats.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by the ARC, LHA, and ZOO formats.
	 */
	public static ParameterizedCRC createCRC16ARC() {
		return new ParameterizedCRC(16, (short)0x8005, 0, true, true, 0, (short)0xBB3D);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by I-CODE UID, GENIbus, and TI Tag-It.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by I-CODE UID, GENIbus, and TI Tag-It.
	 */
	public static ParameterizedCRC createCRC16ICODE() {
		return new ParameterizedCRC(16, (short)0x1021, -1, false, false, -1, (short)0xD64E);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by ITU-T Recommendations V.42 and X.25.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by ITU-T Recommendations V.42 and X.25.
	 */
	public static ParameterizedCRC createCRC16X25() {
		return new ParameterizedCRC(16, (short)0x1021, -1, true, true, -1, (short)0x906E);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by USB.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by USB.
	 */
	public static ParameterizedCRC createCRC16USB() {
		return new ParameterizedCRC(16, (short)0x8005, -1, true, true, -1, (short)0xB4C8);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the modified CRC-16/CCITT used by several floppy disk drives and other CRC-16 implementations.
	 * @return a new ParameterizedCRC object for computing the modified CRC-16/CCITT used by several floppy disk drives and other CRC-16 implementations.
	 */
	public static ParameterizedCRC createCRC16CCITT() {
		return new ParameterizedCRC(16, (short)0x1021, -1, false, false, 0, (short)0x29B1);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by the MCRF4XX series.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by the MCRF4XX series.
	 */
	public static ParameterizedCRC createCRC16MCRF4XX() {
		return new ParameterizedCRC(16, (short)0x1021, -1, true, true, 0, (short)0x6F91);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-16 used by MODICON Inc.'s Modbus.
	 * @return a new ParameterizedCRC object for computing the CRC-16 used by MODICON Inc.'s Modbus.
	 */
	public static ParameterizedCRC createCRC16MODBUS() {
		return new ParameterizedCRC(16, (short)0x8005, -1, true, true, 0, (short)0x4B37);
	}
	
	/**
	 * Creates a new ParameterizedCRC object for computing the CRC-32 of a data stream.
	 * By contract, this object should behave equivalently to a <code>java.util.zip.CRC32</code> object.
	 * @see java.util.zip.CRC32
	 * @return a new ParameterizedCRC object for computing the CRC-32 of a data stream.
	 */
	public static ParameterizedCRC createCRC32() {
		return new ParameterizedCRC(32, 0x04C11DB7, -1, true, true, -1, 0xCBF43926);
	}
	
	/**
	 * Creates a new ParameterizedCRC object.
	 * @param bitWidth the number of bits in the resulting CRC.
	 * @param polynomial the polynomial.
	 * @param initialValue the initial value of the register.
	 * @param reflectIn true if bytes should be read least significant bit first; false for most significant first.
	 * @param reflectOut true if the bits of the resulting CRC should be reversed.
	 * @param xorValue the value to XOR with the resulting CRC.
	 * @param check the expected CRC of the byte string 0x313233343536373839 (ASCII "123456789").
	 */
	public ParameterizedCRC(long bitWidth, long polynomial, long initialValue, boolean reflectIn, boolean reflectOut, long xorValue, long check) {
		this.bitWidth = bitWidth;
		this.polynomial = polynomial;
		this.initialValue = initialValue;
		this.reflectIn = reflectIn;
		this.reflectOut = reflectOut;
		this.xorValue = xorValue;
		this.check = check;
		
		this.lrshift = 64L-bitWidth;
		this.bshift = bitWidth-8L;
		this.msbmask = 1L << (bitWidth-1L);
		this.widmask = (1L << bitWidth)-1L;
		
		this.currValue = initialValue;
	}
	
	/**
	 * Returns the number of significant bits in the CRC value calculated by this object.
	 * @return the number of significant bits in the CRC value calculated by this object.
	 */
	public long getBitWidth() {
		return bitWidth;
	}
	
	/**
	 * Returns a mask of the significant bits in the CRC value calculated by this object.
	 * @return a mask of the significant bits in the CRC value calculated by this object.
	 */
	public long getBitMask() {
		return widmask;
	}
	
	/**
	 * Returns the polynomial.
	 * @return the polynomial.
	 */
	public long getPolynomial() {
		return polynomial;
	}
	
	/**
	 * Returns the initial value of the CRC register.
	 * @return the initial value of the CRC register.
	 */
	public long getInitialValue() {
		return initialValue;
	}
	
	/**
	 * Returns true if bytes are reflected in the input, false otherwise.
	 * @return true if bytes are reflected in the input, false otherwise.
	 */
	public boolean getReflectIn() {
		return reflectIn;
	}
	
	/**
	 * Returns true if the result is reflected on output, false otherwise.
	 * @return true if the result is reflected on output, false otherwise.
	 */
	public boolean getReflectOut() {
		return reflectOut;
	}
	
	/**
	 * Returns the value XORed with the result on output.
	 * @return the value XORed with the result on output.
	 */
	public long getOutputXOR() {
		return xorValue;
	}
	
	/**
	 * Returns the calculated CRC value.
	 * @return the calculated CRC value.
	 */
	public long getValue() {
		return ((reflectOut ? (Long.reverse(currValue) >>> lrshift) : currValue) ^ xorValue) & widmask;
	}
	
	/**
	 * Resets the CRC to its initial value.
	 */
	public void reset() {
		currValue = initialValue;
	}
	
	/**
	 * Updates the CRC with the specified byte.
	 * @param b the byte to update the CRC with.
	 */
	public void update(int b) {
		if (reflectIn) b = (Integer.reverse(b) >>> 24);
		currValue ^= ((long)b << bshift);
		for (int i = 0; i < 8; i++) {
			boolean xor = ((currValue & msbmask) != 0L);
			currValue <<= 1L;
			if (xor) currValue ^= polynomial;
		}
	}
	
	/**
	 * Updates the CRC with the specified array of bytes.
	 * @param b the array of bytes to update the CRC with.
	 */
	public void update(byte[] b) {
		for (byte bb : b) {
			if (reflectIn) bb = (byte)(Integer.reverse(bb) >>> 24);
			currValue ^= ((long)bb << bshift);
			for (int i = 0; i < 8; i++) {
				boolean xor = ((currValue & msbmask) != 0L);
				currValue <<= 1L;
				if (xor) currValue ^= polynomial;
			}
		}
	}
	
	/**
	 * Updates the CRC with the specified array of bytes.
	 * @param b the byte array to update the CRC with.
	 * @param off the start offset of the data.
	 * @param len the number of bytes to use for the update.
	 */
	public void update(byte[] b, int off, int len) {
		for (int i = off, l = 0; i < b.length && l < len; i++, l++) {
			int bb = b[i];
			if (reflectIn) bb = (Integer.reverse(bb) >>> 24);
			currValue ^= ((long)bb << bshift);
			for (int j = 0; j < 8; j++) {
				boolean xor = ((currValue & msbmask) != 0L);
				currValue <<= 1L;
				if (xor) currValue ^= polynomial;
			}
		}
	}
	
	/**
	 * Returns the expected CRC of the byte string 0x313233343536373839 (ASCII "123456789").
	 * Used almost like a CRC of the CRC algorithm itself (!) to verify that parameters
	 * have been specified correctly and the ParameterizedCRC class works correctly.
	 * If any ParameterizedCRC object does not conform to the contract
	 * <blockquote><code>calculatedCheckValue() == expectedCheckValue()</code></blockquote>
	 * either the parameters have been incorrectly specified,
	 * or there is a bug in the ParameterizedCRC class.
	 * @return the expected CRC of the byte string 0x313233343536373839 (ASCII "123456789").
	 */
	public long expectedCheckValue() {
		return check & widmask;
	}
	
	/**
	 * Returns the CRC of the byte string 0x313233343536373839 (ASCII "123456789") as actually calculated.
	 * Used almost like a CRC of the CRC algorithm itself (!) to verify that parameters
	 * have been specified correctly and the ParameterizedCRC class works correctly.
	 * If any ParameterizedCRC object does not conform to the contract
	 * <blockquote><code>calculatedCheckValue() == expectedCheckValue()</code></blockquote>
	 * either the parameters have been incorrectly specified,
	 * or there is a bug in the ParameterizedCRC class.
	 * @return the CRC of the byte string 0x313233343536373839 (ASCII "123456789") as actually calculated.
	 */
	public long calculatedCheckValue() {
		long save = currValue;
		reset();
		for (byte b = '1'; b <= '9'; b++) update(b);
		long calc = getValue();
		currValue = save;
		return calc;
	}
}
