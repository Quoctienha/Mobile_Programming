package com.example.todolistapp

import java.time.Instant
import java.util.Date

object ToDoManager {

    private val toDoList = mutableListOf<Task>()

    fun getAllToDo(): List<Task>{
        return toDoList
    }

    fun addTask(title: String, createdAt: Date){
        toDoList.add(Task(System.currentTimeMillis().toInt(), title, createdAt))
    }

    fun deleteTask(id: Int){
        toDoList.removeIf {
            it.id==id
        }

    }
}