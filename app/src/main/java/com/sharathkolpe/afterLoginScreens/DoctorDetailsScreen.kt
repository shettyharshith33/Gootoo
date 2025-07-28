package com.sharathkolpe.afterLoginScreens

import SetStatusBarColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.viewmodels.DoctorDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailsScreen(
    navController: NavController,
    doctorId: String,
    patientId: String, // you must pass this from FirebaseAuth or navArgs
    viewModel: DoctorDetailsViewModel = viewModel()
) {
    SetStatusBarColor(gootooThemeBlue, useDarkIcons = false)
    val doctorName by viewModel.doctorName.collectAsState()
    val specialization by viewModel.specialization.collectAsState()
    val experience by viewModel.experience.collectAsState()
    val qualification by viewModel.qualification.collectAsState()
    val clinicName by viewModel.clinicName.collectAsState()
    val place by viewModel.place.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val slots by viewModel.slots.collectAsState()
    val isBooking by viewModel.isBooking.collectAsState()

    var bookingStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(doctorId) {
        println("🔍 doctorId: $doctorId") // Debug print
        if (doctorId.isNotBlank()) {
            viewModel.loadDoctorDetails(doctorId)
        } else {
            println("❌ Error: doctorId is blank!")
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Doctor Details",
                    fontFamily = poppinsFontFamily,
                    color = gootooThemeBlue,
                    fontWeight = FontWeight.SemiBold
                )
            })
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .border(
                        .5.dp,
                        shape = RoundedCornerShape(20f),
                        color = gootooThemeBlue
                    ), contentAlignment = Alignment.Center
            )
            {
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (profileImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Doctor Profile Picture",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape),
//                            .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Text(
                        "Dr. $doctorName",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )
                    Text(
                        "Specialization: $specialization",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Clinic: $clinicName",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Place: $place",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Experience: $experience",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Qualification: $qualification",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (bookingStatus != null) {
                Text(
                    bookingStatus ?: "",
                    color = if ("Success" in bookingStatus!!) Color(0xFF388E3C) else Color.Red,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Text("Available Slots:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            if (slots.isEmpty()) {
                Text("Loading slots...", color = Color.DarkGray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(slots.entries.toList()) { (slotName, isBooked) ->
                        val slotColor = if (isBooked) Color.Red else Color(0xFF4CAF50)
                        val text = if (isBooked) "Booked" else "Available"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(slotColor.copy(alpha = 0.1f))
                                .clickable(enabled = !isBooked && !isBooking) {
                                    viewModel.bookSlot(
                                        doctorId = doctorId,
                                        slot = slotName,
                                        patientId = patientId,
                                        onSuccess = {
                                            bookingStatus =
                                                "✅ Slot '$slotName' booked successfully!"
                                        },
                                        onFailure = {
                                            bookingStatus = "❌ Booking failed: $it"
                                        })
                                }
                                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(slotName, fontSize = 16.sp)
                            Text(text, color = slotColor, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
