package com.trip.myapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trip.myapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event = Channel<LoginEvent>(64)
    val event = _event.receiveAsFlow()

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                authRepository.signInWithGoogle(idToken)
                _event.trySend(LoginEvent.GoogleLoginSuccess)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "loginWithGoogle: $e")
                _event.trySend(
                    LoginEvent.LoginFailure(SignInErrorMessage.GOOGLE_SIGN_IN_FAIL),
                )
            }
        }
    }

    fun onLoginFailure(errorMessage: SignInErrorMessage) {
        viewModelScope.launch {
            _event.trySend(LoginEvent.LoginFailure(errorMessage))
        }
    }
}
