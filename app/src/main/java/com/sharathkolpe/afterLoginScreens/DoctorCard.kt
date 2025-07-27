package com.sharathkolpe.afterLoginScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sharathkolpe.gootoo.ui.theme.lightGreen
import com.sharathkolpe.gootoo.ui.theme.myGreen
import com.sharathkolpe.gootoo.ui.theme.poppinsFontFamily
import com.sharathkolpe.gootoo.ui.theme.spotifyGreen

@Composable
fun DoctorCard(doctor: Doctor,
               onClick: () -> Unit ) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = doctor.profileImageUrl,
                contentDescription = "Doctor Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    "Dr. ${doctor.name}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    fontFamily = poppinsFontFamily
                )
                Text("")
                Text("Specialization: ${doctor.specialization}",
                    color = Color.DarkGray,
                    fontFamily = poppinsFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold)
                Text("Experience: ${doctor.experience} Year(s)",
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold)
                Text("Qualification: ${doctor.qualification} Year(s)",
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold)
                Text("Clinic: ${doctor.clinicName}",
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold)
                Text("")
                Box(modifier = Modifier.fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(lightGreen),
                    contentAlignment = Alignment.Center)
                {
                    Text("Book Appointment",
                        fontFamily = poppinsFontFamily,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
