package com.quitarts.puntotruco.game.object.base;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.quitarts.puntotruco.game.FactoryDrawable;

public class GraphicObject implements Cloneable {
    private BitmapDrawable graphic;
    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float xCenter = 0.0f;
    protected float yCenter = 0.0f;

    public GraphicObject(FactoryDrawable.DrawableType drawableType) {
        graphic = FactoryDrawable.createDrawable(drawableType);
    }

    public BitmapDrawable getGraphic() {
        return graphic;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        calculateCenter();
        calculateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        calculateCenter();
        calculateBounds();
    }

    public float getXCenter() {
        return xCenter;
    }

    public void setXCenter(float xCenter) {
        this.xCenter = xCenter;
    }

    public float getYCenter() {
        return yCenter;
    }

    public void setYCenter(float yCenter) {
        this.yCenter = yCenter;
    }

    public int getWidth() {
        return graphic.getMinimumWidth();
    }

    public int getHeight() {
        return graphic.getMinimumHeight();
    }

    public void calculateCenter() {
        xCenter = x + getWidth() / 2.0f;
        yCenter = y + getHeight() / 2.0f;
    }

    public void calculateBounds() {
        graphic.setBounds((int) x, (int) y, (int) x + getWidth(), (int) y + getHeight());
    }

    @Override
    public Object clone() {
        try {
            GraphicObject clonedGraphicObject = (GraphicObject) super.clone();
            return clonedGraphicObject;
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
        }

        return null;
    }
}