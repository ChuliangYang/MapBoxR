package com.demo.cl.mapbox

import androidx.appcompat.app.AppCompatActivity
import com.demo.cl.mapbox.di.base.AutoInject

open class BaseActivity:AppCompatActivity()

open class BaseDaggerActivity:BaseActivity(), AutoInject

