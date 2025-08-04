package com.sharathkolpe.utils

import LoginScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.sharathkolpe.afterLoginScreens.ConfirmationImageScreenVisible
import com.sharathkolpe.afterLoginScreens.DoctorDetailsScreen

import com.sharathkolpe.afterLoginScreens.HomeScreen
import com.sharathkolpe.afterLoginScreens.MyBookingsScreen
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

    const val MY_BOOKING_SCREEN = "myBookingScreen"
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


        composable(route = BeforeLoginScreensNavigationObject.MY_BOOKING_SCREEN) {
            MyBookingsScreen()
        }


        composable(
            route = "confirmation_screen/{tokenNumber}/{doctorName}/{time}/{session}/{bookingDateTime}",
            arguments = listOf(
                navArgument("tokenNumber") { type = NavType.IntType },
                navArgument("doctorName") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("session") { type = NavType.StringType },
                navArgument("bookingDateTime") { type = NavType.StringType })
        ) { backStackEntry ->
            val tokenNumber = backStackEntry.arguments?.getInt("tokenNumber") ?: 0
            val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val session = backStackEntry.arguments?.getString("session") ?: ""
            val bookingDateTime = backStackEntry.arguments?.getString("bookingDateTime") ?: ""

            ConfirmationImageScreenVisible(
                tokenNumber = tokenNumber,
                doctorName = doctorName,
                time = time,
                session = session,
                bookingDateTime = bookingDateTime
            ) {}
        }
    }
}