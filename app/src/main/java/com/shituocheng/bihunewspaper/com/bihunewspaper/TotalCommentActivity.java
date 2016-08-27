package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import CustomAdapter.CustomCommentsAdapter;
import CustomAdapter.FragmentViewPagerAdapter;

public class TotalCommentActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Fragment>mFragments = new ArrayList<>();
    private LongCommentFragment mLongCommentFragment;
    private ShortCommentFragment mShortCommentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_comment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("本文评论");

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("本文长评论"));
        tabLayout.addTab(tabLayout.newTab().setText("本文短评论"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = new ViewPager(this);
        mViewPager = (ViewPager)findViewById(R.id.comment_viewPager);

        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        mViewPager.setAdapter(fragmentViewPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            TotalCommentActivity.this.finish();
        }
        return true;
    }
}
