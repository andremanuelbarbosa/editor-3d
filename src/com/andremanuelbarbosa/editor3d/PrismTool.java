package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class PrismTool implements Tool
{
    private int flag;
    
    private Point center;
    
    private int radiusMin;
    private int radiusMax;
    
    private static Cursor cursor;
    
    public PrismTool()
    {
        center = new Point();
        
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
        int dx;
        int dy;
        
        if(flag == 1)
        {
            dx = (int) (Math.abs(center.x - evt.getX()));
            dy = (int) (Math.abs(center.y - evt.getY()));
            
            radiusMin = (int) (Math.max(dx,dy));
            
            return PAINT;
        }
        else if(flag == 2)
        {
            dx = (int) (Math.abs(center.x - evt.getX()));
            dy = (int) (Math.abs(center.y - evt.getY()));
            
            int i = (int) (Math.max(dx,dy));
            
            if(i > radiusMin) radiusMax = i;
            
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
            if(flag == 0)
            {
                center.x = evt.getX();
                center.y = evt.getY();
                
                flag = 1;
            }
            else if(flag == 1) flag = 2;
            else if(flag == 2)
            {
                int n = 0;
                
                int z1 = 0;
                int z2 = 0;
                
                String str;
                
                boolean flag;
                
                flag = true;
                
                while(flag)
                {
                    str = JOptionPane.showInputDialog(null,"Insira o n� de dentes (3 ou mais)","",JOptionPane.QUESTION_MESSAGE);
                    
                    if(str == null)
                    {
                        reset();
                        return PAINT;
                    }
                    
                    try
                    {
                        n = Integer.parseInt(str);
                        
                        if(n >= 3) flag = false;
                    }
                    catch(NumberFormatException e1)
                    {
                    }
                }
                
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
                
                Prism p = new Prism(new Point(this.center.x - center.x,-(this.center.y - center.y)),radiusMin,radiusMax,n,z1,z2);
                p.setNumber(editor3D.updateNumbers());
                
                solids.addLast(p);
                
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
        
        g2D.drawOval(center.x - radiusMin,center.y - radiusMin,radiusMin * 2,radiusMin * 2);
        g2D.drawOval(center.x - radiusMax,center.y - radiusMax,radiusMax * 2,radiusMax * 2);
    }
    
    public void reset()
    {
        flag = 0;
        
        center.x = 0;
        center.y = 0;
        
        radiusMin = 0;
        radiusMax = 0;
    }
    
    public Cursor getCursor()
    {
        return cursor;
    }
}