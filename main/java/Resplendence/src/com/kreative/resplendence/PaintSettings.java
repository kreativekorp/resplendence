package com.kreative.resplendence;

public class PaintSettings {
	public static final int BRUSH_SHAPE_OVAL = 0;
	public static final int BRUSH_SHAPE_RECTANGLE = 1;
	public static final int BRUSH_SHAPE_FORWARD_SLASH = 2;
	public static final int BRUSH_SHAPE_BACK_SLASH = 3;
	
	public static final int POLYGON_SHAPE_POLYGON = 0;
	public static final int POLYGON_SHAPE_STAR = 1;
	public static final int POLYGON_SHAPE_POLYGRAM = 2;
	
	public static final int POLYGON_SIDES_MINIMUM = 3;
	public static final int POLYGON_SIDES_TRIANGLE = 3;
	public static final int POLYGON_SIDES_SQUARE = 4;
	public static final int POLYGON_SIDES_PENTAGON = 5;
	public static final int POLYGON_SIDES_HEXAGON = 6;
	public static final int POLYGON_SIDES_HEPTAGON = 7;
	public static final int POLYGON_SIDES_OCTAGON = 8;
	public static final int POLYGON_SIDES_NONAGON = 9;
	public static final int POLYGON_SIDES_DECAGON = 10;
	
	public int brushShape = BRUSH_SHAPE_OVAL;
	public int brushOuterHDiameter = 8;
	public int brushOuterVDiameter = 8;
	public int brushInnerHDiameter = 8;
	public int brushInnerVDiameter = 8;
	public int lineWidth = 1;
	public int polygonShape = POLYGON_SHAPE_POLYGON;
	public int polygonSides = 6;
	public boolean drawFromCenter = false;
	public boolean drawFilled = false;
}
