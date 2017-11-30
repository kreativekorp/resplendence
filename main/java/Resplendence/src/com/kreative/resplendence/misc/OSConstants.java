package com.kreative.resplendence.misc;

public interface OSConstants {
	// CONSTANTS USED THROUGHOUT
	public static final boolean RUNNING_ON_MAC_OS = (System.getProperty("os.name").toUpperCase().contains("MAC OS"));
	public static final boolean RUNNING_ON_WINDOWS = (System.getProperty("os.name").toUpperCase().contains("WINDOWS"));
}
