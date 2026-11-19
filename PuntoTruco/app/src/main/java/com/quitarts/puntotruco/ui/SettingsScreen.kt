package com.quitarts.puntotruco.ui

import com.quitarts.puntotruco.R
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quitarts.puntotruco.model.*
import com.quitarts.puntotruco.ui.theme.*

@Composable
internal fun SettingsScreen(
    state: GameState,
    back: () -> Unit,
    onTarget: (Int) -> Unit,
    onCounter: (CounterStyle) -> Unit,
    onTable: (TableStyle) -> Unit,
    onHapticsEnabled: (Boolean) -> Unit,
    onScreenMode: (ScreenMode) -> Unit,
    onAbout: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        TextButton(
            onClick = back,
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text(
                "‹  " + stringResource(R.string.back),
                color = Palette.Givry,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.game))
        Text(stringResource(R.string.points_to_win), color = Palette.Givry)
        Spacer(Modifier.height(10.dp))
        var customPoints by rememberSaveable {
            mutableIntStateOf(state.customTarget.coerceIn(10, 30))
        }
        var customMode by rememberSaveable { mutableStateOf(state.target !in listOf(15, 30)) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChoiceButton(
                "15",
                !customMode && state.target == 15,
                Modifier.weight(1f)
            ) {
                customMode = false
                onTarget(15)
            }
            ChoiceButton(
                "30",
                !customMode && state.target == 30,
                Modifier.weight(1f)
            ) {
                customMode = false
                onTarget(30)
            }
            ChoiceButton(
                stringResource(R.string.custom_points),
                customMode,
                Modifier.weight(1.9f)
            ) {
                customMode = true
            }
        }
        if (customMode) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { customPoints-- },
                    enabled = customPoints > 10,
                    border = BorderStroke(1.dp, Palette.HarvestGold),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Palette.HarvestGold)
                ) {
                    Text("−", fontSize = 22.sp)
                }
                Text(
                    "$customPoints",
                    modifier = Modifier.width(76.dp),
                    textAlign = TextAlign.Center,
                    color = Palette.Givry,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = { customPoints++ },
                    enabled = customPoints < 30,
                    border = BorderStroke(1.dp, Palette.HarvestGold),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Palette.HarvestGold)
                ) {
                    Text("+", fontSize = 22.sp)
                }
            }
            Button(
                onClick = { onTarget(customPoints) },
                modifier = Modifier.fillMaxWidth(),
                enabled = customPoints != state.target,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Palette.HarvestGold,
                    contentColor = Palette.BottleGreen,
                    disabledContainerColor = Palette.HarvestGold.copy(alpha = .45f),
                    disabledContentColor = Palette.BottleGreen.copy(alpha = .65f)
                )
            ) {
                Text(stringResource(R.string.apply_points), fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.scoring_material))
        CounterStyle.entries.forEach { value ->
            ChoiceRow(
                when (value) {
                    CounterStyle.MATCHES -> stringResource(R.string.matches)
                    CounterStyle.BEANS -> stringResource(R.string.beans)
                    CounterStyle.TOKENS -> stringResource(R.string.tokens)
                },
                state.counter == value
            ) {
                onCounter(value)
            }
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.background))
        TableStyle.entries.forEach { value ->
            ChoiceRow(
                when (value) {
                    TableStyle.GREEN -> stringResource(R.string.green_felt)
                    TableStyle.BURGUNDY -> stringResource(R.string.burgundy_felt)
                    TableStyle.LEATHER -> stringResource(R.string.brown_leather)
                    TableStyle.WOOD -> stringResource(R.string.wood)
                },
                state.table == value
            ) {
                onTable(value)
            }
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.feedback_section))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.haptic_feedback),
                color = Palette.Givry,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = state.hapticsEnabled,
                onCheckedChange = onHapticsEnabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Palette.BottleGreen,
                    checkedTrackColor = Palette.HarvestGold
                )
            )
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.screen_behavior))
        ScreenMode.entries.forEach { mode ->
            ChoiceRow(
                when (mode) {
                    ScreenMode.ALWAYS_ON -> stringResource(R.string.screen_always_on)
                    ScreenMode.NORMAL -> stringResource(R.string.screen_normal)
                },
                state.screenMode == mode
            ) {
                onScreenMode(mode)
            }
        }
        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Palette.HarvestGold.copy(alpha = .35f))
        TextButton(
            onClick = onAbout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ⓘ  " + stringResource(R.string.about), color = Palette.Givry)
        }
    }
}

@Composable
private fun SectionTitle(label: String) {
    Text(
        label.uppercase(java.util.Locale.getDefault()),
        color = Palette.HarvestGold,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        fontSize = 12.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun ChoiceButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            Palette.HarvestGold.copy(alpha = if (selected) 1f else .45f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Palette.HarvestGold else Palette.BottleGreen,
            contentColor = if (selected) Palette.BottleGreen else Palette.Givry
        )
    ) {
        Text(
            label,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            fontSize = if (label.length > 10) 13.sp else 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChoiceRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Palette.GreenPea else Palette.BottleGreen,
        border = BorderStroke(
            1.dp,
            if (selected) Palette.HarvestGold else Palette.HarvestGold.copy(alpha = .22f)
        )
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                color = Palette.Givry,
                modifier = Modifier.weight(1f)
            )
            if (selected) Text(
                "✓",
                color = Palette.HarvestGold,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    var state by remember {
        mutableStateOf(
            GameState(
                ours = 0,
                theirs = 0,
                target = 30,
                customTarget = 20,
                counter = CounterStyle.MATCHES,
                table = TableStyle.GREEN,
                screenMode = ScreenMode.ALWAYS_ON,
                hapticsEnabled = true,
                victoryAcknowledged = false
            )
        )
    }

    MaterialTheme {
        Surface(color = Palette.BottleGreen) {
            SettingsScreen(
                state = state,
                back = {},
                onTarget = { target ->
                    state = state.copy(
                        ours = 0,
                        theirs = 0,
                        target = target,
                        customTarget = if (target in listOf(15, 30)) {
                            state.customTarget
                        } else {
                            target
                        }
                    )
                },
                onTable = { state = state.copy(table = it) },
                onCounter = { state = state.copy(counter = it) },
                onHapticsEnabled = { state = state.copy(hapticsEnabled = it) },
                onScreenMode = { state = state.copy(screenMode = it) },
                onAbout = {})
        }
    }
}

