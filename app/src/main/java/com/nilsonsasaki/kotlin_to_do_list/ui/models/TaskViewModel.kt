package com.nilsonsasaki.kotlin_to_do_list.ui.models

import androidx.lifecycle.*
import com.nilsonsasaki.kotlin_to_do_list.R
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.database.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().asLiveData()

    private val emptyTask = Task(
        id = 0,
        title = "",
        date = "",
        startingTime = "",
        endingTime = "",
        priority = "Normal",
        description = ""
    )
    private val emptyErrorsMap = mapOf<String, String?>(
        "title" to null,
        "date" to null,
        "startingTime" to null,
        "endingTime" to null,
        "priority" to null,
    )

    private val _editingTask: MutableLiveData<Task> = MutableLiveData(
        emptyTask
    )
    val editingTask: LiveData<Task> = _editingTask

    fun setEditingTask(editingTask: Task) {
        _editingTask.value = editingTask
    }

    private val _hasChangedValues: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasChangedValues: LiveData<Boolean> = _hasChangedValues

    fun setHasChangedValues(value: Boolean) {
        _hasChangedValues.value = value
    }

    private val _inputErrorsMap: MutableLiveData<Map<String, String?>> =
        MutableLiveData(emptyErrorsMap)

    val inputErrorsMap: LiveData<Map<String, String?>> = _inputErrorsMap

    fun setInputErrorsMap(errorsMap: Map<String, String?>) {
        _inputErrorsMap.value = errorsMap
    }

    fun clearErrorsMap() {
        _inputErrorsMap.value = emptyErrorsMap
    }

    private val _hasInputErrors: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasInputErrors: LiveData<Boolean> = _hasInputErrors

    fun setHasInputErrors(value: Boolean) {
        _hasInputErrors.value = value
    }

    fun getByDate(itemDate: String): LiveData<List<Task>> = taskDao.getByDate(itemDate).asLiveData()

    fun getTaskById(itemId: Int): LiveData<Task> = taskDao.getTaskById(itemId).asLiveData()

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun addNewTask(newTask: Task) {
        insertTask(newTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun updateTask(updatedTask: Task) {
        update(updatedTask)
    }

    private fun update(updatedTask: Task) {
        viewModelScope.launch {
            taskDao.update(updatedTask)
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