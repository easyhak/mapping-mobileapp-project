package com.trip.myapp.domain.model

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val startDate: Long,
    val endDate: Long,
    @ColorInt val pinColor: Int,
    val latitude: Double,
    val longitude: Double,
    val userId: String, // 작성자 ID
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp = Timestamp.now()
)
