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
 * The <code>Base85</code> class is used for encoding and decoding
 * raw binary data (arrays of <code>byte</code>s) in Base85, an
 * encoding scheme using 85 plain-text characters. Base85 achieves
 * a 4:5 expansion ratio, as opposed to 1:2 for hex dump encoding
 * or 3:4 for Base64 encoding.<p>
 * The 85 characters used for Base85 encoding were carefully chosen
 * to minimize the number of escape characters needed when typing
 * a Base85-encoded string on the Unix/Linux command line or embedding
 * a Base85-encoded string in source code. In particular, all quotation
 * marks (" and '), slashes (/ and \), redirection symbols (&lt;, &gt;,
 * and |), wildcards (* and ?), and spaces were removed from the
 * character set. Only one character in the Base85 character set, the
 * colon (:), is an invalid file name character in FAT32 and NTFS, the
 * file systems with the most restrictive file name conventions
 * in use today; the colon can be replaced with the apostrophe if
 * Base85-encoded strings are to be used in file names. The redirection
 * symbols &lt; and &gt; are used only at the very beginning of the
 * Base85-encoded string to specify the length of the decoded string
 * as a Base85-encoded integer.
 * @since KJL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class Base85 {
	private Base85() {
		// this class has all static methods, so no constructor for you
	}
	
	private static final char[] BASE85A = {
		'!', '#', '$', '%', '&', '(', ')', '+', ',', '-', '.',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		':', ';', '=', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', ']',
		'^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
		'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '}', '~'
	};
	
	private static final String BASE85S = "!#$%&()+,-.0123456789:;=@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{}~";
	
	/**
	 * Encodes a single <code>int</code> into a five-character Base85 string.
	 * No length header is attached to the returned string.<p>
	 * This is the inverse operation of <code>decodeInt(String)</code>.
	 * @param i A 32-bit integer.
	 * @return A five-character Base85 string.
	 */
	public static String encode(int i) {
		char[] cc = new char[5];
		long j = i;
		j &= 0xFFFFFFFFl;
		cc[0] = BASE85A[(int)((j)%85)];
		cc[1] = BASE85A[(int)((j/85)%85)];
		cc[2] = BASE85A[(int)((j/7225)%85)];
		cc[3] = BASE85A[(int)((j/614125)%85)];
		cc[4] = BASE85A[(int)((j/52200625)%85)];
		return new String(cc);
	}
	
	/**
	 * Encodes an array of <code>int</code>s into a Base85 string.
	 * No length header is attached to the returned string.<p>
	 * This is the inverse operation of <code>decodeInts(String)</code>.
	 * @param ii An array of 32-bit integers.
	 * @return A Base85 string.
	 */
	public static String encode(int[] ii) {
		String s = "";
		int i;
		for (i=0; i<ii.length; i++) s+=encode(ii[i]);
		return s;
	}
	
	/**
	 * Encodes an array of <code>byte</code>s into a Base85 string.<p>
	 * Since the number of bytes does not need to be a multiple of four,
	 * a length header is attached to the beginning of the returned
	 * string. The length header contains the length of the decoded
	 * string, encoded in Base85, inside &lt; and &gt; symbols.<p>
	 * This is the inverse operation of <code>decode(String)</code>.
	 * @param b An array of bytes.
	 * @return A Base85 string.
	 */
	public static String encode(byte[] b) {
		String s = "<"+encode(b.length)+">";
		int i,v;
		for (i=0,v=0;i<b.length;i++) {
			v=((v>>8) & 0x00FFFFFF)|((b[i] & 0xFF)<<24);
			if ((i&3)==3) s+=encode(v);
		}
		if ((i&3)!=0) {
			while ((i&3)!=0) { i++; v>>=8; }
			s+=encode(v);
		}
		return s;
	}
	
	/**
	 * Encodes a <code>String</code> into a Base85 string, transforming
	 * the <code>String</code> into an array of bytes using the system's
	 * default text encoding.<p>
	 * Since the number of bytes does not need to be a multiple of four,
	 * a length header is attached to the beginning of the returned
	 * string. The length header contains the length of the decoded
	 * string, encoded in Base85, inside &lt; and &gt; symbols.<p>
	 * This is the inverse operation of <code>decodeString(String)</code>.
	 * @param s An ordinary string.
	 * @return A Base85 string.
	 */
	public static String encode(String s) {
		return encode(s.getBytes());
	}
	
	/**
	 * Encodes a <code>String</code> into a Base85 string, transforming
	 * the <code>String</code> into an array of bytes using the specified
	 * text encoding.<p>
	 * Since the number of bytes does not need to be a multiple of four,
	 * a length header is attached to the beginning of the returned
	 * string. The length header contains the length of the decoded
	 * string, encoded in Base85, inside &lt; and &gt; symbols.<p>
	 * This is the inverse operation of <code>decodeString(String)</code>.
	 * @param s An ordinary string.
	 * @param textEncoding The name of a text encoding to use to convert the string into binary data.
	 * @return A Base85 string.
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String encode(String s, String textEncoding) throws java.io.UnsupportedEncodingException {
		return encode(s.getBytes(textEncoding));
	}
	
	/**
	 * Takes a five-character Base85-encoded string and returns the
	 * integer encoded inside it. The Base85-encoded string must not
	 * contain a length header.<p>
	 * This is the inverse operation of <code>encode(int)</code>.
	 * @param s A Base85 string.
	 * @return The integer represented by the Base85 string.
	 */
	public static int decodeInt(String s) {
		return BASE85S.indexOf(s.charAt(0))+BASE85S.indexOf(s.charAt(1))*85+BASE85S.indexOf(s.charAt(2))*7225+BASE85S.indexOf(s.charAt(3))*614125+BASE85S.indexOf(s.charAt(4))*52200625;
	}
	
	/**
	 * Takes a Base85-encoded string and returns an array of integers.
	 * The Base85-encoded string may or may not contain a length header.<p>
	 * This is the inverse operation of <code>encode(int[])</code>.
	 * @param s A Base85 string.
	 * @return The array of integers represented by the Base85 string.
	 */
	public static int[] decodeInts(String s) {
		if (s == null || s.length() < 1 || (s.charAt(0)=='<' && s.length()<8)) return new int[0]; 
		int l = ((s.charAt(0)=='<')?(decodeInt(s.substring(1,6))):((s.length()/5)*4));
		String ws = ((s.charAt(0)=='<')?(s.substring(s.indexOf('>')+1)):s)+"!!!!!";
		int[] ii = new int[(l+3)/4];
		int i;
		for (i=0; i<(l+3)/4; i++) {
			ii[i] = decodeInt(ws.substring(i*5,i*5+5));
		}
		return ii;
	}
	
	/**
	 * Takes a Base85-encoded string and returns an array of bytes.
	 * The Base85-encoded string may or may not contain a length header.<p>
	 * This is the inverse operation of <code>encode(byte[])</code>.
	 * @param s A Base85 string.
	 * @return The array of bytes represented by the Base85 string.
	 */
	public static byte[] decode(String s) {
		if (s == null || s.length() < 1 || (s.charAt(0)=='<' && s.length()<8)) return new byte[0]; 
		int l = ((s.charAt(0)=='<')?(decodeInt(s.substring(1,6))):((s.length()/5)*4));
		String ws = ((s.charAt(0)=='<')?(s.substring(s.indexOf('>')+1)):s)+"!!!!!";
		byte[] ii = new byte[l];
		int i;
		for (i=0; i<((l+3)/4)-1; i++) {
			int v = decodeInt(ws.substring(i*5,i*5+5));
			ii[i<<2|0] = (byte)(v>> 0);
			ii[i<<2|1] = (byte)(v>> 8);
			ii[i<<2|2] = (byte)(v>>16);
			ii[i<<2|3] = (byte)(v>>24);
		}
		{
			int v = decodeInt(ws.substring(i*5,i*5+5));
			if ((i<<2|0) < l) ii[i<<2|0] = (byte)(v>> 0);
			if ((i<<2|1) < l) ii[i<<2|1] = (byte)(v>> 8);
			if ((i<<2|2) < l) ii[i<<2|2] = (byte)(v>>16);
			if ((i<<2|3) < l) ii[i<<2|3] = (byte)(v>>24);
		}
		return ii;
	}
	
	/**
	 * Takes a Base85-encoded string and returns an ordinary string,
	 * converted from an array of bytes using the system's default
	 * text encoding. The Base85-encoded string may or may not contain
	 * a length header. If it doesn't, the length of the decoded string
	 * may be inaccurate.<p>
	 * This is the inverse operation of <code>encode(String)</code>.
	 * @param s A Base85 string.
	 * @return The ordinary string represented by the Base85 string in the default text encoding.
	 */
	public static String decodeString(String s) {
		if (s == null || s.length() < 1 || (s.charAt(0)=='<' && s.length()<8)) return ""; 
		int l = ((s.charAt(0)=='<')?(decodeInt(s.substring(1,6))):((s.length()/5)*4));
		String ws = ((s.charAt(0)=='<')?(s.substring(s.indexOf('>')+1)):s)+"!!!!!";
		byte[] ii = new byte[l];
		int i;
		for (i=0; i<((l+3)/4)-1; i++) {
			int v = decodeInt(ws.substring(i*5,i*5+5));
			ii[i<<2|0] = (byte)((v>> 0)&0xFF);
			ii[i<<2|1] = (byte)((v>> 8)&0xFF);
			ii[i<<2|2] = (byte)((v>>16)&0xFF);
			ii[i<<2|3] = (byte)((v>>24)&0xFF);
		}
		{
			int v = decodeInt(ws.substring(i*5,i*5+5));
			if ((i<<2|0) < l) ii[i<<2|0] = (byte)((v>> 0)&0xFF);
			if ((i<<2|1) < l) ii[i<<2|1] = (byte)((v>> 8)&0xFF);
			if ((i<<2|2) < l) ii[i<<2|2] = (byte)((v>>16)&0xFF);
			if ((i<<2|3) < l) ii[i<<2|3] = (byte)((v>>24)&0xFF);
		}
		return new String(ii);
	}
	
	/**
	 * Takes a Base85-encoded string and returns an ordinary string,
	 * converted from an array of bytes using the specified
	 * text encoding. The Base85-encoded string may or may not contain
	 * a length header. If it doesn't, the length of the decoded string
	 * may be inaccurate.<p>
	 * This is the inverse operation of <code>encode(String)</code>.
	 * @param s A Base85 string.
	 * @param textEncoding The name of a text encoding to use to convert the binary data into a string.
	 * @return The ordinary string represented by the Base85 string in the specified text encoding.
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String decodeString(String s, String textEncoding) throws java.io.UnsupportedEncodingException {
		if (s == null || s.length() < 1 || (s.charAt(0)=='<' && s.length()<8)) return ""; 
		int l = ((s.charAt(0)=='<')?(decodeInt(s.substring(1,6))):((s.length()/5)*4));
		String ws = ((s.charAt(0)=='<')?(s.substring(s.indexOf('>')+1)):s)+"!!!!!";
		byte[] ii = new byte[l];
		int i;
		for (i=0; i<((l+3)/4)-1; i++) {
			int v = decodeInt(ws.substring(i*5,i*5+5));
			ii[i<<2|0] = (byte)((v>> 0)&0xFF);
			ii[i<<2|1] = (byte)((v>> 8)&0xFF);
			ii[i<<2|2] = (byte)((v>>16)&0xFF);
			ii[i<<2|3] = (byte)((v>>24)&0xFF);
		}
		{
			int v = decodeInt(ws.substring(i*5,i*5+5));
			if ((i<<2|0) < l) ii[i<<2|0] = (byte)((v>> 0)&0xFF);
			if ((i<<2|1) < l) ii[i<<2|1] = (byte)((v>> 8)&0xFF);
			if ((i<<2|2) < l) ii[i<<2|2] = (byte)((v>>16)&0xFF);
			if ((i<<2|3) < l) ii[i<<2|3] = (byte)((v>>24)&0xFF);
		}
		return new String(ii, textEncoding);
	}
}
