package com.quitarts.puntotruco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.quitarts.puntotruco.data.GamePreferencesRepository
import com.quitarts.puntotruco.domain.GameEngine
import com.quitarts.puntotruco.model.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gamePreferencesRepository = GamePreferencesRepository(application)
    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()
    private val _loaded = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded.asStateFlow()
    private val saves = Channel<Pair<GameState, CompletableDeferred<Unit>?>>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            for ((state, done) in saves) {
                try {
                    gamePreferencesRepository.save(state)
                    done?.complete(Unit)
                } catch (e: Exception) {
                    done?.completeExceptionally(e)
                }
            }
        }

        viewModelScope.launch {
            // Read persisted values once, subsequent mutations are owned by this ViewModel
            _uiState.value = gamePreferencesRepository.states.first()
            _loaded.value = true
        }
    }

    fun exitAfterSaving(onSaved: () -> Unit) {
        viewModelScope.launch {
            val done = CompletableDeferred<Unit>()
            saves.send(_uiState.value to done)
            try {
                done.await()
                onSaved()
            } catch (_: Exception) {
                // Do not claim data saved on failure
            }
        }
    }

    fun reset(target: Int = _uiState.value.target) {
        require(target in 10..30)
        update(
            _uiState.value.copy(
                ours = 0,
                theirs = 0,
                target = target,
                customTarget = if (target != 15 && target != 30) target else _uiState.value.customTarget,
                victoryAcknowledged = false
            )
        )
    }

    fun add(ours: Boolean) {
        val currentGameState = _uiState.value
        val nextGameState = GameEngine.add(currentGameState, ours)
        if (nextGameState != currentGameState) {
            update(nextGameState.copy(victoryAcknowledged = false))
        }
    }

    fun undo(ours: Boolean) = update(GameEngine.undo(_uiState.value, ours))

    fun canUndo(ours: Boolean): Boolean = if (ours) _uiState.value.ours > 0 else _uiState.value.theirs > 0

    fun setCounter(counterStyle: CounterStyle) = update(_uiState.value.copy(counter = counterStyle))

    fun setTable(tableStyle: TableStyle) = update(_uiState.value.copy(table = tableStyle))

    fun setScreenMode(screenMode: ScreenMode) = update(_uiState.value.copy(screenMode = screenMode))

    fun setHapticsEnabled(hapticsEnabled: Boolean) = update(_uiState.value.copy(hapticsEnabled = hapticsEnabled))

    fun acknowledgeVictory() = update(_uiState.value.copy(victoryAcknowledged = true))

    fun replayVictory() = update(_uiState.value.copy(victoryAcknowledged = false))

    private fun update(gameState: GameState) {
        if (!_loaded.value) return

        _uiState.value = gameState
        saves.trySend(gameState to null)
    }
}
