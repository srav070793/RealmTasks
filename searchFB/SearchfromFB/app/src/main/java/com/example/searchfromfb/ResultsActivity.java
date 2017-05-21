package com.example.searchfromfb;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {
    String userResults;
    String pageResults;
    String eventResults;
    String groupResults;
    String placeResults;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //setup tabs using setupWithViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f7f7f7")));
        tabLayout.setTabTextColors(R.color.black,R.color.black);

        //setting up the icons for the tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if(i==0) {
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_user);
            }
            else if(i==1)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_pages);
            else if(i==2)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_events);
            else if(i==3)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_places);
            else if(i==4)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_groups);
        }
        //telling android to use toolbar as action bar
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbarResults);
        setSupportActionBar(toolbar1);

        //make it show the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setup title as results
        getSupportActionBar().setTitle("Results");

        //retrieves JSON data which was passed from MainActivity
        userResults = getIntent().getStringExtra("users");
        pageResults = getIntent().getStringExtra("pages");
        eventResults = getIntent().getStringExtra("events");
        groupResults = getIntent().getStringExtra("groups");
        placeResults = getIntent().getStringExtra("places");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    //Placeholder Fragment deleted from here

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    Bundle bundleUser = new Bundle();
                    bundleUser.putString("users", userResults);
                    tabUsersFragment userTab = new tabUsersFragment();
                    userTab.setArguments(bundleUser);
                    return userTab;
                case 1:

                    Bundle bundlePage = new Bundle();
                    bundlePage.putString("pages", pageResults);
                    tabPagesFragment pagesTab = new tabPagesFragment();
                    pagesTab.setArguments(bundlePage);
                    return pagesTab;
                case 2:

                    Bundle bundleEvent = new Bundle();
                    bundleEvent.putString("events", eventResults);
                    tabEventsFragment eventsTab = new tabEventsFragment();
                    eventsTab.setArguments(bundleEvent);
                    return eventsTab;
                case 3:

                    Bundle bundlePlace = new Bundle();
                    bundlePlace.putString("places", placeResults);
                    tabPlacesFragment placesTab = new tabPlacesFragment();
                    placesTab.setArguments(bundlePlace);
                    return placesTab;
                case 4:

                    Bundle bundleGroup = new Bundle();
                    bundleGroup.putString("groups", groupResults);
                    tabGroupsFragment groupsTab = new tabGroupsFragment();
                    groupsTab.setArguments(bundleGroup);
                    return groupsTab;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "USERS";
                case 1:
                    return "PAGES";
                case 2:
                    return "EVENTS";
                case 3:
                    return "PLACES";
                case 4:
                    return "GROUPS";
            }
            return null;
        }
    }
}
