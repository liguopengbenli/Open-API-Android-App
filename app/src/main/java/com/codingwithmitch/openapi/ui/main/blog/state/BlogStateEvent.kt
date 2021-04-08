package com.codingwithmitch.openapi.ui.main.blog.state

import okhttp3.MultipartBody

sealed class BlogStateEvent{
    class BlogSearchEvent: BlogStateEvent()

    class checkAuthorOfBlogPost: BlogStateEvent()

    class DeleteBlogPostEvent: BlogStateEvent()

    data class UpdateBlogPostEvent(
        var title: String,
        var body: String,
        val image: MultipartBody.Part?
    ): BlogStateEvent()

    class None: BlogStateEvent()

}