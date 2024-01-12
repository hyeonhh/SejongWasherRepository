package com.example.sejong2washertimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme

class SettingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sejong2WasherTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingApp()
                }
            }
        }
    }
}

@Composable
fun SettingApp(modifier: Modifier=Modifier) {
    var washerChecked by remember { mutableStateOf(true) }
    var dryerChecked by remember {
        mutableStateOf(true)
    }
    Column(
        modifier=modifier
            .padding(10.dp)
    ) {


        Text(text="성현님",
            fontSize = 15.sp)
        Text(text="깨끗하게 빨래중이에요!",
            fontSize = 15.sp)
        Text(
            text = "알림 설정",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = "세탁 알림 수신",
            fontSize = 12.sp)
        Text(text = "세탁이 완료되면 아래와 같이 팝업을 통해 알려드려요!",
            fontSize=10.sp,
            color = Color.Gray
            )


        Card(
            modifier =modifier
                .padding(10.dp)
        ) {
            Row(
                modifier=modifier
                    .padding(5.dp)
            ) {
                Column {
                    Text(text = "\uD83D\uDCA7세탁이 완료되었어요!",
                        fontSize=12.sp)
                    Text(text = "지금 찾으러 와주세요!",
                        fontSize=10.sp)
                }
                Spacer(modifier = modifier.width(40.dp))
                Switch(
                    modifier = modifier
                        .align(Alignment.CenterVertically),
                    checked = washerChecked, onCheckedChange = { washerChecked = it }

                )
            }
        }

        Card(
            modifier =modifier
                .padding(10.dp)
        ) {
            Row(
                modifier=modifier
                    .padding(5.dp)
            ) {
                Column {
                    Text(text = "\uD83D\uDD25건조가 완료되었어요!",
                        fontSize=12.sp)
                    Text(text = "지금 찾으러 와주세요!",
                        fontSize=10.sp)
                }
                Spacer(modifier = modifier.width(40.dp))
                Switch(
                    modifier = modifier
                        .align(Alignment.CenterVertically),
                    checked = dryerChecked, onCheckedChange = { dryerChecked = it }

                )
            }
        }
    }


}

@Preview
@Composable
fun PreviewSetting() {
    SettingApp()
}