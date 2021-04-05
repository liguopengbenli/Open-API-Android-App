package com.codingwithmitch.openapi.ui.main.blog.state

import android.app.DownloadManager
import com.codingwithmitch.openapi.model.BlogPost

data class BlogViewState(
    //BLogFragment vars
    var blogFields: BlogFields = BlogFields(),

    //viewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields()

    //UpdateBlogFragment vars
){
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    )




}