package com.shituocheng.bihunewspaper.com.bihunewspaper;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public static final String idData = "idData";

    public static final String DIALOG_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainStoryFragment()).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,mOnDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);

        //fetchData();

        //mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        //mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        //mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //    @Override
        //    public void onRefresh() {
        //        fetchData();
        //    }
        //});
    }

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener()

    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            calendar.set(Calendar.YEAR,year);

            calendar.set(Calendar.MONTH,monthOfYear);

            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            updateDate();
        }
    };

    private void updateDate(){

        String yearStr = String.valueOf(calendar.get(Calendar.YEAR));

        String monthStr = String.valueOf(calendar.get(Calendar.MONTH)+1);

        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        String date = simpleDateFormat.format(calendar.getTime());

        Intent intent = new Intent(MainActivity.this, YesterdayStoryActivity.class);

        intent.putExtra(DIALOG_DATE,date);

        intent.putExtra("year",yearStr);

        intent.putExtra("month",monthStr);

        intent.putExtra("day",dayStr);

        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_search_action, menu);
        SearchView search = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, YesterdayStoryActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint_name));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_item){
            Intent intent = new Intent(MainActivity.this, AboutApplicationActivity.class);

            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //now you can display the results
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.mainTitleItem) {
            fragmentTransaction.replace(R.id.container,new MainStoryFragment()).commit();
        }else if (id == R.id.themeTitleItem){
            fragmentTransaction.replace(R.id.container,new ThemeTitleFragment()).commit();

        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
