package com.ipvans.meetapp.data

import android.content.Context
import android.preference.PreferenceManager
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import rx.Observable
import rx.subjects.BehaviorSubject

interface Store<T> {

    fun <T>put(value: T?)

    fun get() : T?

    fun observe() : Observable<T?>

}

class PersistentStore<T> (val context: Context, val initValue: T?, val tag: String, val gson: Gson) : Store<T> {

    val subject : BehaviorSubject<T?> = BehaviorSubject.create(initValue)
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        inline fun <reified T : Any> create(context: Context, initValue: T?, tag: String, gson: Gson): PersistentStore<T> {
            val store = PersistentStore(context, initValue, tag, gson)
            try {
                if (store.prefs.contains(tag))
                    store.subject.onNext(gson.fromJson<T>(store.prefs.getString(tag, "")))
                else
                    store.subject.onNext(initValue)
            } catch (e : Exception) {
                store.subject.onNext(initValue)
            }
            return store
        }

    }

    override fun get() : T? = subject.value

    override fun observe(): Observable<T?> = subject.asObservable()

    override fun <T> put(value: T?) {
        prefs.edit().putString(tag, gson.toJson(value)).commit()
    }

}