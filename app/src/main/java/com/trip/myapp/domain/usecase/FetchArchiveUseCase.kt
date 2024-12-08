package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.ArchiveRepository
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.domain.model.Archive
import javax.inject.Inject

class FetchArchiveUseCase @Inject constructor(
    private val archiveRepository: ArchiveRepository,
    private val authRepository: AuthRepository
) {
    suspend fun operator(archiveId: String): Archive {
        val userId =
            authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        return archiveRepository.fetchArchive(userId, archiveId)
    }
}