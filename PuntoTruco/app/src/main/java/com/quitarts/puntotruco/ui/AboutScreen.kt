package com.quitarts.puntotruco.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.ui.theme.Palette

private val AboutBackground = Palette.TimberGreen

@Composable
internal fun AboutScreen(back: () -> Unit) {
    val context = LocalContext.current
    val versionName = androidx.compose.runtime.remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AboutBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = back,
            modifier = Modifier
                .align(Alignment.Start)
                .heightIn(min = 48.dp)
        ) {
            Text(
                "‹  " + stringResource(R.string.back),
                color = Palette.Givry,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            stringResource(R.string.created_by).uppercase(),
            color = Palette.HarvestGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.quitarts_logo),
            contentDescription = "Quitarts - mobile apps and games",
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.weight(1f))
        Text(
            stringResource(R.string.app_name),
            color = Palette.Givry,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))
        Text(
            stringResource(R.string.about_version, versionName),
            color = Palette.Givry,
            fontSize = 13.sp
        )
        TextButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://eliseche.github.io/puntotruco-android/privacy-policy.html".toUri()
                )
                context.startActivity(intent)
            },
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text(
                stringResource(R.string.privacy_policy),
                color = Palette.HarvestGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen(back = {})
}
