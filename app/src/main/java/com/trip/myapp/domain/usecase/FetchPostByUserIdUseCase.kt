package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import com.trip.myapp.domain.model.Post
import javax.inject.Inject

class FetchPostByUserIdUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): List<Post> {
        val userId =
            authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        return postRepository.fetchPostsByUserId(userId)
    }
}