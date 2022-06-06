package com.chocomiruku.myhome.presentation.splash

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    auth: FirebaseAuth
) : ViewModel() {

    val isSignedIn = auth.currentUser != null
}