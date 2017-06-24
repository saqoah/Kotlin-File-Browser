package com.schiwfty.kotlinfilebrowser

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
        presenter = FileBrowserPresenter()
        presenter.setup(this, this)
        checkPermission()
        recycler_view.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.adapter = adapter
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

    override fun updateView(files: List<File>) {
        adapter.files = files
        adapter.notifyDataSetChanged()
    }
}
