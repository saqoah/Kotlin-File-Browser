package com.schiwfty.kotlinfilebrowser

import android.content.Context
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
        fun setUpDirectoryVisible(visible: Boolean)
    }

    interface Presenter{
        fun setup(context: Context, view: FileBrowserContract.View, file: File)
        fun reload()
        fun fileClicked(file: File)
        fun goUpADirectory()
    }
}