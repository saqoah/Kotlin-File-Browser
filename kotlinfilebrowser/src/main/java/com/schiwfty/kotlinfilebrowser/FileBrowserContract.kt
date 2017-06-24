package com.schiwfty.kotlinfilebrowser

import android.content.Context
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
interface FileBrowserContract {
    interface View{
        fun updateView(files: List<File>)
    }

    interface Presenter{
        fun setup(context: Context, view: FileBrowserContract.View)
        fun reload()
        fun fileClicked(file: File)
    }
}