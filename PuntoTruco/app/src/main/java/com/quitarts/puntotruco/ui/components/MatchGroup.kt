package com.quitarts.puntotruco.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quitarts.puntotruco.ui.theme.Palette
import kotlin.math.min

@Composable
internal fun MatchGroup(count: Int, modifier: Modifier = Modifier) {
    val progress by animateFloatAsState(
        count.toFloat(),
        animationSpec = tween(190, easing = FastOutSlowInEasing),
        label = "matchsticks"
    )

    Canvas(modifier) {
        val side = min(size.width * .67f, size.height * .67f)
        val startX = (size.width - side) / 2f
        val startY = (size.height - side) / 2f
        val a = Offset(startX, startY)
        val b = Offset(startX + side, startY)
        val c = Offset(startX + side, startY + side)
        val d = Offset(startX, startY + side)
        val segments = listOf(a to b, b to c, c to d, d to a, d to b)
        // First, we paint the wooden sticks in reverse order, then all the heads.
        // This way, no wooden stick can hide a red head at the intersections.
        segments.indices.reversed().forEach { index ->
            val segment = segments[index]
            val alpha = (progress - index).coerceIn(0f, 1f)
            if (alpha > 0f)
                drawMatchBody(
                    segment.first,
                    segment.second,
                    alpha
                )
            else
                drawLine(
                    Palette.HarvestGold.copy(alpha = .23f),
                    segment.first,
                    segment.second,
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )
        }
        segments.indices.reversed().forEach { index ->
            val alpha = (progress - index).coerceIn(0f, 1f)
            if (alpha > 0f)
                drawMatchHead(
                    segments[index].second,
                    alpha
                )
        }
    }
}

private fun DrawScope.drawMatchBody(from: Offset, to: Offset, alpha: Float) {
    val thickness = matchThickness()

    drawLine(
        Color.Black.copy(alpha = .38f * alpha),
        from + Offset(3f, 4f),
        to + Offset(3f, 4f),
        strokeWidth = thickness + 3f,
        cap = StrokeCap.Round
    )
    drawLine(
        Palette.PottersClay.copy(alpha = alpha),
        from,
        to,
        strokeWidth = thickness + 2f,
        cap = StrokeCap.Round
    )
    drawLine(
        Palette.HarvestGold.copy(alpha = alpha),
        from,
        to,
        strokeWidth = thickness,
        cap = StrokeCap.Round)
    drawLine(
        Palette.Givry.copy(alpha = .5f * alpha),
        from - Offset(1f, 1f),
        to - Offset(1f, 1f),
        strokeWidth = thickness * .23f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawMatchHead(to: Offset, alpha: Float) {
    val thickness = matchThickness()

    drawCircle(
        Color.Black.copy(alpha = .24f * alpha),
        radius = thickness * .95f,
        center = to + Offset(1.5f, 2f)
    )
    drawCircle(
        Palette.Espresso.copy(alpha = alpha),
        radius = thickness * .82f,
        center = to
    )
    drawCircle(
        Palette.TallPoppy.copy(alpha = alpha),
        radius = thickness * .64f,
        center = to - Offset(1f, 1f))
    drawCircle(
        Palette.SandyBrown.copy(alpha = .65f * alpha),
        radius = thickness * .20f,
        center = to - Offset(thickness * .23f, thickness * .23f)
    )
}

private fun DrawScope.matchThickness(): Float = (size.minDimension * .047f).coerceIn(5f, 13f)

@Preview
@Composable
private fun MatchGroupPreview() {
    Column(modifier = Modifier.padding(12.dp)) {
        Row {
            for (count in 0..2) {
                MatchGroup(count = count, modifier = Modifier.size(80.dp))
            }
        }
        Row {
            for (count in 3..5) {
                MatchGroup(count = count, modifier = Modifier.size(80.dp))
            }
        }
    }
}

