package com.trip.myapp.ui.archive.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchArchiveUseCase
import com.trip.myapp.domain.usecase.FetchPagedScrapedPostUseCase
import com.trip.myapp.domain.usecase.UnScrapPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchArchiveUseCase: FetchArchiveUseCase,
    private val unscrapPostUseCase: UnScrapPostUseCase,
    private val fetchPagedScrapedPostUseCase: FetchPagedScrapedPostUseCase
) : ViewModel() {

    val archiveId: String? = savedStateHandle.get<String>("archiveId")
    val archiveName: String? = savedStateHandle.get<String>("archiveName")

    private val _event = Channel<ArchiveDetailEvent>(64)
    val event = _event.receiveAsFlow()

    val pagedPosts: Flow<PagingData<Post>> =
        fetchPagedScrapedPostUseCase(archiveId!!).cachedIn(viewModelScope)

    fun unscrapPost(postId: String) {
        viewModelScope.launch {
            if (archiveId != null) {
                try {
                    unscrapPostUseCase(
                        archiveId = archiveId,
                        postId = postId
                    )
                    _event.send(ArchiveDetailEvent.UnscrapPost.Success)
                } catch (e: Exception) {
                    _event.send(ArchiveDetailEvent.UnscrapPost.Failure)
                }
            }
        }
    }
}
