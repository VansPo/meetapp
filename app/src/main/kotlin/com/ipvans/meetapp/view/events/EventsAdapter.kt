package com.ipvans.meetapp.view.events

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ipvans.meetapp.R
import com.ipvans.meetapp.data.getMapSnapshotURL
import com.ipvans.meetapp.data.restapi.model.AEvent
import java.util.*

class EventsAdapter(val context: Context) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    val items: ArrayList<AEvent> = arrayListOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val header: TextView by lazy { view.findViewById(R.id.header) as TextView }
        val date: TextView by lazy { view.findViewById(R.id.date) as TextView }
        val location: TextView by lazy { view.findViewById(R.id.location) as TextView }
        val locationImage by lazy { view.findViewById(R.id.location_image) as ImageView }
        val participants: TextView by lazy { view.findViewById(R.id.participants) as TextView }
        val description: TextView by lazy { view.findViewById(R.id.description) as TextView }
        val image: ImageView by lazy { view.findViewById(R.id.image) as ImageView }
        val tags: TextView by lazy { view.findViewById(R.id.tags) as TextView }


        fun bind(item: AEvent, context: Context) {
            header.text = item.title ?: "No title"
            date.text = item.createdAt ?: "No date"
            location.text = item.place?.variants?.firstOrNull()?.title ?: "Unknown"
            participants.text = "${item.attendees.size} peoples are going"
            description.text = item.description ?: "No description"
            tags.text = item.tags.filter { !it.isNullOrBlank() }.joinToString(", ")

            Glide.with(context)
                    .load(item.place?.variants?.firstOrNull()?.getMapSnapshotURL())
                    .into(locationImage)
        }

    }

    fun replaceAll(data: List<AEvent>) {
        items.clear()
        items.addAll(data)
        notifyItemRangeInserted(0, items.size)
    }

    fun addItems(data: List<AEvent>) {
        val prevCount = items.size
        items.addAll(data)
        notifyItemRangeInserted(prevCount, items.size)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

}