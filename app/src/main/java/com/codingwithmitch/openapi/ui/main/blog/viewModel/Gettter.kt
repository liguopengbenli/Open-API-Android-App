package com.codingwithmitch.openapi.ui.main.blog.viewModel


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






