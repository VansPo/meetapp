package com.ipvans.meetapp.view.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.places.ui.PlacePicker
import com.ipvans.meetapp.App
import com.ipvans.meetapp.R
import com.ipvans.meetapp.data.restapi.model.*
import com.ipvans.meetapp.data.toMessage
import com.ipvans.meetapp.data.withBackButton
import com.ipvans.meetapp.view.ViewState
import com.tbruyelle.rxpermissions.RxPermissions
import com.trello.rxlifecycle.components.RxActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class CreateActivity : RxActivity() {

    private val PLACE_PICKER_REQUEST = 1

    private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val title by lazy { findViewById(R.id.title) as TextInputLayout }
    private val location by lazy { findViewById(R.id.location) as TextView }
    private val dateTime by lazy { findViewById(R.id.date) as TextView }
    private val description by lazy { findViewById(R.id.description) as TextInputLayout }
    private val create by lazy { findViewById(R.id.create) as Button }
    private val progress by lazy { findViewById(R.id.progress) as View }

    @Inject
    lateinit var presenter: CreatePresenter

    private val locationPickerIntent by lazy { PlacePicker.IntentBuilder().build(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create)

        CreateDaggerComponent().getComponent(application as App, this).inject(this)


        toolbar.withBackButton(this).title = "Create"

        location.setOnClickListener { startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST) }

        create.setOnClickListener { presenter.createClick() }

        presenter.notificationStream
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.state) {
                        ViewState.CONTENT -> showProgress(false)
                        ViewState.PROGRESS -> showProgress(true)
                        ViewState.ERROR -> showError(it.data)
                    }
                }

        presenter.createResultStream
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { if (it != null && it) finish() }
    }

    fun getEvent() = AEventRequest(
            title = title.editText?.text.toString(),
            description = description.editText?.text.toString(),
            tags = emptyList(),
            invites = emptyList(),
            attendees = emptyList(),
            place = presenter.place
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val place = PlacePicker.getPlace(this, data)
            presenter.place = AEventPlace(
                    true,
                    listOf(AVariant(
                            place.latLng.latitude,
                            place.latLng.longitude,
                            place.name?.toString(),
                            AVariantAdditional(
                                    place.address?.toString(),
                                    place.websiteUri?.toString()
                            )
                    ))
            )
            location.text = listOf(place.name, place.address).joinToString("\n")
        }
    }

    fun showProgress(show: Boolean) {
        if (show) progress.visibility = View.VISIBLE
        else progress.visibility = View.GONE
    }

    fun showError(error: Any?) {
        showProgress(false)
        if (error is Throwable) Timber.e(error, "Error while creating")
        Toast.makeText(this, error.toMessage(), Toast.LENGTH_LONG).show()

    }

    override fun onStart() {
        super.onStart()
        presenter.onAttached()
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetached()
    }

}