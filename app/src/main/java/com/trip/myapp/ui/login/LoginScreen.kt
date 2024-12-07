package com.trip.myapp.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.trip.myapp.R

@Composable
fun LoginScreen(
    onClickLoginButton: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val loginEvent by authViewModel.event.collectAsState(initial = null)

    // GoogleSignInOptions 초기화
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    // GoogleSignInClient 초기화
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // ActivityResultLauncher 설정
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result: ActivityResult ->
            Log.d("Login", "onResult: ${result.resultCode}")
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                Log.d("Login", "onResult: data received")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account?.idToken
                    Log.d("Login", "onResult: idToken = $idToken")
                    if (idToken != null) {
                        authViewModel.loginWithGoogle(idToken)
                    } else {
                        Log.e("Login", "ID Token is null")
                        authViewModel.onLoginFailure(SignInErrorMessage.GOOGLE_SIGN_IN_FAIL)
                    }
                } catch (e: ApiException) {
                    Log.e("Login", "Google Sign-In Failed: ${e.statusCode}", e)
                    authViewModel.onLoginFailure(SignInErrorMessage.GOOGLE_SIGN_IN_FAIL)
                }
            } else {
                Log.e("Login", "Google Sign-In Failed: No Result")
                authViewModel.onLoginFailure(SignInErrorMessage.GOOGLE_SIGN_IN_FAIL)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 로고 이미지
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Logo",
            modifier = Modifier.size(480.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        GoogleSignInButton(
            onClick = {
                // Google Sign-In Intent 실행
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        )
    }

    LaunchedEffect(loginEvent) {
        when (loginEvent) {
            is LoginEvent.GoogleLoginSuccess -> onClickLoginButton()
            is LoginEvent.LoginFailure -> {
                Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google Sign-In Logo",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Sign in with Google")
    }
}
