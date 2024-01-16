package com.example.sejong2washertimer.data

data class WasherUiState(
    val isAvailable : Boolean= false,

)
val WasherUiState.canUserWasher :Boolean
    get() = isAvailable
