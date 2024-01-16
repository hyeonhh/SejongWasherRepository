package com.example.sejong2washertimer.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejong2washertimer.R
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme

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

                }
                CardChargeApp()

                }
            }
        }
    }


@Composable
fun CardChargeApp() {

        Column {
            MoneyLeft(viewModel = ChargeViewModel())
            MoneyInfo()
            MoneyUsageList(usageContent = "세탁","-1300월",R.drawable.baseline_local_laundry_service_24)
            MoneyUsageList(usageContent = "건조","-1300월",R.drawable.baseline_dry_cleaning_24)
            MoneyUsageList(usageContent = "충전","+5000원",R.drawable.baseline_monetization_on_24)
        }

}

@Composable
fun MoneyLeft(
    modifier: Modifier = Modifier,
    viewModel: ChargeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(10.dp)
    ) {

        Text("남은 금액 : ${viewModel.chargedMoney}원")

        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                showDialog = true
            }
            // todo : dialog를 통해 남은 금액 업데이트 로직 구현
            // todo : 충전 이력에 추가
        ) {
            Text(text = "채우기")
        }

        when {
            showDialog -> {
                MoneyChargeAlertDialog(
                    viewModel=viewModel,
                    onDismissRequest = {
                      showDialog = false
                    },
                )
            }
        }
    }
}

@Composable
fun MoneyInfo(
    modifier: Modifier = Modifier) {
    Box {
        //todo : 잔여횟수 계산 로직 추가
        Text(
            "앞으로 세탁을 3번 더 할 수 있어요",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
    Text(
        "앞으로 세탁 & 건조를 2번 더 할 수 있어요",
        fontSize = 10.sp,
        textAlign = TextAlign.Center

    )

}
@Composable
fun addChargeMoney(
    isAvailable:Boolean,
    viewModel: ChargeViewModel,
    input:Int)  {

    if(isAvailable) {

        //todo : 남은 금액}

    }
}


@Composable
fun MoneyChargeAlertDialog(
    onDismissRequest: () -> Unit,
    viewModel: ChargeViewModel,
) {


    var chargeValue by remember {
        mutableStateOf(0)
    }
    var isVisible by remember {
        mutableStateOf(false)
    }

    val hint = "얼마나 채울까요?"
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp)

        ) {
            TextField(
                value = "${chargeValue}원",
                onValueChange = {
                    chargeValue = it.toIntOrNull() ?: 0
                    //chargedMoney -> viewModel.updateChargedMoney(chargedMoney.toInt())
                    isVisible = true
                },
                singleLine = true,
                label = { Text(text = hint) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )

            )

            Row() {
                TextButton(
                    onClick = {
                        chargeValue += 1000
                        isVisible = true

                    },
                ) {
                    Text(
                        text = "+1천원",
                        fontSize = 10.sp,
                    )

                }

                TextButton(onClick = {
                    chargeValue += 5000
                    isVisible = true


                }) {
                    Text(
                        text = "+5천원",
                        fontSize = 10.sp,
                    )

                }

                TextButton(onClick = {
                    chargeValue += 10000
                    isVisible = true

                }) {
                    Text(
                        text = "+1만원",
                        fontSize = 10.sp,
                    )

                }
            }
            if (isVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "취소하기")
                    }


                    TextButton(
                        onClick = {
                            onDismissRequest()
                            //todo : 이 잔액을 다이얼로그 밖으로 이동

                            viewModel.updateChargedMoney(chargeValue)
                            Log.d("잔액", chargeValue.toString())
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {

                        Text(text = "채우기")

                    }


                }
            }

        }
    }

}



@Composable
fun MoneyUsageList(
    usageContent: String,
    usageValue: String,
    imageResource: Int,
    modifier: Modifier = Modifier
) {

    ListItem(
        headlineContent = {
            Text(text = usageContent)
        },
        trailingContent = {
            Text(text = usageValue)
        },
        leadingContent = {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = usageContent
            )
        }

    )

}
