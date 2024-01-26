package com.example.sejong2washertimer.data

import android.content.ClipData.Item
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

data class Weather(
    val response: Response
){
    data class Response(
        val header: Header,
        val body: Body
    )
    data class Header(
        val resultCode:Int,
        val resultMessage:String
    )
    data class Body(
        val dataType:String,
        val items:Items
    )
    data class Items(
        val item:List<Item>
    )

    data class Item(
        val baseData: Int,
        val baseTime: Int,
        val category: String,
        val fcstDate : Int,
        val fcstTime : Int,
        val fcstValue : String,
        val nx : Int,
        val ny : Int
    )


}
