package com.example.todolistapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

enum class SortType { DATE, NAME, CREATED }

class ToDoViewModel: ViewModel() {

    private val _sortType = MutableStateFlow(SortType.CREATED)
    val sortType = _sortType.asStateFlow()

    private var _toDoList = MutableLiveData<List<Task>>()
    val toDoList: LiveData<List<Task>> = _toDoList

    fun setSortType(sort: SortType) {
        _sortType.value = sort
        getAllToDo()
    }

    fun getAllToDo(){
        val currentList = ToDoManager.getAllToDo()
        _toDoList.value = when (_sortType.value) {
            SortType.NAME -> currentList.sortedBy { it.title }
            SortType.DATE -> currentList.sortedBy { it.createdAt }
            SortType.CREATED -> currentList.reversed()
        }
    }

    fun addTask(title: String, createdAt: Date){
        ToDoManager.addTask(title, createdAt)
        getAllToDo()
    }

    fun deleteTask(id: Int){
        ToDoManager.deleteTask(id)
        getAllToDo()
    }
}