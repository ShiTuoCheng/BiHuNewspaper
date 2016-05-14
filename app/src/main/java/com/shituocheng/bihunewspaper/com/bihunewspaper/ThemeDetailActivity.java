package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import CustomAdapter.MyCustomAdapter;
import CustomAdapter.ThemeDetailAdapter;
import CustomAdapter.ThemeEditorAdapter;
import Model.MainStoryModel;
import Model.ThemeDetailModel;
import Model.ThemeEditorModel;
import Utility.Utilities;

public class ThemeDetailActivity extends AppCompatActivity {
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<ThemeEditorModel> mThemeEditorModels = new ArrayList<>();
    private ArrayList<ThemeDetailModel> mThemeDetailModels = new ArrayList<>();
    private ThemeDetailAdapter mThemeDetailAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private final int RESULT_BACKGROUND_WHAT = 0;
    private final int RESULT_TITLE_WHAT = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case RESULT_BACKGROUND_WHAT:
                    mNetworkImageView = (NetworkImageView)findViewById(R.id.themeDetail_networkImageView);

                    Log.d("MESSAGE_OBJ",msg.obj.toString());

                    mNetworkImageView.setImageUrl(msg.obj.toString(),mImageLoader);
                    break;

                case RESULT_TITLE_WHAT:

                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(msg.obj.toString());
                    actionBar.setHomeButtonEnabled(true);
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_theme_detail);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.theme_detail_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            ThemeDetailActivity.this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.title_item){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int id = getIntent().getIntExtra("idData",0);
                    Log.d("IDDATA",String.valueOf(id));
                    HttpURLConnection connection = null;
                    InputStream inputStream = null;

                    try{
                        connection = (HttpURLConnection)(new URL(Utilities.THEME_DETAIL_URL_NAME + id)).openConnection();
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
                            final String nameStr = jsonObject.getString("name");
                            final String descriptionSrtr = jsonObject.getString("description");
                            final String backgroundStr = jsonObject.getString("background");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ThemeDetailActivity.this).create();
                                    alertDialog.show();
                                    Window window = alertDialog.getWindow();
                                    window.setContentView(R.layout.custom_dialog_layout);
                                    TextView tv_title = (TextView) window.findViewById(R.id.title_name);
                                    tv_title.setText(nameStr);
                                    TextView tv_message = (TextView) window.findViewById(R.id.description);
                                    tv_message.setText(descriptionSrtr);
                                    NetworkImageView networkImageView = (NetworkImageView)window.findViewById(R.id.background_networkImage);
                                    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                                    networkImageView.setImageUrl(backgroundStr,imageLoader);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return true;
        }else if (id == R.id.editor_item){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int id = getIntent().getIntExtra("idData",0);
                    Log.d("IDDATA",String.valueOf(id));
                    HttpURLConnection connection = null;
                    InputStream inputStream = null;

                    try{
                        connection = (HttpURLConnection)(new URL(Utilities.THEME_DETAIL_URL_NAME + id)).openConnection();
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
                            JSONArray jsonArray = jsonObject.getJSONArray("editors");

                            if (mThemeEditorModels.size() == 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject eachJsonObj = jsonArray.getJSONObject(i);

                                    ThemeEditorModel themeEditorModel = new ThemeEditorModel();

                                    themeEditorModel.setAvatar(eachJsonObj.getString("avatar"));

                                    themeEditorModel.setEditor_name(eachJsonObj.getString("name"));

                                    themeEditorModel.setBio(eachJsonObj.getString("bio"));

                                    themeEditorModel.setNrl(eachJsonObj.getString("url"));

                                    mThemeEditorModels.add(themeEditorModel);
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ThemeDetailActivity.this).create();
                                    alertDialog.show();
                                    Window window = alertDialog.getWindow();
                                    window.setContentView(R.layout.editor_listview_layout);

                                    ListView listView = (ListView)window.findViewById(R.id.editor_listView);
                                    final ThemeEditorAdapter themeEditorAdapter = new ThemeEditorAdapter(getApplicationContext(),R.layout.editor_item_layout,mThemeEditorModels);
                                    listView.setAdapter(themeEditorAdapter);
                                    themeEditorAdapter.notifyDataSetChanged();

                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            ThemeEditorModel themeEditorModel = mThemeEditorModels.get(position);

                                            String url = themeEditorModel.getNrl();

                                            String name = themeEditorModel.getEditor_name();

                                            Intent intent = new Intent(ThemeDetailActivity.this, EditorDetailActivity.class);

                                            intent.putExtra("url",url);

                                            intent.putExtra("name", name);

                                            startActivity(intent);
                                        }
                                    });

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = getIntent().getIntExtra("idData",0);
                Log.d("IDDATA",String.valueOf(id));
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection)(new URL(Utilities.THEME_DETAIL_URL_NAME + id)).openConnection();
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });

                        String backgroundStr = jsonObject.getString("background");

                        Message backgroundMessage = new Message();

                        backgroundMessage.obj = backgroundStr;

                        backgroundMessage.what = RESULT_BACKGROUND_WHAT;

                        mHandler.sendMessage(backgroundMessage);

                        String titleStr = jsonObject.getString("name");

                        Message titleMessage = new Message();

                        titleMessage.obj = titleStr;

                        titleMessage.what = RESULT_TITLE_WHAT;

                        mHandler.sendMessage(titleMessage);

                        int ArrayCount = jsonArray.length();
                        if (mThemeDetailModels.size() == 0){

                            for (int i=0; i < ArrayCount; i++){
                                JSONObject eachJsonObj = jsonArray.getJSONObject(i);

                                ThemeDetailModel themeDetailModel = new ThemeDetailModel();
                                themeDetailModel.setId(eachJsonObj.getInt("id"));
                                themeDetailModel.setTitle(eachJsonObj.getString("title"));
                                mThemeDetailModels.add(themeDetailModel);
                            }

                        }

                        Log.d("JSONRESULT_ARRAY_COUNT",String.valueOf(mThemeDetailModels.size()));


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView = (ListView)findViewById(R.id.themeDetail_listView);
                                mThemeDetailAdapter = new ThemeDetailAdapter(getApplicationContext(),R.layout.theme_detail_item_layout,mThemeDetailModels);
                                mListView.setAdapter(mThemeDetailAdapter);
                                mThemeDetailAdapter.notifyDataSetChanged();

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ThemeDetailModel themeDetailModel = mThemeDetailModels.get(position);

                                        Intent intent = new Intent(ThemeDetailActivity.this, detailActivity.class);
                                        intent.putExtra(MainStoryFragment.idData,themeDetailModel.getId());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });



                    }catch (ConnectException connectE){
                        connectE.printStackTrace();
                    }catch (JSONException jsonE){
                        jsonE.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
