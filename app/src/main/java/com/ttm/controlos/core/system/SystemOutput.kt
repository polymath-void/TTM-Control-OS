package com.ttm.controlos.core.system

import android.util.Log

object SystemOutput {

    var callback: ((String) -> Unit)? = null

    fun send(message: String) {
        Log.d("TTM", message)
        callback?.invoke(message)
    }
}
