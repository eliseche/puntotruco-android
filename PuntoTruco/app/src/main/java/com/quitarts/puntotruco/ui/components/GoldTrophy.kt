package com.quitarts.puntotruco.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quitarts.puntotruco.ui.theme.*

@Composable
internal fun GoldTrophy(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width;
        val h = size.height
        val color = Palette.HarvestGold
        val cup = androidx.compose.ui.graphics.Path().apply {
            moveTo(.24f * w, .17f * h)
            lineTo(.76f * w, .17f * h)
            cubicTo(.75f * w, .49f * h, .65f * w, .62f * h, .5f * w, .64f * h)
            cubicTo(.35f * w, .62f * h, .25f * w, .49f * h, .24f * w, .17f * h)
            close()
        }
        drawPath(
            cup,
            color
        )
        drawArc(
            color,
            95f,
            210f,
            false,
            topLeft = Offset(.07f * w, .22f * h),
            size = Size(.34f * w, .35f * h),
            style = Stroke(width = .075f * w, cap = StrokeCap.Round)
        )
        drawArc(
            color,
            235f,
            210f,
            false,
            topLeft = Offset(.59f * w, .22f * h),
            size = Size(.34f * w, .35f * h),
            style = Stroke(width = .075f * w, cap = StrokeCap.Round)
        )
        drawRect(
            color,
            topLeft = Offset(.46f * w, .64f * h),
            size = Size(.08f * w, .19f * h)
        )
        drawRoundRect(
            color,
            topLeft = Offset(.29f * w, .82f * h),
            size = Size(.42f * w, .08f * h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(.025f * w)
        )
        drawLine(
            Palette.Givry.copy(alpha = .48f),
            Offset(.37f * w, .25f * h),
            Offset(.41f * w, .49f * h),
            strokeWidth = .035f * w,
            cap = StrokeCap.Round
        )
    }
}

@Preview
@Composable
private fun GoldTrophyPreview() {
    GoldTrophy(modifier = Modifier.size(48.dp))
}
