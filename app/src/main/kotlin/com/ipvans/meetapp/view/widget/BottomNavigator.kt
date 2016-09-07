package com.ipvans.meetapp.view.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.ipvans.meetapp.R
import com.ipvans.modelsandbox.core.Model

class BottomNavigator : LinearLayout {

    val container by lazy { findViewById(R.id.buttons_container) as ViewGroup }
    var selectedView: View? = null



    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    }

    init {
        inflate(context, R.layout.navigator_layout, this)
    }

    fun addButton(imageRes: Int, text: String = "", tag: Any? = null, shouldSelect : Boolean = true, listener: (v: View) -> Unit): BottomNavigator {
        val v = LayoutInflater.from(context).inflate(R.layout.navigator_item, container, false) as ImageView
        v.isClickable = true
        v.setImageResource(imageRes)
        v.setTag(tag)
        //todo set text if any
        v.setOnClickListener {
            if (!shouldSelect || selectButton(v))
                listener.invoke(v)
        }
        v.id = v.hashCode()
        container.addView(v)
        return this
    }

    private fun selectButton(b: View?) =
        when (b != null && (selectedView == null || b.equals(selectedView).not())) {
            true -> {
                diselectAllViews()
                b!!.isSelected = true
                selectedView = b
                true
            }
            else -> false
        }

    fun selectButton(tag: Any?) = selectButton(findViewWithTag(tag))

    fun callButton(b: View?) = b?.callOnClick()

    fun callButton(id: Int) = findViewById(id)?.callOnClick()

    fun callButtonWithTag(tag: Any) = findViewWithTag(tag)?.callOnClick() ?: false

    private fun diselectAllViews() = (0..container.childCount)
            .forEach { container.getChildAt(it)?.isSelected = false }





    data class ViewState(val viewId: Int) : Parcelable {

        constructor(source: Parcel): this(source.readInt())

        override fun writeToParcel(dest: Parcel?, p1: Int) {
            dest?.writeInt(viewId)
        }

        override fun describeContents() = 0

        companion object {
            @JvmField var CREATOR: Parcelable.Creator<ViewState> = object : Parcelable.Creator<ViewState> {

                override fun createFromParcel(p0: Parcel) = ViewState(p0)

                override fun newArray(p0: Int) = kotlin.arrayOfNulls<ViewState>(p0)

            }
        }

    }

}