package com.example.sejong2washertimer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.*
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sejong2washertimer.viewModel.ChargeViewModel
import com.example.sejong2washertimer.ui.CardChargeApp
import com.example.sejong2washertimer.ui.DryerApp
import com.example.sejong2washertimer.ui.SettingApp
import com.example.sejong2washertimer.ui.WasherApp
import com.example.sejong2washertimer.ui.theme.Sejong2WasherTimerTheme
import com.example.sejong2washertimer.viewModel.WeatherViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber




enum class RoutingScreen() {
    Setting,
    Washer,
    Dryer,
    Charge
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val chargeViewModel by viewModels<ChargeViewModel>()
    private lateinit var navController: NavHostController
    private lateinit var databaseReference: DatabaseReference
    private val viewModel by viewModels<WeatherViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("timer")


        viewModel.getWeather("JSON",14,1,20240126,1100,"98" ,"75")
        //날씨
        viewModel.weatherResponse.observe(this) {
            response ->
            response.body()?.response?.body?.items?.item?.let{
                items ->
                val filteredItems = items.filter {
                    it.category in listOf("T1H","SKY","RN1","REH","PTY")
                }
                for (i in filteredItems) {
                    Log.d("날씨","$i")
                    if(i.category =="SKY") {
                        if(i.fcstValue=="1"){
                            Log.d("날씨","맑음")
                        }
                        if(i.fcstValue=="3"){
                            Log.d("날씨","구름많음")
                        }
                        if(i.fcstValue=="4"){
                            Log.d("날씨","흐림")
                        }

                    }
            }
            }

            }

        //todo : 추후 스플래시 화면에서 token 받도록 로직 이동 필요

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.tag(TAG).w(task.exception, "Fetching FCM registration token failed")
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log
            Log.d("token보기", token)
        }


        @Composable
        fun Navigation() {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = RoutingScreen.Washer.name
            ) {
                composable(RoutingScreen.Washer.name) {
                    WasherApp()
                }
                composable(RoutingScreen.Dryer.name) {
                    DryerApp()
                }
                composable(RoutingScreen.Setting.name) {
                    SettingApp()
                }

                composable(RoutingScreen.Charge.name) {
                    CardChargeApp(chargeViewModel)
                }
            }

        }


        setContent {
            val selectedItem by remember { mutableStateOf(0) }
            Sejong2WasherTimerTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),

                            title = {
                                Text(
                                    text = "세탁실",
                                )
                            },

                            actions = {
                                IconButton(
                                    onClick = { /*TODO*/ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = "알림"
                                    )

                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigation(
                            backgroundColor = Color.White
                        ) {
                            BottomNavigationItem(
                                selected = selectedItem == 0,
                                onClick = {
                                    navController.navigate(RoutingScreen.Washer.name)
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_local_laundry_service_24),
                                        contentDescription = "washer"
                                    )
                                })

                            BottomNavigationItem(
                                selected = selectedItem == 1,
                                onClick = {
                                    navController.navigate(RoutingScreen.Dryer.name)

                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_dry_cleaning_24),
                                        contentDescription = "washer"
                                    )
                                }

                            )

                            BottomNavigationItem(
                                selected = selectedItem == 2,
                                onClick = {
                                    navController.navigate(RoutingScreen.Charge.name)
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_monetization_on_24),
                                        contentDescription = "washer"
                                    )
                                }
                            )

                            BottomNavigationItem(
                                selected = selectedItem == 3,
                                onClick = {
                                    navController.navigate(RoutingScreen.Setting.name)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "설정"
                                    )

                                })
                        }
                    },
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),

                            contentAlignment = Alignment.Center
                        ) {
                            Navigation()

                        }
                    }
                )


            }
        }

    }



}