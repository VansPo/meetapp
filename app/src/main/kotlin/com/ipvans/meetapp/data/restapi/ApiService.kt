package com.ipvans.meetapp.data.restapi

import com.ipvans.meetapp.data.restapi.model.*
import com.ipvans.meetapp.data.restapi.model.request.LoginRequest
import com.ipvans.meetapp.data.restapi.model.request.SignupRequest
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST
import retrofit.http.Query
import rx.Observable

interface ApiService {

    @POST("/api/signup")
    fun signup(@Body request: SignupRequest): Observable<BaseResponse<AUserShort>>

    @POST("/api/login")
    fun login(@Body request: LoginRequest): Observable<BaseResponse<AUserShort>>

    @POST("/api/user")
    fun getUsers(): Observable<BaseResponse<AUserShort>>

    @GET("/api/event")
    fun getUserEvents(@Query("page") page: Int,
                      @Query("limit") limit: Int): Observable<BaseResponse<ListResponse<AEvent>>>

    @GET("/api/events")
    fun getAllEvents(@Query("page") page: Int,
                      @Query("limit") limit: Int): Observable<BaseResponse<ListResponse<AEvent>>>

    @POST("/api/event")
    fun createEvent(@Body event: AEventRequest): Observable<BaseResponse<Any>>

}