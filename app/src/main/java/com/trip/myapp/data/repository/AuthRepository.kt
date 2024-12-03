package com.trip.myapp.data.repository

import android.content.Context
import android.net.Uri
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth
){

    fun firebaseSignOut() {
        auth.signOut()
    }

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }


    suspend fun snsSignOut() {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    fun getUserEmail(): String? {
        auth.currentUser?.reload()
        return auth.currentUser?.email
    }

    fun getSignInProvider(): String? {
        val user = auth.currentUser
        if (user != null) {
            for (profile in user.providerData) {
                when (profile.providerId) {
                    "google.com" -> return "Google"
                }
            }
        }
        return null
    }

    fun getUserUID(): String? {
        return auth.currentUser?.uid
    }

    fun getUserName(): String? {
        return auth.currentUser?.displayName
    }

    fun getUserPhotoUrl(): Uri? {
        return auth.currentUser?.photoUrl
    }
}
