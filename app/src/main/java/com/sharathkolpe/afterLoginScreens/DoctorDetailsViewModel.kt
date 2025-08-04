package com.sharathkolpe.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.text.get
import kotlin.time.Duration.Companion.seconds

class DoctorDetailsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val closedSessions = mutableStateMapOf<String, Boolean>()

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

    private val _bookingTimers = mutableStateMapOf<String, String>()
    val bookingTimers: Map<String, String> = _bookingTimers

    private fun getCurrentDayKey(): String {
        val calendar = Calendar.getInstance()
        val days =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
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
                startBookingTimers()
            }
        }
    }

    private fun loadBookingsCount(doctorId: String) {
        val todayId = getCurrentDateId()

        val bookingsRef = db.collection("appointments")
            .document(doctorId)
            .collection("tokens")

        bookingsRef.get().addOnSuccessListener { snapshot ->
            val countMap = mutableMapOf<String, Int>()

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val tomorrow = Calendar.getInstance().apply {
                add(Calendar.DATE, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            for (doc in snapshot) {
                val slot = doc.getString("slot") ?: continue
                val timestamp = doc.getTimestamp("timestamp")?.toDate() ?: continue

                if (timestamp >= today.time && timestamp < tomorrow.time) {
                    countMap[slot] = countMap.getOrDefault(slot, 0) + 1
                }
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
        val timestamp = Timestamp.now()

        val bookingsRef = db.collection("appointments")
            .document(doctorId)
            .collection("tokens")

        bookingsRef.get().addOnSuccessListener { snapshot ->
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val tomorrow = Calendar.getInstance().apply {
                add(Calendar.DATE, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val todaySlotTokens = snapshot.filter {
                val slotMatch = it.getString("slot") == slot
                val timestamp = it.getTimestamp("timestamp")?.toDate()
                slotMatch && timestamp != null && timestamp >= today.time && timestamp < tomorrow.time
            }

            val tokenNumber = todaySlotTokens.size + 1


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

    fun isSlotBookable(slot: String): Boolean {
        val sessionTime = availability.value[slot] ?: return false
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val now = Calendar.getInstance()

        val startTime = Calendar.getInstance().apply {
            time = formatter.parse(sessionTime.first) ?: return false
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endTime = Calendar.getInstance().apply {
            time = formatter.parse(sessionTime.second) ?: return false
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val bookingOpenTime = (startTime.clone() as Calendar).apply {
            add(Calendar.HOUR_OF_DAY, -1)
        }

        return now.time.after(bookingOpenTime.time) && now.time.before(endTime.time)
    }

    private fun startBookingTimers() {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

        availability.value.forEach { (slot, sessionTime) ->
            val now = Calendar.getInstance()
            val openTime = Calendar.getInstance().apply {
                time = formatter.parse(sessionTime.first) ?: return@forEach
                add(Calendar.HOUR_OF_DAY, -1)
                set(Calendar.YEAR, now.get(Calendar.YEAR))
                set(Calendar.MONTH, now.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // If already open, skip timer
            if (now.time >= openTime.time) {
                _bookingTimers[slot] = ""
                return@forEach
            }

            // Start timer to count down
            fixedRateTimer(
                name = "booking_timer_$slot",
                initialDelay = 0,
                period = 1000
            ) {
                val diffMillis = openTime.timeInMillis - System.currentTimeMillis()
                if (diffMillis <= 0) {
                    _bookingTimers[slot] = ""
                    cancel()
                } else {
                    val hours = diffMillis / (1000 * 60 * 60)
                    val minutes = (diffMillis / (1000 * 60)) % 60
                    val seconds = (diffMillis / 1000) % 60

                    _bookingTimers[slot] = "Booking opens in ${hours}h ${minutes}m ${seconds}s"
                }
            }
        }
    }


    fun startAutoRefresh(doctorId: String) {
        viewModelScope.launch {
            while (true) {
                loadBookingsCount(doctorId)
                kotlinx.coroutines.delay(5000) // Refresh every 5 seconds
            }
        }
        loadBookingsCount(doctorId)
        startBookingTimers()
        startAutoRefresh(doctorId)
    }
}
