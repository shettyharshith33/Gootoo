package com.sharathkolpe.firebaseRealTimeDB.repository

import androidx.compose.runtime.key
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sharathkolpe.firebaseRealTimeDB.RealTimeModelResponse
import com.sharathkolpe.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealTimeDbRepository @Inject constructor(
    private val db:DatabaseReference
):RealTimeRepository {
    override fun insert(item: RealTimeModelResponse.RealTimeItems): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.push().setValue(
            item).addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Data Inserted Successfully :)"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun getItems(): Flow<ResultState<List<RealTimeModelResponse>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               val items = snapshot.children.map {
                   RealTimeModelResponse(
                       it.getValue(RealTimeModelResponse.RealTimeItems::class.java),
                       key =it.key
                   )
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.addValueEventListener(valueEvent)
        awaitClose{
            db.removeEventListener(valueEvent)
            close()
        }

    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.child(key).removeValue()
            .addOnCompleteListener {
                trySend(ResultState.Success("Item Deleted"))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
    }

    override fun update(res: RealTimeModelResponse): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val map = HashMap<String,Any>()
        map["title"] = res.item?.title!!
        map["description"] = res.item.description!!

        db.child(res.key!!).updateChildren(
            map
        ).addOnCompleteListener{
            trySend(ResultState.Success("Updated Successfully"))
        }.addOnFailureListener {
            ResultState.Failure(it)
        }
        awaitClose {
            close()
        }
    }
}