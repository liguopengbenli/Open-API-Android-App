package com.codingwithmitch.openapi.ui.auth

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.repository.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository

) : ViewModel()
{


}