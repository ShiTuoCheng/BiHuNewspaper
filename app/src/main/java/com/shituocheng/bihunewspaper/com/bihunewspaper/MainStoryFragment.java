package com.shituocheng.bihunewspaper.com.bihunewspaper;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import CustomAdapter.MyCustomAdapter;
import Model.MainStoryModel;
import Utility.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainStoryFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyCustomAdapter mMyCustomAdapter;
    private ArrayList<MainStoryModel> mMainStoryModels = new ArrayList<>();
    private ListView mListView;

    private AlertDialog.Builder dialog;

    private ProgressDialog mProgressDialog;

    public static final String idData = "idData";


    public MainStoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("今天的日报");
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_story,null);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.mainFragment_swipeRefreshLayout);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在载入");
        mProgressDialog.show();
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);

        return view;
    }

    public void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection)(new URL(Utilities.URL_NAME)).openConnection();
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
                        getActivity().runOnUiThread(new Runnable() {
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

                                Log.d("ID",String.valueOf(mainStoryModel.getId()));

                                mMainStoryModels.add(mainStoryModel);
                            }

                        }

                        Log.d("JSONRESULT_ARRAY_COUNT",String.valueOf(mMainStoryModels.size()));

                       getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView = (ListView)getActivity().findViewById(R.id.MainStoryListView);
                                mMyCustomAdapter = new MyCustomAdapter(getActivity(),R.layout.listview_item_layout,mMainStoryModels);
                                mListView.setAdapter(mMyCustomAdapter);
                                mMyCustomAdapter.notifyDataSetChanged();

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        MainStoryModel model = mMainStoryModels.get(position);

                                        Intent intent = new Intent(getActivity(), detailActivity.class);

                                        intent.putExtra(idData,model.getId());

                                        startActivity(intent);
                                    }
                                });
                            }
                        });

                    }catch (ConnectException connectE){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                networkError();
                            }
                        }).start();
                    }catch (JSONException jsonE){
                        jsonE.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void networkError(){

        mProgressDialog.dismiss();
        dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("网络出错");

        dialog.setMessage("网络出错，请检查网络连接");

        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();

        dialog.show();
    }

}
