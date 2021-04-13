package com.codingwithmitch.openapi.ui.main.blog.state

import android.app.DownloadManager
import android.net.Uri
import android.os.Parcelable
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.codingwithmitch.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.android.parcel.Parcelize

const val BLOG_VIEW_STATE_BUNDLE_KEY = "com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState"

@Parcelize
data class BlogViewState(
    //BLogFragment vars
    var blogFields: BlogFields = BlogFields(),

    //viewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),

    //UpdateBlogFragment vars
    var updateBlogFields: UpdateBlogFields = UpdateBlogFields()
) :Parcelable

{
    @Parcelize
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = ORDER_BY_ASC_DATE_UPDATED, //date_update
        var order: String = BLOG_ORDER_ASC //""
    ) : Parcelable

    @Parcelize
    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    ) : Parcelable

    @Parcelize
    data class UpdateBlogFields(
        var updatedBlogTitle: String? = null,
        var updatedBlogBody: String? = null,
        var updatedImageUri: Uri? = null
    ) : Parcelable


}