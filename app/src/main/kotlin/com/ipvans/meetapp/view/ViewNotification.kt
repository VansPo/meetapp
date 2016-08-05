package com.ipvans.meetapp.view

data class ViewNotification<T>(var data: T, var state: ViewState)

enum class ViewState {
    PROGRESS, CONTENT, ERROR
}
