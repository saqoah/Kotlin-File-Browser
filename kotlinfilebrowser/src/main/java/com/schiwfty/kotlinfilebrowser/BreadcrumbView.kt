package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.breadcrumb.view.*
import java.io.File

/**
 * Created by arran on 30/06/2017.
 */
class BreadcrumbView : LinearLayout {
    lateinit var file: File
    val textVeiw: TextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.breadcrumb, this)
        textVeiw = directory
    }

    fun bind(file: File) {
        this.file = file
        textVeiw.text = file.name
    }
}