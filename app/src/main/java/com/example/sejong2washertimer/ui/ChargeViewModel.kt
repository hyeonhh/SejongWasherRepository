package com.example.sejong2washertimer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ChargeViewModel:ViewModel() {


    var chargedMoney by mutableIntStateOf(0)
        private set

    fun updateChargedMoney(input: Int) {
        chargedMoney += input
    }


}