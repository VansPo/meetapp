package com.ipvans.meetapp.data.model

data class UserState(val id: String,
                     val email: String,
                     val name: String?,
                     val phone: String?,
                     val skype: String?,
                     val avatarUrl: String?,
                     val about: String?,
                     val jwt: String)