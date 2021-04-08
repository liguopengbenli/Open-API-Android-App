package com.codingwithmitch.openapi.ui.main.blog.state

sealed class BlogStateEvent{
    class BlogSearchEvent: BlogStateEvent()

    class checkAuthorOfBlogPost: BlogStateEvent()

    class DeleteBlogPostEvent: BlogStateEvent()

    class None: BlogStateEvent()

}