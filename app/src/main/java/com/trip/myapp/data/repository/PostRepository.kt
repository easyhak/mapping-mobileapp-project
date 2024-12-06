package com.trip.myapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.trip.myapp.data.dto.PostPagingSource
import com.trip.myapp.data.dto.PostRequest
import com.trip.myapp.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
) {
    private val postCollection = firebaseFirestore.collection("posts")
    private val userCollection = firebaseFirestore.collection("users")

    suspend fun savePost(
        userId: String,
        title: String,
        content: String,
        imageUrlList: List<String>,
        startDate: String,
        endDate: String,
        userProfileImageUrl: String,
        latitude: Double,
        longitude: Double,
        pinColor: Long,
    ) {
        val postRef = postCollection.document()

        val request = PostRequest(
            id = postRef.id,
            title = title,
            content = content,
            imageUrlList = imageUrlList,
            startDate = startDate,
            endDate = endDate,
            userId = userId,
            userProfileImageUrl = userProfileImageUrl,
            latitude = latitude,
            longitude = longitude,
            pinColor = pinColor,
        )

        try {
            postRef.set(request).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // 모든 user의 post를 가져오기
    fun fetchPagedPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PostPagingSource(
                    postCollection = postCollection
                )
            }
        ).flow
    }

    // scrap 을 하기
    suspend fun scrapPost(
        userId: String,
        archiveId: String
    ) {
        // todo
    }

    // user - archive 내에 있는 scrap 된 post 를 가져오기
    fun fetchPagedScrapedPosts(
        userId: String,
        archiveId: String
    ): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PostPagingSource(
                    postCollection = userCollection.document(userId).collection("archives")
                        .document(archiveId).collection("posts")
                )
            }
        ).flow
    }
}
