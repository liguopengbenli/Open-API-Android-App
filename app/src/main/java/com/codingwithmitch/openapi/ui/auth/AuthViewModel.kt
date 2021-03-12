package com.codingwithmitch.openapi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.auth.network_responses.LoginResponse
import com.codingwithmitch.openapi.auth.network_responses.RegistrationResponse
import com.codingwithmitch.openapi.model.AuthToken
import com.codingwithmitch.openapi.repository.auth.AuthRepository
import com.codingwithmitch.openapi.ui.BaseViewModel
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.state.AuthStateEvent.*
import com.codingwithmitch.openapi.ui.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.state.LoginFields
import com.codingwithmitch.openapi.ui.auth.state.RegistrationFields
import com.codingwithmitch.openapi.util.AbsentLiveData
import com.codingwithmitch.openapi.util.GenericApiResponse
import javax.inject.Inject
import kotlin.math.log

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository

) : BaseViewModel<AuthStateEvent, AuthViewState>()
{

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent){
            is LoginAttemptEvent -> {
                return AbsentLiveData.create()
            }
            is RegisterAttemptEvent ->{
                return AbsentLiveData.create()
            }

            is CheckPreviousAuthEvent ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationField(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }

        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginField(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }


    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

}