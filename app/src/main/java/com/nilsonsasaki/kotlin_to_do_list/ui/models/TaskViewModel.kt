package com.nilsonsasaki.kotlin_to_do_list.ui.models

import androidx.lifecycle.*
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.database.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().asLiveData()

    fun getByDate(itemDate: String): Flow<List<Task>> = taskDao.getByDate(itemDate)

    fun getAllByPriority(): Flow<List<Task>> = taskDao.getAllByPriority()

    fun getTaskById(itemId: Int): LiveData<Task> = taskDao.getTaskById(itemId).asLiveData()

    fun deleteTask(task: Task){
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }


    fun addNewTask(
        taskTitle: String,
        taskDate: String,
        taskStartingTime: String,
        taskEndingTime: String,
        taskPriority: String,
        taskPeriodicity: String,
        taskDescription: String
    ) {
        val newTask = Task(
            id = 0,
            title = taskTitle,
            date = taskDate,
            startingTime = taskStartingTime,
            endingTime = taskEndingTime,
            priority = taskPriority,
            periodicity = taskPeriodicity,
            description = taskDescription
        )
        insertTask(newTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }
}

class TaskViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}