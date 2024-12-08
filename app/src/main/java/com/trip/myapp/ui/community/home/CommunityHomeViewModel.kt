package com.trip.myapp.ui.community.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.auth.FirebaseAuth
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchPagedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor(
    private val fetchPagedPostsUseCase: FetchPagedPostUseCase,
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val pagedPosts: Flow<PagingData<Post>> =
        fetchPagedPostsUseCase().cachedIn(viewModelScope)

    private val _event = Channel<CommunityHomeEvent>(64)
    val event = _event.receiveAsFlow()

    fun signOut() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                _event.trySend(CommunityHomeEvent.SignOUt.Success)
            } catch (e: Exception) {
                _event.trySend(CommunityHomeEvent.SignOUt.Failure)
            }
        }
    }

    val loginEmail = firebaseAuth.currentUser?.email ?: "No Email"
}
