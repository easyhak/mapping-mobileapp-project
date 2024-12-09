package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class UnScrapPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(archiveId: String, postId: String) {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User not signed in")
        postRepository.unScrapPost(
            userId = userId,
            archiveId = archiveId,
            postId = postId
        )
    }
}
