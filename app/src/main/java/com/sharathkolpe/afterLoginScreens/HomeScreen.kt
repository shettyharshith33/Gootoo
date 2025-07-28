package com.sharathkolpe.afterLoginScreens

import SetStatusBarColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sharathkolpe.gootoo.ui.theme.gootooThemeBlue
import com.sharathkolpe.viewmodels.PatientHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: PatientHomeViewModel = viewModel()
    val doctorList by viewModel.doctorList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    SetStatusBarColor(gootooThemeBlue, useDarkIcons = false)

    // Fetch doctors when HomeScreen loads
    LaunchedEffect(Unit)
    {
        viewModel.fetchDoctorsFromFirestore()
    }

    val filteredDoctors = if (searchQuery.isBlank()) {
        doctorList
    } else {
        doctorList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.specialization.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Your Doctor") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("bookings")
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Bookings") },
                    label = { Text("Bookings") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔍 Search Field
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(Color(0xFFEFEFEF), shape = MaterialTheme.shapes.small)
                    .padding(12.dp),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text("Search by name or specialization 🔍", color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (isLoading) {
                    items(5) {
                        DoctorCardShimmer()
                    }
                } else {
                    if (filteredDoctors.isEmpty()) {
                        item {
                            Text("No doctors found.", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        items(filteredDoctors) { doctor ->
                            DoctorCard(
                                doctor = doctor
                            )
                            {
                                println("👀 Clicked doctor ID: ${doctor.id}")
                                navController.navigate("doctorDetails/${doctor.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}