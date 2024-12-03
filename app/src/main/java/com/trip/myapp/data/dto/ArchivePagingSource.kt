package com.trip.myapp.data.dto

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.trip.myapp.domain.model.Archive
import kotlinx.coroutines.tasks.await

class ArchivePagingSource(
    private val archiveCollection: CollectionReference,
) : PagingSource<Query, Archive>() {

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, Archive> {
        return try {
            val query = params.key ?: archiveCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val querySnapshot = query.get().await()
            val documents = querySnapshot.documents
            val archives = mutableListOf<Archive>()
            for (document in documents) {
                try {
                    val communityPost = document.toObject(Archive::class.java)
                    if (communityPost != null) {
                        archives.add(communityPost)
                    }
                } catch (e: Exception) {
                    Log.e("ArchivePagingSource","community post 변환 에러")
                }
            }
            val nextQuery = if (documents.isNotEmpty()) {
                query.startAfter(documents.last())
            } else {
                null
            }

            LoadResult.Page(
                data = archives,
                prevKey = null,
                nextKey = nextQuery,
            )
        } catch (e: Exception) {
            Log.e("ArchivePagingSource","community post 로딩 에러")
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Query, Archive>): Query? {
        return null
    }


}
