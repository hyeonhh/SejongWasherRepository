package com.example.sejong2washertimer.data

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


// Datestore 만들기
const val PREFERENCES_NAME = "user_preferences"
val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)


class ChargeViewModel(application: Application):AndroidViewModel(application) {



    private val dataStore = application.dataStore
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


    fun updateChargedMoney(input: Int) {
        viewModelScope.launch {
                dataStore.edit {
                    preferences ->
                    val currentChargedMoney = preferences[PreferencesKeys.CHARGED_MONEY] ?:0
                    val newChargedMoney = currentChargedMoney + input
                    preferences[PreferencesKeys.CHARGED_MONEY] = newChargedMoney

                }

        }
    }


}