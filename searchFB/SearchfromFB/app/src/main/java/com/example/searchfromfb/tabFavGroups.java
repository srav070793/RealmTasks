package com.example.searchfromfb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sravanianne on 4/26/17.
 */

public class tabFavGroups extends android.support.v4.app.Fragment{
    ArrayList<FBobjectJSON> favGroups = new ArrayList<FBobjectJSON>();
    ListView myGroupFavList;
    ArrayList<String> favGroupsName=new ArrayList<String>();
    ArrayList<String> favGroupsPics = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabfavgroups, container, false);

        myGroupFavList = (ListView)rootView.findViewById(R.id.groupFavList);

        SharedPreferences favList = PreferenceManager.getDefaultSharedPreferences(getContext());
        Map<String,?> keys = favList.getAll();

        Set entrySet = keys.entrySet();
        Iterator it = entrySet.iterator();


        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String keyObtained = (String)pair.getKey();
            String name =null, profilepic=null, type=null;

            if (keyObtained.matches("[0-9]+")) {
                Set<String> set = (Set<String>)pair.getValue();
                int groupFound = 0;
                for (String s : set) {
                    if(s.startsWith("http")){
                        profilepic = s;
                    }
                    else if(s.equals("user")||s.equals("page")||s.equals("event")||s.equals("group")||s.equals("place")){
                        type = s;
                        if(type.equals("group")){
                            groupFound = 1;
                        }
                    }
                    else{
                        name = s;
                    }
                }

                if(groupFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favGroups.add(newFav);
                }
            }

        }
        for(int i=0;i<favGroups.size();i++){
            FBobjectJSON fav = favGroups.get(i);
            favGroupsName.add(fav.name);
            favGroupsPics.add(fav.profilepic);
        }

        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favGroups,favGroupsName,favGroupsPics);
        myGroupFavList.setAdapter(myFavoritesAdapter);

        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();

        favGroups.clear();
        SharedPreferences favList = PreferenceManager.getDefaultSharedPreferences(getContext());
        Map<String,?> keys = favList.getAll();

        Set entrySet = keys.entrySet();
        Iterator it = entrySet.iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            String keyObtained = (String) pair.getKey();
            String name = null, profilepic = null, type = null;

            if (keyObtained.matches("[0-9]+")) {
                Set<String> set = (Set<String>) pair.getValue();
                int groupFound = 0;
                for (String s : set) {
                    if (s.equals("group")) {
                        if (s.startsWith("http")) {
                            profilepic = s;
                        } else if (s.equals("user") || s.equals("page") || s.equals("event") || s.equals("group") || s.equals("place")) {
                            type = s;
                            if(type.equals("group")){
                                groupFound = 1;
                            }
                        } else {
                            name = s;
                        }
                    }
                }

                if(groupFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favGroups.add(newFav);
                }
            }
        }
        for(int i=0;i<favGroups.size();i++){
            FBobjectJSON fav = favGroups.get(i);
            favGroupsName.add(fav.name);
            favGroupsPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favGroups,favGroupsName,favGroupsPics);
        myGroupFavList.setAdapter(myFavoritesAdapter);

    }
}
