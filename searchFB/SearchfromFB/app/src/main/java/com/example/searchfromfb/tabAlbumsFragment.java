package com.example.searchfromfb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by sravanianne on 4/21/17.
 */

public class tabAlbumsFragment extends android.support.v4.app.Fragment {
    JSONObject albumResults;
    String detailResults;
    JSONObject detailData;
    int errorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        detailResults = getArguments().getString("details");
        detailResults = getArguments().getString("details");
        errorMsg=0;

        //parse String to the JSON object
        try {
            detailData = new JSONObject(detailResults);
            albumResults = detailData.getJSONObject("albums");
        } catch (JSONException e) {

            errorMsg=1;
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabalbums, container, false);
        if (errorMsg == 1){
            RelativeLayout myAlbumsLayout = (RelativeLayout)rootView.findViewById(R.id.albumsLayout);
            TextView errorText = new TextView(getContext());
            errorText.setText("No albums available to display");
            myAlbumsLayout.addView(errorText);
        }
        else{
            //display expandable List here
            RelativeLayout myAlbumsLayout = (RelativeLayout)rootView.findViewById(R.id.albumsLayout);

            JSONArray albumsTitles;

            ArrayList<String> titles = new ArrayList<String>();
            ArrayList<String> photosUrls = new ArrayList<String>();
            ArrayList<AlbumsObject> albumphotos = new ArrayList<AlbumsObject>();

            try {
                albumsTitles = albumResults.getJSONArray("data");

                for (int i=0;i<albumsTitles.length();i++){
                    JSONObject albumNow = albumsTitles.getJSONObject(i);
                    String name = albumNow.getString("name");
                    String pic1,pic2,id1,id2;
                    JSONArray albumNowData=albumNow.getJSONObject("photos").getJSONArray("data");

                    id1 = albumNowData.getJSONObject(0).getString("id");
                    id2 = albumNowData.getJSONObject(1).getString("id");

                    //make a http get request and send the url to the object
                    HttpGetRequest getPhoto1 = new HttpGetRequest();
                    HttpGetRequest getPhoto2 = new HttpGetRequest();
                    String url1 = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+id1+"&searchType=albumphotos";
                    String url2 = "http://app12016spr-env.us-west-2.elasticbeanstalk.com/server.php?query="+id2+"&searchType=albumphotos";
                    pic1=getPhoto1.execute(url1).get();
                    pic2=getPhoto2.execute(url2).get();
                    JSONObject picJSON1 = new JSONObject(pic1);
                    JSONObject picJSON2 = new JSONObject(pic2);
                    pic1 = picJSON1.getJSONObject("data").getString("url");
                    pic2 = picJSON2.getJSONObject("data").getString("url");

                    titles.add(name);
                    photosUrls.add(pic1);
                    photosUrls.add(pic2);
                    albumphotos.add(new AlbumsObject(name,pic1,pic2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            final ExpandableListView albumsList = (ExpandableListView)new ExpandableListView(getContext());
            albumsList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            MyAlbumsAdapter myAlbumsAdapter = new MyAlbumsAdapter(getActivity(),R.layout.albumsrow,titles,photosUrls,albumphotos);
            albumsList.setAdapter(myAlbumsAdapter);


            //exclusive expansion of group items
            final int[] lastExpandedPosition = {-1};

            albumsList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition[0] != -1
                            && groupPosition != lastExpandedPosition[0]) {
                        albumsList.collapseGroup(lastExpandedPosition[0]);
                    }
                    lastExpandedPosition[0] = groupPosition;
                }
            });
            myAlbumsLayout.addView(albumsList);
        }

        return rootView;
    }
}
