package com.example.todolistapp


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ToDoListPage(viewModel: ToDoViewModel){

    val toDoList by viewModel.toDoList.observeAsState()
    var inputText by rememberSaveable { mutableStateOf("") }

    //menu sort
    var expanded by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current // Lấy context để dùng Toast

    //dateTimePicker
    val calendar = remember { Calendar.getInstance() }
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val selectedDateTime = rememberSaveable { mutableStateOf(Date()) }

    DateTimePickerDialog(
        showDatePicker = showDatePicker,
        showTimePicker = showTimePicker,
        calendar = calendar,
        onDateTimeSelected = {
            selectedDateTime.value = it
        }
    )

    Column (
        modifier = Modifier.fillMaxHeight()
            .padding(8.dp)
    ){
        //thêm task
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
        ){
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputText,
                onValueChange = {
                    inputText = it
                })

        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showDatePicker.value = true }) {
                Text(SimpleDateFormat("HH:mm a, dd/MM/yyyy", Locale.ENGLISH).format(selectedDateTime.value))
            }
            Button(onClick = {
                if(inputText.isNotBlank()){
                    viewModel.addTask(inputText.trim(), selectedDateTime.value)
                    inputText = ""
                }
                else{
                    Toast.makeText(context, "Vui lòng nhập nội dung task!", Toast.LENGTH_SHORT).show()
                }
            }){
                Text(text = "Add Task")
            }
        }

        toDoList?.let {

            //Sắp xếp
            Box(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = { expanded = true }) {
                    Text("Sort")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(text = { Text("Sort by Name") }, onClick = {
                        viewModel.setSortType(SortType.NAME)
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text("Sort by Date") }, onClick = {
                        viewModel.setSortType(SortType.DATE)
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text("Default Order") }, onClick = {
                        viewModel.setSortType(SortType.CREATED)
                        expanded = false
                    })
                }
            }
            //danh sách task
            LazyColumn (
                content = {
                    itemsIndexed(it){ _: Int, item: Task ->
                        ToDoItem(item = item, onDelete = {
                            viewModel.deleteTask(item.id)
                        })
                    }
                }
            )
        }?: Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "No tasks yet",
            fontSize = 16.sp
        )

    }

}

@Composable
fun ToDoItem(item: Task, onDelete : ()-> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier = Modifier.weight(1f)
        ){
            Text(
                text =SimpleDateFormat("HH:mm a, dd/MM/yyyy", Locale.ENGLISH).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text =item.title,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        //nút xoá task
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}

@Composable
fun DateTimePickerDialog(
    showDatePicker: MutableState<Boolean>,
    showTimePicker: MutableState<Boolean>,
    calendar: Calendar,
    onDateTimeSelected: (Date) -> Unit
) {
    val context = LocalContext.current

    if (showDatePicker.value) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showDatePicker.value = false
                showTimePicker.value = true
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                onDateTimeSelected(calendar.time)
                showTimePicker.value = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}



