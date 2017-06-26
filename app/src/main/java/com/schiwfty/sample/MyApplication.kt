package com.schiwfty.sample

import android.app.Application
import com.schiwfty.kotlinfilebrowser.KotlinFileBrowser

/**
 * Created by arran on 25/06/2017.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KotlinFileBrowser.install(this)
    }
}