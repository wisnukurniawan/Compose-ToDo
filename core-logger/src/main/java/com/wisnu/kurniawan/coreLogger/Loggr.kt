package com.wisnu.kurniawan.coreLogger

import android.util.Log

inline fun Any.LoggrDebug(tag: String? = null, message: () -> Any?) {
    log(priority = Log.DEBUG, tag = tag, message = message)
}

inline fun Any.LoggrError(tag: String? = null, throwable: Throwable? = null, message: () -> Any?) {
    log(priority = Log.ERROR, tag = tag, message = message, throwable = throwable)
}

inline fun Any.LoggrVerbose(tag: String? = null, message: () -> Any?) {
    log(priority = Log.VERBOSE, tag = tag, message = message)
}

inline fun Any.LoggrWarn(tag: String? = null, message: () -> Any?) {
    log(priority = Log.WARN, tag = tag, message = message)
}

inline fun Any.LoggrInfo(tag: String? = null, message: () -> Any?) {
    log(priority = Log.INFO, tag = tag, message = message)
}

inline fun Any.LoggrRecord(tag: String? = null, throwable: Throwable? = null, message: () -> Any?) {
    log(priority = Log.ASSERT, tag = tag, message = message, throwable = throwable)
}

inline fun Any.log(
    priority: Int,
    tag: String? = null,
    message: () -> Any?,
    throwable: Throwable? = null
) {
    val tagOrCaller = tag ?: outerClassSimpleNameInternalOnlyDoNotUseKThxBye()
    Logger.log(
        priority,
        tagOrCaller,
        message()?.toString() ?: "null",
        throwable
    )
}

@PublishedApi
internal fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String {
    val javaClass = this::class.java
    val fullClassName = javaClass.name
    val outerClassName = fullClassName.substringBefore('$')
    val simplerOuterClassName = outerClassName.substringAfterLast('.')
    return if (simplerOuterClassName.isEmpty()) {
        fullClassName
    } else {
        simplerOuterClassName.removeSuffix("Kt")
    }
}
