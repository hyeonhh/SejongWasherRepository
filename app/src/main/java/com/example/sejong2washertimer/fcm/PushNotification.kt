package com.example.sejong2washertimer.fcm

class PushNotification(
    val data : NotiModel,
    //token -> to로 변수명 변경하니 제대로 작동함!
    val to: String="fCZL0u52SXO4g11jmKxZSC:APA91bHgMx6dq_qs5LE_XSmSnksIwnb_y8tryEScy-So8c0bH6-l0IRK5eN_ChAeTiRAnqI9g-46_9vtXlcNYDGNFEs6LYeKwA63lJLTMGktVe5Dj9JrulFGgp9B1X-Lz32rzr78-7Gd"
)