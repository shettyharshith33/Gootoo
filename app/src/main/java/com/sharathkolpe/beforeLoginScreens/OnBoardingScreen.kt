package com.sharathkolpe.beforeLoginScreens


//----------------Onboarding Screen / Landing Screen----------------

//All Necessary Imports

import SetStatusBarColor
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.sharathkolpe.gootoo.R
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.utils.BeforeLoginScreensNavigationObject
import com.sharathkolpe.utils.GoogleAuthUIClient
import com.sharathkolpe.viewmodels.AuthViewModel
import com.sharathkolpe.viewmodels.NetworkViewModel
import com.valentinilk.shimmer.shimmer
import java.util.concurrent.TimeUnit


@Composable
//OnBoarding Screen - This is the code of the first screen that will be visible to user once the app is opened. This page contains login and signup buttons.
fun OnBoardingScreen(
    navController: NavController, viewModel: NetworkViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.observeAsState()

    SetStatusBarColor(Color(gootooThemeBlue.toArgb()), useDarkIcons = false)

    // Get screen width and height
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val activity = context as ComponentActivity
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var loadingAnimation by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_screen))
    var mobileNum by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    //Mutable variable to remember Sign-in Activity
    val isSigningIn = remember { mutableStateOf(false) }


    val viewModel: AuthViewModel = hiltViewModel()
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val googleAuthUIClient = remember { GoogleAuthUIClient(context, oneTapClient) }

    val firebaseFirebaseAppCheck = FirebaseAppCheck.getInstance()
    firebaseFirebaseAppCheck.installAppCheckProviderFactory(
        PlayIntegrityAppCheckProviderFactory.getInstance()
    )

    var confirmLogin by remember { mutableStateOf(false) }


    //Top Screen Text and Various Buttons for Login And Signup
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidth * 0.05f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // App Name
        Spacer(modifier = Modifier.height(screenHeight * 0.2f))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = poppinsFontFamily,
                        fontSize = 35.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Goo")
                }


                withStyle(
                    style = SpanStyle(
                        fontFamily = poppinsFontFamily,
                        color = gootooThemeBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 35.sp
                    )
                ) {
                    append("too")
                }
            }, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )

        val lottieUrl =
            "https://firebasestorage.googleapis.com/v0/b/gootoo-13293.firebasestorage.app/o/Animations%2Fsplash_screen.json?alt=media&token=178bfd0b-3e3b-4850-bcb7-c1d1fffa1901"
        val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))


        if (composition != null) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(200.dp)
            )
        } else {
            // Show shimmer while loading
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shimmer()
                    .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Icon"
                        )
                    },
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= 10) {
                            phoneNumber = it
                        }
                    },
                    colors = TextFieldDefaults.colors().copy(
                        unfocusedIndicatorColor = gootooThemeBlue,
                        focusedIndicatorColor = gootooThemeBlue,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    //prefix = {Text("+91")},
                    label = { Text("Phone Number +91", color = Color.LightGray) },
                    placeholder = { Text("Enter phone number", color = Color.LightGray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    modifier = Modifier
                        .width(130.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = gootooThemeBlue
                    ),
                    onClick = {
                        if (phoneNumber.isNotBlank()) {
                            isLoading = true
                            loadingAnimation = true
                            sendVerificationCode(
                                phoneNumber,
                                activity,
                                onCodeSent = { verificationId ->
                                    isLoading = false
                                    navController.navigate("otp_verify/$verificationId")
                                },
                                onVerificationFailed = {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Failed!",
                                        //${it.message}
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    },
                    enabled = !isLoading,
                ) {
                    Text(if (isLoading) "Loading" else "Send OTP", color = Color.White)
                }


                Spacer(modifier = Modifier.height(screenHeight * 0.03f))
                //SignUp Screen Navigation Button
                Text(
                    "Click here to create account",
                    fontFamily = poppinsFontFamily,
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(
                                BeforeLoginScreensNavigationObject.SIGNUP_SCREEN
                            )
                        }
                        .shimmer()
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.03f))
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(gootooThemeBlue)
                        .fillMaxWidth(.7f)
                        .clickable { navController.navigate(BeforeLoginScreensNavigationObject.LOGIN_SCREEN) },
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        "Login with Email and Password",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
    if (loadingAnimation)
    {
        LoadingAnimation()
    }
}


@Composable
fun LoadingAnimation()
{

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {


        val lottieUrl =
            "https://firebasestorage.googleapis.com/v0/b/gootoo-13293.firebasestorage.app/o/Animations%2Floading_doctor.json?alt=media&token=69a4f774-21d9-4369-b25a-be9971bb27d2"
        val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))


        Column {
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    "Please Wait..Sending OTP",
                    modifier = Modifier.shimmer(),
                    color = gootooThemeBlue,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            } else {
                // Show shimmer while loading
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .shimmer()
                        .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                )
            }
        }
    }

}

fun sendVerificationCode(
    phoneNumber: String,
    activity: Activity,
    onCodeSent: (String) -> Unit,
    onVerificationFailed: (Exception) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
        .setPhoneNumber("+91$phoneNumber")
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                Log.d("OTP", "Auto verification success: $credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("OTP", "Verification failed: ${e.message}")
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("OTP", "Code sent: $verificationId")
                onCodeSent(verificationId)
            }
        })
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}


//@Composable
//fun OnBoardingScreen(navController: NavController) {
//    val context = LocalContext.current
//    val activity = context as ComponentActivity
//    var phoneNumber by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            label = { Text("Enter Phone Number") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !isLoading
//        ) {
//            Text(if (isLoading) "Sending..." else "Send OTP")
//        }
//    }
//}
//

//
//
//
