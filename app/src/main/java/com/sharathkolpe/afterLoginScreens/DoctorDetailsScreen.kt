package com.sharathkolpe.afterLoginScreens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.viewmodels.DoctorDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DoctorDetailsScreen(
    doctorId: String,
    patientId: String,
    viewModel: DoctorDetailsViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val nameRaw by viewModel.doctorName.collectAsState()
    val name = if (nameRaw.startsWith("Dr.", ignoreCase = true)) nameRaw else "Dr. $nameRaw"
    val specialization by viewModel.specialization.collectAsState()
    val experience by viewModel.experience.collectAsState()
    val qualification by viewModel.qualification.collectAsState()
    val clinicName by viewModel.clinicName.collectAsState()
    val place by viewModel.place.collectAsState()
    val imageUrl by viewModel.profileImageUrl.collectAsState()
    val availability by viewModel.availability.collectAsState()
    val bookings by viewModel.currentDayBookings.collectAsState()

    var bookingInProgress by remember { mutableStateOf(false) }
    var showAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDoctorDetails(doctorId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                // Profile Image
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Doctor Profile",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(4.dp, gootooThemeBlue, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Details
                Text(
                    name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append("Specialization: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = gootooThemeBlue
                            )
                        ) {
                            append(specialization)
                        }
                    })
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Qualification: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = gootooThemeBlue
                            )
                        ) {
                            append(qualification)
                        }
                    })
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Experience: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = gootooThemeBlue
                            )
                        ) {
                            append("$experience year(s)")
                        }
                    })
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Clinic: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = gootooThemeBlue
                            )
                        ) {
                            append(clinicName)
                        }
                    })
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Place: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = gootooThemeBlue
                            )
                        ) {
                            append(place)
                        }
                    })

                Spacer(modifier = Modifier.height(20.dp))
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    "Today's Availability",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontFamily = poppinsFontFamily
                )

                if (availability.isEmpty()) {
                    Text(
                        "Doctor is unavailable today",
                        color = Color.Red,
                        fontFamily = poppinsFontFamily
                    )
                } else {


                    availability.forEach { (slot, time) ->
                        val bookedCount = bookings[slot] ?: 0
                        val start = time.first
                        val end = time.second
                        val selectedSession = slot
                        val displayTime = "$start - $end"
                        val encodedTime = Uri.encode(displayTime)

                        SessionCard(
                            slot = slot,
                            time = displayTime,
                            booked = bookedCount,
                            sessionStartTime = start, // pass start time like "09:00 AM"
                            isLoading = bookingInProgress,
                            onBook = {

                                bookingInProgress = true
                                scope.launch {
                                    viewModel.bookSlot(
                                        doctorId = doctorId,
                                        patientId = patientId,
                                        slot = slot,
                                        onSuccess = { tokenNumber, _ ->
                                            bookingInProgress = false
                                            showAnimation = true

                                            val bookingDateTime = java.text.SimpleDateFormat(
                                                "EEEE, dd MMM yyyy, hh:mm a",
                                                java.util.Locale.getDefault()
                                            ).format(java.util.Date())

                                            Toast.makeText(
                                                context,
                                                "Booked! Token #$tokenNumber at $displayTime",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            scope.launch {
                                                delay(5000)
                                                showAnimation = false
                                                navController.navigate(
                                                    "confirmation_screen/$tokenNumber/${
                                                        Uri.encode(
                                                            name
                                                        )
                                                    }/$encodedTime/$selectedSession/${
                                                        Uri.encode(
                                                            bookingDateTime
                                                        )
                                                    }"
                                                )
                                            }
                                        },
                                        onFailure = {
                                            bookingInProgress = false
                                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                        })
                                }
                            }
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (showAnimation) {
            val lottieUrl =
                "https://firebasestorage.googleapis.com/v0/b/gootoo-13293.firebasestorage.app/o/Animations%2Fappointment_running_in.json?alt=media&token=b04820b5-979b-4633-a964-90016637c9e3"
            val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(300.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), color = gootooThemeBlue
                    )
                    Text(
                        "Token Booked Successfully",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@Composable
fun SessionCard(
    slot: String,
    time: String,
    booked: Int,
    sessionStartTime: String,
    isLoading: Boolean,
    onBook: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val sessionTime = remember(sessionStartTime) {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val parsedTime = formatter.parse(sessionStartTime) ?: Date()

        val now = Calendar.getInstance()
        val sessionCalendar = Calendar.getInstance().apply {
            timeInMillis = parsedTime.time
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
        }
        sessionCalendar.timeInMillis
    }


    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val remainingMillis = sessionTime - currentTime - (60 * 60 * 1000) // 1 hour before

    val isBookable = remainingMillis <= 0

    // Update timer every second
    LaunchedEffect(Unit) {
        while (!isBookable) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val hours = (remainingMillis / (1000 * 60 * 60)).coerceAtLeast(0)
    val minutes = ((remainingMillis / (1000 * 60)) % 60).coerceAtLeast(0)
    val seconds = ((remainingMillis / 1000) % 60).coerceAtLeast(0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                slot.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Text("Time: $time", fontFamily = poppinsFontFamily)
            Text("Booked tokens: $booked", fontFamily = poppinsFontFamily)

            if (isLoading) {
                CircularProgressIndicator(color = gootooThemeBlue)
            } else {
                if (isBookable) {
                    Button(
                        onClick = onBook,
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors()
                            .copy(containerColor = gootooThemeBlue)
                    ) {
                        Text("Book Appointment", fontFamily = poppinsFontFamily)
                    }
                } else {
                    Text(
                        text = "You can book token in %02d:%02d:%02d".format(
                            hours,
                            minutes,
                            seconds
                        ),
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}
