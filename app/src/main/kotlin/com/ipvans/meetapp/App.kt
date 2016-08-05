package com.ipvans.meetapp

import android.app.Application

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = AppDaggerComponent().getComponent(this)
        component.inject(this)

    }

}