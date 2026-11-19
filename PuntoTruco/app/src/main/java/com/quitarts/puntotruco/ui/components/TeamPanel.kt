package com.quitarts.puntotruco.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.model.CounterStyle
import com.quitarts.puntotruco.ui.theme.Palette

@Composable
internal fun TeamPanel(
    name: String,
    score: Int,
    target: Int,
    add: () -> Unit,
    undo: () -> Unit,
    canUndo: Boolean,
    counter: CounterStyle,
    hapticsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var previousScore by remember { mutableIntStateOf(score) }
    val pulse = remember { Animatable(1f) }
    LaunchedEffect(score) {
        if (score != previousScore) {
            pulse.snapTo(if (score > previousScore) 1.085f else .94f)
            pulse.animateTo(1f, tween(190, easing = FastOutSlowInEasing))
        }
        previousScore = score
    }
    var previousMaterialScore by remember { mutableIntStateOf(score) }
    val materialPulse = remember { Animatable(1f) }
    LaunchedEffect(score) {
        if (score != previousMaterialScore) {
            materialPulse.snapTo(if (score > previousMaterialScore) .96f else 1.04f)
            materialPulse.animateTo(1f, tween(190, easing = FastOutSlowInEasing))
        }
        previousMaterialScore = score
    }
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier
            .fillMaxHeight()
            .clip(shape)
            .background(Brush.verticalGradient(listOf(
                Palette.GreenPea.copy(alpha = 0xD9 / 255f),
                Palette.BottleGreen.copy(alpha = 0xF5 / 255f))))
            .border(1.dp, Palette.HarvestGold.copy(alpha = .75f), shape)
            .padding(horizontal = 8.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.widthIn(60.dp),
            shape = RoundedCornerShape(50),
            color = Palette.TimberGreen,
            border = BorderStroke(1.dp, Palette.HarvestGold.copy(alpha = .9f)),
            shadowElevation = 3.dp
        ) {
            Text(
                name,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Palette.Givry,
                letterSpacing = .5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.height(12.dp))
        Surface(
            color = Palette.TimberGreen,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Palette.HarvestGold.copy(alpha = .5f))
        ) {
            Text(
                "$score",
                modifier = Modifier
                    .graphicsLayer { scaleX = pulse.value; scaleY = pulse.value }
                    .padding(horizontal = 24.dp, vertical = 2.dp),
                color = Palette.Givry,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 54.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(12.dp))
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val rows = (target + 9) / 10
            val spacing = 7.dp
            val cellWidth = (this@BoxWithConstraints.maxWidth - spacing) / 2
            val cellHeight = ((this@BoxWithConstraints.maxHeight - spacing * (rows - 1)) / rows).coerceAtMost(124.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(rows) { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                        repeat(2) { col ->
                            val index = row * 2 + col
                            if (index * 5 < target) {
                                ScoreGroup(
                                    (score - index * 5).coerceIn(0, 5),
                                    counter,
                                    Modifier
                                        .width(cellWidth)
                                        .height(cellHeight)
                                        .graphicsLayer {
                                            scaleX = materialPulse.value; scaleY =
                                            materialPulse.value
                                        }
                                )
                            } else
                                Spacer(
                                    Modifier
                                        .width(cellWidth)
                                        .height(cellHeight)
                                )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                if (score < target) {
                    if (hapticsEnabled) view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    add()
                }
            },
            enabled = score < target,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Palette.HarvestGold, contentColor = Palette.Zeus)
        ) {
            Text(
                stringResource(R.string.add_point),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
        TextButton(
            onClick = undo,
            enabled = canUndo,
            modifier = Modifier.height(42.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Palette.Givry, disabledContentColor = Palette.Givry.copy(alpha = .68f))
        ) {
            Text(
                "↶ " + stringResource(R.string.undo),
                fontSize = 12.sp,
                color = if (canUndo) Palette.Givry else Palette.Givry.copy(alpha = .68f)
            )
        }
    }
}

@Preview
@Composable
private fun TeamPanelPreview() {
    var score by remember { mutableIntStateOf(17) }
    val target = 30

    TeamPanel(
        name = "Ours",
        score = score,
        target = target,
        add = { score = (score + 1).coerceAtMost(target) },
        undo = { score = (score - 1).coerceAtLeast(0) },
        canUndo = score > 0,
        counter = CounterStyle.MATCHES,
        hapticsEnabled = false,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )
}

