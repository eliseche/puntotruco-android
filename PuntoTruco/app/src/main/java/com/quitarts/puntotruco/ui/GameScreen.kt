package com.quitarts.puntotruco.ui

import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.model.CounterStyle
import com.quitarts.puntotruco.model.ScreenMode
import com.quitarts.puntotruco.model.TableStyle
import com.quitarts.puntotruco.ui.components.TableTexture
import com.quitarts.puntotruco.ui.components.TeamPanel
import com.quitarts.puntotruco.ui.components.VictoryDialog
import com.quitarts.puntotruco.ui.theme.Palette
import com.quitarts.puntotruco.viewmodel.GameViewModel

private enum class Screen {
    GAME,
    SETTINGS,
    ABOUT
}

@Composable
internal fun GameScreen(vm: GameViewModel = viewModel()) {
    val gameState by vm.uiState.collectAsState()
    val loaded by vm.loaded.collectAsState()
    var currentScreen by remember { mutableStateOf(Screen.GAME) }
    var confirmReset by remember { mutableStateOf(false) }
    var pendingTarget by remember { mutableStateOf<Int?>(null) }
    var confirmExit by remember { mutableStateOf(false) }
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenMode = gameState.screenMode
    val gameVisible = loaded && currentScreen == Screen.GAME

    val winner = when {
        gameState.ours >= gameState.target -> stringResource(R.string.won_ours)
        gameState.theirs >= gameState.target -> stringResource(R.string.won_theirs)
        else -> null
    }
    val gameWon = winner != null && currentScreen == Screen.GAME
    val victoryPending = gameWon && !gameState.victoryAcknowledged
    val confirmationOpen = confirmExit || confirmReset || pendingTarget != null

    DisposableEffect(activity, lifecycleOwner, gameVisible, screenMode) {
        val window = activity?.window
        var resumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

        fun applyScreenPolicy() {
            if (resumed && gameVisible && screenMode == ScreenMode.ALWAYS_ON) {
                window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        applyScreenPolicy()

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    resumed = true
                    applyScreenPolicy()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    resumed = false
                    applyScreenPolicy()
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    BackHandler(
        enabled = loaded && !confirmationOpen && !victoryPending
    ) {
        when (currentScreen) {
            Screen.ABOUT -> currentScreen = Screen.SETTINGS
            Screen.SETTINGS -> currentScreen = Screen.GAME
            Screen.GAME -> confirmExit = true
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(primary = Palette.HarvestGold, onSurface = Palette.Givry, surface = Palette.BottleGreen)
    ) {
        Box(Modifier.fillMaxSize()) {
            // Sets the background
            TableTexture(gameState.table)

            if (loaded) {
                when (currentScreen) {
                    Screen.ABOUT -> {
                        AboutScreen(
                            back = { currentScreen = Screen.SETTINGS }
                        )
                    }

                    Screen.SETTINGS -> {
                        SettingsScreen(
                            gameState,
                            back = { currentScreen = Screen.GAME },
                            onTarget = { if (it != gameState.target) pendingTarget = it },
                            onCounter = vm::setCounter,
                            onTable = vm::setTable,
                            onHapticsEnabled = vm::setHapticsEnabled,
                            onScreenMode = vm::setScreenMode,
                            onAbout = { currentScreen = Screen.ABOUT }
                        )
                    }

                    Screen.GAME -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .navigationBarsPadding()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(66.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "PuntoTruco",
                                    color = Palette.Givry,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 34.sp,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    modifier = Modifier.size(48.dp),
                                    onClick = { currentScreen = Screen.SETTINGS }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(27.dp),
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = stringResource(R.string.settings),
                                        tint = Palette.HarvestGold
                                    )
                                }
                                IconButton(
                                    modifier = Modifier.size(48.dp),
                                    onClick = { confirmReset = true }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(27.dp),
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(R.string.reset),
                                        tint = Palette.HarvestGold
                                    )
                                }
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TeamPanel(
                                    stringResource(R.string.ours),
                                    gameState.ours,
                                    gameState.target,
                                    { vm.add(true) },
                                    { vm.undo(true) },
                                    vm.canUndo(true),
                                    gameState.counter,
                                    gameState.hapticsEnabled,
                                    Modifier.weight(1f)
                                )
                                TeamPanel(
                                    stringResource(R.string.theirs),
                                    gameState.theirs,
                                    gameState.target,
                                    { vm.add(false) },
                                    { vm.undo(false) },
                                    vm.canUndo(false),
                                    gameState.counter,
                                    gameState.hapticsEnabled,
                                    Modifier.weight(1f)
                                )
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧉", fontSize = 25.sp)
                            }
                        }
                    }
                }
            } else {
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = Palette.HarvestGold
                )
            }

            // Show celebration
            if (loaded && gameWon && gameState.victoryAcknowledged) {
                TextButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 6.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = Palette.BottleGreen),
                    onClick = {
                        vm.replayVictory()
                    }
                ) {
                    Text(stringResource(R.string.show_victory), color = Palette.HarvestGold)
                }
            }
        }

        // Reset game
        if (confirmReset) {
            AlertDialog(
                title = { Text(stringResource(R.string.new_game)) },
                text = { Text(stringResource(R.string.reset_question)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            vm.reset()
                            confirmReset = false
                        }
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            confirmReset = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                onDismissRequest = {
                    confirmReset = false
                }
            )
        }

        // Exit game
        if (confirmExit) {
            AlertDialog(
                title = { Text(stringResource(R.string.exit_title)) },
                text = { Text(stringResource(R.string.exit_question)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            confirmExit = false
                            vm.exitAfterSaving { activity?.finish() }
                        }
                    ) {
                        Text(stringResource(R.string.exit_app))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            confirmExit = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                onDismissRequest = {
                    confirmExit = false
                }
            )
        }

        // Change custom score
        if (pendingTarget != null) {
            AlertDialog(
                title = { Text(stringResource(R.string.change_target)) },
                text = { Text(stringResource(R.string.change_target_question)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            vm.reset(pendingTarget!!)
                            pendingTarget = null
                        }
                    ) {
                        Text(stringResource(R.string.continue_game))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            pendingTarget = null
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                onDismissRequest = {
                    pendingTarget = null
                }
            )
        }

        // Team wins
        if (loaded && !confirmationOpen && victoryPending) {
            VictoryDialog(
                winner = winner,
                onViewScore = { vm.acknowledgeVictory() },
                onNewGame = { vm.reset() }
            )
        }
    }
}

@Preview
@Composable
private fun GamePreview() {
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            TableTexture(TableStyle.GREEN)

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Nosotros" to 17, "Ellos" to 7).forEach { (name, score) ->
                    TeamPanel(
                        name = name,
                        score = score,
                        target = 30,
                        add = {},
                        undo = {},
                        canUndo = true,
                        counter = CounterStyle.MATCHES,
                        hapticsEnabled = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}