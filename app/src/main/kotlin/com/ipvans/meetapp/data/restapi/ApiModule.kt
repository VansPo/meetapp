package com.ipvans.meetapp.data.restapi

import android.content.Context
import com.ipvans.meetapp.BuildConfig
import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.getCodeVersion
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.qualifiers.ApplicationContext
import com.ipvans.meetapp.data.qualifiers.User
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter
import retrofit.client.OkClient
import java.util.*
import javax.inject.Singleton

@Module()
class ApiModule {

    @Provides
    @Singleton
    fun provideRestAdapter(endpoint: MyEndPoint, apiHeaders: ApiHeaders): RestAdapter {
        return RestAdapter.Builder()
                .setLogLevel(if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE)
                .setEndpoint(endpoint)
                .setRequestInterceptor(apiHeaders)
                .setErrorHandler({
                    if (it.response != null && it.response.status == 401) {
                        //todo logout
                    }
                    it.cause
                })
                .setClient(OkClient())
                .build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(adapter: RestAdapter) = adapter.create(ApiService::class.java)

    @Provides
    @Singleton
    internal fun provideEndpoint() = MyEndPoint("http://192.168.0.112:8080")

    @Provides
    @Singleton
    internal fun provideApiHeaders(@ApplicationContext context: Context,
                                   @User userState: Store<UserState>) = ApiHeaders(
            getCodeVersion(context).toString(),
            (TimeZone.getDefault().rawOffset / 1000 / 60).toString(),
            userState)

}