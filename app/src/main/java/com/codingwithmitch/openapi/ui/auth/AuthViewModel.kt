package com.codingwithmitch.openapi.ui.auth

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.repository.AuthRepository

class AuthViewModel constructor(
    val authRepository: AuthRepository

) : ViewModel()
{


}