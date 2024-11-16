package com.trip.myapp.ui.login

import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val signInRequest: BeginSignInRequest
) : ViewModel() {

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    fun signOut() {
        firebaseAuth.signOut()
    }

    // todo livedata 에서 statflow로 바꾸기
    private val _signInState = MutableStateFlow<Result<Boolean>?>(null)
    val signInState: StateFlow<Result<Boolean>?> = _signInState.asStateFlow()

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signInState.value = Result.success(true)
                    } else {
                        _signInState.value = Result.failure(task.exception ?: Exception("Login failed"))
                    }
                }
        }
    }

    fun resetSignInState() {
        _signInState.value = null
    }
}
