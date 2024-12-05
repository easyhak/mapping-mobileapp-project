package com.trip.myapp.ui.archive

sealed class ArchiveListEvent {
    sealed class AddArchive : ArchiveListEvent() {
        data object Success : AddArchive()

        data object Failure : AddArchive()
    }
}
