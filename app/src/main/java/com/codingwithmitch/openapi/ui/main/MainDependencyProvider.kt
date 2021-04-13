package com.codingwithmitch.openapi.ui.main

import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory

interface MainDependencyProvider{
    fun getVMProviderFactory(): ViewModelProviderFactory

    fun getGlideRequestManager(): RequestManager
}