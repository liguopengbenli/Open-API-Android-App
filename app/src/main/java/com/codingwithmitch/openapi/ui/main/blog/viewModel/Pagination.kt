package com.codingwithmitch.openapi.ui.main.blog.viewModel

import android.util.Log
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent.*
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState

fun BlogViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

fun BlogViewModel.refreshFromCache(){
    setQueryInProgress(true)
    setQueryExhausted(false)
    setStateEvent(RestoreBlogListFromCache())
}

fun BlogViewModel.loadFirstPage(){
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
    //Log.e(TAG, "lig BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")

}

fun BlogViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page
    update.blogFields.page = page + 1
    setViewState(update)
}

fun BlogViewModel.nextPage(){
    if(!getIsQueryExhausted() && !getIsQueryInProgress()){
        Log.d(TAG, "BlogViewModel: Attempting to load next page")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(BlogSearchEvent())
    }
}

fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState){
    setQueryInProgress(viewState.blogFields.isQueryInProgress)
    setQueryExhausted(viewState.blogFields.isQueryExhausted)
    setBlogListData(viewState.blogFields.blogList)
}