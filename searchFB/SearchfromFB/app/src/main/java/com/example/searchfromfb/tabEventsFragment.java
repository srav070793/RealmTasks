package com.example.searchfromfb;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sravanianne on 4/16/17.
 */

public class tabEventsFragment extends android.support.v4.app.Fragment{
    String eventResults;
    ArrayList<FBobjectJSON> eventArrayList = new ArrayList<FBobjectJSON>();
    JSONArray eventsData;

    int nextPosition;
    int prevPosition;
    ArrayList<FBobjectJSON> myEventDisplay = new ArrayList<FBobjectJSON>();
    ListView myEventList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        eventResults = getArguments().getString("events");
        nextPosition =0;
        prevPosition = -10;

        try{
            JSONObject resultsJSON = new JSONObject(eventResults);
            eventsData = resultsJSON.getJSONArray("data");


            for (int i=0;i<eventsData.length();i++){
                JSONObject eventnow = eventsData.getJSONObject(i);
                String id = eventnow.getString("id");
                String name = eventnow.getString("name");
                String profilepic = eventnow.getJSONObject("picture").getJSONObject("data").getString("url");

                eventArrayList.add(new FBobjectJSON(id,name,profilepic,"event"));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){

        super.onResume();

        MyCustomAdapter myEventsAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myEventDisplay);
        myEventList.setAdapter(myEventsAdapter);

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
        View rootView = inflater.inflate(R.layout.tabevents, container, false);

        myEventList = (ListView)rootView.findViewById(R.id.eventList);

        final Button nextBtn = (Button)rootView.findViewById(R.id.nextBtn);
        final Button prevBtn = (Button)rootView.findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        if (eventsData.length()<=10){
            nextBtn.setEnabled(false);
        }


        for (int i=0;i<10;i++){
            myEventDisplay.add(eventArrayList.get(i));
        }
        setNextPosition(10);
        //render the events results here using the eventResults variable

        MyCustomAdapter myEventsAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myEventDisplay);
        myEventList.setAdapter(myEventsAdapter);

        //add event Listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dispPosition = getNextPosition();
                setPrevPosition(dispPosition-1);

                ArrayList<FBobjectJSON> nextToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition<eventArrayList.size()){
                    int i,j=0;
                    for (i=dispPosition;j<Math.min(10,(eventArrayList.size()-dispPosition));j++,i++){
                        nextToDisplay.add(eventArrayList.get(i));
                    }
                    MyCustomAdapter myNextAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, nextToDisplay);
                    myEventList.setAdapter(myNextAdapter);
                    setNextPosition(dispPosition+j);
                    setPrevPosition(dispPosition);
                }
                //setting the next and previous buttons
                if(dispPosition+10>=eventArrayList.size()){
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
                        prevToDisplay.add(eventArrayList.get(i));
                    }
                    Collections.reverse(prevToDisplay);
                    MyCustomAdapter myPrevAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, prevToDisplay);
                    myEventList.setAdapter(myPrevAdapter);
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
