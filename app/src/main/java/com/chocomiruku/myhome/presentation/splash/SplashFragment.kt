package com.chocomiruku.myhome.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.SplashFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)

        showAnimationAndNavigate()

        return binding.root
    }

    private fun showAnimationAndNavigate() {
        val slideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.side_slide)

        slideAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                navigate()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.splashIcon.startAnimation(slideAnimation)
    }

    private fun navigate() {
        when (viewModel.isSignedIn) {
            true -> {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNewsFeedFragment())
            }
            else -> {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}