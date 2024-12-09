package com.trip.myapp.ui.community.detail

sealed class CommunityDetailEvent {

    sealed class ScrapPost : CommunityDetailEvent() {
        data object Success : ScrapPost()
        data object Loading : ScrapPost()
        data object Failure : ScrapPost()
    }
}
