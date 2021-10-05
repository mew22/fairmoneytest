package com.sdelaherche.fairmoneytest.common.domain.failure

import com.sdelaherche.fairmoneytest.common.domain.entity.Id

sealed class DomainException(cause: Throwable? = null): Exception(cause)

sealed class RemoteException(cause: Throwable? = null): DomainException(cause)

/**
 * Exception for unknown user id
 */
data class UserNotFoundException(val userId: Id) : DomainException()

/**
 * Exception when communicating with the remote api. Contains http [statusCode].
 */
data class ApiException(val statusCode: String) : RemoteException()

/**
 * Exception indicating that device is not connected to the internet
 */
class NoInternetException : RemoteException() {
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
class UnexpectedException(cause: Throwable) : RemoteException(cause) {
    override fun equals(other: Any?): Boolean {
        return other is UnexpectedException && cause == other.cause
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

sealed class LocalException(cause: Throwable? = null): DomainException(cause)

class LocalInsertionException: LocalException() {
    override fun equals(other: Any?): Boolean {
        return other is LocalInsertionException
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}