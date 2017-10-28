package com.michael.bitcoinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import utilities.DailyNews;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set default fragment to current price fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, new CurrentPriceFragment());
        fragmentTransaction.commit();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        databaseHelper.insertCurrentPrice();

        Intent intent = new Intent(this, StroreDailyPrice.class);
        startService(intent);

        //Retrieve data for further use
        databaseHelper.retrieveHistoryData();
        databaseHelper.retrieveDailyData();

        DailyNews dailyNews = new DailyNews();
        dailyNews.getWebsiteNews();

    }

    //Disable back press button due to bug
    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    //Handle navigation drawer selection
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    //Navigation drawer selection action
    private void displaySelectedScreen(int id){
        Fragment fragment = null;

        switch (id){
            case R.id.nav_current:
                fragment = new CurrentPriceFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryPriceFragment();
                break;
            case R.id.nav_dailychart:
                fragment = new DailyChartFragment();
                break;
            case R.id.nav_historychart:
                fragment = new HistoryChartFragment();
                break;
            case R.id.nav_news:
                fragment = new NewsFragment();
                break;
        }

        if (fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
