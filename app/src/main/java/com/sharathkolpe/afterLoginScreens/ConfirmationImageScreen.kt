package com.sharathkolpe.afterLoginScreens

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.gootoo.ui.theme.warningRed
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent


@Composable
fun ConfirmationImageScreenVisible(
    tokenNumber: Int,
    doctorName: String,
    time: String,
    session: String,
    bookingDateTime: String,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewRef = remember { Ref<View>() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp


    Spacer(modifier = Modifier.height(screenHeight * 0.1.dp))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            AndroidView(
                factory = { ctx ->
                    ComposeView(ctx).apply {
                        viewRef.value = this
                        setContent {
                            ConfirmationCardVisible(
                                tokenNumber,
                                doctorName,
                                time,
                                session,
                                bookingDateTime
                            )
                        }
                    }
                }
            )

            Box(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Button(
                    modifier = Modifier.shimmer(),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = gootooThemeBlue
                    ), onClick = {
                        scope.launch {
                            viewRef.value?.let { view ->
                                saveViewAsImage(view, context)
                                onSaved()
                            }
                        }
                    }) {
                    Text(
                        "Download Token",
                        fontFamily = poppinsFontFamily,
                        color = Color.White
                    )

                }
            }
            Text(
                "Download or take a screenshot for safety", fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                fontFamily = poppinsFontFamily,
                color = Color.DarkGray
            )
        }
    }
}


@Composable
fun ConfirmationCardVisible(
    tokenNumber: Int,
    doctorName: String,
    time: String,
    session: String,
    bookingDateTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(Color(0xFFEFF6FF), RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Token", fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )
        Divider(
            thickness = 1.dp,
            color = gootooThemeBlue,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Doctor: $doctorName", fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
        )
        Text(
            "Session: $session", fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
        )
        Text(
            "Time: $time", fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
        )
        Text(
            bookingDateTime, fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
            color = warningRed
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Token Number: $tokenNumber",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1769AA),
            fontFamily = poppinsFontFamily,
        )
    }
}


fun saveViewAsImage(view: View, context: Context) {
    val bitmap = view.drawToBitmap(Bitmap.Config.ARGB_8888)
    val fileName = "confirmation_${System.currentTimeMillis()}.png"

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.Images.Media.TITLE, fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        resolver.openOutputStream(uri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }

        Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Failed to create image file", Toast.LENGTH_SHORT).show()
    }
}
