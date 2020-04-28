package com.quitarts.puntotruco.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.quitarts.puntotruco.FactoryDrawable
import com.quitarts.puntotruco.Utils
import kotlin.math.roundToInt


class Grid @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var points = 0
    private var gamePoints = Utils.getPoints()

    fun add() {
        if (points < gamePoints) {
            points++
        }
    }

    fun substract() {
        if (points > 0) {
            points--
        }
    }

    fun reset() {
        gamePoints = Utils.getPoints()
        points = 0
    }

    fun getPoints() = points

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { c ->
            val size = c.height / (gamePoints / 5.0).roundToInt()
            val x = c.width / 2 - size / 2
            val y = 0
            var offsetY = 0

            for (i in 1..points) {
                var matchstick: Drawable? = null
                when (i % 5) {
                    0 -> matchstick = FactoryDrawable().getDrawable(FactoryDrawable.DrawableType.MATCHSTICK_5)
                    1 -> matchstick = FactoryDrawable().getDrawable(FactoryDrawable.DrawableType.MATCHSTICK_1)
                    2 -> matchstick = FactoryDrawable().getDrawable(FactoryDrawable.DrawableType.MATCHSTICK_2)
                    3 -> matchstick = FactoryDrawable().getDrawable(FactoryDrawable.DrawableType.MATCHSTICK_3)
                    4 -> matchstick = FactoryDrawable().getDrawable(FactoryDrawable.DrawableType.MATCHSTICK_4)
                }

                matchstick?.setBounds(x, y + offsetY, x + size, y + offsetY + size)
                matchstick?.draw(c)

                if (i % 5 == 0)
                    offsetY += size
            }
        }

        super.onDraw(canvas)
    }
}