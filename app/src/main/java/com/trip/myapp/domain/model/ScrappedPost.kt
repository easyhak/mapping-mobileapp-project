package com.trip.myapp.domain.model

import com.google.firebase.Timestamp

data class ScrappedPost(
    val id: String,
    val postId: String,
    val archiveId: String,
    val userId: String,
    val scrappedAt: Timestamp = Timestamp.now()
)
