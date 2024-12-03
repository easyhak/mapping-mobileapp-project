package com.trip.myapp.domain.model

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp


data class Archive (
    val id: String = "",
    val title: String = "",
    @ColorInt val color: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
)
