package com.chocomiruku.myhome.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.databinding.UserProfileFragmentBinding
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.util.UserRole
import com.google.android.material.snackbar.Snackbar
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

        val user = UserProfileFragmentArgs.fromBundle(requireArguments()).user
        bindUserDetails(user?.uid)

        binding.editBtn.setOnClickListener {
            (viewModel.userDetailsStateFlow.value as Resource.Success).data?.let { user ->
                this.findNavController().navigate(
                    UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment(
                        user
                    )
                )
            }

        }

        binding.changeRoleBtn.setOnClickListener {
            viewModel.changeRole()
        }

        return binding.root
    }

    private fun bindRole(user: User) = with(binding) {
        viewModel.currentRoleStateFlow.asLiveData().observe(viewLifecycleOwner) { role ->
            if (role == UserRole.ADMIN) {
                changeRoleBtn.isVisible =
                    user.uid != viewModel.currentUserId

                changeRoleBtn.text =
                    if (user.role == UserRole.MODERATOR) getString(R.string.remove_moderator_rights) else
                        getString(R.string.set_moderator_rights)
            }
        }

        viewModel.roleChanged.asLiveData().observe(viewLifecycleOwner) {
            if (it == true) {
                showSnackBar(R.string.role_changed)
            }
        }
    }

    private fun bindUserDetails(uid: String?) = with(binding) {
        viewModel.getUserDetails(uid)
        viewModel.userDetailsStateFlow.asLiveData().observe(viewLifecycleOwner) { resource ->
            userDetailsGroup.isVisible = resource != Resource.Loading
            progressIndicator.isVisible = resource == Resource.Loading

            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { user ->
                        nameText.text = user.name
                        emailText.text = user.email
                        contractText.text = user.contractNumber

                        defaultImageText.isVisible = user.imageUri == null

                        if (user.imageUri != null) {
                            profileImage.setBackgroundColor(
                                getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            loadImage(user.imageUri)
                        } else {
                            profileImage.setBackgroundColor(user.defaultColorId)
                            defaultImageText.text = user.name.first().uppercase()
                            progressIndicator.hide()
                            userDetailsGroup.isVisible = true
                        }
                        currentUserGroup.isVisible = user.uid == viewModel.currentUserId
                        bindRole(user)
                    }
                }
                is Resource.Failure -> {
                    showSnackBar(R.string.unknown_error)
                }
                else -> {}
            }
        }
    }

    private fun loadImage(uriString: String) = with(binding) {
        val uri = uriString.toUri()

        Glide.with(profileImage.context)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_anim)
                    .error(R.drawable.ic_broken_image)
            )
            .into(profileImage)
    }

    private fun showSnackBar(stringId: Int) {
        Snackbar.make(
            binding.layout,
            getString(stringId),
            Snackbar.LENGTH_LONG
        )
            .show()
    }
}