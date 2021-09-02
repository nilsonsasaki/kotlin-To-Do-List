package com.nilsonsasaki.kotlin_to_do_list.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY date ASC,starting_time ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date = :itemDate ORDER BY starting_time ASC")
    fun getByDate(itemDate: String): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date ASC,starting_time ASC")
    fun getAllByPriority(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :itemId")
    fun getTaskById(itemId: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


}