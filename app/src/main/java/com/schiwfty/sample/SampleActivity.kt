package com.schiwfty.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.schiwfty.kotlinfilebrowser.FileBrowserActivity

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FileBrowserActivity.open(this).subscribe {
            Log.v("Files selected", it.name)
        }
    }
}
