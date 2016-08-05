package com.ipvans.meetapp.data

import android.content.Context
import com.google.gson.Gson
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.interactors.LoginInteractor
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.qualifiers.ApplicationContext
import com.ipvans.meetapp.data.qualifiers.User
import com.ipvans.meetapp.data.restapi.ApiModule
import com.ipvans.meetapp.data.restapi.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ApiModule::class))
class DataModule {

    @Provides
    @Singleton
    fun provideLoginInteractor(api: ApiService, @User user: Store<UserState>): LoginInteractor =
            LoginInteractor(api, user)

    @Provides
    @Singleton
    fun provideEventsInteractor(api: ApiService): EventsInteractor =
            EventsInteractor(api)

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    @User
    fun provideUserState(@ApplicationContext context: Context, gson: Gson) : Store<UserState> =
            PersistentStore.create(context, null, "userState", gson)
}