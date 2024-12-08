package com.trip.myapp.domain.usecase

import androidx.paging.PagingData
import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import com.trip.myapp.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPagedScrapedPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(archiveId: String): Flow<PagingData<Post>> {
        val userId =
            authRepository.getUserUID() ?: throw IllegalStateException("User not signed in")
        return postRepository.fetchPagedScrapedPosts(userId, archiveId)
    }
}
