package com.sdelaherche.fairmoneytest.common.presentation

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.common.domain.failure.ApiException
import com.sdelaherche.fairmoneytest.common.domain.failure.DomainException
import com.sdelaherche.fairmoneytest.common.domain.failure.LocalInsertionException
import com.sdelaherche.fairmoneytest.common.domain.failure.NoInternetException
import com.sdelaherche.fairmoneytest.common.domain.failure.UnexpectedException
import com.sdelaherche.fairmoneytest.common.domain.failure.UserNotFoundException

fun showError(binding: ViewBinding?, message: String? = null) {
    binding?.let {
        if (message != null) {
            Snackbar.make(it.root, message, Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(it.root, R.string.generic_error, Snackbar.LENGTH_LONG).show()
        }
    }
}

fun showMessage(binding: ViewBinding?, message: String) {
    binding?.let {
        Snackbar.make(it.root, message, Snackbar.LENGTH_LONG).show()
    }
}

fun getErrorMessageFromException(ex: DomainException, context: Context): String =
    when (ex) {
        is NoInternetException -> {
            context.getString(R.string.internet_error)
        }
        is ApiException,
        is UnexpectedException -> {
            context.getString(R.string.server_error)
        }
        is LocalInsertionException -> {
            context.getString(R.string.local_error)
        }
        is UserNotFoundException -> {
            context.getString(R.string.user_not_found_error)
        }
        else -> context.getString(R.string.generic_error)
    }