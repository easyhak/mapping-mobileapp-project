package com.trip.myapp.ui.archive.home

sealed class ArchiveHomeEvent {

    sealed class AddArchive : ArchiveHomeEvent() {
        data object Success : AddArchive()
        data object Failure : AddArchive()
    }

    sealed class SignOUt : ArchiveHomeEvent() {
        data object Success : SignOUt()
        data object Failure : SignOUt()
    }
}
