package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SelectTool implements Tool
{
    private int x1;
    private int y1;
    
    private int x2;
    private int y2;
    
    private boolean flag;
    
    private static Cursor cursor;
    
    public SelectTool()
    {
        cursor = new Cursor(Cursor.DEFAULT_CURSOR);
    }
    
    public int mouseClicked(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        return -1;
    }
    
    public int mouseDoubleClicked(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        return -1;
    }
    
    public int mouseDragged(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        if(flag)
        {
            x2 = evt.getX();
            y2 = evt.getY();
            
            return PAINT;
        }
        
        return -1;
    }
    
    public int mouseMoved(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        return -1;
    }
    
    public int mousePressed(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        if(SwingUtilities.isLeftMouseButton(evt))
        {
            x1 = evt.getX();
            y1 = evt.getY();
            
            x2 = evt.getX();
            y2 = evt.getY();
            
            flag = true;
        }
        
        return -1;
    }
    
    public int mouseReleased(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        int x = (int) (Math.min(x1,x2));
        int y = (int) (Math.min(y1,y2));
        
        int w = (int) (Math.abs(x1 - x2));
        int h = (int) (Math.abs(y1 - y2));
        
        if(w <= 4)
        {
            x = x - 4;
            w = 8;
        }
        
        if(h <= 4)
        {
            y = y - 4;
            h = 8;
        }
        
        intersections(myJPanel,solids,x,y,w,h);
        reset();
        
        return PAINT_ALL;
    }
    
    private void intersections(MyJPanel myJPanel,LinkedList solids,int x,int y,int w,int h)
    {
        for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
        {
            Solid solid = (Solid) listItr.next();
            
            solid.setSelected(false);
            
            if(solid.intersects(myJPanel,x,y,w,h)) solid.setSelected(true);
        }
    }
    
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setPaintMode();
        g2D.setStroke(Definitions.DASH);
        
        g2D.setColor(Definitions.TOOL_COLOR);
        
        g2D.drawLine(x1,y1,x2,y1);
        g2D.drawLine(x1,y1,x1,y2);
        g2D.drawLine(x2,y2,x1,y2);
        g2D.drawLine(x2,y2,x2,y1);
    }
    
    public void reset()
    {
        x1 = 0;
        y1 = 0;
        
        x2 = 0;
        y2 = 0;
        
        flag = false;
    }
    
    public Cursor getCursor()
    {
        return cursor;
    }
}