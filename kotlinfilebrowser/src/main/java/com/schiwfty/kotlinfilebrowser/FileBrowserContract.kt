package com.schiwfty.kotlinfilebrowser

import android.content.Context
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
interface FileBrowserContract {
    interface View {
        fun showFileList(files: List<File>)
        fun showError(stringId: Int)
        fun setToolbarTitle(title: String)
        fun showNoFilesView()
        fun showExternalFolders()
        fun notifyFileSelected(file: File)
        fun setupBreadcrumbTrail(rootFile: File, file: File)
        fun setToolbarTitle(stringRes: Int)
        fun showAddFileDialog()
        fun showAddFolderDialog()
        fun showRenameFileDialog(file: File)
        fun showDeleteFileDialog(file: File)
    }

    interface Presenter {
        fun setup(context: Context, view: FileBrowserContract.View, isFolderSelectMode: Boolean)
        fun reload()
        fun goUpADirectory()
        fun isAtRoot(): Boolean
        fun notifyBreadcrumbSelected(file: File)
        fun createFileAtCurrentDirectory(name: String)
        fun createFolderAtCurrentDirectory(name: String)
        fun performFileAction(file: File, action: FileBrowserAdapter.FILE_ACTION)
        fun renameFile(file: File, name: String)
        fun deleteFile(file: File)
        fun getCurrentFile(): File
        fun setCurrentFile(file: File)
        fun setNewRootDirectory(file: File)
        fun getExternalFolders(): List<File>
    }
}