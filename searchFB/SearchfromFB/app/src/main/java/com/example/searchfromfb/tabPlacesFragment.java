package com.example.searchfromfb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sravanianne on 4/16/17.
 */

public class tabPlacesFragment extends android.support.v4.app.Fragment{

    String placeResults;
    ArrayList<FBobjectJSON> placeArrayList = new ArrayList<FBobjectJSON>();
    JSONArray placesData;
    int nextPosition;
    int prevPosition;
    ArrayList<FBobjectJSON> myPlaceDisplay = new ArrayList<FBobjectJSON>();
    ListView myPlaceList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        placeResults = getArguments().getString("places");
        nextPosition =0;
        prevPosition = -10;

        try{
            JSONObject resultsJSON = new JSONObject(placeResults);
            placesData = resultsJSON.getJSONArray("data");

            for (int i=0;i<placesData.length();i++){
                JSONObject placenow = placesData.getJSONObject(i);
                String id = placenow.getString("id");
                String name = placenow.getString("name");
                String profilepic = placenow.getJSONObject("picture").getJSONObject("data").getString("url");

                placeArrayList.add(new FBobjectJSON(id,name,profilepic,"place"));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        //Log.d("TAG", "OnResume");
        MyCustomAdapter myPlacesAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myPlaceDisplay);
        myPlaceList.setAdapter(myPlacesAdapter);

    }

    public void setNextPosition(int num){
        nextPosition=num;
    }
    public int getNextPosition(){
        return nextPosition;
    }
    public void setPrevPosition(int num){
        prevPosition=num;
    }
    public int getPrevPosition(){
        return prevPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabplaces, container, false);

        myPlaceList = (ListView)rootView.findViewById(R.id.placeList);

        final Button nextBtn = (Button)rootView.findViewById(R.id.nextBtn);
        final Button prevBtn = (Button)rootView.findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        if (placesData.length()<=10){
            nextBtn.setEnabled(false);
        }

        for (int i=0;i<10;i++){
            myPlaceDisplay.add(placeArrayList.get(i));
        }
        setNextPosition(10);


        MyCustomAdapter myPlacesAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myPlaceDisplay);
        myPlaceList.setAdapter(myPlacesAdapter);

        //add event Listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dispPosition = getNextPosition();
                setPrevPosition(dispPosition-1);

                ArrayList<FBobjectJSON> nextToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition<placeArrayList.size()){
                    int i,j=0;
                    for (i=dispPosition;j<Math.min(10,(placeArrayList.size()-dispPosition));j++,i++){
                        nextToDisplay.add(placeArrayList.get(i));
                    }
                    MyCustomAdapter myNextAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, nextToDisplay);
                    myPlaceList.setAdapter(myNextAdapter);
                    setNextPosition(dispPosition+j);
                    setPrevPosition(dispPosition);
                }
                //setting the next and previous buttons
                if(dispPosition+10>=placeArrayList.size()){
                    nextBtn.setEnabled(false);
                }
                prevBtn.setEnabled(true);
            }
        });

        //add event Listener for prev button
        prevBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int dispPosition = getPrevPosition();

                ArrayList<FBobjectJSON> prevToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition-1>0){
                    int i,j=0;
                    for (i=dispPosition-1;j<10;j++,i--){
                        prevToDisplay.add(placeArrayList.get(i));
                    }
                    Collections.reverse(prevToDisplay);
                    MyCustomAdapter myPrevAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, prevToDisplay);
                    myPlaceList.setAdapter(myPrevAdapter);
                    setPrevPosition(dispPosition-j);
                    setNextPosition(dispPosition);
                }
                //setting the next and previous buttons
                if (dispPosition-10<=0){
                    prevBtn.setEnabled(false);
                }
                nextBtn.setEnabled(true);
            }
        });

        return rootView;
    }
}
