package com.example.searchfromfb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by sravanianne on 4/26/17.
 */

public class MyFavoritesAdapter extends ArrayAdapter<FBobjectJSON> {
    Context context;
    ArrayList<FBobjectJSON> resultSet;
    ArrayList<String> favProfileNames;
    ArrayList<String> favProfilePics;
    int resource;
    int textViewId;

    public MyFavoritesAdapter(Context context, int resource, int textViewId, ArrayList<FBobjectJSON> resultSet, ArrayList<String> favProfileNames, ArrayList<String> favProfilePics){

        super(context, resource, textViewId,resultSet);

        this.context = context;
        this.resource = resource;
        this.resultSet = resultSet;
        this.textViewId = textViewId;

        this.favProfileNames = favProfileNames;
        this.favProfilePics = favProfilePics;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.resultrow,null,true);

        }

        //get the references that are to be set
        FBobjectJSON userToSet = resultSet.get(position);

        ImageView profilePic = (ImageView)convertView.findViewById(R.id.profilepic);
        TextView profileName = (TextView)convertView.findViewById(R.id.profilename);
        ImageView favImg = (ImageView)convertView.findViewById(R.id.favoriteImgBtn);

        //set the profilepics and profile name with the resultSet
        //Picasso.with(this.getContext()).load(userToSet.profilepic).into((ImageView)profilePic);
        profileName.setText(favProfileNames.get(position));
        Picasso.with(this.getContext()).load(favProfilePics.get(position)).into((ImageView)profilePic);
        favImg.setImageResource(R.mipmap.ic_favorites_on);

        //setup onclick Listener for details button
        ImageButton details = (ImageButton)convertView.findViewById(R.id.getDetailsBtn);
        details.setTag(position);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailsActivity.class);

                int position = (Integer)v.getTag();
                FBobjectJSON itemToPass = (FBobjectJSON)resultSet.get(position);
                String idToPass = itemToPass.id;
                String nameToDisplay = itemToPass.name;
                String profilePicToDisplay = itemToPass.profilepic;
                String profileType = itemToPass.type;


                //make http get request
                String urlDetails="http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+idToPass+"&searchType=details";
                HttpGetRequest getDetails = new HttpGetRequest();
                String resultsDetails = null;
                try {
                    resultsDetails = getDetails.execute(urlDetails).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //pass the data to details activity
                intent.putExtra("details", resultsDetails);
                intent.putExtra("displayprofilename", nameToDisplay);
                intent.putExtra("displayprofilepic", profilePicToDisplay);
                intent.putExtra("displaytype", profileType);
                intent.putExtra("displayid", idToPass);

                //now start the activity
                context.startActivity(intent);
            }
        });

        //setup onclick listener for favorites button
        return convertView;
    }
}
