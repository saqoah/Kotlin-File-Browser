package com.schiwfty.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById(R.id.startBrowser)
        val button2  = findViewById(R.id.startBrowserInViewOnlyMode)

        button.setOnClickListener {
            FileBrowserActivity.open(this, false).subscribe {
                Log.v("Files selected", it.name)
            }
        }

        button2.setOnClickListener {
            FileBrowserActivity.open(this, true).subscribe {
                Log.v("Files selected", it.name)
            }
        }

    }

}
