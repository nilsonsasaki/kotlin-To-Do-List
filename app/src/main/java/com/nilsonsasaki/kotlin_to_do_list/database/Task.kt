package com.nilsonsasaki.kotlin_to_do_list.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @NonNull @ColumnInfo(name = "title")
    val title: String,

    @NonNull @ColumnInfo(name = "date")
    val date: String,

    @NonNull @ColumnInfo(name = "starting_time")
    val startingTime: String,

    @ColumnInfo(name = "ending_time")
    val endingTime: String,

    @NonNull @ColumnInfo(name = "priority")
    val priority: String,

    @ColumnInfo(name = "description")
    val description: String,

    )
