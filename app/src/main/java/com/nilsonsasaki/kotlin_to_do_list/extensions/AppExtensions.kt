package com.nilsonsasaki.kotlin_to_do_list.extensions


import com.google.android.material.textfield.TextInputLayout

fun dateFormat(day:Int,month:Int,year:Int):String{
    val dayString = if (day in 1..9)" $day" else "$day"
    val monthString = if(month in 1..9)"0$month" else "$month"
    return "$dayString/$monthString/$year"
}

fun timeFormat(hour:Int,minute:Int):String{
    val hourString = if(hour in 0..9) "0$hour" else "$hour"
    val minuteString = if (minute in 0..9) "0$minute" else "$minute"
    return "$hourString:$minuteString"
}

var TextInputLayout.text : String
    get() = editText?.text?.toString()?:""
    set(value){
        editText?.setText(value)
    }