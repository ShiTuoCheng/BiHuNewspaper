package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class EditorDetailActivity extends AppCompatActivity {
    private WebView editor_detail_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_detail);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        editor_detail_webview = (WebView)findViewById(R.id.editor_detail_webView);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");

        String name = intent.getStringExtra("name");

        actionBar.setTitle(name + "的个人主页");

        editor_detail_webview.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                EditorDetailActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }

}
