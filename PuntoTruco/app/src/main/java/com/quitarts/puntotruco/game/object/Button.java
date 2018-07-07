package com.quitarts.puntotruco.game.object;

import com.quitarts.puntotruco.game.FactoryDrawable;
import com.quitarts.puntotruco.game.object.base.GraphicObject;

public class Button extends GraphicObject {
    public Button(FactoryDrawable.DrawableType drawableType) {
        super(drawableType);
    }

    public boolean isClicked(int x, int y) {
        return x > getX() && x < (getX() + getWidth()) && y > getY() && y < (getY() + getHeight());
    }
}