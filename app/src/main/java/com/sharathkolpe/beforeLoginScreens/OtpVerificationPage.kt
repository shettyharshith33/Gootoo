package com.sharathkolpe.beforeLoginScreens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.utils.BeforeLoginScreensNavigationObject
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationPage(navController: NavController, verificationId: String?) {
    val context = LocalContext.current
    var otpCode by remember { mutableStateOf("") }
    var isVerifying by remember { mutableStateOf(false) }
    var showVerifiedAnimation by remember { mutableStateOf(false) }


    if (verificationId == null) {
        Toast.makeText(context, "Verification ID is missing!", Toast.LENGTH_LONG).show()
        return
    }


    if (showVerifiedAnimation) {
        VerifiedAnimation()

        // Delay navigation for 3 seconds
        LaunchedEffect(Unit) {
            delay(5000)
            navController.navigate(BeforeLoginScreensNavigationObject.HOME_SCREEN) {
                popUpTo(0) // Optional: clears backstack
            }
        }

    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = gootooThemeBlue,
                    unfocusedIndicatorColor = gootooThemeBlue,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = gootooThemeBlue
                ),
                onClick = {
                    isVerifying = true
                    val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            isVerifying = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()
                                showVerifiedAnimation=true
                                //navController.navigate(BeforeLoginScreensNavigationObject.HOME_SCREEN) // navigate to next screen
                            } else {
                                Toast.makeText(context, "OTP Verification Failed!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isVerifying
            ) {
                Text(if (isVerifying) "Verifying..." else "Verify OTP")
            }
        }
    }
}


@Composable
fun VerifiedAnimation() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {


        val lottieUrl =
            "https://firebasestorage.googleapis.com/v0/b/gootoo-13293.firebasestorage.app/o/Animations%2Fapp_successful.json?alt=media&token=6443a213-4835-4043-bdd4-cd0054b7e3a3"
        val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))


        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(300.dp)
                )
                Text(
                    "OTP Verified",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = gootooThemeBlue,
                    fontSize = 30.sp,
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