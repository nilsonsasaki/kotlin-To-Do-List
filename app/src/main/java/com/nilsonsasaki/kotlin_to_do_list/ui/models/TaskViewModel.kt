package com.nilsonsasaki.kotlin_to_do_list.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.database.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getByDate(itemDate: String): Flow<List<Task>> = taskDao.getByDate(itemDate)

    fun getAllByPriority(): Flow<List<Task>> = taskDao.getAllByPriority()

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