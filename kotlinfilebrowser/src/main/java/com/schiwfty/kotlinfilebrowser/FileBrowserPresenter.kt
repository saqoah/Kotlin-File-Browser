package com.schiwfty.kotlinfilebrowser

import android.content.Context

/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserPresenter: FileBrowserContract.Presenter {
    lateinit var context: Context
    lateinit var view: FileBrowserContract.View
    override fun setup(context: Context, view: FileBrowserContract.View) {
        this.context = context
        this.view = view
    }

    override fun reload() {

    }


}