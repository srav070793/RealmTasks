package com.example.searchfromfb;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class FavoritesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //setting up tabs with setuppageviewer
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsFav);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f7f7f7")));
        tabLayout.setTabTextColors(R.color.black,R.color.black);

        //setting up the icons for the tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if(i==0)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_user);
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
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolBarFavorites);
        setSupportActionBar(toolbar1);

        //make it show the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setup title as results
        getSupportActionBar().setTitle("Favorites");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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

    //placeholder deleted from here

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    tabFavUsers userFavTab = new tabFavUsers();
                    return userFavTab;
                case 1:
                    tabFavPages pagesFavTab = new tabFavPages();
                    return pagesFavTab;
                case 2:
                    tabFavEvents eventsFavTab = new tabFavEvents();
                    return eventsFavTab;
                case 3:
                    tabFavPlaces placesFavTab = new tabFavPlaces();
                    return placesFavTab;
                case 4:
                    tabFavGroups groupsFavTab = new tabFavGroups();
                    return groupsFavTab;
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
