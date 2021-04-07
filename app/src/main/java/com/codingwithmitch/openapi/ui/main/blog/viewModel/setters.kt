package com.codingwithmitch.openapi.ui.main.blog.viewModel

import com.codingwithmitch.openapi.model.BlogPost

/*
* Use kotlin extension function to keep thing organised
* use a separate file to contain all the setters
* */

fun BlogViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    //if(query.equals(update.blogFields.searchQuery)){
    //  return
    //}
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun BlogViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

fun BlogViewModel.setBlogPost(blogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)
}

fun BlogViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
    setViewState(update)
}

fun BlogViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun BlogViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun BlogViewModel.setBlogFiter(filter: String?){
   filter?.let {
       val update = getCurrentViewStateOrNew()
       update.blogFields.filter = filter
       setViewState(update)
   }
}


fun BlogViewModel.setBlogOrder(order: String){
   val update = getCurrentViewStateOrNew()
    update.blogFields.order = order
    setViewState(update)
}


