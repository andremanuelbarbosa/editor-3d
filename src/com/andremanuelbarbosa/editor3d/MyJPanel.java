package com.andremanuelbarbosa.editor3d;

import java.awt.*;

public interface MyJPanel
{
    public void addTool(Tool tool);
    public Point getCenter();
    public void setAxis(boolean enabled);
    public void listeners(boolean enabled);
}