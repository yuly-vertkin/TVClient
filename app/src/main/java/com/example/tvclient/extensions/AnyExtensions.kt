package com.example.tvclient.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun <T, R> List<T>?.applyNotNulNorEmpty(func: (List<T>) -> R?) : R? {
    return if (this != null && this.isNotEmpty()) {
        func(this)
    } else { null }
}

fun Activity.hideKeyboard() {
    val v = currentFocus ?: View(this)
    if (v is EditText) v.clearFocus()
    v.windowToken?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            it,
            0
        )
    }
}

