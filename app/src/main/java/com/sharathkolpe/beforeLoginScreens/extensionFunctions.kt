package com.sharathkolpe.beforeLoginScreens

import android.widget.Toast
import android.content.Context
import android.os.Vibrator

fun Context.showMsg(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this,msg,duration).show()


fun triggerHapticFeedback(context: Context,duration: Long) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

}