package com.simon.rickandmorty.di

import android.app.Application
import coil.Coil
import com.simon.rickandmorty.core.coil.SimonCoilLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class SimonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimber()
        Coil.setImageLoader(SimonCoilLoader.getOrSettImageLoader(this))
    }
}
private fun plantTimber() {
    plant(DebugTree())
}