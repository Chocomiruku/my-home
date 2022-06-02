package com.chocomiruku.myhome.presentation.user_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment() {

    private var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProfileFragmentBinding.inflate(inflater, container, false)

        binding.saveBtn.setOnClickListener {
            this.findNavController().navigateUp()
        }

        return binding.root
    }
}