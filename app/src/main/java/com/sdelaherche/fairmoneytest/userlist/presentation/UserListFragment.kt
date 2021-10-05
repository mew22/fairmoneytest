package com.sdelaherche.fairmoneytest.userlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sdelaherche.fairmoneytest.databinding.FragmentUserListBinding
import com.sdelaherche.fairmoneytest.common.presentation.showError
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private var binding: FragmentUserListBinding? = null
    private val userListViewModel: UserListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserListBinding.inflate(inflater, container, false).let {
            binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSwipeRefreshLayout()
        setUpRecyclerView()
    }

    private fun setUpSwipeRefreshLayout() {
        binding?.let {
            it.root.setOnRefreshListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        userListViewModel.refresh().first().let { result ->
                            result.onFailure {
                                showError(binding)
                            }
                            it.root.isRefreshing = false
                            this@launch.cancel()
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        with(UserListAdapter()) {
            this.clickListener = {
                findNavController().navigate(
                    UserListFragmentDirections.showDetail(
                        it.id
                    )
                )
            }
            binding?.recyclerUserList?.adapter = this
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    userListViewModel.userList.collectLatest { response ->
                        when (response) {
                            is Success -> {
                                binding?.root?.isRefreshing = false
                                this@with.submitList(response.list)
                            }
                            is Failure -> {
                                binding?.root?.isRefreshing = false
                                showError(binding, response.message)
                            }
                            is Loading -> {
                                binding?.root?.isRefreshing = true
                            }
                        }
                    }
                }
            }
        }
    }
}
