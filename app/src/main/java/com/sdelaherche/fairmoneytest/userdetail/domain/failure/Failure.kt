package com.sdelaherche.fairmoneytest.userdetail.domain.failure

import com.sdelaherche.fairmoneytest.common.domain.entity.Id
import java.lang.Exception


/**
 * Exception for unknown user id
 */
data class UnknownUserException(val userId: Id) : Exception()
