package com.sdelaherche.fairmoneytest.userdetail.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.databinding.FragmentUserDetailBinding
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Gender
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserDetailFragment : Fragment() {

    private var binding: FragmentUserDetailBinding? = null
    private val args: UserDetailFragmentArgs by navArgs()
    private val userDetailViewModel: UserDetailViewModel by viewModel { parametersOf(args.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserDetailBinding.inflate(inflater, container, false).let {
            binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // start progress indicator
        setProgressVisibility(View.VISIBLE)
        setUpSwipeRefreshLayout()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun loadData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailViewModel.userDetail.collectLatest { result ->
                    result.fold(
                        onSuccess = {
                            fillUserDetailFields(it)
                        },
                        onFailure = {
                            showError()
                        }
                    )
                    // stop Progress indicator
                    setProgressVisibility(View.GONE)
                }
            }
        }
    }

    private fun setUpSwipeRefreshLayout() {
        binding?.let {
            it.root.setOnRefreshListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        userDetailViewModel.refresh().first().let { result ->
                            result.onFailure {
                                showError()
                            }
                            it.root.isRefreshing = false
                            this@launch.cancel()
                        }
                    }
                }
            }
        }
    }

    private fun setProgressVisibility(visibility: Int) {
        when (visibility) {
            View.VISIBLE -> {
                binding?.progress?.visibility = View.VISIBLE
                binding?.detailContainer?.visibility = View.GONE
            }
            View.GONE -> {
                binding?.progress?.visibility = View.GONE
                binding?.detailContainer?.visibility = View.VISIBLE
            }
        }
    }

    private fun fillUserDetailFields(detail: UserDetailModel) {
        binding?.let {
            setUserPicture(detail, it)
            setGenderIndicator(detail, it.profileBanner.genderIndicator)
            it.etProfileFirstName.editText?.setText(detail.firstName)
            it.etProfileLastName.editText?.setText(detail.lastName)
            it.etProfileBirthday.editText?.setText(detail.birthDate)
            it.etProfileEmail.editText?.setText(detail.email)
            it.etProfilePhone.editText?.setText(detail.phone)
            it.etProfileStreet.editText?.setText(detail.street)
            it.etProfileCity.editText?.setText(detail.city)
            it.etProfileState.editText?.setText(detail.state)
            it.etProfileCountry.editText?.setText(detail.country)
        }
    }

    private fun setUserPicture(
        detail: UserDetailModel,
        it: FragmentUserDetailBinding
    ) {
        Glide.with(this)
            .load(detail.picture)
            .apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
            )
            .into(it.profileBanner.ivProfilePicture)
    }

    private fun setGenderIndicator(
        detail: UserDetailModel,
        view: ImageView
    ) {
        when (detail.gender) {
            Gender.FEMALE -> {
                view.setColorFilter(Color.MAGENTA)
            }
            Gender.MALE -> {
                view.setColorFilter(Color.BLUE)
            }
        }
    }

    private fun showError() {
        binding?.let {
            Snackbar.make(it.root, R.string.generic_error, Snackbar.LENGTH_LONG).show()
        }
    }
}
