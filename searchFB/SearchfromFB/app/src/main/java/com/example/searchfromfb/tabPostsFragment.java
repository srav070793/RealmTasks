package com.example.searchfromfb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sravanianne on 4/21/17.
 */

public class tabPostsFragment extends android.support.v4.app.Fragment {
    String detailResults;
    int errorMsg;
    JSONObject detailData;
    JSONObject postResults;
    JSONArray postsArray;
    String displayedProfileName;
    String displayedProfilePic;
    ArrayList<postsDataObject> postsArrayList = new ArrayList<postsDataObject>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //retrieve all the passed parameters
        detailResults = getArguments().getString("details");
        detailResults = getArguments().getString("details");
        displayedProfileName = getArguments().getString("displayprofilename");
        displayedProfilePic = getArguments().getString("displayprofilepic");
        errorMsg=0;

        //parse String to the JSON object
        try {
            //get the posts data
            detailData = new JSONObject(detailResults);
            postResults = detailData.getJSONObject("posts");
            postsArray = postResults.getJSONArray("data");

            for (int i=0;i<postsArray.length();i++){
                JSONObject postnow = postsArray.getJSONObject(i);
                String createdTime = postnow.getString("created_time");
                SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date createdDate = createdDateFormat.parse(createdTime);
                createdDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String formattedDate = createdDateFormat.format(createdDate);
                String message = postnow.getString("message");
                //postsArrayList.add(new postsDataObject(createdTime,message));
                postsArrayList.add(new postsDataObject(formattedDate,message));
            }

        } catch (JSONException e) {
            errorMsg=1;
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabposts, container, false);

        if (errorMsg == 1){
            //display error message here
            RelativeLayout myPostsLayout = (RelativeLayout)rootView.findViewById(R.id.postsLayout);
            TextView errorText = new TextView(getContext());
            errorText.setText("No posts available to display");
            myPostsLayout.addView(errorText);
        }
        else{
            //display posts
            RelativeLayout myPostsLayout = (RelativeLayout)rootView.findViewById(R.id.postsLayout);

            //display in a List View
            ListView postList = new ListView(getContext());

            MyPostsAdapter postsViewAdapter = new MyPostsAdapter(getActivity(), R.layout.postsrow, postsArrayList, displayedProfileName,displayedProfilePic);
            postList.setAdapter(postsViewAdapter);

            myPostsLayout.addView(postList);
        }

        return rootView;
    }
}
