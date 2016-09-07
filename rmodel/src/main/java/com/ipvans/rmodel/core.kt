package com.ipvans.modelsandbox.core

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.lang.ref.WeakReference
import java.util.*

@Suppress("UNCHECKED_CAST")
inline fun <P, M : Model<P>> Context.getModel(key: String, f: () -> M): M {
    val app = when (applicationContext) {
        is ModelContainer -> applicationContext as ModelContainer
        else -> throw IllegalArgumentException("Your Application must implement ModelContainer interface!")
    }

    val model = app.models[key] as M? ?: let {
        val component = f.invoke()
        app.models[key] = component as Model<P>
        component.setModelDestroyListener {
            app.models.remove(key)

            //todo remove debug log
            Log.i("MODEL", "App models count after destroy: ${app.models.size}")
        }
        component
    }

    if (this is AppCompatActivity)
        supportFragmentManager.bindToRetainedFragment(key, model)

    return model
}

fun <P, M : Model<P>> FragmentManager.bindToRetainedFragment(tag: String, model: M): RetainFragment {
    @Suppress("UNCHECKED_CAST")
    val fragment = findFragmentByTag(tag) as RetainFragment?
    return when {
        fragment == null -> {
            // create fragment with new instance
            val newFragment = RetainFragment()
            newFragment.apply {
                newFragment.modelContainer.set(tag, WeakReference(model as Model<*>) )
                beginTransaction()
                        .add(newFragment, tag)
                        .commit()
            }
        }
        fragment.modelContainer.containsKey(tag).not() ||
                fragment.modelContainer[tag]?.get() == null -> {
            fragment.apply { modelContainer.set(tag, WeakReference(model as Model<*>) ) }
        }
        else -> fragment
    }
}

open class RetainFragment() : Fragment() {

    val modelContainer: HashMap<String, WeakReference<Model<*>>> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        modelContainer.forEach { it.value.get()?.onActivityCreated() }
    }

    override fun onDestroy() {
        modelContainer.forEach { it.value.get()?.onActivityDestroyed() }
        super.onDestroy()
    }
}

interface ModelContainer {

    val models: MutableMap<String, Model<*>>

}