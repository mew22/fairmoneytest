package com.sdelaherche.fairmoneytest.common.data.mapper

import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import java.io.IOException
import kotlin.reflect.KClass
import retrofit2.HttpException

abstract class HttpExceptionMapper(protected val callArguments: List<String>) {

    abstract fun map(httpException: HttpException): Exception?
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionsMapper(val value: KClass<out HttpExceptionMapper>)

fun mapToDomainException(
    remoteException: Throwable,
    httpExceptionsMapper: HttpExceptionMapper? = null
): Exception {
    return when (remoteException) {
        is IOException -> NoInternetException()
        is HttpException -> httpExceptionsMapper?.map(remoteException) ?: ApiException(
            remoteException.code().toString()
        )
        else -> UnexpectedException(remoteException)
    }
}
