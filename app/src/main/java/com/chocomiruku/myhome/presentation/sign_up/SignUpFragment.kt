package com.chocomiruku.myhome.presentation.sign_up

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
import com.chocomiruku.myhome.databinding.SignUpFragmentBinding
import com.chocomiruku.myhome.util.AuthState
import com.chocomiruku.myhome.util.isContractNumberValid
import com.chocomiruku.myhome.util.isEmailValid
import com.chocomiruku.myhome.util.isPasswordValid
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        bindAuthState()

        binding.signUpBtn.setOnClickListener {
            if (checkInput()) {
                viewModel.onSignUp(
                    binding.nameEditText.text.toString(),
                    binding.contractEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        return binding.root
    }

    private fun bindAuthState() {
        viewModel.authStateFlow.asLiveData().observe(viewLifecycleOwner) { authState ->
            binding.progressIndicator.isVisible = authState == AuthState.LOADING
            when (authState) {
                AuthState.SIGN_UP_SUCCESS -> {
                    showSnackBar(R.string.sign_up_success)
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToUserProfileFragment()
                    )
                }
                AuthState.SIGN_UP_ERROR -> showSnackBar(R.string.sign_up_error)
                AuthState.SIGN_UP_CONTRACT_ERROR -> showSnackBar(R.string.contract_not_exist)
                else -> {}
            }
        }
    }

    private fun checkInput(): Boolean = with(binding) {
        if (nameEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_name)
            return false
        }
        if (contractEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_contract)
            return false
        }
        if (!isContractNumberValid(contractEditText.text.toString().trim())) {
            showSnackBar(R.string.contract_number_invalid)
            return false
        }
        if (emailEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_email)
            return false
        }
        if (!isEmailValid(emailEditText.text.toString().trim())) {
            showSnackBar(R.string.email_invalid)
            return false
        }
        if (passwordEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_password)
            return false
        }
        if (!isPasswordValid(passwordEditText.text.toString().trim())) {
            showSnackBar(R.string.password_invalid)
            return false
        }
        return true
    }

    private fun showSnackBar(stringId: Int) {
        val snackBar = Snackbar.make(
            binding.signUpBtn,
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