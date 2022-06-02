package com.chocomiruku.myhome.presentation.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.AddEditNewsFragmentBinding
import com.chocomiruku.myhome.databinding.UserProfileFragmentBinding

class UserProfileFragment : Fragment() {

    private var _binding: UserProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false)

        binding.editBtn.setOnClickListener {
            this.findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment())
        }

        return binding.root
    }
}