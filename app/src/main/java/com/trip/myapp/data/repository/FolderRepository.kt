package com.trip.myapp.data.repository

import com.trip.myapp.data.FirebaseDataSource
import com.trip.myapp.data.model.Folder
import javax.inject.Inject

class FolderRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
) {
    suspend fun addFolder(folder: Folder): Boolean {
        return firebaseDataSource.addCategoryFolder(folder)
    }
}