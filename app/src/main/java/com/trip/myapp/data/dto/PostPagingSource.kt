package com.trip.myapp.data.dto

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.trip.myapp.domain.model.Post
import kotlinx.coroutines.tasks.await

class PostPagingSource(
    private val postCollection: CollectionReference
) : PagingSource<Query, Post>() {

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, Post> {
        return try {
            val query = params.key ?: postCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val querySnapshot = query.get().await()
            val documents = querySnapshot.documents
            val posts = mutableListOf<Post>()
            for (document in documents) {
                try {
                    val post = document.toObject(Post::class.java)
                    if (post != null) {
                        posts.add(post)
                    }
                } catch (e: Exception) {
                    Log.e("PostPagingSource", "post 변환 에러")
                    Log.e("PostPagingSource", "$e")
                }
            }
            val nextQuery = if (documents.isNotEmpty()) {
                query.startAfter(documents.last())
            } else {
                null
            }

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = nextQuery,
            )
        } catch (e: Exception) {
            Log.e("PostPagingSource", "$e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, Post>): Query? {
        return null
    }
}
