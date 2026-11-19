package com.quitarts.puntotruco.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quitarts.puntotruco.model.CounterStyle
import com.quitarts.puntotruco.ui.theme.Palette
import kotlin.math.min

@Composable
internal fun ScoreGroup(count: Int, style: CounterStyle, modifier: Modifier = Modifier) {
    // Matches
    if (style == CounterStyle.MATCHES) MatchGroup(count, modifier)
    else {
        val beanImage = if (style == CounterStyle.BEANS) ImageBitmap.imageResource(com.quitarts.puntotruco.R.drawable.bean_ivory) else null
        Canvas(modifier) {
            val radius = min(size.width, size.height) * .12f
            val centers = listOf(
                Offset(.28f, .24f),
                Offset(.72f, .24f),
                Offset(.28f, .50f),
                Offset(.72f, .50f),
                Offset(.50f, .77f)
            )
            centers.forEachIndexed { index, pos ->
                val center = Offset(size.width * pos.x, size.height * pos.y)
                val active = index < count
                // Beans
                if (style == CounterStyle.BEANS) {
                    // Organic kidney-shaped bean. Each position gets a stable, slight rotation.
                    run {
                        // Long, slightly curved alubia silhouette matching the photographic marker.
                        fun beanPath(): Path = Path().apply {
                            val w = radius * 1.43f
                            val h = radius * .80f
                            moveTo(center.x - w, center.y + h * .12f)
                            cubicTo(
                                center.x - w * 1.05f,
                                center.y - h * .65f,
                                center.x - w * .42f,
                                center.y - h * 1.18f,
                                center.x + w * .25f,
                                center.y - h * .94f
                            )
                            cubicTo(
                                center.x + w * .95f,
                                center.y - h * .85f,
                                center.x + w * 1.13f,
                                center.y - h * .24f,
                                center.x + w * .90f,
                                center.y + h * .26f
                            )
                            cubicTo(
                                center.x + w * .72f,
                                center.y + h * .63f,
                                center.x + w * .34f,
                                center.y + h * .20f,
                                center.x + w * .13f,
                                center.y + h * .57f
                            )
                            cubicTo(
                                center.x - w * .44f,
                                center.y + h * 1.18f,
                                center.x - w * .95f,
                                center.y + h * .83f,
                                center.x - w,
                                center.y + h * .12f
                            )
                            close()
                        }
                        if (active) {
                            // Textured ivory bean from the approved visual reference.
                            // Keep each bean centered in the existing five-slot layout.
                            val w = (radius * 3.12f).toInt().coerceAtLeast(1)
                            val h = (radius * 2.36f).toInt().coerceAtLeast(1)
                            beanImage?.let { bitmap ->
                                drawImage(
                                    image = bitmap,
                                    srcOffset = IntOffset.Zero,
                                    srcSize = IntSize(bitmap.width, bitmap.height),
                                    dstOffset = IntOffset((center.x - w / 2f).toInt(), (center.y - h / 2f).toInt()),
                                    dstSize = IntSize(w, h)
                                )
                            }
                        } else {
                            drawPath(
                                beanPath(),
                                Palette.Zombie.copy(alpha = .13f)
                            )
                        }
                    }
                // Tokens
                } else {
                    drawCircle(
                        Color.Black.copy(alpha = if (active) .3f else .06f),
                        radius * 1.12f,
                        center + Offset(2f, 3f)
                    )
                    drawCircle(
                        Palette.HarvestGold.copy(alpha = if (active) 1f else .10f),
                        radius,
                        center
                    )
                    if (active) {
                        drawCircle(
                            Color.White.copy(alpha = .25f),
                            radius * .35f,
                            center - Offset(radius * .22f, radius * .25f)
                        )
                        drawCircle(
                            Palette.Dallas,
                            radius * .72f,
                            center,
                            style = Stroke(width = 2f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF123D2C
)
@Composable
private fun ScoreGroupPreview() {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CounterStyle.entries.forEach { style ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = style.name,
                    color = Palette.Givry,
                    fontSize = 12.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (count in listOf(0, 3, 5)) {
                        ScoreGroup(
                            count = count,
                            style = style,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
        }
    }
}
