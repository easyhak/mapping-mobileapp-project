package com.trip.myapp.ui.community.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchPagedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor(
    private val fetchPagedPostsUseCase: FetchPagedPostUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val pagedPosts: Flow<PagingData<Post>> =
        fetchPagedPostsUseCase().cachedIn(viewModelScope)
}
