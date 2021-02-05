package com.schiwfty.kotlinfilebrowser

//package com.hamza.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File


/**
 * Created by arran on 24/06/2017.
 */

class FileBrowserAdapter(private val itemClickListener: (File, FILE_ACTION) -> Unit) :
    RecyclerView.Adapter<FileViewHolder>() {
    private var files: List<File> = emptyList()

    enum class FILE_ACTION {
        CLICK,
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_file, parent, false)
        val holder = FileViewHolder(view)
        holder.onClick { _, position, _ ->
            itemClickListener.invoke(files[position], FILE_ACTION.CLICK)
        }

        return holder

    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position], holder.view.context)
    }
}

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}

class FileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.filename)
    val info: TextView = view.findViewById(R.id.fileinfo)
    fun bind(file: File, context: Context) {
        name.text = file.name
        if (file.isDirectory) {
            if (file.listFiles() != null) when (file.listFiles().size) {
                1 -> info.text = context.getText(R.string.single_item)
                else -> info.text =
                    context.getString(R.string.items, file.listFiles().size ?: 0)
            }
        }
    }

}
