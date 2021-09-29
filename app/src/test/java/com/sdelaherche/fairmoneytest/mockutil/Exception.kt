package com.sdelaherche.fairmoneytest.mockutil

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException

fun generateExceptionFromClass(clazz: Class<Exception>, param: Any? = null):
        Exception = when (clazz) {
    ApiException::class.javaObjectType -> {
        ApiException("any")
    }
    NoInternetException::class.javaObjectType -> {
        NoInternetException()
    }
    UnexpectedException::class.javaObjectType -> {
        UnexpectedException(Exception())
    }
    UnknownUserException::class.javaObjectType -> {
        UnknownUserException(param as Id)
    }
    else -> {
        assert(true)
        Exception()
    }
}
