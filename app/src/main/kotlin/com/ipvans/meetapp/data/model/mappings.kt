package com.ipvans.meetapp.data.model

import com.ipvans.meetapp.data.restapi.model.AUserShort

fun AUserShort.toUserState() = UserState(id, email, name, phone, skype, avatarUrl, about, token!!)