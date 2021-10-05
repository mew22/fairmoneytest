package com.sdelaherche.fairmoneytest.mockutil

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.LocalInsertionException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.common.domain.failure.UserNotFoundException

fun generateExceptionFromClass(clazz: Class<DomainException>, param: Any? = null):
        DomainException = when (clazz) {
    ApiException::class.javaObjectType -> {
        ApiException("any")
    }
    NoInternetException::class.javaObjectType -> {
        NoInternetException()
    }
    UnexpectedException::class.javaObjectType -> {
        UnexpectedException(Exception())
    }
    UserNotFoundException::class.javaObjectType -> {
        UserNotFoundException(param as Id)
    }
    LocalInsertionException::class.javaObjectType -> {
        LocalInsertionException()
    }
    else -> UnexpectedException(Exception())
}
