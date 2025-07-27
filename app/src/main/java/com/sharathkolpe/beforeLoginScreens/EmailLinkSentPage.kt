
package com.sharathkolpe.beforeLoginScreens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.sharathkolpe.gootoo.R
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.utils.BeforeLoginScreensNavigationObject
import com.sharathkolpe.gootoo.ui.theme.newGreen
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.gootoo.ui.theme.themeBlue

@Composable
fun EmailLinkSentPage(navController: NavController) {
    val context = LocalContext.current
    var isVerified by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    fun checkVerificationStatus() {
        currentUser?.reload()?.addOnCompleteListener {
            if (currentUser.isEmailVerified) {
                isVerified = true
                Toast.makeText(context, "Email Verified!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Not Verified Yet. Please Check Your Email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.otp_animation))
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(screenWidth * 0.7f),
                iterations = LottieConstants.IterateForever
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(
                "Link Sent to your Email",
                fontFamily = FontFamily.Serif,
                color = themeBlue,
                fontSize = (screenWidth.value * 0.05).sp
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            Text(
                "Click on the received link",
                fontFamily = FontFamily.Serif,
                color = newGreen,
                fontSize = (screenWidth.value * 0.045).sp
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            if (!isVerified) {
                Button(onClick = { checkVerificationStatus() }) {
                    Text(
                        "I've Verified, Check Now",
                        color = Color.White,
                        fontFamily = poppinsFontFamily,
                        fontSize = (screenWidth.value * 0.04).sp
                    )
                }
            } else {
                Button(onClick = {
                    navController.navigate(BeforeLoginScreensNavigationObject.LOGIN_SCREEN)
                },
                    colors = ButtonDefaults.buttonColors().copy(containerColor = gootooThemeBlue)) {
                    Text(
                        "Continue",
                        color = Color.White,
                        fontFamily = poppinsFontFamily,
                        fontSize = (screenWidth.value * 0.04).sp
                    )
                }
            }
        }
    }
}
