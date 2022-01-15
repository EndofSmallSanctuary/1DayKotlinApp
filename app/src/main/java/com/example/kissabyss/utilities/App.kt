package com.example.kissabyss.utilities

import android.app.Application
import com.example.kissabyss.di.repositoryModule
import com.example.kissabyss.viewmodels.viewModelModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(repositoryModule, viewModelModel))
        }
    }
}