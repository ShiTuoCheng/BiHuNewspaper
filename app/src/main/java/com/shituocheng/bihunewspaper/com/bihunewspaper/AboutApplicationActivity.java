package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("关于本应用的API");
    }
}
