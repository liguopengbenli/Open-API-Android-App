package com.codingwithmitch.openapi

import android.app.Activity
import android.app.Application
import com.codingwithmitch.openapi.di.AppInjector
import com.codingwithmitch.openapi.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class BaseApplication: Application(), HasActivityInjector {


   @Inject
   lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }


}