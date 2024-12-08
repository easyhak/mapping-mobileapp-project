package com.trip.myapp.domain.usecase

import androidx.paging.PagingData
import com.trip.myapp.data.repository.ArchiveRepository
import com.trip.myapp.domain.model.Archive
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPagedArchiveUseCase @Inject constructor(
    private val archiveRepository: ArchiveRepository
) {

    operator fun invoke(userId: String): Flow<PagingData<Archive>> {
        return archiveRepository.fetchPagedArchives(userId)
    }
}
