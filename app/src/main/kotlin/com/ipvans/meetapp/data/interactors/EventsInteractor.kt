package com.ipvans.meetapp.data.interactors

import com.ipvans.meetapp.data.restapi.ApiService
import com.ipvans.meetapp.data.restapi.model.AEvent
import com.ipvans.meetapp.data.restapi.model.AEventRequest
import rx.schedulers.Schedulers

class EventsInteractor(val api: ApiService) {

    fun getUserEvents(page: Int, limit: Int) = api.getUserEvents(page, limit)
        .subscribeOn(Schedulers.io())

    fun getAllEvents(page: Int, limit: Int) = api.getAllEvents(page, limit)
        .subscribeOn(Schedulers.io())

    fun createEvent(event: AEventRequest) = api.createEvent(event)
        .subscribeOn(Schedulers.io())

}