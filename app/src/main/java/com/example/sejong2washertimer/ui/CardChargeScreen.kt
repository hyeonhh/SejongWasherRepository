package com.example.sejong2washertimer.ui

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sejong2washertimer.R
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme
import java.time.format.TextStyle

class CardChargeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sejong2WasherTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column {
                        MoneyLeft()
                        MoneyInfo()
                        MoneyUsageList(usageContent = "세탁","-1300월",R.drawable.baseline_local_laundry_service_24)
                        MoneyUsageList(usageContent = "건조","-1300월",R.drawable.baseline_dry_cleaning_24)
                        MoneyUsageList(usageContent = "충전","+5000원",R.drawable.baseline_monetization_on_24)
                    }


                }
            }
        }
    }
}

@Composable
fun MoneyLeft(
    modifier: Modifier=Modifier
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment=Alignment.CenterVertically,
            modifier = modifier.padding(10.dp)
    ) {
        Text("남은 금액 : ")
        Text("10000원")
        Button(
            onClick =
            {
                    // todo : dialog를 통해 남은 금액 업데이트 로직 구현
                    // todo : 충전 이력에 추가
            }) {
            Text(text = "채우기")
        }
    }
}

@Composable
fun MoneyInfo(modifier: Modifier=Modifier) {
    Box {
        Text("앞으로 세탁을 3번 더 할 수 있어요",
            fontSize = 10.sp)
    }
    Text("앞으로 세탁 & 건조를 2번 더 할 수 있어요" ,
        fontSize = 10.sp)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyChargeAlertDialog(
    modifier: Modifier=Modifier,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    dialogTitle:String,
    dialogText:String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation
            }) {
                Text(text = "채우기")
            }
        },
        dismissButton = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "취소하기")
            }
        }


        )



}

@Composable
fun MoneyUsageList(
    usageContent:String,
    usageValue:String,
    imageResource:Int,
    modifier: Modifier = Modifier
) {

    ListItem(
        headlineContent = {
            Text(text =usageContent)
        },
        trailingContent = {
            Text(text = usageValue)
        },
        leadingContent = {
            Image(
                painter = painterResource(id = imageResource ),
                contentDescription = usageContent
            )
        }

    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Sejong2WasherTimerTheme {
        Column{
            MoneyLeft()
            MoneyInfo()
            MoneyUsageList(usageContent = "세탁","-1300월",R.drawable.baseline_local_laundry_service_24)
            MoneyUsageList(usageContent = "건조","-1300월",R.drawable.baseline_dry_cleaning_24)
            MoneyUsageList(usageContent = "충전","+5000원",R.drawable.baseline_monetization_on_24)
        }


    }
}