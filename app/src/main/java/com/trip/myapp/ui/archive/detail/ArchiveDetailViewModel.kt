package com.trip.myapp.ui.archive.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchPagedScrapedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArchiveDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPagedScrapedPostUseCase: FetchPagedScrapedPostUseCase
) : ViewModel() {

    private val archiveId: String? = savedStateHandle.get<String>("archiveId")

    val pagedPosts: Flow<PagingData<Post>> =
        fetchPagedScrapedPostUseCase(archiveId!!).cachedIn(viewModelScope)
}
