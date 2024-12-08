package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class FetchPagedScrapedPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(archiveId: String) {
        val userId =
            authRepository.getUserUID() ?: throw IllegalStateException("User not signed in")
        postRepository.fetchPagedScrapedPosts(userId, archiveId)
    }
}
