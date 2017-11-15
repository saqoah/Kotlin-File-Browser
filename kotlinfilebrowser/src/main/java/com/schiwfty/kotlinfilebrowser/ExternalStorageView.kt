package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_external_storage.view.*
import java.io.File

/**
 * Created by arran on 14/11/2017.
 */

class ExternalStorageView : LinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.view_external_storage, this)
    }

    fun init(file: File) {
        driveName.text = file.absolutePath
        freeSpace.text = file.freeSpace.formatBytesAsSize()
        if (file.absolutePath == "/storage/emulated/0") icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_internal_storage, null))
        else icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_sd_storage, null))
    }
}