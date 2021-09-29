package com.sdelaherche.fairmoneytest.common.domain.failure

/**
 * Exception when communicating with the remote api. Contains http [statusCode].
 */
data class ApiException(val statusCode: String) : Exception()

/**
 * Exception indicating that device is not connected to the internet
 */
class NoInternetException : Exception() {
    override fun equals(other: Any?): Boolean {
        return other is NoInternetException
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

/**
 * Not handled unexpected exception
 */
class UnexpectedException(cause: Throwable) : Exception(cause) {
    override fun equals(other: Any?): Boolean {
        return other is UnexpectedException && cause == other.cause
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
