package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

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

        mViewPager = new ViewPager(this);
        mViewPager = (ViewPager)findViewById(R.id.comment_viewPager);
        mLongCommentFragment = new LongCommentFragment();
        mShortCommentFragment = new ShortCommentFragment();
        mFragments.add(mLongCommentFragment);
        mFragments.add(mShortCommentFragment);
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(fragmentViewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mFragments.get(position).equals(mLongCommentFragment)){
                    setTitle("查看本文长评论");
                }else if (mFragments.get(position).equals(mShortCommentFragment)){
                    setTitle("查看本文短评论");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
