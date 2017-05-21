package com.example.searchfromfb;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

/**
 * Created by sravanianne on 4/16/17.
 */

public class tabUsersFragment extends android.support.v4.app.Fragment{

    String userResults;
    ArrayList<FBobjectJSON> userArrayList = new ArrayList<FBobjectJSON>();
    JSONArray usersData;
    JSONObject pagination;
    int nextPosition;
    int prevPosition;
    ArrayList<FBobjectJSON> myUserDisplay = new ArrayList<FBobjectJSON>();
    ListView myUserList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userResults = getArguments().getString("users");
        nextPosition =0;
        prevPosition = -10;

        try{
            JSONObject resultsJSON = new JSONObject(userResults);
            usersData = resultsJSON.getJSONArray("data");
            pagination = resultsJSON.getJSONObject("paging");


            for (int i=0;i<usersData.length();i++){
                JSONObject usernow = usersData.getJSONObject(i);
                String id = usernow.getString("id");
                String name = usernow.getString("name");
                String profilepic = usernow.getJSONObject("picture").getJSONObject("data").getString("url");

                userArrayList.add(new FBobjectJSON(id,name,profilepic,"user"));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        //Log.d("TAG", "OnResume");
        MyCustomAdapter myUsersAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myUserDisplay);
        myUserList.setAdapter(myUsersAdapter);

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
        View rootView = inflater.inflate(R.layout.tabusers, container, false);

        myUserList = (ListView)rootView.findViewById(R.id.userList);
        //final ListView myUserList = (ListView)rootView.findViewById(R.id.userList);
        final Button nextBtn = (Button)rootView.findViewById(R.id.nextBtn);
        final Button prevBtn = (Button)rootView.findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        if (usersData.length()<=10){
            nextBtn.setEnabled(false);
        }

        //--------test with dummy--------------//
        /*String[] menuItems =  {"do something", "do yet another", "leave it"};
        ArrayAdapter<String> myUserAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,menuItems);
        myUserList.setAdapter(myUserAdapter);*/
        //---------test with dummy-------------//

        //ArrayList<FBobjectJSON> myUserDisplay = new ArrayList<FBobjectJSON>();
        for (int i=0;i<10;i++){
            myUserDisplay.add(userArrayList.get(i));
        }
        setNextPosition(10);
        //render the user results here using the userResults variable
        //MyCustomAdapter myUsersAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, userArrayList);

        MyCustomAdapter myUsersAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myUserDisplay);
        myUserList.setAdapter(myUsersAdapter);

        //add event Listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dispPosition = getNextPosition();
                setPrevPosition(dispPosition-1);

                ArrayList<FBobjectJSON> nextToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition<userArrayList.size()){
                    int i,j=0;
                    for (i=dispPosition;j<Math.min(10,(userArrayList.size()-dispPosition));j++,i++){
                        nextToDisplay.add(userArrayList.get(i));
                    }
                    MyCustomAdapter myNextAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, nextToDisplay);
                    myUserList.setAdapter(myNextAdapter);
                    setNextPosition(dispPosition+j);
                    setPrevPosition(dispPosition);
                }
                //setting the next and previous buttons
                if(dispPosition+10>=userArrayList.size()){
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
                        prevToDisplay.add(userArrayList.get(i));
                    }
                    Collections.reverse(prevToDisplay);
                    MyCustomAdapter myPrevAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, prevToDisplay);
                    myUserList.setAdapter(myPrevAdapter);
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
