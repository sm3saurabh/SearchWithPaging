package dev.saurabhmishra.searchwithpagination.utils

import android.util.Log
import android.os.Build
import java.util.regex.Matcher
import java.util.regex.Pattern


// Inspired from timber, but changed to suit my use-case
object Logger {

    private const val MAX_TAG_LENGTH = 23
    private const val CALL_STACK_INDEX = 4
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

    private enum class LogLevel {
        DEBUG, ERROR
    }


    fun debug(msg: String) {
        logMessage(createLogTag(), msg, null, LogLevel.DEBUG)
    }

    fun error(msg: String, exception: Throwable) {
        logMessage(createLogTag(), msg, exception, LogLevel.ERROR)
    }

    private fun createLogTag(): String {
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements" }

        return createTagFromStackTraceElement(stackTrace[CALL_STACK_INDEX])
    }

    private fun createTagFromStackTraceElement(element: StackTraceElement): String {
        var tag: String = element.className
        val m: Matcher = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else tag.substring(0, MAX_TAG_LENGTH)
    }

    private fun logMessage(tag: String, message: String, exception: Throwable? = null, level: LogLevel) {
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, message, exception)
            LogLevel.ERROR -> Log.e(tag, message, exception)
        }
    }
}