package com.example.searchfromfb;

import java.util.ArrayList;

/**
 * Created by sravanianne on 4/21/17.
 */

public class AlbumsObject {
    String name;
    ArrayList<String> pics= new ArrayList<String>();
    public AlbumsObject(String name, String pic1, String pic2){
        this.name=name;
        this.pics.add(pic1);
        this.pics.add(pic2);
    }
}
