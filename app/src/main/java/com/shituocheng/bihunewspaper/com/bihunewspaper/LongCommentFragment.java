package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

import CustomAdapter.CustomCommentsAdapter;
import Model.LongCommentModel;
import Utility.Utilities;

public class LongCommentFragment extends Fragment {
    private ListView mListView;
    private ArrayList<LongCommentModel> mLongCommentModels = new ArrayList<>();
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_long_comment,null);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在载入");
        mProgressDialog.show();

        mListView = (ListView)view.findViewById(R.id.long_comment_listView);
        return view;
    }

    private void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    int idData = getActivity().getIntent().getIntExtra(detailActivity.commute_data,0);

                    Log.d("IDDATA",String.valueOf(idData));
                    connection = (HttpURLConnection) new URL("http://news-at.zhihu.com/api/4/story/"+ idData +"/long-comments").openConnection();
                    connection.setReadTimeout(3000);
                    connection.setConnectTimeout(3000);
                    try{
                        connection.setRequestMethod("GET");
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
                        JSONArray jsonArray = jsonObject.getJSONArray("comments");
                        int jsonArrayCount = jsonArray.length();

                        Log.d("JSONARRAY_COUNT",String.valueOf(jsonArrayCount));

                        for (int i = 0; i < jsonArrayCount; i++){
                            JSONObject eachJsonObj = jsonArray.getJSONObject(i);
                            LongCommentModel longCommentModel = new LongCommentModel();

                            longCommentModel.setAuthor(eachJsonObj.getString("author"));
                            longCommentModel.setAvatar(eachJsonObj.getString("avatar"));
                            longCommentModel.setContent(eachJsonObj.getString("content"));
                            longCommentModel.setLikes(eachJsonObj.getString("likes"));

                            mLongCommentModels.add(longCommentModel);
                        }
                       getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                mListView = (ListView)getActivity().findViewById(R.id.long_comment_listView);
                                CustomCommentsAdapter customCommentsAdapter = new CustomCommentsAdapter(getActivity().getApplicationContext(),R.layout.long_comment_layout,mLongCommentModels);
                                mListView.setAdapter(customCommentsAdapter);
                                customCommentsAdapter.notifyDataSetChanged();
                                if (mLongCommentModels.size() == 0){
                                    TextView textView = (TextView)getActivity().findViewById(R.id.note_long_textView);
                                    textView.setText("本文目前没有长评论，向右滑动查看有没有短评论吧^_^");
                                }
                                mProgressDialog.dismiss();
                            }
                        });
                    }catch (ConnectException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}