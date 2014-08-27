package com.andremanuelbarbosa.editor3d;

import java.awt.*;

public interface Solid
{
    public void paintXY(Graphics g,MyJPanelXY myJPanelXY);
    public void paintXZ(Graphics g,MyJPanelXZ myJPanelXZ);
    public void paintYZ(Graphics g,MyJPanelYZ myJPanelYZ);
    
    public void applyTransform3D(Transform3D t3D);
    public void applyTransparentTransform3D(Transform3D t3D);
    
    public void setSelected(boolean selected);
    public boolean isSelected();
    
    public boolean intersects(MyJPanel myJPanel,int x,int y,int width,int height);
    
    public void initialState();
    
    public void undo();
    public void redo();
    
    public void clearHistory();
    
    public void setNumber(int number);
    public int getNumber();
}