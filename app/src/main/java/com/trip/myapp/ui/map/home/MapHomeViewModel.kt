package com.trip.myapp.ui.map.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.trip.myapp.domain.usecase.AddPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapHomeViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _event = Channel<MapHomeEvent>(64)
    val event = _event.receiveAsFlow()

    fun signOut() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                _event.trySend(MapHomeEvent.SignOUt.Success)
            } catch (e: Exception) {
                _event.trySend(MapHomeEvent.SignOUt.Failure)
            }
        }
    }

    val loginEmail = firebaseAuth.currentUser?.email!!




}
