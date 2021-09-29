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
import com.google.android.material.snackbar.Snackbar
import com.sdelaherche.fairmoneytest.R
import com.sdelaherche.fairmoneytest.databinding.FragmentUserListBinding
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
                    userListViewModel.userList.collectLatest { userListPage ->
                        userListPage.fold(
                            onSuccess = {
                                this@with.submitList(it)
                            },
                            onFailure = {
                                showError()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showError() {
        binding?.let {
            Snackbar.make(it.root, R.string.generic_error, Snackbar.LENGTH_LONG).show()
        }
    }
}
