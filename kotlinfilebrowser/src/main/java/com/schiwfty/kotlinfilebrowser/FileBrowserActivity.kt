package com.schiwfty.kotlinfilebrowser

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_file_browser.*
import java.io.File


class FileBrowserActivity : AppCompatActivity(), FileBrowserContract.View {

    lateinit var presenter: FileBrowserContract.Presenter

    val adapter: FileBrowserAdapter = FileBrowserAdapter({
        presenter.fileClicked(it)
    })

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, FileBrowserActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_browser)
        setSupportActionBar(toolbar)
        presenter = FileBrowserPresenter()
        presenter.setup(this, this, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).parentFile)
        checkPermission()
        recycler_view.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.adapter = adapter
        goUpButton.setOnClickListener {
            presenter.goUpADirectory()
        }
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
        recycler_view.visibility = View.VISIBLE
    }

    override fun showError(stringId: Int) {
        val snackbar = Snackbar.make(rootLayout, getString(stringId), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.RED))
        snackbar.show()
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setUpDirectoryVisible(visible: Boolean) {
        if (visible) goUpButton.visibility = View.VISIBLE
        else goUpButton.visibility = View.GONE
    }

    override fun showNoFilesView() {
        emptyView.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }
}
