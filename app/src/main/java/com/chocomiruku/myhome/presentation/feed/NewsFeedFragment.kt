package com.chocomiruku.myhome.presentation.feed

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
import com.chocomiruku.myhome.databinding.NewsFeedFragmentBinding
import com.chocomiruku.myhome.util.UserRole
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFeedFragment : Fragment() {

    private var _binding: NewsFeedFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsFeedViewModel by viewModels()

    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewsFeedFragmentBinding.inflate(inflater, container, false)

        bindUserDetails()

        binding.addBtn.setOnClickListener {
            this.findNavController()
                .navigate(
                    NewsFeedFragmentDirections.actionNewsFeedFragmentToAddEditNewsFragment(
                        null
                    )
                )
        }

        return binding.root
    }

    private fun bindNews() = with(binding) {
        viewModel.getNews()
        viewModel.newsStateFlow.asLiveData().observe(viewLifecycleOwner) { resource ->
            progressIndicator.isVisible = resource == Resource.Loading
            newsList.isVisible = resource != Resource.Loading

            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { news ->
                        newsAdapter.submitList(news)
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
                        addBtn.isVisible = user.role != UserRole.DEFAULT
                        newsAdapter = NewsAdapter(
                            specialRights = user.role != UserRole.DEFAULT,
                            onDelete = { newsId ->
                                viewModel.deleteNews(newsId)
                                scrollToTop()
                            }
                        )
                        newsList.adapter = newsAdapter
                        bindNews()
                    }
                }
                else -> {}
            }
        }
    }

    private fun scrollToTop() {
        binding.newsList.smoothScrollToPosition(0)
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