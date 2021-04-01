package com.codingwithmitch.openapi.ui.main.blog.state

import android.app.DownloadManager
import com.codingwithmitch.openapi.model.BlogPost

data class BlogViewState(
    //BLogFragment vars
    var blogFields: BlogFields = BlogFields()

    //viewBlogFragment vars

    //UpdateBlogFragment vars
){
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = ""
    )


}