package com.quitarts.puntotruco.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.util.HashMap;
import java.util.Map;

import com.quitarts.puntotruco.ContextContainer;
import com.quitarts.puntotruco.R;
import com.quitarts.puntotruco.Utils;

public class FactoryDrawable {
    private static Map<DrawableType, BitmapDrawable> bitmapDrawables = new HashMap<>();

    public static enum DrawableType {
        ADD,
        SUBSTRACT,
        MULTIPLY,
        MATCHSTICK_0,
        MATCHSTICK_1,
        MATCHSTICK_2,
        MATCHSTICK_3,
        MATCHSTICK_4
    }

    public static BitmapDrawable createDrawable(DrawableType typeToBuild) {
        Bitmap bitmap = null;
        BitmapDrawable bitmapDrawable = bitmapDrawables.get(typeToBuild);

        if (bitmapDrawable == null) {
            switch (typeToBuild) {
                case MATCHSTICK_0:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.matchstick_0);
                    break;
                case MATCHSTICK_1:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.matchstick_1);
                    break;
                case MATCHSTICK_2:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.matchstick_2);
                    break;
                case MATCHSTICK_3:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.matchstick_3);
                    break;
                case MATCHSTICK_4:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.matchstick_4);
                    break;
            }

            switch (typeToBuild) {
                case MATCHSTICK_0:
                case MATCHSTICK_1:
                case MATCHSTICK_2:
                case MATCHSTICK_3:
                case MATCHSTICK_4:
                    bitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            (int) (Utils.getCanvasWidth() * 12 / 100),
                            (int) (Utils.getCanvasWidth() * 12 / 100),
                            false);
                    break;
            }

            bitmapDrawable = new BitmapDrawable(ContextContainer.getApplicationContext().getResources(), bitmap);
            bitmapDrawables.put(typeToBuild, bitmapDrawable);
        }

        if (typeToBuild == DrawableType.ADD || typeToBuild == DrawableType.SUBSTRACT || typeToBuild == DrawableType.MULTIPLY) {
            switch (typeToBuild) {
                case ADD:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.add);
                    break;
                case SUBSTRACT:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.substract);
                    break;
                case MULTIPLY:
                    bitmap = BitmapFactory.decodeResource(ContextContainer.getApplicationContext().getResources(), R.drawable.multiply);
                    break;
            }

            switch (typeToBuild) {
                case ADD:
                case MULTIPLY:
                case SUBSTRACT:
                    bitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            (int) (Utils.getCanvasWidth() * 8 / 100),
                            (int) (Utils.getCanvasWidth() * 8 / 100),
                            false);
                    break;
            }

            bitmapDrawable = new BitmapDrawable(ContextContainer.getApplicationContext().getResources(), bitmap);
            bitmapDrawables.put(typeToBuild, bitmapDrawable);
        }

        return bitmapDrawable;
    }
}