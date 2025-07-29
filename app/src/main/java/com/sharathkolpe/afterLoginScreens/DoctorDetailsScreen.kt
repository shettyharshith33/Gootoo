//package com.sharathkolpe.afterLoginScreens
//
//import SetStatusBarColor
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
//import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
//import com.sharathkolpe.viewmodels.DoctorDetailsViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DoctorDetailsScreen(
//    navController: NavController,
//    doctorId: String,
//    patientId: String, // you must pass this from FirebaseAuth or navArgs
//    viewModel: DoctorDetailsViewModel = viewModel()
//) {
//    SetStatusBarColor(gootooThemeBlue, useDarkIcons = false)
//    val doctorName by viewModel.doctorName.collectAsState()
//    val specialization by viewModel.specialization.collectAsState()
//    val experience by viewModel.experience.collectAsState()
//    val qualification by viewModel.qualification.collectAsState()
//    val clinicName by viewModel.clinicName.collectAsState()
//    val place by viewModel.place.collectAsState()
//    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
//    val slots by viewModel.slots.collectAsState()
//    val isBooking by viewModel.isBooking.collectAsState()
//
//    var bookingStatus by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(doctorId) {
//        println("🔍 doctorId: $doctorId") // Debug print
//        if (doctorId.isNotBlank()) {
//            viewModel.loadDoctorDetails(doctorId)
//        } else {
//            println("❌ Error: doctorId is blank!")
//        }
//    }
//
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = {
//                Text(
//                    "Doctor Details",
//                    fontFamily = poppinsFontFamily,
//                    color = gootooThemeBlue,
//                    fontWeight = FontWeight.SemiBold
//                )
//            })
//        }) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp)
//                    .border(
//                        .5.dp,
//                        shape = RoundedCornerShape(20f),
//                        color = gootooThemeBlue
//                    ), contentAlignment = Alignment.Center
//            )
//            {
//                Column(
//                    modifier = Modifier
//                        .padding(top = 10.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    if (profileImageUrl.isNotBlank()) {
//                        AsyncImage(
//                            model = profileImageUrl,
//                            contentDescription = "Doctor Profile Picture",
//                            modifier = Modifier
//                                .size(200.dp)
//                                .clip(CircleShape),
////                            .align(Alignment.CenterHorizontally),
//                            contentScale = ContentScale.Crop
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                    Text(
//                        "Dr. $doctorName",
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        fontFamily = poppinsFontFamily
//                    )
//                    Text(
//                        "Specialization: $specialization",
//                        fontSize = 16.sp,
//                        color = Color.DarkGray,
//                        fontFamily = poppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        "Clinic: $clinicName",
//                        fontSize = 16.sp,
//                        color = Color.DarkGray,
//                        fontFamily = poppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        "Place: $place",
//                        fontSize = 16.sp,
//                        color = Color.DarkGray,
//                        fontFamily = poppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        "Experience: $experience",
//                        fontSize = 16.sp,
//                        color = Color.DarkGray,
//                        fontFamily = poppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        "Qualification: $qualification",
//                        fontSize = 16.sp,
//                        color = Color.DarkGray,
//                        fontFamily = poppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.height(24.dp))
//
//            if (bookingStatus != null) {
//                Text(
//                    bookingStatus ?: "",
//                    color = if ("Success" in bookingStatus!!) Color(0xFF388E3C) else Color.Red,
//                    modifier = Modifier.padding(bottom = 12.dp)
//                )
//            }
//
//            Text("Available Slots:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            if (slots.isEmpty()) {
//                Text("Loading slots...", color = Color.DarkGray)
//            } else {
//                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(slots.entries.toList()) { (slotName, isBooked) ->
//                        val slotColor = if (isBooked) Color.Red else Color(0xFF4CAF50)
//                        val text = if (isBooked) "Booked" else "Available"
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(slotColor.copy(alpha = 0.1f))
//                                .clickable(enabled = !isBooked && !isBooking) {
//                                    viewModel.bookSlot(
//                                        doctorId = doctorId,
//                                        slot = slotName,
//                                        patientId = patientId,
//                                        onSuccess = {
//                                            bookingStatus =
//                                                "✅ Slot '$slotName' booked successfully!"
//                                        },
//                                        onFailure = {
//                                            bookingStatus = "❌ Booking failed: $it"
//                                        })
//                                }
//                                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
//                            Text(slotName, fontSize = 16.sp)
//                            Text(text, color = slotColor, fontWeight = FontWeight.SemiBold)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


//package com.example.patientapp.ui
//
//import android.net.Uri
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.*
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.*
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.sharathkolpe.viewmodels.DoctorDetailsViewModel
//import kotlinx.coroutines.launch
//
//@Composable
//fun DoctorDetailsScreen(
//    doctorId: String,
//    patientId: String,
//    viewModel: DoctorDetailsViewModel = viewModel(),
//    navController: NavController
//) {
//    var isBooking by remember { mutableStateOf(false) }
//    var showSuccessAnimation by remember { mutableStateOf(false) }
//
//
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    val name by viewModel.doctorName.collectAsState()
//    val specialization by viewModel.specialization.collectAsState()
//    val experience by viewModel.experience.collectAsState()
//    val qualification by viewModel.qualification.collectAsState()
//    val clinicName by viewModel.clinicName.collectAsState()
//    val place by viewModel.place.collectAsState()
//    val imageUrl by viewModel.profileImageUrl.collectAsState()
//    val availability by viewModel.availability.collectAsState()
//    val bookings by viewModel.currentDayBookings.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadDoctorDetails(doctorId)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Profile Image
//        Image(
//            painter = rememberAsyncImagePainter(imageUrl),
//            contentDescription = "Doctor Profile",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp)
//                .padding(4.dp),
//            contentScale = ContentScale.Crop
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Details
//        Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
//        Text("Specialization: $specialization")
//        Text("Qualification: $qualification")
//        Text("Experience: $experience years")
//        Text("Clinic: $clinicName, $place")
//
//        Spacer(modifier = Modifier.height(20.dp))
//        Divider()
//
//        Text("Today's Availability", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp))
//
//        if (availability.isEmpty()) {
//            Text("Doctor is unavailable today", color = Color.Red)
//        } else {
//            availability.forEach { (slot, time) ->
//                val bookedCount = bookings[slot] ?: 0
//                val start = time.first
//                val end = time.second
//                val selectedSession = slot
//                val displayTime = "$start - $end"
//                val encodedTime = Uri.encode(displayTime)
//
//                SessionCard(
//                    slot = slot,
//                    time = displayTime,
//                    booked = bookedCount,
//                    onBook = {
//                        scope.launch {
//                            viewModel.bookSlot(
//                                doctorId = doctorId,
//                                patientId = patientId,
//                                slot = slot,
//                                onSuccess = { tokenNumber, _ ->
//                                    Toast.makeText(
//                                        context,
//                                        "Booked! Token #$tokenNumber at $displayTime",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//
//                                    navController.navigate(
//                                        "confirmation_screen/$tokenNumber/${Uri.encode(name)}/$encodedTime/$selectedSession"
//                                    )
//                                },
//                                onFailure = {
//                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SessionCard(
//    slot: String,
//    time: String,
//    booked: Int,
//    onBook: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FF))
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(slot.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold)
//            Text("Time: $time")
//            Text("Booked tokens: $booked")
//            Button(
//                onClick = onBook,
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Book Appointment")
//            }
//        }
//    }
//}






package com.sharathkolpe.afterLoginScreens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.*
import com.sharathkolpe.beforeLoginScreens.LoadingAnimation
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.viewmodels.DoctorDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun DoctorDetailsScreen(
    doctorId: String,
    patientId: String,
    viewModel: DoctorDetailsViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val name by viewModel.doctorName.collectAsState()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Image
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Doctor Profile",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Details
        Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Specialization: $specialization")
        Text("Qualification: $qualification")
        Text("Experience: $experience years")
        Text("Clinic: $clinicName, $place")

        Spacer(modifier = Modifier.height(20.dp))
        Divider()

        Text(
            "Today's Availability",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (availability.isEmpty()) {
            Text("Doctor is unavailable today", color = Color.Red)
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
                                        "EEEE, dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()
                                    ).format(java.util.Date())


                                    Toast.makeText(
                                        context,
                                        "Booked! Token #$tokenNumber at $displayTime",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Wait for animation before navigating
                                    scope.launch {
                                        kotlinx.coroutines.delay(1200)
                                        showAnimation = false
                                        navController.navigate(
                                            "confirmation_screen/$tokenNumber/${Uri.encode(name)}/$encodedTime/$selectedSession/${Uri.encode(bookingDateTime)}"

                                        )
                                    }
                                },
                                onFailure = {
                                    bookingInProgress = false
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                )
            }
        }

        if (showAnimation) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val lottieUrl =
                    "https://firebasestorage.googleapis.com/v0/b/gootoo-13293.firebasestorage.app/o/Animations%2Fappointment_running_in.json?alt=media&token=b04820b5-979b-4633-a964-90016637c9e3"
                val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}

@Composable
fun SessionCard(
    slot: String,
    time: String,
    booked: Int,
    isLoading: Boolean,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(slot.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold)
            Text("Time: $time")
            Text("Booked tokens: $booked")
            if (isLoading) {
                CircularProgressIndicator(color = gootooThemeBlue)
            } else {
                Button(
                    onClick = onBook,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Book Appointment")
                }
            }
        }
    }
}
