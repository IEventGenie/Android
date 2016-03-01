package com.beacons.app.beaconsapp;

import android.app.Application;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;

/**
 * Created by aman on 2/27/16.
 */
public class Globals extends Application {

    //Main Event Details after Code Entry==
    EventDetailMainModel eventDetailMainModel = new EventDetailMainModel();
    public EventDetailMainModel getEventDetailMainModel() {
        return eventDetailMainModel;
    }

    public void setEventDetailMainModel(EventDetailMainModel eventDetailMainModel) {
        this.eventDetailMainModel = eventDetailMainModel;
    }
    //===========





}
