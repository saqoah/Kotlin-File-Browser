package com.schiwfty.kotlinfilebrowser

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File


/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserAdapter(val itemClickListener: (File, FILE_ACTION) -> Unit) : RecyclerView.Adapter<FileViewHolder>() {
    var files: List<File> = emptyList()
    var viewOnlyMode: Boolean = false

    enum class FILE_ACTION {
        CLICK,
        RENAME,
        DELETE
    }

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
            itemClickListener.invoke(files[position], FILE_ACTION.CLICK)
        }
        holder.onLongClick { view, position, type ->
            if(!viewOnlyMode) false
            else{
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.menu_file_long_click)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete -> {
                            itemClickListener.invoke(files[position], FILE_ACTION.DELETE)
                            true
                        }
                        R.id.rename -> {
                            itemClickListener.invoke(files[position], FILE_ACTION.RENAME)
                            true
                        }
                        else -> false
                    }

                }
                popupMenu.show()
            }
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

fun <T : RecyclerView.ViewHolder> T.onLongClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnLongClickListener {
        event.invoke(it, adapterPosition, itemViewType)
        true
    }
    return this
}