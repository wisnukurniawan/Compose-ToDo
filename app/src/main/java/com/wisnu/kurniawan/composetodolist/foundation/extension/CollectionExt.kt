package com.wisnu.kurniawan.composetodolist.foundation.extension

fun <T> Iterable<T>.joinToString(
    separator: (Int) -> CharSequence = { ", " }
): String {
    return joinTo(StringBuilder(), separator).toString()
}

private fun <T, A : Appendable> Iterable<T>.joinTo(
    buffer: A,
    separator: (Int) -> CharSequence
): A {
    val limit = -1
    var count = 0
    for (element in this) {
        if (++count > 1) buffer.append(separator(count - 1))
        if (limit < 0 || count <= limit) {
            buffer.append(element.toString())
        } else break
    }
    return buffer
}
