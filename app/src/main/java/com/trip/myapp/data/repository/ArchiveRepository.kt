package com.trip.myapp.data.repository

import androidx.annotation.ColorInt
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trip.myapp.data.dto.ArchiveRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ArchiveRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) {

    private val userCollection = firebaseFirestore.collection("users")

    // 폴더만 생성
    suspend fun saveArchive(
        userId: String,
        title: String,
    ) {
        val userRef = userCollection.document(userId)
        val folderRef = userRef.collection("folders").document()

        val request = ArchiveRequest(
            id = folderRef.id,
            title = title,
        )

        try {
            folderRef.set(request).await()
        } catch (e: Exception) {
            throw e // 오류 처리
        }
    }

}