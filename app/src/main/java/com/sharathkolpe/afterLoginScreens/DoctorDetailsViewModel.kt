package com.sharathkolpe.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorDetailsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

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

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl


    private val _place = MutableStateFlow("")
    val place: StateFlow<String> = _place


    private val _slots = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val slots: StateFlow<Map<String, Boolean>> = _slots

    private val _isBooking = MutableStateFlow(false)
    val isBooking: StateFlow<Boolean> = _isBooking

    fun loadDoctorDetails(doctorId: String) {
        viewModelScope.launch {
            val docRef = firestore.collection("doctors").document(doctorId)
            docRef.get().addOnSuccessListener { snapshot ->
                _doctorName.value = snapshot.getString("name") ?: "Unknown"
                _specialization.value = snapshot.getString("specialization") ?: ""
                _experience.value = snapshot.getString("experience") ?: ""
                _qualification.value = snapshot.getString("qualification") ?: ""
                _clinicName.value = snapshot.getString("clinicName") ?: ""
                _profileImageUrl.value = snapshot.getString("profileImageUrl") ?: ""
                _place.value = snapshot.getString("place") ?: ""
            }

            val slotsRef = firestore.collection("doctors")
                .document(doctorId)
                .collection("slots")

            slotsRef.addSnapshotListener { snapshot, _ ->
                val newSlots = mutableMapOf<String, Boolean>()
                snapshot?.documents?.forEach {
                    val slotName = it.id
                    val booked = it.getBoolean("booked") ?: false
                    newSlots[slotName] = booked
                }
                _slots.value = newSlots
            }
        }
    }

    fun bookSlot(
        doctorId: String,
        slot: String,
        patientId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        _isBooking.value = true
        val slotRef = firestore.collection("doctors")
            .document(doctorId)
            .collection("slots")
            .document(slot)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(slotRef)
            val isBooked = snapshot.getBoolean("booked") ?: false
            if (isBooked) {
                throw Exception("Slot already booked.")
            }

            // Mark slot as booked
            transaction.set(
                slotRef,
                mapOf("booked" to true, "bookedBy" to patientId),
                SetOptions.merge()
            )

            // Optional: add to a global 'appointments' collection
            val globalBooking = mapOf(
                "doctorId" to doctorId,
                "slot" to slot,
                "patientId" to patientId,
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("appointments").add(globalBooking)

        }.addOnSuccessListener {
            _isBooking.value = false
            onSuccess()
        }.addOnFailureListener {
            _isBooking.value = false
            onFailure(it.message ?: "Booking failed")
        }
    }
}
