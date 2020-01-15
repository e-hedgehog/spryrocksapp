package com.ehedgehog.android.spryrocksapp

import android.annotation.SuppressLint
import android.app.Application
import com.ehedgehog.android.spryrocksapp.di.AppComponent
import com.ehedgehog.android.spryrocksapp.di.DaggerAppComponent
import com.ehedgehog.android.spryrocksapp.di.DataModule

@SuppressLint("Registered")
class Application : Application() {

    companion object {
        val appComponent: AppComponent =
            DaggerAppComponent.builder()
                .dataModule(DataModule())
                .build()
    }

}