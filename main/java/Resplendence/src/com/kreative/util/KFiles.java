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

import java.io.*;

/**
 * The <code>KFiles</code> class contains a variety of useful
 * static methods to be used on <code>File</code>s.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class KFiles {
	private KFiles() {
		//no constructor for you
	}
	
	/**
	 * Cuts a segment of data out of a random-access file.
	 * The two parts of the file before and after the cut
	 * are joined together. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>fileCut(f, 4, 3)</code> will return
	 * <br><br>
	 * <code>FF 2E 09</code>
	 * <br><br>
	 * and the new contents of the file will be
	 * <br><br>
	 * <code>05 23 75 A8 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset of the first byte of the cut.
	 * @param bytesToCut the number of bytes to cut.
	 * @return an array containing the cut bytes.
	 * @throws IOException if an I/O error occurs during the cut process.
	 */
	public static byte[] cut(RandomAccessFile f, long offset, int bytesToCut) throws IOException {
		if (bytesToCut <= 0) return new byte[0];
		byte[] stuff = new byte[bytesToCut];
		f.seek(offset);
		f.read(stuff);
		long l = f.length();
		byte[] junk = new byte[1048576];
		for (long s = offset+bytesToCut, d = offset; s < l; d += 1048576, s += 1048576) {
			f.seek(s);
			f.read(junk);
			f.seek(d);
			f.write(junk);
		}
		f.setLength(Math.max(l-bytesToCut,offset));
		return stuff;
	}
	
	/**
	 * Cuts a segment of data out of a random-access file.
	 * The two parts of the file before and after the cut
	 * are joined together. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>fileCut(f, 4L, 3L)</code> will return 3L
	 * and the new contents of the file will be
	 * <br><br>
	 * <code>05 23 75 A8 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset of the first byte of the cut.
	 * @param bytesToCut the number of bytes to cut.
	 * @return the number of bytes cut.
	 * @throws IOException if an I/O error occurs during the cut process.
	 */
	public static long cut(RandomAccessFile f, long offset, long bytesToCut) throws IOException {
		if (bytesToCut <= 0l) return 0l;
		long l = f.length();
		byte[] junk = new byte[1048576];
		for (long s = offset+bytesToCut, d = offset; s < l; d += 1048576, s += 1048576) {
			f.seek(s);
			f.read(junk);
			f.seek(d);
			f.write(junk);
		}
		f.setLength(Math.max(l-bytesToCut,offset));
		return Math.min(l-offset, bytesToCut);
	}
	
	/**
	 * Copies a segment of data from a random-access file.
	 * The file itself is not affected. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>fileCopy(f, 4, 3)</code> will return
	 * <br><br>
	 * <code>FF 2E 09</code>
	 * <br><br>
	 * and the contents of the file will not change.
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset of the first byte to copy.
	 * @param bytesToCopy the number of bytes to copy.
	 * @return an array containing the copied bytes.
	 * @throws IOException if an I/O error occurs during the copy process.
	 */
	public static byte[] copy(RandomAccessFile f, long offset, int bytesToCopy) throws IOException {
		if (bytesToCopy <= 0) return new byte[0];
		byte[] stuff = new byte[bytesToCopy];
		f.seek(offset);
		f.read(stuff);
		return stuff;
	}
	
	/**
	 * Pastes a segment of zero bytes into a random-access file.
	 * The bytes after the specified offset will be moved to
	 * later in the file. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>filePaste(f, 4, 3)</code> will return
	 * <br><br>
	 * <code>00 00 00</code>
	 * <br><br>
	 * and the new contents of the file will be
	 * <br><br>
	 * <code>05 23 75 A8 00 00 00 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset where the first byte of pasted data should be written.
	 * @param bytesToPaste the number of zero bytes to paste.
	 * @return an array containing the pasted bytes.
	 * @throws IOException if an I/O error occurs during the paste process.
	 */
	public static byte[] paste(RandomAccessFile f, long offset, int bytesToPaste) throws IOException {
		if (bytesToPaste <= 0) return new byte[0];
		byte[] stuff = new byte[bytesToPaste];
		long l = f.length();
		long btm = (l-offset-1) & (~0xFFFFFl);
		byte[] junk = new byte[1048576];
		for (long s = offset+btm, d = offset+btm+bytesToPaste; s >= offset; d -= 1048576, s -= 1048576) {
			f.seek(s);
			f.read(junk);
			f.seek(d);
			f.write(junk);
		}
		f.seek(offset);
		f.write(stuff);
		f.setLength(l+bytesToPaste);
		return stuff;
	}
	
	/**
	 * Pastes a segment of zero bytes into a random-access file.
	 * The bytes after the specified offset will be moved to
	 * later in the file. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>filePaste(f, 4L, 3L)</code> will return 3L
	 * and the new contents of the file will be
	 * <br><br>
	 * <code>05 23 75 A8 00 00 00 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset where the first byte of pasted data should be written.
	 * @param bytesToPaste the number of zero bytes to paste.
	 * @return the number of bytes pasted.
	 * @throws IOException if an I/O error occurs during the paste process.
	 */
	public static long paste(RandomAccessFile f, long offset, long bytesToPaste) throws IOException {
		if (bytesToPaste <= 0) return 0;
		long l = f.length();
		long btm = (l-offset-1) & (~0xFFFFFl);
		byte[] junk = new byte[1048576];
		for (long s = offset+btm, d = offset+btm+bytesToPaste; s >= offset; d -= 1048576, s -= 1048576) {
			f.seek(s);
			f.read(junk);
			f.seek(d);
			f.write(junk);
		}
		f.seek(offset);
		long w = bytesToPaste;
		byte[] stuff = new byte[1048576];
		while (w >= 1048576l) {
			f.write(stuff);
			w -= 1048576l;
		}
		if (w > 0) {
			stuff = new byte[(int)w];
			f.write(stuff);
		}
		f.setLength(l+bytesToPaste);
		return bytesToPaste;
	}
	
	/**
	 * Pastes a segment of data into a random-access file.
	 * The bytes after the specified offset will be moved to
	 * later in the file. For instance, if your file is
	 * <br><br>
	 * <code>05 23 75 A8 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * then <code>filePaste(f, 4, new byte[]{0x55, 0xEE, 0xB4})</code> will return
	 * <br><br>
	 * <code>55 EE B4</code>
	 * <br><br>
	 * and the new contents of the file will be
	 * <br><br>
	 * <code>05 23 75 A8 55 EE B4 FF 2E 09 DB 3C 01 A9 99 80</code>
	 * <br><br>
	 * Precondition: <code>offset</code> is between zero and the length of the file, inclusive.
	 * Other values may give unexpected results.
	 * @param f a <code>RandomAccessFile</code>.
	 * @param offset the offset where the first byte of pasted data should be written.
	 * @param stuff the data to paste.
	 * @return an array containing the pasted bytes.
	 * @throws IOException if an I/O error occurs during the paste process.
	 */
	public static byte[] paste(RandomAccessFile f, long offset, byte[] stuff) throws IOException {
		if (stuff.length <= 0) return new byte[0];
		long l = f.length();
		long btm = (l-offset-1) & (~0xFFFFFl);
		byte[] junk = new byte[1048576];
		for (long s = offset+btm, d = offset+btm+stuff.length; s >= offset; d -= 1048576, s -= 1048576) {
			f.seek(s);
			f.read(junk);
			f.seek(d);
			f.write(junk);
		}
		f.seek(offset);
		f.write(stuff);
		f.setLength(l+stuff.length);
		return stuff;
	}
}
