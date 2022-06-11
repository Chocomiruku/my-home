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
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.SignInFragmentBinding
import com.chocomiruku.myhome.util.AuthState
import com.chocomiruku.myhome.util.isEmailValid
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
            if (checkInput()) {
                viewModel.onSignIn(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        binding.getAccessBtn.setOnClickListener {
            findNavController()
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
                AuthState.SIGN_IN_ERROR -> showSnackBar(R.string.sign_in_failed)
                else -> {}
            }
        }
    }

    private fun checkInput(): Boolean = with(binding) {
        if (emailEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_email)
            return false
        }
        if (passwordEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_password)
            return false
        }
        if (!isEmailValid(emailEditText.text.toString().trim())) {
            showSnackBar(R.string.email_invalid)
            return false
        }
        return true
    }

    private fun showSnackBar(stringId: Int) {
        val snackBar = Snackbar.make(
            binding.signInBtn,
            getString(stringId),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.action_ok) {
        }
        snackBar.setTextMaxLines(SNACKBAR_MAX_LINES)
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val SNACKBAR_MAX_LINES = 5
    }
}