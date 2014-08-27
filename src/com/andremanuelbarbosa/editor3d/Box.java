package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Box implements Solid
{
    private Vertex originalVertices[];
    private Vertex originalCenter;
    
    private Vertex vertices[];
    private Edge edges[];
    
    private Vertex center;

    private boolean selected;
    
    private LinkedList undo;
    private LinkedList redo;
    
    private boolean flag;
    private Transform3D tmpT3D;
    
    private int number;
    
    public Box(float x1,float y1,float x2,float y2,float z1,float z2)
    {
        originalVertices = new Vertex[8];
        vertices = new Vertex[8];
        edges = new Edge[12];
        
        originalVertices[0] = new Vertex(x1,y1,z1);
        originalVertices[1] = new Vertex(x1,y1,z2);
        originalVertices[2] = new Vertex(x2,y2,z1);
        originalVertices[3] = new Vertex(x2,y2,z2);
        originalVertices[4] = new Vertex(x1,y2,z1);
        originalVertices[5] = new Vertex(x1,y2,z2);
        originalVertices[6] = new Vertex(x2,y1,z1);
        originalVertices[7] = new Vertex(x2,y1,z2);
        
        vertices[0] = new Vertex(x1,y1,z1);
        vertices[1] = new Vertex(x1,y1,z2);
        vertices[2] = new Vertex(x2,y2,z1);
        vertices[3] = new Vertex(x2,y2,z2);
        vertices[4] = new Vertex(x1,y2,z1);
        vertices[5] = new Vertex(x1,y2,z2);
        vertices[6] = new Vertex(x2,y1,z1);
        vertices[7] = new Vertex(x2,y1,z2);
        
        edges[0] = new Edge(vertices[0],vertices[1]);
        edges[1] = new Edge(vertices[2],vertices[3]);
        edges[2] = new Edge(vertices[4],vertices[5]);
        edges[3] = new Edge(vertices[6],vertices[7]);
        edges[4] = new Edge(vertices[0],vertices[4]);
        edges[5] = new Edge(vertices[0],vertices[6]);
        edges[6] = new Edge(vertices[2],vertices[4]);
        edges[7] = new Edge(vertices[2],vertices[6]);
        edges[8] = new Edge(vertices[1],vertices[5]);
        edges[9] = new Edge(vertices[1],vertices[7]);
        edges[10] = new Edge(vertices[3],vertices[5]);
        edges[11] = new Edge(vertices[3],vertices[7]);
        
        originalCenter = new Vertex(x1 + (x2 - x1) / 2,y1 + (y2 - y1) / 2,z1 + (z2 - z1) / 2);
        center = new Vertex(x1 + (x2 - x1) / 2,y1 + (y2 - y1) / 2,z1 + (z2 - z1) / 2);

        undo = new LinkedList();
        redo = new LinkedList();
    }
    
    public void undo()
    {
        if(flag)
        {
            if(undo.size() == Definitions.HISTORY_SIZE) undo.removeFirst();
            
            undo.addLast(tmpT3D);
            flag = false;
        }
        
        if(undo.size() == 0) return;
        
        Transform3D t3DUndo = (Transform3D) undo.removeLast();
        
        t3DUndo.invert();
        
        apply(t3DUndo);
        
        if(redo.size() == Definitions.HISTORY_SIZE) redo.removeFirst();
        
        redo.addLast(t3DUndo);
    }
    
    public void redo()
    {
        if(redo.size() == 0) return;
        
        Transform3D t3DRedo = (Transform3D) redo.removeLast();
        
        t3DRedo.invert();
        
        apply(t3DRedo);
        
        if(undo.size() == Definitions.HISTORY_SIZE) undo.removeFirst();
        
        undo.addLast(t3DRedo);
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
    public boolean intersects(MyJPanel myJPanel,int x,int y,int width,int height)
    {
        boolean flag = false;
        
        Point center = myJPanel.getCenter();
        
        Line2D.Float line2DFloat = new Line2D.Float();
        
        for(int i = 0 ; i < edges.length ; i++)
        {
            Vertex v1 = edges[i].v1;
            Vertex v2 = edges[i].v2;
            
            if(myJPanel instanceof MyJPanelXY)
            {
                line2DFloat.x1 = center.x + v1.x;
                line2DFloat.y1 = center.y - v1.y;
                line2DFloat.x2 = center.x + v2.x;
                line2DFloat.y2 = center.y - v2.y;
            }
            else if(myJPanel instanceof MyJPanelXZ)
            {
                line2DFloat.x1 = center.x + v1.x;
                line2DFloat.y1 = center.y + v1.z;
                line2DFloat.x2 = center.x + v2.x;
                line2DFloat.y2 = center.y + v2.z;
            }
            else if(myJPanel instanceof MyJPanelYZ)
            {
                line2DFloat.x1 = center.x - v1.z;
                line2DFloat.y1 = center.y - v1.y;
                line2DFloat.x2 = center.x - v2.z;
                line2DFloat.y2 = center.y - v2.y;
            }
            
            if(line2DFloat.intersects(x,y,width,height))
            {
                flag = true;
                break;
            }
        }
        
        return flag;
    }
    
    public void applyTransform3D(Transform3D t3D)
    {
        if(undo.size() == Definitions.HISTORY_SIZE) undo.removeFirst();
        
        if(flag)
        {
            undo.addLast(tmpT3D);
            flag = false;
        }
        
        if(undo.size() == Definitions.HISTORY_SIZE) undo.removeFirst();
        
        undo.addLast(t3D);
        
        redo.clear();
        
        apply(t3D);
    }
    
    public void applyTransparentTransform3D(Transform3D t3D)
    {
        redo.clear();
        
        if(!flag)
        {
            tmpT3D = new Transform3D();
            flag = true;
        }
        
        tmpT3D.multiply(t3D);
        
        apply(t3D);
    }
    
    public void initialState() 
    {
        for(int i = 0; i < vertices.length; i++)
        {
            vertices[i].x = originalVertices[i].x;
            vertices[i].y = originalVertices[i].y;
            vertices[i].z = originalVertices[i].z;
        }
        
        center.x = originalCenter.x;
        center.y = originalCenter.y;
        center.z = originalCenter.z;
        
        redo.clear();
        undo.clear();
    }
    
    public void paintXY(Graphics g,MyJPanelXY myJPanelXY)
    {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setPaintMode();
        g2D.setStroke(Definitions.NORMAL);
        
        if(!selected) g2D.setColor(Definitions.SOLID_COLOR);
        else g2D.setColor(Definitions.SELECTED_SOLID_COLOR);
        
        Point center = myJPanelXY.getCenter();
        
        Line2D.Float line2DFloat = new Line2D.Float();
        
        for(int i = 0 ; i < edges.length ; i++)
        {
            Vertex v1 = edges[i].v1;
            Vertex v2 = edges[i].v2;
            
            if((int) v1.x < xMin) xMin = (int) v1.x;
            if((int) v2.x < xMin) xMin = (int) v2.x;
            
            if((int) v1.y < yMin) yMin = (int) v1.y;
            if((int) v2.y < yMin) yMin = (int) v2.y;
            
            line2DFloat.x1 = center.x + v1.x;
            line2DFloat.y1 = center.y - v1.y;
            line2DFloat.x2 = center.x + v2.x;
            line2DFloat.y2 = center.y - v2.y;
            
            g2D.draw(line2DFloat);
        }
        
        String s = "" + number;
        
        g2D.setFont(Definitions.NUMBER_FONT);
        
        int w = g2D.getFontMetrics().stringWidth(s);
        int h = g2D.getFontMetrics().getAscent();
        
        g2D.drawString(s,center.x + (xMin - w),center.y - (yMin - h));
    }
    
    public void paintXZ(Graphics g,MyJPanelXZ myJPanelXZ)
    {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setPaintMode();
        g2D.setStroke(Definitions.NORMAL);
        
        if(!selected) g2D.setColor(Definitions.SOLID_COLOR);
        else g2D.setColor(Definitions.SELECTED_SOLID_COLOR);
        
        Point center = myJPanelXZ.getCenter();
        
        Line2D.Float line2DFloat = new Line2D.Float();
        
        for(int i = 0 ; i < edges.length ; i++)
        {
            Vertex v1 = edges[i].v1;
            Vertex v2 = edges[i].v2;
            
            if((int) v1.x < xMin) xMin = (int) v1.x;
            if((int) v2.x < xMin) xMin = (int) v2.x;
            
            if((int) v1.z < yMin) yMin = (int) v1.z;
            if((int) v2.z < yMin) yMin = (int) v2.z;
            
            line2DFloat.x1 = center.x + v1.x;
            line2DFloat.y1 = center.y + v1.z;
            line2DFloat.x2 = center.x + v2.x;
            line2DFloat.y2 = center.y + v2.z;
            
            g2D.draw(line2DFloat);
        }
        
        String s = "" + number;
        
        g2D.setFont(Definitions.NUMBER_FONT);
        
        int w = g2D.getFontMetrics().stringWidth(s);
        int h = g2D.getFontMetrics().getAscent();
        
        g2D.drawString(s,center.x + (xMin - w),center.y + (yMin - h));
    }
    
    public void paintYZ(Graphics g,MyJPanelYZ myJPanelYZ)
    {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setPaintMode();
        g2D.setStroke(Definitions.NORMAL);
        
        if(!selected) g2D.setColor(Definitions.SOLID_COLOR);
        else g2D.setColor(Definitions.SELECTED_SOLID_COLOR);
        
        Point center = myJPanelYZ.getCenter();
        
        Line2D.Float line2DFloat = new Line2D.Float();
        
        for(int i = 0 ; i < edges.length ; i++)
        {
            Vertex v1 = edges[i].v1;
            Vertex v2 = edges[i].v2;
            
            if((int) v1.z < xMin) xMin = (int) v1.z;
            if((int) v2.z < xMin) xMin = (int) v2.z;
            
            if((int) v1.y < yMin) yMin = (int) v1.y;
            if((int) v2.y < yMin) yMin = (int) v2.y;
            
            line2DFloat.x1 = center.x - v1.z;
            line2DFloat.y1 = center.y - v1.y;
            line2DFloat.x2 = center.x - v2.z;
            line2DFloat.y2 = center.y - v2.y;
            
            g2D.draw(line2DFloat);
        }
        
        String s = "" + number;
        
        g2D.setFont(Definitions.NUMBER_FONT);
        
        int w = g2D.getFontMetrics().stringWidth(s);
        int h = g2D.getFontMetrics().getAscent();
        
        g2D.drawString(s,center.x - (xMin - w),center.y - (yMin - h));
    }
    
    public void clearHistory()
    {
        undo.clear();
        redo.clear();
    }
    
    public void setNumber(int number)
    {
        if(number <= 0) return;
        
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    private void apply(Transform3D t3D)
    {
        float x = center.x;
        float y = center.y;
        float z = center.z;
        
        Transform3D t3DAux = new Transform3D();
        
        t3DAux.translate(-x,-y,-z);
        
        int i;
        
        for(i = 0 ; i < vertices.length ; i++) t3DAux.multiply(vertices[i]);
        t3DAux.multiply(center);
        
        for(i = 0 ; i < vertices.length ; i++) t3D.multiply(vertices[i]);
        t3D.multiply(center);
        
        t3DAux.translate(x,y,z);
        
        for(i = 0 ; i < vertices.length ; i++) t3DAux.multiply(vertices[i]);
        t3DAux.multiply(center);
    }
}