package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
interface FileBrowserContract {
    interface View{
        fun showFileList(files: List<File>)
        fun showError(stringId: Int)
        fun setToolbarTitle(title: String)
        fun showNoFilesView()
        fun notifyFileSelected(file: File)
        fun setupBreadcrumbTrail(file: File)
        fun setToolbarTitle(stringRes: Int)
        fun showAddFileDialog()
        fun showAddFolderDialog()
        fun showRenameFileDialog(file: File)
    }

    interface Presenter{
        val rootDirectory: File
        fun setup(context: Context, view: FileBrowserContract.View, file: File)
        fun reload()
        fun goUpADirectory()
        fun isAtRoot(): Boolean
        fun notifyBreadcrumbSelected(file:File)
        fun createFileAtCurrentDirectory(name: String)
        fun createFolderAtCurrentDirectory(name: String)
        fun performFileAction(file: File, action: FileBrowserAdapter.FILE_ACTION)
        fun  renameFile(file: File, name: String)
    }
}