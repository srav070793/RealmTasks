package com.example.searchfromfb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

public class tabFavEvents extends android.support.v4.app.Fragment{

    ArrayList<FBobjectJSON> favEvents = new ArrayList<FBobjectJSON>();
    ListView myEventFavList;
    ArrayList<String> favEventsName=new ArrayList<String>();
    ArrayList<String> favEventsPics = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabfavevents, container, false);

        myEventFavList = (ListView)rootView.findViewById(R.id.eventFavList);

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
                int eventFound = 0;
                for (String s : set) {
                    if(s.startsWith("http")){
                        profilepic = s;
                    }
                    else if(s.equals("user")||s.equals("page")||s.equals("event")||s.equals("group")||s.equals("place")){
                        type = s;
                        if (type.equals("event")){
                            eventFound = 1;
                        }
                    }
                    else{
                        name = s;
                    }
                }
                if(eventFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favEvents.add(newFav);
                }
            }

        }
        for(int i=0;i<favEvents.size();i++){
            FBobjectJSON fav = favEvents.get(i);
            favEventsName.add(fav.name);
            favEventsPics.add(fav.profilepic);
        }

        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favEvents,favEventsName,favEventsPics);
        myEventFavList.setAdapter(myFavoritesAdapter);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        favEvents.clear();
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
                int eventFound = 0;
                for (String s : set) {
                        eventFound =0;
                        if (s.startsWith("http")) {
                            profilepic = s;
                        } else if (s.equals("user") || s.equals("page") || s.equals("event") || s.equals("group") || s.equals("place")) {
                            type = s;
                            if (type.equals("event")){
                                eventFound = 1;
                            }
                        } else {
                            name = s;
                        }
                }

                if(eventFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favEvents.add(newFav);
                }
            }
        }
        for(int i=0;i<favEvents.size();i++){
            FBobjectJSON fav = favEvents.get(i);
            favEventsName.add(fav.name);
            favEventsPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favEvents,favEventsName,favEventsPics);
        myEventFavList.setAdapter(myFavoritesAdapter);

    }
}

