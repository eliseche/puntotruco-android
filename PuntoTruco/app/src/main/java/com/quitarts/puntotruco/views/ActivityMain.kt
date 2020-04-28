package com.quitarts.puntotruco.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.quitarts.puntotruco.BR
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.databinding.ActivityMainBinding
import com.quitarts.puntotruco.viewmodels.ViewModelMain
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    private lateinit var viewModelMain: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelMain = ViewModelProviders.of(this).get(ViewModelMain::class.java)


        val binding: ActivityMainBinding? = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.setVariable(BR.viewModelMain, viewModelMain)
        binding?.run {
            lifecycleOwner = this@ActivityMain
        }

        init()
    }

    override fun onResume() {
        super.onResume()

        viewModelMain.updatePoints()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, ActivitySettings::class.java)
                startActivity(intent)

                return true
            }

            R.id.menu_about -> {
                val intent = Intent(this, ActivityAbout::class.java)
                startActivity(intent)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        viewModelMain.init(grid_us, grid_them)

        viewModelMain.actionRefresh.observe(this, Observer {
            refreshUi()
        })

        viewModelMain.actionShowAlertReset.observe(this, Observer {
            showAlertReset()
        })

        viewModelMain.actionShowAlertGameOver.observe(this, Observer {
            showAlertGameOver()
        })
    }

    private fun refreshUi() {
        grid_us.invalidate()
        grid_them.invalidate()
    }

    // Alerts
    fun showAlertReset() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(resources.getString(R.string.reset_counter))
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, i ->
            viewModelMain.reset()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, i -> }
        builder.create().show()
    }

    fun showAlertGameOver() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(resources.getString(R.string.game_win))
        builder.setPositiveButton(resources.getString(R.string.accept)) { dialogInterface, i ->
        }
        builder.create().show()
    }
}
