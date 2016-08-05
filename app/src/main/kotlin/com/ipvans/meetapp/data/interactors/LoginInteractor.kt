package com.ipvans.meetapp.data.interactors

import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.model.toUserState
import com.ipvans.meetapp.data.restapi.ApiService
import com.ipvans.meetapp.data.restapi.model.request.LoginRequest
import com.ipvans.meetapp.data.restapi.model.request.SignupRequest
import rx.schedulers.Schedulers

class LoginInteractor(val api: ApiService, val user: Store<UserState>) {

    fun signup(request: SignupRequest) = api.signup(request)
            .doOnNext { u -> u.data?.token?.let { user.put(u.data.toUserState()) } }
            .subscribeOn(Schedulers.io())

    fun login(request: LoginRequest) = api.login(request)
        .doOnNext { u -> u.data?.token?.let { user.put(u.data.toUserState()) } }
        .subscribeOn(Schedulers.io())

    fun logout() { user.put(null) }

    fun getUsers() = api.getUsers()
        .subscribeOn(Schedulers.io())

}