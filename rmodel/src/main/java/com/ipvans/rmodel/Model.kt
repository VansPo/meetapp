package com.ipvans.modelsandbox.core

abstract class Model<in T> {

    private var destroyListener:() -> Unit? = { }

    /**
     * called when retain fragment is created
     */
    abstract fun onActivityCreated()

    /**
     * called when retain fragment is destroyed
     */
    open fun onActivityDestroyed() = destroy()

    /**
     * call this when you are done with model
     */
    fun destroy() { destroyListener.invoke() }

    /**
     * called when view is created
     */
    abstract fun attachPresenter(presenter: T)

    /**
     * called when view is destroyed
     */
    abstract fun detachPresenter()

    fun setModelDestroyListener(listener: () -> Unit) { destroyListener = listener }

}