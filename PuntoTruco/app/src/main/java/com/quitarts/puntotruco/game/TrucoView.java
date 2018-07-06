package com.quitarts.puntotruco.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import javax.security.auth.callback.Callback;

import com.quitarts.puntotruco.ContextContainer;
import com.quitarts.puntotruco.R;
import com.quitarts.puntotruco.Utils;
import com.quitarts.puntotruco.game.object.Button;
import com.quitarts.puntotruco.game.object.Points;
import com.quitarts.puntotruco.game.object.base.GraphicObject;
import com.quitarts.puntotruco.ui.MainActivity;

public class TrucoView extends View implements Callback {
    private MainActivity trucoActivity;
    private BitmapDrawable background;
    private TrucoGrid grid;
    private Rect hudArea;
    private Paint hudPaint;
    private Button buttonReload;
    private Button buttonDeleteLeft;
    private Button buttonAddLeft;
    private Button buttonDeleteRight;
    private Button buttonAddRight;

    public TrucoView(Context context) {
        super(context);

        trucoActivity = (MainActivity) context;
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        grid.draw(canvas);
        drawHud(canvas);
        drawButtons(canvas);
    }

    private void drawBackground(Canvas c) {
        background.setBounds(0, 0, c.getWidth(), c.getHeight());
        background.draw(c);
    }

    private void drawHud(Canvas c) {
        c.drawRect(hudArea, hudPaint);
    }

    private void drawButtons(Canvas c) {
        buttonReload.getGraphic().draw(c);
        buttonDeleteLeft.getGraphic().draw(c);
        buttonAddLeft.getGraphic().draw(c);
        buttonDeleteRight.getGraphic().draw(c);
        buttonAddRight.getGraphic().draw(c);
    }

    private void initialize() {
        grid = new TrucoGrid(this);

        background = (BitmapDrawable) getResources().getDrawable(R.drawable.background_1);
        background.setTileModeX(Shader.TileMode.MIRROR);
        background.setTileModeY(Shader.TileMode.MIRROR);

        hudArea = new Rect();
        hudArea.set(0,
                Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100),
                Utils.getCanvasWidth(),
                Utils.getCanvasHeight());

        hudPaint = new Paint();
        hudPaint.setARGB(127, 0, 0, 0);

        buttonReload = new Button(FactoryDrawable.DrawableType.MULTIPLY);
        buttonReload.setX(Utils.getCanvasWidth() / 2 - buttonReload.getWidth() / 2);
        buttonReload.setY(Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) + buttonReload.getHeight() / 2);

        buttonDeleteLeft = new Button(FactoryDrawable.DrawableType.SUBSTRACT);
        buttonDeleteLeft.setX(0 + buttonDeleteLeft.getWidth());
        buttonDeleteLeft.setY(Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) + buttonDeleteLeft.getHeight() / 2);

        buttonAddLeft = new Button(FactoryDrawable.DrawableType.ADD);
        buttonAddLeft.setX(buttonReload.getX() - buttonAddLeft.getWidth() * 2);
        buttonAddLeft.setY(Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) + buttonAddLeft.getHeight() / 2);

        buttonDeleteRight = new Button(FactoryDrawable.DrawableType.SUBSTRACT);
        buttonDeleteRight.setX(buttonReload.getX() + buttonDeleteRight.getWidth() * 2);
        buttonDeleteRight.setY(Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) + buttonDeleteRight.getHeight() / 2);

        buttonAddRight = new Button(FactoryDrawable.DrawableType.ADD);
        buttonAddRight.setX(Utils.getCanvasWidth() - buttonAddRight.getWidth() * 2);
        buttonAddRight.setY(Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) + buttonAddRight.getHeight() / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hudClick((int) event.getX(), (int) event.getY());
            postInvalidate();
        }

        return super.onTouchEvent(event);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void hudClick(int x, int y) {
        if (buttonReload.isClicked(x, y)) {
            trucoActivity.showResetDialog();
        } else if (buttonDeleteLeft.isClicked(x, y)) {
            synchronized (Points.left) {
                int lastIndex = Points.left.size() - 1;
                if (Points.left.size() != 0) {
                    Points.left.remove(lastIndex);
                }
            }
        } else if (buttonAddLeft.isClicked(x, y)) {
            synchronized (Points.left) {
                if (Points.left.size() < Utils.getPoints()) {
                    int lastIndex = Points.left.size() % 5;
                    GraphicObject image = new GraphicObject(FactoryDrawable.DrawableType.valueOf("MATCHSTICK_" + lastIndex));
                    Points.left.add(image.getGraphic());
                }
            }
        } else if (buttonDeleteRight.isClicked(x, y)) {
            synchronized (Points.right) {
                int lastIndex = Points.right.size() - 1;
                if (Points.right.size() != 0) {
                    Points.right.remove(lastIndex);
                }
            }
        } else if (buttonAddRight.isClicked(x, y)) {
            synchronized (Points.right) {
                if (Points.right.size() < Utils.getPoints()) {
                    int lastIndex = Points.right.size() % 5;
                    GraphicObject image = new GraphicObject(FactoryDrawable.DrawableType.valueOf("MATCHSTICK_" + lastIndex));
                    Points.right.add(image.getGraphic());
                }
            }
        }
    }

    public void showWinDialog() {
        trucoActivity.showWinDialog();
    }

    public void resetGame() {
        synchronized (Points.left) {
            Points.left.clear();
        }

        synchronized (Points.right) {
            Points.right.clear();
        }

        invalidate();
    }
}