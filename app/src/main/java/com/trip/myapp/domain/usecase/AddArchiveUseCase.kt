package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.ArchiveRepository
import com.trip.myapp.data.repository.AuthRepository
import javax.inject.Inject

class AddArchiveUseCase @Inject constructor(
    private val archiveRepository: ArchiveRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
    ) {
        val author = authRepository.getUserUID() ?: throw IllegalStateException("User not signed in")
        return archiveRepository.saveArchive(
            userId = author,
            name = name,
        )
    }
}
