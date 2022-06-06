package com.chocomiruku.myhome.presentation.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.SignInFragmentBinding
import com.chocomiruku.myhome.util.AuthState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)

        bindAuthState()

        binding.signInBtn.setOnClickListener {
            viewModel.onSignIn(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString())
        }

        binding.getAccessBtn.setOnClickListener {
            this.findNavController()
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        return binding.root
    }

    private fun bindAuthState() {
        viewModel.authStateFlow.asLiveData().observe(viewLifecycleOwner) { authState ->
            binding.progressIndicator.isVisible = authState == AuthState.LOADING
            when (authState) {
                AuthState.SIGN_IN_SUCCESS -> findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToNewsFeedFragment()
                )
                AuthState.SIGN_IN_ERROR -> showSnackBarError()
                else -> { }
            }
        }
    }

    private fun showSnackBarError() {
        Snackbar.make(
            binding.signInBtn,
            "Ошибка авторизации",
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}