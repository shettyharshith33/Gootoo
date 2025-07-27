package com.sharathkolpe.afterLoginScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sharathkolpe.viewmodels.DoctorDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailsScreen(
    navController: NavController,
    doctorId: String,
    patientId: String, // you must pass this from FirebaseAuth or navArgs
    viewModel: DoctorDetailsViewModel = viewModel()
) {
    val doctorName by viewModel.doctorName.collectAsState()
    val specialization by viewModel.specialization.collectAsState()
    val slots by viewModel.slots.collectAsState()
    val isBooking by viewModel.isBooking.collectAsState()

    var bookingStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(doctorId) {
        viewModel.loadDoctorDetails(doctorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Doctor Details") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(doctorName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(specialization, fontSize = 16.sp, color = Color.Gray)
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
                Text("Loading slots...", color = Color.Gray)
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
                                            bookingStatus = "✅ Slot '$slotName' booked successfully!"
                                        },
                                        onFailure = {
                                            bookingStatus = "❌ Booking failed: $it"
                                        }
                                    )
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(slotName, fontSize = 16.sp)
                            Text(text, color = slotColor, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
