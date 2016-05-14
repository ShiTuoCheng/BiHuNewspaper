package com.shituocheng.bihunewspaper.com.bihunewspaper;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import CustomAdapter.ThemeTitleAdapter;
import Model.MainStoryModel;
import Model.ThemeTitleModel;
import Utility.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThemeTitleFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ThemeTitleModel> mThemeTitleModels = new ArrayList<>();
    private ThemeTitleAdapter mThemeTitleAdapter;


    public ThemeTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("主题日报");
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView)getActivity().findViewById(R.id.themeTitle_listView);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_title, container, false);
    }

    public void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection)(new URL(Utilities.THEME_URL_NAME)).openConnection();
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
                        JSONArray jsonArray = jsonObject.getJSONArray("others");

                        Log.d("JSONARRAY_RESULT",jsonArray.toString());
                        Log.d("JSONARRAY_COUNT",String.valueOf(jsonArray.length()));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });

                        int ArrayCount = jsonArray.length();
                        if (mThemeTitleModels.size() == 0){

                            for (int i=0; i < ArrayCount; i++){
                                JSONObject eachJsonObj = jsonArray.getJSONObject(i);
                                ThemeTitleModel themeTitleModel = new ThemeTitleModel();
                                themeTitleModel.setTheme_name(eachJsonObj.getString("name"));
                                themeTitleModel.setDescription(eachJsonObj.getString("description"));
                                themeTitleModel.setId(eachJsonObj.getInt("id"));
                                themeTitleModel.setThumbnail(eachJsonObj.getString("thumbnail"));

                                mThemeTitleModels.add(themeTitleModel);
                            }

                        }

                        Log.d("JSONRESULT_ARRAY_COUNT",String.valueOf(mThemeTitleModels.size()));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView = (ListView)getActivity().findViewById(R.id.themeTitle_listView);
                                mThemeTitleAdapter = new ThemeTitleAdapter(getActivity(),R.layout.themetitlelayout,mThemeTitleModels);
                                mListView.setAdapter(mThemeTitleAdapter);
                                mThemeTitleAdapter.notifyDataSetChanged();

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ThemeTitleModel model = mThemeTitleModels.get(position);

                                        Intent intent = new Intent(getActivity(), ThemeDetailActivity.class);
                                        intent.putExtra("idData",model.getId());

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
