package com.example.sejong2washertimer.data

data class Weather(
    val response: Response
) {
    data class Response(
        val header: Header,
        val body: Body
    ) {
        data class Header(
            val resultCode: Int,
            val resultMessage: String,
        )

        data class Body(
            val dataType: String,
            val items: Items
        )

        data class Items(
            val item: List<Item>
        )

        data class Item(
            val baseDate: Int,
            val baseTime: Int,
            val category: String,
            val fsctDate: Int,
            val fcstTime: Int,
            val fcstValue: String,
            val nx: Int,
            val ny: Int
        )


    }
}