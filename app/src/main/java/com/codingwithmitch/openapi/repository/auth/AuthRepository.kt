package com.codingwithmitch.openapi.repository.auth

import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.auth.OpenApiAuthService
import com.codingwithmitch.openapi.auth.network_responses.LoginResponse
import com.codingwithmitch.openapi.auth.network_responses.RegistrationResponse
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.util.GenericApiResponse

class AuthRepository constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {
    fun testLoginRequest(email: String, password: String): LiveData<GenericApiResponse<LoginResponse>>{
        return openApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<GenericApiResponse<RegistrationResponse>>{
        return openApiAuthService.register(email, username, password, confirmPassword)
    }

}