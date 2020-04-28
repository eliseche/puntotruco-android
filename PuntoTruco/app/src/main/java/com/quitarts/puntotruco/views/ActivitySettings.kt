package com.quitarts.puntotruco.views

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.Utils
import kotlinx.android.synthetic.main.activity_settings.*

class ActivitySettings : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        settings_points_spinner.setOnItemSelectedListener(this);
    }

    override fun onResume() {
        super.onResume()

        val points = Utils.getPoints()
        val pointsArray = resources.getStringArray(R.array.arrays_points)
        val index = getIndex(pointsArray, points)
        settings_points_spinner.setSelection(index)
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        val pointsString = adapterView?.getItemAtPosition(i) as String
        val points = Integer.parseInt(pointsString)
        Utils.savePoints(points)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    private fun getIndex(pointsArray: Array<String>, points: Int): Int {
        for (i in pointsArray.indices) {
            if (Integer.parseInt(pointsArray[i]) == points)
                return i
        }

        return 0
    }
}