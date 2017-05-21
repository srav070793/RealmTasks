package com.example.searchfromfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.text.TextUtils.concat;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences favList;
    SharedPreferences.Editor favListEditor;
    private static final int PREFERENCE_MODE_PRIVATE =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setup shared preferences
        favList = getPreferences(PREFERENCE_MODE_PRIVATE);
        favListEditor = favList.edit();
        favListEditor.clear();
        favListEditor.commit();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        } else if (id == R.id.nav_favorites) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame, new FavoritesFragment()).commit();
            Intent intent = new Intent(this,FavoritesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutme) {
            //navigate to the about me activity
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*----event listener for search button-------*/
    public void sendQuery(View view){

        //Validate the user input
        EditText searchQuery = (EditText) findViewById(R.id.searchBox);
        String query = searchQuery.getText().toString();

        if (query.length() == 0){
            //dont navigate...display error message
            Toast.makeText(this, "Please enter a keyword!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Build the URL/URLS for AWS/Google App Engine
        String lat=null,longt=null;

        //get the current location
        Location myLocation = new Location("My Dummy Location");
        myLocation.setLatitude(34.0224);
        myLocation.setLongitude(118.2851);

        String urlUser="http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+query+"&searchType=user";
        String urlPage = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+query+"&searchType=page";
        String urlEvent = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+query+"&searchType=event";
        String urlGroup = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+query+"&searchType=group";
        String urlPlace = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+query+"" +
                "&searchType=place&queryLat=34.0224&queryLong=118.2851";

        //Create a new task which is of type AsyncTask to fetch the JSON data. It will initiate an asynchronous call.
        HttpGetRequest getFBUsers = new HttpGetRequest();
        HttpGetRequest getFBPages = new HttpGetRequest();
        HttpGetRequest getFBEvents = new HttpGetRequest();
        HttpGetRequest getFBGroups = new HttpGetRequest();
        HttpGetRequest getFBPlaces = new HttpGetRequest();

        //Execute the task
        String resultUser = null;
        String resultPage = null;
        String resultEvent = null;
        String resultGroup = null;
        String resultPlace = null;

        try {
            resultUser= getFBUsers.execute(urlUser).get();
            resultPage = getFBPages.execute(urlPage).get();
            resultEvent = getFBEvents.execute(urlEvent).get();
            resultGroup = getFBGroups.execute(urlGroup).get();
            resultPlace = getFBPlaces.execute(urlPlace).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //move to another activity if needed!
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("users", resultUser);
        intent.putExtra("pages", resultPage);
        intent.putExtra("events", resultEvent);
        intent.putExtra("groups", resultGroup);
        intent.putExtra("places", resultPlace);
        startActivity(intent);


    }
    /*-------event listener for clear button------------*/
    public void clearText(View view){
        EditText searchQuery = (EditText)findViewById(R.id.searchBox);
        searchQuery.setText("");
    }
}

