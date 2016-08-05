package com.ipvans.meetapp.view.events

import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.plusAssign
import com.ipvans.meetapp.data.restapi.model.AEvent
import com.ipvans.meetapp.data.restapi.model.BaseResponse
import com.ipvans.meetapp.data.restapi.model.ListResponse
import com.ipvans.meetapp.view.ViewNotification
import com.ipvans.meetapp.view.ViewState
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class EventListPresenter(val view: EventListFragment,
                         val eventsInteractor: EventsInteractor,
                         val mode: String) {

    private val subscriptions = CompositeSubscription()

    private val refreshSubject = PublishSubject.create<Any>();
    private val paginationSubject = PublishSubject.create<Int>();
    private val sortSubject = BehaviorSubject.create<String>("gg");
    private val notificationSubject = BehaviorSubject.create<ViewNotification<*>>(ViewNotification(0, ViewState.CONTENT));

    private val DEFAULT_PAGE = 1;
    private var page = DEFAULT_PAGE;
    private var totalPages = DEFAULT_PAGE;
    private var limit = 10


    val dataSubscription = Observable.combineLatest(
            paginationSubject
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .zipWith(notificationSubject
                            .filter { it.state == ViewState.CONTENT || it.state == ViewState.ERROR }) {
                        page, notification ->
                        page
                    }
                    .filter { page <= totalPages }
                    .startWith(DEFAULT_PAGE),
            Observable.combineLatest(
                    sortSubject.distinctUntilChanged(),
                    refreshSubject.startWith(0)) {
                sort, o ->
                sort
            }
                    .doOnNext { notificationSubject.onNext(ViewNotification(it, ViewState.PROGRESS)) }) {
        o, sort ->
        sort
    }
            .debounce(200, TimeUnit.MILLISECONDS)
            .flatMap {
                //switch api method based by mode
                when (mode) {
                    EventListFragment.MODE_EXPLORE -> eventsInteractor.getAllEvents(page, limit)
                    else -> eventsInteractor.getUserEvents(page, limit)
                }.onErrorReturn {
                    notificationSubject.onNext(ViewNotification(it, ViewState.ERROR))
                    BaseResponse<ListResponse<AEvent>>()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                it.data?.let {
                    totalPages = it.pages
                    it.page?.let {
                        if (it.toInt() == DEFAULT_PAGE) view.clearData()
                    }
                    view.showContent(it.docs)
                    notificationSubject.onNext(ViewNotification(it.docs, ViewState.CONTENT))
                    page++
                }
            }
            .publish()

    val notificationSubscription = notificationSubject.subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                when (it.state) {
                    ViewState.ERROR -> view.showError()
                    ViewState.CONTENT -> view.showProgress(false)
                    ViewState.PROGRESS -> view.showProgress(true)
                }
            }
            .publish()

    fun refresh() {
        resetPages()
        refreshSubject.onNext(0)
        //paginationSubject.onNext(0)
    }

    fun nextPage() {
        paginationSubject.onNext(0)
    }

    private fun resetPages() {
        page = DEFAULT_PAGE;
    }

    fun onAttached() {
        subscriptions += dataSubscription.connect()
        subscriptions += notificationSubscription.connect()

        refresh()
    }

    fun onDetached() {
        subscriptions.clear()
    }

}