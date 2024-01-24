package com.example.sejong2washertimer.ui

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sejong2washertimer.R
import com.example.sejong2washertimer.data.Datasource
import com.example.sejong2washertimer.fcm.NotiModel
import com.example.sejong2washertimer.fcm.PushNotification
import com.example.sejong2washertimer.fcm.RetrofitInstance
import com.example.sejong2washertimer.fcm.pushWasherCompleted
import com.example.sejong2washertimer.model.Dryer
import com.example.sejong2washertimer.model.Washer
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme
import com.example.sejong2washertimer.viewModel.ChargeViewModel
import com.example.sejong2washertimer.viewModel.DryerViewModel
import com.example.sejong2washertimer.viewModel.WasherViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class DryerScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sejong2WasherTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
                DryerApp()

            }
        }
    }
}



@Composable
fun DryerList(
    dryerList: List<Dryer>,
    modifier: Modifier = Modifier,
    dryerViewModel: DryerViewModel,
) {
    Column(
        horizontalAlignment=Alignment.Start
    ) {
        LazyColumn(
            modifier = modifier) {
            items(dryerList) {
                    dryer -> DryerCard(
                dryer = dryer,
                dryerViewModel=dryerViewModel,
                modifier= Modifier.padding(10.dp)
            )
            }
        }
    }
}

fun createDryerReference(dryerId: String?) : DatabaseReference {
    val database = Firebase.database
    return database.getReference("dryer${dryerId}startTime")
}
@Composable
fun DryerCard(
    dryerViewModel: DryerViewModel,
    dryer: Dryer,
    modifier: Modifier = Modifier
){
    var updatedTime by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false)}

    val myRef = createDryerReference(dryer.dryerId)

    fun isCurrentTimeEqualsCompletionTime(completionTime:String):Boolean{
        try {
            val currentTime = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
            return currentTime==completionTime
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }


    myRef.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val dryerStartTime = snapshot.getValue(String::class.java)
            if(dryerStartTime!= null)    {
                val startDate = SimpleDateFormat("HH:mm").parse(dryerStartTime)
                val updateDate = Calendar.getInstance().apply {
                    time=startDate
                    add(Calendar.MINUTE,1)
                }.time

                updatedTime = SimpleDateFormat("HH:mm").format(updateDate)

                dryerViewModel.setDryerFinishTime(dryerId = dryer.dryerId, finishTime = updatedTime)
                dryerViewModel.setDryerState(dryer.dryerId,false)

                //todo :2
                if (isCurrentTimeEqualsCompletionTime(updatedTime)){
                    Log.d("${dryer.dryerId}완료시간", dryerViewModel.getDryerState(dryer.dryerId).toString())
                    dryerViewModel.setDryerState(dryer.dryerId,true)
                    Log.d("${dryer.dryerId}완료시간 상태 변경 후 ", dryerViewModel.getDryerState(dryer.dryerId).toString())


                }
            }
            else {
                dryerViewModel.setDryerState(dryer.dryerId,true)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseError","Error reading completion time: $error")
        }

    })


    LaunchedEffect(dryer.dryerId) {
        while (!isCurrentTimeEqualsCompletionTime(updatedTime)) {
            delay(1000)
        }
        showToast = true
        dryerViewModel.setDryerState(dryer.dryerId, true)
    }

    if(showToast) {
        Handler(Looper.getMainLooper()).postDelayed({
            showToast=false
        },500)


        //todo : token 저장 후 해당 token 넣어주는 로직 구현 필요!

        LaunchedEffect(Unit){
            try {
                val notiModel = NotiModel("${dryer.dryerId}번 건조가 완료되었어요!","\uD83D\uDE0A 세탁물을 찾으러와주세요 ")
                val pushModel = PushNotification(notiModel)
                pushDryerCompleted(pushModel)
                dryerViewModel.setDryerState(dryer.dryerId,true)
                myRef.removeValue()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FCM", "FCM 전송 중 예외 발생: ${e.message}")
            }
        }


    }


    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = dryer.dryerImageResourceId),
                contentDescription = stringResource(id = dryer.dryerResourceId),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = stringResource(id = dryer.dryerResourceId),
            )
            Spacer(modifier = Modifier.size(20.dp))
            if (dryerViewModel.getDryerState(dryer.dryerId)) {
                DryerCardClickableContent(
                    dryerViewModel = dryerViewModel,
                    dryer  = dryer,
                )

            }
            else{
                Text(text = "완료 시간 : ${dryerViewModel.getDryerFinishTime(dryer.dryerId)}",
                    style = TextStyle(fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                )
            }
        }
    }
}



@Composable
fun DryerApp() {
    DryerList(
        dryerList = Datasource().dryers,
        dryerViewModel = viewModel()
        )

}



@Composable
fun StartDryerAlertDialog(
    dialogTitle: String,
    dialogText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        confirmButton = {
            TextButton(onClick =onConfirm) {
                Text(text = "건조 시작하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "건조 취소하기")
            }
        }
    )
}




@Composable
fun DryerCardClickableContent(
    dryerViewModel: DryerViewModel,
    dryer: Dryer,
) {

    val context = LocalContext.current

    val chargeViewModel = ChargeViewModel(context.applicationContext as Application)

    var showDialog by remember { mutableStateOf(false) }
    val myRef = createDryerReference(dryer.dryerId)

    fun saveCurrentTimeDatabase() {
        val startedTime = System.currentTimeMillis()
        val formattedTime = SimpleDateFormat("HH:mm").format(startedTime)
        myRef.setValue( formattedTime)

    }
    if (showDialog) {
        // AlertDialog 호출
        StartDryerAlertDialog(
            dialogTitle = "${dryer.dryerId}번 건조기 시작",
            dialogText =" 50분동안 건조가 진행돼요! 건조를 시작할까요?" ,
            onConfirm = {
                saveCurrentTimeDatabase()
                showDialog = false
                dryerViewModel.setDryerState(dryerId = dryer.dryerId, isAvailable = false)
                chargeViewModel.updateChargedMoney(-1300)

            },
            onDismiss = {
                showDialog = false

            }
        )
    }

    Spacer(modifier = Modifier.size(10.dp))

    if(dryerViewModel.getDryerState(dryerId = dryer.dryerId)) {
        Image(
            painter = painterResource(id = R.drawable.playicon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    showDialog = true

                }
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "사용 가능",
            style = TextStyle(fontSize = 13.sp, color = Color.Blue)
        )
    }
    else{
        Text(
            text = "완료 시간 : ${dryerViewModel.getDryerFinishTime(dryer.dryerId)}",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
        )
    }
}



//Push
private fun pushDryerCompleted(notification:PushNotification)= CoroutineScope(Dispatchers.IO).launch {

    try {
        val response = RetrofitInstance.api.postNotification(notification)
        if(response.isSuccessful) {
            Log.d("testPush성공",response.body().toString())

        }
        else {
            Log.e("실패","${response.errorBody()?.string()}")
            Log.e("실패","${response.code()}")
            Log.e("실패","${response.headers()}")


        }
    }
    catch (e:Exception){
        e.printStackTrace()
    }

}
