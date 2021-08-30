package com.nilsonsasaki.kotlin_to_do_list

import android.app.Application
import com.nilsonsasaki.kotlin_to_do_list.database.AppDatabase

class TaskApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}