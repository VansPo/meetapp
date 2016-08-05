package com.ipvans.meetapp.data.restapi.model

import com.google.gson.annotations.SerializedName

data class AEvent(
        @SerializedName("_id") val id: String,
        val createdAt: String?,
        val updatedAd: String?,
        val user: AEventUser?,
        val title: String?,
        val description: String?,
        val type: String?,
        val tags: List<String>,
        val invites: List<AEventUser>,
        val attendees: List<AEventUser>,
        val place: AEventPlace?
)

data class AEventUser(@SerializedName("_id") val id: String,
                      val local: AUserLocal?)

data class AUserLocal(val name: String?,
                      val avatarUrl: String?)

data class AEventPlace(val voteClosed: Boolean?,
                       val variants: List<AVariant>)

data class AVariant(val lat: Number,
                    val lon: Number,
                    val title: String?,
                    val additional: AVariantAdditional?)

data class AVariantAdditional(val phone: String?,
                              val openFrom: String?,
                              val openTill: String?)