package com.example.famouspeople

import android.app.Application
import com.example.famouspeople.di.component.AppComponent
import com.example.famouspeople.di.component.DaggerAppComponent
import com.example.famouspeople.di.modules.NetworkModule
import com.example.famouspeople.networking.NetworkConnectionInterceptor

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
        _appComponent = DaggerAppComponent
            .factory()
            .create(NetworkModule(this), this, NetworkConnectionInterceptor(this))

    }
}