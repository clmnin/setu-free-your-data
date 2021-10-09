package software.sauce.easyledger.presentation.utils

sealed class AsyncEvent<out T: Any> {
    data class Success<out T: Any>(val value: T): AsyncEvent<T>()
    data class Error(val msg: String, val cause: Exception? = null): AsyncEvent<Nothing>()
}