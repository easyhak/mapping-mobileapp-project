package com.trip.myapp.domain.model

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp

data class Archive (
    val id: String,
    val name: String,
    @ColorInt val color: Int,
    val isDefault: Boolean = false, // 기본 보관함(전체) 여부
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp = Timestamp.now()
)
