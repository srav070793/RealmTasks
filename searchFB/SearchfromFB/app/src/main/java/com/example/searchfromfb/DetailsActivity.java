package com.example.searchfromfb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.security.AccessController.getContext;

public class DetailsActivity extends AppCompatActivity {

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
    ShareDialog shareDialog = new ShareDialog(this);
    CallbackManager callbackManager;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //setup tabs and draw them
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f7f7f7")));
        tabLayout.setTabTextColors(R.color.black,R.color.black);

        //setup the icons for the tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if(i==0)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_albums);
            else if(i==1)
                tabLayout.getTabAt(i).setIcon(R.mipmap.ic_posts);
        }

        //setup the backbutton - set up back stack here
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setup title as results
        getSupportActionBar().setTitle("More Details");

        //register callback to facebook dialog box
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(), "You shared this post", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "You cancelled sharing this post", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Post sharing failed with unknown error", Toast.LENGTH_LONG).show();
            }});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle extras = getIntent().getExtras();
        String prof_name = extras.getString("displayprofilename");
        String prof_pic = extras.getString("displayprofilepic");
        String prof_type = extras.getString("displaytype");
        String prof_id = extras.getString("displayid");

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                Toast.makeText(this, "Sharing "+prof_name+"...", Toast.LENGTH_LONG).show();

                if (ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentTitle(prof_name)
                            .setImageUrl(Uri.parse(prof_pic))
                            .setContentDescription("FB SEARCH FROM USC CSCI571")
                            .build();
                    shareDialog.show(content);
                }
                LoginManager.getInstance().logOut();
                return true;
            case R.id.action_favorites:
                SharedPreferences favList = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor favListEditor = favList.edit();
                if (favList.getStringSet(prof_id,null)!=null) {
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_LONG).show();
                    favListEditor.remove(prof_id);favListEditor.commit();
                }
                else {
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show();

                    HashSet<String> newFavHash = new HashSet<String>();
                    newFavHash.add(prof_name);
                    newFavHash.add(prof_pic);
                    newFavHash.add(prof_type);
                    Set<String> myOrder = new TreeSet<String>(newFavHash);

                    favListEditor.putStringSet(prof_id, myOrder);
                    favListEditor.commit();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        Bundle extras = getIntent().getExtras();
        String idSelected = extras.getString("displayid");
        SharedPreferences favList = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String,?> keys = favList.getAll();

        Set entrySet = keys.entrySet();
        Iterator it = entrySet.iterator();

        if (favList != null) {

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String keyObtained = (String) pair.getKey();
                if (keyObtained.matches("[0-9]+")) {

                    if (keyObtained.equals(idSelected)) {
                        //set it to add to favorites
                        menu.findItem(R.id.action_favorites).setTitle("Remove from favorites");
                        return true;
                    } else {
                        //set it to remove from favorites
                        menu.findItem(R.id.action_favorites).setTitle("Add to favorites");
                        return true;
                    }
                }
                else {
                    continue;
                }
            }
        }
        return true;
    }


    //Deleted Placeholder fragment from here

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
            Bundle extras = getIntent().getExtras();
            switch (position){
                case 0:
                    tabAlbumsFragment albumsTab = new tabAlbumsFragment();
                    albumsTab.setArguments(extras);
                    return albumsTab;
                case 1:
                    tabPostsFragment postsTab = new tabPostsFragment();
                    postsTab.setArguments(extras);
                    return postsTab;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALBUMS";
                case 1:
                    return "POSTS";
            }
            return null;
        }
    }
}
