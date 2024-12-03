package com.trip.myapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trip.myapp.data.dto.ArchivePagingSource
import com.trip.myapp.domain.model.Archive
import kotlinx.coroutines.flow.Flow
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
        val folderRef = userRef.collection("archives").document()

        val request = Archive(
            id = folderRef.id,
            title = title,
        )

        try {
            folderRef.set(request).await()
        } catch (e: Exception) {
            throw e
        }
    }


    fun fetchPagedArchives(
        userId: String,
    ): Flow<PagingData<Archive>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ArchivePagingSource(
                    archiveCollection = userCollection.document(userId).collection("archives")
                )
            }
        ).flow
    }
}
