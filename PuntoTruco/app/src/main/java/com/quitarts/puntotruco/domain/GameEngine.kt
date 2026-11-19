package com.quitarts.puntotruco.domain

import com.quitarts.puntotruco.model.GameState

object GameEngine {
    fun add(state: GameState, ours: Boolean): GameState {
        if (state.ours >= state.target || state.theirs >= state.target) return state

        return if (ours) {
            state.copy(ours = state.ours + 1)
        } else {
            state.copy(theirs = state.theirs + 1)
        }
    }

    fun undo(state: GameState, ours: Boolean): GameState {
        return if (ours) {
            state.copy(ours = (state.ours - 1).coerceAtLeast(0))
        } else {
            state.copy(theirs = (state.theirs - 1).coerceAtLeast(0))
        }
    }
}