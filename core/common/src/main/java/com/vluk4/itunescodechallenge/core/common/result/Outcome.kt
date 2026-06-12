package com.vluk4.itunescodechallenge.core.common.result

sealed interface Outcome<out T> {
    data class Success<T>(val data: T) : Outcome<T>
    data class Failure(val error: Throwable) : Outcome<Nothing>
}

inline fun <T> runCatchingOutcome(block: () -> T): Outcome<T> = try {
    Outcome.Success(block())
} catch (t: Throwable) {
    Outcome.Failure(t)
}
