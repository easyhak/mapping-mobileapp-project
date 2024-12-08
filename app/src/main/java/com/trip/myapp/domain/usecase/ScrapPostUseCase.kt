package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class ScrapPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(archiveId: String ,postId: String) {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        postRepository.scrapPost(
            userId = userId,
            archiveId = archiveId,
            postId = postId,
        )
    }
}
