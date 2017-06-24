package com.schiwfty.kotlinfilebrowser

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tbruyelle.rxpermissions.RxPermissions

class FileBrowserActivity : AppCompatActivity(), FileBrowserContract.View {
    lateinit var presenter: FileBrowserContract.Presenter

    companion object{
        fun open(context: Context){
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
    }

    private fun checkPermission(){
        RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({
                    if (it != null && it) {
                        presenter.reload()
                    } else {
                        throw IllegalStateException("permission is required to show file browser")
                       finish()
                    }
                }, {
                    it.printStackTrace()
                })
    }
}
