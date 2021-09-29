package com.sdelaherche.fairmoneytest.userdetail.data.mapper

import com.sdelaherche.fairmoneytest.common.data.mapper.HttpExceptionMapper
import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import com.sdelaherche.fairmoneytest.userdetail.domain.failure.UnknownUserException
import retrofit2.HttpException

class UnknownUserExceptionMapper(arguments: List<String>) : HttpExceptionMapper(arguments) {
    companion object {
        private const val DATA_NOT_FOUND_CODE = 404
    }

    override fun map(httpException: HttpException): Exception? {
        return if (httpException.code() == DATA_NOT_FOUND_CODE) {
            UnknownUserException(Id(callArguments.first()))
        } else {
            null
        }
    }
}
