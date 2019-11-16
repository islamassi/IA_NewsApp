package com.islamassi.latestnews

import android.app.Application

/**
 * Application class
 */
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}