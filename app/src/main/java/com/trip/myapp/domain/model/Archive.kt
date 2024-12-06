package com.trip.myapp.domain.model

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp

data class Archive (
    val id: String = "",
    val name: String = "",
    val thumbnailImageUrl: String = "",
    @ColorInt val color: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp = Timestamp.now()
)
