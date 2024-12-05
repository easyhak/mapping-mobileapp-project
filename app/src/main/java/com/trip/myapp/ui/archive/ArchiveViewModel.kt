package com.trip.myapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.domain.usecase.AddArchiveUseCase
import com.trip.myapp.domain.usecase.FetchPagedArchivesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val fetchPagedArchivesUseCase: FetchPagedArchivesUseCase,
    private val addArchiveUseCase: AddArchiveUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val pagedArchives: Flow<PagingData<Archive>> = authRepository.getUserUID()?.let { uid ->
        fetchPagedArchivesUseCase(uid)
        .cachedIn(viewModelScope)
    } ?: flowOf(PagingData.empty())

    fun addArchive(name: String, color: Int) {
        viewModelScope.launch {
            addArchiveUseCase(
                name = name,
                color = color,
                isDefault = false
            )
        }
    }
}
