package com.trip.myapp.ui.archive.home

sealed class ArchiveHomeEvent {
    sealed class AddArchive : ArchiveHomeEvent() {
        data object Success : AddArchive()

        data object Failure : AddArchive()
    }
}
