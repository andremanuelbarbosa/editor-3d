package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public interface Tool
{
    public static final int PAINT = 0;
    public static final int PAINT_ALL = 1;
    
    public int mousePressed(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    public int mouseReleased(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    public int mouseClicked(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    public int mouseDoubleClicked(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    public int mouseDragged(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    public int mouseMoved(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D);
    
    public void reset();
    public void paint(Graphics g);
    
    public Cursor getCursor();
}