package com.sakawet.simplenotepad

import android.app.Application

class SimpleNotepadApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
