package com.quitarts.puntotruco

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class FactoryDrawable {
    companion object {
        val drawables: HashMap<DrawableType, Drawable> = hashMapOf()
    }

    enum class DrawableType {
        MATCHSTICK_1,
        MATCHSTICK_2,
        MATCHSTICK_3,
        MATCHSTICK_4,
        MATCHSTICK_5
    }

    fun getDrawable(typeToBuild: DrawableType): Drawable {
        var drawable = drawables.get(typeToBuild)

        if (drawable == null) {
            when (typeToBuild) {
                DrawableType.MATCHSTICK_1 -> drawable = ContextCompat.getDrawable(
                    ApplicationMain.applicationContext(),
                    R.drawable.matchstick_1
                )!!
                DrawableType.MATCHSTICK_2 -> drawable = ContextCompat.getDrawable(
                    ApplicationMain.applicationContext(),
                    R.drawable.matchstick_2
                )!!
                DrawableType.MATCHSTICK_3 -> drawable = ContextCompat.getDrawable(
                    ApplicationMain.applicationContext(),
                    R.drawable.matchstick_3
                )!!
                DrawableType.MATCHSTICK_4 -> drawable = ContextCompat.getDrawable(
                    ApplicationMain.applicationContext(),
                    R.drawable.matchstick_4
                )!!
                DrawableType.MATCHSTICK_5 -> drawable = ContextCompat.getDrawable(
                    ApplicationMain.applicationContext(),
                    R.drawable.matchstick_5
                )!!
            }

            drawables.put(typeToBuild, drawable)
        }

        return drawable
    }
}