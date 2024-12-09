package com.trip.myapp.domain.usecase

import androidx.paging.PagingData
import com.trip.myapp.data.repository.ArchiveRepository
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Archive
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPagedArchiveUseCase @Inject constructor(
    private val archiveRepository: ArchiveRepository,
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<PagingData<Archive>> {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        return archiveRepository.fetchPagedArchives(userId)
    }
}
