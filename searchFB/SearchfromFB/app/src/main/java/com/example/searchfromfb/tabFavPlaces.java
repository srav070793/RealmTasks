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

public class tabFavPlaces extends android.support.v4.app.Fragment{
    ArrayList<FBobjectJSON> favPlaces = new ArrayList<FBobjectJSON>();
    ListView myPlaceFavList;
    ArrayList<String> favPlacesName=new ArrayList<String>();
    ArrayList<String> favPlacesPics = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabfavplaces, container, false);

        myPlaceFavList = (ListView)rootView.findViewById(R.id.placeFavList);

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
                int placeFound = 0;
                for (String s : set) {
                    if(s.startsWith("http")){
                        profilepic = s;
                    }
                    else if(s.equals("user")||s.equals("page")||s.equals("event")||s.equals("group")||s.equals("place")){
                        type = s;
                        if (type.equals("place")){
                            placeFound = 1;
                        }
                    }
                    else{
                        name = s;
                    }
                }
                if(placeFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favPlaces.add(newFav);
                }
            }
        }
        for(int i=0;i<favPlaces.size();i++){
            FBobjectJSON fav = favPlaces.get(i);
            favPlacesName.add(fav.name);
            favPlacesPics.add(fav.profilepic);
        }

        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favPlaces,favPlacesName,favPlacesPics);
        myPlaceFavList.setAdapter(myFavoritesAdapter);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        favPlaces.clear();
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
                int placeFound = 0;
                for (String s : set) {
                    if (s.equals("place")) {
                        if (s.startsWith("http")) {
                            profilepic = s;
                        } else if (s.equals("user") || s.equals("page") || s.equals("event") || s.equals("group") || s.equals("place")) {
                            type = s;
                            if (type.equals("place")){
                                placeFound = 1;
                            }
                        } else {
                            name = s;
                        }
                    }
                }
                if(placeFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favPlaces.add(newFav);
                }
            }
        }
        for(int i=0;i<favPlaces.size();i++){
            FBobjectJSON fav = favPlaces.get(i);
            favPlacesName.add(fav.name);
            favPlacesPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favPlaces,favPlacesName,favPlacesPics);
        myPlaceFavList.setAdapter(myFavoritesAdapter);

    }
}
