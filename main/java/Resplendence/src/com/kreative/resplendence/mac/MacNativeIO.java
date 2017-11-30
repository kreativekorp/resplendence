package com.kreative.resplendence.mac;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import com.kreative.ksfl.KSFLUtilities;
import quicktime.*;
import quicktime.app.view.*;
import quicktime.io.QTFile;
import quicktime.qd.*;
import quicktime.std.StdQTConstants4;
import quicktime.std.image.GraphicsExporter;
import glguerin.io.*;
import glguerin.util.*;

@SuppressWarnings("deprecation")
public class MacNativeIO {
	private MacNativeIO() {}
	
	public static void getAndWriteFinderInfo(File getFrom, RandomAccessFile writeTo) throws IOException {
		FileForker.SetFactory(MacPlatform.selectFactoryName(null));
		FileForker fkr = FileForker.MakeOne();
		fkr.usePathname(new Pathname(getFrom));
		FileInfo info = fkr.getFileInfo(false);
		writeTo.writeInt(info.getFileType());
		writeTo.writeInt(info.getFileCreator());
		writeTo.writeShort((short)(info.getFinderFlags() & 0xFFFF));
		SmallPoint p = info.getFinderIconAt();
		writeTo.writeShort(p.y);
		writeTo.writeShort(p.x);
		writeTo.writeShort(0); //parent dir
		writeTo.writeShort(0); //icon id
		writeTo.writeShort(0);
		writeTo.writeShort(0);
		writeTo.writeShort(0);
		writeTo.writeShort((short)((info.getFinderFlags() >>> 16) & 0xFFFF));
		writeTo.writeShort(0); //comment id
		writeTo.writeInt(0); //original parent id
	}
	
	public static void readAndSetFinderInfo(RandomAccessFile readFrom, File setTo) throws IOException {
		FileForker.SetFactory(MacPlatform.selectFactoryName(null));
		FileForker fkr = FileForker.MakeOne();
		fkr.usePathname(new Pathname(setTo));
		FileInfo info = fkr.getFileInfo(false);
		info.setFileType(readFrom.readInt());
		info.setFileCreator(readFrom.readInt());
		short fdFlags = readFrom.readShort();
		short iconY = readFrom.readShort();
		short iconX = readFrom.readShort();
		readFrom.skipBytes(10);
		short fdFlagsX = readFrom.readShort();
		info.setFinderIconAt(new SmallPoint(iconX, iconY));
		info.setFinderFlags((fdFlags & 0xFFFF) | ((fdFlagsX & 0xFFFF) << 16));
		fkr.setFileInfo(info);
	}
	
	public static String getFinderComment(File f) throws IOException {
		FileForker.SetFactory(MacPlatform.selectFactoryName(null));
		FileForker fkr = FileForker.MakeOne();
		fkr.usePathname(new Pathname(f));
		return fkr.getComment();
	}
	
	public static void setFinderComment(File f, String text) throws IOException {
		FileForker.SetFactory(MacPlatform.selectFactoryName(null));
		FileForker fkr = FileForker.MakeOne();
		fkr.usePathname(new Pathname(f));
		fkr.setComment(text);
	}
	
	public static Image readQuickTimePict(byte[] stuff) {
		try {
			File rt = File.createTempFile("readqt", ".png");
			if (stuff.length > 512 && KSFLUtilities.getLong(stuff, 2) == 0) {
				byte[] b = new byte[stuff.length-512];
				for (int s=512, d=0; s<stuff.length && d<b.length; s++, d++) b[d]=stuff[s];
				stuff = b;
			}
			QTSession.open();
			GraphicsExporter e = new GraphicsExporter(StdQTConstants4.kQTFileTypePNG);
			e.setInputPicture(new Pict(stuff));
			e.setOutputFile(new QTFile(rt));
			e.doExport();
			QTSession.close();
			Image im = ImageIO.read(rt);
			rt.delete();
			return im;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Image readQuickTimeImage(byte[] stuff) {
		try {
			File rt = File.createTempFile("readqt", "");
			RandomAccessFile raf = new RandomAccessFile(rt, "rwd");
			raf.setLength(0);
			raf.seek(0);
			raf.write(stuff);
			raf.close();
			QTSession.open();
			GraphicsImporterDrawer id = new GraphicsImporterDrawer(new QTFile(rt));
			QDRect r = id.getDisplayBounds();
			QTImageProducer ip = new QTImageProducer(id, new Dimension(r.getWidth(), r.getHeight()));
			Image im = Toolkit.getDefaultToolkit().createImage(ip);
			QTSession.close();
			rt.delete();
			return im;
		} catch (Exception e) {
			return null;
		}
	}
}
