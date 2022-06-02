package com.chocomiruku.myhome.presentation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.AddEditNewsFragmentBinding

class AddEditNewsFragment : Fragment() {

    private var _binding: AddEditNewsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEditNewsFragmentBinding.inflate(inflater, container, false)

        binding.publishBtn.setOnClickListener {
            this.findNavController().navigateUp()
        }

        return binding.root
    }
}