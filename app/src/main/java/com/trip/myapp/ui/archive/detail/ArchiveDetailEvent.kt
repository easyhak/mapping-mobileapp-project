package com.trip.myapp.ui.archive.detail

sealed class ArchiveDetailEvent {
    sealed class UnscrapPost : ArchiveDetailEvent() {
        data object Success : UnscrapPost()
        data object Failure : UnscrapPost()
    }
}
