package com.example.qstreak

import android.app.Application
import com.example.qstreak.utils.initKoin
import timber.log.Timber

class QstreakApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
