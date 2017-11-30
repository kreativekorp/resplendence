package com.kreative.resplendence;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

import com.kreative.dff.*;
import com.kreative.ksfl.*;
import com.kreative.resplendence.acc.*;
import com.kreative.resplendence.datafilter.*;
import com.kreative.resplendence.editors.*;
import com.kreative.resplendence.filecodec.*;
import com.kreative.resplendence.menus.*;
import com.kreative.resplendence.misc.*;
import com.kreative.resplendence.pickers.*;
import com.kreative.resplendence.textfilter.*;

public class ResplMain implements OSConstants, MenuConstants {
	public static void main(String[] args) {
		final Vector<File> f = new Vector<File>();
		for (String arg : args) {
			if (arg.equals("--safemode")) {
				System.setProperty("com.kreative.resplendence.safemode", "true");
			} else {
				f.add(new File(arg));
			}
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				resplInit(f.toArray(new File[0]));
			}
		});
	}
	
	private ResplMain() {
		//static only
		//no constructor for you
	};
	
	public static boolean safeMode() {
		String s = System.getProperty("com.kreative.resplendence.safemode");
		return (s != null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("1")));
	}
	
	// RESPLENDENCE LISTENERS
	private static EventListenerList myListeners = new EventListenerList();
	private static ResplendenceListener frontmost = null;
	
	public static void addResplendenceListener(ResplendenceListener l) {
		myListeners.add(ResplendenceListener.class, l);
		Object o = l;
		while (o != null) {
			if (o instanceof Window) {
				Window w = (Window)o;
				w.addWindowListener(new FrontmostMonitor(l));
				break;
			} else if (o instanceof Component) {
				o = ((Component)o).getParent();
			} else {
				break;
			}
		}
	}
	
	public static void addResplendenceListener(ResplendenceListener l, Window w) {
		myListeners.add(ResplendenceListener.class, l);
		w.addWindowListener(new FrontmostMonitor(l));
	}
	
	public static void removeResplendenceListener(ResplendenceListener l) {
		myListeners.remove(ResplendenceListener.class, l);
	}
	
	public static ResplendenceListener[] getResplendenceListeners() {
		return myListeners.getListeners(ResplendenceListener.class);
	}
	
	public static ResplendenceListener getFrontmostResplendenceListener() {
		return frontmost;
	}
	
	public static void setFrontmostResplendenceListener(ResplendenceListener l) {
		frontmost = l;
	}
	
	public static void addWindowListener(WindowListener l) {
		myListeners.add(WindowListener.class, l);
	}
	
	public static void removeWindowListener(WindowListener l) {
		myListeners.remove(WindowListener.class, l);
	}
	
	public static WindowListener[] getWindowListeners() {
		return myListeners.getListeners(WindowListener.class);
	}
	
	public static Object[] sendResplendenceEventToAll(ResplendenceEvent e) {
		ResplendenceListener[] rl = myListeners.getListeners(ResplendenceListener.class);
		Object[] responses = new Object[rl.length];
		for (int i=0; i<rl.length; i++) {
			responses[i] = rl[i].respondToResplendenceEvent(e);
		}
		return responses;
	}
	
	public static Object sendResplendenceEventToFrontmost(ResplendenceEvent e) {
		if (frontmost != null) {
			return frontmost.respondToResplendenceEvent(e);
		} else {
			return null;
		}
	}
	
	public static Object sendResplendenceEventTo(ResplendenceListener l, ResplendenceEvent e) {
		if (l != null) {
			return l.respondToResplendenceEvent(e);
		} else {
			return null;
		}
	}
	
	private static void forwardWindowEvent(int method, WindowEvent e) {
		WindowListener[] wl = myListeners.getListeners(WindowListener.class);
		switch (method) {
		case 1: for (WindowListener w : wl) w.windowActivated(e); break;
		case 2: for (WindowListener w : wl) w.windowClosed(e); break;
		case 3: for (WindowListener w : wl) w.windowClosing(e); break;
		case 4: for (WindowListener w : wl) w.windowDeactivated(e); break;
		case 5: for (WindowListener w : wl) w.windowDeiconified(e); break;
		case 6: for (WindowListener w : wl) w.windowIconified(e); break;
		case 7: for (WindowListener w : wl) w.windowOpened(e); break;
		}
	}
	
	private static class FrontmostMonitor implements WindowListener {
		private ResplendenceListener l;
		public FrontmostMonitor(ResplendenceListener l) {
			this.l = l;
		}
		public void windowActivated(WindowEvent e) {
			frontmost = l;
			forwardWindowEvent(1,e);
		}
		public void windowClosed(WindowEvent e) {
			if (frontmost == l) frontmost = null;
			forwardWindowEvent(2,e);
		}
		public void windowClosing(WindowEvent e) {
			if (frontmost == l) frontmost = null;
			forwardWindowEvent(3,e);
		}
		public void windowDeactivated(WindowEvent e) {
			if (frontmost == l) frontmost = null;
			forwardWindowEvent(4,e);
		}
		public void windowDeiconified(WindowEvent e) {
			forwardWindowEvent(5,e);
		}
		public void windowIconified(WindowEvent e) {
			forwardWindowEvent(6,e);
		}
		public void windowOpened(WindowEvent e) {
			frontmost = l;
			forwardWindowEvent(7,e);
		}
	}
	
	
	// MENUS
	private static ArrayList<Window> windowRegistry = new ArrayList<Window>();
	private static WindowRegistryListener windowRegistryListener = new WindowRegistryListener();
	private static Map<Window,FileMenu> fileMenuRegistry = new HashMap<Window,FileMenu>();
	private static Map<Window,WindowMenu> windowMenuRegistry = new HashMap<Window,WindowMenu>();
	
	public static void registerWindow(JFrame w) {
		w.setJMenuBar(getMenuBar(w,0,null));
		w.addWindowListener(windowRegistryListener);
	}
	
	public static void registerWindow(JFrame w, long menuFeatures) {
		w.setJMenuBar(getMenuBar(w,menuFeatures,null));
		w.addWindowListener(windowRegistryListener);
	}
	
	public static void registerWindow(JFrame w, long menuFeatures, JMenu[] otherMenus) {
		w.setJMenuBar(getMenuBar(w,menuFeatures,otherMenus));
		w.addWindowListener(windowRegistryListener);
	}
	
	public static Window[] getWindows() {
		List<Window> wr = new ArrayList<Window>();
		for (Window w : windowRegistry) {
			if (w.isVisible()) wr.add(w);				
		}
		return wr.toArray(new Window[0]);
	}
	
	public static Window getTopmostWindow() {
		for (Window w : windowRegistry) {
			if (w.isVisible()) return w;			
		}
		return null;
	}
	
	private static JMenuBar getMenuBar(Window w, long features, JMenu[] extra) {
		ResplendenceMenuBar mb = new ResplendenceMenuBar(features, extra);
		if (mb == null || mb.getMenuCount() == 0) return null;
		FileMenu fm = mb.getFileMenu();
		if (fm != null) fileMenuRegistry.put(w, fm);
		WindowMenu wm = mb.getWindowMenu();
		if (wm != null) windowMenuRegistry.put(w, wm);
		return mb;
	}
	
	public static void updateFileMenu() {
		for (FileMenu m : ResplMain.fileMenuRegistry.values()) m.update();
	}
	
	public static void updateWindowMenu() {
		for (WindowMenu m : ResplMain.windowMenuRegistry.values()) m.update();
	}
	
	private static class WindowRegistryListener implements WindowListener {
		public void windowActivated(WindowEvent e) {
			if (!(e.getSource() instanceof Window)) return;
			windowRegistry.remove(e.getSource());
			windowRegistry.add(0,(Window)e.getSource());
			updateWindowMenu();
		}
		public void windowClosed(WindowEvent e) {
			if (!(e.getSource() instanceof Window)) return;
			windowRegistry.remove(e.getSource());
			fileMenuRegistry.remove(e.getSource());
			windowMenuRegistry.remove(e.getSource());
			updateWindowMenu();
		}
		public void windowClosing(WindowEvent e) {
			if (!(e.getSource() instanceof Window)) return;
			windowRegistry.remove(e.getSource());
			fileMenuRegistry.remove(e.getSource());
			windowMenuRegistry.remove(e.getSource());
			updateWindowMenu();
		}
		public void windowDeactivated(WindowEvent e) {
			if (!(e.getSource() instanceof Window)) return;
			updateWindowMenu();
		}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {
			if (!(e.getSource() instanceof Window)) return;
			windowRegistry.remove(e.getSource());
			windowRegistry.add(0,(Window)e.getSource());
			updateWindowMenu();
		}
	}
	
	
	// PLUGINS
	private static TextFilterList textFilterRegistry = new TextFilterList();
	public static void registerTextFilter(TextFilter tf) {
		textFilterRegistry.add(tf);
	}
	public static String[] getTextFilterCategories() {
		return textFilterRegistry.getCategoryList();
	}
	public static String[] getTextFilterNames(String cat) {
		return textFilterRegistry.getNameList(cat);
	}
	public static TextFilter getTextFilter(String cat, String name) {
		return textFilterRegistry.getTextFilter(cat, name);
	}
	public static int getTextFilterIndex(String cat, String name) {
		return textFilterRegistry.getTextFilterIndex(cat, name);
	}
	
	private static DataFilterList dataFilterRegistry = new DataFilterList();
	public static void registerDataFilter(DataFilter df) {
		dataFilterRegistry.add(df);
	}
	public static String[] getDataFilterCategories() {
		return dataFilterRegistry.getCategoryList();
	}
	public static String[] getDataFilterNames(String cat) {
		return dataFilterRegistry.getNameList(cat);
	}
	public static DataFilter getDataFilter(String cat, String name) {
		return dataFilterRegistry.getDataFilter(cat, name);
	}
	public static int getDataFilterIndex(String cat, String name) {
		return dataFilterRegistry.getDataFilterIndex(cat, name);
	}
	
	private static AccessoryWindowList accWinRegistry = new AccessoryWindowList();
	public static void registerAccessoryWindow(AccessoryWindow aw) {
		accWinRegistry.add(aw);
	}
	public static String[] getAccessoryWindowNames() {
		return accWinRegistry.getNameList();
	}
	public static AccessoryWindow getAccessoryWindow(String name) {
		return accWinRegistry.getAccWin(name);
	}
	public static int getAccessoryWindowIndex(String name) {
		return accWinRegistry.getAccWinIndex(name);
	}
	
	private static FileCodecList fileCodecRegistry = new FileCodecList();
	public static void registerFileCodec(FileCodec fc) {
		fileCodecRegistry.add(fc);
	}
	public static FileCodec getFileCodec(File f) {
		FileCodec codec = null;
		int recognizes = FileCodec.DOES_NOT_RECOGNIZE;
		for (FileCodec fc : fileCodecRegistry.getFileCodecList()) {
			int r = fc.recognizes(f);
			if (r > recognizes) {
				codec = fc;
				recognizes = r;
			}
		}
		return codec;
	}
	public static FileCodec[] getFileCodecList() {
		return fileCodecRegistry.getFileCodecList();
	}
	
	private static ResplendenceEditorList editorRegistry = new ResplendenceEditorList();
	public static void registerEditor(ResplendenceEditor e) {
		editorRegistry.add(e);
	}
	public static ResplendenceEditor getEditor(ResplendenceObject ro) {
		ResplendenceEditor editor = null;
		int recognizes = ResplendenceEditor.DOES_NOT_RECOGNIZE;
		for (ResplendenceEditor ed : editorRegistry.getResplendenceEditorList()) {
			int r = ed.recognizes(ro);
			if (r > recognizes) {
				editor = ed;
				recognizes = r;
			}
		}
		return editor;
	}
	public static ResplendenceEditor[] getEditors(ResplendenceObject ro) {
		Collection<ResplendenceEditor> editors = new ArrayList<ResplendenceEditor>();
		for (ResplendenceEditor ed : editorRegistry.getResplendenceEditorList()) {
			if (ed.recognizes(ro) != ResplendenceEditor.DOES_NOT_RECOGNIZE) {
				editors.add(ed);
			}
		}
		return editors.toArray(new ResplendenceEditor[0]);
	}
	public static ResplendenceEditor[] getEditorList() {
		return editorRegistry.getResplendenceEditorList();
	}
	
	private static ResplendencePickerList pickerRegistry = new ResplendencePickerList();
	public static void registerPicker(ResplendencePicker p) {
		pickerRegistry.add(p);
	}
	public static ResplendencePicker getPicker(ResplendenceObject ro) {
		ResplendencePicker picker = null;
		int recognizes = ResplendencePicker.DOES_NOT_RECOGNIZE;
		for (ResplendencePicker pk : pickerRegistry.getResplendencePickerList()) {
			int r = pk.recognizes(ro);
			if (r > recognizes) {
				picker = pk;
				recognizes = r;
			}
		}
		return picker;
	}
	public static ResplendencePicker[] getPickerList() {
		return pickerRegistry.getResplendencePickerList();
	}
	
	public static void registerPlugin(Object o) {
		if (o instanceof AccessoryWindow) registerAccessoryWindow((AccessoryWindow)o);
		if (o instanceof DataFilter) registerDataFilter((DataFilter)o);
		if (o instanceof ResplendenceEditor) registerEditor((ResplendenceEditor)o);
		if (o instanceof FileCodec) registerFileCodec((FileCodec)o);
		if (o instanceof ResplendencePicker) registerPicker((ResplendencePicker)o);
		if (o instanceof TextFilter) registerTextFilter((TextFilter)o);
	}
	
	public static boolean isPlugin(Object o) {
		return (
				   o instanceof AccessoryWindow
				|| o instanceof DataFilter
				|| o instanceof ResplendenceEditor
				|| o instanceof FileCodec
				|| o instanceof ResplendencePicker
				|| o instanceof TextFilter
		);
	}
	
	public static boolean isPluginType(Class<?> c) {
		return (
				   c.equals(AccessoryWindow.class)
				|| c.equals(DataFilter.class)
				|| c.equals(ResplendenceEditor.class)
				|| c.equals(FileCodec.class)
				|| c.equals(ResplendencePicker.class)
				|| c.equals(TextFilter.class)
		);
	}
	
	
	// START IT ALL
	public static void resplInit() {
		resplInit(null);
	}
	
	public static void resplInit(File[] toOpen) {
		//set Mac OS properties
		if (RUNNING_ON_MAC_OS) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Resplendence");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("apple.awt.use-file-dialog-packages", "false");
		}
		//set version number property
		System.setProperty("com.kreative.resplendence.version", "2.0");
		//setup for trapping modifiers
		ResplUtils.setTrap();
		//initialize built-in stuff
		registerPlugins();
		//open first window
		if (RUNNING_ON_MAC_OS) {
			new com.kreative.resplendence.mac.MacDummyWindow(getMenuBar(null, MENUS_GLOBAL, null));
			if (toOpen != null && toOpen.length > 0) for (File f : toOpen) resplOpen(f);
			try {
				//this must be done here, otherwise open document
				//events fire before everything is initialized
				new com.kreative.resplendence.mac.ResplApplListener().register();
			} catch (Exception e) {}
		} else if (toOpen == null || toOpen.length == 0) {
			String s = ResplPrefs.getString("Startup Path");
			if (s != null && s.length() > 0) {
				resplOpen(new File(s));
			} else {
				resplOpen(new FolderWindow(), new FolderWindow.MyComputerObject());
			}
		} else {
			for (File f : toOpen) resplOpen(f);
		}
	}
	
	private static void registerPlugins() {
		String[] pkgs = {
				"com.kreative.resplendence.acc",
				"com.kreative.resplendence.datafilter",
				"com.kreative.resplendence.editors",
				"com.kreative.resplendence.filecodec",
				"com.kreative.resplendence.pickers",
				"com.kreative.resplendence.textfilter",
				"test.acc"
		};
		for (String pkg : pkgs) {
			try {
				Class<?>[] clses = getClasses(pkg);
				for (Class<?> cls : clses) {
					if (!(
							cls.isMemberClass()
							|| cls.isAnonymousClass()
							|| cls.isInterface()
							|| Modifier.isAbstract(cls.getModifiers())
					)) {
						Class<?>[] faces = cls.getInterfaces();
						for (Class<?> face : faces) {
							if (isPluginType(face)) {
								try {
									registerPlugin(cls.newInstance());
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		DFFResourceProvider dp = ResplRsrcs.getAppDFFResourceProvider();
		DFFClassLoader dl = new DFFClassLoader(dp);
		Set<String> names = new HashSet<String>();
		for (int id : dp.getIDs(KSFLConstants.ExecJava)) {
			String name = dp.getNameFromID(KSFLConstants.ExecJava, id);
			if (name.endsWith(".class")) name = name.substring(0, name.length()-6);
			names.add(name);
		}
		for (String name : names) {
			if (name != null && name.length() > 0) {
				try {
					Class<?> cls = dl.loadClass(name);
					Class<?>[] faces = cls.getInterfaces();
					for (Class<?> face : faces) {
						if (isPluginType(face)) {
							try {
								registerPlugin(cls.newInstance());
								break;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {}
			}
		}
	}
	
	private static Class<?>[] getClasses(String pckgname) throws ClassNotFoundException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			//String path = "/"+pckgname.replace('.','/');
			String path = pckgname.replace('.',File.separatorChar);
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for "+path);
			}
			//directory = new File(resource.getFile());
			directory = new File(URLDecoder.decode(resource.getPath(),"UTF-8"));
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname+" ("+directory+") does not appear to be a valid package");
		} catch (UnsupportedEncodingException x) {
			throw new ClassNotFoundException(pckgname+" ("+directory+") does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(pckgname+'.'+files[i].substring(0, files[i].length()-6)));
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname+" does not appear to be a valid package");
		}
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	public static ResplendenceEditorWindow resplOpen(File f) {
		return resplOpen(new FSObject(f));
	}
	
	public static ResplendenceEditorWindow resplOpen(ResplendenceObject ro) {
		if (ro.getClass().getSimpleName().equals("FSObject")) {
			File f = ro.getNativeFile();
			int maxrec = ResplPrefs.getInt("Max Recent");
			if (maxrec < 1) maxrec = 20;
			List<File> rlist = ResplPrefs.getFiles("Recent Files");
			while (rlist.contains(f)) rlist.remove(f);
			if (!f.exists()) {
				while (rlist.size() > maxrec) rlist.remove(maxrec);
				ResplPrefs.setFiles("Recent Files", rlist);
				updateFileMenu();

				JOptionPane.showMessageDialog(null, "Could not open \""+f.getName()+"\" because it no longer exists.", "Open", JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				rlist.add(0, f);
				while (rlist.size() > maxrec) rlist.remove(maxrec);
				ResplPrefs.setFiles("Recent Files", rlist);
				updateFileMenu();

				ResplendenceEditor ed = ResplMain.getEditor(ro);
				if (ed == null) {
					JOptionPane.showMessageDialog(null, "There is no editor for \""+f.getName()+".\" This shouldn't happen, because either FileWindow or FolderWindow should catch it. Dammit!", "Open", JOptionPane.ERROR_MESSAGE);
					return null;
				} else {
					return ed.openEditor(ro);
				}
			}
		} else {
			ResplendenceEditor ed = ResplMain.getEditor(ro);
			if (ed == null) {
				JOptionPane.showMessageDialog(null, "There is no editor for this object.", "Open", JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				return ed.openEditor(ro);
			}
		}
	}
	
	public static ResplendenceEditorWindow resplOpen(ResplendenceEditor ed, ResplendenceObject ro) {
		if (ed.recognizes(ro) == ResplendenceEditor.DOES_NOT_RECOGNIZE) {
			JOptionPane.showMessageDialog(null, ed.name()+" cannot be used to edit this object.", "Open", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return ed.openEditor(ro);
	}
	
	public static ResplendenceEditorWindow resplOpen(ResplendenceEditorWindow rew) {
		// here in case of expansion
		return rew;
	}
	
	public static void resplExit() {
		// TODO stuff
		System.exit(0);
	}
	
	public static void resplHelp() {
		// TODO stub method
		System.out.println("Help stub!!!");
	}
}
