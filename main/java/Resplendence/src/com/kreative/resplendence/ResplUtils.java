package com.kreative.resplendence;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.media.jai.*;
import javax.swing.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.misc.*;
import com.sun.media.jai.codec.*;

public class ResplUtils implements OSConstants {
	private ResplUtils() {
		// no constructor for you
	}
	
	private static final JFileChooser fchoos = new JFileChooser();
	public static Icon getFileIcon(File f, boolean largeSize) {
		if (largeSize) {
			Icon icon;
			try {
				sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(f);
				icon = new ImageIcon(sf.getIcon(true), sf.getFolderType());
			} catch (Exception e) {
				icon = fchoos.getIcon(f);
				BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				icon.paintIcon(null, bi.getGraphics(), 0, 0);
				AffineTransform tx = AffineTransform.getScaleInstance(2.0, 2.0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(
						bi.getSource(), new BufferedImageFilter(op)
				)));
			}
			return icon;
		} else {
			return fchoos.getIcon(f);
		}
	}

	public static Image shrink(Image im) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		while (!tk.prepareImage(im, -1, -1, null));
		AffineTransform tx = AffineTransform.getScaleInstance(0.5, 0.5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
		return tk.createImage(new FilteredImageSource(im.getSource(), new BufferedImageFilter(op)));
	}
	public static Image shrinkNow(Image im) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		while (!tk.prepareImage(im, -1, -1, null));
		AffineTransform tx = AffineTransform.getScaleInstance(0.5, 0.5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
		Image im2 = tk.createImage(new FilteredImageSource(im.getSource(), new BufferedImageFilter(op)));
		while (!tk.prepareImage(im2, -1, -1, null));
		return im2;
	}
	public static Image shrink(Toolkit tk, Image im) {
		while (!tk.prepareImage(im, -1, -1, null));
		AffineTransform tx = AffineTransform.getScaleInstance(0.5, 0.5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
		return tk.createImage(new FilteredImageSource(im.getSource(), new BufferedImageFilter(op)));
	}
	public static Image shrinkNow(Toolkit tk, Image im) {
		while (!tk.prepareImage(im, -1, -1, null));
		AffineTransform tx = AffineTransform.getScaleInstance(0.5, 0.5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
		Image im2 = tk.createImage(new FilteredImageSource(im.getSource(), new BufferedImageFilter(op)));
		while (!tk.prepareImage(im2, -1, -1, null));
		return im2;
	}
	
	public static Image createImage(File file) {
		try {
			Image i = Toolkit.getDefaultToolkit().createImage(file.toURI().toURL());
			if (i == null) {
				return createJAIImage(file.toURI().toURL());
			} else {
				long ms = System.currentTimeMillis()+100;
				while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null)) {
					if (System.currentTimeMillis() > ms) {
						return createJAIImage(file.toURI().toURL());
					}
				}
				return i;
			}
		} catch (MalformedURLException mue) {
			return null;
		}
	}
	public static Image createImage(URL url) {
		Image i = Toolkit.getDefaultToolkit().createImage(url);
		if (i == null) {
			return createJAIImage(url);
		} else {
			long ms = System.currentTimeMillis()+100;
			while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null)) {
				if (System.currentTimeMillis() > ms) {
					return createJAIImage(url);
				}
			}
			return i;
		}
	}
	public static Image createImage(byte[] data) {
		Image i = Toolkit.getDefaultToolkit().createImage(data);
		if (i == null) {
			return createJAIImage(data);
		} else {
			long ms = System.currentTimeMillis()+100;
			while (!Toolkit.getDefaultToolkit().prepareImage(i, -1, -1, null)) {
				if (System.currentTimeMillis() > ms) {
					return createJAIImage(data);
				}
			}
			return i;
		}
	}
	private static Image createJAIImage(URL url) {
		try {
			RenderedImage ri = JAI.create("url", url);
			Image i = PlanarImage.wrapRenderedImage(ri).getAsBufferedImage();
			return i;
		} catch (Exception e) {
			return null;
		}
	}
	private static Image createJAIImage(byte[] data) {
		try {
			SeekableStream stream = new ByteArraySeekableStream(data);
			String[] names = ImageCodec.getDecoderNames(stream);
			ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
			RenderedImage ri = dec.decodeAsRenderedImage();
			Image i = PlanarImage.wrapRenderedImage(ri).getAsBufferedImage();
			return i;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static InputEvent lastInputEvent = null;
	protected static void setTrap() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			public void eventDispatched(AWTEvent e) {
				if (e instanceof InputEvent) {
					lastInputEvent = (InputEvent)e;
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}
	public static int getModifiers() {
		if (lastInputEvent == null) return 0;
		else return lastInputEvent.getModifiers();
	}
	public static int getModifiersEx() {
		if (lastInputEvent == null) return 0;
		else return lastInputEvent.getModifiersEx();
	}
	public static InputEvent getLastInputEvent() {
		return lastInputEvent;
	}
	
	public static void setCancelButton(JComponent c, AbstractButton cancel) {
		c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
		c.getActionMap().put("cancel", new CancelAction(cancel));
	}
	private static class CancelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private AbstractButton cancel;
		public CancelAction(AbstractButton cancel) {
			this.cancel = cancel;
		}
		public void actionPerformed(ActionEvent ev) {
			cancel.doClick();
		}
	}
	
	public static void copyFile(File src, File dst) throws IOException {
		if (src.isDirectory()) {
			dst.mkdir();
			for (String n : src.list()) {
				copyFile(new File(src,n), new File(dst,n));
			}
		} else {
			InputStream in;
			OutputStream out;
			byte[] buf = new byte[16*1024*1024];
			int l;
			
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);
			while ((l = in.read(buf)) > 0) {
				out.write(buf, 0, l);
			}
			in.close();
			out.close();
			
			if (RUNNING_ON_MAC_OS) {
				File srcr = new File(new File(src, "..namedfork"), "rsrc");
				File dstr = new File(new File(dst, "..namedfork"), "rsrc");
				
				in = new FileInputStream(srcr);
				out = new FileOutputStream(dstr);
				while ((l = in.read(buf)) > 0) {
					out.write(buf, 0, l);
				}
				in.close();
				out.close();
				
				File f = File.createTempFile("finfTemp-", ".tmp");
				RandomAccessFile raf = new RandomAccessFile(f, "rwd");
				raf.seek(0);
				com.kreative.resplendence.mac.MacNativeIO.getAndWriteFinderInfo(src, raf);
				raf.seek(0);
				com.kreative.resplendence.mac.MacNativeIO.readAndSetFinderInfo(raf, dst);
				raf.close();
				f.delete();
				
				String cmt = com.kreative.resplendence.mac.MacNativeIO.getFinderComment(src);
				com.kreative.resplendence.mac.MacNativeIO.setFinderComment(dst, cmt);
			}
		}
	}
	
	private static final String[] B_LEVELS = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB" };
	private static final String[] M_LEVELS = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
	public static String describeSize(long l) {
		if (l < 0) return "--";
		int bLvl = 0;
		double bAmt = l;
		while (bAmt >= 1024.0) {
			bLvl++;
			bAmt /= 1024.0;
		}
		int mLvl = 0;
		double mAmt = l;
		while (mAmt >= 1000.0) {
			mLvl++;
			mAmt /= 1000.0;
		}
		return ((bLvl==0)?Integer.toString((int)bAmt):Double.toString(Math.round(bAmt*10.0)/10.0)) + B_LEVELS[bLvl]
		+ " / "
		+ ((mLvl==0)?Integer.toString((int)mAmt):Double.toString(Math.round(mAmt*10.0)/10.0)) + M_LEVELS[mLvl]
		+ ((l < 1000)?"":(" ("+l+"B)"));
	}
	
	
	private static boolean isPrivateUse(int i) {
		return (i < 0) || (i >= 0xE000 && i < 0xF900) || (i >= 0xF0000);
	}
	
	private static byte[] getUnicodeTable(String name) throws IOException {
		ByteArrayOutputStream table = new ByteArrayOutputStream();
		byte[] cbuf = new byte[65536];
		URL u;
		URLConnection uc;
		InputStream is;
		int cblen;
		java.util.List<String> urls = ResplPrefs.getStrings("UnicodeDBServers");
		if (urls == null || urls.isEmpty()) {
			urls = new java.util.Vector<String>();
			urls.add("http://www.unicode.org/Public/UNIDATA/");
			urls.add("http://www.kreativekorp.com/ucsur/UNIDATA/");
			ResplPrefs.setStrings("UnicodeDBServers", urls);
		}
		
		for (String url : urls) {
			u = new URL(url + "/" + name + ".txt");
			uc = u.openConnection();
			is = uc.getInputStream();
			while ((cblen = is.read(cbuf)) >= 0) {
				table.write(cbuf, 0, cblen);
			}
			is.close();
			table.write('\n');
			table.write('\n');
		}
		return table.toByteArray();
	}
	
	private static Map<Integer,String> uniBlockCache = null;
	public static Map<Integer,String> getUnicodeBlocks() {
		if (uniBlockCache == null) {
			DFFResourceProvider dp = ResplPrefs.getPrefsDFFResourceProvider();
			long now = new Date().getTime()/1000;
			boolean has = dp.contains(KSFLConstants.Text_Pln, 2);
			long then; if (has) try {
				then = Long.parseLong(dp.getAttributes(KSFLConstants.Text_Pln, 2).name);
			} catch (NumberFormatException nfe) { then = 0; } else { then = 0; }
			if ((now - then) > 604800) {
				try {
					byte[] table = getUnicodeTable("Blocks");
					if (has) dp.remove(KSFLConstants.Text_Pln, 2);
					DFFResource dr = new DFFResource(KSFLConstants.Text_Pln, 2, Long.toString(now), table);
					dp.add(dr);
				} catch (Exception e) {}
			}
			
			uniBlockCache = new HashMap<Integer,String>();
			uniBlockCache.put(0x000000, "Undefined");
			uniBlockCache.put(0x00E000, "Private Use Area");
			uniBlockCache.put(0x00F900, "Undefined");
			uniBlockCache.put(0x0F0000, "Private Use Area");
			uniBlockCache.put(0x110000, "End");
			byte[] data = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.Text_Pln, 2);
			Scanner sc = new Scanner(new ByteArrayInputStream(data));
			while (sc.hasNextLine()) {
				String s = sc.nextLine().trim();
				if (s.length() > 0 && s.charAt(0) != '#') {
					String[] p = s.split("\\.\\.|; ");
					if (p.length == 3) {
						int start = Integer.parseInt(p[0], 16);
						int end = Integer.parseInt(p[1], 16)+1;
						String name = p[2];
						uniBlockCache.put(start, name);
						if (!uniBlockCache.containsKey(end))
							uniBlockCache.put(end, isPrivateUse(end)?"Private Use Area":"Undefined");
					}
				}
			}
		}
		return uniBlockCache;
	}
	
	private static Map<Integer,String> uniNameCache = null;
	public static Map<Integer,String> getUnicodeNames() {
		if (uniNameCache == null) {
			DFFResourceProvider dp = ResplPrefs.getPrefsDFFResourceProvider();
			long now = new Date().getTime()/1000;
			boolean has = dp.contains(KSFLConstants.Text_Pln, 3);
			long then; if (has) try {
				then = Long.parseLong(dp.getAttributes(KSFLConstants.Text_Pln, 3).name);
			} catch (NumberFormatException nfe) { then = 0; } else { then = 0; }
			if ((now - then) > 604800) {
				try {
					byte[] table = getUnicodeTable("UnicodeData");
					if (has) dp.remove(KSFLConstants.Text_Pln, 3);
					DFFResource dr = new DFFResource(KSFLConstants.Text_Pln, 3, Long.toString(now), table);
					dp.add(dr);
				} catch (Exception e) {}
			}
			
			uniNameCache = new HashMap<Integer,String>();
			byte[] data = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.Text_Pln, 3);
			Scanner sc = new Scanner(new ByteArrayInputStream(data));
			while (sc.hasNextLine()) {
				String s = sc.nextLine().trim();
				if (s.length() > 0 && s.charAt(0) != '#') {
					String[] p = s.split(";");
					if (p.length >= 3) {
						int cp = Integer.parseInt(p[0], 16);
						String name = p[1];
						uniNameCache.put(cp, name);
					}
				}
			}
		}
		return uniNameCache;
	}
	
	private static Map<Integer,String> uniCategoryCache = null;
	public static Map<Integer,String> getUnicodeCategories() {
		if (uniCategoryCache == null) {
			DFFResourceProvider dp = ResplPrefs.getPrefsDFFResourceProvider();
			long now = new Date().getTime()/1000;
			boolean has = dp.contains(KSFLConstants.Text_Pln, 3);
			long then; if (has) try {
				then = Long.parseLong(dp.getAttributes(KSFLConstants.Text_Pln, 3).name);
			} catch (NumberFormatException nfe) { then = 0; } else { then = 0; }
			if ((now - then) > 604800) {
				try {
					byte[] table = getUnicodeTable("UnicodeData");
					if (has) dp.remove(KSFLConstants.Text_Pln, 3);
					DFFResource dr = new DFFResource(KSFLConstants.Text_Pln, 3, Long.toString(now), table);
					dp.add(dr);
				} catch (Exception e) {}
			}
			
			uniCategoryCache = new HashMap<Integer,String>();
			byte[] data = ResplRsrcs.getAppDFFResourceProvider().getData(KSFLConstants.Text_Pln, 3);
			Scanner sc = new Scanner(new ByteArrayInputStream(data));
			while (sc.hasNextLine()) {
				String s = sc.nextLine().trim();
				if (s.length() > 0 && s.charAt(0) != '#') {
					String[] p = s.split(";");
					if (p.length >= 3) {
						int cp = Integer.parseInt(p[0], 16);
						String cat = p[2];
						uniCategoryCache.put(cp, cat);
					}
				}
			}
		}
		return uniCategoryCache;
	}
	
	
	public static void sizeWindow(JFrame f) {
		Dimension screen = f.getToolkit().getScreenSize();
		Dimension window = f.getSize();
		boolean diff = false;
		if (window.width > screen.width) { window.width = screen.width; diff = true; }
		if (window.height > screen.height) { window.height = screen.height; diff = true; }
		if (diff) f.setSize(window);
	}
	
	public static void sizeWindow(JFrame f, int hm, int hd) {
		Dimension screen = f.getToolkit().getScreenSize();
		Dimension window = f.getSize();
		boolean diff = false;
		if (window.width > screen.width) { window.width = screen.width; diff = true; }
		if (window.height > screen.height*hm/hd) { window.height = screen.height*hm/hd; diff = true; }
		if (diff) f.setSize(window);
	}
	
	public static void sizeWindow(JFrame f, int wm, int wd, int hm, int hd) {
		Dimension screen = f.getToolkit().getScreenSize();
		Dimension window = f.getSize();
		boolean diff = false;
		if (window.width > screen.width*wm/wd) { window.width = screen.width*wm/wd; diff = true; }
		if (window.height > screen.height*hm/hd) { window.height = screen.height*hm/hd; diff = true; }
		if (diff) f.setSize(window);
	}
	
	
	
	
	private static final String[] LOOKUP_HEX = new String[] {
		"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
		"10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
		"20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
		"30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
		"40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
		"50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
		"60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
		"70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
		"80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
		"90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
		"A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
		"B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
		"C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
		"D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
		"E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
		"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"
	};
	
	public static String binaryToString(byte[] theData) {
		if (theData == null) return "";
		StringBuffer theString = new StringBuffer(theData.length*2);
		for (byte b : theData) theString.append(LOOKUP_HEX[b & 0xFF]);
		return theString.toString();
	}
}
