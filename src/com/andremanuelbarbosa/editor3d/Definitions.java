package com.andremanuelbarbosa.editor3d;

import java.awt.*;

public abstract class Definitions
{
    public static final Color AXIS_COLOR = Color.black;
    
    public static final Color TOOL_COLOR = Color.green;
    
    public static final Color SOLID_COLOR = Color.blue;
    public static final Color SELECTED_SOLID_COLOR = Color.red;
    
    private static final float[] dash = {4f};
    
    public static final Stroke NORMAL = new BasicStroke(1f);
    public static final Stroke DASH = new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0f,dash,0f);
    
    public static final Font AXIS_FONT = new Font("Times New Roman",Font.BOLD,11);
    public static final Font NUMBER_FONT = new Font("Times New Roman",Font.BOLD,11);
    
    public static final int MOUSE_PRESSED = 0;
    public static final int MOUSE_RELEASED = 1;
    public static final int MOUSE_CLICKED = 2;
    public static final int MOUSE_DOUBLE_CLICKED = 3;
    public static final int MOUSE_DRAGGED = 4;
    public static final int MOUSE_MOVED = 5;
    
    public static final int HISTORY_SIZE = 20;
    
    public static final int IMG_WIDTH = 1024;
    public static final int IMG_HEIGHT = 768;
}