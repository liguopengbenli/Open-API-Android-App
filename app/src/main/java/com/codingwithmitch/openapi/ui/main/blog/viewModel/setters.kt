package com.codingwithmitch.openapi.ui.main.blog.viewModel

import android.net.Uri
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


fun BlogViewModel.removeDeletedBlogPost(){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()

    for (i in 0..(list.size-1)){
        if (list[i] == getBlogPost()){
            list.remove(getBlogPost())
            break
        }
    }
    setBlogListData(list)
}


fun BlogViewModel.setUpdatedBlogFields(
    title: String?,
    body: String?,
    uri: Uri?
){
    val update = getCurrentViewStateOrNew()
    val updateBlogFields = update.updateBlogFields
    title?.let { updateBlogFields.updatedBlogTitle = it }
    body?.let { updateBlogFields.updatedBlogBody = it }
    uri?.let { updateBlogFields.updatedImageUri = it }

    update.updateBlogFields = updateBlogFields
    setViewState(update)

}


fun BlogViewModel.updateListItem(newBlogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for (i in 0..(list.size-1)){
        if(list[i].pk == newBlogPost.pk){
            list[i] = newBlogPost
            break
        }
    }
    update.blogFields.blogList = list
    setViewState(update)
}

fun BlogViewModel.onBlogPostUpdateSuccess(blogPost: BlogPost){
    setUpdatedBlogFields(
        uri = null,
        title = blogPost.title,
        body = blogPost.body
    )// update UpdateBlogFragment
    setBlogPost(blogPost) // update ViewblogFragment
    updateListItem(blogPost)
}