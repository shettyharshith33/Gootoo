//package com.sharathkolpe.afterLoginScreens
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class MyBookingsViewModel : ViewModel() {
//    private val _bookings = mutableStateListOf<UserBooking>()
//    val bookings: List<UserBooking> = _bookings
//
//    private val db = FirebaseFirestore.getInstance()
//    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//
//    init {
//        fetchUserBookings()
//    }
//
//    private fun fetchUserBookings() {
//        if (currentUserId == null) return
//
//        db.collection("appointments").get()
//            .addOnSuccessListener { doctorSnapshots ->
//                doctorSnapshots.documents.forEach { doctorDoc ->
//                    val doctorId = doctorDoc.id
//                    db.collection("appointments")
//                        .document(doctorId)
//                        .collection("tokens")
//                        .whereEqualTo("patientId", currentUserId)
//                        .get()
//                        .addOnSuccessListener { tokenSnapshots ->
//                            tokenSnapshots.documents.mapNotNull { doc ->
//                                val data = doc.toObject(UserBooking::class.java)
//                                data?.copy(doctorId = doctorId)
//                            }.let { bookings ->
//                                _bookings.addAll(bookings)
//                            }
//                        }
//                }
//            }
//    }
//}






package com.sharathkolpe.viewmodels

import UserBooking
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class MyBookingsViewModel : ViewModel() {

    private val _userBookings = mutableStateListOf<UserBooking>()
    val userBookings: List<UserBooking> = _userBookings

    private val _doctorNames = mutableStateMapOf<String, String>()
    val doctorNames: Map<String, String> = _doctorNames

    init {
        fetchBookingsForUser()
    }

    private fun fetchBookingsForUser() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("appointments")
            .document(userId)
            .collection("tokens")
            .get()
            .addOnSuccessListener { snapshot ->
                val bookings = snapshot.documents.mapNotNull { doc ->
                    try {
                        val doctorId = doc.getString("doctorId") ?: return@mapNotNull null
                        val patientId = doc.getString("patientId") ?: return@mapNotNull null
                        val tokenNumber = doc.getLong("tokenNumber")?.toInt() ?: 0
                        val session = doc.getString("slot") ?: ""
                        val timestamp = doc.getTimestamp("timestamp")?.toDate()?.time ?: 0L

                        UserBooking(
                            doctorId = doctorId,
                            patientId = patientId,
                            tokenNumber = tokenNumber,
                            session = session,
                            timestamp = timestamp
                        )
                    } catch (e: Exception) {
                        Log.e("MyBookingsViewModel", "Error parsing booking", e)
                        null
                    }
                }.sortedByDescending { it.timestamp }

                Log.d("MyBookingsViewModel", "Fetched ${bookings.size} bookings for user: $userId")

                _userBookings.clear()
                _userBookings.addAll(bookings)

                bookings.forEach { booking ->
                    fetchDoctorNameIfNeeded(booking.doctorId)
                }
            }
            .addOnFailureListener { e ->
                Log.e("MyBookingsViewModel", "Failed to fetch bookings", e)
                _userBookings.clear()
            }
    }

    private fun fetchDoctorNameIfNeeded(doctorId: String) {
        if (_doctorNames.containsKey(doctorId)) return

        FirebaseFirestore.getInstance()
            .collection("doctors")
            .document(doctorId)
            .get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: "Unknown Doctor"
                _doctorNames[doctorId] = name
            }
            .addOnFailureListener {
                _doctorNames[doctorId] = "Unknown Doctor"
            }
    }
}
