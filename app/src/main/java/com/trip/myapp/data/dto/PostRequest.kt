package com.trip.myapp.data.dto

import androidx.annotation.ColorLong
import com.google.firebase.Timestamp

data class PostRequest(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrlList: List<String> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val userId: String = "",
    val userProfileImageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Timestamp = Timestamp.now(),
    @ColorLong val pinColor: Long = 0xFF000000
)
