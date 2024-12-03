package com.trip.myapp.ui.login

sealed class LoginEvent {
    data object GoogleLoginSuccess : LoginEvent()

    data class LoginFailure(val signInErrorMessage: SignInErrorMessage) : LoginEvent()
}

enum class SignInErrorMessage {
    GOOGLE_SIGN_IN_FAIL,
}
