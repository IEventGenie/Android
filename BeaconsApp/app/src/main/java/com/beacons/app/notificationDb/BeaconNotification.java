package com.beacons.app.notificationDb;

import java.util.Date;

/**
 * Created by aman on 4/9/16.
 */

public class BeaconNotification {

    //private variables
    String _id = "";
    String _title = "";
    String _type = "";
    String _date = "";

    // Empty constructor
    public BeaconNotification(){

    }
    // constructor
    public BeaconNotification(String title, String type){
        this._title = title;
        this._type = type;
    }

    // getting name
    public String getTitle(){
        return this._title;
    }

    // setting name
    public void setTitle(String title){
        this._title = title;
    }

    // getting phone number
    public String getType(){
        return this._type;
    }

    // setting phone number
    public void setType(String type){
        this._type = type;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public void setDate(String date){
        _date = date;
    }

    public String getDate(){
        return _date;
    }

}