package com.example.sejong2washertimer.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


// Datestore 만들기
private const val PREFERENCES_NAME = "user_preferences"
val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class ChargeViewModel(context:Context):ViewModel() {


     val dataStore = context.dataStore
    private object PreferencesKeys {
        val CHARGED_MONEY = intPreferencesKey("charged_money")
    }

    val chargedMoneyFlow: Flow<Int> = dataStore.data
        .catch {
            exception -> if (exception is IOException) {
                emit(emptyPreferences())
        } else{
            throw exception
        }
        }
        .map { preferences ->
            preferences[PreferencesKeys.CHARGED_MONEY] ?: 0 }


    private suspend fun saveChargedMoney(value:Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHARGED_MONEY] = value  }

    }

    var chargedMoney by  mutableIntStateOf(0)
        private set

    fun updateChargedMoney(input: Int) {
        viewModelScope.launch {
            chargedMoney += input
            saveChargedMoney(chargedMoney)
        }
    }


}