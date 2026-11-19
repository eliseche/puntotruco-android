package com.quitarts.puntotruco.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quitarts.puntotruco.model.TableStyle
import com.quitarts.puntotruco.ui.theme.Palette

@Composable
internal fun TableTexture(style: TableStyle) {
    // Full-resolution material images make the grain legible on actual phone displays.
    // Kept in drawable-nodpi to avoid density scaling; no networking is involved.
    if (style == TableStyle.LEATHER || style == TableStyle.WOOD) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(
                if (style == TableStyle.LEATHER) com.quitarts.puntotruco.R.drawable.texture_leather
                else com.quitarts.puntotruco.R.drawable.texture_walnut
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        return
    }
    Canvas(Modifier.fillMaxSize()) {
        val colors =
            if (style == TableStyle.GREEN) listOf(Palette.GreenPea, Palette.TimberGreen, Palette.BottleGreen)
            else listOf(Palette.Cosmic, Palette.CocoaBean, Palette.CoffeeBean)

        drawRect(
            Brush.radialGradient(
                colors,
                center = Offset(size.width * .5f, size.height * .45f),
                radius = size.maxDimension * .8f
            )
        )
        // Preserve the original fine felt weave.
        var x = -size.height
        while (x < size.width + size.height) {
            drawLine(
                Color.White.copy(alpha = .014f),
                Offset(x, 0f),
                Offset(x + size.height, size.height),
                1f
            )
            drawLine(
                Color.Black.copy(alpha = .019f),
                Offset(x + size.height, 0f),
                Offset(x, size.height),
                1f
            )
            x += 9f
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF111111
)
@Composable
private fun TableTexturePreview() {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TableStyle.entries.chunked(2).forEach { styles ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                styles.forEach { style ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = style.name,
                            color = Palette.Givry,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 144.dp, height = 180.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            TableTexture(
                                style = style
                            )
                        }
                    }
                }
            }
        }
    }
}
