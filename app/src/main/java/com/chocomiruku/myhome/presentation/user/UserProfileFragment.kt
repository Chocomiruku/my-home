package com.chocomiruku.myhome.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.UserProfileFragmentBinding
import com.chocomiruku.myhome.util.generateRandomColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: UserProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false)

        bindUserDetails()

        binding.editBtn.setOnClickListener {
            viewModel.userDetailsStateFlow.value?.let { user ->
                this.findNavController()
                    .navigate(
                        UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment(
                            user
                        )
                    )
            }
        }

        return binding.root
    }

    private fun bindUserDetails() {
        viewModel.userDetailsStateFlow.asLiveData().observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.nameText.text = user.name
                binding.emailText.text = user.email

                if (user.profilePicPath != null) {
                    // TODO: Glide
                } else {
                    binding.userProfilePic.setBackgroundColor(
                        getColor(
                            requireContext(),
                            generateRandomColor()
                        )
                    )
                    binding.defaultPicText.text = user.name.first().uppercase()
                }
            }
        }
    }
}