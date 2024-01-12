package com.example.sejong2washertimer.fcm

class PushNotification(
    val data : NotiModel,
    //token -> to로 변수명 변경하니 제대로 작동함!
    val to: String="edd6OyJlQrilexZ3Bv4X50:APA91bF12JAHghG2AwLwNSovULkEyibpx8VSjX5SmJUkavOM94zka2j7UkOnYL672D_zWF3ta5CkqDi12_LOnPKtwaNpuKIH4dpp9j1IXgyLWUYZY1GPaXtbnqUHUQ2GYnUH2XTvHHcf"
)