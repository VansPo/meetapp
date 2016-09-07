package com.ipvans.meetapp.view.events

import android.os.Parcelable
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.plusAssign
import com.ipvans.meetapp.data.restapi.model.AEvent
import com.ipvans.meetapp.data.restapi.model.BaseResponse
import com.ipvans.meetapp.data.restapi.model.ListResponse
import com.ipvans.meetapp.view.ViewNotification
import com.ipvans.meetapp.view.ViewState
import com.ipvans.modelsandbox.core.Model
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit

class EventListModel(val eventsInteractor: EventsInteractor,
                     val mode: String) : Model<EventListFragment>() {

    data class RecyclerViewState(val dataCache: ArrayList<AEvent> = arrayListOf(), var scrollState: Parcelable? = null)

    private val subscriptions = CompositeSubscription()

    private val refreshSubject = PublishSubject.create<Any>()
    private val paginationSubject = PublishSubject.create<Int>()
    private val sortSubject = BehaviorSubject.create<String>("")
    private val notificationSubject = BehaviorSubject.create<ViewNotification<*>>(ViewNotification(0, ViewState.CONTENT))

    val dataSubject = PublishSubject.create<List<AEvent>>()
    val clearSubject = PublishSubject.create<Any>()

    private val DEFAULT_PAGE = 1
    private var page = DEFAULT_PAGE
    private var prevPage = DEFAULT_PAGE
    private var totalPages = DEFAULT_PAGE
    private var limit = 10

//    private val dataCache = arrayListOf<AEvent>()
    private val recyclerState = RecyclerViewState()

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
                    page = prevPage
                    notificationSubject.onNext(ViewNotification(it, ViewState.ERROR))
                    BaseResponse<ListResponse<AEvent>>()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                it.data?.let {
                    totalPages = it.pages
                    dataSubject.onNext(it.docs)
                    notificationSubject.onNext(ViewNotification(it.docs, ViewState.CONTENT))

                    page++
                    prevPage = page
                }
            }
            .publish()


    val notifications = notificationSubject.subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .asObservable()

    fun refresh() {
        resetPages()
        refreshSubject.onNext(0)
        //paginationSubject.onNext(0)
    }

    fun nextPage() {
        paginationSubject.onNext(0)
    }

    private fun resetPages() {
        page = DEFAULT_PAGE
    }

    override fun onActivityCreated() {
        refresh()

        subscriptions += dataSubscription.connect()
    }

    override fun onActivityDestroyed() {
        super.onActivityDestroyed()
        subscriptions.clear()
    }

    override fun attachPresenter(presenter: EventListFragment) {
        presenter.showContent(recyclerState.dataCache)
        presenter.restoreScrollState(recyclerState.scrollState)
        recyclerState.dataCache.clear()
    }

    fun saveAdapterData(data: List<AEvent>, scrollState: Parcelable) {
        recyclerState.dataCache.addAll(data)
        recyclerState.scrollState = scrollState
    }

    override fun detachPresenter() {

    }

    fun isFirstPage() = page == DEFAULT_PAGE

}