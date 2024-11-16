package com.trip.myapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.trip.myapp.ui.MainScreen
import com.trip.myapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var oneTapClient: SignInClient

    @Inject
    lateinit var signInRequest: BeginSignInRequest


    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                handleGoogleSignInResult(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(onGoogleSignInClick = ::startGoogleSignIn)
            }
        }
    }

    private fun startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                signInLauncher.launch(
                    IntentSenderRequest.Builder(result.pendingIntent).build()
                )
            }
            .addOnFailureListener { exception ->
                Log.e("GoogleSignIn", "Google Sign-In Failed", exception)
            }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken)
            } else {
                Log.e("GoogleSignIn", "Google Sign-In Token is null")
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error processing Google Sign-In result", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d("GoogleSignIn", "Sign-In Successful: ${user?.displayName}")
                    // Handle success (e.g., navigate to home screen)
                } else {
                    Log.e("GoogleSignIn", "Sign-In Failed", task.exception)
                }
            }
    }
}
