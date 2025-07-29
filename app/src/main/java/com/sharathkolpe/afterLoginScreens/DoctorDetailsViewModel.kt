//package com.sharathkolpe.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.SetOptions
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class DoctorDetailsViewModel : ViewModel() {
//
//    private val firestore = FirebaseFirestore.getInstance()
//
//    private val _doctorName = MutableStateFlow("")
//    val doctorName: StateFlow<String> = _doctorName
//
//    private val _specialization = MutableStateFlow("")
//    val specialization: StateFlow<String> = _specialization
//
//
//    private val _experience = MutableStateFlow("")
//    val experience: StateFlow<String> = _experience
//
//    private val _qualification = MutableStateFlow("")
//    val qualification: StateFlow<String> = _qualification
//
//    private val _clinicName = MutableStateFlow("")
//    val clinicName: StateFlow<String> = _clinicName
//
//    private val _profileImageUrl = MutableStateFlow("")
//    val profileImageUrl: StateFlow<String> = _profileImageUrl
//
//
//    private val _place = MutableStateFlow("")
//    val place: StateFlow<String> = _place
//
//
//    private val _slots = MutableStateFlow<Map<String, Boolean>>(emptyMap())
//    val slots: StateFlow<Map<String, Boolean>> = _slots
//
//    private val _isBooking = MutableStateFlow(false)
//    val isBooking: StateFlow<Boolean> = _isBooking
//
//    fun loadDoctorDetails(doctorId: String) {
//        viewModelScope.launch {
//            val docRef = firestore.collection("doctors").document(doctorId)
//            docRef.get().addOnSuccessListener { snapshot ->
//                _doctorName.value = snapshot.getString("name") ?: "Unknown"
//                _specialization.value = snapshot.getString("specialization") ?: ""
//                _experience.value = snapshot.getString("experience") ?: ""
//                _qualification.value = snapshot.getString("qualification") ?: ""
//                _clinicName.value = snapshot.getString("clinicName") ?: ""
//                _profileImageUrl.value = snapshot.getString("profileImageUrl") ?: ""
//                _place.value = snapshot.getString("place") ?: ""
//            }
//
//            val slotsRef = firestore.collection("doctors")
//                .document(doctorId)
//                .collection("slots")
//
//            slotsRef.addSnapshotListener { snapshot, _ ->
//                val newSlots = mutableMapOf<String, Boolean>()
//                snapshot?.documents?.forEach {
//                    val slotName = it.id
//                    val booked = it.getBoolean("booked") ?: false
//                    newSlots[slotName] = booked
//                }
//                _slots.value = newSlots
//            }
//        }
//    }
//
//    fun bookSlot(
//        doctorId: String,
//        slot: String,
//        patientId: String,
//        onSuccess: () -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        _isBooking.value = true
//        val slotRef = firestore.collection("doctors")
//            .document(doctorId)
//            .collection("slots")
//            .document(slot)
//
//        firestore.runTransaction { transaction ->
//            val snapshot = transaction.get(slotRef)
//            val isBooked = snapshot.getBoolean("booked") ?: false
//            if (isBooked) {
//                throw Exception("Slot already booked.")
//            }
//
//            // Mark slot as booked
//            transaction.set(
//                slotRef,
//                mapOf("booked" to true, "bookedBy" to patientId),
//                SetOptions.merge()
//            )
//
//            // Optional: add to a global 'appointments' collection
//            val globalBooking = mapOf(
//                "doctorId" to doctorId,
//                "slot" to slot,
//                "patientId" to patientId,
//                "timestamp" to System.currentTimeMillis()
//            )
//            firestore.collection("appointments").add(globalBooking)
//
//        }.addOnSuccessListener {
//            _isBooking.value = false
//            onSuccess()
//        }.addOnFailureListener {
//            _isBooking.value = false
//            onFailure(it.message ?: "Booking failed")
//        }
//    }
//}



//package com.sharathkolpe.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.*
//
//class DoctorDetailsViewModel : ViewModel() {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    private val _doctorName = MutableStateFlow("")
//    val doctorName: StateFlow<String> = _doctorName
//
//    private val _specialization = MutableStateFlow("")
//    val specialization: StateFlow<String> = _specialization
//
//    private val _experience = MutableStateFlow("")
//    val experience: StateFlow<String> = _experience
//
//    private val _qualification = MutableStateFlow("")
//    val qualification: StateFlow<String> = _qualification
//
//    private val _clinicName = MutableStateFlow("")
//    val clinicName: StateFlow<String> = _clinicName
//
//    private val _place = MutableStateFlow("")
//    val place: StateFlow<String> = _place
//
//    private val _profileImageUrl = MutableStateFlow("")
//    val profileImageUrl: StateFlow<String> = _profileImageUrl
//
//    private val _availability = MutableStateFlow<Map<String, Pair<String, String>>>(emptyMap())
//    val availability: StateFlow<Map<String, Pair<String, String>>> = _availability
//
//    private val _currentDayBookings = MutableStateFlow<Map<String, Int>>(emptyMap())
//    val currentDayBookings: StateFlow<Map<String, Int>> = _currentDayBookings




//    private fun getCurrentDayKey(): String {
//        val calendar = Calendar.getInstance()
//        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())!!
//            .lowercase(Locale.getDefault())
//    }

//    private fun getCurrentDayKey(): String {
//        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
//        return sdf.format(Date()) // e.g., "Monday"
//    }
//
//
//    private fun getCurrentDateId(): String {
//        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//        return formatter.format(Date())
//    }
//
//    fun loadDoctorDetails(doctorId: String) {
//        viewModelScope.launch {
//            val docRef = db.collection("doctors").document(doctorId)
//            docRef.get().addOnSuccessListener { document ->
//                _doctorName.value = document.getString("name") ?: ""
//                _specialization.value = document.getString("specialization") ?: ""
//                _experience.value = document.getString("experience") ?: ""
//                _qualification.value = document.getString("qualification") ?: ""
//                _clinicName.value = document.getString("clinicName") ?: ""
//                _place.value = document.getString("place") ?: ""
//                _profileImageUrl.value = document.getString("profileImageUrl") ?: ""
//
//
//                val today = getCurrentDayKey()
//                docRef.collection("availability").document(today).get().addOnSuccessListener {
//                    val morning = it.getString("morning") ?: ""
//                    val afternoon = it.getString("afternoon") ?: ""
//
//                    val availabilityMap = mutableMapOf<String, Pair<String, String>>()
//                    if (morning.contains("-")) {
//                        val (start, end) = morning.split("-")
//                        availabilityMap["morning"] = Pair(start, end)
//                    }
//                    if (afternoon.contains("-")) {
//                        val (start, end) = afternoon.split("-")
//                        availabilityMap["afternoon"] = Pair(start, end)
//                    }
//                    _availability.value = availabilityMap
//                }
//
//                loadBookingsCount(doctorId)
//            }
//        }
//    }
//
//    private fun loadBookingsCount(doctorId: String) {
//        val todayId = getCurrentDateId()
//        val bookingsRef = db.collection("bookings").document("${doctorId}_$todayId").collection("tokens")
//        bookingsRef.get().addOnSuccessListener { snapshot ->
//            val countMap = mutableMapOf<String, Int>()
//            for (doc in snapshot) {
//                val slot = doc.getString("slot") ?: continue
//                countMap[slot] = countMap.getOrDefault(slot, 0) + 1
//            }
//            _currentDayBookings.value = countMap
//        }
//    }
//
//    fun bookSlot(
//        doctorId: String,
//        patientId: String,
//        slot: String,
//        onSuccess: (Int, String) -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        val todayId = getCurrentDateId()
//        val timestamp = Timestamp.now()
//        val bookingsRef = db.collection("bookings")
//            .document("${doctorId}_$todayId")
//            .collection("tokens")
//
//        bookingsRef.get().addOnSuccessListener { snapshot ->
//            val tokenNumber = snapshot.filter { it.getString("slot") == slot }.size + 1
//            val tokenData = mapOf(
//                "doctorId" to doctorId,
//                "patientId" to patientId,
//                "slot" to slot,
//                "tokenNumber" to tokenNumber,
//                "timestamp" to timestamp
//            )
//
//            bookingsRef.add(tokenData).addOnSuccessListener {
//                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                onSuccess(tokenNumber, sdf.format(Date()))
//            }.addOnFailureListener {
//                onFailure("Failed to save booking: ${it.message}")
//            }
//        }.addOnFailureListener {
//            onFailure("Error loading bookings: ${it.message}")
//        }
//    }
//}






package com.sharathkolpe.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class DoctorDetailsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _doctorName = MutableStateFlow("")
    val doctorName: StateFlow<String> = _doctorName

    private val _specialization = MutableStateFlow("")
    val specialization: StateFlow<String> = _specialization

    private val _experience = MutableStateFlow("")
    val experience: StateFlow<String> = _experience

    private val _qualification = MutableStateFlow("")
    val qualification: StateFlow<String> = _qualification

    private val _clinicName = MutableStateFlow("")
    val clinicName: StateFlow<String> = _clinicName

    private val _place = MutableStateFlow("")
    val place: StateFlow<String> = _place

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _availability = MutableStateFlow<Map<String, Pair<String, String>>>(emptyMap())
    val availability: StateFlow<Map<String, Pair<String, String>>> = _availability

    private val _currentDayBookings = MutableStateFlow<Map<String, Int>>(emptyMap())
    val currentDayBookings: StateFlow<Map<String, Int>> = _currentDayBookings

    private fun getCurrentDayKey(): String {
        val calendar = Calendar.getInstance()
        val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

    private fun getCurrentDateId(): String {
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(Date())
    }

    fun loadDoctorDetails(doctorId: String) {
        val docRef = db.collection("doctors").document(doctorId)

        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                _doctorName.value = document.getString("name") ?: ""
                _specialization.value = document.getString("specialization") ?: ""
                _experience.value = document.getString("experience") ?: ""
                _qualification.value = document.getString("qualification") ?: ""
                _clinicName.value = document.getString("clinicName") ?: ""
                _place.value = document.getString("place") ?: ""
                _profileImageUrl.value = document.getString("profileImageUrl") ?: ""

                // ✅ Fetch today's availability from inline map
                val today = getCurrentDayKey()
                val availabilityMap = mutableMapOf<String, Pair<String, String>>()

                val todayAvailability = document.get("availability") as? Map<*, *>
                val todaySlotMap = todayAvailability?.get(today) as? Map<*, *>

                val morning = todaySlotMap?.get("morning") as? String ?: "Not Set"
                val afternoon = todaySlotMap?.get("afternoon") as? String ?: "Not Set"

                if (morning != "Not Set" && morning.contains(" - ")) {
                    val times = morning.split(" - ")
                    if (times.size == 2) {
                        availabilityMap["morning"] = Pair(times[0].trim(), times[1].trim())
                    }
                }

                if (afternoon != "Not Set" && afternoon.contains(" - ")) {
                    val times = afternoon.split(" - ")
                    if (times.size == 2) {
                        availabilityMap["afternoon"] = Pair(times[0].trim(), times[1].trim())
                    }
                }

                _availability.value = availabilityMap

                loadBookingsCount(doctorId)
            }
        }
    }

    private fun loadBookingsCount(doctorId: String) {
        val todayId = getCurrentDateId()
        val bookingsRef = db.collection("bookings")
            .document("${doctorId}_$todayId")
            .collection("tokens")

        bookingsRef.get().addOnSuccessListener { snapshot ->
            val countMap = mutableMapOf<String, Int>()
            for (doc in snapshot) {
                val slot = doc.getString("slot") ?: continue
                countMap[slot] = countMap.getOrDefault(slot, 0) + 1
            }
            _currentDayBookings.value = countMap
        }
    }

    fun bookSlot(
        doctorId: String,
        patientId: String,
        slot: String,
        onSuccess: (tokenNumber: Int, formattedTime: String) -> Unit,
        onFailure: (errorMessage: String) -> Unit
    ) {
        val todayId = getCurrentDateId()
        val timestamp = Timestamp.now()

        val bookingsRef = db.collection("bookings")
            .document("${doctorId}_$todayId")
            .collection("tokens")

        bookingsRef.get().addOnSuccessListener { snapshot ->
            val tokenNumber = snapshot.filter { it.getString("slot") == slot }.size + 1

            val tokenData = mapOf(
                "doctorId" to doctorId,
                "patientId" to patientId,
                "slot" to slot,
                "tokenNumber" to tokenNumber,
                "timestamp" to timestamp
            )

            bookingsRef.add(tokenData).addOnSuccessListener {
                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                val formattedTime = sdf.format(Date())
                onSuccess(tokenNumber, formattedTime)
            }.addOnFailureListener {
                onFailure("Failed to save booking: ${it.message}")
            }

        }.addOnFailureListener {
            onFailure("Error loading bookings: ${it.message}")
        }
    }
}
