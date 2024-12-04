package com.trip.myapp.domain.model

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val startDate: Long? = null,
    val endDate: Long? = null,
    @ColorInt val pinColor: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val userId: String = "", // 작성자 ID (FK)
    val archivedId: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp = Timestamp.now()
)
