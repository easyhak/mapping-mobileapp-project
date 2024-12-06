package com.trip.myapp.domain.model

import androidx.annotation.ColorLong
import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageList: List<String> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    @ColorLong val pinColor: Long = 0xFF000000,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val userId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
)
