package com.codingwithmitch.openapi.ui.main.blog.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.repository.main.BlogRepository
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.BaseViewModel
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Loading
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent.*
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import com.codingwithmitch.openapi.util.AbsentLiveData
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val requestManager: RequestManager
): BaseViewModel<BlogStateEvent, BlogViewState>(){

    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        when(stateEvent){
            is BlogSearchEvent->{
                return sessionManager.cachedToken.value?.let { authToken ->
                    blogRepository.searchBlogPosts(
                        authToken,
                        getSearchQuery(),
                        getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is checkAuthorOfBlogPost->{
                return AbsentLiveData.create()
            }

            is None->{
                return object : LiveData<DataState<BlogViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        value = DataState(
                            null,
                            Loading(false),
                            null
                        )
                    }
                }
            }
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    fun cancelActiveJobs(){
        blogRepository.cancelActiveJobs()
        handlePendingData()
    }

    private fun handlePendingData() {
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}