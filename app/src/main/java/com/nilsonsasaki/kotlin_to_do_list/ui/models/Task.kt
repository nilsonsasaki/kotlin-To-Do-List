package com.nilsonsasaki.kotlin_to_do_list.ui.models

data class Task(
    val title: String,
    val time: String,
    val date: String,
    val id :Int = -1,
){
    override fun equals(other: Any?): Boolean {
        if (this === other)return true
        if (javaClass!= other?.javaClass)return false

        other as Task
        if (id!= other.id)return false
        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
