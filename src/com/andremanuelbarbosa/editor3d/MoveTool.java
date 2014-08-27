package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MoveTool implements Tool
{
    private int x;
    private int y;
    
    private boolean flag;
    
    private static Cursor cursor;
    
    public MoveTool()
    {
        cursor = new Cursor(Cursor.MOVE_CURSOR);
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
            int dx = evt.getX() - x;
            int dy = evt.getY() - y;
            
            Transform3D t3D;
            
            for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
            {
                Solid solid = (Solid) listItr.next();
                
                if(solid.isSelected())
                {
                    t3D = new Transform3D();
                    
                    if(myJPanel instanceof MyJPanelXY) t3D.translate(dx,-dy,0);
                    else if(myJPanel instanceof MyJPanelXZ) t3D.translate(dx,0,dy);
                    else if(myJPanel instanceof MyJPanelYZ) t3D.translate(0,-dy,-dx);
                    
                    solid.applyTransparentTransform3D(t3D);
                }
            }
            
            x = evt.getX();
            y = evt.getY();
            
            return PAINT_ALL;
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
            x = evt.getX();
            y = evt.getY();
            
            flag = true;
        }
        
        return -1;
    }
    
    public int mouseReleased(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        reset();
        
        return -1;
    }
    
    public void paint(Graphics g)
    {
    }
    
    public void reset()
    {
        x = 0;
        y = 0;
        
        flag = false;
    }
    
    public Cursor getCursor()
    {
        return cursor;
    }
}