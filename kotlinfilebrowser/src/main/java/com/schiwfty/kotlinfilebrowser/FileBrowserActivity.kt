package com.schiwfty.kotlinfilebrowser

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import com.afollestad.materialdialogs.MaterialDialog
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_file_browser.*
import java.io.File


class FileBrowserActivity : AppCompatActivity(), FileBrowserContract.View {

    lateinit var presenter: FileBrowserContract.Presenter

    val adapter: FileBrowserAdapter = FileBrowserAdapter({ file, action ->
        presenter.performFileAction(file, action)
    })

    companion object {
        const val ARG_FILE_RESULT = "arg_file_selected_result"
        const val ARG_OPEN_BROWSER_TO_FILE = "arg_open_file_to_browser"
        fun startActivity(activity: Activity, requestCode: Int, file: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).parentFile) {
            val intent = Intent(activity, FileBrowserActivity::class.java)
            intent.putExtra(ARG_OPEN_BROWSER_TO_FILE, file.absolutePath)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_browser)
        setSupportActionBar(toolbar)
        val startFile = intent.getStringExtra(ARG_OPEN_BROWSER_TO_FILE)
        presenter = FileBrowserPresenter()
        presenter.setup(this, this, File(startFile))
        checkPermission()

        fileBrowserRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        fileBrowserRecyclerView.layoutManager = mLayoutManager
        fileBrowserRecyclerView.adapter = adapter

        addFileFab.setOnClickListener {
            showAddFileDialog()
        }

        addFolderFab.setOnClickListener {
            showAddFolderDialog()
        }
    }

    override fun setupBreadcrumbTrail(file: File) {
        breadcrumb_root_layout.removeAllViews()
        val rootBreadcrumb = BreadcrumbView(this)
        rootBreadcrumb.bind(Environment.getExternalStorageDirectory())
        rootBreadcrumb.textVeiw.text = getString(R.string.root)
        rootBreadcrumb.setOnClickListener {
            presenter.notifyBreadcrumbSelected(rootBreadcrumb.file)
        }
        breadcrumb_root_layout.addView(rootBreadcrumb)

        val fileList = mutableListOf<File>()
        var parentFile: File = file
        while (parentFile != Environment.getExternalStorageDirectory()) {
            if (parentFile.name.isNotEmpty()) fileList.add(parentFile)
            parentFile = parentFile.parentFile
        }
        fileList.reverse()
        fileList.forEach {
            val breadcrumb = BreadcrumbView(this)
            breadcrumb.bind(it)
            breadcrumb.setOnClickListener {
                presenter.notifyBreadcrumbSelected(breadcrumb.file)
            }
            breadcrumb_root_layout.addView(breadcrumb)
        }
        breadcrumbScroll.postDelayed({ breadcrumbScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT) }, 100L)
    }

    private fun checkPermission() {
        RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({
                    if (it != null && it) {
                        presenter.reload()
                    } else {
                        throw IllegalStateException("permission is required to show files browser")
                    }
                }, {
                    it.printStackTrace()
                })
    }

    override fun showFileList(files: List<File>) {
        adapter.files = files
        adapter.notifyDataSetChanged()
        emptyView.visibility = View.GONE
        fileBrowserRecyclerView.visibility = View.VISIBLE
    }

    override fun showError(stringId: Int) {
        val snackbar = Snackbar.make(rootLayout, getString(stringId), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.RED))
        snackbar.show()
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setToolbarTitle(stringRes: Int) {
        supportActionBar?.title = getString(stringRes)
    }

    override fun showNoFilesView() {
        emptyView.visibility = View.VISIBLE
        fileBrowserRecyclerView.visibility = View.GONE
    }

    override fun notifyFileSelected(file: File) {
        val returnIntent = Intent()
        returnIntent.putExtra(ARG_FILE_RESULT, file)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onBackPressed() {
        if (presenter.isAtRoot()) super.onBackPressed()
        else presenter.goUpADirectory()
    }

    override fun showAddFileDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.new_file)
                .customView(R.layout.dialog_add_file, false)
                .positiveText(android.R.string.ok)
                .onPositive { dialog, _ ->
                    RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe({
                                if (it != null && it) {
                                    val edittext = dialog.customView?.findViewById(R.id.fileNameInput) as EditText
                                    presenter.createFileAtCurrentDirectory(edittext.text.toString())
                                } else {
                                    throw IllegalStateException("permission is required to show files browser")
                                }
                            }, {
                                it.printStackTrace()
                            })
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun showAddFolderDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.new_folder)
                .customView(R.layout.dialog_add_folder, false)
                .positiveText(android.R.string.ok)
                .onPositive { dialog, _ ->
                    RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe({
                                if (it != null && it) {
                                    val edittext = dialog.customView?.findViewById(R.id.folderNameInput) as EditText
                                    presenter.createFolderAtCurrentDirectory(edittext.text.toString())
                                } else {
                                    throw IllegalStateException("permission is required to show files browser")
                                }
                            }, {
                                it.printStackTrace()
                            })
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun showRenameFileDialog(file: File) {
        MaterialDialog.Builder(this)
                .title(R.string.rename_file)
                .customView(R.layout.dialog_rename_file, false)
                .positiveText(android.R.string.ok)
                .onPositive { dialog, _ ->
                    RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe({
                                if (it != null && it) {
                                    val edittext = dialog.customView?.findViewById(R.id.renameFileInput) as EditText
                                    presenter.renameFile(file, edittext.text.toString())
                                } else {
                                    throw IllegalStateException("permission is required to show files browser")
                                }
                            }, {
                                it.printStackTrace()
                            })
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun showDeleteFileDialog(file: File) {
        var content = ""
        if (file.isDirectory) content = getString(R.string.confirm_delete_folder, file.name)
        else content = getString(R.string.confirm_delete_file, file.name)
        MaterialDialog.Builder(this)
                .title(R.string.delete_file)
                .content(content)
                .positiveText(android.R.string.ok)
                .onPositive { dialog, _ ->
                    RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe({
                                if (it != null && it) {
                                    presenter.deleteFile(file)
                                } else {
                                    throw IllegalStateException("permission is required to show files browser")
                                }
                            }, {
                                it.printStackTrace()
                            })
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .negativeText(android.R.string.cancel)
                .show()
    }
}
