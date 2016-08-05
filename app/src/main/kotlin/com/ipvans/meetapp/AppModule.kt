package com.ipvans.meetapp

import android.content.Context
import com.ipvans.meetapp.data.DataModule
import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.interactors.LoginInteractor
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.qualifiers.ApplicationContext
import com.ipvans.meetapp.data.qualifiers.User
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class AppModule(val app: App) {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideAppContext(): Context = app

}

@Singleton
@Component (modules = arrayOf(AppModule::class))
interface AppComponent {

    @ApplicationContext fun provideContext(): Context

    @User fun provideUserState(): Store<UserState>

    fun provideLoginInteractor(): LoginInteractor

    fun provideEventsInteractor(): EventsInteractor

    fun inject(app: App): Unit

}
