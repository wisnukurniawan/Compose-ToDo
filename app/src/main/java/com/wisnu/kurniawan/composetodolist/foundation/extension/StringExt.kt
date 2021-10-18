package com.wisnu.kurniawan.composetodolist.foundation.extension

fun String.firstOrEmpty(): String = if (isBlank()) "" else first().toString()

/**
 * Build phrase queries
 */
fun String.sanitizeQuery(): String {
    return trim()
        .split(" ")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { it.replace(Regex.fromLiteral("\""), "\"\"") }
        .joinToString(separator = " ") { "\"$it*\"" }
}

fun String.ellipsisAt(n: Int): String {
    require(n >= 0) { "Requested character count $n is less than zero." }
    return if (length > n) {
        substring(0, n.coerceAtMost(length)) + "..."
    } else {
        this
    }
}
