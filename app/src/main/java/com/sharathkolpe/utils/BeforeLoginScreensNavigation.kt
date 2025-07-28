package com.sharathkolpe.utils

import LoginScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sharathkolpe.afterLoginScreens.DoctorDetailsScreen
import com.sharathkolpe.afterLoginScreens.HomeScreen
import com.sharathkolpe.beforeLoginScreens.AuthCheckScreen
import com.sharathkolpe.beforeLoginScreens.EmailLinkSentPage
import com.sharathkolpe.beforeLoginScreens.OnBoardingScreen
import com.sharathkolpe.beforeLoginScreens.OtpVerificationPage
import com.sharathkolpe.beforeLoginScreens.SignUpScreen

object BeforeLoginScreensNavigationObject {
    const val AUTH_CHECK = "authCheck"
    const val OTP_REQUEST_PAGE = "otpRequestPage"

    const val OTP_VERIFICATION_PAGE = "otpVerificationPage"
    const val ONBOARDING_SCREEN = "onBoardingScreen"
    const val SIGNUP_SCREEN = "signUpScreen"
    const val LOGIN_SCREEN = "loginScreen"
    const val HOME_SCREEN = "homeScreen"
    const val EMAIL_LINK_SENT_PAGE = "emailLinkSentPage"

    const val DOCTOR_DETAILS_SCREEN = "doctorDetailsScreen"
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BeforeLoginScreensNavigation(navController: NavController) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = BeforeLoginScreensNavigationObject.ONBOARDING_SCREEN
    ) {
        composable("otp_verify/{verificationId}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId")
            OtpVerificationPage(navController, verificationId)
        }

        composable(BeforeLoginScreensNavigationObject.AUTH_CHECK) {
            AuthCheckScreen(navController)
        }

        composable(route = BeforeLoginScreensNavigationObject.ONBOARDING_SCREEN) {
            OnBoardingScreen(navController)
        }

        composable(route = BeforeLoginScreensNavigationObject.SIGNUP_SCREEN) {
            SignUpScreen(navController)
        }

        composable(route = BeforeLoginScreensNavigationObject.LOGIN_SCREEN) {
            LoginScreen(navController)
        }

        composable(route = BeforeLoginScreensNavigationObject.HOME_SCREEN) {
            HomeScreen(navController)
        }

        composable(route = BeforeLoginScreensNavigationObject.EMAIL_LINK_SENT_PAGE) {
            EmailLinkSentPage(navController)
        }

        composable("doctorDetails/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            DoctorDetailsScreen(
                navController = navController,
                doctorId = doctorId,
                patientId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )
        }

    }
}