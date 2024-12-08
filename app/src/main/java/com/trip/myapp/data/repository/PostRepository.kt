package com.trip.myapp.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.trip.myapp.data.dto.PostPagingSource
import com.trip.myapp.data.dto.PostRequest
import com.trip.myapp.domain.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {
    private val postCollection = firebaseFirestore.collection("posts")
    private val userCollection = firebaseFirestore.collection("users")
    private val imageStorage = firebaseStorage.reference.child("images")

    suspend fun savePost(
        userId: String,
        userName: String,
        title: String,
        content: String,
        imageUrlList: List<String>,
        startDate: String,
        endDate: String,
        userProfileImageUrl: String,
        latitude: Double,
        longitude: Double,
        pinColor: Long,
        address: String
    ): Unit = coroutineScope {
        val postRef = postCollection.document()

        val uploadedImageUrlList = imageUrlList.mapIndexed { index, uriString ->
            async(Dispatchers.IO) {
                val imageUri = Uri.parse(uriString)
                val imageRef = imageStorage.child("${postRef.id}/image_$index.jpg")
                imageRef.putFile(imageUri).await()
                imageRef.downloadUrl.await().toString()
            }
        }.awaitAll()

        val request = PostRequest(
            id = postRef.id,
            title = title,
            content = content,
            imageUrlList = uploadedImageUrlList,
            startDate = startDate,
            endDate = endDate,
            userId = userId,
            userName = userName,
            userProfileImageUrl = userProfileImageUrl,
            latitude = latitude,
            longitude = longitude,
            pinColor = pinColor,
            address = address
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

    // post 하나 가져오기
    suspend fun fetchPostById(
        postId: String
    ): Post {
        val postDocument = postCollection.document(postId).get().await()
        return postDocument.toObject(Post::class.java) ?: throw Exception("Post not found")
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
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = {
                PostPagingSource(
                    postCollection = userCollection.document(userId).collection("archives")
                        .document(archiveId).collection("posts")
                )
            }
        ).flow
    }

    suspend fun fetchPostsByUserId(userId: String): List<Post> {
        return try {
            val snapshot: QuerySnapshot = postCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.toObjects(Post::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // 에러 발생 시 빈 리스트 반환
        }
    }
}
