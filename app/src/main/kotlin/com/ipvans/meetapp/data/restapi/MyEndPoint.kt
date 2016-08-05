package com.ipvans.meetapp.data.restapi

import retrofit.Endpoint

class MyEndPoint(private var url: String?) : Endpoint {
    override fun getUrl() = url

    fun setUrl(url: String) { this.url = url }

    override fun getName() = url
}