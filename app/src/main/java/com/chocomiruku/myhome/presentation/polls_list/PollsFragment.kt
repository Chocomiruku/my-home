package com.chocomiruku.myhome.presentation.polls_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.databinding.PollsFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PollsFragment : Fragment() {

    private var _binding: PollsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PollsViewModel by viewModels()

    private lateinit var pollAdapter: PollAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PollsFragmentBinding.inflate(inflater, container, false)

        bindUserDetails()

        binding.addBtn.setOnClickListener {
            findNavController().navigate(PollsFragmentDirections.actionPollsFragmentToAddPollFragment())
        }

        return binding.root
    }

    private fun bindPolls() = with(binding) {
        viewModel.getPolls()
        viewModel.pollsStateFlow.asLiveData().observe(viewLifecycleOwner) { resource ->
            progressIndicator.isVisible = resource == Resource.Loading
            pollsList.isVisible = resource != Resource.Loading

            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { polls ->
                        pollAdapter.submitList(polls)
                        scrollToTop()
                    }
                }
                is Resource.Failure -> {
                    showSnackBar(R.string.unknown_error)
                }
                else -> {}
            }
        }
    }

    private fun bindUserDetails() = with(binding) {
        viewModel.getUserDetails()
        viewModel.userDetailsStateFlow.asLiveData().observe(viewLifecycleOwner) { resource ->
            progressIndicator.isVisible = resource == Resource.Loading

            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { user ->
                        addBtn.isVisible = user.admin
                        pollAdapter = PollAdapter(
                            onVote = { pollId, selectedOptions ->
                                viewModel.vote(pollId, selectedOptions)
                            },
                            onClose = { pollId ->
                                viewModel.close(pollId)
                            },
                            isAdmin = user.admin
                        )
                        pollsList.adapter = pollAdapter
                        bindPolls()
                    }
                }
                else -> {}
            }
        }
    }


    private fun scrollToTop() {
        binding.pollsList.smoothScrollToPosition(0)
    }

    private fun showSnackBar(stringId: Int) {
        Snackbar.make(
            binding.layout,
            getString(stringId),
            Snackbar.LENGTH_LONG
        )
            .show()
    }
}