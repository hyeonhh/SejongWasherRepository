package com.example.sejong2washertimer.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sejong2washertimer.model.Washer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class WasherViewModel() : ViewModel(
) {
    private val _washerStates = mutableStateMapOf<String,Boolean>()
    private val _washerFinishTime = mutableStateMapOf<String,String>()

    fun getWasherState(washerId:String): Boolean {
        return _washerStates[washerId] ?: true
    }

    fun setWasherState(washerId: String,isAvailable:Boolean) {
        _washerStates[washerId] = isAvailable
    }



    fun getWasherFinishTime(washerId: String): String? {
        Timber.tag("끝나는 시간").d(washerId + "의 완료 시간 " + _washerFinishTime[washerId])
        return _washerFinishTime[washerId]
    }

    fun setWasherFinishTime(washerId: String,finishTime:String) {
        _washerFinishTime[washerId] = finishTime
    }


    fun resetWasherData(washerId: String) {


    }
}
