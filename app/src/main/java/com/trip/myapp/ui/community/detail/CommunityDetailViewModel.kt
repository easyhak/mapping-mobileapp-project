package com.trip.myapp.ui.community.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchPagedArchiveUseCase
import com.trip.myapp.domain.usecase.FetchPostUseCase
import com.trip.myapp.domain.usecase.ScrapPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPostUseCase: FetchPostUseCase,
    private val fetchPagedArchiveUseCase: FetchPagedArchiveUseCase,
    private val scrapPostUseCase: ScrapPostUseCase
) : ViewModel() {
    private val postId: String? = savedStateHandle.get<String>("postId")
    val postName: String? = savedStateHandle.get<String>("postName")

    private val _post = MutableStateFlow(Post())
    val post = _post.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _event = Channel<CommunityDetailEvent>(64)
    val event = _event.receiveAsFlow()

    init {
        postId?.let { fetchPost(it) }
    }

    private fun fetchPost(postId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                fetchPostUseCase(postId).let {
                    _post.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CommunityDetailViewModel", "Failed to fetch post", e)
            } finally {
                _isLoading.value = false
            }

        }
    }

    val pagedArchives: Flow<PagingData<Archive>> =
        fetchPagedArchiveUseCase()
            .cachedIn(viewModelScope)

    fun scrapPost(archiveId: String) {
        viewModelScope.launch {
            try {
                _event.trySend(CommunityDetailEvent.ScrapPost.Loading)
                postId?.let { scrapPostUseCase(archiveId, it) }
                _event.trySend(CommunityDetailEvent.ScrapPost.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CommunityDetailViewModel", "Failed to scrap post", e)
                _event.trySend(CommunityDetailEvent.ScrapPost.Failure)
            }
        }
    }
}
