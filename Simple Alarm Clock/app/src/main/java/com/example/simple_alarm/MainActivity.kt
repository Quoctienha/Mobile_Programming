package com.example.simple_alarm

import android.app.AlarmManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*
import android.provider.Settings
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kiểm tra và yêu cầu quyền báo thức chính xác
        checkAndRequestAlarmPermission(this)  // Kiểm tra quyền đặt báo thức
        setContent {
            AlarmApp(context = this)
        }
    }
}


/*
Từ Android 12 (API 31), Google giới hạn quyền này để tránh tiêu hao pin và tài nguyên hệ thống.
Nếu không có quyền, báo thức có thể bị trì hoãn hoặc không hoạt động đúng như mong đợi.
*/
fun checkAndRequestAlarmPermission(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (!alarmManager.canScheduleExactAlarms()) { //Kiểm tra xem ứng dụng có quyền đặt báo thức chính xác không.
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM) //Nếu ứng dụng chưa có quyền, nó sẽ mở trang cài đặt của hệ thống, để yêu cầu người dùng cấp quyền.
        context.startActivity(intent)
    }
}



@Composable
fun AlarmApp(context: Context) {
    // Dùng rememberSaveable đễ vẫn lưu khi xoay màn hình
    var selectedHour by rememberSaveable { mutableIntStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by rememberSaveable { mutableIntStateOf(Calendar.getInstance().get(Calendar.MINUTE)) }
    var label by rememberSaveable { mutableStateOf("") }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    //Header
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Simple Alarm Clock", style = MaterialTheme.typography.titleLarge)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Alarm at: $selectedHour:$selectedMinute", style = MaterialTheme.typography.titleLarge, fontSize = 40.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showTimePicker = true }) {
            Text(text = "Choose the time")
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Type in an alarm message") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { setAlarm(context, selectedHour, selectedMinute, label) }) {
            Text(text = "Set the Alarm")
        }

        if (showTimePicker) {
            TimePickerDialog(
                onTimeSelected = { hour, minute ->
                    selectedHour = hour
                    selectedMinute = minute
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

//Hiện nơi chọn giờ, phút
@Composable
fun TimePickerDialog(onTimeSelected: (Int, Int) -> Unit, onDismiss: () -> Unit) {
    val calendar = Calendar.getInstance()
    var hour by remember { mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableIntStateOf(calendar.get(Calendar.MINUTE)) }

    AlertDialog(
        title = { Text("Pick a time", fontWeight = FontWeight.Bold) },



        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Hour: $hour - Minute: $minute", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                //Chỉnh giờ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Hour: ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Button(onClick = { hour = (hour + 1) % 24 }) { Text("+", style = MaterialTheme.typography.labelLarge, fontSize = 15.sp)}
                    Text(text = "$hour", fontSize = 15.sp)
                    Button(onClick = { hour = (hour - 1 + 24) % 24 }) { Text("-", style = MaterialTheme.typography.labelLarge, fontSize = 15.sp) }
                }

                //Chỉnh phút
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Minute: ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Button(onClick = { minute = (minute + 1) % 60 }) { Text("+", style = MaterialTheme.typography.labelLarge, fontSize = 15.sp) }
                    Text(text = "$minute", fontSize = 15.sp)
                    Button(onClick = { minute = (minute - 1 + 60) % 60 }) { Text("-", style = MaterialTheme.typography.labelLarge, fontSize = 15.sp) }
                }
            }
        },



        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onDismiss, modifier = Modifier.padding(8.dp)) {
                    Text("Cancel")
                }
                Button(onClick = { onTimeSelected(hour, minute) }, modifier = Modifier.padding(8.dp)) {
                    Text("OK")
                }
            }
        }

    )
}



fun setAlarm(context: Context, hour: Int, minute: Int, label: String) {
    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minute)
        putExtra(AlarmClock.EXTRA_MESSAGE, label)
        putExtra(AlarmClock.EXTRA_SKIP_UI, false) // Mở giao diện báo thức của hệ thống
    }
    try {
        context.startActivity(intent)
        Toast.makeText(context, "Alarm about '$label' has set at $hour:$minute", Toast.LENGTH_SHORT).show()
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No alarm clock app is available to support", Toast.LENGTH_SHORT).show()
    }
}
