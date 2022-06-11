package com.chocomiruku.myhome

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.chocomiruku.myhome.databinding.ActivityMainBinding
import com.chocomiruku.myhome.domain.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exit -> {
                    authRepo.signOut()
                    navController.navigate(R.id.signInFragment)
                    true
                }
                else -> {
                    true
                }
            }
        }

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            val isUnsignedScreen =
                nd.id == R.id.splashFragment || nd.id == R.id.signInFragment || nd.id == R.id.signUpFragment
            binding.topAppBar.isGone = isUnsignedScreen
            binding.bottomNavigation.isGone = isUnsignedScreen

            if (isUnsignedScreen) {
                window.statusBarColor = getColor(R.color.gray_100)
            } else window.statusBarColor = getColor(R.color.green_800)

            val canNavigateBack =
                nd.id == R.id.editProfileFragment || nd.id == R.id.userProfileFragment || nd.id == R.id.addEditNewsFragment || nd.id == R.id.addPollFragment
            if (canNavigateBack) {
                binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            } else binding.topAppBar.navigationIcon = null
        }

        createNotificationChannel()

        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    private fun createNotificationChannel() {
        val name = "test_channel"
        val descriptionText = "test_channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private companion object {
        private const val CHANNEL_ID = "default_channel_id"
    }
}