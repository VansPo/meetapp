package com.ipvans.meetapp.data

import android.view.View
import com.google.gson.reflect.TypeToken
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