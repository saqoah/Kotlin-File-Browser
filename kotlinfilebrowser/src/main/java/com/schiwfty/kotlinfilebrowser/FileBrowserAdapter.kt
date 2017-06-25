package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item_file.view.*
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
        val holder =  FileViewHolder(view)
        holder.onClick { _, position, _ ->
            itemClickListener.invoke(files[position])
        }
        return holder
    }
}


class FileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val icon: ImageView = view.icon
    val name: TextView = view.name
    val info: TextView = view.extra_info
    fun bind(file: File, context: Context) {
        name.text = file.name
        if(file.isDirectory){
            icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder_black_24dp))
            if(file.listFiles()!=null){
                when(file.listFiles().size){
                    1 -> info.text = context.getText(R.string.single_item)
                    else -> info.text = context.getString(R.string.items, file.listFiles().size ?: 0)
                }
            }
        }else{
            icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file_black_24dp))
            info.text = file.length().formatBytesAsSize()
        }
    }
}

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}