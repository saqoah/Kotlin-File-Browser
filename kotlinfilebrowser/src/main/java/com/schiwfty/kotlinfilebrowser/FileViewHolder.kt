package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_file.view.*
import java.io.File


/**
 * Created by arran on 26/06/2017.
 */
class FileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val icon: ImageView = view.icon
    val name: TextView = view.name
    val info: TextView = view.extra_info
    fun bind(file: File, context: Context) {
        name.text = file.name
        if (file.isDirectory) {
            (icon as ImageView).setImageResource(R.drawable.ic_folder_black_24dp)
            if (file.listFiles() != null) {
                when (file.listFiles().size) {
                    1 -> info.text = context.getText(R.string.single_item)
                    else -> info.text = context.getString(R.string.items, file.listFiles().size ?: 0)
                }
            }
        } else {
            val splitMime = file.getMimeType().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val fileType = splitMime[0].toLowerCase()
            val fileFormat = splitMime[1].toLowerCase()
            when (fileType) {
                "video" -> {
                    setImage(Uri.fromFile(file), context)
                    setPlaceholder(R.drawable.ic_videocam_black_24dp, context)
                }
                "image" -> {
                    setImage(Uri.fromFile(file), context)
                    setPlaceholder(R.drawable.ic_image_black_24dp, context)
                }
                "audio" -> (icon as ImageView).setImageResource(R.drawable.ic_audiotrack_black_24dp)
                "text" -> (icon as ImageView).setImageResource(R.drawable.ic_text_fields_black_24dp)
                "application" -> {
                    when (fileFormat) {
                        "pdf" -> (icon as ImageView).setImageResource(R.drawable.ic_pdf_black_24dp)
                        else -> (icon as ImageView).setImageResource(R.drawable.ic_file_black_24dp)
                    }
                }
                else -> (icon as ImageView).setImageResource(R.drawable.ic_file_black_24dp)
            }
            info.text = file.length().formatBytesAsSize()
        }


    }

    fun setPlaceholder(resId: Int, context: Context) {
        icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, resId, null))
    }

    fun setImage(uri: Uri, context: Context) {
        Glide.with(context)
                .load(uri)
                .into(icon)
    }
}
