package com.example.sejong2washertimer.ui


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Context.*
import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sejong2washertimer.R
import com.example.sejong2washertimer.data.ChargeViewModel
import com.example.sejong2washertimer.data.Datasource
import com.example.sejong2washertimer.data.WasherViewModel
import com.example.sejong2washertimer.fcm.NotiModel
import com.example.sejong2washertimer.fcm.PushNotification
import com.example.sejong2washertimer.fcm.RetrofitInstance
import com.example.sejong2washertimer.model.Washer
import com.example.sejong2washertimer.ui.ui.theme.Sejong2WasherTimerTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun WasherApp(
    washerViewModel: WasherViewModel =viewModel(),

) {
    WasherList(
        washerList = Datasource().washers,
        washerViewModel = washerViewModel,
    )
}




@Composable
fun WasherList(
    washerList: List<Washer>,
    modifier: Modifier = Modifier,
    washerViewModel: WasherViewModel,
) {
    LazyColumn(
        modifier=modifier
    ) {
        items(washerList) {
                washer -> WasherCard(
            washerViewModel = washerViewModel,
            washer = washer,
            modifier= Modifier
                .padding(10.dp)

        )
        }
    }
}

@Composable
fun createWasherReference(washerId: String?): DatabaseReference {
    val database = Firebase.database
    return database.getReference("washer${washerId}startTime")
}





@Composable
fun WasherCard(
    washer: Washer,
    modifier: Modifier = Modifier,
    washerViewModel: WasherViewModel,
) {

    var updatedTime by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false)
    }
    val myRef = createWasherReference(washer.washerId)


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
            val washerStartTime = snapshot.getValue(String::class.java)
            if(washerStartTime!= null)    {
                val startDate = SimpleDateFormat("HH:mm").parse(washerStartTime)
                val updateDate = Calendar.getInstance().apply {
                    time=startDate
                    add(Calendar.MINUTE,1)
                }.time

                updatedTime = SimpleDateFormat("HH:mm").format(updateDate)

                washerViewModel.setWasherFinishTime(washerId = washer.washerId, finishTime = updatedTime)
                washerViewModel.setWasherState(washer.washerId,false)

                //todo :2
                if (isCurrentTimeEqualsCompletionTime(updatedTime)){
                    Log.d("${washer.washerId}완료시간", washerViewModel.getWasherState(washer.washerId).toString())
                    washerViewModel.setWasherState(washer.washerId,true)
                    Log.d("${washer.washerId}완료시간 상태 변경 후 ", washerViewModel.getWasherState(washer.washerId).toString())


                }
            }
            else {
                washerViewModel.setWasherState(washer.washerId,true)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseError","Error reading completion time: $error")
        }

    })



    LaunchedEffect(washer.washerId) {
        while (!isCurrentTimeEqualsCompletionTime(updatedTime)) {
            kotlinx.coroutines.delay(1000)
        }
        showToast = true
        washerViewModel.setWasherState(washer.washerId, true)
    }

    if(showToast) {
        Handler(Looper.getMainLooper()).postDelayed({
            showToast=false
        },500)


        //todo : token 저장 후 해당 token 넣어주는 로직 구현 필요!

        LaunchedEffect(Unit){
            try {
                val notiModel = NotiModel("${washer.washerId}번 세탁이 완료되었어요!","\uD83D\uDE0A 세탁물을 찾으러와주세요 ")
                // TODO: 토큰 저장 후 해당 토큰을 넣어주는 로직 추가 필요
                val pushModel = PushNotification(notiModel)
                pushWasherCompleted(pushModel)
                washerViewModel.setWasherState(washer.washerId,true)
                myRef.removeValue()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FCM", "FCM 전송 중 예외 발생: ${e.message}")
            }
        }


    }


    Card(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = washer.washerImageResourceId),
                contentDescription = stringResource(id = washer.washerStringResourceId),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = stringResource(id = washer.washerStringResourceId),
            )
            Spacer(modifier = Modifier.size(30.dp))

            if (washerViewModel.getWasherState(washer.washerId)) {
                    WasherCardClickableContent(
                        washer = washer,
                        washerViewModel  = washerViewModel,
                    )

            }
            else{
                Text(text = "완료 시간 : ${washerViewModel.getWasherFinishTime(washer.washerId)}",
                    style = TextStyle(fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                )
            }


        }
    }




}



@Composable
fun WasherCardClickableContent(
    washerViewModel: WasherViewModel,
    washer: Washer,
) {

    val context = LocalContext.current

    val chargeViewModel =ChargeViewModel(context.applicationContext as Application)

    var showDialog by remember { mutableStateOf(false) }
    val myRef = createWasherReference(washer.washerId)

    fun saveCurrentTimeDatabase() {
        val startedTime = System.currentTimeMillis()
        val formattedTime = SimpleDateFormat("HH:mm").format(startedTime)
        myRef.setValue( formattedTime)

    }
    if (showDialog) {
        // AlertDialog 호출
        StartWasherAlertDialog(
            dialogTitle = "${washer.washerId}번 세탁기 시작",
            dialogText =" 50분동안 세탁이 진행되어요! 세탁을 시작할까요?" ,
            onConfirm = {
                saveCurrentTimeDatabase()
                showDialog = false
                washerViewModel.setWasherState(washerId = washer.washerId, isAvailable = false)
                chargeViewModel.updateChargedMoney(-1300)
                        },
            onDismiss = {
                showDialog = false
//                setIsFirebaseDataAvailable(false)

            }
        )
    }

    // Washer가 사용 가능한 상태일 때 클릭하면 AlertDialog 호출
    Spacer(modifier = Modifier.size(10.dp))

    //todo : 상태값이 사용되는 부분
    if(washerViewModel.getWasherState(washerId = washer.washerId)) {
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
            text = "완료 시간 : ${washerViewModel.getWasherFinishTime(washer.washerId)}",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun StartWasherAlertDialog(
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
                Text(text = "세탁 시작하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "세탁 취소하기")
            }
        }
    )
}






//Push
private fun pushWasherCompleted(notification:PushNotification)= CoroutineScope(Dispatchers.IO).launch {

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



