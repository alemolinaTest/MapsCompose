package com.amolina.mapscompose

import android.app.Application
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@HiltAndroidApp
class MapsComposeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-wide resources or services here if needed
    }
}