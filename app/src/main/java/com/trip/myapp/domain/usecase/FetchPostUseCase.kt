package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class FetchPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String) = postRepository.fetchPostById(postId)
}
