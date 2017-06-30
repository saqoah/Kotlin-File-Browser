package com.schiwfty.kotlinfilebrowser

import android.content.Context
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserPresenter: FileBrowserContract.Presenter {
    lateinit var currentFile: File

    lateinit var context: Context
    lateinit var view: FileBrowserContract.View
    override fun setup(context: Context, view: FileBrowserContract.View, file: File) {
        currentFile = file
        this.context = context
        this.view = view
    }

    override fun reload() {
        if(currentFile.listFiles()!=null) {
            if(currentFile.listFiles().isEmpty()) view.showNoFilesView()
            else{
                view.showFileList(currentFile.listFiles().toList())
                view.setToolbarTitle(currentFile.name)
            }
        }
        else {
            view.showError(R.string.error_accessing_file)
            view.showNoFilesView()
        }
        view.setupBreadcrumbTrail(currentFile)

//        if(currentFile.parentFile == null) view.setUpDirectoryVisible(false)
//        else view.setUpDirectoryVisible(true)
    }

    override fun fileClicked(file: File) {
        currentFile = file
        if(file.isDirectory) {
            reload()
        }
        else view.notifyFileSelected(file)
    }

    override fun goUpADirectory() {
        val newFile = currentFile.parentFile
        if(newFile!=null){
            currentFile = newFile
            reload()
        }
    }

}