package com.example.sejong2washertimer.viewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import timber.log.Timber

class DryerViewModel: ViewModel() {

    private val _dryerStates = mutableStateMapOf<String,Boolean>()
    private val _dryerFinishTime = mutableStateMapOf<String,String>()

    fun getDryerState(dryerId:String): Boolean {
        return _dryerStates[dryerId] ?: true
    }

    fun setDryerState(dryerId: String,isAvailable:Boolean) {
        _dryerStates[dryerId] = isAvailable
    }



    fun getDryerFinishTime(dryerId: String): String? {
        Timber.tag("끝나는 시간").d(dryerId + "의 완료 시간 " + _dryerFinishTime[dryerId])
        return _dryerFinishTime[dryerId]
    }

    fun setDryerFinishTime(dryerId: String,finishTime:String) {
        _dryerFinishTime[dryerId] = finishTime
    }



}

