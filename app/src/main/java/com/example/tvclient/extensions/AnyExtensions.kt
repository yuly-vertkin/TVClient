package com.example.tvclient.extensions

fun <T, R> List<T>?.applyNotNulNorEmpty(func: (List<T>) -> R?) : R? {
    return if (this != null && this.isNotEmpty()) {
        func(this)
    } else { null }
}
