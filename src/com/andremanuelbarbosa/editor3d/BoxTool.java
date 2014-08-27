package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BoxTool implements Tool
{
    private int x1;
    private int y1;
    
    private int x2;
    private int y2;
    
    private boolean flag;
    
    private static Cursor cursor;
    
    public BoxTool()
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
        return -1;
    }
    
    public int mouseMoved(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        if(flag)
        {
            x2 = evt.getX();
            y2 = evt.getY();
            
            return PAINT;
        }
        
        return -1;
    }
    
    public int mousePressed(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        if(SwingUtilities.isRightMouseButton(evt))
        {
            reset();
            return PAINT;
        }
        
        if(SwingUtilities.isLeftMouseButton(evt))
        {
            if(!flag)
            {
                x1 = evt.getX();
                y1 = evt.getY();
                
                x2 = evt.getX();
                y2 = evt.getY();
                
                flag = true;
            }
            else
            {
                int z1 = 0;
                int z2 = 0;
                
                String str;
                
                boolean flag;
                
                flag = true;
                
                while(flag)
                {
                    str = JOptionPane.showInputDialog(null,"Insira o valor de z1","",JOptionPane.QUESTION_MESSAGE);
                    
                    if(str == null)
                    {
                        reset();
                        return PAINT;
                    }
                    
                    try
                    {
                        z1 = Integer.parseInt(str);
                        flag = false;
                    }
                    catch(NumberFormatException e1)
                    {
                    }
                }
                
                flag = true;
                
                while(flag)
                {
                    str = JOptionPane.showInputDialog(null,"Insira o valor de z2","",JOptionPane.QUESTION_MESSAGE);
                    
                    if(str == null)
                    {
                        reset();
                        return PAINT;
                    }
                    
                    try
                    {
                        z2 = Integer.parseInt(str);
                        flag = false;
                    }
                    catch(NumberFormatException e1)
                    {
                    }
                }
                
                Point center = myJPanel.getCenter();
                
                Box b = new Box(x1 - center.x,-(y1 - center.y),x2 - center.x,-(y2 - center.y),z1,z2);
                b.setNumber(editor3D.updateNumbers());
                
                solids.addLast(b);
                
                reset();
                
                return PAINT_ALL;
            }
        }
        
        return -1;
    }
    
    public int mouseReleased(MouseEvent evt,MyJPanel myJPanel,LinkedList solids,Editor3D editor3D)
    {
        return -1;
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