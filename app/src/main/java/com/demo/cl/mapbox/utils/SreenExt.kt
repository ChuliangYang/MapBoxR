@file:JvmName("ScreenUtils")

package com.demo.cl.mapbox.utils

import android.content.Context

fun Context.dp2px(dp:Float):Float{
    return dp*resources.displayMetrics.density
}
