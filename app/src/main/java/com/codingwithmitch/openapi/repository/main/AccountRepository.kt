package com.codingwithmitch.openapi.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.codingwithmitch.openapi.auth.main.OpenApiMainService
import com.codingwithmitch.openapi.model.AccountProperties
import com.codingwithmitch.openapi.model.AuthToken
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.main.account.state.AccountViewState
import com.codingwithmitch.openapi.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository
    @Inject
    constructor(
        val openApiMainService: OpenApiMainService,
        val accountPropertiesDao: AccountPropertiesDao,
        val sessionManager: SessionManager
    )

{
    private val TAG: String = "AppDebug"

    private var repositoryJOb: Job? = null

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>>{
        return object: NetworkBoundResource<AccountProperties, AccountProperties , AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
                withContext(Main){
                    result.addSource(loadFromCache()){
                        viewState->
                        onCompleteJob(
                            DataState.data(
                                viewState,
                                null
                            )
                        )
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<AccountProperties>) {
                updateLocalDb(response.body)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<AccountProperties>> {
                return openApiMainService.getAccountProperties("Token ${authToken.token}")
            }

            override fun setJob(job: Job) {
                repositoryJOb?.cancel()
                repositoryJOb = job
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPk(authToken.account_pk!!).switchMap {
                    object : LiveData<AccountViewState>(){
                        override fun onActive() {
                            super.onActive()
                            value = AccountViewState(it)
                        }
                    }
                }
            }

            override suspend fun updateLocalDb(cacheObject: AccountProperties?) {
                cacheObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        cacheObject.pk,
                        cacheObject.email,
                        cacheObject.username
                    )
                }
            }

        }.asLiveData()
    }

    fun cancelActiveJobs(){
        Log.d(TAG, "AuthRepository: cancelActiveJobs")
        repositoryJOb?.cancel()
    }

}