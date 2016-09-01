package com.ipvans.meetapp.data.restapi.model

data class BaseResponse<out T>(val success: Boolean = false,
                               val message: String? = null,
                               val data: T? = null)