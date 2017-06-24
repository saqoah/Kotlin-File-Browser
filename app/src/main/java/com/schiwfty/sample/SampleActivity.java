package com.schiwfty.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.schiwfty.kotlinfilebrowser.FileBrowserActivity;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileBrowserActivity.Companion.open(this);
    }
}
