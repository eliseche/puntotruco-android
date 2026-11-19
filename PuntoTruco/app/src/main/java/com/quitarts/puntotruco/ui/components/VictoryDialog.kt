package com.quitarts.puntotruco.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.ui.theme.*

@Composable
internal fun VictoryDialog(winner: String, onViewScore: () -> Unit, onNewGame: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "victory")
    val glow by transition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse
        ),
        label = "victoryGlow"
    )
    Dialog(onDismissRequest = {}) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.verticalGradient(listOf(Palette.GreenPea, Palette.BottleGreen)))
                .border(1.5.dp, Palette.HarvestGold, RoundedCornerShape(26.dp))
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                "✦  ✦  ✦",
                color = Palette.HarvestGold.copy(alpha = glow),
                fontSize = 30.sp
            )
            GoldTrophy(
                Modifier
                    .size(72.dp)
                    .graphicsLayer {
                        scaleX = 0.94f + glow * 0.06f
                        scaleY = 0.94f + glow * 0.06f
                    }
            )
            Text(
                winner,
                color = Palette.Givry,
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(R.string.game_over),
                color = Palette.Givry.copy(alpha = .75f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            OutlinedButton(
                onClick = onViewScore,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                border = BorderStroke(1.dp, Palette.HarvestGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    stringResource(R.string.view_score),
                    color = Palette.Givry
                )
            }
            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Palette.HarvestGold, contentColor = Palette.Zeus),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    stringResource(R.string.new_game),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(
    widthDp = 480,
    heightDp = 640
)
@Composable
private fun VictoryDialogPreview() {
    VictoryDialog(
        winner = "We won!",
        onViewScore = {},
        onNewGame = {}
    )
}
