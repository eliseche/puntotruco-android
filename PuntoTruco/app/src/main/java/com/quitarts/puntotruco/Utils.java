package com.quitarts.puntotruco;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    private static int width;
    private static int height;
    private static SharedPreferences sharedPreferences;
    private static final String preferenceKey = "PUNTOTRUCO_PREFERENCE";
    private static final String savedPointsKey = "SAVED_POINTS_KEY";

    private Utils() {
    }

    public static void setCanvasSize(int width, int height) {
        Utils.width = width;
        Utils.height = height;
    }

    public static int getCanvasWidth() {
        return width;
    }

    public static int getCanvasHeight() {
        return height;
    }

    public static float getScaleFactor() {
        return ContextContainer.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static void configurePreferences() {
        sharedPreferences = ContextContainer.getApplicationContext().getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
    }

    public static int getPoints() {
        return sharedPreferences.getInt(savedPointsKey, 30);
    }

    public static void savePoints(int points) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(savedPointsKey, points);
        editor.apply();
    }
}