package com.example.searchfromfb;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Created by sravanianne on 4/16/17.
 */

public class tabPagesFragment extends android.support.v4.app.Fragment{

    String pageResults;
    ArrayList<FBobjectJSON> pageArrayList = new ArrayList<FBobjectJSON>();
    JSONArray pagesData;
    int nextPosition;
    int prevPosition;
    ArrayList<FBobjectJSON> myPageDisplay = new ArrayList<FBobjectJSON>();
    ListView myPagesList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        pageResults = getArguments().getString("pages");
        nextPosition =0;
        prevPosition = -10;

        try{
            JSONObject resultsJSON = new JSONObject(pageResults);
            pagesData = resultsJSON.getJSONArray("data");

            for (int i=0;i<pagesData.length();i++){
                JSONObject pagenow = pagesData.getJSONObject(i);
                String id = pagenow.getString("id");
                String name = pagenow.getString("name");
                String profilepic = pagenow.getJSONObject("picture").getJSONObject("data").getString("url");

                pageArrayList.add(new FBobjectJSON(id,name,profilepic,"page"));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //Log.d("TAG", "OnResume");
        MyCustomAdapter myUsersAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myPageDisplay);
        myPagesList.setAdapter(myUsersAdapter);

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
        View rootView = inflater.inflate(R.layout.tabpages, container, false);

        myPagesList = (ListView)rootView.findViewById(R.id.pagesList);
        final Button nextBtn = (Button)rootView.findViewById(R.id.nextBtn);
        final Button prevBtn = (Button)rootView.findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        if (pagesData.length()<=10){
            nextBtn.setEnabled(false);
        }
        //--------test with dummy--------------//
        /*String[] menuItems =  {"do pages", "do yet pages", "leave pages"};
        ArrayAdapter<String> myPagesAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,menuItems);
        myPagesList.setAdapter(myPagesAdapter);*/
        //---------test with dummy-------------//

        for (int i=0;i<10;i++){
            myPageDisplay.add(pageArrayList.get(i));
        }
        setNextPosition(10);

        //render the pages results here using the pageResults variable
        MyCustomAdapter myPagesAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, myPageDisplay);
        myPagesList.setAdapter(myPagesAdapter);

        //add event Listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dispPosition = getNextPosition();
                setPrevPosition(dispPosition-1);

                ArrayList<FBobjectJSON> nextToDisplay = new ArrayList<FBobjectJSON>();
                if (dispPosition<pageArrayList.size()){
                    int i,j=0;
                    for (i=dispPosition;j<Math.min(10,(pageArrayList.size()-dispPosition));j++,i++){
                        nextToDisplay.add(pageArrayList.get(i));
                    }
                    MyCustomAdapter myNextAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, nextToDisplay);
                    myPagesList.setAdapter(myNextAdapter);
                    setNextPosition(dispPosition+j);
                    setPrevPosition(dispPosition);
                }
                //setting the next and previous buttons
                if(dispPosition+10>=pageArrayList.size()){
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
                        prevToDisplay.add(pageArrayList.get(i));
                    }
                    Collections.reverse(prevToDisplay);
                    MyCustomAdapter myPrevAdapter = new MyCustomAdapter(getActivity(), R.layout.resultrow, R.id.profilename, prevToDisplay);
                    myPagesList.setAdapter(myPrevAdapter);
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
