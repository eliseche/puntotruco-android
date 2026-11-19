package com.quitarts.puntotruco

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.quitarts.puntotruco.data.GamePreferencesRepository
import com.quitarts.puntotruco.ui.GameScreen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.rgb(8, 47, 36)
        window.navigationBarColor = android.graphics.Color.BLACK
        window.decorView.systemUiVisibility = 0

        lifecycleScope.launch {
            /**
             * Clear the old in-app language override once on upgrade.
             * Afterward Android selects the locale from the device settings.
             */
            if (GamePreferencesRepository(applicationContext).clearLegacyLanguageOverrideOnce()) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
            }

            setContent {
                GameScreen()
            }
        }
    }
}