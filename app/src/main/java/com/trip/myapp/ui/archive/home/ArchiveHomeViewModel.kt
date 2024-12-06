package com.trip.myapp.ui.archive.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.domain.usecase.AddArchiveUseCase
import com.trip.myapp.domain.usecase.FetchPagedArchivesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArchiveHomeViewModel @Inject constructor(
    private val fetchPagedArchivesUseCase: FetchPagedArchivesUseCase,
    private val addArchiveUseCase: AddArchiveUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event = Channel<ArchiveHomeEvent>(64)
    val event = _event.receiveAsFlow()

    val pagedArchives: Flow<PagingData<Archive>> = authRepository.getUserUID()?.let { uid ->
        fetchPagedArchivesUseCase(uid)
        .cachedIn(viewModelScope)
    } ?: flowOf(PagingData.empty())

    fun addArchive(name: String) {
        viewModelScope.launch {
            try {
                addArchiveUseCase(
                    name = name,
                )
                _event.trySend(ArchiveHomeEvent.AddArchive.Success)

            } catch (e: Exception) {
                _event.trySend(ArchiveHomeEvent.AddArchive.Failure)
            }

        }
    }
}
