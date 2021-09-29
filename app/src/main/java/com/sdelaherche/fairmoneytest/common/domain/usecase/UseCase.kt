package com.sdelaherche.fairmoneytest.common.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ReactiveUseCase<in U, T> : UseCase<U, Flow<T>> {
    override operator fun invoke(params: U?): Flow<T>
}

interface UseCase<in U, T> {
    operator fun invoke(params: U? = null): T
}
