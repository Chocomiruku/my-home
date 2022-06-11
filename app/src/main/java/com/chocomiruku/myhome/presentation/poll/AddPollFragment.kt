package com.chocomiruku.myhome.presentation.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.AddPollFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPollFragment : Fragment() {

    private var _binding: AddPollFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddPollViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPollFragmentBinding.inflate(inflater, container, false)

        binding.publishBtn.setOnClickListener {
            binding.progressIndicator.show()
            if (checkInput()) {
                viewModel.addPoll(
                    title = binding.titleEditText.text.toString(),
                    multipleChoices = binding.multipleChoiceCheckbox.isChecked,
                    binding.option1EditText.text.toString(),
                    binding.option2EditText.text.toString(),
                    binding.option3EditText.text.toString(),
                    binding.option4EditText.text.toString(),
                    binding.option5EditText.text.toString()
                )

                viewModel.navigateUpFlow.asLiveData().observe(viewLifecycleOwner) { navigateUp ->
                    if (navigateUp) {
                        binding.progressIndicator.hide()
                        this.findNavController().navigateUp()
                    }
                }
            }
        }

        return binding.root
    }

    private fun checkInput(): Boolean = with(binding) {
        if (titleEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.title_empty)
            return false
        }
        if (option1EditText.text.toString().trim()
                .isEmpty() && option2EditText.text.toString().trim().isEmpty()
        ) {
            showSnackBar(R.string.enter_options)
            return false
        }
        return true
    }

    private fun showSnackBar(stringId: Int) {
        val snackBar = Snackbar.make(
            binding.layout,
            getString(stringId),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.action_ok) {
        }
        snackBar.setTextMaxLines(SNACKBAR_MAX_LINES)
        snackBar.show()
    }

    private companion object {
        const val SNACKBAR_MAX_LINES = 5
    }
}