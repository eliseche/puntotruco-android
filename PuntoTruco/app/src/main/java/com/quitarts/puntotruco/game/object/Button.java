package com.quitarts.puntotruco.game.object;

import android.util.Log;

import com.quitarts.puntotruco.game.FactoryDrawable;
import com.quitarts.puntotruco.game.object.base.GraphicObject;

public class Button extends GraphicObject {
    public Button(FactoryDrawable.DrawableType drawableType) {
        super(drawableType);
    }

    public boolean isClicked(int x, int y) {

        if (x > getX() && x < (getX() + getWidth()) && y > getY() && y < (getY() + getHeight()))
            return true;

        return false;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
        }

        return null;
    }
}