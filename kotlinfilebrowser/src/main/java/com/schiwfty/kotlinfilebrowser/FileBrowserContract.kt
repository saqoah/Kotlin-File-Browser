package com.schiwfty.kotlinfilebrowser

import android.content.Context

/**
 * Created by arran on 24/06/2017.
 */
interface FileBrowserContract {
    interface View{

    }

    interface Presenter{
        fun setup(context: Context, view: FileBrowserContract.View)
        fun reload()
    }
}