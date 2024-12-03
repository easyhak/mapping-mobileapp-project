package com.trip.myapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.domain.usecase.AddArchiveUseCase
import com.trip.myapp.domain.usecase.FetchPagedArchivesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val fetchPagedArchivesUseCase: FetchPagedArchivesUseCase,
    private val addArchiveUseCase: AddArchiveUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val pagedArchives: StateFlow<PagingData<Archive>> = authRepository.getUserUID()?.let { uid ->
        fetchPagedArchivesUseCase(uid)
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PagingData.empty()
            )
    } ?: MutableStateFlow(PagingData.empty())
}
