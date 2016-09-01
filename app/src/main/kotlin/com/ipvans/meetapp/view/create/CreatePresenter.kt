package com.ipvans.meetapp.view.create

import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.plusAssign
import com.ipvans.meetapp.data.restapi.model.AEvent
import com.ipvans.meetapp.data.restapi.model.AEventPlace
import com.ipvans.meetapp.view.ViewNotification
import com.ipvans.meetapp.view.ViewState
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class CreatePresenter(val view: CreateActivity, val eventInteractor: EventsInteractor) {

    private val createStream = PublishSubject.create<Any>()
    private val subscriptions = CompositeSubscription()

    val notificationStream = PublishSubject.create<ViewNotification<*>>()
    val createResultStream: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    var place: AEventPlace? = null

    private val createSubject = createStream
            .doOnNext { notificationStream.onNext(ViewNotification(null, ViewState.PROGRESS)) }
            .debounce(200, TimeUnit.MILLISECONDS)
            .map { view.getEvent() }
            .flatMap {
                eventInteractor.createEvent(it)
                    .onErrorReturn {
                        notificationStream.onNext(ViewNotification(it, ViewState.ERROR))
                        null
                    }
            }
            .doOnNext {
                notificationStream.onNext(ViewNotification(null, ViewState.CONTENT))
                createResultStream.onNext(it?.success ?: false)
            }
            .subscribeOn(Schedulers.io())
            .publish()

    fun createClick() = createStream.onNext(0)

    fun onAttached() {
        subscriptions += createSubject.connect()
    }

    fun onDetached() = subscriptions.clear()

}