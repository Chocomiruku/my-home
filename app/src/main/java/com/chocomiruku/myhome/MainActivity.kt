package com.chocomiruku.myhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.chocomiruku.myhome.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            val isUnsignedScreen = nd.id == R.id.splashFragment || nd.id == R.id.signInFragment || nd.id == R.id.signUpFragment
            binding.topAppBar.isGone = isUnsignedScreen
            binding.bottomNavigation.isGone = isUnsignedScreen

            val canNavigateBack = nd.id == R.id.editProfileFragment || nd.id == R.id.userProfileFragment || nd.id == R.id.addEditNewsFragment
            if (canNavigateBack) {
                binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            } else binding.topAppBar.navigationIcon = null
        }

        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}