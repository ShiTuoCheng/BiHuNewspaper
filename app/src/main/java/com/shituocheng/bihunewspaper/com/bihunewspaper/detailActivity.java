package com.shituocheng.bihunewspaper.com.bihunewspaper;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utility.Utilities;

public class detailActivity extends AppCompatActivity {
    private WebView mWebView;

    public static final String commute_data = "commute_id";

    private final int RESULT_MESSAGE_BODY = 0;
    private final int RESULT_MESSAGE_TITLE = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case RESULT_MESSAGE_BODY:

                    mWebView = (WebView)findViewById(R.id.detail_webView);

                    WebSettings webSettings = mWebView.getSettings();

                    webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8

                    webSettings.setJavaScriptEnabled(true);

                    final Map<String,String> map = (Map<String, String>) msg.obj;

                    Log.d("CSS_RESULT",map.get("css").toString());



                    mWebView.loadData(map.get("body"), "text/html;charset=UTF-8", null);//这种写法可以正确解码


                    Log.d("MESSAGE",map.get("body").toString());


                    mWebView.setWebViewClient(new WebViewClient(){

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            mWebView.loadUrl(
                                    "javascript:" + "eval("+
                                            '"'+"function ResizeImages() { " +
                                            "var myimg,oldwidth ;"+
                                            "var maxwidth = " + "window.innerWidth" + ";" +// UIWebView中显示的图片宽度
                                            "for(i=0;i <document.images.length;i++){"+
                                            "myimg = document.images[i];"+
                                            "var avatar = myimg.getAttribute('class');"+
                                            "if(avatar != 'avatar' && myimg.width > maxwidth){"+
                                            "oldwidth = myimg.width;"+
                                            "myimg.width = maxwidth-25;"+
                                            "}"+
                                            "if(avatar != 'avatar' && myimg.width < maxwidth){"+
                                            "oldwidth = myimg.width;"+
                                            "myimg.width = maxwidth;"+
                                            "}"+
                                            "}"+
                                            "}"+
                                            "ResizeImages();"+'"'+")"
                            );

                        }
                    });

                    /*
                    mWebView.setWebViewClient(new WebViewClient(){

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            mWebView.loadUrl(
                                    "javascript:" + "eval("+'"'+
                                    "function loadStyleString(css) {"+
                                    "var style = document.createElement('style');"+
                                "style.type = 'text/css';"+
                                "try{" +
                                "style.appendChild(document.createTextNode(css));"+
                                "} catch (ex) {"+
                                " style.styleSheet.cssText = css;"+
                                "}"+
                                "var head = document.getElementsByTagName('head')[0];"+
                                "head.appendChild(style);"+
                            "}"+
                            "loadStyleString('"+ map.get("css").toString() +"');"+
                                            +'"'+");"
                            );

                        }
                    });
                    */

                    break;
                case RESULT_MESSAGE_TITLE:
                     ActionBar actionBar = getSupportActionBar();

                    getSupportActionBar().setDisplayShowCustomEnabled(true);

                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                    getSupportActionBar().setDisplayShowTitleEnabled(false);

                    View view = LayoutInflater.from(detailActivity.this).inflate(R.layout.title_custom_acionbar,null);

                    TextView customTitle = (TextView)view.findViewById(R.id.custom_actionBar_titleTextView);

                    customTitle.setText(msg.obj.toString());

                    actionBar.setCustomView(view);
                    break;
                case 2:


                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window=detailActivity.this.getWindow();
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        fetchData();
    }

    private void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    int idStory = getIntent().getIntExtra(MainActivity.idData,0);
                    Log.d("data",String.valueOf(idStory));
                    connection = (HttpURLConnection) (new URL(Utilities.DETAIL_URL_NAME + idStory)).openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);
                    try {
                        connection.setConnectTimeout(3000);
                        connection.setReadTimeout(3000);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        inputStream = connection.getInputStream();
                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line + "\r\n");
                        }

                        JSONObject jsonObj = new JSONObject(stringBuilder.toString());

                        String bodyStr = jsonObj.getString("body");

                        String titleStr = jsonObj.getString("title");

                        JSONArray jsonArray = jsonObj.getJSONArray("css");

                        String cssStr = jsonArray.getString(0);

                        Log.d("RESULT_CSS",cssStr);
                        inputStream.close();
                            connection.disconnect();

                            connection = (HttpURLConnection)(new URL(cssStr)).openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();
                            inputStream = connection.getInputStream();
                            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream));
                            String line1 = null;
                            StringBuffer stringBuildertest = new StringBuffer();
                            while ((line1 = bufferedReader1.readLine())!=null){
                                stringBuildertest.append(line1);
                            }

                        String css = stringBuildertest.toString();

                        Log.d("CSS_STRINGBUILDER_RESUL",css);

                        Map<String,String> map = new HashMap<String, String>();

                        map.put("css",css);

                        map.put("body",bodyStr);

                        Log.d("STRINGBUILDER",stringBuildertest.toString());
                            inputStream.close();
                            connection.disconnect();


                        Log.d("RESULT_DATA",bodyStr);

                        // body message set up

                        Message bodyMessage = new Message();

                        bodyMessage.what = RESULT_MESSAGE_BODY;

                        bodyMessage.obj = map;

                        mHandler.sendMessage(bodyMessage);

                        //title message set up

                        Message titleMessage = new Message();

                        titleMessage.what = RESULT_MESSAGE_TITLE;

                        titleMessage.obj = titleStr;

                        mHandler.sendMessage(titleMessage);

                    } catch (ConnectException ce) {
                        Log.d("ERROR", "error");
                    } catch (UnknownHostException ue) {
                        Log.d("ERROR", "UNKNOWN");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Json", "error");

                }
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            detailActivity.this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_item) {

            return true;
        }else if (id == R.id.comment_item){

            int idData = getIntent().getIntExtra(MainActivity.idData,0);

            Intent intent = new Intent(detailActivity.this, TotalCommentActivity.class);

            intent.putExtra(commute_data,idData);

            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}