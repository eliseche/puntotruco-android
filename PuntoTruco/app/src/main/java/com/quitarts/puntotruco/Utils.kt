package com.quitarts.puntotruco

import android.content.Context
import android.content.SharedPreferences
import com.quitarts.puntotruco.enums.PlayerType

class Utils {
    companion object {
        private const val preferenceKey = "PUNTOTRUCO_PREFERENCE"
        private const val preferenceKeyPoints = "${preferenceKey}_POINTS"
        private const val preferenceKeyUs = "${preferenceKey}_US"
        private const val preferenceKeyThem = "${preferenceKey}_THEM"
        private var sharedPreferences: SharedPreferences = ApplicationMain.applicationContext().getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)

        fun getPoints(): Int {
            return sharedPreferences.getInt(preferenceKeyPoints, 30)
        }

        fun savePoints(points: Int) {
            val editor = sharedPreferences.edit()
            editor.putInt(preferenceKeyPoints, points)
            editor.apply()
        }

        fun getPlayerNameUs(): String {
            return sharedPreferences.getString(preferenceKeyUs, "US")!!
        }

        fun getPlayerNameThem(): String {
            return sharedPreferences.getString(preferenceKeyThem, "THEM")!!
        }

        fun savePlaerName(playerName: String, playerType: PlayerType) {
            val editor = sharedPreferences.edit()
            if (playerType == PlayerType.US)
                editor.putString(preferenceKeyUs, playerName)
            else
                editor.putString(preferenceKeyThem, playerName)
            editor.apply()
        }
    }
}