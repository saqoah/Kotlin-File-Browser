package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserPresenter : FileBrowserContract.Presenter {
    override fun createFileAtCurrentDirectory(name: String) {
        val newFile = File(currentFile.absolutePath, name)
        if (!newFile.exists()) {
            if (currentFile.canWrite()) {
                try {
                    newFile.createNewFile()
                } catch (e: Exception) {
                    view.showError(R.string.error_creating_file)
                    e.printStackTrace()
                }
            } else {
                view.showError(R.string.cannot_create_File)
            }
        } else {
            view.showError(R.string.file_already_exists)
        }
    }

    override fun createFolderAtCurrencyDirecory(name: String) {

    }

    override val rootDirectory: File
        get() = Environment.getExternalStorageDirectory()

    lateinit var currentFile: File

    lateinit var context: Context
    lateinit var view: FileBrowserContract.View

    override fun setup(context: Context, view: FileBrowserContract.View, file: File) {
        currentFile = file
        this.context = context
        this.view = view
    }

    override fun isAtRoot(): Boolean {
        return currentFile == Environment.getExternalStorageDirectory()
    }

    override fun reload() {
        if (currentFile.listFiles() != null) {
            if (currentFile.listFiles().isEmpty()) view.showNoFilesView()
            else {
                view.showFileList(currentFile.listFiles().toList())
            }
        } else {
            view.showError(R.string.error_accessing_file)
            view.showNoFilesView()
        }
        if (isAtRoot()) view.setToolbarTitle(R.string.root)
        else view.setToolbarTitle(currentFile.name)
        view.setupBreadcrumbTrail(currentFile)
    }

    override fun fileClicked(file: File) {
        currentFile = file
        if (file.isDirectory) {
            reload()
        } else view.notifyFileSelected(file)
    }

    override fun goUpADirectory() {
        val newFile = currentFile.parentFile
        if (newFile != null) {
            currentFile = newFile
            reload()
        }
    }

    override fun notifyBreadcrumbSelected(file: File) {
        currentFile = file
        reload()
    }
}