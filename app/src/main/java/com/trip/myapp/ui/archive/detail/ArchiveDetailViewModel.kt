package com.trip.myapp.ui.archive.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.trip.myapp.domain.usecase.FetchPagedScrapedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArchiveDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPagedScrapedPostUseCase: FetchPagedScrapedPostUseCase
) : ViewModel() {

    private val archiveId: String? = savedStateHandle.get<String>("archiveId")

}
