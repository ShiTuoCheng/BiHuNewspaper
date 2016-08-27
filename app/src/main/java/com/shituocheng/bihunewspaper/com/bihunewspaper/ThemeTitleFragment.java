package com.shituocheng.bihunewspaper.com.bihunewspaper;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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
    private RecyclerView mRecyclerView;
    private ArrayList<ThemeTitleModel> mThemeTitleModels = new ArrayList<>();
    private ThemeTitleAdapter mThemeTitleAdapter;
    private ProgressDialog mProgressDialog;


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
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        // Inflate the layout for this fragment
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在载入");
        mProgressDialog.show();

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
                                mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
                                mRecyclerView.setHasFixedSize(true);
                                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

                                mThemeTitleAdapter = new ThemeTitleAdapter(mThemeTitleModels);

                                mRecyclerView.setAdapter(mThemeTitleAdapter);
                                mRecyclerView.addItemDecoration(new ItemSpaceDivider(30));
                                mThemeTitleAdapter.notifyDataSetChanged();


                                mThemeTitleAdapter.setOnItemClickListener(new ThemeTitleAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View view, int position) {
                                        ThemeTitleModel model = mThemeTitleModels.get(position);

                                        Intent intent = new Intent(getActivity(), ThemeDetailActivity.class);
                                        intent.putExtra("idData",model.getId());

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void OnItemLongClick(View view, int position) {

                                        Toast.makeText(getActivity(),"进去看看吧^_^",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                mProgressDialog.dismiss();
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

    private class ItemSpaceDivider extends RecyclerView.ItemDecoration{
        private int space;

        public ItemSpaceDivider(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildPosition(view) == 0){
                outRect.top = space;
            }
        }
    }

}
