package com.ipvans.meetapp.data.restapi.model

data class ListResponse<T>(val docs: List<T> = emptyList(),
                           val total: Int = 0,
                           val limit: String? = null,
                           val page: String? = null,
                           val pages: Int = 0)