package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import CustomAdapter.MyCustomAdapter;
import Model.MainStoryModel;

public class YesterdayStoryActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyCustomAdapter mMyCustomAdapter;
    private ArrayList<MainStoryModel> mMainStoryModels = new ArrayList<>();
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesterday_story);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        mListView = (ListView)findViewById(R.id.yesterday_listView);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在载入...");
        mProgressDialog.show();

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        fetchData();
    }
    public void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {

                    if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
                        String dateStr = getIntent().getStringExtra(SearchManager.QUERY);
                        Log.d("RESULT_SEARCH",dateStr);
                    }

                    final String dateStr = getIntent().getStringExtra(MainActivity.DIALOG_DATE);

                    final String year = getIntent().getStringExtra("year");

                    final String month = getIntent().getStringExtra("month");

                    final String day = getIntent().getStringExtra("day");

                    connection = (HttpURLConnection)(new URL("http://news.at.zhihu.com/api/4/news/before/"+dateStr)).openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);
                    try{
                        connection.setRequestMethod("GET");
                        connection.connect();
                        inputStream = connection.getInputStream();
                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = bufferedReader.readLine()) != null){
                            stringBuilder.append(line);
                        }
                        inputStream.close();
                        connection.disconnect();
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("stories");

                        Log.d("JSONARRAY_RESULT",jsonArray.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mProgressDialog.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });

                        int ArrayCount = jsonArray.length();
                        if (mMainStoryModels.size() == 0){

                            for (int i=0; i < ArrayCount; i++){
                                JSONObject eachJsonObj = jsonArray.getJSONObject(i);
                                MainStoryModel mainStoryModel = new MainStoryModel();

                                mainStoryModel.setTitle(eachJsonObj.getString("title"));
                                mainStoryModel.setId(eachJsonObj.getInt("id"));
                                JSONArray imageJsonArray = eachJsonObj.getJSONArray("images");
                                mainStoryModel.setImage(imageJsonArray.getString(0));

                                Log.d("IMAGE",mainStoryModel.getImage());

                                mMainStoryModels.add(mainStoryModel);
                            }

                        }

                        Log.d("JSONRESULT_ARRAY_COUNT",String.valueOf(mMainStoryModels.size()));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMyCustomAdapter = new MyCustomAdapter(getApplicationContext(),R.layout.listview_item_layout,mMainStoryModels);
                                mListView.setAdapter(mMyCustomAdapter);
                                mMyCustomAdapter.notifyDataSetChanged();

                                ActionBar actionBar = getSupportActionBar();

                                actionBar.setTitle(year+"年"+month+"月"+ day +"日"+"的日报");

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        MainStoryModel mainStoryModel = mMainStoryModels.get(position);

                                        Intent intent = new Intent(YesterdayStoryActivity.this, detailActivity.class);

                                        intent.putExtra(MainActivity.idData,mainStoryModel.getId());

                                        startActivity(intent);
                                    }
                                });
                            }
                        });

                    }catch (ConnectException connectE){
                        connectE.printStackTrace();
                    }catch (JSONException jsonE){
                        jsonE.printStackTrace();
                    }catch (FileNotFoundException fileE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

                                alertDialog.setTitle("你搜索的时间或者查找的日期不太对吧（≧∇≦）");

                                alertDialog.setMessage("换个搜索时间吧");

                                alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        YesterdayStoryActivity.this.finish();
                                    }
                                });

                                alertDialog.create();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                YesterdayStoryActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}

