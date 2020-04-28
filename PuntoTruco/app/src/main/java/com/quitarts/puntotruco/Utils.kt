package com.quitarts.puntotruco

import android.content.Context
import android.content.SharedPreferences

class Utils {
    companion object {
        private const val preferenceKey = "PUNTOTRUCO_PREFERENCE"
        private const val preferencePointsKey = "PUNTOTRUCO_PREFERENCE_POINTS"
        private var sharedPreferences: SharedPreferences = ApplicationMain.applicationContext().getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)

        fun getPoints(): Int {
            return sharedPreferences!!.getInt(preferencePointsKey, 30)
        }

        fun savePoints(points: Int) {
            val editor = sharedPreferences!!.edit()
            editor.putInt(preferencePointsKey, points)
            editor.apply()
        }
    }
}