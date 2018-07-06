package com.quitarts.puntotruco.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.quitarts.puntotruco.ContextContainer;
import com.quitarts.puntotruco.R;
import com.quitarts.puntotruco.Utils;
import com.quitarts.puntotruco.game.object.Points;
import com.quitarts.puntotruco.game.object.base.GraphicObject;

public class TrucoGrid {
    private TrucoView trucoView;
    private String textPlayer1;
    private String textPlayer2;
    private int hudHeight;
    private int matchHeight;
    private Paint paintBoard;
    private Paint paintText;
    private int matchOffsetY;

    public TrucoGrid(TrucoView trucoView) {
        this.trucoView = trucoView;

        initialize();
    }

    public void draw(Canvas canvas) {
        drawBoard(canvas);
        drawPlayers(canvas);
        drawMatches(canvas);
    }

    private void drawBoard(Canvas canvas) {
        canvas.drawLine(
                0,
                10 * Utils.getCanvasHeight() / 100,
                Utils.getCanvasWidth(),
                10 * Utils.getCanvasHeight() / 100,
                paintBoard);

        canvas.drawLine(
                Utils.getCanvasWidth() / 2,
                0,
                Utils.getCanvasWidth() / 2,
                Utils.getCanvasHeight() - hudHeight,
                paintBoard);
    }

    public void drawGoodLine(Canvas canvas) {
        canvas.drawLine(
                0,
                (10 * Utils.getCanvasHeight() / 100) + matchHeight * 3 + 3 * matchOffsetY + matchOffsetY /2,
                Utils.getCanvasWidth(),
                (10 * Utils.getCanvasHeight() / 100) + matchHeight * 3 + 3 * matchOffsetY + matchOffsetY /2,
                paintBoard);
    }

    private void drawPlayers(Canvas canvas) {
        canvas.drawText(textPlayer1,
                ((Utils.getCanvasWidth() / 2) - (paintText.measureText(textPlayer1))) / 2,
                (10 * Utils.getCanvasHeight() / 100) - ((10 * Utils.getCanvasHeight() / 100) - paintText.getTextSize()) / 2,
                paintText);

        canvas.drawText(textPlayer2,
                (Utils.getCanvasWidth() / 2) + ((Utils.getCanvasWidth() / 2) - paintText.measureText(textPlayer2)) / 2,
                (10 * Utils.getCanvasHeight() / 100) - ((10 * Utils.getCanvasHeight() / 100) - paintText.getTextSize()) / 2,
                paintText);
    }

    private void drawMatches(Canvas canvas) {
        int x = ((Utils.getCanvasWidth() / 2) - matchHeight) / 2;
        int y = (10 * Utils.getCanvasHeight() / 100) + matchOffsetY;
        int i = 0;
        int offsetY = 0;

        synchronized (Points.left) {
            for (Drawable match : Points.left) {
                match.setBounds(x,
                        y + offsetY,
                        x + match.getMinimumWidth(),
                        y + offsetY + match.getMinimumHeight());
                match.draw(canvas);
                i++;
                if (i % 5 == 0)
                    offsetY += match.getMinimumHeight() + matchOffsetY;
                if (i > 15)
                    drawGoodLine(canvas);
            }
        }

        x = (Utils.getCanvasWidth() / 2) + ((Utils.getCanvasWidth() / 2) - matchHeight) / 2;
        y = (10 * Utils.getCanvasHeight() / 100) + matchOffsetY;
        i = 0;
        offsetY = 0;

        synchronized (Points.right) {
            for (Drawable match : Points.right) {
                match.setBounds(
                        x,
                        y + offsetY,
                        x + match.getMinimumWidth(),
                        y + offsetY + match.getMinimumHeight());
                match.draw(canvas);
                i++;
                if (i % 5 == 0)
                    offsetY += match.getMinimumHeight() + matchOffsetY;
                if (i > 15)
                    drawGoodLine(canvas);
            }
        }

        if (Points.left.size() >= Utils.getPoints() || Points.right.size() >= Utils.getPoints()) {
            trucoView.showWinDialog();
        }
    }

    private void initialize() {
        textPlayer1 = ContextContainer.getApplicationContext().getResources().getString(R.string.us);
        textPlayer2 = ContextContainer.getApplicationContext().getResources().getString(R.string.them);
        hudHeight = 10 * Utils.getCanvasHeight() / 100;
        matchHeight = new GraphicObject(FactoryDrawable.DrawableType.MATCHSTICK_0).getGraphic().getMinimumHeight();

        paintBoard = new Paint();
        paintBoard.setColor(Color.WHITE);
        paintBoard.setStrokeWidth(2 * Utils.getScaleFactor());

        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(25 * Utils.getScaleFactor());
        paintText.setAntiAlias(true);

        int availableArea = Utils.getCanvasHeight() - (10 * Utils.getCanvasHeight() / 100) - hudHeight;
        matchOffsetY = 4 * availableArea / 100;
    }
}