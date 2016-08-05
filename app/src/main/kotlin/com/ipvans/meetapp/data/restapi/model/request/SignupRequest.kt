package com.ipvans.meetapp.data.restapi.model.request

data class SignupRequest(val email: String,
                         val password: String,
                         val name: String? = null,
                         val phone: String? = null)