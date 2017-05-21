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

public class tabGroupsFragment  extends android.support.v4.app.Fragment{

    String groupResults;
    ArrayList<FBobjectJSON> groupArrayList = new ArrayList<FBobjectJSON>();
    JSONArray groupsData;
    int nextPosition;
    int prevPosition;
    ArrayList<FBobjectJSON> myGroupDisplay = new ArrayList<FBobjectJSON>();
    ListView myGroupList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        groupResults = getArguments().getString("groups");
        nextPosition =0;
        prevPosition = -10;

        try{
            JSONObject resultsJSON = new JSONObject(groupResults);
            groupsData = resultsJSON.getJSONArray("data");

            for (int i=0;i<groupsData.length();i++){
                JSONObject groupnow = groupsData.getJSONObject(i);
                String id = groupnow.getString("id");
                String name = groupnow.getString("name");
                String profilepic = groupnow.getJSONObject("picture").getJSONObject("data").getString("url");

                groupArrayList.add(new FBobjectJSON(id,name,profilepic,"group"));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        super.onResume();

        MyCustomAdapter myGroupsAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myGroupDisplay);
        myGroupList.setAdapter(myGroupsAdapter);

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
        View rootView = inflater.inflate(R.layout.tabgroups, container, false);

        myGroupList = (ListView)rootView.findViewById(R.id.groupList);

        final Button nextBtn = (Button)rootView.findViewById(R.id.nextBtn);
        final Button prevBtn = (Button)rootView.findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        if (groupsData.length()<=10){
            nextBtn.setEnabled(false);
        }

        for (int i=0;i<10;i++){
            myGroupDisplay.add(groupArrayList.get(i));
        }
        setNextPosition(10);

        //render the results here
        MyCustomAdapter myGroupsAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myGroupDisplay);
        myGroupList.setAdapter(myGroupsAdapter);

        //add event Listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dispPosition = getNextPosition();
                setPrevPosition(dispPosition-1);

                ArrayList<FBobjectJSON> nextToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition<groupArrayList.size()){
                    int i,j=0;
                    for (i=dispPosition;j<Math.min(10,(groupArrayList.size()-dispPosition));j++,i++){
                        nextToDisplay.add(groupArrayList.get(i));
                    }
                    MyCustomAdapter myNextAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, nextToDisplay);
                    myGroupList.setAdapter(myNextAdapter);
                    setNextPosition(dispPosition+j);
                    setPrevPosition(dispPosition);
                }
                //setting the next and previous buttons
                if(dispPosition+10>=groupArrayList.size()){
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
                        prevToDisplay.add(groupArrayList.get(i));
                    }
                    Collections.reverse(prevToDisplay);
                    MyCustomAdapter myPrevAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, prevToDisplay);
                    myGroupList.setAdapter(myPrevAdapter);
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
