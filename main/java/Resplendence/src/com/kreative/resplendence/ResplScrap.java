package com.kreative.resplendence;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import com.kreative.cff.*;
import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.prc.*;
import com.kreative.rsrc.*;
import com.kreative.paint.datatransfer.*;

public class ResplScrap implements ClipboardOwner {
	private static ResplScrap instance = new ResplScrap();
	private ResplScrap() {}
	public void lostOwnership(Clipboard clipboard, Transferable contents) {}
	
	private static boolean allAreFiles(Collection<?> l) {
		if (l == null || l.size() < 1) return false;
		for (Object o : l) if (!(o instanceof File)) return false;
		return true;
	}
	
	public static void setScrap(Transferable t) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(t, instance);
	}
	
	public static void setScrap(String s) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new StringSelection(s), instance);
	}
	
	public static void setScrap(Image i) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new ImageSelection(i), instance);
	}
	
	public static void setScrap(byte[] data) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new ResplendenceSelection(data), instance);
	}
	
	public static void setScrap(File f) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new FileListSelection(f), instance);
	}
	
	public static void setScrap(Collection<?> c) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (allAreFiles(c)) {
			Collection<File> f = new Vector<File>();
			for (Object oo : c) f.add((File)oo);
			cb.setContents(new FileListSelection(f), instance);
		} else {
			cb.setContents(new ResplendenceSelection(c), instance);
		}
	}
	
	public static void setScrap(Object[] o) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new ResplendenceSelection(o), instance);
	}
	
	public static void setScrap(Object o) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (o instanceof Transferable) cb.setContents((Transferable)o, instance);
		else if (o instanceof String) cb.setContents(new StringSelection((String)o), instance);
		else if (o instanceof Image) cb.setContents(new ImageSelection((Image)o), instance);
		else if (o instanceof byte[]) cb.setContents(new ResplendenceSelection(o), instance);
		else if (o instanceof File) cb.setContents(new FileListSelection((File)o), instance);
		else if (o instanceof Collection) {
			if (allAreFiles((Collection<?>)o)) {
				Collection<File> f = new Vector<File>();
				for (Object oo : (Collection<?>)o) f.add((File)oo);
				cb.setContents(new FileListSelection(f), instance);
			} else {
				cb.setContents(new ResplendenceSelection((Collection<?>)o), instance);
			}
		}
		else if (o instanceof Object[]) cb.setContents(new ResplendenceSelection((Object[])o), instance);
		else cb.setContents(new ResplendenceSelection(o), instance);
	}
	
	private static byte[] gb(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException uee) {
			return s.getBytes();
		}
	}
	
	private static byte[] gbw(String s) {
		try {
			return s.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException uee) {
			return s.getBytes();
		}
	}
	
	private static byte[] imageToBytes(Image i) {
		RenderedImage ri;
		if (i instanceof RenderedImage) ri = (RenderedImage)i;
		else {
			BufferedImage bi = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			bi.getGraphics().drawImage(i, 0, 0, null);
			ri = bi;
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(ri, "png", os);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return os.toByteArray();
	}
	
	private static byte[] serialize(Serializable object) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
			ObjectOutput out = new ObjectOutputStream(bos) ;
			out.writeObject(object);
			out.close();
			return bos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static Transferable getScrapTransferable() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		return cb.getContents(null);
	}
	
	public static String getScrapString() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try {
				return (String)cb.getData(DataFlavor.stringFlavor);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static Image getScrapImage() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
			try {
				return (Image)cb.getData(DataFlavor.imageFlavor);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static byte[] getScrapData() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(resplendenceFlavor)) {
			try {
				Collection<?> c = (Collection<?>)cb.getData(resplendenceFlavor);
				for (Object o : c) if (o instanceof byte[]) return (byte[])o;
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try {
				String s = (String)cb.getData(DataFlavor.stringFlavor);
				return gb(s);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static Collection<File> getScrapFiles() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(resplendenceFlavor)) {
			try {
				Collection<File> v = new Vector<File>();
				Collection<?> c = (Collection<?>)cb.getData(resplendenceFlavor);
				for (Object o : c) if (o instanceof File) v.add((File)o);
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
			try {
				Collection<File> v = new Vector<File>();
				Collection<?> c = (Collection<?>)cb.getData(DataFlavor.javaFileListFlavor);
				for (Object o : c) v.add((File)o);
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static Object[] getScrapArray() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(resplendenceFlavor)) {
			try {
				Collection<?> c = (Collection<?>)cb.getData(resplendenceFlavor);
				return c.toArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
			try {
				Collection<?> c = (Collection<?>)cb.getData(DataFlavor.javaFileListFlavor);
				return c.toArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
			try {
				Image i = (Image)cb.getData(DataFlavor.imageFlavor);
				return new Object[]{i};
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try {
				String s = (String)cb.getData(DataFlavor.stringFlavor);
				return new Object[]{s};
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static Collection<?> getScrapCollection() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (cb.isDataFlavorAvailable(resplendenceFlavor)) {
			try {
				return (Collection<?>)cb.getData(resplendenceFlavor);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
			try {
				return (Collection<?>)cb.getData(DataFlavor.javaFileListFlavor);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
			try {
				Vector<Image> v = new Vector<Image>();
				Image i = (Image)cb.getData(DataFlavor.imageFlavor);
				v.add(i); return v;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try {
				Vector<String> v = new Vector<String>();
				String s = (String)cb.getData(DataFlavor.stringFlavor);
				v.add(s); return v;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public static Collection<DFFResource> getScrapDFFResources(DFFResourceProvider dp) {
		int stringId = (dp == null) ? 0 : dp.getNextAvailableID(KSFLConstants.TextUTF8);
		int imageId = (dp == null) ? 0 : dp.getNextAvailableID(KSFLConstants.ImagePNG);
		int dataId = (dp == null) ? 0 : dp.getNextAvailableID(KSFLConstants.Data_Bin);
		int objectId = (dp == null) ? 0 : dp.getNextAvailableID(KSFLConstants.Java_Obj);
		Vector<DFFResource> v = new Vector<DFFResource>();
		Collection<?> scrap = getScrapCollection();
		if (scrap != null) for (Object o : scrap) {
			if (o instanceof DFFResource) v.add((DFFResource)o);
			else if (o instanceof MacResource) v.add(KSFLConverter.makeDFFResourceFromMacResource((MacResource)o));
			else if (o instanceof PalmResource) v.add(KSFLConverter.makeDFFResourceFromPalmResource((PalmResource)o));
			else if (o instanceof Chunk) {
				Header h = ((Chunk)o).getHeader();
				byte[] d = ((Chunk)o).getData();
				long l = (h.containsKey(FieldType.CHARACTER_TYPE) ?
						h.get(FieldType.CHARACTER_TYPE).longValue() :
							h.containsKey(FieldType.INTEGER_TYPE) ?
									h.get(FieldType.INTEGER_TYPE).longValue() :
										KSFLConstants.Chunk);
				while ((l & 0xFF00000000000000l) == 0 || (l & 0xFF00000000000000l) == 0xFF00000000000000l) {
					l = (l << 8) | 0x20;
				}
				int id = (h.containsKey(FieldType.ID_NUMBER) ?
						h.get(FieldType.ID_NUMBER).intValue() :
							dp.getNextAvailableID(l));
				v.add(new DFFResource(l, id, d));
			}
			else if (o instanceof String) v.add(new DFFResource(KSFLConstants.TextUTF8, stringId++, gb((String)o)));
			else if (o instanceof Image) v.add(new DFFResource(KSFLConstants.ImagePNG, imageId++, imageToBytes((Image)o)));
			else if (o instanceof byte[]) v.add(new DFFResource(KSFLConstants.Data_Bin, dataId++, (byte[])o));
			else if (o instanceof Serializable) v.add(new DFFResource(KSFLConstants.Java_Obj, objectId++, serialize((Serializable)o)));
		}
		return v;
	}
	
	public static Collection<MacResource> getScrapResources(MacResourceProvider rp) {
		short stringId = (rp == null) ? 128 : rp.getNextAvailableID(KSFLConstants.utxt);
		short imageId = (rp == null) ? 128 : rp.getNextAvailableID(KSFLConstants.PNG);
		short dataId = (rp == null) ? 128 : rp.getNextAvailableID(KSFLConstants.DATA);
		short objectId = (rp == null) ? 128 : rp.getNextAvailableID(KSFLConstants.jObj);
		Vector<MacResource> v = new Vector<MacResource>();
		Collection<?> scrap = getScrapCollection();
		if (scrap != null) for (Object o : scrap) {
			if (o instanceof MacResource) v.add((MacResource)o);
			else if (o instanceof DFFResource) v.add(KSFLConverter.makeMacResourceFromDFFResource((DFFResource)o));
			else if (o instanceof PalmResource) v.add(KSFLConverter.makeMacResourceFromPalmResource((PalmResource)o));
			else if (o instanceof Chunk) {
				Header h = ((Chunk)o).getHeader();
				byte[] d = ((Chunk)o).getData();
				int l = (h.containsKey(FieldType.CHARACTER_TYPE) ?
						h.get(FieldType.CHARACTER_TYPE).intValue() :
							h.containsKey(FieldType.INTEGER_TYPE) ?
									h.get(FieldType.INTEGER_TYPE).intValue() :
										KSFLConstants.Chnk);
				while ((l & 0xFF000000) == 0 || (l & 0xFF000000) == 0xFF000000) {
					l = (l << 8) | 0x20;
				}
				short id = (h.containsKey(FieldType.ID_NUMBER) ?
						h.get(FieldType.ID_NUMBER).shortValue() :
							rp.getNextAvailableID(l));
				v.add(new MacResource(l, id, d));
			}
			else if (o instanceof String) v.add(new MacResource(KSFLConstants.utxt, stringId++, gbw((String)o)));
			else if (o instanceof Image) v.add(new MacResource(KSFLConstants.PNG, imageId++, imageToBytes((Image)o)));
			else if (o instanceof byte[]) v.add(new MacResource(KSFLConstants.DATA, dataId++, (byte[])o));
			else if (o instanceof Serializable) v.add(new MacResource(KSFLConstants.jObj, objectId++, serialize((Serializable)o)));
		}
		return v;
	}
	
	public static Collection<PalmResource> getScrapPalmResources(PalmResourceProvider rp) {
		short stringId = (rp == null) ? 0 : rp.getNextAvailableID(KSFLConstants.utxt);
		short imageId = (rp == null) ? 0 : rp.getNextAvailableID(KSFLConstants.PNG);
		short dataId = (rp == null) ? 0 : rp.getNextAvailableID(KSFLConstants.DATA);
		short objectId = (rp == null) ? 0 : rp.getNextAvailableID(KSFLConstants.jObj);
		Vector<PalmResource> v = new Vector<PalmResource>();
		Collection<?> scrap = getScrapCollection();
		if (scrap != null) for (Object o : scrap) {
			if (o instanceof PalmResource) v.add((PalmResource)o);
			else if (o instanceof DFFResource) v.add(KSFLConverter.makePalmResourceFromDFFResource((DFFResource)o));
			else if (o instanceof MacResource) v.add(KSFLConverter.makePalmResourceFromMacResource((MacResource)o));
			else if (o instanceof Chunk) {
				Header h = ((Chunk)o).getHeader();
				byte[] d = ((Chunk)o).getData();
				int l = (h.containsKey(FieldType.CHARACTER_TYPE) ?
						h.get(FieldType.CHARACTER_TYPE).intValue() :
							h.containsKey(FieldType.INTEGER_TYPE) ?
									h.get(FieldType.INTEGER_TYPE).intValue() :
										KSFLConstants.Chnk);
				while ((l & 0xFF000000) == 0 || (l & 0xFF000000) == 0xFF000000) {
					l = (l << 8) | 0x20;
				}
				short id = (h.containsKey(FieldType.ID_NUMBER) ?
						h.get(FieldType.ID_NUMBER).shortValue() :
							rp.getNextAvailableID(l));
				v.add(new PalmResource(l, id, d));
			}
			else if (o instanceof String) v.add(new PalmResource(KSFLConstants.utxt, stringId++, gbw((String)o)));
			else if (o instanceof Image) v.add(new PalmResource(KSFLConstants.PNG, imageId++, imageToBytes((Image)o)));
			else if (o instanceof byte[]) v.add(new PalmResource(KSFLConstants.DATA, dataId++, (byte[])o));
			else if (o instanceof Serializable) v.add(new PalmResource(KSFLConstants.jObj, objectId++, serialize((Serializable)o)));
		}
		return v;
	}
	
	public static Collection<Chunk> getScrapChunks(ChunkFileSpec spec) {
		Vector<Chunk> v = new Vector<Chunk>();
		Collection<?> scrap = getScrapCollection();
		if (scrap != null) for (Object o : scrap) {
			if (o instanceof Chunk) {
				Chunk ch = (Chunk)o;
				for (FieldSpec fs : spec.chunkHeaderSpec()) {
					if (!ch.getHeader().containsKey(fs.type())) {
						switch (fs.type()) {
						case SIZE_WITHOUT_HEADER:
							ch.getHeader().put(fs.type(), ch.getData().length);
							break;
						case SIZE_WITH_HEADER:
							ch.getHeader().put(fs.type(), ch.getData().length+spec.chunkHeaderSpec().byteCount());
							break;
						case DATA:
							break;
						default:
							ch.getHeader().put(fs.type(), 0L);
							break;
						}
					}
				}
				v.add(ch);
			}
			else if (o instanceof DFFResource) {
				DFFResource d = (DFFResource)o;
				Header h = new Header();
				Chunk ch = new Chunk(h, d.data);
				for (FieldSpec fs : spec.chunkHeaderSpec()) {
					switch (fs.type()) {
					case INTEGER_TYPE:
					case CHARACTER_TYPE:
						h.put(fs.type(), d.type);
						break;
					case ID_NUMBER:
						h.put(fs.type(), d.id);
						break;
					case SIZE_WITHOUT_HEADER:
						h.put(fs.type(), d.data.length);
						break;
					case SIZE_WITH_HEADER:
						h.put(fs.type(), d.data.length+spec.chunkHeaderSpec().byteCount());
						break;
					case DATA:
						break;
					default:
						h.put(fs.type(), 0);
						break;
					}
				}
				v.add(ch);
			}
			else if (o instanceof MacResource) {
				MacResource r = (MacResource)o;
				Header h = new Header();
				Chunk ch = new Chunk(h, r.data);
				for (FieldSpec fs : spec.chunkHeaderSpec()) {
					switch (fs.type()) {
					case INTEGER_TYPE:
					case CHARACTER_TYPE:
						h.put(fs.type(), r.type);
						break;
					case ID_NUMBER:
						h.put(fs.type(), r.id);
						break;
					case SIZE_WITHOUT_HEADER:
						h.put(fs.type(), r.data.length);
						break;
					case SIZE_WITH_HEADER:
						h.put(fs.type(), r.data.length+spec.chunkHeaderSpec().byteCount());
						break;
					case DATA:
						break;
					default:
						h.put(fs.type(), 0);
						break;
					}
				}
				v.add(ch);
			}
			else if (o instanceof PalmResource) {
				PalmResource r = (PalmResource)o;
				Header h = new Header();
				Chunk ch = new Chunk(h, r.data);
				for (FieldSpec fs : spec.chunkHeaderSpec()) {
					switch (fs.type()) {
					case INTEGER_TYPE:
					case CHARACTER_TYPE:
						h.put(fs.type(), r.type);
						break;
					case ID_NUMBER:
						h.put(fs.type(), r.id);
						break;
					case SIZE_WITHOUT_HEADER:
						h.put(fs.type(), r.data.length);
						break;
					case SIZE_WITH_HEADER:
						h.put(fs.type(), r.data.length+spec.chunkHeaderSpec().byteCount());
						break;
					case DATA:
						break;
					default:
						h.put(fs.type(), 0);
						break;
					}
				}
				v.add(ch);
			}
			else {
				byte[] d = null;
				if (o instanceof String) d = gbw((String)o);
				else if (o instanceof Image) d = imageToBytes((Image)o);
				else if (o instanceof byte[]) d = (byte[])o;
				else if (o instanceof Serializable) d = serialize((Serializable)o);
				if (d != null) {
					Header h = new Header();
					Chunk ch = new Chunk(h, d);
					for (FieldSpec fs : spec.chunkHeaderSpec()) {
						switch (fs.type()) {
						case INTEGER_TYPE:
						case CHARACTER_TYPE:
							h.put(fs.type(), (fs.size().byteCount() > 4) ? KSFLConstants.TextUTF8 : KSFLConstants.TEXT);
							break;
						case SIZE_WITHOUT_HEADER:
							h.put(fs.type(), d.length);
							break;
						case SIZE_WITH_HEADER:
							h.put(fs.type(), d.length+spec.chunkHeaderSpec().byteCount());
							break;
						case DATA:
							break;
						default:
							h.put(fs.type(), 0);
							break;
						}
					}
					v.add(ch);
				}
			}
		}
		return v;
	}
	
	
	private static final DataFlavor resplendenceFlavor = new DataFlavor(Collection.class, "application/x-java-resplendence-objects");
	
	private static class ResplendenceSelection implements ClipboardOwner, Transferable {
		private Collection<?> things;
		
		public ResplendenceSelection(Object o) {
			Vector<Object> v = new Vector<Object>();
			v.add(o); things = v;
		}
		
		public ResplendenceSelection(Object[] o) {
			Vector<Object> v = new Vector<Object>();
			v.addAll(Arrays.asList(o)); things = v;
		}
		
		public ResplendenceSelection(Collection<?> o) {
			Vector<Object> v = new Vector<Object>();
			v.addAll(o); things = v;
		}
		
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (resplendenceFlavor.equals(flavor)) {
				return things;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{resplendenceFlavor};
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return (resplendenceFlavor.equals(flavor));
		}

		public void lostOwnership(Clipboard clipboard, Transferable contents) {
			//nothing
		}
	}
}
