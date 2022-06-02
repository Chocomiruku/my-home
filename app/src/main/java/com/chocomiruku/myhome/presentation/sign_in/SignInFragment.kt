package com.chocomiruku.myhome.presentation.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.SignInFragmentBinding

class SignInFragment : Fragment() {

    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)

        binding.signInBtn.setOnClickListener {
            this.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToNewsFeedFragment())
        }

        binding.getAccessBtn.setOnClickListener {
            this.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        return binding.root
    }
}