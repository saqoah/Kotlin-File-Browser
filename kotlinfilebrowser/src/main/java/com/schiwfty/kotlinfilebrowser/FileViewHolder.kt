package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.list_item_file.view.*
import java.io.File


/**
 * Created by arran on 26/06/2017.
 */
class FileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val icon: SimpleDraweeView = view.icon
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
                    setImage(Uri.fromFile(file))
                    setPlaceholder(R.drawable.ic_videocam_black_24dp, context)
                }
                "image" -> {
                    setImage(Uri.fromFile(file))
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
        val builder = GenericDraweeHierarchyBuilder(context.resources)
        val hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(resId)
                .build()
        icon.hierarchy = hierarchy
    }

    fun setImage(uri: Uri) {
        val imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
        if (UriUtil.isNetworkUri(uri)) {
            imageRequestBuilder.isProgressiveRenderingEnabled = true
        } else {
            imageRequestBuilder.resizeOptions = ResizeOptions(
                    icon.layoutParams.width,
                    icon.layoutParams.height)
        }
        val draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequestBuilder.build())
                .setOldController(icon.controller)
                // .setControllerListener(icon.getListener())
                .setAutoPlayAnimations(true)
                .build()
        icon.controller = draweeController
    }
}
