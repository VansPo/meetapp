package com.ipvans.meetapp.data

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.gson.reflect.TypeToken
import com.ipvans.meetapp.R
import rx.Subscription
import rx.subscriptions.CompositeSubscription

infix operator fun CompositeSubscription.plusAssign(s: Subscription) { add(s) }

fun View.visibleIf(visible: Boolean) {
    visibility = when {
        visible -> View.VISIBLE
        else -> View.GONE
    }
}

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

fun Any?.toMessage() = when (this) {
        is Throwable -> message
        else -> this?.toString() ?: "Unknown error"
}

fun Toolbar.withBackButton(context: Context) = apply {
    setNavigationIcon(context.resources.getDrawable(R.drawable.ic_arrow_back_white_24dp, context.theme))
    if (context is Activity) setNavigationOnClickListener { context.onBackPressed() }
}