package com.quitarts.puntotruco.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quitarts.puntotruco.ApplicationMain
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.Utils
import com.quitarts.puntotruco.components.Grid

class ViewModelMain : ViewModel() {
    private lateinit var gridUs: Grid
    private lateinit var gridThem: Grid
    private var gamePoints = Utils.getPoints()
    val actionShowAlertReset = MutableLiveData<Unit>()
    val actionShowAlertGameOver = MutableLiveData<String>()
    val actionRefresh = MutableLiveData<Unit>()

    fun init(gridUs: Grid, gridThem: Grid) {
        this.gridUs = gridUs
        this.gridThem = gridThem
    }

    fun actionUsSubstract() {
        gridUs.substract()
        actionRefresh.postValue(Unit)
    }

    fun actionUsAdd() {
        gridUs.add()
        checkForGameOver()
        actionRefresh.postValue(Unit)
    }

    fun actionThemSubstract() {
        gridThem.substract()
        actionRefresh.postValue(Unit)
    }

    fun actionThemAdd() {
        gridThem.add()
        checkForGameOver()
        actionRefresh.postValue(Unit)
    }

    fun actionMultiply() {
        actionShowAlertReset.postValue(Unit)
    }

    fun reset() {
        gridUs.reset()
        gridThem.reset()
        actionRefresh.postValue(Unit)
    }

    fun updatePoints() {
        if (gamePoints != Utils.getPoints()) {
            gamePoints = Utils.getPoints()
            reset()
        }
    }

    private fun checkForGameOver() {
        if (gridUs.getPoints() == gamePoints)
            actionShowAlertGameOver.postValue(ApplicationMain.applicationContext().getString(R.string.us))
        else if (gridThem.getPoints() == gamePoints)
            actionShowAlertGameOver.postValue(ApplicationMain.applicationContext().getString(R.string.them))
    }
}