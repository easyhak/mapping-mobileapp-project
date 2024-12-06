package com.trip.myapp.ui.map.home

sealed class MapHomeEvent {
    sealed class AddPost : MapHomeEvent() {
        data object Success : AddPost()

        data object Failure : AddPost()
    }
}