package com.ipvans.meetapp

import android.app.Application
import com.ipvans.modelsandbox.core.Model
import com.ipvans.modelsandbox.core.ModelContainer
import java.util.*

class App : Application(), ModelContainer {

    override val models = HashMap<String, Model<*>>()

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = AppDaggerComponent().getComponent(this)
        component.inject(this)

    }

}