package com.quitarts.puntotruco.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quitarts.puntotruco.BR
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.Utils
import com.quitarts.puntotruco.databinding.ActivityMainBinding
import com.quitarts.puntotruco.enums.PlayerType
import com.quitarts.puntotruco.viewmodels.ViewModelMain
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ActivityMain : AppCompatActivity(), FragmentPlayer.IFragmentPlayerListener {
    private lateinit var viewModelMain: ViewModelMain
    private lateinit var fragmentPlayer: FragmentPlayer

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelMain = ViewModelProvider(this).get(ViewModelMain::class.java)

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
    //endregion

    //region IFragmentPlayerListener
    override fun onDialogPositiveClick(playerName: String, playerType: PlayerType) {
        fragmentPlayer.dismiss()
        viewModelMain.savePlayerName(playerName, playerType)
    }
    //endregion

    //region Setup
    private fun init() {
        viewModelMain.init(grid_us, grid_them)

        viewModelMain.actionRefresh.observe(this, Observer {
            refreshUi()
        })

        viewModelMain.actionShowAlertReset.observe(this, Observer {
            showAlertReset()
        })

        viewModelMain.actionShowAlertGameOver.observe(this, Observer {
            it?.let {
                showAlertGameOver(it)
            }
        })

        view_us.onClick {
            changePlayerName(PlayerType.US)
        }

        view_them.onClick {
            changePlayerName(PlayerType.THEM)
        }
    }
    //endregion

    private fun refreshUi() {
        grid_us.invalidate()
        grid_them.invalidate()

        viewModelMain.updatePlayerNames()
    }

    private fun reset() {
        viewModelMain.reset()
    }

    private fun changePlayerName(playerType: PlayerType) {
        val args = Bundle()
        args.putSerializable("playerType", playerType)

        fragmentPlayer = FragmentPlayer()
        fragmentPlayer.isCancelable = false
        fragmentPlayer.arguments = args
        fragmentPlayer.show(supportFragmentManager, ActivityMain::class.java.simpleName)
    }

    //region Alerts
    private fun showAlertReset() {
        val alertParams = ActivityAlert.AlertParams(
            getString(R.string.warning),
            getString(R.string.reset_counter),
            getString(R.string.yes),
            getString(R.string.no)
        )
        ActivityAlert.completionPositive = this::reset
        val intent = Intent(this, ActivityAlert::class.java)
        intent.putExtra(ActivityAlert.ALERT_PARAMS, alertParams)
        startActivity(intent)
    }

    private fun showAlertGameOver(winner: String) {
        val alertParams = ActivityAlert.AlertParams(
            getString(R.string.game_win),
            String.format(getString(R.string.game_win_description), winner),
            getString(R.string.yes),
            getString(R.string.no)
        )
        ActivityAlert.completionPositive = this::reset
        val intent = Intent(this, ActivityAlert::class.java)
        intent.putExtra(ActivityAlert.ALERT_PARAMS, alertParams)
        startActivity(intent)
    }
    //endregion
}
