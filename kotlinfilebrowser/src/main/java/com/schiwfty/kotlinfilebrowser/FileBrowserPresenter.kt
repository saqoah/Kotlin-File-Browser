package com.schiwfty.kotlinfilebrowser

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Created by arran on 24/06/2017.
 */
class FileBrowserPresenter : FileBrowserContract.Presenter {

    override val rootDirectory: File
        get() = Environment.getExternalStorageDirectory()

    lateinit var currentFile: File

    lateinit var context: Context
    lateinit var view: FileBrowserContract.View

    override fun deleteFile(file: File) {
        try {
            val deleted = file.deleteRecursively()
            if (!deleted) {
                if (file.isDirectory) view.showError(R.string.error_creating_folder)
                else view.showError(R.string.error_deleting_file)
            }else{
                reload()
            }
        } catch (e: Exception) {
            if (file.isDirectory) view.showError(R.string.error_creating_folder)
            else view.showError(R.string.error_deleting_file)
        }
    }

    override fun renameFile(file: File, name: String) {
        val directory = file.parentFile.absoluteFile
        val newFile = File(directory, name)
        try {
            file.renameTo(newFile)
        } catch (e: Exception) {
            if (file.isDirectory) view.showError(R.string.error_renaming_folder)
            else view.showError(R.string.error_renaming_file)
        }
        reload()
    }

    override fun performFileAction(file: File, action: FileBrowserAdapter.FILE_ACTION) {
        when (action) {
            FileBrowserAdapter.FILE_ACTION.CLICK -> {
                currentFile = file
                if (file.isDirectory) {
                    reload()
                } else view.notifyFileSelected(file)
            }
            FileBrowserAdapter.FILE_ACTION.DELETE -> {
                view.showDeleteFileDialog(file)
            }
            FileBrowserAdapter.FILE_ACTION.RENAME -> {
                view.showRenameFileDialog(file)
            }
        }
    }

    override fun createFileAtCurrentDirectory(name: String) {
        val newFile = File(currentFile.absolutePath, name)
        if (!newFile.exists()) {
            if (currentFile.canWrite()) {
                try {
                    newFile.createNewFile()
                    reload()
                } catch (e: Exception) {
                    view.showError(R.string.error_creating_file)
                    e.printStackTrace()
                }
            } else {
                view.showError(R.string.cannot_create_File)
            }
        } else {
            view.showError(R.string.file_already_exists)
        }
    }

    override fun createFolderAtCurrentDirectory(name: String) {
        val testFolder = File(currentFile.absolutePath, name)
        if (testFolder.exists() && testFolder.isDirectory) {
            view.showError(R.string.folder_already_exists)
        } else {
            if (currentFile.canWrite()) {
                try {
                    val newFolder = File(currentFile.absolutePath, name)
                    newFolder.mkdirs()
                    reload()
                } catch (e: Exception) {
                    view.showError(R.string.error_creating_folder)
                    e.printStackTrace()
                }
            } else {
                view.showError(R.string.cannot_create_folder)
            }
        }
    }

    override fun setup(context: Context, view: FileBrowserContract.View, file: File) {
        currentFile = file
        this.context = context
        this.view = view
    }

    override fun isAtRoot(): Boolean {
        return currentFile == Environment.getExternalStorageDirectory()
    }

    override fun reload() {
        if (currentFile.listFiles() != null) {
            if (currentFile.listFiles().isEmpty()) view.showNoFilesView()
            else {
                view.showFileList(currentFile.listFiles().toList())
            }
        } else {
            view.showError(R.string.error_accessing_file)
            view.showNoFilesView()
        }
        if (isAtRoot()) view.setToolbarTitle(R.string.root)
        else view.setToolbarTitle(currentFile.name)
        view.setupBreadcrumbTrail(currentFile)
    }


    override fun goUpADirectory() {
        val newFile = currentFile.parentFile
        if (newFile != null) {
            currentFile = newFile
            reload()
        }
    }

    override fun notifyBreadcrumbSelected(file: File) {
        currentFile = file
        reload()
    }
}