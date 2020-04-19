package io.mellouk.common

import android.view.View
import androidx.core.view.isVisible

/**
 * Helper extension to make `when` statements exhaustive and to help compiler
 * warn us about non-exhaustive when statements.
 *
 * https://medium.com/r/?url=https%3A%2F%2Fproandroiddev.com%2Ftil-when-is-when-exhaustive-31d69f630a8b
 */
val <T> T.exhaustive: T
    get() = this

fun View.show() {
    this.isVisible = true
}

fun View.hide() {
    this.isVisible = false
}