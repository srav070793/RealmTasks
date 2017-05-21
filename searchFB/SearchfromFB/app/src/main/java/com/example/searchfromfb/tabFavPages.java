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

public class tabFavPages extends android.support.v4.app.Fragment{

    ArrayList<FBobjectJSON> favPages = new ArrayList<FBobjectJSON>();
    ListView myPageFavList;
    ArrayList<String> favPagesName=new ArrayList<String>();
    ArrayList<String> favPagesPics = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabfavpages, container, false);

        myPageFavList = (ListView)rootView.findViewById(R.id.pageFavList);

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
                int pageFound = 0;
                for (String s : set) {
                    if(s.startsWith("http")){
                        profilepic = s;

                    }
                    else if(s.equals("user")||s.equals("page")||s.equals("event")||s.equals("group")||s.equals("place")){
                        type = s;
                        if (type.equals("page")){
                            pageFound = 1;
                        }
                    }
                    else{
                        name = s;
                    }
                }
                if(pageFound == 1) {
                    //FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favPages.add(new FBobjectJSON(keyObtained,name,profilepic,type));
                }
            }

        }

        for(int i=0;i<favPages.size();i++){
            FBobjectJSON fav = favPages.get(i);
            favPagesName.add(fav.name);
            favPagesPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favPages,favPagesName,favPagesPics);
        myPageFavList.setAdapter(myFavoritesAdapter);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        favPages.clear();
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
                int pageFound = 0;
                for (String s : set) {
                    if (s.equals("page")) {
                        if (s.startsWith("http")) {
                            profilepic = s;

                        } else if (s.equals("user") || s.equals("page") || s.equals("event") || s.equals("group") || s.equals("place")) {
                            type = s;
                            if (type.equals("page")){
                                pageFound = 1;
                            }
                        } else {
                            name = s;
                        }
                    }
                }
                if(pageFound == 1) {
                    FBobjectJSON newFav = new FBobjectJSON(keyObtained, name, profilepic, type);
                    favPages.add(newFav);
                }
            }
        }
        for(int i=0;i<favPages.size();i++){
            FBobjectJSON fav = favPages.get(i);
            favPagesName.add(fav.name);
            favPagesPics.add(fav.profilepic);
        }
        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(getActivity(), R.layout.resultrow,R.id.profilename,favPages,favPagesName,favPagesPics);
        myPageFavList.setAdapter(myFavoritesAdapter);

    }
}
