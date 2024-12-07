package com.trip.myapp.ui.map.write

sealed class MapWriteEvent {
    sealed class AddPost : MapWriteEvent() {
        data object Success : AddPost()
        data object Loading : AddPost()
        data object Failure : AddPost()
    }
}
