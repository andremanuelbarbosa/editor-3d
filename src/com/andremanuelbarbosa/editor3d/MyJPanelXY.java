package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MyJPanelXY extends JPanel implements MyJPanel
{
    private Editor3D editor3D;
    private JInternalFrame jInternalFrame;
    
    private LinkedList tools;
    
    private Image img;
    private Graphics g;
    
    private boolean axisEnabled = true;
    
    private static final Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private MouseListener myMouseListener;
    private MouseMotionListener myMouseMotionListener;
    
    private boolean enabled = true;
    
    public MyJPanelXY(Editor3D editor3D,JInternalFrame jInternalFrame)
    {
        this.editor3D = editor3D;
        this.jInternalFrame = jInternalFrame;
        
        tools = new LinkedList();
        
        setBackground(Color.white);
        
        myMouseListener = new MouseListener()
        {
            public void mousePressed(MouseEvent evt)
            {
                myMousePressed(evt);
            }
            
            public void mouseReleased(MouseEvent evt)
            {
                myMouseReleased(evt);
            }
            
            public void mouseClicked(MouseEvent evt)
            {
                myMouseClicked(evt);
            }
            
            public void mouseEntered(MouseEvent evt)
            {
                myMouseEntered(evt);
            }
            
            public void mouseExited(MouseEvent evt)
            {
                myMouseExited(evt);
            }
        };
        
        myMouseMotionListener = new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent evt)
            {
                myMouseDragged(evt);
            }
            
            public void mouseMoved(MouseEvent evt)
            {
                myMouseMoved(evt);
            }
        };
        
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseMotionListener);
    }
    
    private void myMousePressed(MouseEvent evt)
    {
        updateTool(Definitions.MOUSE_PRESSED,evt);
    }
    
    private void myMouseReleased(MouseEvent evt)
    {
        updateTool(Definitions.MOUSE_RELEASED,evt);
    }
    
    private void myMouseClicked(MouseEvent evt)
    {
        if(evt.getClickCount() == 1) updateTool(Definitions.MOUSE_CLICKED,evt);
        else updateTool(Definitions.MOUSE_DOUBLE_CLICKED,evt);
    }
    
    private void myMouseEntered(MouseEvent evt)
    {
        try
        {
            jInternalFrame.setSelected(true);
        }
        catch(Exception e)
        {
        }
        
        editor3D.setMousePosition(Editor3D.XY);
        
        if(tools.contains(editor3D.getSelectedTool())) setCursor(editor3D.getSelectedTool().getCursor());
        else setCursor(cursor);
    }
    
    private void myMouseExited(MouseEvent evt)
    {
        editor3D.getSelectedTool().reset();
        repaint();
    }
    
    private void myMouseDragged(MouseEvent evt)
    {
        updateTool(Definitions.MOUSE_DRAGGED,evt);
    }
    
    private void myMouseMoved(MouseEvent evt)
    {
        Point center = getCenter();
        
        editor3D.setMousePosition(evt.getX() - center.x,-(evt.getY() - center.y));
        
        updateTool(Definitions.MOUSE_MOVED,evt);
    }
    
    private void updateTool(int request,MouseEvent evt)
    {
        if(!tools.contains(editor3D.getSelectedTool())) return;
        
        int i = -1;
        
        if(request == Definitions.MOUSE_PRESSED) i = editor3D.getSelectedTool().mousePressed(evt,this,editor3D.getSolids(),editor3D);
        else if(request == Definitions.MOUSE_RELEASED) i = editor3D.getSelectedTool().mouseReleased(evt,this,editor3D.getSolids(),editor3D);
        else if(request == Definitions.MOUSE_CLICKED) i = editor3D.getSelectedTool().mouseClicked(evt,this,editor3D.getSolids(),editor3D);
        else if(request == Definitions.MOUSE_DOUBLE_CLICKED) i = editor3D.getSelectedTool().mouseDoubleClicked(evt,this,editor3D.getSolids(),editor3D);
        else if(request == Definitions.MOUSE_DRAGGED) i = editor3D.getSelectedTool().mouseDragged(evt,this,editor3D.getSolids(),editor3D);
        else if(request == Definitions.MOUSE_MOVED) i = editor3D.getSelectedTool().mouseMoved(evt,this,editor3D.getSolids(),editor3D);
        
        if(i == Tool.PAINT) repaint();
        else if(i == Tool.PAINT_ALL) editor3D.paintRequest();
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        
        if(img != null) g.drawImage(img,0,0,this);
    }
    
    public void repaint()
    {
        updateImage();
        
        super.repaint();
    }
    
    public void updateImage()
    {
        if(img == null && isDisplayable())
        {
            img = createImage(Definitions.IMG_WIDTH,Definitions.IMG_HEIGHT);
            g = img.getGraphics();
        }
        
        if(img != null)
        {
            Graphics2D g2D = (Graphics2D) g;
            
            g2D.setPaintMode();
            g2D.setStroke(Definitions.DASH);
            
            g2D.setColor(Color.white);
            g2D.fillRect(0,0,Definitions.IMG_WIDTH,Definitions.IMG_HEIGHT);
            
            if(axisEnabled)
            {
                g2D.setColor(Definitions.AXIS_COLOR);

                int i = (getSize().width - 40) / 2;
                int j = (getSize().height - 40) / 2;

                Point center = getCenter();

                g2D.drawLine(center.x - i,center.y,center.x + i,center.y);
                g2D.drawLine(center.x,center.y - j,center.x,center.y + j);

                g2D.setStroke(Definitions.NORMAL);

                g2D.drawLine(center.x + i,center.y - 3,center.x + i,center.y + 3);
                g2D.drawLine(center.x + i + 6,center.y,center.x + i,center.y - 3);
                g2D.drawLine(center.x + i + 6,center.y,center.x + i,center.y + 3);

                g2D.drawLine(center.x - 3,center.y - j,center.x + 3,center.y - j);
                g2D.drawLine(center.x,center.y - j - 6,center.x - 3,center.y - j);
                g2D.drawLine(center.x,center.y - j - 6,center.x + 3,center.y - j);

                g2D.setFont(Definitions.AXIS_FONT);

                g2D.drawString("x",center.x + i,center.y + 14);
                g2D.drawString("y",center.x - 12,center.y - j);
            }
            
            LinkedList solids = editor3D.getSolids();
            
            for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                ((Solid) listItr.next()).paintXY(g,this);
            
            editor3D.getSelectedTool().paint(g);
        }
    }
    
    public void addTool(Tool tool)
    {
        if(!tools.contains(tool)) tools.addLast(tool);
    }
    
    public Point getCenter()
    {
        Dimension size = getSize();
        
        return new Point(size.width / 2,size.height / 2);
    }
    
    public void setAxis(boolean enabled)
    {
        axisEnabled = enabled;
        
        updateImage();
        repaint();
    }
    
    public void listeners(boolean enabled)
    {
        if(this.enabled == enabled) return;
        
        if(enabled)
        {
            addMouseListener(myMouseListener);
            addMouseMotionListener(myMouseMotionListener);
            
            this.enabled = true;
        }
        else
        {
            removeMouseListener(myMouseListener);
            removeMouseMotionListener(myMouseMotionListener);
            
            this.enabled = false;
            
            setCursor(cursor);
        }
    }
}