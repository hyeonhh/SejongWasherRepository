package com.example.sejong2washertimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sejong2washertimer.data.Datasource
import com.example.sejong2washertimer.ui.CardChargeApp
import com.example.sejong2washertimer.ui.CardChargeScreen
import com.example.sejong2washertimer.ui.ChargeViewModel
import com.example.sejong2washertimer.ui.DryerApp
import com.example.sejong2washertimer.ui.MoneyLeft
import com.example.sejong2washertimer.ui.MoneyUsageList
import com.example.sejong2washertimer.ui.SettingApp
import com.example.sejong2washertimer.ui.SettingScreen
import com.example.sejong2washertimer.ui.WasherApp
import com.example.sejong2washertimer.ui.WasherList
import com.example.sejong2washertimer.ui.WasherViewModel
import com.example.sejong2washertimer.ui.theme.Sejong2WasherTimerTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import org.checkerframework.common.subtyping.qual.Bottom
import timber.log.Timber


enum class RoutingScreen() {
    Setting,
    Washer,
    Dryer,
    Charge
}

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var databaseReference : DatabaseReference
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("timer")


        //todo : 추후 스플래시 화면에서 token 받도록 로직 이동 필요

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.tag(TAG).w(task.exception, "Fetching FCM registration token failed")
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log
           Log.d("token보기",token)
         }


        @Composable
        fun Navigation() {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = RoutingScreen.Washer.name){
                composable(RoutingScreen.Washer.name) {
                    WasherApp()
                }
                composable(RoutingScreen.Dryer.name) {
                    DryerApp()
                }
                composable(RoutingScreen.Setting.name) {
                    SettingApp()
                }

                composable(RoutingScreen.Charge.name){
                    CardChargeApp()
                }
            }

        }


        setContent {
            val selectedItem by remember { mutableStateOf(0)}
            Sejong2WasherTimerTheme {


                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),

                            title = { Text(
                                text = "세탁실",
                            ) },

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
                                        selected = selectedItem==0,
                                        onClick = {
                                            navController.navigate(RoutingScreen.Washer.name)
                                        },
                                        icon = {
                                            Image(painter = painterResource(id = R.drawable.baseline_local_laundry_service_24), contentDescription ="washer" )
                                        })

                                    BottomNavigationItem(
                                        selected = selectedItem==1,
                                        onClick = {
                                            navController.navigate(RoutingScreen.Dryer.name)

                                        },
                                        icon = {
                                            Image(painter = painterResource(id = R.drawable.baseline_dry_cleaning_24), contentDescription ="washer" )
                                        }

                                    )

                                    BottomNavigationItem(
                                        selected = selectedItem==2,
                                        onClick = {
                                                  navController.navigate(RoutingScreen.Charge.name)
                                        },
                                        icon = {
                                            Image(painter = painterResource(id = R.drawable.baseline_monetization_on_24), contentDescription ="washer" )
                                        }
                                       )

                                    BottomNavigationItem(
                                        selected = selectedItem==3,
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
                            modifier=Modifier
                                .fillMaxSize(),

                        contentAlignment = Alignment.Center) {
                            Navigation()

                        }
                    }
                )


            }
        }

    }


}



