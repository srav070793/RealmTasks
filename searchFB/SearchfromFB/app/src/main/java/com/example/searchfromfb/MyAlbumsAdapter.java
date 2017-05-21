package com.example.searchfromfb;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by sravanianne on 4/21/17.
 */

public class MyAlbumsAdapter extends BaseExpandableListAdapter{
    Context context;
    int resource;
    ArrayList<String> titleGroups;
    ArrayList<String> photosUrls;
    ArrayList<AlbumsObject> albumphotos;

    public MyAlbumsAdapter(Context context, int resource, ArrayList<String> titles, ArrayList<String> photosUrls, ArrayList<AlbumsObject>albumphotos){

        this.titleGroups = titles;
        this.context=context;
        this.resource=resource;
        this.photosUrls=photosUrls;
        this.albumphotos=albumphotos;
    }
    @Override
    public int getGroupCount() {
        return albumphotos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return albumphotos.get(groupPosition).pics.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return albumphotos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return albumphotos.get(groupPosition).pics.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.albumsrow,null,true);

            CheckedTextView checkedTitles = (CheckedTextView)convertView.findViewById(R.id.checkedTitles);
            AlbumsObject myAlbum = (AlbumsObject)getGroup(groupPosition);

            checkedTitles.setText(myAlbum.name);
            checkedTitles.setChecked(isExpanded);


        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.albumschildrow,null,true);

        //get the references
        ImageView pic = (ImageView)convertView.findViewById(R.id.pic);

        //load the images using picasso appropriately
        Picasso.with(this.context).load((String)getChild(groupPosition,childPosition)).into((ImageView)pic);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
