package com.trip.myapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.trip.myapp.domain.model.Archive
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseDataSource @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addCategoryFolder(archive: Archive): Boolean {
        return suspendCoroutine { continuation ->
            db.collection("community")
                .add(archive)
                .addOnSuccessListener {
                    Log.d("add folder","DocumentSnapshot added with ID: ${it.id}")
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Log.w("add folder fail", "Error adding document ${exception.message}")
                    continuation.resume(false)
                }
        }
    }
}