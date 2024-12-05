package com.trip.myapp.data.dto

import androidx.annotation.ColorInt
import com.google.firebase.Timestamp
import kotlin.random.Random

data class ArchiveRequest (
    val id: String = "",
    val name: String = "",
    @ColorInt val  color: Int = generateRandomColor(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    companion object {
        @ColorInt
        private fun generateRandomColor(): Int {
            val red = Random.nextInt(0, 256)
            val green = Random.nextInt(0, 256)
            val blue = Random.nextInt(0, 256)
            return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
        }
    }
}
