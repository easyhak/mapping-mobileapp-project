package com.trip.myapp.ui.map.home

sealed class MapHomeEvent {

    sealed class SignOUt : MapHomeEvent() {
        data object Success : SignOUt()
        data object Failure : SignOUt()
    }
}