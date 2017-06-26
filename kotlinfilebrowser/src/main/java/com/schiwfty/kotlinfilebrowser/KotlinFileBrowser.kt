package com.schiwfty.kotlinfilebrowser

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by arran on 25/06/2017.
 */
object KotlinFileBrowser {
    fun install(application: Application){
        Fresco.initialize(application)
    }
}