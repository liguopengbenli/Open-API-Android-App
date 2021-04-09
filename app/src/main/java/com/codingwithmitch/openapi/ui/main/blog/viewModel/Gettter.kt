package com.codingwithmitch.openapi.ui.main.blog.viewModel

import android.net.Uri
import com.codingwithmitch.openapi.model.BlogPost


fun BlogViewModel.getFilter(): String{
    getCurrentViewStateOrNew().let {
        return it.blogFields.filter
    }
}

fun BlogViewModel.getOrder(): String{
    getCurrentViewStateOrNew().let {
        return it.blogFields.order
    }
}


fun BlogViewModel.getSearchQuery(): String{
    getCurrentViewStateOrNew().let {
        return it.blogFields.searchQuery
    }
}

fun BlogViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.blogFields.page
    }
}

fun BlogViewModel.getIsQueryExhausted(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryExhausted
    }
}

fun BlogViewModel.getIsQueryInProgress(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryInProgress
    }
}


fun BlogViewModel.getSlug(): String{
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

fun BlogViewModel.isAuthorOfBlogPost(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.isAuthorOfBlogPost
    }
}

fun BlogViewModel.getBlogPost(): BlogPost{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        }?: getDummyBlogPost()
    }
}

fun getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "","","","",1,"")
}

fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updateBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}








