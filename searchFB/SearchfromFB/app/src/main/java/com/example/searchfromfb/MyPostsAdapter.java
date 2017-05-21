package com.example.searchfromfb;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sravanianne on 4/21/17.
 */

public class MyPostsAdapter extends ArrayAdapter<postsDataObject> {
    Context context;
    int resource;
    ArrayList<postsDataObject> postsResults;
    String profilename;
    String pic;

    public MyPostsAdapter(Context context, int resource,ArrayList<postsDataObject> postsResults, String profilename, String pic){
        super(context,resource, postsResults);
        this.context = context;
        this.resource = resource;
        this.postsResults = postsResults;
        this.profilename = profilename;
        this.pic = pic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.postsrow,null,true);

        }
        //get the reference to set
        ImageView profilePic = (ImageView)convertView.findViewById(R.id.postProfilePic);
        postsDataObject postObject = postsResults.get(position);
        TextView profileName = (TextView)convertView.findViewById(R.id.PostProfileName);
        TextView createdTime = (TextView)convertView.findViewById(R.id.PostCreatedTime);
        TextView postText = (TextView)convertView.findViewById(R.id.PostText);

        //set the references now
        Picasso.with(this.getContext()).load(pic).into((ImageView)profilePic);
        profileName.setText(profilename);
        createdTime.setText(postObject.createdTime);
        postText.setText(postObject.postsText);

        return convertView;
    }
}
