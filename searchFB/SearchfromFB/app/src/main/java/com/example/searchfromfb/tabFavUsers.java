package com.example.searchfromfb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
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

public class tabFavUsers extends android.support.v4.app.Fragment {
    ArrayList<FBobjectJSON> favUsers = new ArrayList<FBobjectJSON>();
    ListView myUserFavList;
    ArrayList<String> favUsersName=new ArrayList<String>();
    ArrayList<String> favUsersPics = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tabfavusers, container, false);

        myUserFavList = (ListView)rootView.findViewById(R.id.userFavList);

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
                int userFound = 0;
                for (String s : set) {
                    if(s.startsWith("http")){
                        profilepic = s;
                    }
                    else if(s.equals("user")||s.equals("page")||s.equals("event")||s.equals("group")||s.equals("place")){
                        type = s;
                        if(type.equals("user")) {
                            userFound = 1;
                        }
                    }
                    else{
                        name = s;
                    }
                }
                if (userFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favUsers.add(newFav);
                }
            }

        }
        for(int i=0;i<favUsers.size();i++){
            FBobjectJSON fav = favUsers.get(i);
            favUsersName.add(fav.name);
            favUsersPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favUsers,favUsersName,favUsersPics);
        myUserFavList.setAdapter(myFavoritesAdapter);

        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();

        favUsers.clear();
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
                int userFound = 0;
                for (String s : set) {
                    if (s.equals("user")) {
                        if (s.startsWith("http")) {
                            profilepic = s;
                        } else if (s.equals("user") || s.equals("page") || s.equals("event") || s.equals("group") || s.equals("place")) {
                            type = s;
                            if(type.equals("user")) {
                                userFound = 1;
                            }
                        } else {
                            name = s;
                        }

                        if (userFound == 1) {
                            FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                            favUsers.add(newFav);
                        }
                    }
                }
            }
        }
        for(int i=0;i<favUsers.size();i++){
            FBobjectJSON fav = favUsers.get(i);
            favUsersName.add(fav.name);
            favUsersPics.add(fav.profilepic);
        }

        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favUsers,favUsersName,favUsersPics);
        myUserFavList.setAdapter(myFavoritesAdapter);

    }
}
