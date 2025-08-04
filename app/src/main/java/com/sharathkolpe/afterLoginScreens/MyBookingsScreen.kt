package com.sharathkolpe.afterLoginScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.viewmodels.MyBookingsViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

@Composable
fun MyBookingsScreen(viewModel: MyBookingsViewModel = viewModel()) {
    val bookings by remember { derivedStateOf { viewModel.userBookings } }
    val doctorNames by remember { derivedStateOf { viewModel.doctorNames } }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(statusBarHeight + (screenHeight * 0.001f .dp)))
        Text(
            text = "My Bookings",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = screenHeight * 0.001.dp)
        )

        if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No bookings found.")
            }
        } else {
            LazyColumn {
                items(bookings) { booking ->
                    val doctorName = doctorNames[booking.doctorId] ?: "Loading..."

                    // Proper timestamp conversion
                    val formattedDate = remember(booking.timestamp) {
                        try {
                            val date = Date(booking.timestamp) // timestamp is already Long
                            val formatter = SimpleDateFormat("EEE, dd MMM yyyy - hh:mm a", Locale.getDefault())
                            formatter.format(date)
                        } catch (e: Exception) {
                            "Invalid date"
                        }
                    }


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Doctor: $doctorName", style = MaterialTheme.typography.bodyLarge)
                            Text("Session: ${booking.session.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.bodyMedium)
                            Text("Token No: ${booking.tokenNumber}", style = MaterialTheme.typography.bodyMedium)
                            Text("Date: $formattedDate", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
