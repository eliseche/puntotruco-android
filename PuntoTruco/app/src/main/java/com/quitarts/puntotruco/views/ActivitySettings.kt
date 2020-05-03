package com.quitarts.puntotruco.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.Utils
import kotlinx.android.synthetic.main.activity_settings.*

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_save -> {
                val pointsString = settings_points_spinner.selectedItem as String
                val points = Integer.parseInt(pointsString)
                if (points != Utils.getPoints())
                    showConfirmAlert()
                else
                    finish()

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        val arrayPoints = resources.getStringArray(R.array.arrays_points)
        val arrayPointsIndex = arrayPoints.indexOf(Utils.getPoints().toString())
        settings_points_spinner.setSelection(arrayPointsIndex)
    }

    //region Setup
    private fun init() {
    }
    //endregion

    private fun actionConfirm() {
        val pointsString = settings_points_spinner.selectedItem as String
        val points = Integer.parseInt(pointsString)
        Utils.savePoints(points)

        finish()
    }

    private fun actionCancel() {
    }

    //region Alert
    private fun showConfirmAlert() {
        runOnUiThread {
            val alertParams = ActivityAlert.AlertParams(
                getString(R.string.edit_points_title),
                getString(R.string.edit_points_description),
                getString(R.string.yes),
                getString(R.string.no)
            )

            ActivityAlert.completionPositive = this::actionConfirm
            ActivityAlert.completionNegative = this::actionCancel
            val intent = Intent(this, ActivityAlert::class.java)
            intent.putExtra(ActivityAlert.ALERT_PARAMS, alertParams)
            startActivity(intent)
        }
    }
    //endregion
}