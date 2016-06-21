package com.beacons.app.beaconsapp;

import android.app.Application;
import android.support.v4.app.FragmentActivity;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.beacons.BeaconsDetector;
import com.beacons.app.notificationDb.EventDetailDBModel;

/**
 * Created by aman on 2/27/16.
 */
public class Globals extends Application {

    public boolean DoLogout = false;

    //Main Event Details after Code Entry==
    EventDetailMainModel eventDetailMainModel = new EventDetailMainModel();
    public EventDetailMainModel getEventDetailMainModel() {
        return eventDetailMainModel;
    }
    public void setEventDetailMainModel(EventDetailMainModel eventDetailMainModel) {
        this.eventDetailMainModel = eventDetailMainModel;
    }

    EventDetailDBModel eventDetailDbModel = new EventDetailDBModel();
    public EventDetailDBModel getEventDetailDBModel() {
        return eventDetailDbModel;
    }
    public void setEventDetailDBModel(EventDetailDBModel eventDetaildbModel) {
        this.eventDetailDbModel = eventDetaildbModel;
    }

    String EventDetailJson = "";
    public void setEventDetailJson(String json){
        EventDetailJson = json;
    }

    public String getEventDetailJson(){
        return EventDetailJson;
    }
    //===========

    //Beacons Work==
    public FragmentActivity fragActivity;
    public FragmentActivity getFragActivity() {
        return fragActivity;
    }
    public void setFragActivity(FragmentActivity fragActivity) {
        this.fragActivity = fragActivity;
        BeaconsDetector.getInstance().startSetup(this);
    }
    public void fragActivityResumed(Class classObj,FragmentActivity act){
        BeaconsDetector.getInstance().activityResumed(classObj,act);
    }
    public void fragActivityPaused(){
        BeaconsDetector.getInstance().activityPaused();
    }
    public void fragActivityDestroyed(){
        BeaconsDetector.getInstance().activityDestroyed();
    }
    //=============

}
