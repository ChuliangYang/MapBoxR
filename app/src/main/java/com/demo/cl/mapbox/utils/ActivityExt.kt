package com.demo.cl.mapbox.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.submitFragment(contentId:Int, fragment:Fragment, tag:String?=null){
    supportFragmentManager.beginTransaction().apply {
        add(contentId,fragment,tag)
        commit()
    }
}