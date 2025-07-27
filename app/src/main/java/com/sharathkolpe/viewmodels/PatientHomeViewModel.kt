package com.sharathkolpe.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.sharathkolpe.afterLoginScreens.Doctor

class PatientHomeViewModel : ViewModel() {

    private val _doctorList = MutableStateFlow<List<Doctor>>(emptyList())
    val doctorList: StateFlow<List<Doctor>> = _doctorList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchDoctorsFromFirestore() {
        _isLoading.value = true

        FirebaseFirestore.getInstance()
            .collection("doctors")
            .get()
            .addOnSuccessListener { result ->
                val doctors = result.documents.mapNotNull { doc ->
                    doc.toObject(Doctor::class.java)
                }
                _doctorList.value = doctors
                _isLoading.value = false
            }
            .addOnFailureListener {
                _doctorList.value = emptyList()
                _isLoading.value = false
            }
    }
}
