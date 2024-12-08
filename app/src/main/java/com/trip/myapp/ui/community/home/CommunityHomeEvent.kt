package com.trip.myapp.ui.community.home

sealed class CommunityHomeEvent {

    sealed class SignOUt : CommunityHomeEvent() {
        data object Success : SignOUt()
        data object Failure : SignOUt()
    }
}