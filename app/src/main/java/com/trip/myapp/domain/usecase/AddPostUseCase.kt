package com.trip.myapp.domain.usecase

import com.trip.myapp.data.repository.AuthRepository
import com.trip.myapp.data.repository.PostRepository
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(
        title: String,
        content: String,
        selectedImages: List<String>,
        startDate: String,
        endDate: String,
        pinColor: Long,
        latitude: Double,
        longitude: Double,
        address: String
    ) {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        val userName = authRepository.getUserName() ?: throw IllegalStateException("User is not signed in")
        val userProfileImageUrl = authRepository.getUserPhotoUrl() ?: throw IllegalStateException("User is not signed in")
        postRepository.savePost(
            userId = userId,
            userProfileImageUrl = userProfileImageUrl.toString(),
            userName = userName,
            title = title,
            content = content,
            imageUrlList = selectedImages,
            startDate = startDate,
            endDate = endDate,
            pinColor = pinColor,
            latitude = latitude,
            longitude = longitude,
            address = address
        )
    }
}
