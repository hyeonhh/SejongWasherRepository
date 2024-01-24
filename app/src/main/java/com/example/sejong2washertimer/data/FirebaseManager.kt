package com.example.sejong2washertimer.data

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class FirebaseManager(washerId: String? ) {

    private val myRef: DatabaseReference


    init {
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase.getReference("washer${washerId}startTime")

    }

    fun getWasherStartTime(callback: (String) -> Unit, errorCallback: (DatabaseError) -> Unit) {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val washerStartTime = snapshot.getValue(String::class.java)
                if (washerStartTime != null) {
                    callback.invoke(washerStartTime)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallback.invoke(error)
            }
        })
    }

    fun saveCurrentTimeDatabase(formattedTime: String) {
        myRef.setValue(formattedTime)
    }
}



