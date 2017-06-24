package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.os.Environment
import java.io.File

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
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).parentFile
        view.updateView(file.listFiles().toList())
    }

    override fun fileClicked(file: File) {
        if(file.isDirectory) view.updateView(file.listFiles().toList())
    }

}