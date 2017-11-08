package com.schiwfty.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity
import java.io.File

class SampleActivity : AppCompatActivity() {
    private val RC_SELECT_FILE = 302

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val normalModeButton = findViewById(R.id.startBrowser)
        val folderSelectModeButton = findViewById(R.id.startBrowserFolderMode)

        normalModeButton.setOnClickListener {
            FileBrowserActivity.startActivity(this, RC_SELECT_FILE)
        }

        folderSelectModeButton.setOnClickListener {
            FileBrowserActivity.startActivity(this, RC_SELECT_FILE, true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val file = data.extras.getSerializable(FileBrowserActivity.ARG_FILE_RESULT) as File
                    Log.v("Files selected", file.absolutePath)
                }
            }
        }
    }
}
