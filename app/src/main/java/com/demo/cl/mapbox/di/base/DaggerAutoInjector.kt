package com.demo.cl.mapbox.di.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.demo.cl.mapbox.MapApplication
import com.demo.cl.mapbox.di.DaggerAppComponent
import dagger.android.AndroidInjection


object DaggerAutoInjector {
    fun inject(application: MapApplication) {
        DaggerAppComponent.builder().application(application)
            .build().inject(application)
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {

                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

                }

                override fun onActivityDestroyed(activity: Activity) {

                }
            })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is AutoInject) {
            AndroidInjection.inject(activity)
        }
//        if (activity is FragmentActivity) {
//            activity.supportFragmentManager
//                .registerFragmentLifecycleCallbacks(
//                    object : FragmentManager.FragmentLifecycleCallbacks() {
//                        override fun onFragmentCreated(
//                            fm: FragmentManager,
//                            f: Fragment,
//                            savedInstanceState: Bundle?
//                        ) {
//                            if (f is Injectable) {
//                                AndroidSupportInjection.inject(f)
//                            }
//                        }
//                    }, true
//                )
//        }
    }
}
