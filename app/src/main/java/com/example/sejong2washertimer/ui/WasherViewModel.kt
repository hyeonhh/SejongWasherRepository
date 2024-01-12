package com.example.sejong2washertimer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WasherViewModel : ViewModel() {
    val washerStates : MutableLiveData<Map<String,Boolean>> = MutableLiveData()


}