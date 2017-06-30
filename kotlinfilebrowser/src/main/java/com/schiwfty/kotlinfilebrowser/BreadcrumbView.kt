package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.breadcrumb.view.*

/**
 * Created by arran on 30/06/2017.
 */
class BreadcrumbView: LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) :this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        View.inflate(context, R.layout.breadcrumb, this)
    }

    fun render(filename: String){
        directory.text = filename
    }


}