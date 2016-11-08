package com.ipvans.meetapp.view.events

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.ipvans.meetapp.App
import com.ipvans.meetapp.R
import com.ipvans.meetapp.data.restapi.model.AEvent
import com.ipvans.meetapp.data.toMessage
import com.ipvans.meetapp.view.ViewState
import com.ipvans.meetapp.view.main.ToolbarProvider
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import javax.inject.Inject

class EventListFragment : Fragment() {

    companion object {

        val MODE = "MODE"
        val MODE_HOME = "MODE_HOME"
        val MODE_EXPLORE = "MODE_EXPLORE"

        fun create(mode: String): EventListFragment {
            val fragment = EventListFragment()
            return fragment.apply { arguments = Bundle().apply { putString(MODE, mode) } }
        }
    }

    @Inject
    lateinit var model: EventListModel

    lateinit var component: EventListComponent

    private val recycler: RecyclerView by lazy { activity.findViewById(R.id.recycler) as RecyclerView }
    private val swipe: SwipeRefreshLayout by lazy { activity.findViewById(R.id.swipe) as SwipeRefreshLayout }
    private val empty by lazy { activity.findViewById(R.id.empty) as TextView }

    lateinit private var adapter: EventsAdapter
    lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mode = arguments.getString(MODE)

        component = EventListDaggerComponent().getComponent(activity.application as App, activity, this, mode)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventsAdapter(activity)
        recycler.adapter = adapter
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(activity)

        swipe.setOnRefreshListener { model.refresh() }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        >= (recyclerView.adapter.itemCount - 2)) {
                    model.nextPage();
                }
            }
        })

        setupToolbar()

        model.attachPresenter(this)

        model.dataSubject
                .bindToLifecycle(view)
                .subscribe { showContent(it) }

        model.notifications
                .bindToLifecycle(view)
                .subscribe {
                    when (it.state) {
                        ViewState.ERROR -> showError(it.data)
                        ViewState.CONTENT -> showProgress(false)
                        ViewState.PROGRESS -> showProgress(true)
                    }
                }
    }

    fun setupToolbar() {
        when (mode) {
            MODE_HOME -> {
                (activity as ToolbarProvider).provideToolbar().title = "Created by me"
            }
            else -> {
                (activity as ToolbarProvider).provideToolbar().title = "Explore"
            }
        }

    }

    fun clearData() {
        adapter.clear()
    }

    fun showContent(data: List<AEvent>) {
        if (model.isFirstPage())
            clearData()
        adapter.addItems(data)
        showProgress(false)
    }

    fun showError(t: Any?) {
        showProgress(false)
        Toast.makeText(activity, t.toMessage(), Toast.LENGTH_SHORT).show()
    }

    fun showEmpty(isEmpty: Boolean) = when {
        isEmpty -> empty.visibility = View.VISIBLE
        else -> empty.visibility = View.GONE
    }

    fun showProgress(progress: Boolean) {
        swipe.isRefreshing = progress
        if (progress.not()) showEmpty(adapter.itemCount == 0)
    }

    fun restoreScrollState(state: Parcelable?) {
        state?.let { recycler.layoutManager.onRestoreInstanceState(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.saveAdapterData(adapter.items, recycler.layoutManager.onSaveInstanceState())
        model.detachPresenter()
    }

}