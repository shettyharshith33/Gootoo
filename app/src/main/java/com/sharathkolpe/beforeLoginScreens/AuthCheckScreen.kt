package com.sharathkolpe.beforeLoginScreens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.sharathkolpe.utils.BeforeLoginScreensNavigationObject

@Composable
fun AuthCheckScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        Log.d("AuthCheckScreen", "Current User: ${currentUser?.email}")

        if (currentUser != null) {
            // Session exists — redirect to HOME
            navController.navigate(BeforeLoginScreensNavigationObject.HOME_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // No session — redirect to ONBOARDING
            navController.navigate(BeforeLoginScreensNavigationObject.ONBOARDING_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
