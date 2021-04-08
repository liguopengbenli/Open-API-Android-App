package com.codingwithmitch.openapi.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.codingwithmitch.openapi.R

// take ressource file as parameter
fun Activity.displayToast(@StringRes message: Int){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displaySuccessDialog(message: String?){
    MaterialDialog(this).show {
        title(R.string.text_success)
        message(text = message)
        positiveButton(R.string.text_ok)
    }
}

fun Activity.displayErrorDialog(message: String?){
    MaterialDialog(this).show {
        title(R.string.text_error)
        message(text = message)
        positiveButton(R.string.text_ok)
    }
}

fun Activity.displayInfoDialog(message: String?){
    MaterialDialog(this)
        .show {
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.areYouSureDialog(message: String, callBack: AreYouSureCallback){
    MaterialDialog(this)
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callBack.cancel()
            }
            positiveButton(R.string.text_yes){
                callBack.proceed()
            }
        }
}

interface AreYouSureCallback{
    fun proceed()

    fun cancel()
}