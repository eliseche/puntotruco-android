package com.quitarts.puntotruco

import android.app.Application
import android.content.Context

class ApplicationMain : Application() {
    companion object {
        private lateinit var instance: ApplicationMain

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    init {
        instance = this
    }
}