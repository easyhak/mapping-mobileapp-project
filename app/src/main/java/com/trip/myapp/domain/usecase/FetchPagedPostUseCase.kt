package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class FetchPagedPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke() = postRepository.fetchPagedPosts()
}
