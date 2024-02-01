package com.example.sejong2washertimer.viewModel

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejong2washertimer.R
import com.example.sejong2washertimer.api.WeatherRepository
import com.example.sejong2washertimer.data.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel  @Inject constructor(

    private val repository : WeatherRepository):ViewModel() {
    private val _weatherResponse: MutableLiveData<Response<Weather>> = MutableLiveData()
    val weatherResponse : LiveData<Response<Weather>> get() = _weatherResponse
    private val _temperature : MutableState<String> = mutableStateOf("")

    val temperature :MutableState<String> get() = _temperature

    var rainStateText = ""


    @DrawableRes
    var weatherImageResource = R.drawable.sunny


    fun makeWeatherRequest(
        dataType: String, numOfRows: Int, pageNo: Int,
        baseDate: Int, baseTime: Int, nx: String, ny: String
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny)
                _weatherResponse.value = response

                updateTemperature(response.body()?.response?.body?.items?.item)


            }
            catch (e:Exception){
                Log.d("날씨", e.toString())

            }

        }

    }


    private fun updateTemperature(items: List<Weather.Response.Item>?) {
        if ((items != null) && items.isNotEmpty()) {

                    val filteredItems = items.filter {
                        it.category in listOf("TMP")
                    }

                    for (i in filteredItems) {
                        Log.d("날씨", "${i}")

                        if (i.category == "TMP") {
                            val temperature = i.fcstValue + "°C"
                            Log.d("날씨뷰모델", temperature)
                            //todo : 뷰 모델에서 temperature 업데이트

                            _temperature.value = temperature

                        }
                        //강수 처리
                        if (i.category == "PTY") {
                            if (i.fcstValue == "1" || i.fcstValue == "4") {
                                rainStateText = "비"
                                weatherImageResource = R.drawable.rainy
                            }
                            if (i.fcstValue == "2") {
                                rainStateText = "비/눈"
                                weatherImageResource = R.drawable.rainy_snow
                            }
                            if (i.fcstValue == "3") {
                                rainStateText = "눈"
                                weatherImageResource = R.drawable.snowy
                            }


                        }

                    }


                }




        }

    }






