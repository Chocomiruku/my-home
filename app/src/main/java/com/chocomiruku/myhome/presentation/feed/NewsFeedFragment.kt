package com.chocomiruku.myhome.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.NewsFeedFragmentBinding

class NewsFeedFragment : Fragment() {

    private var _binding: NewsFeedFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewsFeedFragmentBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {
            this.findNavController().navigate(NewsFeedFragmentDirections.actionNewsFeedFragmentToAddEditNewsFragment())
        }

        return binding.root
    }
}