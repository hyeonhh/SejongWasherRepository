package com.example.sejong2washertimer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.sejong2washertimer.viewModel.ChargeViewModel
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme


class CardChargeScreen : ComponentActivity() {

    private val chargeViewModel by viewModels<ChargeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sejong2WasherTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
                CardChargeApp(chargeViewModel)

                }
            }
        }
    }


@SuppressLint("SuspiciousIndentation")
@Composable
fun CardChargeApp(chargeViewModel: ChargeViewModel) {
        Column {
            MoneyLeft(viewModel = chargeViewModel)
        }

}

@Composable
fun MoneyLeft(
    modifier: Modifier = Modifier,
    viewModel: ChargeViewModel
) {

    var showDialog by remember { mutableStateOf(false) }
    val chargedMoney by viewModel.chargedMoneyFlow.collectAsState(initial = 0)


    Column(
        modifier = modifier
    ) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(10.dp)
    ) {

        Text("남은 금액 : ${chargedMoney}원")

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
    }
        Text(
            "앞으로 세탁을 ${chargedMoney.div(1300)}번 더 할 수 있어요",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )

        Text(
            "앞으로 세탁 & 건조를 ${chargedMoney.div(2600)}번 더 할 수 있어요",
            fontSize = 10.sp,
            textAlign = TextAlign.Center

        )

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
                            viewModel.updateChargedMoney(chargeValue)
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



