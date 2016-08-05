package com.ipvans.meetapp.data.restapi

import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.model.UserState
import retrofit.RequestInterceptor

class ApiHeaders(private val version: String,
                 private val timeZoneOffset: String,
                 val userState: Store<UserState>) : RequestInterceptor {

    override fun intercept(request: RequestInterceptor.RequestFacade) {
        request.addHeader("codeVersion", version)
        request.addHeader("time-zone-offset", timeZoneOffset)
        userState.get()?.let { request.addHeader("x-access-token", it.jwt) }
    }
}
