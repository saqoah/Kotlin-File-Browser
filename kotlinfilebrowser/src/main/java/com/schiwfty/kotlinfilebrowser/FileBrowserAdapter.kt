package com.schiwfty.kotlinfilebrowser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File


/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserAdapter(val itemClickListener: (File) -> Unit) : RecyclerView.Adapter<FileViewHolder>() {
    var files: List<File> = emptyList()

    override fun onBindViewHolder(holderFile: FileViewHolder?, position: Int) {
        holderFile?.bind(files[position], holderFile.view.context)
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FileViewHolder {
        if (parent == null) throw NullPointerException("parent should not be null")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_file, parent, false)
        val holder = FileViewHolder(view)
        holder.onClick { _, position, _ ->
            itemClickListener.invoke(files[position])
        }
        return holder

    }
}

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}