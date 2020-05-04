package com.quitarts.puntotruco.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quitarts.puntotruco.ApplicationMain
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.Utils
import com.quitarts.puntotruco.components.Grid
import com.quitarts.puntotruco.enums.PlayerType

class ViewModelMain : ViewModel() {
    private lateinit var gridUs: Grid
    private lateinit var gridThem: Grid
    lateinit var gridUsPlayerName: MutableLiveData<String>
    lateinit var gridUsPlayerNameAndPoints: MutableLiveData<String>
    lateinit var gridThemPlayerName: MutableLiveData<String>
    lateinit var gridThemPlayerNameAndPoints: MutableLiveData<String>
    private var gamePoints = Utils.getPoints()
    val actionShowAlertReset = MutableLiveData<Unit>()
    val actionShowAlertGameOver = MutableLiveData<String>()
    val actionRefresh = MutableLiveData<Unit>()

    fun init(gridUs: Grid, gridThem: Grid) {
        this.gridUs = gridUs
        this.gridThem = gridThem
        this.gridUsPlayerName = MutableLiveData()
        this.gridThemPlayerName = MutableLiveData()
        this.gridUsPlayerNameAndPoints = MutableLiveData()
        this.gridThemPlayerNameAndPoints = MutableLiveData()
        updatePlayerNames()
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

    fun updatePlayerNames() {
        gridUsPlayerName.postValue(Utils.getPlayerNameUs())
        gridThemPlayerName.postValue(Utils.getPlayerNameThem())

        gridUsPlayerNameAndPoints.postValue("${Utils.getPlayerNameUs()} (${gridUs.getPoints()})")
        gridThemPlayerNameAndPoints.postValue("${Utils.getPlayerNameThem()} (${gridThem.getPoints()})")
    }

    fun savePlayerName(playerName: String, playerType: PlayerType) {
        Utils.savePlaerName(playerName, playerType)
        updatePlayerNames()
    }

    private fun checkForGameOver() {
        if (gridUs.getPoints() == gamePoints)
            actionShowAlertGameOver.postValue(gridUsPlayerName.value)
        else if (gridThem.getPoints() == gamePoints)
            actionShowAlertGameOver.postValue(gridThemPlayerName.value)
    }
}