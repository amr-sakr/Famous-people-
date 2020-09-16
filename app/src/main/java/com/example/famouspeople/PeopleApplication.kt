package com.example.famouspeople

import android.app.Application
import com.example.famouspeople.di.component.AppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class PeopleApplication : Application() {

    private lateinit var _appComponent: AppComponent
    val appComponent get() = _appComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

    }
}