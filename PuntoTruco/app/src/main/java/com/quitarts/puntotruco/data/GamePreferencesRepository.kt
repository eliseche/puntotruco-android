package com.quitarts.puntotruco.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.quitarts.puntotruco.model.*
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal val Context.gameDataStore by preferencesDataStore(name = "puntotruco_settings")

internal object Keys {
    val ours = intPreferencesKey("ours")
    val theirs = intPreferencesKey("theirs")
    val target = intPreferencesKey("target")
    val customTarget = intPreferencesKey("custom_target")
    val material = stringPreferencesKey("material")
    val background = stringPreferencesKey("background")
    val screenMode = stringPreferencesKey("screen_mode")
    val haptics = booleanPreferencesKey("haptics_enabled")
    val victoryAcknowledged = booleanPreferencesKey("victory_acknowledged")
    val legacyLanguage = stringPreferencesKey("language")
    val languageCleanupDone = booleanPreferencesKey("language_cleanup_done")
}

class GamePreferencesRepository(context: Context) {
    private val store = context.applicationContext.gameDataStore
    private val validTargets = 10..30
    private val DEFAULT_TARGET = 30
    private val DEFAULT_CUSTOM_TARGET = 20

    val states = store.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { prefs ->
            val target = prefs[Keys.target]
                ?.takeIf { it in validTargets }
                ?: DEFAULT_TARGET

            GameState(
                ours = (prefs[Keys.ours] ?: 0).coerceIn(0, target),
                theirs = (prefs[Keys.theirs] ?: 0).coerceIn(0, target),
                target = target,
                customTarget = (prefs[Keys.customTarget] ?: DEFAULT_CUSTOM_TARGET).coerceIn(validTargets),
                counter = enumValueOrDefault(prefs[Keys.material], CounterStyle.MATCHES),
                table = enumValueOrDefault(prefs[Keys.background], TableStyle.GREEN),
                // Migrate the legacy DIM setting to ALWAYS_ON
                screenMode = when (prefs[Keys.screenMode]) {
                    "NORMAL" -> ScreenMode.NORMAL
                    else -> ScreenMode.ALWAYS_ON
                },
                hapticsEnabled = prefs[Keys.haptics] ?: true,
                victoryAcknowledged = prefs[Keys.victoryAcknowledged] ?: true
            )
        }

    // One-time cleanup, for previous versions
    suspend fun clearLegacyLanguageOverrideOnce(): Boolean {
        var shouldClear = false
        store.edit { prefs ->
            if (prefs[Keys.languageCleanupDone] != true) {
                prefs.remove(Keys.legacyLanguage)
                prefs[Keys.languageCleanupDone] = true
                shouldClear = true
            }
        }

        return shouldClear
    }

    suspend fun save(gameState: GameState) {
        store.edit {
            it[Keys.ours] = gameState.ours
            it[Keys.theirs] = gameState.theirs
            it[Keys.target] = gameState.target
            it[Keys.customTarget] = gameState.customTarget
            it[Keys.material] = gameState.counter.name
            it[Keys.background] = gameState.table.name
            it[Keys.screenMode] = gameState.screenMode.name
            it[Keys.haptics] = gameState.hapticsEnabled
            it[Keys.victoryAcknowledged] = gameState.victoryAcknowledged
        }
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(value: String?, fallback: T): T =
        enumValues<T>().firstOrNull { it.name == value } ?: fallback
}
