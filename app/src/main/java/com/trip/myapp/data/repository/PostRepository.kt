package com.trip.myapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.trip.myapp.data.dto.PostRequest
import kotlinx.coroutines.tasks.await

class PostRepository(
    private val firebaseFirestore: FirebaseFirestore,
) {

    private val postCollection = firebaseFirestore.collection("posts")

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
}
