package com.slc.morse.utils

fun String.isLetter(): Boolean {
    return this.matches(Regex("[a-zA-Z]"))
}

fun String.isNumber(): Boolean {
    return this.toDoubleOrNull() != null
}