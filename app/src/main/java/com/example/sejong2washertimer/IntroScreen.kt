package com.example.sejong2washertimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sejong2washertimer.data.Datasource
import com.example.sejong2washertimer.ui.WasherList
import com.example.sejong2washertimer.ui.theme.ui.theme.Sejong2WasherTimerTheme

class IntroScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sejong2WasherTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Loader()
                }
            }
        }
    }
}

@Composable
fun Loader() {
    val navController= rememberNavController()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_timer))
    NavHost(
        navController = navController,
        startDestination = RoutingScreen.Intro.name,
    ) {


        composable(RoutingScreen.Washer.name) {
            WasherList(
                washerList = Datasource().washers
            )
        }


    }
    Column(
        verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition = composition,
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {

                navController.navigate(RoutingScreen.Washer.name)


            },
        ) {
            Text(text = "세탁하러가기")
        }
    }

    
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Sejong2WasherTimerTheme {
        Loader()
    }
}