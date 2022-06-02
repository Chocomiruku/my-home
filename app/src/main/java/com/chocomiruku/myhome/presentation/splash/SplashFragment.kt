package com.chocomiruku.myhome.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.SplashFragmentBinding

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)

        binding.splashIcon.setOnClickListener {
            this.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
        }

        return binding.root
    }
}